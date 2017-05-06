package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

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

}
