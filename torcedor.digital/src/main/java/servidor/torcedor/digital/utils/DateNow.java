package servidor.torcedor.digital.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
	 */
	public static Date getDateNow() {
		Calendar saoPauloDate = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
		return saoPauloDate.getTime();
	}

}
