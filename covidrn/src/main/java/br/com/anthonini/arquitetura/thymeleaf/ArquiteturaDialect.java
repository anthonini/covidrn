package br.com.anthonini.arquitetura.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import br.com.anthonini.arquitetura.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import br.com.anthonini.arquitetura.thymeleaf.processor.MessageElementTagProcessor;
import br.com.anthonini.arquitetura.thymeleaf.processor.PaginationElementTagProcessor;

/**
 * @author Anthonini
 */
public class ArquiteturaDialect extends AbstractProcessorDialect {

    public ArquiteturaDialect() {
        super("Arquitetura", "an", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processadores = new HashSet<>();
        processadores.add(new MessageElementTagProcessor(dialectPrefix));
        processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
        processadores.add(new PaginationElementTagProcessor(dialectPrefix));

        return processadores;
    }

}
