package br.com.anthonini.arquitetura.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import br.com.anthonini.arquitetura.controller.message.TipoMensagem;

/**
 * @author Anthonini
 */
public class MessageElementTagProcessor extends AbstractElementTagProcessor {
    private static final String NOME_TAG = "message";
    private static final int PRECEDENCIA = 1000;

    public MessageElementTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
                             IElementTagStructureHandler structureHandler) {
        IModelFactory modelFactory = context.getModelFactory();
        IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag("div", "id", "sf-alert-container"));
        for(TipoMensagem tipoMensagem : TipoMensagem.values()) {
            model.add(modelFactory.createStandaloneElementTag("th:block"
                    , "th:replace"
                    , String.format("layout/fragments/Mensagem :: mensagens (${%s})", tipoMensagem.toString())));
        }
        model.add(modelFactory.createCloseElementTag("div"));

        structureHandler.replaceWith(model, true);
    }
}
