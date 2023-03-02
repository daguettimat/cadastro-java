package br.com.global5.rastreamento.model.bi;

import java.math.BigDecimal;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.rastreamento.model.sinc.SincCarreta;
import br.com.global5.rastreamento.model.sinc.SincCliente;
import br.com.global5.rastreamento.model.sinc.SincCorretora;
import br.com.global5.rastreamento.model.sinc.SincMotorista;
import br.com.global5.rastreamento.model.sinc.SincSeguradora;
import br.com.global5.rastreamento.model.sinc.SincVeiculo;
import br.com.global5.rastreamento.model.sinc.SincViagem;
import br.com.global5.rastreamento.model.trafegus.EventoInfoRastreador;

/**
 * 
 * @author francis
 * 
 * --- está tabela não pode ser atualizada e nem inserida dados pelo sistema apenas via integração pelo pentaho
 *
 */
@Entity
@Table(name="rastreamento.evento_info_viagem")
public class EventoInfoViagem implements BaseEntity {
	
	@Id
	@Column(name="evev_eveoid")
	private Integer id;
	
	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = Area.class
			)
	@JoinColumn(name="evev_areaoid")
	private Area area;
	
	@Column(name="evev_cliente")
	private String nomeCliente;

	@Column(name="evev_segoid")
	private Integer seguradora;

	@Column(name="evev_seguradora")
	private String nomeSeguradora;

	@Column(name="evev_corroid")
	private Integer corretora;
	
	@Column(name="evev_corretora")
	private String nomeCorretora;
	
	@Column(name="evev_transportadora")
	private String nomeTransportadora;

	@Column(name="evev_veioid")
	private Integer veiculo;

	@Column(name="evev_veioid_placa")
	private String veiculoPlaca;

	@Column(name="evev_veioid_marca")
	private String veiculoMarca;

	@Column(name="evev_veioid_modelo")
	private String veiculoModelo;

	@Column(name="evev_veioid_ano")
	private String veiculoAno;

	@Column(name="evev_veioid_cor")
	private String veiculoCor;

	@Column(name="evev_veioid_id_rastreador")
	private String veiculoIdRastreador;

	@Column(name="evev_veioid_tecnologia")
	private String veiculoIdTecnologia;

	@Column(name="evev_caroid1")
	private Integer carreta1;
	
	@Column(name="evev_caroid1_placa")
	private String carretaPlaca1;
	
	@Column(name="evev_caroid1_marca")
	private String carretaMarca1;
	
	@Column(name="evev_caroid1_ano")
	private Integer carretaAno1;
	
	@Column(name="evev_caroid1_cor")
	private String carretaCor1;

	@Column(name="evev_caroid1_tipo")
	private String carretaTipo1;

	@Column(name="evev_caroid2")
	private Integer carreta2;
	
	@Column(name="evev_caroid2_placa")
	private String carretaPlaca2;

	@Column(name="evev_caroid2_marca")
	private String carretaMarca2;
	
	@Column(name="evev_caroid2_ano")
	private Integer carretaAno2;

	@Column(name="evev_caroid2_cor")
	private String carretaCor2;

	@Column(name="evev_caroid2_tipo")
	private String carretaTipo2;

	@Column(name="evev_caroid3")
	private Integer carreta3;
	
	@Column(name="evev_caroid3_placa")
	private String carretaPlaca3;
	
	@Column(name="evev_caroid3_marca")
	private String carretaMarca3;
	
	@Column(name="evev_caroid3_ano")
	private Integer carretaAno3;
	
	@Column(name="evev_caroid3_cor")
	private String carretaCor3;

	@Column(name="evev_caroid3_tipo")
	private String carretaTipo3;

	@Column(name="evev_motoid1")
	private Integer motorista1;	

	@Column(name="evev_motoid1_nome")
	private String motoristaNome1;

	@Column(name="evev_motoid1_cpf")
	private String motoristaCpf1;

	@Column(name="evev_motoid1_fone")
	private String motoristaFone1;
	
	@Column(name="evev_motoid1_vinculo")
	private String motoristaVinculo1;

	@Column(name="evev_motoid2")
	private Integer motorista2;	

	@Column(name="evev_motoid2_nome")
	private String motoristaNome2;
	
	@Column(name="evev_motoid2_cpf")
	private String motoristaCpf2;
	
	@Column(name="evev_motoid2_fone")
	private String motoristaFone2;

	@Column(name="evev_motoid2_vinculo")
	private String motoristaVinculo2;

	@Column(name="evev_viagoid")
	private Integer viagem;
	
	@Column(name="evev_origem_cidade")
	private String origemCidade;

	@Column(name="evev_origem_uf")
	private String origemUf;

	@Column(name="evev_destino_cidade")
	private String destinoCidade;

	@Column(name="evev_destino_uf")
	private String destinoUf;

	@Column(name="evev_emboid_origem")
	private String embarcadorOrigem;
	
	@Column(name="evev_emboid_destino")
	private String embarcadorDestino;

	@Column(name="evev_mercadoria")
	private String mercadoria;

	@Column(name="evev_valor")
	private BigDecimal valor;

	@Column(name="evev_tipo_operacao")
	private String tipoOperacao;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = EventoInfoRastreador.class
		)
	@JoinColumn(name="evev_rastreador")
	private EventoInfoRastreador rastreador;
	
	public EventoInfoViagem(){}
	
	public EventoInfoViagem(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public Integer getSeguradora() {
		return seguradora;
	}

	public String getNomeSeguradora() {
		return nomeSeguradora;
	}

	public Integer getCorretora() {
		return corretora;
	}

	public String getNomeCorretora() {
		return nomeCorretora;
	}

	public String getNomeTransportadora() {
		return nomeTransportadora;
	}

	public Integer getVeiculo() {
		return veiculo;
	}

	public String getVeiculoPlaca() {
		return veiculoPlaca;
	}

	public String getVeiculoMarca() {
		return veiculoMarca;
	}

	public String getVeiculoModelo() {
		return veiculoModelo;
	}

	public String getVeiculoAno() {
		return veiculoAno;
	}

	public String getVeiculoCor() {
		return veiculoCor;
	}



	public String getVeiculoIdTecnologia() {
		return veiculoIdTecnologia;
	}

	public Integer getCarreta1() {
		return carreta1;
	}

	public String getCarretaPlaca1() {
		return carretaPlaca1;
	}

	public String getCarretaMarca1() {
		return carretaMarca1;
	}

	public Integer getCarretaAno1() {
		return carretaAno1;
	}

	public String getCarretaCor1() {
		return carretaCor1;
	}

	public String getCarretaTipo1() {
		return carretaTipo1;
	}

	public Integer getCarreta2() {
		return carreta2;
	}

	public String getCarretaPlaca2() {
		return carretaPlaca2;
	}

	public String getCarretaMarca2() {
		return carretaMarca2;
	}

	public Integer getCarretaAno2() {
		return carretaAno2;
	}

	public String getCarretaCor2() {
		return carretaCor2;
	}

	public String getCarretaTipo2() {
		return carretaTipo2;
	}

	public Integer getCarreta3() {
		return carreta3;
	}

	public String getCarretaPlaca3() {
		return carretaPlaca3;
	}

	public String getCarretaMarca3() {
		return carretaMarca3;
	}

	public Integer getCarretaAno3() {
		return carretaAno3;
	}

	public String getCarretaCor3() {
		return carretaCor3;
	}

	public String getCarretaTipo3() {
		return carretaTipo3;
	}

	public Integer getMotorista1() {
		return motorista1;
	}

	public String getMotoristaNome1() {
		return motoristaNome1;
	}

	public String getMotoristaCpf1() {
		return motoristaCpf1;
	}

	public String getMotoristaFone1() {
		return motoristaFone1;
	}

	public String getMotoristaVinculo1() {
		return motoristaVinculo1;
	}

	public Integer getMotorista2() {
		return motorista2;
	}

	public String getMotoristaNome2() {
		return motoristaNome2;
	}

	public String getMotoristaCpf2() {
		return motoristaCpf2;
	}

	public String getMotoristaFone2() {
		return motoristaFone2;
	}

	public String getMotoristaVinculo2() {
		return motoristaVinculo2;
	}

	public Integer getViagem() {
		return viagem;
	}

	public String getOrigemCidade() {
		return origemCidade;
	}

	public String getOrigemUf() {
		return origemUf;
	}

	public String getDestinoCidade() {
		return destinoCidade;
	}

	public String getDestinoUf() {
		return destinoUf;
	}

	public String getEmbarcadorOrigem() {
		return embarcadorOrigem;
	}

	public String getEmbarcadorDestino() {
		return embarcadorDestino;
	}

	public String getMercadoria() {
		return mercadoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public EventoInfoRastreador getRastreador() {
		return rastreador;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public void setSeguradora(Integer seguradora) {
		this.seguradora = seguradora;
	}

	public void setNomeSeguradora(String nomeSeguradora) {
		this.nomeSeguradora = nomeSeguradora;
	}

	public void setCorretora(Integer corretora) {
		this.corretora = corretora;
	}

	public void setNomeCorretora(String nomeCorretora) {
		this.nomeCorretora = nomeCorretora;
	}

	public void setNomeTransportadora(String nomeTransportadora) {
		this.nomeTransportadora = nomeTransportadora;
	}

	public void setVeiculo(Integer veiculo) {
		this.veiculo = veiculo;
	}

	public void setVeiculoPlaca(String veiculoPlaca) {
		this.veiculoPlaca = veiculoPlaca;
	}

	public void setVeiculoMarca(String veiculoMarca) {
		this.veiculoMarca = veiculoMarca;
	}

	public void setVeiculoModelo(String veiculoModelo) {
		this.veiculoModelo = veiculoModelo;
	}

	public void setVeiculoAno(String veiculoAno) {
		this.veiculoAno = veiculoAno;
	}

	public void setVeiculoCor(String veiculoCor) {
		this.veiculoCor = veiculoCor;
	}


	public String getVeiculoIdRastreador() {
		return veiculoIdRastreador;
	}

	public void setVeiculoIdRastreador(String veiculoIdRastreador) {
		this.veiculoIdRastreador = veiculoIdRastreador;
	}

	public void setVeiculoIdTecnologia(String veiculoIdTecnologia) {
		this.veiculoIdTecnologia = veiculoIdTecnologia;
	}

	public void setCarreta1(Integer carreta1) {
		this.carreta1 = carreta1;
	}

	public void setCarretaPlaca1(String carretaPlaca1) {
		this.carretaPlaca1 = carretaPlaca1;
	}

	public void setCarretaMarca1(String carretaMarca1) {
		this.carretaMarca1 = carretaMarca1;
	}

	public void setCarretaAno1(Integer carretaAno1) {
		this.carretaAno1 = carretaAno1;
	}

	public void setCarretaCor1(String carretaCor1) {
		this.carretaCor1 = carretaCor1;
	}

	public void setCarretaTipo1(String carretaTipo1) {
		this.carretaTipo1 = carretaTipo1;
	}

	public void setCarreta2(Integer carreta2) {
		this.carreta2 = carreta2;
	}

	public void setCarretaPlaca2(String carretaPlaca2) {
		this.carretaPlaca2 = carretaPlaca2;
	}

	public void setCarretaMarca2(String carretaMarca2) {
		this.carretaMarca2 = carretaMarca2;
	}

	public void setCarretaAno2(Integer carretaAno2) {
		this.carretaAno2 = carretaAno2;
	}

	public void setCarretaCor2(String carretaCor2) {
		this.carretaCor2 = carretaCor2;
	}

	public void setCarretaTipo2(String carretaTipo2) {
		this.carretaTipo2 = carretaTipo2;
	}

	public void setCarreta3(Integer carreta3) {
		this.carreta3 = carreta3;
	}

	public void setCarretaPlaca3(String carretaPlaca3) {
		this.carretaPlaca3 = carretaPlaca3;
	}

	public void setCarretaMarca3(String carretaMarca3) {
		this.carretaMarca3 = carretaMarca3;
	}

	public void setCarretaAno3(Integer carretaAno3) {
		this.carretaAno3 = carretaAno3;
	}

	public void setCarretaCor3(String carretaCor3) {
		this.carretaCor3 = carretaCor3;
	}

	public void setCarretaTipo3(String carretaTipo3) {
		this.carretaTipo3 = carretaTipo3;
	}

	public void setMotorista1(Integer motorista1) {
		this.motorista1 = motorista1;
	}

	public void setMotoristaNome1(String motoristaNome1) {
		this.motoristaNome1 = motoristaNome1;
	}

	public void setMotoristaCpf1(String motoristaCpf1) {
		this.motoristaCpf1 = motoristaCpf1;
	}

	public void setMotoristaFone1(String motoristaFone1) {
		this.motoristaFone1 = motoristaFone1;
	}

	public void setMotoristaVinculo1(String motoristaVinculo1) {
		this.motoristaVinculo1 = motoristaVinculo1;
	}

	public void setMotorista2(Integer motorista2) {
		this.motorista2 = motorista2;
	}

	public void setMotoristaNome2(String motoristaNome2) {
		this.motoristaNome2 = motoristaNome2;
	}

	public void setMotoristaCpf2(String motoristaCpf2) {
		this.motoristaCpf2 = motoristaCpf2;
	}

	public void setMotoristaFone2(String motoristaFone2) {
		this.motoristaFone2 = motoristaFone2;
	}

	public void setMotoristaVinculo2(String motoristaVinculo2) {
		this.motoristaVinculo2 = motoristaVinculo2;
	}

	public void setViagem(Integer viagem) {
		this.viagem = viagem;
	}

	public void setOrigemCidade(String origemCidade) {
		this.origemCidade = origemCidade;
	}

	public void setOrigemUf(String origemUf) {
		this.origemUf = origemUf;
	}

	public void setDestinoCidade(String destinoCidade) {
		this.destinoCidade = destinoCidade;
	}

	public void setDestinoUf(String destinoUf) {
		this.destinoUf = destinoUf;
	}

	public void setEmbarcadorOrigem(String embarcadorOrigem) {
		this.embarcadorOrigem = embarcadorOrigem;
	}

	public void setEmbarcadorDestino(String embarcadorDestino) {
		this.embarcadorDestino = embarcadorDestino;
	}

	public void setMercadoria(String mercadoria) {
		this.mercadoria = mercadoria;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public void setRastreador(EventoInfoRastreador rastreador) {
		this.rastreador = rastreador;
	}

	
}
