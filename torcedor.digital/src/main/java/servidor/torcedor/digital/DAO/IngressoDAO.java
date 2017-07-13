package servidor.torcedor.digital.DAO;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.Ingresso;

@Component
public class IngressoDAO {
	
private static final Logger logger = LoggerFactory.getLogger(IngressoDAO.class);
	
	@Autowired
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<Ingresso> buscaIngressosByIdUsuario(Integer idUsuario) {
		
		try {			
			return em.createNativeQuery("select * from ingressos i where i.id_usuario=:idUsuario", Ingresso.class)
					.setParameter("idUsuario", idUsuario)
					.getResultList();
		} catch (Exception e) {
			logger.error(String.format("Ingresso não encontrado: %s", e.getMessage()));
		}
		return null;
		
		
		
	}

}
