package servidor.torcedor.digital.DAO;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.ViewRankGeral;
import servidor.torcedor.digital.models.ViewRankMensal;
import servidor.torcedor.digital.models.ViewRankSemanal;

@Component
public class ViewRankDAO {
	
	@Autowired
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<ViewRankGeral> getRankGeral() {
		
		
		return this.em.createNativeQuery(""
				+ "select * from rank_geral", ViewRankGeral.class).getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewRankSemanal> getRankSemanal() {
		
		
		return this.em.createNativeQuery(""
				+ "select * from rank_semanal", ViewRankSemanal.class).getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewRankMensal> getRankMensal() {
		
		
		return this.em.createNativeQuery(""
				+ "select * from rank_mensal", ViewRankMensal.class).getResultList();
		
	}

}
