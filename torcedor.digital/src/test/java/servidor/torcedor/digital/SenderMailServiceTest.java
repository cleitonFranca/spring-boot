package servidor.torcedor.digital;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import servidor.torcedor.digital.utils.SenderMailService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SenderMailServiceTest {

    @Autowired
    SenderMailService senderMailService;
    
    @Test
    public void testEnvioEmail() {
	      senderMailService.enviar();
    }
}
