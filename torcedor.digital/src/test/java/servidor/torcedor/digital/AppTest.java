package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import servidor.torcedor.digital.repositories.UsuarioRepository;


/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
public class AppTest {
	
	@Autowired
	private UsuarioRepository repo;
	
	/**
	 * teste para saber se o repositorio está funcionando
	 */
	@Test
	public void testRepo() {
		System.out.println(this.repo.findAll());
		assertTrue(this.repo.findAll().size() > 0);
	}
	
	@Test
	public void test() {
		int a = 4;
		int b = 3;
		assertTrue(a > b);

	}
	

}
