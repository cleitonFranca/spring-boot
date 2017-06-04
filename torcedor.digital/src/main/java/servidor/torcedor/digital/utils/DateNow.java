package servidor.torcedor.digital.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * Classe retorna a data atual com TimeZone = America/Sao_Paulo
 * 
 * @author cleiton
 *
 */
public class DateNow {
	
	/**
	 * Método tem como objetivo retorna a data atual
	 * Com TimeZone configurado para America/Sao_Paulo 
	 * @return
	 * @throws IOException 
	 */
	public static String getDateNow() throws IOException {
		
		NTPUDPClient client = new NTPUDPClient();
		client.open();
		InetAddress hostAddr = InetAddress.getByName("a.st1.ntp.br");
		TimeInfo info = client.getTime(hostAddr);
		info.computeDetails(); // compute offset/delay if not already done
		
		Date date = new Date(info.getReturnTime());
		
		// já que roda no aws esse ajuste para garantir a hora de Brasilia
		// se for rodado em ambiente local(Brasil) a data ficarar errada ****
		//DateUtils.addHours(date, -3);
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "br"));
		String strData = sd.format(date);
		
			
		return strData;	
	}

}
