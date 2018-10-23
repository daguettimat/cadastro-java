package br.com.global5.manager.model.geral;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import br.com.global5.infra.model.BaseEntity;

@SqlResultSetMapping(
        name = "UsuarioAreaMapping",
        entities = @EntityResult(
                entityClass = UsuarioArea.class,
                fields = {
                		@FieldResult(name = "id" , column = "id"),
                		@FieldResult(name = "areaoid" , column = "areaoid"),
                		@FieldResult(name = "area_areaoid_pai" , column = "area_areaoid_pai"),
                		@FieldResult(name = "area_anvloid" , column = "area_anvloid")
                }
            )
        )

@Entity
public class UsuarioArea implements BaseEntity{
	
	@Id
	private Integer id;
	private Integer areaoid;
	private Integer area_areaoid_pai;
	private Integer area_anvloid;
	
	public UsuarioArea(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAreaoid() {
		return areaoid;
	}

	public void setAreaoid(Integer areaoid) {
		this.areaoid = areaoid;
	}

	public Integer getArea_areaoid_pai() {
		return area_areaoid_pai;
	}

	public void setArea_areaoid_pai(Integer area_areaoid_pai) {
		this.area_areaoid_pai = area_areaoid_pai;
	}

	public Integer getArea_anvloid() {
		return area_anvloid;
	}

	public void setArea_anvloid(Integer area_anvloid) {
		this.area_anvloid = area_anvloid;
	};
	
	
}
