package br.com.anthonini.arquitetura.controller.message;

/**
 * @author Anthonini
 */
public enum TipoMensagem {
    ERROR("alert-danger"),
    INFO("alert-info"),
    SUCCESS("alert-success");

    private String alert;

    TipoMensagem(String alert) {
        this.alert = alert;
    }

    public String getAlert() {
        return alert;
    }
}
