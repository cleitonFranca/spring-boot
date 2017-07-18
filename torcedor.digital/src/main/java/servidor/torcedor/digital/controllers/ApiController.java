package servidor.torcedor.digital.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import servidor.torcedor.digital.DAO.FaturamentoDAO;
import servidor.torcedor.digital.DAO.IngressoDAO;
import servidor.torcedor.digital.DAO.EnderecoDAO;
import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.DAO.ViewRankDAO;
import servidor.torcedor.digital.models.Calendario;
import servidor.torcedor.digital.models.Faturamento;
import servidor.torcedor.digital.models.Ingresso;
import servidor.torcedor.digital.models.DTOImage;
import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.models.ViewRankGeral;
import servidor.torcedor.digital.repositories.CalendarioRepository;
import servidor.torcedor.digital.repositories.RankRepository;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;
import servidor.torcedor.digital.utils.DateNow;
import servidor.torcedor.digital.utils.JsonTransform;
import servidor.torcedor.digital.utils.PassRandom;
import servidor.torcedor.digital.utils.PontuaUsuario;
import servidor.torcedor.digital.utils.SenderMailService;

@Controller
@RequestMapping("/api")
public class ApiController {
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	private static final String location = "/home/ubuntu/app-static/img/";
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private EnderecoDAO enderecoDAO;
	
	@Autowired
	private FaturamentoDAO cartaFaturaDAO;

	@Autowired
	private CalendarioRepository calendarioRepo;

	@Autowired
	private UsuarioDAO usuarioDao;
	
	@Autowired
	private DTOImage image;

	@Autowired
	private RankRepository rankRepo;

	@Autowired
	private ViewRankDAO rankDao;
	
	@Autowired
	private IngressoDAO ingressoDAO;

	@Autowired
	private SenderMailService senderMailService;

	

	@RequestMapping(value="/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		
		byte[] arquivo = file.getBytes();
		String nomeArquivo = PassRandom.getRandomPass(10)+"_"+file.getOriginalFilename();
		Files.write(arquivo, new File(location+nomeArquivo));
		
		image.message = "success";
		image.location = "http://torcedordigital.com:8081/img/"+nomeArquivo;
		
		Gson gson = new Gson();
		
		return gson.toJson(image);
	}
	
	@RequestMapping(value="/upload/salvarNoBanco", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String salvarNoBanco(@RequestParam("local") String local, @RequestParam("email") String email) throws IOException {
		
		Usuario usuario = usuarioDao.buscaUsuarioPorEmail(email);
		
		usuario.setImg(local);
		repository.save(usuario);
		
		return "{\"msg\":\"sucess\"}";
	}

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) {

		Usuario usuario = usuarioDao.buscaUsuarioPorEmailSenha(u);

		if (usuario == null) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado");
		}

