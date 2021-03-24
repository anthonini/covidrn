package br.com.anthonini.covidrn.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.anthonini.covidrn.dto.PeriodoDTO;
import br.com.anthonini.covidrn.model.DadoDiario;
import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;
import br.com.anthonini.covidrn.service.exception.DadosJaAtualizadosException;

@Service
public class DadoDiarioService {

	public List<DadoDiario> extrairDados() throws IOException {	
		Path path = Paths.get(DadoDiarioParameters.LOCAL_ARQUIVO_DADOS);

		if(Files.exists(path)) {
			CsvMapper mapper = (CsvMapper) new CsvMapper().registerModule(new JavaTimeModule());
			CsvSchema schema = mapper.schemaFor(DadoDiario.class)
					.withColumnSeparator(DadoDiarioParameters.SEPARADOR_CSV.charAt(0));
			
			MappingIterator<DadoDiario> mappingIterator = mapper.readerFor(DadoDiario.class).with(schema).readValues(path.toFile());
			return mappingIterator.readAll();
		}
		
		return new ArrayList<>();
	}
	
	public void atualizarDados() throws IOException, ParseException {
		Document doc = Jsoup.connect(DadoDiarioParameters.URL_DADOS_COVID).get();
		
		gravarUltimaAtualizacao(doc);
		
		Element scriptElement = doc.select("div#row-10 script").first();
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(scriptElement.html());
		Iterator<JsonNode> datasIterator = jsonNode.get("x").get("hc_opts").get("xAxis").get("categories").elements();
		Iterator<JsonNode> valoresIterator = jsonNode.get("x").get("hc_opts").get("series").get(0).get("data").elements();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		StringBuilder fileString = new StringBuilder();
		
		while(datasIterator.hasNext()) {
			String data = datasIterator.next().asText();
			String valor = valoresIterator.next().asText();
			
			Date date = format1.parse(data);
			
			fileString.append(converterParaCSV(format2.format(date), valor));
		}
		
		Path path = Paths.get(DadoDiarioParameters.LOCAL_ARQUIVO_DADOS);
		FileUtils.write(path.toFile(), fileString.toString(), Charset.defaultCharset());
	}
	
	public LocalDateTime getUltimaAtualizacao() throws IOException {
		Path path = Paths.get(DadoDiarioParameters.LOCAL_ARQUIVO_ULTIMA_ATUALIZACAO);
		if(Files.exists(path)) {
			String ultimaAtualizacao = Files.lines(path).findFirst().get();
	
			if (ultimaAtualizacao != null) {
				return LocalDateTime.parse(ultimaAtualizacao, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); }
		}
		
		return null;
	}
	
	public PeriodoDTO getPrimeiroDiaUltimoDia() throws IOException {	
		List<DadoDiario> dados = extrairDados();
		
		PeriodoDTO primeiroDiaUltimoDia = new PeriodoDTO();		
		primeiroDiaUltimoDia.setInicio(dados.get(0).getData());
		primeiroDiaUltimoDia.setFim(dados.get(dados.size()-1).getData());
		
		return primeiroDiaUltimoDia;
	}
	
	public byte[] getCSV() throws IOException {
		Path path = Paths.get(DadoDiarioParameters.LOCAL_ARQUIVO_DADOS);
		return Files.readAllBytes(path);
	}
	
	private String converterParaCSV(String data, String total) {
		return String.join(DadoDiarioParameters.SEPARADOR_CSV 
				,data, total
				,DadoDiarioParameters.DELIMITADOR_LINHA_CSV);
	}
	
	private void gravarUltimaAtualizacao(Document doc) throws IOException {
		String p = doc.select("p").first().html();
		int indexBr = p.indexOf("<br>")+4;
		String ultimaAtualizacaoString = p.substring(indexBr, p.indexOf("<br>", indexBr));
		
		LocalDateTime ultimaAtualizacao = LocalDateTime.parse(ultimaAtualizacaoString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		LocalDateTime ultimaAtualizacaoLocal = getUltimaAtualizacao();
		if(ultimaAtualizacaoLocal != null && ultimaAtualizacaoLocal.equals(ultimaAtualizacao)) {
			throw new DadosJaAtualizadosException();
		}
		
		Path path = Paths.get(DadoDiarioParameters.LOCAL_ARQUIVO_ULTIMA_ATUALIZACAO);
		FileUtils.write(path.toFile(), ultimaAtualizacaoString, Charset.defaultCharset());
	}
}
