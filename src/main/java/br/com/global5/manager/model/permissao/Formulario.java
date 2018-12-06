package br.com.global5.manager.model.permissao;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.hibernate.annotations.OrderBy;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;

@SqlResultSetMapping(
        name = "LstFormularioMapping",
        entities = @EntityResult(
                entityClass = Formulario.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "titulo", column = "titulo"),
                        @FieldResult(name = "tag", column = "tag"),
                        @FieldResult(name = "descricao", column = "descricao"),
                        @FieldResult(name = "ordemMenu", column = "ordemMenu"),
                        @FieldResult(name = "areaInterna", column = "areaInterna"),
                        @FieldResult(name = "areaMatriz", column = "areaMatriz"),
                        @FieldResult(name = "areaFilial", column = "areaFilial"),
                        @FieldResult(name = "idProduto", column = "idProduto"),
                        @FieldResult(name = "dtCriacao", column = "dtCriacao"),
                        @FieldResult(name = "dtExclusao", column = "dtExclusao"),
                        @FieldResult(name = "usuarioCriacao", column = "usuarioCriacao"),
                        @FieldResult(name = "usuarioExclusao", column = "usuarioExclusao"),
                        @FieldResult(name = "urlMenu", column = "urlMenu"),
                        @FieldResult(name = "nivelPai", column = "nivelPai"),
                        @FieldResult(name = "nivel", column = "nivel"),
                        @FieldResult(name = "indice", column = "indice"),
                        @FieldResult(name = "icone", column = "icone"),
                        @FieldResult(name = "nivelTipo", column = "nivelTipo")
                        
                }
        )
)

@Entity
@Table(name ="formulario")
public class Formulario implements BaseEntity {
	
	@Id
	@SequenceGenerator ( name = "formulario_formoid_seq", sequenceName = "formulario_formoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "formulario_formoid_seq")
	@Column(name = "formoid")  
	private Integer id;
	
	@Column(name = "form_titulo")
	private String titulo;
		
	@Column(name = "form_tag")
	private String tag;
	
	@Column(name = "form_descricao")      
	private String descricao;	
	
	@Column(name = "form_ordem_menu")
	private Integer ordemMenu;
	
	@Column(name = "form_interna")
	private boolean areaInterna;
	
	@Column(name = "form_area_matriz")
	private boolean areaMatriz;
	
	@Column(name = "form_area_filial")
	private boolean areaFilial;
	
	@Column(name = "form_prodoid")
	private Integer idProduto;
	
	@Column(name = "form_dt_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;
	
	@Column(name = "form_dt_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
	  
	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = Usuario.class,
				cascade = {CascadeType.DETACH, CascadeType.MERGE}
			)
	@JoinColumn(name = "form_usuoid_criacao")
	private Usuario usuarioCriacao;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
	@JoinColumn(name = "form_usuoid_exclusao")
	private Usuario usuarioExclusao;
	
	@Column(name = "form_url")
	private String urlMenu;
		
	@ManyToOne
	@JoinColumn(name="form_nivel_pai" , nullable = true , foreignKey = @ForeignKey(name = "formulario_form_nivel_pai_fkey"))
	private Formulario nivelPai;	

	@Column(name = "form_nivel")
	private Integer nivel;
	
	@Column(name = "form_indice")
	private String indice;
	
	@Column(name = "form_icone")
	private String icone;
	
	@Column(name = "form_nivel_tipo")
	private String nivelTipo;
	
	@OneToMany(mappedBy = "nivelPai")
	private List<Formulario> filhos;
	
	public Formulario(){}
	
	public Formulario(final Integer id, final Formulario nivelPai, final String titulo){
		this.id = id;
		this.nivelPai = nivelPai;
		this.titulo = titulo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdemMenu() {
		return ordemMenu;
	}

	public void setOrdemMenu(Integer ordemMenu) {
		this.ordemMenu = ordemMenu;
	}

	public boolean isAreaInterna() {
		return areaInterna;
	}

	public void setAreaInterna(boolean areaInterna) {
		this.areaInterna = areaInterna;
	}

	public boolean isAreaMatriz() {
		return areaMatriz;
	}

	public void setAreaMatriz(boolean areaMatriz) {
		this.areaMatriz = areaMatriz;
	}

	public boolean isAreaFilial() {
		return areaFilial;
	}

	public void setAreaFilial(boolean areaFilial) {
		this.areaFilial = areaFilial;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
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

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public Usuario getUsuarioExclusao() {
		return usuarioExclusao;
	}

	public void setUsuarioExclusao(Usuario usuarioExclusao) {
		this.usuarioExclusao = usuarioExclusao;
	}

	public String getUrlMenu() {
		return urlMenu;
	}

	public void setUrlMenu(String urlMenu) {
		this.urlMenu = urlMenu;
	}

	public Formulario getNivelPai() {
		return nivelPai;
	}

	public void setNivelPai(Formulario nivelPai) {
		this.nivelPai = nivelPai;
	}

	public List<Formulario> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<Formulario> filhos) {
		this.filhos = filhos;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getNivelTipo() {
		return nivelTipo;
	}

	public void setNivelTipo(String nivelTipo) {
		this.nivelTipo = nivelTipo;
	}			
	
	
	
}
