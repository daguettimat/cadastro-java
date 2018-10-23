package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_usuario")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TipoUsuario implements BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "tipusuoid")
  private Integer id;

  @Column(name = "tipusuario")
  private String usuario;

  @Column(name = "tipdescricao")
  private String descricao;


  public TipoUsuario() {}

  public TipoUsuario(String usuario, String descricao) {
    this.usuario = usuario;
    this.descricao = descricao;
  }

  public boolean hasDescricao() {
    return descricao != null && !"".equals(descricao.trim());
  }

  public TipoUsuario(Integer id) {
    this.id = id;
  }

  public TipoUsuario descricao(String descricao) {
    this.descricao = descricao;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
