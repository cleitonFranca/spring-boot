package servidor.torcedor.digital.utils;

import java.nio.charset.StandardCharsets;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;

public class CriptyEncode {

	public static String encodeSha256Hex(String string) throws Exception {

		if (Strings.isNullOrEmpty(string)) {
			throw new Exception("Foi passado um parâmetro inválido!");
		}
		return Hashing
				.sha256()
				.hashString(string, StandardCharsets.UTF_8)
				.toString();
	}
	
}
