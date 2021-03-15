package br.com.anthonini.covidrn.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.covidrn.dto.PeriodoDTO;
import br.com.anthonini.covidrn.service.CompararPeriodoService;
import br.com.anthonini.covidrn.service.exception.PeriodoException;

@Controller
@RequestMapping("/comparar-periodos")
public class CompararPeriodosController extends AbstractController {
	
	@Autowired
	private CompararPeriodoService service;
	
	@GetMapping
	public ModelAndView form(PeriodoDTO periodoDTO, ModelMap modelMap) throws IOException {		
		modelMap.addAttribute("periodos", service.getPeriodos());
		return new ModelAndView("Comparar-Periodos");
	}
	
	@PostMapping(value = "/adicionar-periodo", params="action=adicionar")
	public ModelAndView adicionarPeriodo(@Valid PeriodoDTO periodoDTO, BindingResult bindingResult, ModelMap modelMap, RedirectAttributes redirectAttributes) throws IOException {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return form(periodoDTO, modelMap);
		}
		
		try {
			service.adicionarPeriodo(periodoDTO);
		} catch (PeriodoException e) {
			addMensagemErro(modelMap, e.getMessage());
			if(e.isInicio())
				bindingResult.rejectValue("inicio", null, null);
			if(e.isFim())
				bindingResult.rejectValue("fim", null, null);
			
			return form(periodoDTO, modelMap);
		}
		
		addMensagemSucesso(redirectAttributes, "Período adicionado com sucesso!");
		return new ModelAndView("redirect:/comparar-periodos");
	}
	
	@PostMapping(value = "/adicionar-periodo", params="action=resetar")
	public ModelAndView resetar(RedirectAttributes redirectAttributes) {
		service.resetar();
		addMensagemSucesso(redirectAttributes, "Formulário resetado com sucesso!");
		return new ModelAndView("redirect:/comparar-periodos");
	}
	
	@GetMapping("/remover-periodo/{index}")
	public ModelAndView remover(@PathVariable Integer index, RedirectAttributes redirectAttributes) {
		service.removerPeriodo(index);
		addMensagemSucesso(redirectAttributes, "Período removido com sucesso!");
		return new ModelAndView("redirect:/comparar-periodos");
	}
	
	@GetMapping("/visualizar")
	public ModelAndView visualizar(ModelMap modelMap) throws IOException {
		service.calcularTotais();
		modelMap.addAttribute("periodos", service.getPeriodos());
		modelMap.addAttribute("totalGeral", service.getTotalGeral());
		return new ModelAndView("Visualizar");
	}
	
	@GetMapping("/periodos")
	public @ResponseBody List<PeriodoDTO> periodos() throws IOException {
		return service.getPeriodos();
	}
}
