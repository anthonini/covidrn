package br.com.anthonini.arquitetura.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Anthonini
 */
public class PaginationElementTagProcessor extends AbstractElementTagProcessor {

    private static final String NOME_TAG = "paginacao";
    private static final int PRECEDENCIA = 1000;

    public PaginationElementTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
                             IElementTagStructureHandler structureHandler) {
        IModelFactory modelFactory = context.getModelFactory();

        IAttribute page = tag.getAttribute("pagina");

        IModel model = modelFactory.createModel();
        model.add(modelFactory.createStandaloneElementTag("th:block"
                , "th:replace"
                , String.format("layout/fragments/Paginacao :: paginacao (%s)", page.getValue())));

        structureHandler.replaceWith(model, true);
    }

}

