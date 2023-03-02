package br.com.global5.manager.model.gerencial;

import java.util.Date;
import javax.persistence.*;
import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name = "java.transacao_gerencial")
public class TransacaoGerencial  implements BaseEntity{

	@Id
    @SequenceGenerator(name = "transacao_gerencial_trdgoid_seq", sequenceName = "transacao_gerencial_trdgoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transacao_gerencial_trdgoid_seq")
	@Column(name = "trdgoid")
	private Integer id;
	
	@Column(name ="trdg_modulo")	
	private String modulo;
	
	@Column(name ="trdg_dt_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInicio;
	
	@Column(name ="trdg_dt_fim")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFim;
	
	@Column(name ="trdg_dt_cancelamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCancelamento;
	
	@Column(name = "trdg_detalhe")
	private String detalhe;
	
	public TransacaoGerencial(){}
	
	public TransacaoGerencial(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}

	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}

	public String getDetalhe() {
		return detalhe;
	}

	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}
	
}
