package servidor.torcedor.digital;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.validation.constraints.AssertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import junit.framework.Assert;
import servidor.torcedor.digital.DAO.CartaoFaturamentoDAO;
import servidor.torcedor.digital.DAO.EnderecoDAO;
import servidor.torcedor.digital.DAO.UsuarioDAO;
import servidor.torcedor.digital.jdbc.FaturaJDBC;
import servidor.torcedor.digital.jdbc.SimpleJdbcConection;
import servidor.torcedor.digital.models.CartaoFaturamento;
import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.Rank;
import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.models.ViewRankGeral;
import servidor.torcedor.digital.repositories.RankRepository;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;
import servidor.torcedor.digital.utils.DateNow;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
public class AppTest {

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private EnderecoDAO enderecoDAO;

	@Autowired
	private CartaoFaturamentoDAO cartaFatura;

	@Autowired
	private RankRepository rank;

	@Autowired
	private EntityManager em;

	@Autowired
	private UsuarioDAO dao;
	
	private Connection connect;

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://localhost:5432/torcedordigital";

	// Database credentials
	static final String USER = "postgres";
	static final String PASS = "postgres";

	/**
	 * teste para saber se o repositorio está funcionando
	 */
	@Test
	public void testRepo() {
		System.out.println(this.repo.findAll());
		assertTrue(this.repo.findAll().size() > 0);
	}

	@Test
	public void test_login() throws Exception {
		String email = "cleiton@teste.com";
		String senha = CriptyEncode.encodeSha256Hex("123456");

		Usuario usuario = (Usuario) this.em
				.createNativeQuery("" + "select * from usuarios where " + "email=:email and senha=:senha",
						Usuario.class)
				.setParameter("email", email).setParameter("senha", senha).getSingleResult();

		System.out.println(usuario);

		assertTrue(usuario != null);
	}

	@Test
	public void test_usuarioDAO() {

		Usuario u = new Usuario();
		u.setEmail("cleiton@teste.com");
		u.setSenha("123456");

		Usuario usuario = dao.buscaUsuarioPorEmailSenha(u);

		System.out.println(usuario);
	}
	
	@Test
	public void test_atualizando_usuario() {

		Usuario u = new Usuario();
		u.setEmail("cleiton@teste.com");

		Usuario usuario = dao.buscaUsuarioPorEmailSenha(u);

		System.out.println(usuario);
	}

	@Test
	public void test_rank() throws IOException {

		Rank r = new Rank();
		r.setIdUsuario(1L);
		r.setPontos(200.2);
		// Timestamp.valueOf(DateNow.getDateNow().toString());
		r.setAtualizado(Timestamp.valueOf(DateNow.getDateNow()));

		try {
			rank.save(r);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_List_rank_geral() {

		List<ViewRankGeral> list = this.em.createNativeQuery("" + "select * from rank_geral", ViewRankGeral.class)
				.getResultList();

		Gson gson = new Gson();

		String json = gson.toJson(list);

		// [ViewRankGeral [id=4, nome=Cleiton, pontos=686.644348], ViewRankGeral
		// [id=48, nome=Dilma França, pontos=100.0], ViewRankGeral [id=65,
		// nome=Pedro, pontos=100.0]]

		// {ViewRankGeral:[{id:4, nome:Cleiton, pontos:686.64},{}]}

		System.out.println(list);
		System.out.println(json);

	}

	@Test
	public void test_busca_endereco_usurario() {
		Endereco endereco = enderecoDAO.buscaEnderecoUsuario(1L);

		System.out.println(endereco);

	}

	@Test
	public void test_criacao_endereco_usuario() {
		Map<String, String> map = Maps.newHashMap();

		map.put("email", "cleiton@teste.com");
		map.put("estado", "RN");
		map.put("cidade", "Natal");
		map.put("bairro", "Candelaria");
		map.put("cep", "59150680");
		map.put("logradouro", "Rua teste");
		map.put("numero", "256");
		map.put("complemento", "teste");

		Endereco endereco = enderecoDAO.salvarOuAtualizarEndereco(map);

		System.out.println(endereco);
		assertTrue(!java.util.Objects.isNull(endereco));

	}

	@Test
	public void test_criacao_cartao_fatura() throws ParseException {
		Map<String, String> map = Maps.newHashMap();

		map.put("email", "cleiton@teste.com");
		map.put("numero_cartao", "16359866654885");
		map.put("bandeira", "visa");
		map.put("validade", "2018-11-10 00:00:00");
		map.put("codigo", "369");
		map.put("quantidade", "2");
		map.put("data_criacao", "2017-06-10 00:00:00");

		CartaoFaturamento faturamento = cartaFatura.salvarOuAtualizarCartaoFaturamento(map);

		System.out.println(faturamento);
		assertTrue(!java.util.Objects.isNull(faturamento));

	}

	@Test
	public void test_buscaFaturasConcluidas() {

		List<CartaoFaturamento> teste = cartaFatura.buscaFaturasConcluidas();

		System.out.println(teste);

	}

	@Test
	public void test_jdbcSingleConection() {
		FaturaJDBC jdbc = new FaturaJDBC();
		List<CartaoFaturamento> lista = jdbc.listFaturamento();
		System.out.print("quatidade :"+lista.size());
		System.out.println(lista);
	}
	
	

}
