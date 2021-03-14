package br.com.anthonini.covidrn.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.covidrn.dto.PeriodoDTO;
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

	private void validarAdicaoPeriodo(PeriodoDTO periodoDTO) throws IOException {
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
}
