package br.com.anthonini.covidrn.controller;

import java.io.IOException;
import java.text.ParseException;
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
import br.com.anthonini.covidrn.service.exception.DadosJaAtualizadosException;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController extends AbstractController {

	@Autowired
	private DadoDiarioService dadoDiarioService;
	
	@GetMapping
	public ModelAndView index(ModelMap modelMap) throws IOException {
		modelMap.addAttribute("ultimaAtualizacao", dadoDiarioService.getUltimaAtualizacao());
		return new ModelAndView("dashboard");
	}
	
	@PostMapping
	public ModelAndView atualizar(ModelMap model, RedirectAttributes redirectAttributes) throws IOException, ParseException {
		try {
			dadoDiarioService.atualizarDados();
			addMensagemSucesso(redirectAttributes, "Dados atualizados com sucesso!");
			return new ModelAndView("redirect:/");
		} catch (DadosJaAtualizadosException e) {
			addMensagemInfo(model, "Os dados atuais já são os mais recentes!");
			return index(model);
		}		
	}
	
	@GetMapping("/totalCasosConfirmados")
	public @ResponseBody List<DadoDiario> listTotalByMonth() throws IOException {
		return dadoDiarioService.extrairDados();
	}
}
