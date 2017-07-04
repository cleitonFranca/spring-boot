package servidor.torcedor.digital.controllers;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import servidor.torcedor.digital.utils.JsonTransform;

@Controller
public class checkoutController {
	private static final Double valor = 20.00;
	
	@RequestMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	
	public String checkout(Model model, @RequestParam String email,@RequestParam String id_jogo, @RequestParam Integer quantidade, HttpServletResponse res) throws ParseException  {
		
		try {
			
			model.addAttribute("email", email);
			model.addAttribute("id_jogo", id_jogo);
			model.addAttribute("quantidade", quantidade);
			model.addAttribute("valor", valor);
			
		} catch (Exception e) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Falha na criação de checkout");
		}
		return "checkout";
	}

}