		return JsonTransform.jsonUser(usuario);
	}

	@RequestMapping("/recuperarSenha")
	@ResponseBody
	public String recuperarSenha(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res) throws Exception {

		Usuario usuario = usuarioDao.recuperarSenha(u);
		String senha = PassRandom.getRandomPass(6);

		if (usuario != null) {
			usuario.setSenha(CriptyEncode.encodeSha256Hex(senha));
			usuario.setAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			Usuario update = repository.save(usuario);

			senderMailService.send(update.getEmail(), "Nova senha - Torcedor Digital",
					String.format("Estamos enviando sua senha de acesso [ %s ], "
							+ "para altera-la acesse as configurações de perfil "
							+ "do app Torcedor digital ou acesse %s", senha, "torcedordigital.com/admin"));

			if (update != null) {
				return String.format("{\"message\": \"Enviado para e-mail: %s\"}", usuario.getEmail());
			}

		}
		return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "E-mail não encontrado");

	}

	@RequestMapping("/convite")
	@ResponseBody
	public String convite(@RequestParam(value = "convidado") String convidado,
			@RequestParam(value = "emailConvidado") String emailConvidado,
			@RequestParam(value = "usuarioNome") String usuarioNome, HttpServletResponse res) throws Exception {

		if (!Strings.isNullOrEmpty(convidado) && !Strings.isNullOrEmpty(emailConvidado)
				&& !Strings.isNullOrEmpty(usuarioNome)) {

			boolean send = senderMailService.sendConfirm(emailConvidado, "Convite",
					String.format("%s está convidando-o para fazer parte do Torcedor Digital, acesse: %s", usuarioNome,
							"http://torcedordigital.com/api/cadastrarUsuarioByFacebook?nome=+" + convidado + "&email="
									+ emailConvidado + ""));

			if (send) {
				return String.format("{\"message\": \"Convite enviado para e-mail: %s\"}", emailConvidado);
			}

		}
		return JsonTransform.jsonError(res, HttpServletResponse.SC_NOT_FOUND, "E-mail não encontrado");

	}

	@RequestMapping(value = "/existe", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiExiste(@RequestParam(value = "email") String email, HttpServletResponse res) {

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

		if (Strings.isNullOrEmpty(u.getNome()) || Strings.isNullOrEmpty(u.getSobreNome())
				|| Strings.isNullOrEmpty(u.getEmail()) || Strings.isNullOrEmpty(u.getSenha())) {

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
			String errorMsg = String.format("O e-mail [%s], já está cadastrado em nossa base de dados!",
					usuario.getEmail());
			logger.error(e.toString());
			json = JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		}

		return json;

	}

	@RequestMapping(value = "/cadastrarUsuarioByFacebook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String cadastrarUsuarioByFacebook(@ModelAttribute("Usuario") Usuario u, HttpServletResponse res)
			throws Exception {
		String json = "";
		Usuario usuario = new Usuario();
		String senha = PassRandom.getRandomPass(6);

		usuario.setNome(u.getNome());
		usuario.setEmail(u.getEmail());
		usuario.setSenha(CriptyEncode.encodeSha256Hex(senha));
		usuario.setCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		usuario.setTipo("app");

		try {
			Usuario novo = repository.save(usuario);
			rankRepo.save(PontuaUsuario.pontuar(novo.getId(), 100.0));

			senderMailService.send(u.getEmail(), "Bem vindo",
					String.format("Estamos enviando sua senha de acesso %s, "
							+ "para altera-la acesse as configurações de perfil "
							+ "do app Torcedor digital ou acesse %s", senha, "torcedordigital.com/admin"));

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
	
	@RequestMapping(value = "/pontuarPorConvite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String pontuarPorConvite(@RequestParam("email") String email, HttpServletResponse res)
			throws Exception {
		String json = "";
		Usuario usuario = usuarioDao.buscaUsuarioPorEmail(email);
		try {
			rankRepo.save(PontuaUsuario.pontuar(usuario.getId(), 100.0));
			json = JsonTransform.jsonSuccess(res, HttpServletResponse.SC_ACCEPTED, "Pontuado com sucesso!");
		} catch (Exception e) {
			String errorMsg = String.format("Falha na pontuação para o usuario!", usuario.getNome());
			logger.error(e.toString());
			json = JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		}

		return json;

	}

	@RequestMapping(value = "/pontuarIngresso", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String pontuarIngresso(@RequestParam(value = "id") String id, HttpServletResponse res) throws IOException {
		Long idUser = Long.valueOf(id);
		rankRepo.save(PontuaUsuario.pontuar(idUser, 300.0));

		return "{'status': 'Ingresso Confirmado'}";
	}

	@RequestMapping(value = "/calendario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String calendario(HttpServletResponse res) {

		List<Calendario> jogos = calendarioRepo.findAll();
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/YYYY HH:mm").create();

		return gson.toJson(jogos);
	}

	@RequestMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String checkout(@RequestParam Map<String, String> allRequestParams, HttpServletResponse res) throws ParseException  {
		
		try {
			
			salvarOuAtualizarEndereco(allRequestParams);
			novoFaturamento(allRequestParams);
			
		} catch (Exception e) {
			return JsonTransform.jsonError(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Falha na criação de checkout");
		}
		return JsonTransform.jsonSuccess(res, HttpServletResponse.SC_CREATED, "Fatura criada com sucesso");
	}

	private Faturamento novoFaturamento(Map<String, String> allRequestParams) throws ParseException {
		Faturamento cartaFatura =  cartaFaturaDAO.salvarOuAtualizarCartaoFaturamento(allRequestParams);
		return cartaFatura;
	}

	private Endereco salvarOuAtualizarEndereco(Map<String, String> allRequestParams) {
		Endereco endereco = enderecoDAO.salvarOuAtualizarEndereco(allRequestParams);
		return endereco;
	}

	@RequestMapping(value = "/now", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String now(HttpServletResponse res) throws IOException {

		Timestamp now = Timestamp.valueOf(DateNow.getDateNow());

		return now.toString();
	}
	
	@RequestMapping(value = "/ingressos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String ingressos(@RequestParam("email") String email) throws IOException {
		Usuario usuario = usuarioDao.buscaUsuarioPorEmail(email);
		Gson json = new Gson();
		List<Ingresso> list = ingressoDAO.buscaIngressosByIdUsuario(usuario.getId().intValue());
		return json.toJson(list);
	}
	
	

}

