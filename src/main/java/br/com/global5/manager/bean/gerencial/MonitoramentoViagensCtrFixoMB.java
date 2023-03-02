package br.com.global5.manager.bean.gerencial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.DateViewChangeEvent;

import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.geral.ContratoProduto;
import br.com.global5.manager.model.gerencial.MonitoramentoViagensCtrFixo;
import br.com.global5.manager.model.gerencial.PlacasFixaPorContrato;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.geral.ContratoProdutoService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.manager.service.gerencial.MonitoramentoViagensService;
import br.com.global5.manager.service.gerencial.PlacasFixaPorContratoService;

@Named
@ViewAccessScoped
public class MonitoramentoViagensCtrFixoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dtInicioBusca;
	private Date dtFinalBusca;
	
	private List<MonitoramentoViagensCtrFixo> listMonViagCtrFixo;
	
	private List<PlacasFixaPorContrato> listPlacasFixaPorContrato;
	
	private List<PlacasFixaPorContrato> listPlacasFixa;
	
	private MonitoramentoViagensCtrFixo mon;
	
	// Variaveis do Filtro

	
	private Date dtInicial;
	private Date dtFinal;
	
	
	///
	private String previousPage = null;
	private boolean mostraEdicao = false;
	
	@Inject
	private UsuarioService usuService;
	
	@Inject
	AreaService areaService;
	
	
	@Inject
	PlacasFixaPorContratoService pfCtrService;
	
	@Inject
	MonitoramentoViagensService mvCtrService;
	
	@Inject
	ContratoProdutoService ctrProdutoService;
	
	@PostConstruct
	public void init(){
		clear();
	
	}


	private void clear() {
		
		//listMonViagCtrFixo = null;
		
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);

		dtInicial = now.getTime();
		dtFinal = now.getTime();
		
	}
	
	private Date calendar(){
		
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		
		Date teste = now.getTime();
		Date a = teste;
		
		//onChangeDataInicio( new Date(now.getTime());
		
		return now.getTime();
		
	}
	
	public void onChangeDataInicio(DateViewChangeEvent event) throws ParseException {
		dtInicial = new Date();
	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	    String inicio = event.getYear() + "-" + event.getMonth() + "-" + "01";
	    this.setDtInicial(dateformat.parse(inicio));
	   
	    
	}
	
	public void onChangeDataFinal(DateViewChangeEvent event) throws ParseException {
		dtFinal = new Date();
	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	    String inicio = event.getYear() + "-" + event.getMonth() + "-" + "01";
	    this.setDtFinal(dateformat.parse(inicio));
	   
	    
	}
	
	public void checkF5(){
		
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		String id = viewRoot.getViewId();
		
		 if (previousPage != null && (previousPage.equals(id))) {
		       // It's a reload event
			 	mostraEdicao = false;
			 
		    }
		
		previousPage = id;
		
	}
	
	public void inserirRegistros(){
		
	}
	
	
	/**
	 * 2° método a ser executado
	 * Busca as viagens dentro do periodo informado
	 * 2.1 - método (pesquisarPlacaFixo) que atualiza as placas encontradas para "checado = 1" 
	 */
	public void buscarViagens(){
		
		EntityManager em = usuService.crud().getEntityManager();
		
		// Aqui a query busca as viagens dentro do periodo informado e ficará dentro do list listMonViagCtrFixo
		
		String query = " select  row_number() over() as id, v.viag_areaoid_cliente as idCliente, a.area_nome as cliente, "
			     + " (select p.pes_documento1 from area c , pessoa p where c.areaoid =  v.viag_areaoid_cliente and c.area_pesoid_sacado = p.pesoid ) as cnpj, "
			     + " extract(month from v.viag_dt_inicio ) as mes, extract(year from v.viag_dt_inicio ) as ano, "
			     + " count(v.viag_cavalo) as qtdViagens, v.viag_cavalo as cavalo, "
			     + " case when v.viag_prodoid_viagem = 7 then 'Veículo Fixo Automático' when v.viag_prodoid_viagem = 8 then "
			     + " 'Veículo Fixo' when v.viag_prodoid_viagem = 9 then 'Viagem Avulsa - Viagem' "
			     + " else 'Viagem Avulsa - Dia de Viagem' end as produto, sum(v.viag_vlr) as valor " 
			     + " from rastreamento.viagem v, java.area a " 
			     + "   where v.viag_areaoid_cliente = a.areaoid and "
			     + "   v.viag_areaoid_cliente = 6663 and v.viag_trdboid = 182 " 
			     + "   group by idCliente,cliente, ano, mes , v.viag_cavalo, produto, v.viag_conoid, v.viag_prodoid_viagem " ;
		
		listMonViagCtrFixo = em.createNativeQuery(query, "ListaMonViagCtrFixoMapping").getResultList();
		
		if ( listMonViagCtrFixo.size() > 0) {
			
			for ( int i = 0; i < listMonViagCtrFixo.size(); i++){
				
				// 2.1 método que atualiza o campo "checado = 1" 
				//  Atualiza a tabela tmp_placasfixas (pojo PlacasFixaPorContrato) se caso 
				// as placas forem a mesma passando checado para 1
				Integer idPlacaPesq =  pesquisarPlacaFixo(listMonViagCtrFixo.get(i).getCavalo(), listMonViagCtrFixo.get(i).getIdCliente(), ""); 
				
				if ( idPlacaPesq > 0 ) {
					// tabela tmp_placasfixas
					PlacasFixaPorContrato pf = pfCtrService.findById(idPlacaPesq);
					
					pf.setChecado(1);
					pf.setCliente(listMonViagCtrFixo.get(i).getCliente());
					pf.setCnpj(listMonViagCtrFixo.get(i).getCnpj());
					pf.setMes(listMonViagCtrFixo.get(i).getMes());
					pf.setAno(listMonViagCtrFixo.get(i).getAno());
					pfCtrService.crud().update(pf);					
					
				}
				
				// 2.2 método de inserir registros na tabela 

			}
			
		}
	}
	
	/**
	 * 2.1 - Ordem de execução 
	 * @param placa
	 * @param idCliente
	 * @return
	 */
	private Integer pesquisarPlacaFixo(String placa, Integer idCliente, String periodo){
	
		EntityManager em = usuService.crud().getEntityManager();
		
		String query = " select  pcv_idregistro as id, pcv_idplaca as idPlaca, pcv_idcliente as idCliente,  "
				+ " pcv_placafixa as placaFixa, pcv_checado as checado,pcv_cliente as cliente, pcv_cnpj as cnpj, "
				+ " pcv_mes as mes, pcv_ano as ano, pcv_excluida as excluida, pcv_periodo  as periodo, "
				+ " pcv_vlr_contrato as vlrContrato, pcv_id_produto as idProduto, pcv_nome_produto as nomeProduto, "
				+ " pcv_num_contrato as numContrato " 
				+ " from java.tmp_placasfixas  "
				+ " where pcv_idCliente =  " + idCliente +  " and pcv_placaFixa = '" + placa +"' and "
				+ " pcv_periodo='" + periodo  +"'" ;
		
		// Carreaga a list listPlacasFixaPorContrato 
		listPlacasFixaPorContrato = em.createNativeQuery(query, "ListaPlacasFixaPorContratoMapping").getResultList();
		
		if ( listPlacasFixaPorContrato.size() == 1) {
			return listPlacasFixaPorContrato.get(0).getId();
		} else {
			return 0;
		}
		
	}
	
	
	/**
	 * @author Francis Bueno
	 * @param d1
	 * @param d2
	 * @return
	 * Método que retorna a diferença entre os meses(meses entre data inicial d1 e data final d2).
	 * Esse método retorna o numero de meses entre uma data inicial e data final 
	 */
	public static int monthsBetween(Date d1, Date d2){

		// verifica se algum parametro está vazio
		if(d2==null || d1==null){
	        return -1;//Error
	    }
		
	    Calendar m_calendar=Calendar.getInstance();
	    
	    m_calendar.setTime(d1);
	    
	    int nMonth1 = 12 * m_calendar.get(Calendar.YEAR) + m_calendar.get(Calendar.MONTH);
	    
	    m_calendar.setTime(d2);	    
	    
	    int nMonth2 = 12 * m_calendar.get(Calendar.YEAR) + m_calendar.get(Calendar.MONTH);
	    
	    // retorna a quantidade de meses
	    return java.lang.Math.abs(nMonth2-nMonth1);
	    
	}
	
	/**
	 * @author Francis Bueno
	 * @param d1
	 * @param qtdMes
	 * @return
	 * Método que retorna o próximo mês da data recebida calculado com o numero de meses que 
	 * se deseja adicionar ao calculo. O retorno é uma data.
	 */
	public Date nextMonth(Date d1, int qtdMes){
		Calendar m_calendar=Calendar.getInstance();
		m_calendar.setTime(d1);
		
		m_calendar.add(Calendar.MONTH, qtdMes);
		
		Date dt = m_calendar.getTime();
		
		return dt;
	}
	
	public Date lastMonth(Date d1, int qtdDias){
		
		Calendar m_calendar=Calendar.getInstance();
		m_calendar.setTime(d1);
		
		m_calendar.add(Calendar.DATE, -qtdDias);
		//m_calendar.add(Calendar.MONTH, qtdMes);
		
		Date dt = m_calendar.getTime();
		
		return dt;
	}
	
	
	public Date nextMonthDays(Date d1, int qtdDias){
		
		Calendar m_calendar=Calendar.getInstance();
		m_calendar.setTime(d1);
		
		m_calendar.add(Calendar.DATE, qtdDias);
		
		Date dt = m_calendar.getTime();
		
		return dt;
	}
	
	/**
	 * Método em teste em backup para o metodo em execução testeDataGeradorExec alterado para -- buscarPlacasFixas
	 */
	public void testeDataGerador(){
		
		// Qtd de meses entre uma data Inicial e Final, adicionado o 1 para ser apresentado conforme calculado.
		int meses = monthsBetween(dtInicial, dtFinal) + 1;		

		Calendar m_calendar=Calendar.getInstance();
	    
		m_calendar.setTime(dtInicial);
		
		int nMonth1 = m_calendar.get(Calendar.MONTH) + 1 ;
		
		int mesAno = 12;
		
		int mesesParaCompletarAno = mesAno - (nMonth1) + 1;
		
		int count=0;
		int countM = 0;
		int countOM = 0;
		int countYear = 0;
		
		for(int i = 0; i < meses; i++){
			// Meses processados
			count++;
			
			if( count <= (mesesParaCompletarAno) ){
				// Primeiros Meses
				countM++;
				int nMonthPM = m_calendar.get(Calendar.MONTH) + countM ;
				int t1a = nMonthPM;
				int nYearPM = m_calendar.get(Calendar.YEAR) ;
				int t1b = nYearPM;
				
			} else {
				
				countOM++;
				
				
				if(countOM == 12){
					countOM = 0;
					countYear++;
				}
				
				
			}
		}
	}
	
	/**
	 * 1 º execução do método buscarPlacasFixas
	 * 2 ° execução do método (fazer varedura do periodo informado nas placas dentro de viagens cruzando as viagenes finalizadas com as placas fixas
	 *     se encontrar deverá ser feito um update na tabela tmp_placasfixas no campo  pcv_checado alterando para 1.
	 */
	
	/**
	 * Método teste para fazer varedura
	 */
	public void fazerVareduraViagensVsPlacasFixasEAtualizar(){
		
		EntityManager em = usuService.crud().getEntityManager();
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date dtInicioPesq = lastMonth(dtInicial , 16 );
		
		String dtInicioP = dateformat.format(dtInicioPesq);

		// Aqui a query busca as viagens dentro do periodo informado e ficará dentro do list listMonViagCtrFixo
		
		String query = " select  row_number() over() as id, v.viag_areaoid_cliente as idCliente, a.area_nome as cliente, "
			     + " (select p.pes_documento1 from area c , pessoa p where c.areaoid =  v.viag_areaoid_cliente and c.area_pesoid_sacado = p.pesoid ) as cnpj, "
			     + " extract(month from v.viag_dt_inicio ) as mes, extract(year from v.viag_dt_inicio ) as ano, "
			     + " count(v.viag_cavalo) as qtdViagens, v.viag_cavalo as cavalo, "
			     + " case when v.viag_prodoid_viagem = 7 then 'Veículo Fixo Automático' when v.viag_prodoid_viagem = 8 then "
			     + " 'Veículo Fixo' when v.viag_prodoid_viagem = 9 then 'Viagem Avulsa - Viagem' "
			     + " else 'Viagem Avulsa - Dia de Viagem' end as produto, sum(v.viag_vlr) as valor, "
			     + " v.viag_prodoid_viagem as idProduto, v.viag_conoid as idContrato "
			     + " from rastreamento.viagem v, java.area a " 
			     + "   where v.viag_areaoid_cliente = a.areaoid and "
			     + "  v.viag_dt_inicio > '" + dtInicioP + "' and v.viag_dt_termino < '" + dtFinal  + "'"     
			     + "   group by idCliente,cliente, ano, mes , v.viag_cavalo, produto, v.viag_conoid, v.viag_prodoid_viagem " ;
				
				/*
				viag_conoid           | 90030
				viag_prodoid_viagem   | 8
				*/
		
		 //+ "   v.viag_areaoid_cliente = 6663 and v.viag_trdboid = 182 " 
		
		listMonViagCtrFixo = em.createNativeQuery(query, "ListaMonViagCtrFixoMapping").getResultList();
		
		if ( listMonViagCtrFixo.size() > 0) {
			
			for ( int i = 0; i < listMonViagCtrFixo.size(); i++){
				
				String mes = "";
				String periodo = "";
				
				if (listMonViagCtrFixo.get(i).getMes().toString().trim().length() == 1){
					mes = "0" + listMonViagCtrFixo.get(i).getMes().toString().trim();
				} else {
					mes = listMonViagCtrFixo.get(i).getMes().toString().trim();
				}
				
				periodo = listMonViagCtrFixo.get(i).getAno().toString().trim() + "-" + mes + "-01";
						  
				
				// 2.1 método que atualiza o campo "checado = 1" 
				//  Atualiza a tabela tmp_placasfixas (pojo PlacasFixaPorContrato) se caso 
				// as placas forem a mesma passando checado para 1
				//Integer idPlacaPesq =  pesquisarPlacaFixo(listMonViagCtrFixo.get(i).getCavalo(), listMonViagCtrFixo.get(i).getIdCliente(), periodo ); 
				/*
				if ( idPlacaPesq > 0 ) {
					// tabela tmp_placasfixas
					PlacasFixaPorContrato pf = pfCtrService.findById(idPlacaPesq);
					
					pf.setChecado(1);
					pf.setCliente(listMonViagCtrFixo.get(i).getCliente());
					pf.setCnpj(listMonViagCtrFixo.get(i).getCnpj());
					pf.setMes(listMonViagCtrFixo.get(i).getMes());
					pf.setAno(listMonViagCtrFixo.get(i).getAno());
					pfCtrService.crud().update(pf);					
					
				}
				*/
				
				// 2.2 método de inserir registros na tabela que estão checada, isto é onde checado = 0 ( isto é as viagens não utilizaram estas placas ) 
				/*
				Criteria placaFixas = pfCtrService.crud().getSession().createCriteria(PlacasFixaPorContrato.class);
				placaFixas.add(Restrictions.eq("checado", 0));
				
				int resultPf = placaFixas.list().size();
				
				if (resultPf > 0 ) {
					
					listPlacasFixa = placaFixas.list();
					
					for ( int p = 0; p < listPlacasFixa.size() ; p++) {
						
						MonitoramentoViagensCtrFixo mon = new MonitoramentoViagensCtrFixo();
						 
						 mon.setIdCliente(listPlacasFixa.get(p).getIdCliente());
						 mon.setCliente(listPlacasFixa.get(p).getCliente());
						 mon.setCnpj(listPlacasFixa.get(p).getCnpj());
						 mon.setMes(listPlacasFixa.get(p).getMes());
						 mon.setAno(listPlacasFixa.get(p).getAno());
						 mon.setQtdViagens(0);						 
						 mon.setCavalo(listPlacasFixa.get(p).getPlacaFixa());
						 mon.setProduto(listPlacasFixa.get(p).getNomeProduto());
						 mon.setValor(listPlacasFixa.get(p).getVlrContrato());
						 
						 mvCtrService.crud().save(mon);
						
					}
					
				}
				*/
				// 2.3 método para inserir registros na tabela tmp_monitoramento MonitoramentoViagensCtrFixo
				
				MonitoramentoViagensCtrFixo mon1 = new MonitoramentoViagensCtrFixo();
				 
				 mon1.setIdCliente(listMonViagCtrFixo.get(i).getIdCliente());
				 mon1.setCliente(listMonViagCtrFixo.get(i).getCliente());
				 mon1.setCnpj(listMonViagCtrFixo.get(i).getCnpj());
				 mon1.setMes(listMonViagCtrFixo.get(i).getMes());
				 mon1.setAno(listMonViagCtrFixo.get(i).getAno());
				 mon1.setQtdViagens(listMonViagCtrFixo.get(i).getQtdViagens());						 
				 mon1.setCavalo(listMonViagCtrFixo.get(i).getCavalo());
				 mon1.setIdContrato(listMonViagCtrFixo.get(i).getIdContrato());
				 mon1.setIdProduto(listMonViagCtrFixo.get(i).getIdProduto());
				 
				 if (( listMonViagCtrFixo.get(i).getIdProduto() == 7 ) || (listMonViagCtrFixo.get(i).getIdProduto() == 8) ) {
					 
					// Contrato Produto 
					ContratoProduto ctrProd = ctrProdutoService.getProduto(
								listMonViagCtrFixo.get(i).getIdContrato(), 
								listMonViagCtrFixo.get(i).getIdProduto());
					
					BigDecimal vlrPROD = ctrProd.getVlr1Unitario();
					
					 	mon1.setValor(vlrPROD);
					 
					 	 mon1.setProduto(listMonViagCtrFixo.get(i).getProduto());
				 } else {
					 mon1.setProduto(listMonViagCtrFixo.get(i).getProduto());
				 }
				 
				 
				 
				 if( listMonViagCtrFixo.get(i).getIdProduto() == 8) {


					 
				 } else if (listMonViagCtrFixo.get(i).getIdProduto() == 7 ) {
					 
					 
					 
				 } else {
					 mon1.setValor(listMonViagCtrFixo.get(i).getValor());
				 }
				 
				 
				 
				 mvCtrService.crud().save(mon1);
				
				

			}
			
		}
		
	}
	
	
	
	/**
	 * 
	 * @author Francis Bueno
	 * Método em teste de execução, busca as placas fixas baseado num periodo inicial e final
	 * por ciclo 715 - Cadastro (16-mm-yyyy a 15-mm-yyyy ) e 716 - Monitoramento (01-mm-yyyy a 30-mm-yyyy)
	 * que chamando outro método (buscarPlacasFixasPorContratosExec) faz insert na tabela temporária tmp_placafixas 
	 */
	public void buscarPlacasFixas(){
		
		Calendar m_calendar = Calendar.getInstance();
		
		// Instancia m_calendar com base na data inicial (informado pelo usuario)
		m_calendar.setTime(dtInicial);		
		
		//### Var do método
		// Qtd de meses no ano (periodo 12 meses)
		int mesAno = 12;	
		
		// Recebe o numero do mês da data inicial 
		// (ex.: Fev = m_calendar.get(Calendar.MONTH) é igual 1 então + 1 fica 2 = fevereiro    
		int nMonth1 = m_calendar.get(Calendar.MONTH) + 1 ;
		
		// Qtd de meses entre a dtInicial e dtFinal (informado pelo Usuario)
		int meses = monthsBetween(dtInicial, dtFinal) + 1;		
		
		// Resultado para o primeiro Ano do periodo escolhido. ex.: mesAno(12) - (nMonth1(2)) + 1 = 10
		int mesesParaCompletarAno = mesAno - (nMonth1) + 1;

		//### Var do For
		int count=0;
		int countM = 0;
		int countOM = 0;
		int countYear = 0;
		
		//### For, passando mes a mes a realizando o insert na tabela
		for(int i = 0; i < meses; i++){
			
			// Meses processados
			count++;
			
			Calendar nextMount = Calendar.getInstance();

			/**
			 * O motivo pelo qual fiz isso é para calcular a quantidade de meses dentro do primeiro periodo.
			 * ex: se o usuario setar data inicial 01/03/2017 a 20/06/2020 
			 * Não tenho como saber qual é os primeiros 7 meses do processo e por qual mês é iniciado.
			 * Então desenvolvi esse método para que no futuro possa ser revisado e ajustado.
			 * O else é para os demais periodos.  
			 */
			// Parte do processamento do primeiro periodo
			if ( count <= (mesesParaCompletarAno) ) {
				
				// Primeiros Meses 
				countM++;
				
				//int nMonthPM = m_calendar.get(Calendar.MONTH) + countM ;
				//int t1a = nMonthPM;
				//int nYearPM = m_calendar.get(Calendar.YEAR) ;
				//int t1b = nYearPM;								
				
				// String de Passagem
				String dtInicio, dtFinal, periodo , mes, ano = "";
				
				String periodo715 , mes715, ano715 = "";
				
				// Buscar proximo Mes, recebe dtInicial e adiciona 1 para somar e recebe prox mes		
				Date df = nextMonth(dtInicial, 1);								
				
				// Formatador de data
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				
		        //String strDate = dateformat.format(df);  
				
				// Para a inclusão do primeiro mês dentro do primeiro periodo
				if(countM == 1){
					
					
					// ### Variaveis para parametros do método
					
					// Data Inicio
					Date dtI = m_calendar.getTime();
					dtInicio = dateformat.format(dtI);
					
					// Data Final, chama nextMonth passando dtInicial e a qtd de meses a ser adicionado.
					Date dtF = nextMonth(dtInicial , 1 );
					dtFinal = dateformat.format(dtF);
					
					//periodo = dtInicio.substring(8, 10) +"/" + dtInicio.substring(5, 7) + "/" + dtInicio.substring(0, 4);
					//dtInicio.substring(0, 4) + "-" + dtInicio.substring(5, 7) + "-" + dtInicio.substring(8, 10);
					
					// Periodo, data de inicio (informada pelo usuario)
					periodo = dtInicio;					
					
					// Mes, remove o mes da dtInicio, cfe sua posicao
					mes = dtInicio.substring(5, 7) ;
										
					// Ano, remove o ano da dtInicio, cfe sua posicao
					ano = dtInicio.substring(0, 4) ;					
					
					// Ciclo 716 -  Chamada do método passando os parametros
					buscarPlacasFixasPorContratosExec("716",dtInicio, dtFinal, periodo, mes, ano);
					
					// ### Ciclo 715
					// Data Inicial, chama lastMonth, passando data inicial e a quantidade de dias a ser subtraida da data inicial 
					Date dtI715 = lastMonth(dtInicial , 16 );
					dtInicio = dateformat.format(dtI715);
					
					// Data Final, chama nextMothDays, passando data inicial(dtI715) e a quantidade de dias a ser adicionada da data inicial
					Date dtF715 = nextMonthDays(dtI715 , 30 );
					dtFinal = dateformat.format(dtF715);
					
					mes715 = dtInicio.substring(5, 7) ;
					ano715 = dtInicio.substring(0, 4) ;
					
					periodo715 = ano715+"-"+mes715+"-"+"01";
					
					buscarPlacasFixasPorContratosExec("715" ,dtInicio, dtFinal, periodo715, mes715, ano715);
					
					
					
				} else {
					
					// ### para os demais periodos dentro do primeiro periodo
					
					// Data Inicio, chama nextMonth passando dtInicial e a qtd de meses. (Contagem do Mes deste periodo subtraindo 1)
					Date dtI = nextMonth(dtInicial , (countM -1 ) );
					dtInicio = dateformat.format(dtI);
					
					// Data Final, chama nextMonth passando dtInicial e a qtd de meses. (Contagem do Mes)
					Date dtF = nextMonth(dtInicial , (countM ) );
					dtFinal = dateformat.format(dtF);

					// Periodo, mes, e ano
					periodo = dtInicio;
					mes = dtInicio.substring(5, 7) ;
					ano = dtInicio.substring(0, 4) ;
					
					// Ciclo 716 - Chamada do método passando os parametros
					buscarPlacasFixasPorContratosExec("716",dtInicio, dtFinal, periodo, mes, ano);
					
					// ### Ciclo 715
					// Data Inicial, chama lastMonth, passando data inicial e a quantidade de dias a ser subtraida da data inicial 
					Date dtI715 = lastMonth(dtI , 16 );
					dtInicio = dateformat.format(dtI715);
					
					// Data Final, chama nextMothDays, passando data inicial(dtI715) e a quantidade de dias a ser adicionada da data inicial
					Date dtF715 = nextMonthDays(dtI715 , 30 );
					dtFinal = dateformat.format(dtF715);
					
					mes715 = dtInicio.substring(5, 7) ;
					ano715 = dtInicio.substring(0, 4) ;
					periodo715 = ano715+"-"+mes715+"-"+"01";
					
					buscarPlacasFixasPorContratosExec("715" ,dtInicio, dtFinal, periodo715, mes715, ano715);
				}
				
			} else {
				
				// ### Realiza a inclusão dos proximos periodos
				
				String dtInicio, dtFinal, periodo , mes, ano = "";
				String periodo715 , mes715, ano715 = "";
				
				// Formatador de datas
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				
				// Contador de meses deste periodo
				countOM++;	
				
				// Data Inicio, chama nextMonth passando dtInicial e a qtd de meses. (Contagem do Mes Geral de todos os periodo subtraindo 1)
				Date dtI = nextMonth(dtInicial, (count - 1 ));
				dtInicio = dateformat.format(dtI);
				
				// Data Final, chama nextMonth passando dtInicial e a qtd de meses. (Contagem do Mes deste periodo)
				Date dtF = nextMonth(dtInicial , (countM ) );
				dtFinal = dateformat.format(dtF);
				
				// Periodo, mes, e ano
				periodo = dtInicio;
				mes = dtInicio.substring(5, 7) ;
				ano = dtInicio.substring(0, 4) ;
				
				//### Ciclo 716 Chamada do método passando os parametros
				 buscarPlacasFixasPorContratosExec("716", dtInicio, dtFinal, periodo, mes, ano);
				
				// ### Ciclo 715
				// Data Inicial, chama lastMonth, passando data inicial e a quantidade de dias a ser subtraida da data inicial 
				Date dtI715 = lastMonth(dtI , 16 );
				dtInicio = dateformat.format(dtI715);
				
				// Data Final, chama nextMothDays, passando data inicial(dtI715) e a quantidade de dias a ser adicionada da data inicial
				Date dtF715 = nextMonthDays(dtI715 , 30 );
				dtFinal = dateformat.format(dtF715);
				
				mes715 = dtInicio.substring(5, 7) ;
				ano715 = dtInicio.substring(0, 4) ;
				periodo715 = ano715+"-"+mes715+"-"+"01";
				
				buscarPlacasFixasPorContratosExec("715" ,dtInicio, dtFinal, periodo715, mes715, ano715);
				
				// Contador dos periodos em meses deste periodo
				if(countOM == 12){
					// Será o mes quando o contador chegar a 12 meses contado
					countOM = 0;
					//countYear++;
				}
				
				
			}
		}
	}
	
	/**
	 * @author Francis Bueno
	 * @param dtInicio
	 * @param dtFinal
	 * @param periodo
	 * @param mes
	 * @param ano
	 * Método que busca as placas fixas que estão nos contratos e adiciona elas na tabela tmp_placasfixas.
	 */
	public void buscarPlacasFixasPorContratosExec(String ciclo, String dtInicio, String dtFinal, String periodo, String mes, String ano){
		
		// Reserva o entitymanager para ser executado apos a leitura da query 
		EntityManager em = usuService.crud().getEntityManager();		  	           
	    
		// Query que fará a pesquisa no banco de dados, recebendo os parametros
		String query =  " select row_number() over() as id, c.cliv_areaoid_cliente as idCliente , p.pes_documento1 as cnpj, p.pes_nome as cliente, "
		        + " c.clivoid as idPlaca, "
		        + " replace(c.cliv_vei_placa,'-','') as placaFixa,  0 as checado , "
		        + " coalesce((c.cliv_dt_exclusao between '" + dtInicio  + " 00:00:00.00-00' and '" + dtFinal  + " 00:00:00.00-00'),false) as excluida, "
		        + " '" + periodo +"' as periodo , '"+ mes +"' as mes , '" + ano +"' as ano , "
		        + " (select ( case when t2.conp_prodoid = 7 then t2.conp_valor1_unitario when t2.conp_prodoid = 8 then t2.conp_valor1_unitario  else 0 end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and  t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid ) as vlrContrato, "
		        + " (select ( case when t2.conp_prodoid = 7 then 7 when t2.conp_prodoid = 8 then 8  else 0 end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid ) as idProduto, "        
		        + " (select ( case when t2.conp_prodoid = 7 then 'Veículo Fixo Automático' when t2.conp_prodoid = 8 then 'Veículo Fixo Plano Mensal'  else '' end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid) as nomeProduto, t.conoid as numContrato "
		    + " from administrativo.cliente_veiculo c, java.area a , java.pessoa p, java.contrato t "
		        + " where  "
		        + "    c.cliv_areaoid_cliente = a.areaoid and " 
		        + "    a.area_pesoid_sacado = p.pesoid and    "                
		        + "    a.areaoid = t.con_areaoid and  "
		        + "    p.pesoid = t.con_pesoid and    "                
		        + "    t.con_enumoid_produto_tipo = 16 and "  
		        + "    t.con_enumoid_ciclo_faturamento in ("+ ciclo +") and "
		        + "    t.con_enumoid_status not in (650,767) and "
		        + "    c.cliv_dt_criacao < '" + dtFinal + " 00:00:00.00-00' and " 
		        + "    (c.cliv_dt_exclusao is null or c.cliv_dt_exclusao > '" + dtInicio + " 16:19:20.853-03') "
		        + "    order by c.cliv_areaoid_cliente, c.clivoid, periodo  ";    
				
		
		// Carrega a list listPlacasFixaPorContrato da classe PlacasFixaPorContrato com base na query
		listPlacasFixaPorContrato = em.createNativeQuery(query, "ListaPlacasFixaPorContratoMapping").getResultList();
		
		// Se houver registro na list começa o processo de insert
		if ( listPlacasFixaPorContrato.size() > 0) {
			
			// For que checa cada registro para inclusão
			for (int i=0; i < listPlacasFixaPorContrato.size() ; i++ ){

				// Instancia da classe PlacasFixaPorContrato
				PlacasFixaPorContrato pf = new PlacasFixaPorContrato();
				
				// Inclui na tabela apenas os veiculos que estão como não excluido nesse periodo informado
				if ( listPlacasFixaPorContrato.get(i).isExcluida() == false){
					
					// A instancia e seus campos recebem os valores da list listPlacasFixaPorContrato
					pf.setIdPlaca(listPlacasFixaPorContrato.get(i).getIdPlaca());
					pf.setIdCliente(listPlacasFixaPorContrato.get(i).getIdCliente());
					pf.setPlacaFixa(listPlacasFixaPorContrato.get(i).getPlacaFixa());
					pf.setChecado(listPlacasFixaPorContrato.get(i).getChecado());
					pf.setCnpj(listPlacasFixaPorContrato.get(i).getCnpj());
					pf.setCliente(listPlacasFixaPorContrato.get(i).getCliente());
					pf.setMes(listPlacasFixaPorContrato.get(i).getMes());
					pf.setAno(listPlacasFixaPorContrato.get(i).getAno());
					pf.setPeriodo(listPlacasFixaPorContrato.get(i).getPeriodo());
					pf.setExcluida(listPlacasFixaPorContrato.get(i).isExcluida());
					pf.setVlrContrato(listPlacasFixaPorContrato.get(i).getVlrContrato());
					pf.setIdProduto(listPlacasFixaPorContrato.get(i).getIdProduto());
					pf.setNomeProduto(listPlacasFixaPorContrato.get(i).getNomeProduto());
					pf.setNumContrato(listPlacasFixaPorContrato.get(i).getNumContrato());					
					
					// Salva os registros na tabela instanciada
					pfCtrService.crud().save(pf);

					
				}
								
			}			
			
		}
		
	}
	
	/**
	 * 1° método a ser executado
	 * Faz uma pesquisa buscando os veiculos fixo na tabela cliente_veiculo e adiciona (insere registros) na tabela
	 * tmp_placasfixas (pojo PlacasFixaPorContrato)
	 */
	public void buscarPlacasFixasPorContratos(){
				    
		// Inicio do Método 
		EntityManager em = usuService.crud().getEntityManager();		  	           
	    
		// Pesquisa na tabela cliente_veiculo aqui é a origem para busca e depois fazer o insert 
		String queryVB = " select  row_number() over() as id, clivoid as idPlaca, cliv_areaoid_cliente as idCliente,  "
				+ " replace(cliv_vei_placa,'-','') as placaFixa, 0 as checado , '' as cliente , '' as cnpj , 0 as mes, 0 as ano "
				+ " from cliente_veiculo "
				+ " where cliv_areaoid_cliente = 6663 and cliv_dt_fim_vigencia = 'infinity' "
				+ " order by cliv_vei_placa desc ";
		
		
		String query =  " select row_number() over() as id, c.cliv_areaoid_cliente as idCliente , p.pes_documento1 as cnpj, p.pes_nome as cliente, "
		        + " c.clivoid as idPlaca, "
		        + " replace(c.cliv_vei_placa,'-','') as placaFixa,  0 as checado , "
		        + " coalesce((c.cliv_dt_exclusao between '2020-02-01 16:19:20.853-03' and '2020-03-01 11:14:25.89-03'),false) as excluida, "
		        + " '01/02/2020' as periodo , '02' as mes , '2020' as ano , "
		        + " (select ( case when t2.conp_prodoid = 7 then t2.conp_valor1_unitario when t2.conp_prodoid = 8 then t2.conp_valor1_unitario  else 0 end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and  t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid ) as vlrContrato, "
		        + " (select ( case when t2.conp_prodoid = 7 then 7 when t2.conp_prodoid = 8 then 8  else 0 end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid ) as idProduto, "        
		        + " (select ( case when t2.conp_prodoid = 7 then 'Veículo Fixo Automático' when t2.conp_prodoid = 8 then 'Veículo Fixo Plano Mensal'  else '' end ) from java.contrato_produto t2, java.produto p where t2.conp_conoid = t.conoid and t2.conp_prodoid in (7,8) and t2.conp_produto_ativo= true and t2.conp_prodoid = p.prodoid) as nomeProduto, t.conoid as numContrato "
		    + " from administrativo.cliente_veiculo c, java.area a , java.pessoa p, java.contrato t "
		        + " where  "
		        + "    c.cliv_areaoid_cliente = a.areaoid and " 
		        + "    a.area_pesoid_sacado = p.pesoid and    "                
		        + "    a.areaoid = t.con_areaoid and  "
		        + "    p.pesoid = t.con_pesoid and    "                
		        + "    t.con_enumoid_produto_tipo = 16 and "  
		        + "    t.con_enumoid_ciclo_faturamento in (716) and "
		        + "    t.con_enumoid_status not in (650,767) and "
		        + "    c.cliv_dt_criacao < '2020-03-01 11:14:25.89-03' and " 
		        + "    (c.cliv_dt_exclusao is null or c.cliv_dt_exclusao > '2020-02-01 16:19:20.853-03') "
		        + "    order by c.cliv_areaoid_cliente, c.clivoid, periodo  ";    
				
		
		// Carreaga a list listPlacasFixaPorContrato 
		listPlacasFixaPorContrato = em.createNativeQuery(query, "ListaPlacasFixaPorContratoMapping").getResultList();
		
		// Se houver registro na list começa o processo de insert
		if ( listPlacasFixaPorContrato.size() > 0) {
			
			// For que checa cada registro para inclusão
			for (int i=0; i < listPlacasFixaPorContrato.size() ; i++ ){

				// Inclusão
				PlacasFixaPorContrato pf = new PlacasFixaPorContrato();
				
				if ( listPlacasFixaPorContrato.get(i).isExcluida() == false){

					//pf.setId(listPlacasFixaPorContrato.get(i).getId());
					pf.setIdPlaca(listPlacasFixaPorContrato.get(i).getIdPlaca());
					pf.setIdCliente(listPlacasFixaPorContrato.get(i).getIdCliente());
					pf.setPlacaFixa(listPlacasFixaPorContrato.get(i).getPlacaFixa());
					pf.setChecado(listPlacasFixaPorContrato.get(i).getChecado());
					pf.setCnpj(listPlacasFixaPorContrato.get(i).getCnpj());
					pf.setMes(listPlacasFixaPorContrato.get(i).getMes());
					pf.setAno(listPlacasFixaPorContrato.get(i).getAno());
					pf.setPeriodo(listPlacasFixaPorContrato.get(i).getPeriodo());
					pf.setExcluida(listPlacasFixaPorContrato.get(i).isExcluida());
					pf.setVlrContrato(listPlacasFixaPorContrato.get(i).getVlrContrato());
					pf.setIdProduto(listPlacasFixaPorContrato.get(i).getIdProduto());
					pf.setNomeProduto(listPlacasFixaPorContrato.get(i).getNomeProduto());
					pf.setNumContrato(listPlacasFixaPorContrato.get(i).getNumContrato());					
					
					
					pfCtrService.crud().save(pf);

					
				}
				
				

				
			}			
			
		}
		
	}
	
	
	/// INICIO DAS BUSCAS DE TODOS OS CLIENTES DENTRO DO ANO DE 2O2O A 2021 PARA VEICULOS FIXOS 
	
	
	
	

	// Inicio dos Getters e Setters
	public Date getDtInicioBusca() {
		return dtInicioBusca;
	}


	public Date getDtFinalBusca() {
		return dtFinalBusca;
	}


	public List<MonitoramentoViagensCtrFixo> getListMonViagCtrFixo() {
		return listMonViagCtrFixo;
	}


	public void setDtInicioBusca(Date dtInicioBusca) {
		this.dtInicioBusca = dtInicioBusca;
	}


	public void setDtFinalBusca(Date dtFinalBusca) {
		this.dtFinalBusca = dtFinalBusca;
	}


	public void setListMonViagCtrFixo(List<MonitoramentoViagensCtrFixo> listMonViagCtrFixo) {
		this.listMonViagCtrFixo = listMonViagCtrFixo;
	}


	public List<PlacasFixaPorContrato> getListPlacasFixaPorContrato() {
		return listPlacasFixaPorContrato;
	}


	public void setListPlacasFixaPorContrato(List<PlacasFixaPorContrato> listPlacasFixaPorContrato) {
		this.listPlacasFixaPorContrato = listPlacasFixaPorContrato;
	}


	public Date getDtInicial() {
		return dtInicial;
	}


	public Date getDtFinal() {
		return dtFinal;
	}


	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}


	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}


	public String getPreviousPage() {
		return previousPage;
	}


	public boolean isMostraEdicao() {
		return mostraEdicao;
	}


	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}


	public void setMostraEdicao(boolean mostraEdicao) {
		this.mostraEdicao = mostraEdicao;
	}


	public List<PlacasFixaPorContrato> getListPlacasFixa() {
		return listPlacasFixa;
	}


	public void setListPlacasFixa(List<PlacasFixaPorContrato> listPlacasFixa) {
		this.listPlacasFixa = listPlacasFixa;
	}
	
	
	
	
}
