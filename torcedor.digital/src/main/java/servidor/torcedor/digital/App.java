package servidor.torcedor.digital;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

import servidor.torcedor.digital.files.StorageProperties;
import servidor.torcedor.digital.files.StorageService;


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
		springApplication.run(args);
		
		
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
	
	
}
