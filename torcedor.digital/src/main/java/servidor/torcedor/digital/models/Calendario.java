package servidor.torcedor.digital.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Calendario implements Serializable {
	

	private static final long serialVersionUID = 812720288942676488L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="time_casa")
	private String timeCasa;
	@Column(name="time_visitante")
	private String timeVisitante;
	@Column(name="img_casa")
	private String imgCasa;
	@Column(name="img_visitante")
	private String imgVisitante;
	@Column(name="data_inicio")
	private Timestamp dataInicio;
	@Column(name="data_fim")
	private Timestamp dataFim;
	@Column(name="resultado_time_casa")
	private String resultadoTimeCasa;
	@Column(name="resultado_time_visitante")
	private String resultadoTimeVisitante;
	@Column(name="resultado_penalti_casa")
	private String resultatoPenaltiCasa;
	@Column(name="resultado_penalti_visitante")
	private String resultadoPenaltiVisitante;
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTimeCasa() {
		return timeCasa;
	}
	public void setTimeCasa(String timeCasa) {
		this.timeCasa = timeCasa;
	}
	public String getTimeVisitante() {
		return timeVisitante;
	}
	public void setTimeVisitante(String timeVisitante) {
		this.timeVisitante = timeVisitante;
	}
	public String getImgCasa() {
		return imgCasa;
	}
	public void setImgCasa(String imgCasa) {
		this.imgCasa = imgCasa;
	}
	public String getImgVisitante() {
		return imgVisitante;
	}
	public void setImgVisitante(String imgVisitante) {
		this.imgVisitante = imgVisitante;
	}
	
	public String getDataInicio() {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyy hh:mm");
		return data.format(dataInicio);
	}
	public void setDataInicio(Timestamp dataInicio) {
		this.dataInicio = dataInicio;
	}
	public String getDataFim() {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyy hh:mm");
		return data.format(dataFim);
	}
	public void setDataFim(Timestamp dataFim) {
		this.dataFim = dataFim;
	}
	public String getResultadoTimeCasa() {
		return resultadoTimeCasa;
	}
	public void setResultadoTimeCasa(String resultadoTimeCasa) {
		this.resultadoTimeCasa = resultadoTimeCasa;
	}
	public String getResultadoTimeVisitante() {
		return resultadoTimeVisitante;
	}
	public void setResultadoTimeVisitante(String resultadoTimeVisitante) {
		this.resultadoTimeVisitante = resultadoTimeVisitante;
	}
	public String getResultatoPenaltiCasa() {
		return resultatoPenaltiCasa;
	}
	public void setResultatoPenaltiCasa(String resultatoPenaltiCasa) {
		this.resultatoPenaltiCasa = resultatoPenaltiCasa;
	}
	public String getResultadoPenaltiVisitante() {
		return resultadoPenaltiVisitante;
	}
	public void setResultadoPenaltiVisitante(String resultadoPenaltiVisitante) {
		this.resultadoPenaltiVisitante = resultadoPenaltiVisitante;
	}
	@Override
	public String toString() {
		return "Calendario [id=" + id + ", timeCasa=" + timeCasa + ", timeVisitante=" + timeVisitante + ", imgCasa="
				+ imgCasa + ", imgVisitante=" + imgVisitante + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
				+ ", resultadoTimeCasa=" + resultadoTimeCasa + ", resultadoTimeVisitante=" + resultadoTimeVisitante
				+ ", resultatoPenaltiCasa=" + resultatoPenaltiCasa + ", resultadoPenaltiVisitante="
				+ resultadoPenaltiVisitante + "]";
	}
	
	
	
	
	
	
	

}
