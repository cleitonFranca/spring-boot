package servidor.torcedor.digital.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rank_semanal")
public class ViewRankSemanal implements Serializable{

	private static final long serialVersionUID = -712101774129134839L;
	
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="pontos")
	private double pontos;
	
	@Column(name="img")
	private String img;

	
	
	/**
	 *
	 * getters and setters
	 * 
	 */
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPontos() {
		return pontos;
	}

	public void setPontos(double pontos) {
		this.pontos = pontos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "ViewRankGeral [id=" + id + ", nome=" + nome + ", pontos=" + pontos + ", img=" + img + "]";
	}

	

	
	
	
	
	
	
	

}
