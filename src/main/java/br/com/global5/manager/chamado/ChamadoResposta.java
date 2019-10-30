package br.com.global5.manager.chamado;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name="chamado_respostas")
public class ChamadoResposta implements BaseEntity{
	
	@Id
    @SequenceGenerator(name = "chamado_respostas_chamroid_seq", sequenceName = "chamado_respostas_chamroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "chamado_respostas_chamroid_seq")
	@Column(name = "chamroid")
	private Integer id;

	@Column(name="chamr_arquivo_enviado")
	private String arquivoEnviado;

	@Column(name="chamr_dt_mensagem")
	private Date dtMensagem;

	@Column(name="chamr_mensagem")
	private String mensagem;
	
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Chamado.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    @JoinColumn(name ="chamr_chamoid")
	private Chamado chamado;

	@Column(name="chamr_usuoid_interno")
	private Boolean usuInterno;

	@ManyToOne(
	            fetch = FetchType.EAGER,
	            targetEntity = Usuario.class,
	            cascade = {CascadeType.DETACH, CascadeType.MERGE}
	    )
	@JoinColumn(name ="chamr_usuoid")
	private Usuario usuario;
	

	public ChamadoResposta() {}
	
	public ChamadoResposta(Integer id) {
		this.id = id;
	}
	
	public ChamadoResposta(Integer id, String mensagem) {
		this.id = id;
		this.mensagem = mensagem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArquivoEnviado() {
		return arquivoEnviado;
	}

	public void setArquivoEnviado(String arquivoEnviado) {
		this.arquivoEnviado = arquivoEnviado;
	}

	public Date getDtMensagem() {
		return dtMensagem;
	}

	public void setDtMensagem(Date dtMensagem) {
		this.dtMensagem = dtMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Chamado getChamado() {
		return chamado;
	}

	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}

	public Boolean getUsuInterno() {
		return usuInterno;
	}

	public void setUsuInterno(Boolean usuInterno) {
		this.usuInterno = usuInterno;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
}
