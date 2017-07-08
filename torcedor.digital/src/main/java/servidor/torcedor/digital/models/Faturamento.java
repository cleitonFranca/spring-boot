package servidor.torcedor.digital.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Faturamento implements Serializable {

	private static final long serialVersionUID = 4733099478667887786L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@Column(name = "id_usuario")
	private Long idUsuario;

	@NotNull
	@Column(name = "id_jogo")
	private Long idJogo;

	@Column(name = "id_transacao")
	private String idTransacao;

	@Column(name = "data_criacao")
	private Timestamp dataCriacao;
	@NotNull
	private Integer quantidade;
	@NotNull
	@Column(name = "valor_total")
	private BigDecimal valorTotal;
	@Column(name = "ultima_atualizacao")
	private Timestamp ultimaAtualizacao;

	private String status;

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

	public Timestamp getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Timestamp dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Timestamp getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Timestamp ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Long getIdJogo() {
		return idJogo;
	}

	public void setIdJogo(Long idJogo) {
		this.idJogo = idJogo;
	}

	

	public String getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(String idTransacao) {
		this.idTransacao = idTransacao;
	}

	@Override
	public String toString() {
		return "Faturamento [id=" + id + ", idUsuario=" + idUsuario + ", idJogo=" + idJogo + ", idTransacao="
				+ idTransacao + ", dataCriacao=" + dataCriacao + ", quantidade=" + quantidade + ", valorTotal="
				+ valorTotal + ", ultimaAtualizacao=" + ultimaAtualizacao + ", status=" + status + "]";
	}

}
