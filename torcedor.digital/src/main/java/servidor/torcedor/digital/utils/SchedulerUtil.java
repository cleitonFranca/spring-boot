package servidor.torcedor.digital.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerUtil.class);
	
	public static void init() {

		new Thread(() -> {
			
			try {
				
				MonitorCheckout m = new MonitorCheckout();
				m.start();
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			
			
		}).start();

	}

}
