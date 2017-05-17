package servidor.torcedor.digital.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import servidor.torcedor.digital.models.Usuario;

public class JsonTransform {
	
	/**
	 * Método recebe um Usuario e devolve em 
	 * forma de json
	 * 
	 * @param usuario
	 * @return
	 */
	public static String jsonUser(Usuario usuario) {
		String json = "";
		
		json = String.format(
				"{"
						+ "\"usuario\": "
							+ "{\"id\": %s, "
							+ "\"nome\": \"%s\", "
							+ "\"email\" : \"%s\", "
							+ "\"tipo\": \"%s\" }"
				+ "}", 
				usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTipo());
		
		return json;
	}
	
	public static String jsonError(HttpServletResponse res) {
		try {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.format("{\"error\" : \"%s\"}", HttpServletResponse.SC_NOT_FOUND);
	}

}
