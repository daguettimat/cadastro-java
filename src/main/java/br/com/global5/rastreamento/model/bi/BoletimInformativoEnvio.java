package br.com.global5.rastreamento.model.bi;

import java.util.Date;

import javax.persistence.*;


import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name="boletim_informativo_envio")
public class BoletimInformativoEnvio implements BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name = "boletim_informativo_envio_bieoid_seq", sequenceName = "boletim_informativo_envio_bieoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boletim_informativo_envio_bieoid_seq")
	@Column(name="bieoid")
	private Integer id;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = BoletimInformativo.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
	)
	@JoinColumn(name="bie_bioid")
	private BoletimInformativo boletimInformativo;

	@Column(name="bie_emails")
	private String emails;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bie_dt_envio")
	private Date dtEnvio;

	public BoletimInformativoEnvio(){}
	
	public BoletimInformativoEnvio(Integer id){this.id = id ;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BoletimInformativo getBoletimInformativo() {
		return boletimInformativo;
	}

	public void setBoletimInformativo(BoletimInformativo boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public Date getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(Date dtEnvio) {
		this.dtEnvio = dtEnvio;
	}
	
}
