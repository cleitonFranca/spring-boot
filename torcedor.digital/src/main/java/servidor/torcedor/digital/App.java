package servidor.torcedor.digital;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;


/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(App.class);
		springApplication.addListeners(new ApplicationPidFileWriter(new File("/home/cleiton/PID")));
		springApplication.run(args);
		
		
	}
}
