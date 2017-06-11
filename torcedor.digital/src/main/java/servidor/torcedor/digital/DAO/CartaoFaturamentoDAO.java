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
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.CartaoFaturamentoRepository;
import servidor.torcedor.digital.utils.DateNow;
import servidor.torcedor.digital.utils.SenderMailService;
import servidor.torcedor.digital.models.CartaoFaturamento;

@Component
public class CartaoFaturamentoDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(CartaoFaturamentoDAO.class);
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private CartaoFaturamentoRepository repo;
	
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
	public List<CartaoFaturamento> buscaFaturaUsuario(Long idUsuario) {
		
		try {
			String sql = "SELECT * FROM cartao_faturamento WHERE id_usuario=:idUsuario";
			return 	em
					.createNativeQuery(sql, CartaoFaturamento.class)
					.setParameter("idUsuario", idUsuario)
					.getResultList();
					
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CartaoFaturamento> buscaFaturasConcluidas() {
		
		try {
			String sql = "SELECT * FROM cartao_faturamento WHERE status='concluido'";
			
			return 	em
					.createNativeQuery(sql, CartaoFaturamento.class)
					.getResultList();
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	public CartaoFaturamento salvarOuAtualizarCartaoFaturamento(Map<String, String> enderecoInfo) throws ParseException {
		
		String email = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("email")).findAny().get().getValue();
		String numeroCartao = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("numero_cartao")).findAny().get().getValue();
		String bandeira = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("bandeira")).findAny().get().getValue();
		String validade = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("validade")).findAny().get().getValue();
		String codigo = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("codigo")).findAny().get().getValue();
		String quantidade = enderecoInfo.entrySet().stream().filter(e -> e.getKey().equals("quantidade")).findAny().get().getValue();
		
		Usuario usuario = usuarioDAO.buscaUsuarioPorEmail(email);
		
		CartaoFaturamento novo = novaFatura(numeroCartao, bandeira, validade, codigo, quantidade, usuario);
		
		senderMailService.send(email, "Obrigado por Compra seu ingresso pelo Torcedor Digital","Assim que confirmamos o pagamento estaremos enviado o seu ingresso.");
		
		return repo.save(novo);
		
		
		
	}

	private CartaoFaturamento novaFatura(String numeroCartao, String bandeira, String validade, String codigo,
			String quantidade, Usuario usuario) throws ParseException {
		
		CartaoFaturamento cartaFatura = new CartaoFaturamento();
		cartaFatura.setIdUsuario(usuario.getId());
		cartaFatura.setNumero(numeroCartao);
		cartaFatura.setBandeira(bandeira);
		cartaFatura.setDataExp(DateNow.formatDate(validade));
		cartaFatura.setDataCriacao(Timestamp.valueOf(DateNow.getDateNow()));
		cartaFatura.setCodigoCCV(codigo);
		cartaFatura.setQuantidade(Integer.valueOf(quantidade));
		cartaFatura.setValorTotal(BigDecimal.valueOf(calculaValorFatura(quantidade)));
		
		return cartaFatura;
	}

	private Double calculaValorFatura(String quantidade) {
		return Integer.valueOf(quantidade) * valorUnitario;
	}


}
