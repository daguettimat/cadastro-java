package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.atualiza_motvei_chp.MotoristaClient;
import br.com.global5.infra.atualiza_motvei_chp.VeiculoClient;
import br.com.global5.infra.atualiza_motvei_chp.acVeiculoCad;
import br.com.global5.infra.atualiza_motvei_chp.acVeiculoCadT;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.ValidaBrasil;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.analise.acVeiculos;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.manager.model.cadastro.Marca;
import br.com.global5.manager.model.cadastro.Modelo;
import br.com.global5.manager.model.cadastro.Proprietario;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.VeiculoCategoria;
import br.com.global5.manager.model.enums.VeiculoTipo;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.auxiliar.CorService;
import br.com.global5.manager.service.cadastro.MarcaService;
import br.com.global5.manager.service.cadastro.ModeloService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.enums.VeiculoCategoriaService;
import br.com.global5.manager.service.enums.VeiculoTipoService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.template.exception.BusinessException;
import jdk.nashorn.internal.runtime.ECMAErrors;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

@Named
@ViewAccessScoped
public class VeiculoMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Veiculo> veiculoList;

	private Usuario usuario;

	private List<Modelo> lstModelo;
	private List<Marca> lstMarca;
    private List<Cor> lstCor;
    private List<VeiculoTipo> lstTipoVeiculo;
    private List<VeiculoCategoria> lstCategoriaVeiculo;

    private List<acVeiculos> lstacVeiculos;

	private List<Veiculo> filteredValue;
	private Integer id;
	private Integer numMarca = null;
	private Integer numModelo = null;
	private Integer numCor = null;
	private Integer numTipo = null;
	private boolean edit;

	private Veiculo veiculo;
	private Filter<Veiculo> filter = new Filter<Veiculo>(new Veiculo());
	
	//Variaveis para SelectItens
	private Integer numCodModelo;
	
	
	// Variaveis validação da placa
	private String numPlacaOld = "";
	private Boolean placaAlteradaMercosul = true;
	private Boolean aplicadoProcAltPlaca  = false;
	private String placaPadraoMercosul = "";
	private String placaAnterior = "";
	
	// Variaveis - Lista para ws consulta na CHP/Trafegus
    private List<acVeiculoCad> listaAcVeiculoCad;
    private List<acVeiculoCadT> listaAcVeiculoParaTrafegus;
	
	@Inject
	VeiculoService veiculoService;

	@Inject
    ModeloService modeloService;

	@Inject
    MarcaService marcaService;

	@Inject
    CorService corService;

	@Inject
    VeiculoTipoService veiculoTipoService;

	@Inject
    VeiculoCategoriaService veiculoCategoriaService;

	@Inject
    VeiculosService veiculosService;

	@Inject 
	private UsuarioService usuService;
	
	@Inject
	Crud<Veiculo> veiculoCrud;

	@Inject
	CrudService<Veiculo> veiculoCrudService;

	@PostConstruct
	public void init() {
		clear();

        lstMarca = marcaService.crud().listAll();
        lstCor = corService.crud().listAll();
        lstTipoVeiculo = veiculoTipoService.crud().listAll();
        lstCategoriaVeiculo = veiculoCategoriaService.crud().listAll();
        numCodModelo = null;

	}

	public LazyDataModel<Veiculo> getVeiculoList() {
		if( veiculoList == null ) {	
			
			veiculoList = new LazyDataModel<Veiculo>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Veiculo> load(int first, int pageSize,
											  String sortField, SortOrder sortOrder,
											  Map<String, Object> filters) {

					br.com.global5.infra.enumerator.SortOrder order = null;
					if( sortOrder != null ) {
						order = sortOrder.equals(SortOrder.ASCENDING) ? br.com.global5.infra.enumerator.SortOrder.ASCENDING
								: sortOrder.equals(SortOrder.DESCENDING) ? br.com.global5.infra.enumerator.SortOrder.DESCENDING
								: br.com.global5.infra.enumerator.SortOrder.UNSORTED;
					}
					filter.setFirst(first).setPageSize(pageSize)
							.setSortField(sortField).setSortOrder(order)
							.setParams(filters);
					List<Veiculo> list = veiculoService.paginate(filter);
					setRowCount(veiculoService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Veiculo getRowData(String key) {
					return veiculoService.findById(new Integer(key));
				}
			};

		}
		return veiculoList;
	}

	public void remove() {
		if( veiculo != null && veiculo.getId() != null) {
			veiculoService.remove(veiculo);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Veículo " + veiculo.getPlaca()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void onModelo(AjaxBehaviorEvent event){
		this.setNumModelo(this.veiculo.getModelo().getId());
	}
	
	public void update() {
		
		String msg;

		if (veiculo.getId() == null) {
			veiculo.setTipo(this.veiculo.getTipo());
			veiculoService.insert(veiculo);
			msg = "Veículo " + veiculo.getPlaca() + " criado com sucesso!";
			
		} else {
			
		    if(this.getNumMarca()!=null){
                this.veiculo.setMarca(new Marca(this.getNumMarca()));
            }
		    if(this.getNumModelo()!=null){
                this.veiculo.setModelo(new Modelo(this.getNumModelo()));
            }
		    if(this.getNumCor()!=null){
                this.veiculo.setCor(new Cor(this.getNumCor()));
            }
            if(this.getNumTipo()!=null){
                this.veiculo.setTipo(new VeiculoTipo(this.getNumTipo()));
            }
		    
            if(aplicadoProcAltPlaca == true ) {
            	this.veiculo.setPlaca(placaPadraoMercosul);
            	this.veiculo.setPlacaAnterior(placaAnterior);
            	this.veiculo.setDtPlacaConversao(new Date());
            	aplicadoProcAltPlaca = false;
            	placaAlteradaMercosul = true;
            }
                       
			veiculoService.update(veiculo);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formVeiculo:idConvertePlaca");
			
			msg = "Veículo " + veiculo.getPlaca() + " alterado com sucesso!";
			
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();

            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir veículo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		veiculo = new Veiculo();
		filter = new Filter<Veiculo>(new Veiculo());

		veiculo.setMarca(new Marca());
		veiculo.setModelo(new Modelo());
		veiculo.setCor(new Cor());
		veiculo.setTipo(new VeiculoTipo());
		

		usuario = checkUsuario.valid();
		
		id = null;
		edit = true;
		placaAlteradaMercosul = true;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}

		veiculo = veiculoCrud.get(id);
		
		if (veiculo.getPlacaAnterior() == null) {
			placaAlteradaMercosul = false;		
		} else {
			placaAlteradaMercosul = true;
		}
		
		if (veiculo == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}

        Hibernate.initialize(veiculo.getProprietario());
        Hibernate.initialize(veiculo.getProprietario().getEndereco());
        Hibernate.initialize(veiculo.getProprietario().getTelefone());

        try {
        	/*
            Hibernate.initialize(veiculo.getArrendatario());
            Hibernate.initialize(veiculo.getArrendatario().getEndereco());
            Hibernate.initialize(veiculo.getArrendatario().getTelefone());
            */
        } catch (Exception e) {}

        Hibernate.initialize(veiculo.getMarca());
        Hibernate.initialize(veiculo.getModelo());

		lstModelo = modeloService.crud().eq("marca.id", veiculo.getMarca().getId()).list();

        lstacVeiculos = veiculosService.crud().eq("veiculo.id",veiculo.getId()).list();
        Collections.sort(lstacVeiculos, new Comparator<acVeiculos>() {

            @Override
            public int compare(acVeiculos o1, acVeiculos o2) {

                return -o1.getAcCadastro().getId().compareTo(o2.getAcCadastro().getId());

            }
        });

//
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("formVeiculo:veiModelo");
	}

    public void showFicha(Integer id) {
        this.id = id;
        edit = false;
        findById(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Veículo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

	public Veiculo findPlaca(String placa) {
		veiculo = veiculoCrud.find().placa(placa);
		if( veiculo != null) {
			return veiculo;
		} else {
			return null;
		}
	}



    public void findModelo(AjaxBehaviorEvent event) {

        Integer marca = (Integer) ((UIOutput)event.getSource()).getValue();
        
        this.setNumMarca(marca);

        lstModelo = new ArrayList<Modelo>();
        
        lstModelo = modeloService.crud().criteria().eq("marca.id", marca)
                .list();

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiModelo");

    }
    
    public void onCor(){this.setNumCor(this.veiculo.getCor().getId());}
    public void onTipo(){this.setNumTipo(this.veiculo.getTipo().getId());}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Veiculo) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Veículos " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			placaAlteradaMercosul = true;
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista de veículos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	
	public void findVeiculo(AjaxBehaviorEvent event){
		
		String placa = (String) ((UIOutput) event.getSource()).getValue();
		
		String placaAnterior = veiculo.getPlacaAnterior();
		
		
		if (veiculo.getPlacaAnterior() == null) {
			findVeiculoPlaca(placa);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "A Placa: " + placa + " não será alterada." + 
					" Já existe um processo de conversão realizado na placa anterior: " + placaAnterior));
		}
		
		
	}
			
	public void findVeiculoPlaca(String placa) {
		 
		if (placa.length() > 0) {
			
			Veiculo vei = null; 
			
		 } 
		
	}
	/**
	 * Method: utilizado para o processo de conversão da placa convencional ex.:"ABC-1234" para placa padrão mercosul
	 * created at: fev/2019
	 * @author Francis Carlos
	 */
	public void converterPlaca(){
		
	  	String caracterPosicao = "";
    	
    	if( veiculo.getPlaca() != null){
    		
    		caracterPosicao = veiculo.getPlaca().substring(5,6);
    		
    		List<String> listaConvert = new ArrayList<>();    			
    			listaConvert.add(0, "A");
    			listaConvert.add(1, "B");
    			listaConvert.add(2, "C");
    			listaConvert.add(3, "D");
    			listaConvert.add(4, "E");
    			listaConvert.add(5, "F");
    			listaConvert.add(6, "G");
    			listaConvert.add(7, "H");
    			listaConvert.add(8, "I");
    			listaConvert.add(9, "J");
    		
    		String digConversao = Integer.toString(listaConvert.indexOf(caracterPosicao));
    		
    		String placaNew, placaOldP1, placaOldP2 , placaOldP3= "";
    		
    		// Recebe a placa anterior ao mercosul
    		StringBuilder myString = new StringBuilder(veiculo.getPlaca());
    		// troca o caractere da 5ª posição pelo id da listaConvert baseado na regra de conversão
    		myString.setCharAt(5, digConversao.charAt(1));			
    		
    		String charPlacaPos5 = listaConvert.get( Integer.valueOf(caracterPosicao)); 
    		
			// Montagem da formato Mercosul
    		placaOldP1 = myString.substring(0, 3);
			placaOldP2 = myString.substring(4, 5) + charPlacaPos5 ;
			placaOldP3 = myString.substring(6, 8) ;
			placaNew = placaOldP1 + placaOldP2 + placaOldP3;
    		
    		placaPadraoMercosul = placaNew;
    		placaAnterior = veiculo.getPlaca();
    		aplicadoProcAltPlaca = true;
    		
    		RequestContext context = RequestContext.getCurrentInstance();
    		context.update("formVeiculo:dlgConvertePlaca");
    	}
    	
		
	}
	
	// Projeto atualizar dados CHP/Trafegus
	/**
	 * @author Francis Bueno
	 * @return
	 * Método de chamada do front, carrega todos os motorista na tabela listaAcMotoristaCad e só para que após possa ser atualizado
	 * Utilizado pela tela /webapp/pages/ws/trafegus/chamada_ws_chp.xhtml -- Atualiza Motorista, Atualiza Veiculo (esse será outra chamada)
	 */
    public String buscaVeiculoPorPlaca(){
    	
    	EntityManager em = usuService.crud().getEntityManager();
    	
    	String query = " select v.veioid as id, v.vei_placa as placa, v.vei_uf as uf, v.vei_municipio as municipio, v.vei_pais as pais, "
    		       	 + " v.vei_renavam as renavam, v.vei_chassi as chassi, v.vei_anofabricacao as anoFabricacao, v.vei_anomodelo as anoModelo, "
    		       	 + " c.cor_nome as cor "
    		       	 + " from veiculo  v, cor c " 
    		         + " where v.vei_coroid = c.coroid and v.vei_dt_criacao > '2017/12/01' ";
    	
    	listaAcVeiculoCad = em.createNativeQuery(query, "ListaVeiculoCadMapping").getResultList();
    	
    	if ( listaAcVeiculoCad.size() > 0){
    		
    		int result = listaAcVeiculoCad.size();
    		
    		for(int i=0; i < result;i++){
    			    			
    			buscarVeiculoPorPlaca(listaAcVeiculoCad.get(i).getPlaca().toString());    				
    			
    		}
    		
    	}    	
    	
    	return null;
    }
    
    
    private void buscarVeiculoPorPlaca(String placa){
    	
    	VeiculoClient wsV = new VeiculoClient();

    	// Busca dados do veiculo para checar se o veiculo existe na chp/Trafegus
    	JSONObject objJsonMot = wsV.getVeiculo(placa);

    	if( objJsonMot != null){
    		
    		String idVeiculoChp = "";
    		
    		idVeiculoChp = objJsonMot.get("codigo").toString();
    		
    		// Dados coletado do ws  192.168.64.101/ws_rest/public/api/veiculo/{ placa}
    		objJsonMot.get("codigo").toString();	// id do veiculo na chp
    		objJsonMot.get("placa").toString();		// placa do veiculo na chp
    		    		
    		// Atualiza dados do veiculo no sistema CHP/Trafegus
    		
    		//atualizarMotoristaCHP(cpf, idMotoristaChp);
    		
    				
    	}
    	
    }
    /**
     * teste atualiza veiculos 23-04-2021
     */
    public void buscarVeiculosPorPlacasCHP(){
    	
    	EntityManager em = usuService.crud().getEntityManager();
    	
    	String query  = " select veioid as id, vei_placa as placa , vei_renavam as renavam , vei_chassi as chassi , "
    				  + "vei_municipio as municipio, vei_uf as uf, vei_pais as pais, vei_anofabricacao as anoFabricacao, "
    				  + " vei_anomodelo as anoModelo, vei_nacional as nacional,"
    				  + " (case  when vei_tipo = 59 then '1' else '2' end) as tipo, "
    				  + " (select mar_nome  from marca where maroid = vei_maroid ) as marca,"
    				  + " (select mod_nome from modelo where modoid = vei_modoid ) as modelo,"
    				  + " (select cor_nome from cor where coroid = vei_coroid) as cor"
    				  + " from veiculo "
    				  + " where "
    				  + " vei_dt_verificacao_ws is null and "
    				  + " vei_dt_atualiza_ws is null and "
    				  + " vei_dt_criacao > '2021-06-01'";
    	
    	//21/04/2020
    	
    	listaAcVeiculoCad = em.createNativeQuery(query, "ListaVeiculoCadMapping").getResultList();
    	
    	if ( listaAcVeiculoCad.size() > 0) {
    		
    		int result = listaAcVeiculoCad.size();
    		
    		for ( int i = 0 ; i < result ; i++ ){
    			
    			int tamanhoPlaca = listaAcVeiculoCad.get(i).getPlaca().length();
    				
    				boolean t = listaAcVeiculoCad.get(i).getPlaca().contains("-");
    				//boolean c = listaAcVeiculoCad.get(i).getPlaca().matches("-");    				
    				//boolean d = t;
    				
    				String novaPlaca = "";
    				Integer idPlaca = null;
    				if ( t == true ){
    					novaPlaca = listaAcVeiculoCad.get(i).getPlaca().replace("-", "").toString().trim();
    					idPlaca = listaAcVeiculoCad.get(i).getId();
    					String te = novaPlaca;
    					
    				} else {
    					novaPlaca = listaAcVeiculoCad.get(i).getPlaca().toString().trim();
    					idPlaca = listaAcVeiculoCad.get(i).getId();			
    				}
    					buscarVeiculoPorPlaca(idPlaca, novaPlaca);
    			
    		}
    		
    	}
    	
    }
    
    private void buscarVeiculoPorPlaca(Integer idPlaca, String placa){
    	
    	VeiculoClient wsV = new VeiculoClient();
    	
    	 JSONObject objJsonVei = wsV.getVeiculo(placa);
    	 
    	 if ( objJsonVei != null ) {
    		 
    		 String idVeiculoChp = "";
    		 
    		 idVeiculoChp = objJsonVei.get("codigo").toString();
    		 
    		 objJsonVei.get("codigo").toString();
    		 objJsonVei.get("placa").toString();
    		 objJsonVei.get("tipo_veiculo").toString();
    		 objJsonVei.get("cor").toString();
    		 objJsonVei.get("marca").toString();
    		 objJsonVei.get("modelo").toString();
    		 objJsonVei.get("ano_modelo").toString();
    		 objJsonVei.get("ano_fabricacao").toString();
    		 objJsonVei.get("renavam").toString();
    		 objJsonVei.get("chassi").toString();
    		 objJsonVei.get("sigla_estado").toString();
    		 objJsonVei.get("cidade_emplacamento").toString();
    		 objJsonVei.get("pais").toString();    		 
    		 
    		 atualizarVeiculoNaChp(idPlaca, placa, idVeiculoChp);
    		 
    	 } else {
    		 
    		 // Checar oque fará o sistema quando passar por aqui... 
    		 
    		 // Atualiza o registro no cadastro, informando que consultou ws mas não teve
    		 // nada para ser atualizado campo dtAtualizaWs = null e dtVerificaoWs com Date
    		 Veiculo vei = veiculoService.findById(idPlaca);
    		 
    		 //vei.setDtAtualizacaoWs(null);
    		 vei.setDtVerificacaoWs(new Date());
    		 veiculoService.crud().update(vei);
    		 veiculoService.commit();
    		 
    	 }
    	
    }
    
    private void atualizarVeiculoNaChp(Integer idPlaca, String placa, String idVeiculoChp ){
    	
    	EntityManager em = usuService.crud().getEntityManager();
    	
    	String query  = " select veioid as id, vei_placa as placa , vei_renavam as renavam , vei_chassi as chassi , "
    				  + "vei_municipio as municipio, vei_uf as uf, vei_pais as pais, vei_anofabricacao as anoFabricacao, "
    				  + " vei_anomodelo as anoModelo, vei_nacional as nacional,"
    				  + " (case  when vei_tipo = 59 then '1' else '2' end) as tipo, "
    				  + " (select mar_nome  from marca where maroid = vei_maroid ) as marca,"
    				  + " (select mod_nome from modelo where modoid = vei_modoid ) as modelo,"
    				  + " (select cor_nome from cor where coroid = vei_coroid) as cor"
    				  + " from veiculo "
    				  + " where "
    				  + " vei_dt_verificacao_ws is null and vei_dt_atualiza_ws is null and "
    				  + " veioid = " + idPlaca + " and" 
    				  + " vei_dt_criacao > '2021-06-01'";
    	    	
    	listaAcVeiculoParaTrafegus = em.createNativeQuery(query, "ListaVeiculoCadTMapping").getResultList();
    	
    	VeiculoClient wsV = new VeiculoClient();
    	
    	// Variaveis do métod
    	
    	Integer idVeiculoCad = null;
    	String 	placaVei = null, tipoVei = null, corVei = null, marcaVei = null , modeloVei = null,
    			anoModeloVei = null, anoFabricacaoVei = null, renavanVei = null, chassiVei = null,
    			siglaEstadoVei = null, cidadeEmplacamentoVei = null, paisVeiculo = null ;
    	
    	if ( listaAcVeiculoParaTrafegus.size() == 1) {
    		
    		idVeiculoCad = listaAcVeiculoParaTrafegus.get(0).getId();
    		
    		if( listaAcVeiculoParaTrafegus.get(0).getPlaca() != null){
    			
    			if(listaAcVeiculoParaTrafegus.get(0).getPlaca().toString().contains("-")){
        			placaVei  = listaAcVeiculoParaTrafegus.get(0).getPlaca().toString().replace("-", "");	
        		} else {
        			placaVei  = listaAcVeiculoParaTrafegus.get(0).getPlaca().toString();
        		}        		
    			
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getTipo() != null){
    			tipoVei = listaAcVeiculoParaTrafegus.get(0).getTipo().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getCor() != null){
    			corVei = listaAcVeiculoParaTrafegus.get(0).getCor().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getMarca() != null){
    			marcaVei = listaAcVeiculoParaTrafegus.get(0).getMarca().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getModelo() != null){
    			modeloVei = listaAcVeiculoParaTrafegus.get(0).getModelo().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getAnoModelo() != null){
    			anoModeloVei = listaAcVeiculoParaTrafegus.get(0).getAnoModelo().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getAnoFabricacao() != null){
    			anoFabricacaoVei = listaAcVeiculoParaTrafegus.get(0).getAnoFabricacao().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getRenavam() != null){
    			renavanVei = listaAcVeiculoParaTrafegus.get(0).getRenavam().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getChassi() != null){
    			chassiVei = listaAcVeiculoParaTrafegus.get(0).getChassi().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getUf() != null){
    			siglaEstadoVei = listaAcVeiculoParaTrafegus.get(0).getUf().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getMunicipio() != null){
    			cidadeEmplacamentoVei = listaAcVeiculoParaTrafegus.get(0).getMunicipio().toString();
    		}
    		if( listaAcVeiculoParaTrafegus.get(0).getPais() != null){
    			paisVeiculo = listaAcVeiculoParaTrafegus.get(0).getPais().toString();
    		}
    		
    		boolean result = wsV.putVeiculo(idVeiculoChp, placaVei, tipoVei, corVei, marcaVei, modeloVei, anoModeloVei, 
    					   					anoFabricacaoVei, renavanVei, chassiVei, siglaEstadoVei, (cidadeEmplacamentoVei+" - "+siglaEstadoVei), paisVeiculo);
    		
    		if ( result == true ){
    			// Atualiza o banco de dado tabela veiculo 
    			Integer v = idVeiculoCad;
    			Integer b = v;
    			Veiculo vei = veiculoService.findById(idVeiculoCad);
    			vei.setDtVerificacaoWs(new Date());
    			vei.setDtAtualizacaoWs(new Date());
    			veiculoService.crud().update(vei);
    			veiculoService.commit();
    		} else {
    			Veiculo vei = veiculoService.findById(idVeiculoCad);
    			vei.setDtVerificacaoWs(new Date());
    			//vei.setDtAtualizacaoWs(null);
    			veiculoService.crud().update(vei);
    			veiculoService.commit();    			
    		}
    		
    	}
    	
    }
    
    
    private void atualizarMotoristaCHP(String cpf, String idMotoristaChp){
    	
       	EntityManager em = usuService.crud().getEntityManager();
        	
        	String query =  " select v.veioid as id, v.vei_placa as placa, v.vei_uf as uf, v.vei_municipio as municipio,"
        		 + " v.vei_pais as pais, "
   		       	 + " v.vei_renavam as renavam, v.vei_chassi as chassi, v.vei_anofabricacao as anoFabricacao, v.vei_anomodelo as anoModelo, "
   		       	 + " c.cor_nome as cor "
   		       	 + " from veiculo  v, cor c " 
   		         + " where v.vei_coroid = c.coroid and v.vei_dt_criacao > '2017/12/01' ";
        			       	
        
        	listaAcVeiculoParaTrafegus = em.createNativeQuery(query, "ListaVeiculoCadMapping").getResultList();
        	
        	VeiculoClient wsV = new VeiculoClient();        	
        	
        	Integer idVeiCad = null;
        	String placaVei = "" , ufVei = "", municipioVei = "", paisVei = "", renavamVei = "", chassiVei ;
        	String anoFabricacaoVei = null, anoModeloVei = null;
        	String corVei = "";      	
        	
        	if( listaAcVeiculoParaTrafegus.size() == 1 ){
        		
        		// Carga de Variaveis para serem atualizadas no sistema Trafegus
        		idVeiCad= listaAcVeiculoParaTrafegus.get(0).getId();    		
        		
        		if(listaAcVeiculoParaTrafegus.get(0).getPlaca() != null){
            		if(listaAcVeiculoParaTrafegus.get(0).getPlaca().toString().contains("-")){
            			placaVei  = listaAcVeiculoParaTrafegus.get(0).getPlaca().toString().replace("-", "");	
            		} else {
            			placaVei  = listaAcVeiculoParaTrafegus.get(0).getPlaca().toString();
            		}        			
        		}
        		
        		if( listaAcVeiculoParaTrafegus.get(0).getUf() != null){
        			ufVei = listaAcVeiculoParaTrafegus.get(0).getUf().toString(); 
        		}    			
        		
        		if (listaAcVeiculoParaTrafegus.get(0).getMunicipio() != null ){
        			municipioVei   = listaAcVeiculoParaTrafegus.get(0).getMunicipio().toString();
        		}    		
        		
        		if ( listaAcVeiculoParaTrafegus.get(0).getPais() != null){
        			paisVei	= listaAcVeiculoParaTrafegus.get(0).getPais().toString();
        		}    		
        		
        		if( listaAcVeiculoParaTrafegus.get(0).getRenavam() != null){
        			renavamVei  = listaAcVeiculoParaTrafegus.get(0).getRenavam().toString();    		
        		}
        		
        		if( listaAcVeiculoParaTrafegus.get(0).getChassi() != null){
        			chassiVei  = listaAcVeiculoParaTrafegus.get(0).getChassi().toString();    		
        		}

        		if( listaAcVeiculoParaTrafegus.get(0).getAnoFabricacao() != null){
        			anoFabricacaoVei  = listaAcVeiculoParaTrafegus.get(0).getAnoFabricacao().toString();    		
        		}        		

        		if( listaAcVeiculoParaTrafegus.get(0).getAnoModelo() != null){
        			anoModeloVei  = listaAcVeiculoParaTrafegus.get(0).getAnoModelo().toString();    		
        		}       
        		
        		if( listaAcVeiculoParaTrafegus.get(0).getCor() != null){
        			corVei  = listaAcVeiculoParaTrafegus.get(0).getCor().toString();    		
        		}       
        		
        		// Envia dados para atualizar sistema Trafegus via ws | put
        		//boolean result = wsV.putVeiculo(placaVei, ufVei, municipioVei, paisVei, renavamVei, chassiVei, anoFabricacaoVei, 
        		//								anoModeloVei, corVei);
        		boolean result = true;
        		
        		 if (result == true){    			 
        			 // Atualiza o banco de dados veiculo
        			 Veiculo vei = veiculoService.findById(idVeiCad);
        			 /*
        			 mot.setDtAtualizaWs(new Date());
        			 mot.setDtVerificacaoWs(new Date());
        			 //mot.setExisteRegistroWs(true);
        			 motService.crud().update(mot);
        			 //motService.commit();
        			  
        			  * */
        			 

        		 } else {
        			 /*
        			 Motorista mot = motService.findById(idMotCad);
        			 mot.setDtAtualizaWs(null);
        			 mot.setDtVerificacaoWs(new Date());
        			 motService.crud().update(mot);
        			 //motService.commit();
        			  * 
        			  */
        		 }
        		 
        	}
        	
        }    
    
	public void onRowUnselect(UnselectEvent event) {
		veiculo = new Veiculo();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setVeiculoList(LazyDataModel<Veiculo> veiculoList) {
		this.veiculoList = veiculoList;
	}

	public List<Veiculo> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Veiculo> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Filter<Veiculo> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Veiculo> filter) {
		this.filter = filter;
	}

	public VeiculoService getVeiculoService() {
		return veiculoService;
	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	public Crud<Veiculo> getVeiculoCrud() {
		return veiculoCrud;
	}

	public void setVeiculoCrud(Crud<Veiculo> veiculoCrud) {
		this.veiculoCrud = veiculoCrud;
	}

	public CrudService<Veiculo> getVeiculoCrudService() {
		return veiculoCrudService;
	}

	public void setVeiculoCrudService(CrudService<Veiculo> veiculoCrudService) {
		this.veiculoCrudService = veiculoCrudService;
	}

    public List<Modelo> getLstModelo() {
        return lstModelo;
    }

    public void setLstModelo(List<Modelo> lstModelo) {
        this.lstModelo = lstModelo;
    }

    public List<Marca> getLstMarca() {
        return lstMarca;
    }

    public void setLstMarca(List<Marca> lstMarca) {
        this.lstMarca = lstMarca;
    }

    public List<Cor> getLstCor() {
        return lstCor;
    }

    public void setLstCor(List<Cor> lstCor) {
        this.lstCor = lstCor;
    }

    public List<VeiculoTipo> getLstTipoVeiculo() {
        return lstTipoVeiculo;
    }

    public void setLstTipoVeiculo(List<VeiculoTipo> lstTipoVeiculo) {
        this.lstTipoVeiculo = lstTipoVeiculo;
    }

    public List<VeiculoCategoria> getLstCategoriaVeiculo() {
        return lstCategoriaVeiculo;
    }

    public void setLstCategoriaVeiculo(List<VeiculoCategoria> lstCategoriaVeiculo) {
        this.lstCategoriaVeiculo = lstCategoriaVeiculo;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }


	public ModeloService getModeloService() {
        return modeloService;
    }

    public void setModeloService(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    public MarcaService getMarcaService() {
        return marcaService;
    }

    public void setMarcaService(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    public CorService getCorService() {
        return corService;
    }

    public void setCorService(CorService corService) {
        this.corService = corService;
    }

    public VeiculoTipoService getVeiculoTipoService() {
        return veiculoTipoService;
    }

    public void setVeiculoTipoService(VeiculoTipoService veiculoTipoService) {
        this.veiculoTipoService = veiculoTipoService;
    }

    public VeiculoCategoriaService getVeiculoCategoriaService() {
        return veiculoCategoriaService;
    }

    public void setVeiculoCategoriaService(VeiculoCategoriaService veiculoCategoriaService) {
        this.veiculoCategoriaService = veiculoCategoriaService;
    }

    public List<acVeiculos> getLstacVeiculos() {
        return lstacVeiculos;
    }

    public void setLstacVeiculos(List<acVeiculos> lstacVeiculos) {
        this.lstacVeiculos = lstacVeiculos;
    }

    public VeiculosService getVeiculosService() {
        return veiculosService;
    }

    public void setVeiculosService(VeiculosService veiculosService) {
        this.veiculosService = veiculosService;
    }

	public Integer getNumCodModelo() {
		return numCodModelo;
	}

	public void setNumCodModelo(Integer numCodModelo) {
		this.numCodModelo = numCodModelo;
	}

	public Integer getNumMarca() {
		return numMarca;
	}

	public void setNumMarca(Integer numMarca) {
		this.numMarca = numMarca;
	}

	public Integer getNumModelo() {
		return numModelo;
	}

	public void setNumModelo(Integer numModelo) {
		this.numModelo = numModelo;
	}

	public Integer getNumCor() {
		return numCor;
	}

	public void setNumCor(Integer numCor) {
		this.numCor = numCor;
	}

	public Integer getNumTipo() {
		return numTipo;
	}

	public void setNumTipo(Integer numTipo) {
		this.numTipo = numTipo;
	}

	public String getNumPlacaOld() {
		return numPlacaOld;
	}

	public void setNumPlacaOld(String numPlacaOld) {
		this.numPlacaOld = numPlacaOld;
	}

	public Boolean getPlacaAlteradaMercosul() {
		return placaAlteradaMercosul;
	}

	public void setPlacaAlteradaMercosul(Boolean placaAlteradaMercosul) {
		this.placaAlteradaMercosul = placaAlteradaMercosul;
	}

	public String getPlacaPadraoMercosul() {
		return placaPadraoMercosul;
	}

	public void setPlacaPadraoMercosul(String placaPadraoMercosul) {
		this.placaPadraoMercosul = placaPadraoMercosul;
	}

	public String getPlacaAnterior() {
		return placaAnterior;
	}

	public void setPlacaAnterior(String placaAnterior) {
		this.placaAnterior = placaAnterior;
	}

	public Boolean getAplicadoProcAltPlaca() {
		return aplicadoProcAltPlaca;
	}

	public void setAplicadoProcAltPlaca(Boolean aplicadoProcAltPlaca) {
		this.aplicadoProcAltPlaca = aplicadoProcAltPlaca;
	}

	public List<acVeiculoCad> getListaAcVeiculoCad() {
		return listaAcVeiculoCad;
	}

	public void setListaAcVeiculoCad(List<acVeiculoCad> listaAcVeiculoCad) {
		this.listaAcVeiculoCad = listaAcVeiculoCad;
	}

	public List<acVeiculoCadT> getListaAcVeiculoParaTrafegus() {
		return listaAcVeiculoParaTrafegus;
	}

	public void setListaAcVeiculoParaTrafegus(List<acVeiculoCadT> listaAcVeiculoParaTrafegus) {
		this.listaAcVeiculoParaTrafegus = listaAcVeiculoParaTrafegus;
	}

    
}
