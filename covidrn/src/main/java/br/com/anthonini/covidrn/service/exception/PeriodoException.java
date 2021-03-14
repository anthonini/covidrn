package br.com.anthonini.covidrn.service.exception;

public class PeriodoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private boolean inicio;
	private boolean fim;

	public PeriodoException(String message, boolean inicio, boolean fim) {
		super(message);
		this.inicio = inicio;
		this.fim = fim;
	}

	public boolean isInicio() {
		return inicio;
	}

	public void setInicio(boolean inicio) {
		this.inicio = inicio;
	}

	public boolean isFim() {
		return fim;
	}

	public void setFim(boolean fim) {
		this.fim = fim;
	}
}
