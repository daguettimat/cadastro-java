package br.com.global5.rastreamento.model.bi;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name="rastreamento.boletim_informativo_numeracao")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
			name = "boletim_informativo_global",
			procedureName = "boletim_informativo_global",
			parameters = {
					@StoredProcedureParameter(mode = ParameterMode.IN, type = Character.class, name = "tipo"),
					@StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "bin")
			}
			)
})
public class BoletimInformativoNumeracao implements BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	public static final char EVENTO_TIPO_SUSPEITA 	= 'S';
	public static final char EVENTO_TIPO_PERDA		= 'P';
	public static final char EVENTO_TIPO_DANO		= 'D';

	@Id
	@SequenceGenerator(name = "boletim_informativo_numeracao_binoid_seq", sequenceName = "boletim_informativo_numeracao_binoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boletim_informativo_numeracao_binoid_seq")
	@Column(name="binoid")
	private Integer id;

	@Column(name="bin_ano")
	private Integer ano;

	@Column(name="bin_contador")
	private Integer contador;

	@Column(name="bin_tipo", nullable = false, precision = 1)
	private Character tipo;

	public BoletimInformativoNumeracao() {}
	
	public BoletimInformativoNumeracao(Integer id){ this.id = id; }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getContador() {
		return contador;
	}

	public void setContador(Integer contador) {
		this.contador = contador;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

}
