package br.com.anthonini.covidrn.session;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.com.anthonini.covidrn.dto.PeriodoDTO;

@Component
@SessionScope
public class PeriodosSession {

	private List<PeriodoDTO> periodos = new ArrayList<>();

	public void adicionarPeriodoDTO(PeriodoDTO periodoDTO) {
		periodos.add(periodoDTO);
	}
	
	public List<PeriodoDTO> getPeriodos() {
		return periodos;
	}
}
