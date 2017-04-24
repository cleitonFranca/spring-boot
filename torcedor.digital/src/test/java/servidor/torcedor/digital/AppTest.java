package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.repositories.UsuarioRepository;
import servidor.torcedor.digital.utils.CriptyEncode;


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
	private EntityManager em;
	
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
	

}
