package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import org.apache.velocity.app.Velocity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
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
	public void test_rank() {
		
		Rank r = new Rank();
		r.setIdUsuario(4L);
		r.setPontos(200.82236);
		r.setAtualizado(DateNow.getDateNow());
		
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
