package servidor.torcedor.digital.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.utils.JsonTransform;

@Controller()
@RequestMapping("/api")
public class ApiController {
	
	//@Autowired
	//private UsuarioRepository repository;

	@Autowired
	private UsuarioDAO usuarioDao;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) {

		String json = "";
		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);

		if (usuario != null) {
			json = JsonTransform.jsonUser(usuario);
		} else {
			json = JsonTransform.jsonError(res);
		}

		return json;

	}

}
