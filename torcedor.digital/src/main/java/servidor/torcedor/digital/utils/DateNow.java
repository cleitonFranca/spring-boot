package servidor.torcedor.digital.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;

/**
 * Classe retorna a data atual com TimeZone = America/Sao_Paulo
 * 
 * @author cleiton
 *
 */
public class DateNow {
	
	/**
	 * Método tem como objetivo retorna a data atual
	 * só usado para setar campos pelo servidor campos setados 
	 * pelos usuario não poderam usar esse método!!!!
	 * Com TimeZone configurado para America/Sao_Paulo 
	 * @return
	 * @throws IOException 
	 */
	public static String getDateNow() {
		
	/*	NTPUDPClient client = new NTPUDPClient();
		client.open();
		InetAddress hostAddr = InetAddress.getByName("a.st1.ntp.br");
		TimeInfo info = client.getTime(hostAddr);
		info.computeDetails(); // compute offset/delay if not already done
		*/
		Date date = new Date();
		
		// já que roda no aws esse ajuste para garantir a hora de Brasilia
		// se for rodado em ambiente local(Brasil) a data ficarar errada ****
		Date dataAjustada = DateUtils.addHours(date, -3);
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "br"));
		String strData = sd.format(dataAjustada);
		
			
		return strData;	
	}
	
	public static Timestamp formatDate(String data) throws ParseException {
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "br"));
		Date d = new Date(sd.parse(data).getTime());
		String strData = sd.format(d);
		
		return Timestamp.valueOf(strData);
		
	}

}
