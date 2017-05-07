package servidor.torcedor.digital.DAO;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import servidor.torcedor.digital.models.Usuario;
import servidor.torcedor.digital.utils.CriptyEncode;

@Component
public class UsuarioDAO {
	private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);
	
	private EntityManager em;
	
	@Autowired
	public UsuarioDAO(EntityManager entity) {
		this.em = entity;
	}
	
	public Usuario buscaUsuarioPorEmailSenha(Usuario u) {

		try {
			return (Usuario) em
					.createNativeQuery("SELECT * FROM usuarios WHERE email=:email and senha=:senha", Usuario.class)
					.setParameter("email", u.getEmail())
					.setParameter("senha", CriptyEncode.encodeSha256Hex(u.getSenha()))
					.getSingleResult();

		} catch (Exception ex) {
			logger.error(ex.getMessage());

			return null;
		}
	}

	public Usuario recuperarSenha(Usuario u) {
		
		try {
			return (Usuario) em
					.createNativeQuery("SELECT * FROM usuarios WHERE email=:email", Usuario.class)
					.setParameter("email", u.getEmail())
					//.setParameter("senha", CriptyEncode.encodeSha256Hex(u.getSenha()))
					.getSingleResult();

		} catch (Exception ex) {
			logger.error(ex.getMessage());

			return null;
		}
	}

}
