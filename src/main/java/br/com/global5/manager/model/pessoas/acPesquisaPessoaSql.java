package br.com.global5.manager.model.pessoas;



import javax.persistence.*;

@SqlResultSetMapping(
        name = "ListaPesquisaPessoa",
        entities = @EntityResult(
                entityClass = acPesquisaPessoaSql.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "valor", column = "valor")
                }
        )
)

@Entity
public class acPesquisaPessoaSql {
	  	
		@Id
	  	private Integer id;
		private Double valor;
		
		public acPesquisaPessoaSql(){}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Double getValor() {
			return valor;
		}

		public void setValor(Double valor) {
			this.valor = valor;
		}
		
}
