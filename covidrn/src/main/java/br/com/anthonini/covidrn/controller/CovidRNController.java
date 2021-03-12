package br.com.anthonini.covidrn.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.covidrn.model.DadoDiario;
import br.com.anthonini.covidrn.service.DadoDiarioService;

@Controller
@RequestMapping("/covidrn")
public class CovidRNController extends AbstractController {

	@Autowired
	private DadoDiarioService DadoDiarioService;
	
	@GetMapping
	public ModelAndView index(ModelMap modelMap) throws IOException {
		modelMap.addAttribute("ultimaAtualizacao", DadoDiarioService.getUltimaAtualizacao());
		return new ModelAndView("index");
	}
	
	@PostMapping
	public ModelAndView atualizar(ModelMap model, RedirectAttributes redirectAttributes) throws IOException, ParseException {
		LocalDate ultimaAtualizacao = DadoDiarioService.getUltimaAtualizacao();
		if(ultimaAtualizacao == null || ultimaAtualizacao.isBefore(LocalDate.now())) {
			DadoDiarioService.atualizarDados();
			addMensagemSucesso(redirectAttributes, "Dados atualizados com sucesso!");
			return new ModelAndView("redirect:/covidrn");
		}
		
		addMensagemInfo(model, "Dados já atualizados!");
		return index(model);
	}
	
	@GetMapping("/totalByMonth")
	public @ResponseBody List<DadoDiario> listTotalByMonth() throws IOException {
		return DadoDiarioService.extrairDados();
	}
}
