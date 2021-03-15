package br.com.anthonini.covidrn.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;

public class PeriodoDTO implements Comparable<PeriodoDTO> {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DadoDiarioParameters.FORMATO_DATAS)
	@NotNull(message = "Início é obrigatório")
	@DateTimeFormat(pattern=DadoDiarioParameters.FORMATO_DATAS)
	private LocalDate inicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DadoDiarioParameters.FORMATO_DATAS)
	@NotNull(message = "Fim é obrigatório")
	@DateTimeFormat(pattern=DadoDiarioParameters.FORMATO_DATAS)
	private LocalDate fim;
	
	private Integer totalInicio;
	
	private Integer totalFim;
	
	public PeriodoDTO() {}

	public PeriodoDTO(@NotNull(message = "Início é obrigatório") LocalDate inicio, @NotNull(message = "Fim é obrigatório") LocalDate fim) {
		this.inicio = inicio;
		this.fim = fim;
	}

	public Integer getTotal() {
		return totalFim - totalInicio;
	}

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

	public Integer getTotalInicio() {
		return totalInicio;
	}

	public void setTotalInicio(Integer totalInicio) {
		this.totalInicio = totalInicio;
	}

	public Integer getTotalFim() {
		return totalFim;
	}

	public void setTotalFim(Integer totalFim) {
		this.totalFim = totalFim;
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

	@Override
	public int compareTo(PeriodoDTO o) {
		int compare = this.getInicio().compareTo(o.getInicio());
		
		if(compare == 0)
			compare = this.getFim().compareTo(o.getFim());
		
		return compare;
	}
}
