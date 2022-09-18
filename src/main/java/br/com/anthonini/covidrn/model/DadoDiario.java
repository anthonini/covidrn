package br.com.anthonini.covidrn.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.anthonini.covidrn.parameters.DadoDiarioParameters;

public class DadoDiario {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DadoDiarioParameters.FORMATO_DATAS)
	@DateTimeFormat(pattern=DadoDiarioParameters.FORMATO_DATAS)
	private LocalDate data;
	
	private Integer dia;
	
	private Integer total;

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
