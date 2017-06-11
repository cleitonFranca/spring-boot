package servidor.torcedor.digital;

import java.io.File;

import org.hibernate.tool.schema.internal.SchemaDropperImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import servidor.torcedor.digital.files.StorageProperties;
import servidor.torcedor.digital.files.StorageService;
import servidor.torcedor.digital.utils.SchedulerUtil;


/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
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
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
	
	
}
