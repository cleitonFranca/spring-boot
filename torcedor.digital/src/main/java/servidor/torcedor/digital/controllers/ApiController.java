package servidor.torcedor.digital.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;
import servidor.torcedor.digital.utils.JsonTransform;

@Controller()
@RequestMapping("/api")
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioDAO usuarioDao;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) {
	
		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);

		if (usuario == null) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado");
		} 

		return JsonTransform.jsonUser(usuario);
	}
	
	@RequestMapping(value = "/cadastrarUsuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String cadastrarUsuario(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) throws Exception {
		String json = "";
		Usuario usuario = new Usuario();
		
		if(Strings.isNullOrEmpty(u.getNome()) 
				|| Strings.isNullOrEmpty(u.getSobreNome()) 
				|| Strings.isNullOrEmpty(u.getEmail()) 
				|| Strings.isNullOrEmpty(u.getSenha())) {
			
			String errorMsg = String.format("Há Campos obrigatórios não preenchidos!");
			logger.error(errorMsg);
			return JsonTransform.jsonError(res, HttpServletResponse.SC_PRECONDITION_FAILED, errorMsg);
			
		}
		
		usuario.setNome(u.getNome());
		usuario.setSobreNome(u.getSobreNome());
		usuario.setEmail(u.getEmail());
		usuario.setSenha(CriptyEncode.encodeSha256Hex(u.getSenha()));
		usuario.setTipo("app");
		
		
		Usuario novo = null;
		
		try {
			novo = repository.save(usuario);
			json = JsonTransform.jsonUser(novo);
		} catch (Exception e) {
			String errorMsg = String.format("O e-mail [%s], já está cadastrado em nossa base de dados!", usuario.getEmail());
			logger.error(e.getMessage());
			json = JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		}
		
		return json;
		
	}

}
