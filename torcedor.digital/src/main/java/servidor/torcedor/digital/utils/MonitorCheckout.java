package servidor.torcedor.digital.utils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servidor.torcedor.digital.jdbc.FaturaJDBC;
import servidor.torcedor.digital.models.CartaoFaturamento;


public class MonitorCheckout {
	private static final Logger logger = LoggerFactory.getLogger(MonitorCheckout.class);	
	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);
	
	
	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					FaturaJDBC jdbc = new FaturaJDBC();
					List<CartaoFaturamento> lista = jdbc.listFaturamento();
					
					System.out.print("quatidade :"+lista.size()+", ");
					System.out.println(lista);
					
					
					
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}, 3, 20, TimeUnit.SECONDS);

	}

}
