package br.com.global5.rastreamento.model.trafegus;

import java.util.Date;
import javax.persistence.*;
import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name = "transacao")
public class Transacao  implements BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name = "transacao_trdboid_seq", sequenceName = "transacao_trdboid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transacao_trdboid_seq")
	@Column(name = "trdboid")
	private Integer id;
	
	@Column(name ="trdb_modulo")	
	private String modulo;
	
	@Column(name ="trdb_dt_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInicio;
	
	@Column(name ="trdb_dt_fim")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFim;
	
	@Column(name ="trdb_dt_cancelamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCancelamento;
	
	@Column(name = "trdb_detalhe")
	private String detalhe;
	
	public Transacao(){}
	
	public Transacao(Integer id){this.id = id;}

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
