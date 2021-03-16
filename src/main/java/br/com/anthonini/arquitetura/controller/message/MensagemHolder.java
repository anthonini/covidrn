package br.com.anthonini.arquitetura.controller.message;

import org.springframework.ui.ModelMap;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

/**
 * @author Anthonini (anthonini@info.ufrn.br)
 * @since 09/12/2019
 */
public enum MensagemHolder {

    MODEL_AND_VIEW(ModelAndView.class) {
        @Override
        public void adicionarMensagem(Object object, TipoMensagem tipoMensagem, String mensagem) {
            ModelAndView mensagemHolder = (ModelAndView) object;
            MensagemWrapper mensagemWrapper =  getMensagens((MensagemWrapper)mensagemHolder.getModel().get(tipoMensagem.toString()), tipoMensagem);
            mensagemWrapper.adicionarMensagem(mensagem);
            mensagemHolder.addObject(tipoMensagem.toString(), mensagemWrapper);
        }
    },
    MODEL_MAP(BindingAwareModelMap.class) {
        @Override
        public void adicionarMensagem(Object object, TipoMensagem tipoMensagem, String mensagem) {
            ModelMap mensagemHolder = (ModelMap) object;
            MensagemWrapper mensagemWrapper =  getMensagens((MensagemWrapper)mensagemHolder.get(tipoMensagem.toString()), tipoMensagem);
            mensagemWrapper.adicionarMensagem(mensagem);
            mensagemHolder.addAttribute(tipoMensagem.toString(), mensagemWrapper);
        }
    },
    REDIRECT_ATTRIBUTES(RedirectAttributesModelMap.class) {
        @Override
        public void adicionarMensagem(Object object, TipoMensagem tipoMensagem, String mensagem) {
            RedirectAttributes mensagemHolder = (RedirectAttributes) object;
            MensagemWrapper mensagemWrapper = getMensagens((MensagemWrapper)mensagemHolder.getFlashAttributes().get(tipoMensagem.toString()), tipoMensagem);
            mensagemWrapper.adicionarMensagem(mensagem);
            mensagemHolder.addFlashAttribute(tipoMensagem.toString(), mensagemWrapper);
        }
    };

	private Class<?> clazz;

    MensagemHolder(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    protected MensagemWrapper getMensagens(MensagemWrapper mensagemWrapper, TipoMensagem tipoMensagem) {
        if(mensagemWrapper == null)
            mensagemWrapper = new MensagemWrapper(tipoMensagem);
        return mensagemWrapper;
    }

    public abstract void adicionarMensagem(Object mensagemHolder, TipoMensagem tipoMensagem, String mensagem);

    public static MensagemHolder getTipoMensagemHolder(Object object) {
        for(MensagemHolder mensagemHolder : MensagemHolder.values()) {
            if( mensagemHolder.getClazz() == object.getClass()) {
                return mensagemHolder;
            }
        }
        return null;
    }
}
