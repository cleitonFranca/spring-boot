package servidor.torcedor.digital.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertieResources {
	
	
	public static Properties getResources() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Properties prop = new Properties();
		InputStream resources = classLoader.getResourceAsStream("application.properties");
		
		try {
			prop.load(resources);
			resources.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop;
		
	}

}
