package servidor.torcedor.digital.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.DAO.ViewRankDAO;
import servidor.torcedor.digital.models.Rank;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.models.ViewRankGeral;
import servidor.torcedor.digital.repositories.RankRepository;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;
import servidor.torcedor.digital.utils.JsonTransform;
import servidor.torcedor.digital.utils.PassRandom;
import servidor.torcedor.digital.utils.PontuaUsuario;
import servidor.torcedor.digital.utils.SenderMailService;

@Controller()
@RequestMapping("/api")
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioDAO usuarioDao;
	
	@Autowired
	private RankRepository rankRepo;
	
	@Autowired
	private ViewRankDAO rankDao;
	
	@Autowired
    SenderMailService senderMailService;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) {
	
		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);

		if (usuario == null) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado");
		} 

		return JsonTransform.jsonUser(usuario);
	}
	
	@RequestMapping(value = "/existe", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiExiste(@RequestParam(value="email") String email, HttpServletResponse res) {
	
		Usuario usuario = usuarioDao.buscaUsuarioPorEmail(email);

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
			rankRepo.save(PontuaUsuario.pontuar(novo.getId(), 100.0));
			json = JsonTransform.jsonUser(novo);
		} catch (Exception e) {
			String errorMsg = String.format("O e-mail [%s], já está cadastrado em nossa base de dados!", usuario.getEmail());
			logger.error(e.toString());
			json = JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		}
		
		return json;
		
	}
	
	@RequestMapping(value = "/cadastrarUsuarioByFacebook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String cadastrarUsuarioByFacebook(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) throws Exception {
		String json = "";
		Usuario usuario = new Usuario();
		String senha = PassRandom.getRandomPass(6);
		
		usuario.setNome(u.getNome());
		usuario.setEmail(u.getEmail());
		usuario.setSenha(CriptyEncode.encodeSha256Hex(senha));
		usuario.setTipo("app");
		
		try {
			Usuario novo = repository.save(usuario);
			rankRepo.save(PontuaUsuario.pontuar(novo.getId(), 100.0));
			
			senderMailService.send(u.getEmail(), "Bem vindo", String.format("Estamos enviando sua senha de acesso %s, "
					+ "para altera-la acesse as configurações de perfil "
					+ "do app Torcedor digital ou acesse %s",senha, "torcedordigital.com/admin"));
			
			json = JsonTransform.jsonUser(novo);
		} catch (Exception e) {
			String errorMsg = String.format("O e-mail %s, já está cadastrado!", usuario.getEmail());
			logger.error(e.toString());
			json = JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		}
		
		return json;
		
	}
	
	@RequestMapping(value = "/rank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String rank(HttpServletResponse res) {
	
		List<ViewRankGeral> rankGeral = rankDao.getRankGeral();
		

		if (rankGeral.size() <= 0) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "Não há resgistros!");
		} 

		return JsonTransform.jsonListRank(rankGeral);
	}
	
	@RequestMapping(value = "/pontuarIngresso", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String pontuarIngresso(@RequestParam(value="id") String id, HttpServletResponse res) {	
		Long idUser = Long.valueOf(id);
		rankRepo.save(PontuaUsuario.pontuar(idUser, 300.0));

		return "{'status': 'Ingresso Confirmado'}";
	}

}
