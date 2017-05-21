package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.google.common.base.Strings;

import servidor.torcedor.digital.utils.CriptyEncode;

public class TestHash {
	
	@Test
	public void test_hash() throws Exception {
		String encode = CriptyEncode.encodeSha256Hex("123456");
		assertTrue(!Strings.isNullOrEmpty(encode));
		assertTrue(encode.equals("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"));
	}
	
	/**
	 * Espara ""
	 * @throws Exception 
	 * 
	 */
	@Test
	public void test_hash_fail() {
		String encode = "";
		try {
			encode = CriptyEncode.encodeSha256Hex("");
		} catch (Exception e) {e.printStackTrace();}
		assertTrue(Strings.isNullOrEmpty(encode));
	}
	
	
	/**
	 * Espera null
	 * @throws Exception 
	 */
	@Test
	public void test_hash_fail2() throws Exception {
		String encode = null;
		try {
			encode = CriptyEncode.encodeSha256Hex(null);			
		} catch (Exception e) {e.printStackTrace();}
		
		assertTrue(Strings.isNullOrEmpty(encode));
	}
	
	@Test
	public void test_datas() {
		Calendar systemDate = Calendar.getInstance();
		Calendar saoPauloDate = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
		
		Date nova = saoPauloDate.getTime();
		
		System.out.println("====> "+ nova);
		
		Calendar brazilEastDate = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
		System.out.println("Sem Timezone: " + TestHash.getFormatedDate(systemDate));
		System.out.println("America/São_Paulo: " + TestHash.getFormatedDate(saoPauloDate));
		System.out.println("Brazil/East: " + TestHash.getFormatedDate(brazilEastDate));
	}
	
	private static String getFormatedDate(Calendar date) {
		StringBuffer formattedDate = new StringBuffer();
		formattedDate.append(date.get(Calendar.DAY_OF_MONTH)).append("/");
		formattedDate.append(date.get(Calendar.MONTH) + 1).append("/");
		formattedDate.append(date.get(Calendar.YEAR)).append(" ");
		formattedDate.append(date.get(Calendar.HOUR_OF_DAY)).append(":");
		formattedDate.append(date.get(Calendar.MINUTE));
		return formattedDate.toString();
	}

}
