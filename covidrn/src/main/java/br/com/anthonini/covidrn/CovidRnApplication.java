package br.com.anthonini.covidrn;

import java.util.Collections;
import java.util.Locale;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import br.com.anthonini.arquitetura.controller.SairController;
import br.com.anthonini.arquitetura.thymeleaf.ArquiteturaDialect;

@SpringBootApplication
public class CovidRnApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidRnApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
	
	@Bean
	public ArquiteturaDialect arquiteturaDialect() {
		return new ArquiteturaDialect();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
	    
	    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

	    RestTemplate restTemplate = builder.additionalMessageConverters(converter).build();
	    restTemplate.setRequestFactory(factory);
	    
		return restTemplate;
	}
	
	@Configuration
	@ComponentScan(basePackageClasses = { SairController.class })
	public static class MvcConfig implements WebMvcConfigurer {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addRedirectViewController("/", "/covidrn");
		}
	}
}
