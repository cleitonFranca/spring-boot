package servidor.torcedor.digital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.UsuarioRepository;

@Controller
public class HelloController {

	@Autowired
	private UsuarioRepository repository;

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "<center>Torcedor Digital<br>Aguarde ...</center>";
	}

	@RequestMapping("/usuarios")
	@ResponseBody
	public String teste() {
		List<Usuario> records = repository.findAll();
		return records.toString();
	}
	
	
}
