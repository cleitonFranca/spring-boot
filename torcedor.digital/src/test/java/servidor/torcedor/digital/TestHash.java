package servidor.torcedor.digital;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Strings;

import servidor.torcedor.digital.utils.CriptyEncode;

public class TestHash {
	
	@Test
	public void test_hash() throws Exception {
		String encode = CriptyEncode.encodeSha256Hex("123456");
		//8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
		System.out.println(encode);
		assertTrue(!Strings.isNullOrEmpty(encode));
	}
	
	/**
	 * Espara ""
	 * @throws Exception 
	 * 
	 */
	@Test
	public void test_hash_fail() throws Exception {
		String encode = CriptyEncode.encodeSha256Hex("");
		assertTrue(Strings.isNullOrEmpty(encode));
	}
	
	
	/**
	 * Espera null
	 * @throws Exception 
	 */
	@Test
	public void test_hash_fail2() throws Exception {
		String encode = CriptyEncode.encodeSha256Hex(null);
		assertTrue(Strings.isNullOrEmpty(encode));
	}

}
