package servidor.torcedor.digital.models;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "imagens")
public class Imagem implements Serializable {
	private static final long serialVersionUID = -8596663322017959495L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@Column(name="id_usuario")
	private Long idUsuario;
	
	private byte[] arquivo;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] bytes) {
		this.arquivo = bytes;
	}

	@Override
	public String toString() {
		return "Imagem [id=" + id + ", idUsuario=" + idUsuario + ", arquivo=" + arquivo + "]";
	}
	
	
	

}
