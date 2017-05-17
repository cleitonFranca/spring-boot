package servidor.torcedor.digital.controllers;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

		boolean auth = validarSessao(session);

		if (!auth) {
			return "loginForm";
		}
		return "redirect:/";
	}
	
	@RequestMapping("/novoUsuario")
	public String novoUsuario() {
		
		return "formNovoUsuario";
		
	}
	
	@RequestMapping("/recuperarSenha")
	public String recuperarSenha() {
		
		return "formNovaSenha";
		
	}
	
	@RequestMapping("/cadastrarUsuario")
	public String cadastrarUsuario(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) throws Exception {
		
		Usuario usuario = new Usuario();
		
		usuario.setNome(u.getNome());
		usuario.setSobreNome(u.getSobreNome());
		usuario.setEmail(u.getEmail());
		usuario.setSenha(CriptyEncode.encodeSha256Hex(u.getSenha()));
		usuario.setTipo("app");
		
		Usuario novo = null;
		try {
			novo = repository.save(usuario);
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("erro", "Este usuário já está cadastrado em nosso sistema!");
			return "/formNovoUsuario";
		}
		
		if(novo!=null) {
			model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");
			return "/formNovoUsuario";
		} else {
			model.addAttribute("error", "Error para criar o usuario!");
			return "/formNovoUsuario";
		}
		
	}
	
	@RequestMapping("/editarUsuario/{idUsuario}")
	public String editarUsuario(@PathVariable Long idUsuario, Model model) {
		
		

		if (idUsuario!=null) {
			
			Usuario usuario = repository.findOne(idUsuario);
			model.addAttribute("usuario", usuario);		
			
			return "formEditarUsuario";
		}
		
		model.addAttribute("error", "Usuário não encontrado!");
		
		return "formEditarUsuario";
		
		
		
	}
	
	@RequestMapping("/apagarUsuario/{idUsuario}")
	public String apagarUsuario(@PathVariable Long idUsuario, Model model) {
		repository.delete(idUsuario);
		return "redirect:/usuarios";
	
	}
	
	@RequestMapping("/alterarInfoUsuario")
	public String alterarInfoUsuario(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) throws Exception {
		
		Usuario usuario = repository.findOne(u.getId());
		
		usuario.setNome(u.getNome());
		usuario.setSobreNome(u.getSobreNome());
		usuario.setEmail(u.getEmail());
		if(u.getSenha()!=null) {
			usuario.setSenha(CriptyEncode.encodeSha256Hex(u.getSenha()));
		}
		
		usuario.setTipo("app");
		
		Usuario update = null;
		
		try {
			
			update = repository.save(usuario);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("erro", e.getMessage());
			return "/formEditarUsuario";
		}
		
		if(update!=null) {
			model.addAttribute("sucesso", "Usuário alterado com sucesso!");
			return "/formEditarUsuario";
		} else {
			model.addAttribute("error", "Error para editar o usuario!");
			return "/formEditarUsuario";
		}
		
	}
	
	@RequestMapping("/recuperarSenhaPorEmail")
	public String recuperarSenha(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) {
		
		try {
		
			Usuario usuario = usuarioDao.recuperarSenha(u);
			
			model.addAttribute("senha", usuario.getSenha());
			return "formNovaSenha";
			
		} catch (Exception e) {
			model.addAttribute("senha", "Usuário não existe em nossa base de dados!");
			return "formNovaSenha";
		}
		
	}
	
	

	@RequestMapping("/login")
	public String login(Model model, @ModelAttribute("Usuario") Usuario u, HttpSession session) {
		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);
		if (usuario != null) {
			session.setAttribute("id", usuario.getId());
			session.setAttribute("nome", usuario.getNome());
			session.setAttribute("email", usuario.getEmail());
			session.setAttribute("tipo", usuario.getTipo());
			return "redirect:/";
		} else {
			model.addAttribute("error", "Usuarios não encontrado");
			return "loginForm";
		}
	}

	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	private boolean validarSessao(HttpSession session) {
		Object objSessao = session.getAttribute("id");

		if (objSessao != null) {
			return true;
		}

		return false;

	}

}
