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
	
	/**
	 * Método para retorno de erro, recebe um modificador da resposta
	 * o tipo HTTP de error e a mensagem de erro.
	 * 
	 * @param res
	 * @param resposta
	 * @param msg
	 * @return
	 */
	public static String jsonError(HttpServletResponse res, int resposta, String msg) {
		try {
			// altera resposta do cabeçalho
			res.sendError(resposta, msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.format("{\"error\" : \"%s\", \"erroMsg\" : \"%s\"}", resposta, msg);
	}

}
