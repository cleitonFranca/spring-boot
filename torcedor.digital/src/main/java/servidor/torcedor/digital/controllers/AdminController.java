package servidor.torcedor.digital.controllers;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;

@Controller
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);
	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioDAO usuarioDao;

	@RequestMapping("/admin")
	public String admin(HttpSession session) {

		boolean teste = validarSessao(session);

		if (!teste) {
			return "loginForm";
		}
		return "site";
	}
	
	@RequestMapping("/novoUsuario")
	public String novoUsuario() {
		
		return "formNovoUsuario";
		
	}
	
	@RequestMapping("/cadastrarUsuario")
	@ResponseBody
	public String cadastrarUsuario(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) throws Exception {
		
		Usuario usuario = new Usuario();
		
		usuario.setNome(u.getNome());
		usuario.setSobreNome(u.getSobreNome());
		usuario.setEmail(u.getEmail());
		usuario.setSenha(CriptyEncode.encodeSha256Hex(u.getSenha()));
		
		Usuario novo = repository.save(usuario);
		
		if(novo!=null) {
			return novo.toString();
		} else {
			return "Error para criar o usuario";
		}
		
	}

	@RequestMapping("/login")
	public String login(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) {
		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);
		if (usuario != null) {
			session.setAttribute("id", usuario.getId());
			session.setAttribute("nome", usuario.getNome());
			session.setAttribute("email", usuario.getEmail());
			return "site";
		} else {
			model.addAttribute("error", "Usuarios não encontrado");
			return "loginForm";
		}
	}

	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "site";
	}

	private boolean validarSessao(HttpSession session) {
		Object objSessao = session.getAttribute("id");

		if (objSessao != null) {
			return true;
		}

		return false;

	}

}
