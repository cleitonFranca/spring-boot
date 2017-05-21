package servidor.torcedor.digital.utils;

import servidor.torcedor.digital.models.Rank;

public class PontuaUsuario {
	
	public static Rank pontuar(Long idUsuario, Double pontos) {
		
		Rank r = new Rank();
		r.setIdUsuario(idUsuario);
		r.setPontos(pontos);
		r.setAtualizado(DateNow.getDateNow());
		
		return r;
		
	}

}
