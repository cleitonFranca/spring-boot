package servidor.torcedor.digital.utils;

import java.io.IOException;
import java.sql.Timestamp;

import servidor.torcedor.digital.models.Rank;

public class PontuaUsuario {
	
	public static Rank pontuar(Long idUsuario, Double pontos) throws IOException {
		
		Rank r = new Rank();
		r.setIdUsuario(idUsuario);
		r.setPontos(pontos);
		r.setAtualizado(Timestamp.valueOf(DateNow.getDateNow()));
		
		return r;
		
	}

}
