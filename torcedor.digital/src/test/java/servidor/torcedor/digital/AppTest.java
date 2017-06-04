package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import servidor.torcedor.digital.DAO.UsuarioDAO;
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
	private RankRepository rank;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private UsuarioDAO dao;
	
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

		Usuario usuario = (Usuario) this.em.createNativeQuery(""
				+ "select * from usuarios where "
				+ "email=:email and senha=:senha", Usuario.class)
				.setParameter("email", email)
				.setParameter("senha", senha).getSingleResult();
		
		System.out.println(usuario);
		
		assertTrue(usuario!=null);
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
	public void test_rank() throws IOException {
		
		Rank r = new Rank();
		r.setIdUsuario(1L);
		r.setPontos(200.2);
		//Timestamp.valueOf(DateNow.getDateNow().toString());
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
		
		
		List<ViewRankGeral> list = this.em.createNativeQuery(""
				+ "select * from rank_geral", ViewRankGeral.class).getResultList();
		
		Gson gson = new Gson();
		
		String json = gson.toJson(list);

		
		
		
		
		//[ViewRankGeral [id=4, nome=Cleiton, pontos=686.644348], ViewRankGeral [id=48, nome=Dilma França, pontos=100.0], ViewRankGeral [id=65, nome=Pedro, pontos=100.0]]
		
		// {ViewRankGeral:[{id:4, nome:Cleiton, pontos:686.64},{}]}
		
		System.out.println(list);
		System.out.println(json);
		
	}
	

}
