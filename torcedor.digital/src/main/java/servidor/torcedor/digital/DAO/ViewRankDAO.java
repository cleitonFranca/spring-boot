package servidor.torcedor.digital.DAO;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.ViewRankGeral;

@Component
public class ViewRankDAO {
	
	@Autowired
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<ViewRankGeral> getRankGeral() {
		
		
		return this.em.createNativeQuery(""
				+ "select * from rank_geral", ViewRankGeral.class).getResultList();
		
	}

}
