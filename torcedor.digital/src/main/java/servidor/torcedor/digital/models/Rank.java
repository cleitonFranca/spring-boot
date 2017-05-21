package servidor.torcedor.digital.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Rank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8346253329704339190L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@NotNull
	@Column(name="id_usuario")
	private Long idUsuario;
	
	@NotNull
	private Double pontos;
	
	@NotNull
	private Date atualizado;

	
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

	public Double getPontos() {
		return pontos;
	}

	public void setPontos(Double pontos) {
		this.pontos = pontos;
	}

	public Date getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(Date atualizado) {
		this.atualizado = atualizado;
	}

	@Override
	public String toString() {
		return "Rank [id=" + id + ", idUsuario=" + idUsuario + ", pontos=" + pontos + ", atualizado=" + atualizado
				+ "]";
	}
	
	
	
	
	
	

}
