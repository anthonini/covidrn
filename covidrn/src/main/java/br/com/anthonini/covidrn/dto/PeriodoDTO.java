package br.com.anthonini.covidrn.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;

public class PeriodoDTO {

	@NotNull(message = "Início é obrigatório")
	@DateTimeFormat(pattern=DadoDiarioParameters.FORMATO_DATAS)
	private LocalDate inicio;
	
	@NotNull(message = "Fim é obrigatório")
	@DateTimeFormat(pattern=DadoDiarioParameters.FORMATO_DATAS)
	private LocalDate fim;

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getFim() {
		return fim;
	}

	public void setFim(LocalDate fim) {
		this.fim = fim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fim == null) ? 0 : fim.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodoDTO other = (PeriodoDTO) obj;
		if (fim == null) {
			if (other.fim != null)
				return false;
		} else if (!fim.equals(other.fim))
			return false;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		return true;
	}
}
