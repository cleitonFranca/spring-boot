package servidor.torcedor.digital.DAO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.ResponseNotification;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.FaturamentoRepository;
import servidor.torcedor.digital.utils.DateNow;
import servidor.torcedor.digital.utils.SenderMailService;
import servidor.torcedor.digital.models.Faturamento;

@Component
public class FaturamentoDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(FaturamentoDAO.class);
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private FaturamentoRepository repo;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
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
	@SuppressWarnings("unchecked")
	public Faturamento buscaFatura(String idFatura) {
		
		try {
			String sql = "SELECT * FROM faturamento WHERE id_usuario=:idFatura";
			return 	(Faturamento) em
					.createNativeQuery(sql, Faturamento.class)
					.setParameter("idFatura", idFatura).getSingleResult();
					
			
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

	public Faturamento salvarOuAtualizarFaturamento(ResponseNotification response) {
		
		String email = response.getPayer_email();
		Faturamento fatura = buscaFatura(response.getTxn_id());
		
		if(fatura!=null) {
			fatura.setStatus(response.getPayment_status());
			fatura.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
			criarTicket(response.getPayment_status());
			return repo.save(fatura);
		}
		
		
		Usuario usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		
		Faturamento novo = novaFatura(response, usuario);
		
		// fução para criação de ticket
		criarTicket(response.getPayment_status());
				
		senderMailService.send(email, "Obrigado por Compra seu ingresso pelo Torcedor Digital","Assim que confirmamos o pagamento estaremos enviado o seu ingresso.");
		
		return repo.save(novo);
	}

	/**
	 * Método responsavel pela criação de um ticket se 
	 * status da fatura for completed
	 * @param status
	 */
	private void criarTicket(String status) {
		System.out.println("ESTATUS DA FATURA:::::"+status);
	}

	private Faturamento novaFatura(ResponseNotification response, Usuario usuario) {
		Faturamento cartaFatura = new Faturamento();
		cartaFatura.setIdUsuario(usuario.getId());
		cartaFatura.setIdTransacao(Long.valueOf(response.getTxn_id()));
		cartaFatura.setIdJogo(Long.valueOf(response.getCustom()));
		cartaFatura.setDataCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		cartaFatura.setUltimaAtualizacao(Timestamp.valueOf(DateNow.getDateNow()));
		cartaFatura.setQuantidade(Integer.valueOf(response.getQuantity()));
		cartaFatura.setValorTotal(BigDecimal.valueOf(calculaValorFatura(response.getQuantity())));
		cartaFatura.setStatus(response.getPayment_status());
		return cartaFatura;
	}


}
