package servidor.torcedor.digital;

import org.junit.Test;

import servidor.torcedor.digital.utils.PropertieResources;

public class TestGetProperties {
	
	
	
	@Test
	public void get_resources() {
	
		String url = PropertieResources.getResources().getProperty("spring.datasource.url");
		
		System.out.println(url);
		
		
		
		
	}

}
