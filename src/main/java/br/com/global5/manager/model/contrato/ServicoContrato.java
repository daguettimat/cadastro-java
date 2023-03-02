package br.com.global5.manager.model.contrato;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "java.servico" )
public class ServicoContrato implements BaseEntity{

    @Id
    @SequenceGenerator(name = "servico_servoid_seq", sequenceName = "servico_servoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "servico_servoid_seq")
    @Column(name ="servoid")
    private Integer id;

    @Column(name="serv_nome")
    private String nome;
  
    @Column(name="serv_aut_tabela")
    private String tabela;
    
    @Column(name="serv_aut_campo")
    private String campo;
    
    @Column(name ="serv_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;

    @Column(name ="serv_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;

    @Column(name ="serv_dt_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="serv_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="serv_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="serv_usuoid_exclusao")
    private Usuario usuExclusao;
    
    public ServicoContrato(){}
    
    public ServicoContrato(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public Usuario getUsuCriacao() {
		return usuCriacao;
	}

	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}

	public Usuario getUsuAlteracao() {
		return usuAlteracao;
	}

	public void setUsuAlteracao(Usuario usuAlteracao) {
		this.usuAlteracao = usuAlteracao;
	}

	public Usuario getUsuExclusao() {
		return usuExclusao;
	}

	public void setUsuExclusao(Usuario usuExclusao) {
		this.usuExclusao = usuExclusao;
	}
        
}
