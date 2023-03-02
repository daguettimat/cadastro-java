package br.com.global5.rastreamento.bean.ws;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.rastreamento.infra.ViagemClient;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.enums.Tecnologia;
import br.com.global5.rastreamento.model.sinc.SincCliente;
import br.com.global5.rastreamento.model.trafegus.Transacao;
import br.com.global5.rastreamento.model.trafegus.VersaoTecnologia;
import br.com.global5.rastreamento.model.trafegus.Viagem;
import br.com.global5.rastreamento.service.bi.EventoService;
import br.com.global5.rastreamento.service.ws.TransacaoService;
import br.com.global5.rastreamento.service.ws.VersaoTecnologiaService;
import br.com.global5.rastreamento.service.ws.ViagemService;


@Named
@ViewAccessScoped
public class ViagemMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Viagem viagem;
	private Integer id;

	private String dataFinalizacaoInicial = "";
	private String dataFinalizacaoFinal = "";

	private List<VersaoTecnologia> lstVersaoTecnologia;

	@Inject
	ViagemService viagemService;

	@Inject
	VersaoTecnologiaService versaoTecnologiaService;

	@Inject
	TransacaoService tnsService;

	@Inject
	EventoService evService;
	
	@Inject
	AreaService areaService;
	
	@Inject
	private Crud<Viagem> viagemCrud;

	@Inject
	private Crud<Area> areaCrud;
	
	@PostConstruct
	public void init() {
		clear();
	}

	private void clear() {
		viagem = new Viagem();
		id = null;

		viagem.setTecnologia(new Tecnologia());
		viagem.setArea(new Area());
		viagem.setProduto(new Produto());

		lstVersaoTecnologia = versaoTecnologiaService.crud().list();

	}

	public void buscarViagem() throws ParseException {

		// Execução: diriamente, buscando checklists do dia anterior (D-1).
		String dtFinalI = dataFinalizacaoInicial;
		String dtFinalF = dataFinalizacaoFinal;

		ViagemClient wsV = new ViagemClient();

		Transacao tnsWS = new Transacao();
		tnsWS.setDtInicio(new Date());
		tnsWS.setModulo("WSClient_Viagem");
		tnsWS.setDetalhe("Data Inicial:" + dataFinalizacaoInicial + " - Data Final: " + dataFinalizacaoFinal);
		tnsService.crud().save(tnsWS);

		// ultCodigo = 1
		int passa = 1;
		int ultCodigo = getWsDataViagem(wsV, tnsWS, passa, dtFinalI, dtFinalF);
		Integer verUltCodigo = ultCodigo;

		// int ultCodigoInt = ultCodigo.intValue();

		Integer zero = 0;
		int menosUm = -1;

		while (ultCodigo > 0) {
			// chamada recursivas do WsClient

			if (wsV == null) {
				String a = "a";
				String b = a;
			}

			if (tnsWS == null) {
				String a = "a";
				String b = a;
			}

			if (verUltCodigo.equals(-1)) {
				Integer ver = ultCodigo;
				Integer vern = ver;
				String a = "a";
				String b = a;
			}

			if (dtFinalI.equals("")) {
				String a = "a";
				String b = a;
			}

			if (dtFinalF.equals("")) {
				String a = "a";
				String b = a;
			}

			ultCodigo = getWsDataViagem(wsV, tnsWS, ultCodigo, dtFinalI, dtFinalF);
		}

		tnsWS.setDtFim(new Date());
		tnsService.update(tnsWS);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Viagens integradas no banco de dados!"));

	}

	private int getWsDataViagem(ViagemClient wsV, Transacao tnsWS, int ultCodigo, String dtFinalI, String dtFinalF)
			throws ParseException {

		JSONArray objJsonArray = wsV.getViagem(ultCodigo, dtFinalI, dtFinalF);

		Integer idUltimoCodigo = null;

		if (objJsonArray != null) {

			// tamanho do JsonArray retornado do Ws
			int viagensCount = objJsonArray.length();

			// Contador interno do laço for para separar o ultimo registro da
			// lista do Ws
			int countRegistrosDaChamada = 0;
			int countRegistrosDaChamadaCancelados = 0;

			// Viagem - processo individual por viagem
			for (int i = 0; i < viagensCount; i++) {

				String numSm = objJsonArray.getJSONObject(i).opt("codigoSM").toString();

				if (numSm.equals("61342")) {
					String docTransportadorA = objJsonArray.getJSONObject(i).opt("documento_transportador").toString();
					String doc = docTransportadorA;

				}

				String docTransportador = objJsonArray.getJSONObject(i).opt("documento_transportador").toString();

				if (docTransportador.equals("")) {
					String d = "deu problema";
					String a = d;
				}

				countRegistrosDaChamada++;

				// Se docTransportador for diferente de null
				if (!docTransportador.equals("null")) {

					String statusViagemJson = objJsonArray.getJSONObject(i).optString("status_viagem").toString();
					String bc = statusViagemJson;

					// Motoristas
					JSONArray jsonArrayMotoristas = objJsonArray.getJSONObject(i).getJSONArray("motoristas");

					// aqui contador oficial
					// countRegistrosDaChamada++;

					// Var Motoristas
					int arrayMotoristasCount = 0;
					int motoristaCount = 0;
					String jsonMotoristas = "";

					if (jsonArrayMotoristas != null) {

						arrayMotoristasCount = jsonArrayMotoristas.length();

						for (int im = 0; im < arrayMotoristasCount; im++) {

							motoristaCount++;

							jsonMotoristas += jsonArrayMotoristas.getJSONObject(im).toString() + ",";

						} // final for arrayMotoristaCount

						// Motorista Preparação de dados para Persistencia
						if (motoristaCount > 0) {

							int countChar = jsonMotoristas.length();
							String jsonClear = jsonMotoristas.substring(0, (countChar - 1)); // tira
																								// virgula
																								// no
																								// final
																								// do
																								// arquivo
							String jsonMotoristaMontado = "[" + jsonClear + "]";
							jsonMotoristas = jsonMotoristaMontado;

						} // final if escoltaCount

					} // final if jsonArrayMotoristas --> Motoristas

					// Veiculos (Trativa para Cavalo, Reboque e Escolta)
					JSONArray jsonArrayVeiculos = objJsonArray.getJSONObject(i).getJSONArray("veiculos");

					// Var Veiculos
					int arrayVeiculosCount = 0;
					int cavaloCount = 0;
					String placaCavalo = "";
					int reboqueCount = 0;
					String placaReboque1 = "";
					String placaReboque2 = "";
					String placaReboque3 = "";
					int escoltaCount = 0;
					String jsonVeiculosEscolta = "";
					String codAutotracMct = "";

					if (jsonArrayVeiculos != null) {

						arrayVeiculosCount = jsonArrayVeiculos.length();

						// Variaveis Escolta
						escoltaCount = 0;

						// Map<Integer, String> maps = new HashMap<>();

						// Verificar Veiculo Individualmente
						for (int iv = 0; iv < arrayVeiculosCount; iv++) {

							int tipoVeiculo = jsonArrayVeiculos.getJSONObject(iv).getInt("tipo_veiculo");

							// Valor Escolta para uso em criterio de regra -->
							// valor do elemento escolta para condição if
							String veiculoEscolta = jsonArrayVeiculos.getJSONObject(iv).opt("escolta").toString();

							// Cavalo: coleta placa onde (tipo_veiculo != 1 e
							// escolta = null)
							if (tipoVeiculo != 1 && veiculoEscolta.equals("null")) {
								// Aqui teste francis
								// String placaCavaloTipoVeic2 =
								// jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString();
								// maps.put(iv, placaCavaloTipoVeic2);

								cavaloCount++;
								placaCavalo += jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString() + "|";

							} // final if tipo_veiculo != 1 && escolta = null

							// Reboque: coleta placa onde (tipo_veiculo = 1 e
							// escolta = null )
							if (tipoVeiculo == 1 && veiculoEscolta.equals("null")) {

								reboqueCount++;

								if (reboqueCount == 1) {
									placaReboque1 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
								}
								if (reboqueCount == 2) {
									placaReboque2 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
								}
								if (reboqueCount == 3) {
									placaReboque3 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
								}

							} // final if tipo_veiculo = 1 e escolta = null

							// Escolta: (Se existir e se diferente de null )
							// (AJUSTE... deixar condição <> de null | ! )
							if (!veiculoEscolta.equals("null")) {

								escoltaCount++;

								jsonVeiculosEscolta += jsonArrayVeiculos.getJSONObject(iv).toString() + ",";

							}

						} // Final For arrayVeiculosCount --> Verifica Veiculo
							// Individualmente

						// aqui teste francis
						// for (Integer key : maps.keySet()) {
						// String valorProcurado = maps.get(key);
						// System.out.println(key + " = " + valorProcurado);
						// }

						// Cavalo Preparação de dados para Persistencia
						if (cavaloCount > 0) {

							int countChar = placaCavalo.length();
							String strClear = placaCavalo.substring(0, (countChar - 1));
							placaCavalo = strClear;

						} // final if cavaloCount

						// Escolta Preparação de dados para Persistencia
						if (escoltaCount > 0) {

							int countChar = jsonVeiculosEscolta.length();
							String jsonClear = jsonVeiculosEscolta.substring(0, (countChar - 1)); // tira
																									// virgula
																									// no
																									// final
																									// do
																									// arquivo
							String jsonEscoltaMontado = "[" + jsonClear + "]";
							jsonVeiculosEscolta = jsonEscoltaMontado;

						} // final if escoltaCount

					} // Final if jsonArrayVeiculos --> Veiculos

					// Tecnologia (Terminais) (Trativa para tecnologia e iscas )
					JSONArray jsonArrayTecnologia = objJsonArray.getJSONObject(i).getJSONArray("terminais");

					// Var Tecnologias
					int arrayTecnologiaCount = 0;
					int tecnologiaCount = 0;
					int iscaSimCount = 0;
					int iscaNaoCount = 0;
					String jsonTerminalComIsca = "";
					String numeroTerminal = ""; // para Isca = N ,
												// term_numero_terminal (mct)
					int terminalCodigo = 0; // para Isca = N , term_vtec_codigo
					int idTecnologia = 0;

					if (jsonArrayTecnologia != null) {

						arrayTecnologiaCount = jsonArrayTecnologia.length();

						for (int it = 0; it < arrayTecnologiaCount; it++) {

							String terminalComIsca = jsonArrayTecnologia.getJSONObject(it).opt("isca").toString();

							if (terminalComIsca.equals("S")) {

								iscaSimCount++;
								jsonTerminalComIsca += jsonArrayTecnologia.getJSONObject(it).toString() + ",";

							}

							if (terminalComIsca.equals("N")) {

								iscaNaoCount++;

								numeroTerminal += jsonArrayTecnologia.getJSONObject(it).opt("term_numero_terminal")
										.toString() + "|";

								terminalCodigo = (int) jsonArrayTecnologia.getJSONObject(it).opt("term_vtec_codigo");

								terminalCodigo = (int) jsonArrayTecnologia.getJSONObject(it).opt("term_vtec_codigo");

								for (VersaoTecnologia vstecnologia : getLstVersaoTecnologia()) {

									if (vstecnologia.getIdTecnologia() == terminalCodigo) {

										idTecnologia = vstecnologia.getDeParaTecnologia().getId();

										// Tratamento do MCT - Tecnologia
										// autotrac 721
										if (idTecnologia == 721) {
											String codMctViagem = jsonArrayTecnologia.getJSONObject(it)
													.opt("term_numero_terminal").toString().trim();
											codAutotracMct = interpretarMCT(codMctViagem);
										} else {
											codAutotracMct = "";
										}
										break;
									}

								} // Final For vsTecnologia
							}

						}

						// Terminal com Isca, Preparação de dados para
						// Persistencia
						if (iscaSimCount > 0) {

							int countChar = jsonTerminalComIsca.length();
							String jsonClear = jsonTerminalComIsca.substring(0, (countChar - 1)); // tira
																									// virgula
																									// no
																									// final
																									// do
																									// arquivo
							String jsonTerminalComIscaMontado = "[" + jsonClear + "]";
							jsonTerminalComIsca = jsonTerminalComIscaMontado;

						} // final if iscaSimCount

						// Terminal sem Isca (Veiculo Motor), Preparação de
						// dados para Persistencia
						if (iscaNaoCount > 0) {

							int countChar = numeroTerminal.length();
							String strClear = numeroTerminal.substring(0, (countChar - 1)); // tira
																							// pipe
																							// no
																							// final
																							// do
																							// arquivo
							numeroTerminal = strClear;

						} // final if iscaNaoCount

					} // final if jsonArrayTecnologia (Terminais)

					// 1º Persistencia dos dados comum

					String jsonViagemWs = "";
					jsonViagemWs = objJsonArray.getJSONObject(i).toString();

					Viagem objVm = new Viagem();
					// jsonArrayTecnologia.getJSONObject(it).opt("term_numero_terminal").toString()
					if (objJsonArray.getJSONObject(i).opt("codigoSM").equals("43277")) {

						String a = "b";
						String b = a;

					}

					Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataFinalizacaoInicial);
					Date dataFinal = new SimpleDateFormat("dd/MM/yyyy").parse(dataFinalizacaoFinal);

					// if (
					// objJsonArray.getJSONObject(i).getString("codigoSM").trim().equals(""))

					String dtFinalizacaoInicialJson = "";
					String dtFinalizacaoFinalJson = "";
					Date convertData_FinalizacaoInicial_Json = new Date();
					Date convertData_FinalizacaoFinal_Json = new Date();

					if (!objJsonArray.getJSONObject(i).opt("viag_data_inicio").equals(null)) {

						dtFinalizacaoInicialJson = objJsonArray.getJSONObject(i).getString("viag_data_inicio").trim();

						if (!dtFinalizacaoInicialJson.equals("")) {
							convertData_FinalizacaoInicial_Json = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.parse(objJsonArray.getJSONObject(i).getString("viag_data_inicio"));
						} else {
							convertData_FinalizacaoInicial_Json = null;
						}

					} else {
						convertData_FinalizacaoInicial_Json = null;
					}

					if (!objJsonArray.getJSONObject(i).opt("viag_data_fim").equals(null)) {

						dtFinalizacaoFinalJson = objJsonArray.getJSONObject(i).getString("viag_data_fim").trim();

						if (!dtFinalizacaoFinalJson.equals("")) {
							convertData_FinalizacaoFinal_Json = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.parse(objJsonArray.getJSONObject(i).getString("viag_data_fim"));
						} else {
							convertData_FinalizacaoFinal_Json = null;
						}

					} else {
						convertData_FinalizacaoFinal_Json = null;
					}

					objVm.setDtImportacao(new Date());
					objVm.setSm(objJsonArray.getJSONObject(i).getInt("codigoSM"));
					objVm.setDtInicio(convertData_FinalizacaoInicial_Json);
					objVm.setDtTermino(convertData_FinalizacaoFinal_Json);
					objVm.setStatusViagem(statusViagemJson);
					objVm.setTransportador(objJsonArray.getJSONObject(i).getString("documento_transportador"));
					objVm.setCavalo(placaCavalo);
					objVm.setReboque1(placaReboque1);
					objVm.setReboque2(placaReboque2);
					objVm.setReboque3(placaReboque3);
					objVm.setAutotrac_mct(codAutotracMct);
					// so para teste
					if (idTecnologia > 0) {
						objVm.setTecnologia(new Tecnologia(idTecnologia));
					} else {
						objVm.setTecnologia(new Tecnologia(731));
					}
					objVm.setMct(numeroTerminal);
					if (escoltaCount > 0) {
						objVm.setQtdEscoltas(escoltaCount);
					} else {
						objVm.setQtdEscoltas(0);
					}
					if (iscaSimCount > 0) {
						objVm.setQtdIscas(iscaSimCount);
					} else {
						objVm.setQtdIscas(0);
					}

					if (escoltaCount == 0) {
						objVm.setEscolta(null);
					}

					objVm.setTransacao(tnsWS);

					viagemService.crud().save(objVm);

					Integer idViagem = objVm.getId();

					// 2º Update Json por Procedure
					// Motorista
					Integer resultMot = null;
					if (motoristaCount > 0) {
						StoredProcedureQuery qryUpJsonMot = viagemCrud.getEntityManager()
								.createNamedStoredProcedureQuery("update_json_viagem");
						qryUpJsonMot.setParameter("tabela", "rastreamento.viagem");
						qryUpJsonMot.setParameter("campo_chave", "viagoid");
						qryUpJsonMot.setParameter("chave_id", idViagem);
						qryUpJsonMot.setParameter("campo_tabela_json", "viag_motoristas");
						qryUpJsonMot.setParameter("valor_json", jsonMotoristas);

						resultMot = (Integer) qryUpJsonMot.getOutputParameterValue("resultUp");
					}

					// Escolta
					// jsonVeiculosEscolta {SE NÃO EXISTIR JSON de Escolta =
					// Persiste Null no campo viag_escolta }
					Integer resultEsc = null;
					if (escoltaCount > 0) {
						StoredProcedureQuery qryUpJsonEsc = viagemCrud.getEntityManager()
								.createNamedStoredProcedureQuery("update_json_viagem");
						qryUpJsonEsc.setParameter("tabela", "rastreamento.viagem");
						qryUpJsonEsc.setParameter("campo_chave", "viagoid");
						qryUpJsonEsc.setParameter("chave_id", idViagem);
						qryUpJsonEsc.setParameter("campo_tabela_json", "viag_escolta");
						qryUpJsonEsc.setParameter("valor_json", jsonVeiculosEscolta);

						resultEsc = (Integer) qryUpJsonEsc.getOutputParameterValue("resultUp");
					} else {
						objVm.setEscolta(null);
					}

					// Tecnologia (viag_isca = S)
					Integer resultTec = null;
					if (tecnologiaCount > 0 && iscaSimCount > 0) {
						StoredProcedureQuery qryUpJsonTec = viagemCrud.getEntityManager()
								.createNamedStoredProcedureQuery("update_json_viagem");
						qryUpJsonTec.setParameter("tabela", "rastreamento.viagem");
						qryUpJsonTec.setParameter("campo_chave", "viagoid");
						qryUpJsonTec.setParameter("chave_id", idViagem);
						qryUpJsonTec.setParameter("campo_tabela_json", "viag_isca");
						qryUpJsonTec.setParameter("valor_json", jsonTerminalComIsca);

						resultTec = (Integer) qryUpJsonTec.getOutputParameterValue("resultUp");
					}

					// Viagem
					Integer resultViagem = null;

					StoredProcedureQuery qryUpJsonViagem = viagemCrud.getEntityManager()
							.createNamedStoredProcedureQuery("update_json_viagem");
					qryUpJsonViagem.setParameter("tabela", "rastreamento.viagem");
					qryUpJsonViagem.setParameter("campo_chave", "viagoid");
					qryUpJsonViagem.setParameter("chave_id", idViagem);
					qryUpJsonViagem.setParameter("campo_tabela_json", "viag_json");
					qryUpJsonViagem.setParameter("valor_json", jsonViagemWs);

					resultViagem = (Integer) qryUpJsonViagem.getOutputParameterValue("resultUp");

				} // final checagem se docTRansportador é diferente de null

			} // final viagensCount ( Viagem - analise individual )

			if (countRegistrosDaChamada == (viagensCount)) {

				int p = viagensCount - 1;

				idUltimoCodigo = objJsonArray.getJSONObject(p).getInt("codigoSM");

				Integer e = idUltimoCodigo;
				/*
				 * Integer verUltimaSM =
				 * objJsonArray.getJSONObject(p).getInt("codigoSM");
				 * 
				 * if( verUltimaSM != null) { idUltimoCodigo =
				 * objJsonArray.getJSONObject(p).getInt("codigoSM");
				 * 
				 * Integer e = idUltimoCodigo;
				 * 
				 * }
				 */

			}

		} // final objJsonArray ( chamada do Ws )
		else {
			return idUltimoCodigo = -1;
		}

		return idUltimoCodigo;

	}

	
	private String interpretarMCT(String codMctViagem) {

		char[] codM = codMctViagem.toCharArray();

		int sizeString = codMctViagem.length();
		int countAntesPipe = 0;
		int countPosPipe = 0;

		String codMct = "";

		for (int i = 0; i < sizeString; ++i) {

			if (!String.valueOf(codM[i]).equals("|")) {
				++countAntesPipe;
			}
		}

		codMct = codMctViagem.substring(0, 7);
		return codMct;

	}

	
	
	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataFinalizacaoInicial() {
		return dataFinalizacaoInicial;
	}

	public void setDataFinalizacaoInicial(String dataFinalizacaoInicial) {
		this.dataFinalizacaoInicial = dataFinalizacaoInicial;
	}

	public String getDataFinalizacaoFinal() {
		return dataFinalizacaoFinal;
	}

	public void setDataFinalizacaoFinal(String dataFinalizacaoFinal) {
		this.dataFinalizacaoFinal = dataFinalizacaoFinal;
	}

	public List<VersaoTecnologia> getLstVersaoTecnologia() {
		return lstVersaoTecnologia;
	}

	public void setLstVersaoTecnologia(List<VersaoTecnologia> lstVersaoTecnologia) {
		this.lstVersaoTecnologia = lstVersaoTecnologia;
	}

}
