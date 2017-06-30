package servidor.torcedor.digital;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class App {
	public static void main(String[] args) {
		
		
		
		SpringApplication springApplication = new SpringApplication(App.class);
		springApplication.addListeners(new ApplicationPidFileWriter(new File("/home/cleiton/PID")));
		
		ConfigurableApplicationContext contexto = springApplication.run(args);
		
		if(contexto.isRunning()) {
			// monitoramneto da aplicação
			//SchedulerUtil.init();
		}
		
		
	}
	
	
	
}
