package servidor.torcedor.digital.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Endereco implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5140880104836344116L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@Column(name="id_usuario")
	private Long idUsuario;
	@NotNull
	private String cep;
	
	private String estado;
	
	private String cidade;
	
	private String bairro;
	
	private String logradouro;
	
	private String obs;
	@NotNull
	private String numero;
	
	private Timestamp criacao;
	
	@Column(name="ultima_atualizacao")
	private Timestamp ultimaAtualizacao;

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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Timestamp getCriacao() {
		return criacao;
	}

	public void setCriacao(Timestamp criacao) {
		this.criacao = criacao;
	}

	public Timestamp getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Timestamp ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	@Override
	public String toString() {
		return "Endereco [id=" + id + ", idUsuario=" + idUsuario + ", cep=" + cep + ", estado=" + estado + ", cidade="
				+ cidade + ", bairro=" + bairro + ", logradouro=" + logradouro + ", obs=" + obs + ", numero=" + numero
				+ ", criacao=" + criacao + ", ultimaAtualizacao=" + ultimaAtualizacao + "]";
	}
	
	
	

}
