	package br.com.anthonini.arquitetura.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Anthonini
 */
@Controller
public class SairController implements ApplicationContextAware  {

	private ApplicationContext applicationContext;
	
	@PostMapping("/sair")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) applicationContext).close();
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
