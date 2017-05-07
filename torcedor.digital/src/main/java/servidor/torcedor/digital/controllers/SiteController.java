package servidor.torcedor.digital.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.UsuarioRepository;

@Controller
public class SiteController {
	
	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);
	private static final ObjectMapper om = new ObjectMapper();


	@Autowired
	private UsuarioRepository repository;

	@RequestMapping("/")
	public String home() {
		return "site";
	}

	@RequestMapping("/usuarios")
	@ResponseBody
	public String teste() {
		List<Usuario> records = repository.findAll();
		return records.toString();
	}
	
	@RequestMapping("/loginForm")
	public String loginForm(Model model, String error, String logout) {
		
		if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "loginForm";
		
		

		
	}
	

	public static String convertObjectToJson(Object obj) {
        String result = "";
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Error In JSON conversion : {}", e);
        }
        return result;
    }
	
	
}
