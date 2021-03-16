package br.com.anthonini.covidrn.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.covidrn.dto.PeriodoDTO;
import br.com.anthonini.covidrn.model.DadoDiario;
import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;
import br.com.anthonini.covidrn.service.exception.PeriodoException;
import br.com.anthonini.covidrn.session.PeriodosSession;

@Service
public class CompararPeriodoService {

	@Autowired
	private PeriodosSession session;
	
	@Autowired
	private DadoDiarioService dadoDiarioService;
	
	public void adicionarPeriodo(PeriodoDTO periodoDTO) throws IOException {
		validarAdicaoPeriodo(periodoDTO);
		session.adicionarPeriodoDTO(periodoDTO);
		Collections.sort(session.getPeriodos());
	}
	
	public List<PeriodoDTO> getPeriodos() {
		return session.getPeriodos();
	}
	
	public void resetar() {
		session.getPeriodos().clear();
	}
	
	public void removerPeriodo(Integer index) {
		session.getPeriodos().remove(index.intValue());		
	}
	
	public void calcularTotais() throws IOException {
		List<DadoDiario> dadosDiarios = dadoDiarioService.extrairDados();
		for(int i = 0; i < dadosDiarios.size(); i++) {
			DadoDiario dado = dadosDiarios.get(i);
			for(PeriodoDTO periodoDTO : session.getPeriodos()) {
				if(periodoDTO.getInicio().equals(dado.getData())) {
					if(i == 0)
						periodoDTO.setTotalInicio(0);
					else
						periodoDTO.setTotalInicio(dadosDiarios.get(i-1).getTotal());
				}
				if(periodoDTO.getFim().equals(dado.getData())) {
					periodoDTO.setTotalFim(dado.getTotal());
				}
			}
		}
	}
	
	public Integer getTotalGeral() throws IOException {
		Integer total = 0;
		
		List<DadoDiario> dadosDiarios = dadoDiarioService.extrairDados();
		for(int i = 0; i < dadosDiarios.size(); i++) {
			DadoDiario dado = dadosDiarios.get(i);
			for(PeriodoDTO periodoDTO : session.getPeriodos()) {
				if(isDataDentroPeriodo(dado.getData(), periodoDTO.getInicio(), periodoDTO.getFim())) {
					Integer diferenca;
					if(i == 0)
						diferenca = dado.getTotal();
					else
						diferenca = dadosDiarios.get(i).getTotal() - dadosDiarios.get(i-1).getTotal();
					
					total += diferenca;
					break;
				}
			}
		}
		
		return total;
	}

	private void validarAdicaoPeriodo(PeriodoDTO periodoDTO) throws IOException {
		if(dadoDiarioService.getUltimaAtualizacao() == null) {
			throw new PeriodoException("Dados ainda não importados. Vá em Dashboard e Atualize Agora.", false, false);
		}
		
		if(periodoDTO.getInicio().isAfter(periodoDTO.getFim())) {
			throw new PeriodoException("Inicio não pode ser maior que o fim.", true,true);
		}
		
		PeriodoDTO primeiroDiaUltimoDia = dadoDiarioService.getPrimeiroDiaUltimoDia();
		if(periodoDTO.getInicio().isBefore(primeiroDiaUltimoDia.getInicio())) {
			throw new PeriodoException(
					String.format("Inicio não pode ser menor que %s.", 
							primeiroDiaUltimoDia.getInicio().format(DateTimeFormatter.ofPattern(DadoDiarioParameters.FORMATO_DATAS)))
					,true,false);
		}
		if(periodoDTO.getInicio().isAfter(primeiroDiaUltimoDia.getFim())) {
			throw new PeriodoException(
					String.format("Inicio não pode ser maior que %s.", 
							primeiroDiaUltimoDia.getFim().format(DateTimeFormatter.ofPattern(DadoDiarioParameters.FORMATO_DATAS)))
					,true,false);
		}
		
		if(periodoDTO.getFim().isBefore(primeiroDiaUltimoDia.getInicio())) {
			throw new PeriodoException(
					String.format("Fim não pode ser menor que %s.", 
							primeiroDiaUltimoDia.getInicio().format(DateTimeFormatter.ofPattern(DadoDiarioParameters.FORMATO_DATAS)))
					,false,true);
		}
		
		if(periodoDTO.getFim().isAfter(primeiroDiaUltimoDia.getFim())) {
			throw new PeriodoException(
					String.format("Fim não pode ser maior que %s.", 
							primeiroDiaUltimoDia.getFim().format(DateTimeFormatter.ofPattern(DadoDiarioParameters.FORMATO_DATAS)))
					,false,true);
		}
		
		for(PeriodoDTO periodoDTOAdicionado : session.getPeriodos()) {
			if(periodoDTO.equals(periodoDTOAdicionado)) {
				throw new PeriodoException("Período já adicionado", true, true);
			}
		}
	}
	
	private boolean isDataDentroPeriodo(LocalDate data, LocalDate inicio, LocalDate fim) {
		return (data.equals(inicio) || data.isAfter(inicio)) && (data.isEqual(fim) || data.isBefore(fim));
	}
}
