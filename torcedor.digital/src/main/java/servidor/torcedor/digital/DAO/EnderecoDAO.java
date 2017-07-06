package servidor.torcedor.digital.DAO;

import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.ResponseNotification;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.EnderecoRepository;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.DateNow;

@Component
public class EnderecoDAO {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private EnderecoRepository enderecoRepo;

	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioRepository userRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(EnderecoDAO.class);
	
	
	/**
	 * Busca de endereco de um usuario
	 * @param idUsuario
	 * @return
	 */
	public Endereco buscaEnderecoUsuario(Long idUsuario) {
		
		try {
			String sql = "SELECT * FROM endereco WHERE id_usuario=:idUsuario "
					+ "ORDER BY id DESC LIMIT 1";
			return (Endereco) em
					.createNativeQuery(sql, Endereco.class)
					.setParameter("idUsuario", idUsuario)
					.getSingleResult();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	public Endereco salvarOuAtualizarEndereco(Map<String, String> enderecoInfo) {
		
		String email = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("email")).findAny().get().getValue();
		String telefone = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("telefone")).findAny().get().getValue();
		String cep = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("cep")).findAny().get().getValue();
		String estado = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("estado")).findAny().get().getValue();
		String cidade = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("cidade")).findAny().get().getValue();
		String bairro = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("bairro")).findAny().get().getValue();
		String logradouro = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("logradouro")).findAny().get().getValue();
		String numero = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("numero")).findAny().get().getValue();
		String complemento = (enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("complemento")).findAny().get().getValue() != null) ? enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("complemento")).findAny().get().getValue(): null;
		
		Usuario usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		// atualiza o telefone;
		usuario.setTelefone(telefone);
		userRepo.save(usuario);
				
		Endereco endereco = buscaEnderecoUsuario(usuario.getId());
		
		if(endereco!=null) {
			endereco.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			return enderecoRepo.save(endereco);			
		}
		
		Endereco novo = novoEndereco(cep, estado, cidade, bairro, logradouro, numero, complemento, usuario);
		return enderecoRepo.save(novo);
		
		
		
	}

	private Endereco novoEndereco(String cep, String estado, String cidade, String bairro, String logradouro,
		String numero, String complemento, Usuario usuario) {
		
		Endereco endereco = new Endereco();
		endereco.setIdUsuario(usuario.getId());
		endereco.setCep(cep);
		endereco.setEstado(estado);
		endereco.setCidade(cidade);
		endereco.setBairro(bairro);
		endereco.setLogradouro(logradouro);
		endereco.setObs(complemento);
		endereco.setNumero(numero);
		endereco.setCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		endereco.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
		return endereco;
	}

	public Endereco salvarOuAtualizarEndereco(ResponseNotification response) {
		String email = response.getPayer_email();
		String cep = response.getAddress_zip();
		String estado = response.getAddress_state();
		String cidade = response.getAddress_city();
		String bairro = response.getAddress_name();
		String logradouro = response.getAddress_street();
		
		Usuario usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		
				
		Endereco endereco = buscaEnderecoUsuario(usuario.getId());
		
		if(endereco!=null) {
			endereco.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			return enderecoRepo.save(endereco);			
		}
		
		Endereco novo = novoEndereco(cep, estado, cidade, bairro, logradouro, usuario);
		
		return enderecoRepo.save(novo);
	}

	private Endereco novoEndereco(String cep, String estado, String cidade, String bairro, String logradouro,
			Usuario usuario) {
		Endereco endereco = new Endereco();
		endereco.setIdUsuario(usuario.getId());
		endereco.setCep(cep);
		endereco.setEstado(estado);
		endereco.setCidade(cidade);
		endereco.setBairro(bairro);
		endereco.setLogradouro(logradouro);
		endereco.setCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		endereco.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
		return endereco;
	}
}
