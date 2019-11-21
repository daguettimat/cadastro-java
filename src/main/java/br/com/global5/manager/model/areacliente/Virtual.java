package br.com.global5.manager.model.areacliente;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;




@Entity
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "usuario_mensal_monitoramento",
            procedureName = "usuario_mensal_monitoramento",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    ),
    @NamedStoredProcedureQuery(
            name = "usuario_mensal_cadastro",
            procedureName = "usuario_mensal_cadastro",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    ),
    @NamedStoredProcedureQuery(
            name = "usuario_detalhe_cadastro",
            procedureName = "usuario_detalhe_cadastro",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    ),
    @NamedStoredProcedureQuery(
            name = "usuario_detalhe_viagens",
            procedureName = "usuario_detalhe_viagens",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    ),
    @NamedStoredProcedureQuery(
            name = "usuario_detalhe_checklists",
            procedureName = "usuario_detalhe_checklists",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    ) ,
    @NamedStoredProcedureQuery(
            name = "usuario_detalhe_autotrac",
            procedureName = "usuario_detalhe_autotrac",
            parameters = {
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Date.class, name = "dt_mes"),
                    @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_area")

            }
    )     
}
)
public class Virtual implements BaseEntity {
	
	 @Id
	 @Column(name="apv_registro")
	 private Integer id;
	 
	 @Column(name="apv_trdboid")
	 private Integer transacao;
	 
	 @Column(name="apv_modulo")
	 private String  modulo;
	 
	 @Column(name="apv_idregistro")
	 private String  idRegistro;
	 
	 @Column(name="apv_descricao")
	 private String  descricao;
	 
	 public Virtual(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTransacao() {
		return transacao;
	}

	public void setTransacao(Integer transacao) {
		this.transacao = transacao;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	 
}
