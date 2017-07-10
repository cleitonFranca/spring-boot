package servidor.torcedor.digital.DAO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import servidor.torcedor.digital.models.Calendario;
import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.ResponseNotification;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.CalendarioRepository;
import servidor.torcedor.digital.repositories.FaturamentoRepository;
import servidor.torcedor.digital.repositories.IngressoRepository;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;
import servidor.torcedor.digital.utils.DateNow;
import servidor.torcedor.digital.utils.SenderMailService;
import servidor.torcedor.digital.models.Faturamento;
import servidor.torcedor.digital.models.Ingresso;


@Component
public class FaturamentoDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(FaturamentoDAO.class);
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private FaturamentoRepository repo;
	
	@Autowired
	private IngressoRepository ingressoRepo;
	
	@Autowired
	private CalendarioRepository calendarioRepo;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private SenderMailService senderMailService;
	
	private static final Double valorUnitario = 20.00;

	
	
	/**
	 * Busca de endereco de um usuario
	 * @param idUsuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Faturamento> buscaFaturaUsuario(Long idUsuario) {
		
		try {
			String sql = "SELECT * FROM faturamento WHERE id_usuario=:idUsuario";
			return 	em
					.createNativeQuery(sql, Faturamento.class)
					.setParameter("idUsuario", idUsuario)
					.getResultList();
					
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Busca de endereco de um usuario
	 * @param idUsuario
	 * @return
	 */
	public Faturamento buscaFatura(String transacao) {
		
		try {
			String sql = "SELECT * FROM faturamento WHERE id_transacao=:transacao limit 1";
			return 	(Faturamento) em
					.createNativeQuery(sql, Faturamento.class)
					.setParameter("transacao", transacao).getSingleResult();
					
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Faturamento> buscaFaturasConcluidas() {
		
		try {
			String sql = "SELECT * FROM faturamento WHERE status='concluido'";
			
			return 	em
					.createNativeQuery(sql, Faturamento.class)
					.getResultList();
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	public Faturamento salvarOuAtualizarCartaoFaturamento(Map<String, String> enderecoInfo) throws ParseException {
		
		String idJogo = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("id_jogo")).findAny().get().getValue();
		String email = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("email")).findAny().get().getValue();
		String numeroCartao = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("numero_cartao")).findAny().get().getValue();
		String bandeira = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("bandeira")).findAny().get().getValue();
		String validade = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("validade")).findAny().get().getValue();
		String codigo = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("codigo")).findAny().get().getValue();
		String quantidade = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("quantidade")).findAny().get().getValue();
		
		Usuario usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		
		Faturamento novo = novaFatura(numeroCartao, bandeira, validade, codigo, quantidade, idJogo,usuario);
		
		senderMailService.send(email, "Obrigado por Compra seu ingresso pelo Torcedor Digital","Assim que confirmamos o pagamento estaremos enviado o seu ingresso.");
		
		return repo.save(novo);
		
		
	}

	private Faturamento novaFatura(String numeroCartao, String bandeira, String validade, String codigo,
			String quantidade, String idJogo,Usuario usuario) throws ParseException {
		
		Faturamento cartaFatura = new Faturamento();
		cartaFatura.setIdUsuario(usuario.getId());
		cartaFatura.setIdJogo(Long.valueOf(idJogo));
		cartaFatura.setDataCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		cartaFatura.setQuantidade(Integer.valueOf(quantidade));
		cartaFatura.setValorTotal(BigDecimal.valueOf(calculaValorFatura(quantidade)));
		
		return cartaFatura;
	}

	private Double calculaValorFatura(String quantidade) {
		return Integer.valueOf(quantidade) * valorUnitario;
	}

	public Faturamento salvarOuAtualizarFaturamento(ResponseNotification response) throws Exception {
		
		String email = response.getPayer_email();
		Usuario usuario = null;
		usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		// criação de usuario à partir da compra do ingresso.
		// não deverar ser usado, porem para casos futuros!!!!
		if(usuario==null) {
			usuario = new Usuario();
			usuario.setNome(response.getFirst_name());
			usuario.setSobreNome(response.getLast_name());
			usuario.setEmail(response.getPayer_email());
			String senha = "senhaGenerica12345";
			usuario.setSenha(CriptyEncode.encodeSha256Hex(senha));
			usuario.setTipo("app");
			usuario.setCriacao(Timestamp.valueOf(DateNow.getDateNow()));
			usuario.setAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			usuarioRepo.save(usuario);
		}
		Faturamento fatura = buscaFatura(response.getTxn_id());
		
		if(fatura!=null) {
			fatura.setStatus(response.getPayment_status());
			fatura.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			preTicket(response, fatura.getId(), usuario.getId());
			return repo.save(fatura);
		}
		
		Faturamento novo = novaFatura(response, usuario);
		
		// fução para criação de ticket
		preTicket(response, novo.getId(), usuario.getId());
				
		senderMailService.send(email, "Obrigado por Compra seu ingresso pelo Torcedor Digital","Assim que confirmamos o pagamento estaremos enviado o seu ingresso.");
		
		return repo.save(novo);
	}

	/**
	 * Método responsavel pela criação de um ticket se 
	 * status da fatura for completed
	 * @param status
	 * @throws ParseException 
	 */
	private void preTicket(ResponseNotification response, Long idFatura, Long idUsuario) throws ParseException {
		
		switch (response.getPayment_status()) {
		case "Completed":
			criarTicket(response, idFatura, idUsuario);
			break;

		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	private void criarTicket(ResponseNotification response, Long idFatura, Long idUsuario) throws ParseException {
		//Calendario jogo = calendarioRepo.getOne(Long.valueOf(response.getCustom()));
		int q = Integer.valueOf(response.getQuantity());
		
		for(int i = 0; i <= q; i++){			
			Ingresso ticket = new Ingresso();
			ticket.setIdFatura(idFatura);
			ticket.setIdJogo(Long.valueOf(response.getCustom()));
			ticket.setIdUsuario(idUsuario);
			ticket.setStatus(true);
			ticket.setUrl("http://api.qrserver.com/v1/create-qr-code/?data=http://torcedordigital.com/api/pontuarIngresso?id=" + idUsuario + "&amp;size=300x500");
			
		}
		
		senderMailService.send(response.getPayer_email(), "Obrigado por Compra seu ingresso pelo Torcedor Digital","Seus ingressos já pode ser visualizados em seu aplicatico");
	}

	private Faturamento novaFatura(ResponseNotification response, Usuario usuario) {
		Faturamento cartaFatura = new Faturamento();
		cartaFatura.setIdUsuario(usuario.getId());
		cartaFatura.setIdTransacao(response.getTxn_id());
		cartaFatura.setIdJogo(Long.valueOf(response.getCustom()));
		cartaFatura.setDataCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		cartaFatura.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
		Integer quant = !Strings.isNullOrEmpty(response.getQuantity()) ? Integer.valueOf(response.getQuantity()): 0;
		cartaFatura.setQuantidade(quant);
		
		BigDecimal valorTotal = !Strings.isNullOrEmpty(response.getMc_gross()) ? new BigDecimal(response.getMc_gross()) : new BigDecimal("0.00");
		cartaFatura.setValorTotal(valorTotal);
		
			cartaFatura.setStatus(response.getPayment_status());
		return cartaFatura;
	}


}
