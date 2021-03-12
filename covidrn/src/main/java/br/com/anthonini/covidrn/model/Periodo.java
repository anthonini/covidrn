package br.com.anthonini.covidrn.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class Periodo {

	@NotNull(message = "Início não pode estar vazio")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate inicio;
	
	@NotNull(message = "Fim não pode estar vazio")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fim;
	
	private Long valorInicial;
	
	private Long valorFinal;

	public Long getTotal() {
		return valorFinal - valorInicial;
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

	public Long getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(Long valorInicial) {
		this.valorInicial = valorInicial;
	}

	public Long getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(Long valorFinal) {
		this.valorFinal = valorFinal;
	}
}
