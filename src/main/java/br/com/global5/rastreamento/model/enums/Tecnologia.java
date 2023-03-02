package br.com.global5.rastreamento.model.enums;

import java.sql.Timestamp;
import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name = "rastreamento.enum_tecnologia")
public class Tecnologia implements BaseEntity {
	
		@Id
	    @SequenceGenerator(name="enum_sistema_enumoid_seq",
	            sequenceName="enum_sistema_enumoid_seq",
	            allocationSize=1)
	    @GeneratedValue(strategy = GenerationType.SEQUENCE,
	            generator="enum_sistema_enumoid_seq")
	    @Column(name = "enumoid")
	    private Integer id;
	    @Column(name = "enum_nome")
	    private String descricao;

	    @Column(name = "enum_dt_exclusao")
	    private Timestamp exclusao;

	    public Tecnologia(){}

	    public Tecnologia (Integer id, String descricao, Timestamp exclusao){
	        this.id = id;
	        this.descricao = descricao;
	        this.exclusao = exclusao;
	    }

	    public Tecnologia(Integer id) {
	        this.id = id;
	    }

	    public Tecnologia descricao(String descricao){
	        this.descricao = descricao;
	        return this;
	    }

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public Timestamp getExclusao() {
			return exclusao;
		}

		public void setExclusao(Timestamp exclusao) {
			this.exclusao = exclusao;
		}
	
}
