package br.com.anthonini.covidrn.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.anthonini.covidrn.model.DadoDiario;
import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;

@Service
public class DadoDiarioService {
	
	@Autowired
	ResourceLoader resourceLoader;

	public List<DadoDiario> extrairDados() throws IOException {	
		MappingIterator<DadoDiario> dadosIterator = getDadosGravadosIterator();
		return dadosIterator.readAll();
	}
	
	public void atualizarDados() throws IOException, ParseException {
		Resource resource = resourceLoader.getResource(DadoDiarioParameters.LOCAL_ARQUIVO_DADOS);
		File file = resource.getFile();	
		StringBuilder fileString = new StringBuilder();
		
		Document doc = Jsoup.connect(DadoDiarioParameters.URL_SERVICO_LOTERIAS).get();
		Elements scriptElement = doc.select("script[data-for=\"htmlwidget-7892988164b9c540c40b\"]");
		
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode jsonNode = mapper.readTree(scriptElement.html());
		Iterator<JsonNode> datasIterator = jsonNode.get("x").get("hc_opts").get("xAxis").get("categories").elements();
		Iterator<JsonNode> valoresIterator = jsonNode.get("x").get("hc_opts").get("series").get(0).get("data").elements();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		
		while(datasIterator.hasNext()) {
			String data = datasIterator.next().asText();
			String valor = valoresIterator.next().asText();
			
			Date date = format1.parse(data);
			
			fileString.append(converterParaCSV(format2.format(date), valor));
		}
		
		FileUtils.write(file, fileString.toString(), Charset.defaultCharset());
	}
	
	public LocalDate getUltimaAtualizacao() throws IOException {
		List<DadoDiario> dados = getDadosGravadosIterator().readAll();
		
		if(!dados.isEmpty()) {
			return dados.get(dados.size()-1).getData();
		}
		
		return null;
	}
	
	private MappingIterator<DadoDiario> getDadosGravadosIterator() throws IOException {
		Resource resource = resourceLoader.getResource(DadoDiarioParameters.LOCAL_ARQUIVO_DADOS);
		File file = resource.getFile();
		CsvMapper mapper = (CsvMapper) new CsvMapper().registerModule(new JavaTimeModule());
		CsvSchema schema = mapper.schemaFor(DadoDiario.class)
				.withColumnSeparator(DadoDiarioParameters.SEPARADOR_CSV.charAt(0));

		return mapper.readerFor(DadoDiario.class).with(schema).readValues(file);
	}
	
	
	private String converterParaCSV(String data, String total) {
		return String.join(DadoDiarioParameters.SEPARADOR_CSV 
				,data, total
				,DadoDiarioParameters.DELIMITADOR_LINHA_CSV);
	}
}
