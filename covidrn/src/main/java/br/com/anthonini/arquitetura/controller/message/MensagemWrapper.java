package br.com.anthonini.arquitetura.controller.message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Anthonini (anthonini@info.ufrn.br)
 * @since 09/12/2019
 */
public class MensagemWrapper {
    private TipoMensagem tipoMensagem;
    private Collection<String> mensagens;

    public MensagemWrapper(TipoMensagem tipoMensagem) {
        this.mensagens = new ArrayList<>();
        this.tipoMensagem = tipoMensagem;
    }

    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public Collection<String> getMensagens() {
        return mensagens;
    }

    public void setMensagens(Collection<String> mensagens) {
        this.mensagens = mensagens;
    }

    public void adicionarMensagem(String mensagem) {
        this.mensagens.add(mensagem);
    }
}
