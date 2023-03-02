package br.com.global5.manager.bean.analise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import br.com.global5.infra.Crud;
import br.com.global5.infra.fourinf.MotoristaFourInf;
import br.com.global5.infra.fourinf.MotoristaInfFinanceiraFourInf;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.FichaFinanceiraMotorista;
import br.com.global5.manager.model.analise.Vitimologia;
import br.com.global5.manager.model.analise.acMercadoria;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.analise.acPerguntas;
import br.com.global5.manager.model.analise.acReferencia;
import br.com.global5.manager.model.analise.acVitimologia;
import br.com.global5.manager.model.cadastro.CNH;
import br.com.global5.manager.model.contrato.Contrato;
import br.com.global5.manager.model.contrato.RegraSeguradoraMotorista;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.ReferenciasCategoria;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import br.com.global5.manager.model.geral.ContratoProduto;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MercadoriaService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PerguntasService;
import br.com.global5.manager.service.analise.ReferenciaService;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.analise.VitimologiaService;
import br.com.global5.manager.service.cadastro.CNHService;
import br.com.global5.manager.service.cadastro.ContratoProdutoService;
import br.com.global5.manager.service.cadastro.ContratoService;
import br.com.global5.manager.service.cadastro.FichaFinanceiraMotoristaService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import br.com.global5.manager.service.enums.ReferenciasCategoriaService;
import br.com.global5.manager.service.externos.ConsultaPessoaService;
import br.com.global5.manager.service.geral.RegraSeguradoraMotoristaService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class MotAnaliseMB  implements Serializable{

	private static final long serialVersionUID = 1L;
	private LazyDataModel<acMotorista> motList;
	private List<acMotorista> filteredValue;

	private acMotorista anacmot;
	private ConsultaPessoa consultaPessoa;
    private acReferencia refPessoal;
    
    private acMercadoria anacmerc;
    private RegraSeguradoraMotorista regSegMot;
    
    private Vitimologia vitmot;
    private FichaFinanceiraMotorista fichaFinanc;
    private List<ReferenciasAvaliacao> lstReferenciasVitimo;
    private List<ReferenciasAvaliacao> lstReferenciasFichaFinac;
    
    private CNH cnh;

	private Filter<acMotorista> filter = new Filter<acMotorista>(new acMotorista());
    private List<ReferenciasAvaliacao> lstReferencias;
    private List<acReferencia> lstReferenciasPessoais;
    private List<acReferencia> lstReferenciasComerciais;

    private List<acPerguntas> lstPerguntas;
    private List<ReferenciasCategoria> lstCategorias;

    private Map<String,Object> options;
    private String textoReferencia;

    private Integer idRef;
    private Integer img;
    private Integer id;
    private boolean erro;

    private Integer Criminal;
    private Integer Pessoal;

    private Integer statusCNH;
    private Integer validadeCNH;
    private Integer pontosCNH;
    private Integer categoriaCNH;

    private MapModel geoModel;
    private MapModel revGeoModel;

    private String centerGeoMap = "-15.7801, -54.9292";
    private String zoomMap = "2";

    private boolean comercial;
    private boolean tipoArqCnhPdf;
    
    // Vitimologia
    private Integer Vitimo;
    private boolean analiseVitimologia = false;
    private Integer fichaVit = null;
    private String  ufVit = "";
    private String  reqVit = "";
    private String  chaveVit = "";
    private String  statusVit = "Pendente";
    private Integer statusVitIcon = 39;
    private Integer idStatusVitimologia = null;
    private String  descPendVitimologia = "";
    private String 	dateWsVit = null;
    private List<Vitimologia> listVitPorAnalise = null;
    private List<FichaFinanceiraMotorista> listFFinancPorAnalise = null;
    private boolean openVitimologia = true;
    
    private boolean openFichaFinanceira = true;
    private Integer fichaFinacIntAvaliacao = null;
    private Integer idStatusFichaFinac = null;
    private String  descPendFichaFinac = "";

    Integer iconMercadoria = null;
    Integer resIconMercadoria = null;
    Integer iconValidaEmpresa = null;
    Integer resIconValidaEmpresa = null;
    Integer iconUfRGUfPlaca = null;
    Integer resIconUfRGUfPlaca = null;
    Integer iconVeiPropMot  = null;
    Integer resIconVeiPropMot = null;
    Integer iconVeiPropEmp	= null;
    Integer resIconVeiPropEmp = null;
    Integer iconComprovRes  = null;
    boolean blockDownComprovRes = false;
    Integer qtdRecMin = null;
    Integer resQtdRecMin = null;
    Integer recMinMeses = null;
    String ufPlaca = "";
    String ufPlacaReb1 = "";
    String ufPlacaReb2 = "";
    String ufPlacaReb3 = "";
    String docPropVei = "";
    String docMtzFil = "";
    private UploadedFile imgFile = null;
    private boolean changeImgFile = false;
    private boolean boolVinculoMotorista3 = true;
    private boolean boolFichaComVeiculo = true;
    
	@Inject
	MotoristaService motService;

	@Inject
    ReferenciasAvaliacaoService refService;

	@Inject
	ReferenciasAvaliacaoService refVitService;
	
	@Inject
	ReferenciaService referenciaService;

	@Inject
	PerguntasService perguntasService;

	@Inject
    ReferenciasCategoriaService refCatService;

	@Inject
	RegraSeguradoraMotoristaService regSegMotService;
	
	@Inject
	CadastroService cadService;
	
	@Inject
    ConsultaPessoaService conPessoaService;

	@Inject
    CNHService cnhService;
	
	@Inject
	private MercadoriaService acMercService;
	
	@Inject
	private br.com.global5.manager.service.geral.MercadoriaService merService;
	
	@Inject
	private VitimologiaService vitService;
	
	@Inject
	private FichaFinanceiraMotoristaService fFinancService;
	
	@Inject
	private ContratoService ctrService;  
	
	@Inject
	private ContratoProdutoService ctrPrdService;
	
	@Inject
	Crud<RegraSeguradoraMotorista> regSecCrud;
	
	@Inject
	private VeiculoService veiService;
	
	@Inject
	private VeiculosService acVeiService;
	
	@PostConstruct
	public void init() {

        options = new HashMap<String, Object>();
        options.put("draggable", true);
        options.put("modal", true);

        options.put("contentWidth", "98%");
        options.put("height", "70%");
        options.put("contentheight", "90%");

        options.put("resizable", false);
        options.put("closable",true);

        erro = false;

        lstReferencias = refService.listAll();
        lstCategorias  = refCatService.listAll();

        geoModel = new DefaultMapModel();
        zoomMap = "2";
        
        //listVitPorAnalise = null;

        loadParameters();
        
        checarVitimologiaCliente();
        checarFichaFinanceira();

	}

    public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();

        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            zoomMap = "18";

            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
    }

	public void loadParameters() {
        try {
            Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            id = Integer.valueOf(requestParams.get("id").toString());
            if( id != null ) {
                //findByCadastro(id);
//                try {
//                    FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml");
//                } catch (IOException e) {
//                    FacesContext.getCurrentInstance().addMessage(
//                            null,
//                            new FacesMessage(FacesMessage.SEVERITY_INFO,"acMotorista " + getId()
//                                    + " não pode ser carregado. Informe ao suporte técnico.",null));
//                }
            }
        } catch (Exception e) {}
    }

	public String printTexto(String resposta) {
        return onResposta(resposta,1);
    }

    public String printFlag(String resposta) {
        return onResposta(resposta, 2);
    }

    public String onResposta(String resposta, int tipo) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String[] pairs = resposta.replace("{","").replace("}","").replace(" ","").split(",");
            for (int i = 0; i < pairs.length; i++) {
                String pair = pairs[i];
                String[] keyValue = pair.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
            String flag = "";
            switch (tipo) {
                case 1:
                    flag = map.get("Resposta");
                    break;
                case 2:
                    flag =  pathFlag(Integer.valueOf(map.get("Avaliacao")));
                    break;
                case 3:
                    flag = map.get("Avaliacao");
                    break;
            }
            return flag;
        } catch (Exception e) {
            switch (tipo) {
                case 2:
                    return "images/ref_sign_question.png";
                default:
                    return "";
            }
        }
    }


    public void onRowEdit(RowEditEvent event) {

    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Resposta cancelada", ((acPerguntas) event.getObject()).getPergunta().getTexto());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    
    public void openDlgReferencias() {

	    if( anacmot.getStatusPessoal().getId() == 39 ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgVazio').show();onTop('dlgVazio');");
            return;
        }

        lstReferenciasPessoais = referenciaService.crud().eq("acCadastro.id", id)
                .and().eq("categoria.id", 31).list();

        comercial = false;
        options.put("width", "70%");
        RequestContext.getCurrentInstance().openDialog("dlgAnaliseReferencias", options, null);
        RequestContext.getCurrentInstance().execute("onTop('dlgAnaliseReferencias')");

    }

    public void openDlgComercial() {
        if( anacmot.getStatusComercial().getId() == 39 ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgVazio').show();onTop('dlgVazio');");
            return;
        }
	    comercial = true;
        options.put("width", "70%");
        RequestContext.getCurrentInstance().openDialog("dlgAnaliseRefComercial", options, null);
        RequestContext.getCurrentInstance().execute("onTop('dlgAnaliseRefComercial')");

    }

    public void openDlgCriminal() {
        if( anacmot.getStatusCriminal().getId() == 39 ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgVazio').show();onTop('dlgVazio');");
            return;
        }
        options.put("width", "70%");
        RequestContext.getCurrentInstance().openDialog("dlgAnaliseCriminal.html", options, null);

    }

    public void dlgPerguntas(Integer idReferencia) {
        options.put("width", "70%");
        options.put("heigth", "70%");
        lstPerguntas = perguntasService.crud().eq("acReferencia.id", idReferencia).list();

        idRef = idReferencia;

        refPessoal = referenciaService.crud().get(idReferencia);


        Hibernate.initialize(lstPerguntas);

    }

    public void openDlgPerguntas(Integer idReferencia) {

	    dlgPerguntas(idReferencia);

        RequestContext.getCurrentInstance().openDialog("dlgAnalisePerguntas", options, null);
    }


    public String mostraTextoReferencia() {

        return referenciaService.crud().get(idRef).getInformativo();
    }

    public void onReturnDlgClose(SelectEvent event) {
        RequestContext.getCurrentInstance().closeDialog(event.getObject());
    }

    public String foto() {
	    return AppUtils.foto(anacmot.getMotorista().getId(), anacmot.getMotorista().getUrlFoto());
    }

    public String fotoCnhFrente(){
    	
    	String extArq = "";
    	
    	if(getId() != null ){    		    		
    		
    		if( anacmot.getCnh() != null ){
    			if(anacmot.getCnh().getUrlCnhFrente() != null){
        			
    				int qtdCaracteresNomeArquivo = anacmot.getCnh().getUrlCnhFrente().length();
        			int qtdCaracteresNomeArquivoI = anacmot.getCnh().getUrlCnhFrente().length();
        			
        			if(qtdCaracteresNomeArquivo > 0){
        				
        				extArq = anacmot.getCnh().getUrlCnhFrente().substring(qtdCaracteresNomeArquivoI -3, qtdCaracteresNomeArquivo);
        				
            			if(extArq.equals("pdf") || extArq.equals("PDF")){
            				return AppUtils.imageName("sem_imagem.png", true);
            				
            			} else {
            				return AppUtils.foto(getId(), anacmot.getCnh().getUrlCnhFrente());
            			}	

        			}        			        			
    			}
    		}    		
    	} 
    		return AppUtils.imageName("sem_imagem.png", true);	    	    	
    }
    
    public String pdfCnhFrente(){
    	
    	String extArq = "";
    	
    	if(getId()!=null){
    		 
    		if(anacmot.getCnh() != null){
    			if( anacmot.getCnh().getUrlCnhFrente() != null ){
    				
        			int qtdCaracteresNomeArquivo = anacmot.getCnh().getUrlCnhFrente().length();
        			int qtdCaracteresNomeArquivoI = anacmot.getCnh().getUrlCnhFrente().length();
        			
        			if (qtdCaracteresNomeArquivo > 0 ){
        				
            			extArq = anacmot.getCnh().getUrlCnhFrente().substring(qtdCaracteresNomeArquivoI -3, qtdCaracteresNomeArquivo);
            			
            			if(!extArq.equals("pdf") || !extArq.equals("PDF")){
            				return AppUtils.foto(getId(), anacmot.getCnh().getUrlCnhFrente());
            			}
            			
        			}
    			}
    		}    		
    	}
    		return null;	    	    	
    }

    public String fotoCnhVerso(){
    	
  	String extArq = "";
    	
    	if(getId()!=null){
    		 		
    		if(anacmot.getCnh() != null ){
    			if( anacmot.getCnh().getUrlCnhVerso() != null){
    				
        			int qtdCaracteresNomeArquivo = anacmot.getCnh().getUrlCnhVerso().length();
        			int qtdCaracteresNomeArquivoI = anacmot.getCnh().getUrlCnhVerso().length();
        			
        			if(qtdCaracteresNomeArquivo > 0 ){
            			extArq = anacmot.getCnh().getUrlCnhVerso().substring(qtdCaracteresNomeArquivoI -3, qtdCaracteresNomeArquivo);
            			
            			if(extArq.equals("pdf") || extArq.equals("PDF")){
            				return AppUtils.imageName("sem_imagem.png", true);
            			} else {
            				return AppUtils.foto(getId(), anacmot.getCnh().getUrlCnhVerso());
            			}		
        			}
        			
    			}
    		}    		
    	}
    	
    	return AppUtils.imageName("sem_imagem.png", true);	    	
    }
    
    
   public String pdfCnhVerso(){
    	
    	String extArq = "";
    	
    	if(getId()!=null){
    		    		
    		if(anacmot.getCnh() != null){
    			if( anacmot.getCnh().getUrlCnhVerso() != null){
        			
    				int qtdCaracteresNomeArquivo = anacmot.getCnh().getUrlCnhVerso().length();
        			int qtdCaracteresNomeArquivoI = anacmot.getCnh().getUrlCnhVerso().length();
        			
        			if( qtdCaracteresNomeArquivo > 0) {
        				
            			extArq = anacmot.getCnh().getUrlCnhVerso().substring(qtdCaracteresNomeArquivoI -3, qtdCaracteresNomeArquivo);
            			
            			if(!extArq.equals("pdf") || !extArq.equals("PDF")){
            				return AppUtils.foto(getId(), anacmot.getCnh().getUrlCnhVerso());
            			}    				
	
        			}        			
    			}
    		}    		
    	}
    		return null;	    	    	
    }
    
    public void salvarDlgPerguntas() {
	    try {

            List<Integer> iStatus = new ArrayList<>();

            for (acPerguntas acPerguntas : lstPerguntas) {

                String result = onResposta(acPerguntas.getResposta(), 3);
                if (result.isEmpty() || result == null) {
                    result = "38";
                }
                iStatus.add(Integer.valueOf(result));
                acPerguntas.setAvaliacao(refService.crud().get(Integer.valueOf(result)));
                perguntasService.crud().saveOrUpdate(acPerguntas);
            }

            if (!comercial) {
                //
                // Atualizando o StatusPendencias para outros usuários
                //
                Pessoal = AppUtils.statusRecord(iStatus);
                anacmot.setStatusPessoal(refService.crud().get(AppUtils.statusRecord(iStatus)));

            } else {
                anacmot.setStatusComercial(refService.crud().get(AppUtils.statusRecord(iStatus)));
            }
            motService.crud().saveOrUpdate(anacmot);
            //
            // Atualizando o StatusPendencias geral da Ficha
            //
            updateStatusGeral();

            //
            // Atualizando a acReferencia
            //
            refPessoal = referenciaService.crud().get(idRef);
            refPessoal.setAvaliacao(refService.crud().get(AppUtils.statusRecord(iStatus)));
            refPessoal.setDtRegistro(new Date());

            referenciaService.crud().saveOrUpdate(refPessoal);

            lstReferenciasPessoais = referenciaService.crud().eq("acCadastro.id", id)
                    .and().eq("categoria.id", 31).list();

            lstReferenciasComerciais = referenciaService.crud().eq("acCadastro.id", id)
                    .and().eq("categoria.id", 32).list();


            RequestContext.getCurrentInstance().closeDialog("dlgAnalisePerguntas");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e) {}

//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("@form");

    }

	public void remove() {
		if( anacmot != null && anacmot.getId() != null) {
			motService.remove(anacmot);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso ","Analise Cadastral Motorista " + anacmot.getMotorista().getNome()
                            + " removido com sucesso"));
			clear();
		}
	}

    public void updateStatusGeral() {
        List<Integer> motStatus = new ArrayList<>();
        motStatus.add(0, anacmot.getStatusComercial().getId());
        motStatus.add(1, anacmot.getStatusPessoal().getId());
        motStatus.add(2, anacmot.getStatusCNH().getId());
        motStatus.add(3, anacmot.getStatusCriminal().getId());

        anacmot.setStatusGeral(refService.crud().get(AppUtils.statusRecord(motStatus)));
        motService.crud().saveOrUpdate(anacmot);
        
    }

	public void updateCNH() {

	    String msg = "Analise CNH de " + anacmot.getMotorista().getNome() + " atualizada com sucesso!";

        anacmot.setCnhValidade(new ReferenciasAvaliacao(validadeCNH));
        anacmot.setCnhPontos(new ReferenciasAvaliacao(pontosCNH));
	    anacmot.setCnhCategoria(new ReferenciasAvaliacao(categoriaCNH));

        List<Integer> cnhStatus = new ArrayList<>();
        cnhStatus.add(0, validadeCNH);
        cnhStatus.add(1, pontosCNH);
        cnhStatus.add(2, categoriaCNH);

        anacmot.setStatusCNH(refService.crud().get(AppUtils.statusRecord(cnhStatus)));

	    motService.crud().saveOrUpdate(anacmot);

	    updateStatusGeral();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formMotorista");

	}

    public void updateCriminal() {

        String msg = "Analise do resultado da pesquisa do criminal de " + anacmot.getMotorista().getNome() + " atualizada com sucesso!";

        //
        // Atualizando o StatusPendencias geral da Ficha
        //

        if( Criminal == anacmot.getStatusCriminal().getId() ) {
            return;
        }
        anacmot.setStatusCriminal(refService.crud().get(Criminal));
        motService.crud().saveOrUpdate(anacmot);
        motService.crud().commit();

        updateStatusGeral();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formMotorista:panelCriminal");


    }

    
    public void updateVitimologiaPendencia() {
    	
        String msg = "Analise do resultado da pesquisa da vitimologia de " + anacmot.getMotorista().getNome() + " atualizada com sucesso!";                

        vitmot.setPendencia(this.getDescPendVitimologia());
        
        if(idStatusVitimologia!=null){
        	vitmot.setAvaliacao(new ReferenciasAvaliacao(idStatusVitimologia));
        }
        
        vitService.crud().saveOrUpdate(vitmot);
        vitService.crud().commit();        
        
        updateStatusGeral();        
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formMotorista");                
    }
    
    public void checarStatusVitimologia (AjaxBehaviorEvent event){
    	Integer idStatus =   (Integer) ((UIOutput) event.getSource()).getValue();
    	
    	if(idStatus != null){    		
    		idStatusVitimologia = idStatus;
    	}
    }
    
    public void checarPendenciaVitimologia(AjaxBehaviorEvent event){
    	String vlr = (String) ((UIOutput) event.getSource()).getValue();
    	if(vlr.length()>0){
    		this.setDescPendVitimologia(vlr);
    	}
    }
    
    public void checarStatusFichaFinanceira(AjaxBehaviorEvent event){
    	Integer idStatus =   (Integer) ((UIOutput) event.getSource()).getValue();
    	Integer n = idStatus;
    	if(idStatus != null){
    		idStatusFichaFinac = idStatus;
    	}
    }
    
    public void checarPendencia(AjaxBehaviorEvent event){
    	String vlr = (String) ((UIOutput) event.getSource()).getValue();
    	if(vlr.length()>0){
    		this.setDescPendFichaFinac(vlr);
    	}
    }
    
    
	public void updateFichaFinanceiraPendencia() {

        String msg = "Analise do resultado da pesquisa da ficha financeira de " + anacmot.getMotorista().getNome() + " atualizada com sucesso!";
        
        fichaFinanc.setPendencia(this.getDescPendFichaFinac());        		
 
        if(idStatusFichaFinac!=null){
            fichaFinanc.setAvaliacao(new ReferenciasAvaliacao(idStatusFichaFinac));        	
        }
             
        fFinancService.crud().saveOrUpdate(fichaFinanc);
        fFinancService.crud().commit();
        
        updateStatusGeral();
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formMotorista");
                
    }
    
    
    
	public void clear() {
		anacmot = new acMotorista();
		filter = new Filter<acMotorista>(new acMotorista());

		id = null;
		
	}

	public void findByCadastro(Integer idPesquisa) {
	    try {
            if (idPesquisa == null) {
                Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                if( requestParams.get("id") != null ) {
                    idPesquisa = Integer.valueOf(requestParams.get("id").toString());
                    id = idPesquisa;
                } // else {
//                    idPesquisa = this.id;
//                }
            }
            
        
            
            anacmot = motService.crud().eq("acCadastro.id", idPesquisa).list().get(0);
            

            // Tipo Vinculo = 93 
            //if(anacmot.getMotoristaVinculo().getId() == 93){
            	
            	//anacmerc = acMercService.crud().eq("acCadastro.id", idPesquisa).list().get(0);
            	
//            	acCadastro cad = new acCadastro();
            	
            	//regSegMot  = cadService.crud().eq("id", idPesquisa).find().getClienteArea().getId();
            //}
            
//            List<acMercadoria>ListM = (List<acMercadoria>) acMercService.crud().eq("acCadastro.id", idPesquisa).list().get(0);
            
            int resM = acMercService.crud().eq("acCadastro.id", idPesquisa).list().size();
             
            if(resM>0){
            	if(resM ==1){
            		anacmerc = acMercService.crud().eq("acCadastro.id", idPesquisa).list().get(0);
            		
            		Hibernate.initialize(anacmerc.getAcCadastro());
            		Hibernate.initialize(anacmerc.getMercadoria());
            		
            	}
            }
            
            if (anacmot == null) {
                throw new BusinessException("Análise Cadastral não foi encontrado pelo id: " + idPesquisa);
            }
            Hibernate.initialize(anacmot.getMotorista());
            Hibernate.initialize(anacmot.getAcCadastro());
            Hibernate.initialize(anacmot.getConsultaPessoa());
            Hibernate.initialize(anacmot.getMotorista().getLocalizador());
            Hibernate.initialize(anacmot.getAcCadastro().getUsuarioCriacao());
            Hibernate.initialize(anacmot.getCnh());
           
//            Hibernate.initialize(anacmerc.getMercadoria());
            
            if( anacmot.getStatusPessoal().getId() != 39 ) {
                lstReferenciasPessoais = referenciaService.crud().eq("acCadastro.id", idPesquisa)
                        .and().eq("categoria.id", 31).list();
            }

            if( anacmot.getStatusComercial().getId() != 39 ) {
                lstReferenciasComerciais = referenciaService.crud().eq("acCadastro.id", idPesquisa)
                        .and().eq("categoria.id", 32).list();
            }

            Criminal = anacmot.getStatusCriminal().getId();
            Pessoal = anacmot.getStatusPessoal().getId();

            validadeCNH = anacmot.getCnhValidade().getId();
            pontosCNH = anacmot.getCnhPontos().getId();
            categoriaCNH = anacmot.getCnhCategoria().getId();

            cnh = cnhService.crud().get(anacmot.getCnh().getId());            
            
        } catch (Exception e) {
	        System.out.println("Erro : " + e.getLocalizedMessage() + " - " + e.getMessage());
        }

	}

	public void loadScreen(Integer id) {
        findByCadastro(id);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseMotorista.xhtml?id=" + id);
        } catch (IOException e) {}
    }



	
	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do cadastro de motoristas " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

    public LazyDataModel<acMotorista> getMotList() {
        if( motList == null ) {
            motList = new LazyDataModel<acMotorista>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public List<acMotorista> load(int first, int pageSize,
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
                    List<acMotorista> list = motService.paginate(filter);
                    setRowCount(motService.count(filter));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public acMotorista getRowData(String key) {
                    return motService.findById(new Integer(key));
                }
            };

        }
        return motList;
    }

    public String formatJSON() {

        try {

            String file = UUID.randomUUID().toString();

            String retorno = "";
            String s;

            ConsultaPessoa cp;

            PrintWriter writer = new PrintWriter("/tmp/"+ file , "UTF-8");

            try {
                if (anacmot.getConsultaPessoa() == null) {
                    return "Sem resposta";
                }
            } catch (Exception e) {
                return "Sem resposta";
            }

            try {

              cp = conPessoaService.crud().get(anacmot.getConsultaPessoa().getId());

            } catch (Exception e) {

                return "Sem resposta";

            }

            writer.println(cp.getConsulta());

            writer.close();

            String[] cmd = {
                    "/bin/bash",
                    "-c",
                    "python /opt/gcadastro/jsonhtml.py /tmp/" + file
            };
            Process p =  Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            while ((s = stdInput.readLine()) != null) {
                retorno += s;
            }

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null) {
                retorno += s;
            }

            return retorno;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Sem resposta";

    }

    public String classCollumn(){
    	
    	
    	String tipo1 = "lg-3";
    	String tipo2 = "lg-2";
    	
    	setAnaliseVitimologia(true);    	
    	
    	if(isAnaliseVitimologia() == true){
    		return "col-lg-2";
    	} else {    		
    		return "col-lg-3";
    	}
    	
    }
    
    public void htmlRetornoVitimologia(){
    	
    	//1º - Saber se o cliente tem o produto ativo em contrato id, produto 15(cadastro), status (650,767)
        Contrato ctr = ctrService.crud().eq("area.id", anacmot.getAcCadastro().getClienteArea().getId())
        								.eq("produtoTipo.id", 15)
        								.addCriterion(Restrictions.between("statusContrato.id", 645, 649)).find();
        
        // 2º - Se caso encontrou o contrato do cliente
        if(ctr != null){
        	
        	// 2.1º - Pesquisa o contrato produto, e verifica se Vitimologia está ativo
        	ContratoProduto ctrPrd = ctrPrdService.crud().eq("contrato", ctr.getId())
        												 .eq("produto", 32)
        												 .eq("produtoAtivo", true).find();
        	
        	// 2.2º - Se caso encontrou o produto (isto é o cliente tem o produto vitimologia ativo)
        	if(ctrPrd != null){
            	// Consultar status da Vitimologia Principal
            	/**
            	 * Caso todas as vitimologia detalhes estiverem CONCLUIDAS então o statusWS = CONCLUIDAS
            	 * Caso contrário falta alguma vitimologia está pendente de de conclusão e se isso existir
            	 * o sistema deverá consultar essa requisição com sua chave de acesso.
            	 */    	
            	@SuppressWarnings("deprecation")
        		Criteria criteria = vitService.crud().getSession().createCriteria(Vitimologia.class);
            	criteria.add(Restrictions.eq("analiseCadastral", id));
            	criteria.add(Restrictions.isNotNull("numRequisicao")); 
            	
            	int result = criteria.list().size();
            	
            		if( result > 0 ){
            		
            			DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            			
            			listVitPorAnalise = criteria.list();
            			
            			for(int i = 0 ; i < result ; i++){
            				// criteria.list().get(i)
            				Vitimologia vit = listVitPorAnalise.get(i);
            	        	
            	        	if (vit.getStWsStatus().equals("CONCLUIDA")){
            	        		String g = "CONCLUIDA";

            	        	} else {
            	        		 // Acesso ao ws da FourInf
                        		   MotoristaFourInf wsM = new MotoristaFourInf();
            	        		
            	        		// Retorno em JSON da pesquisa feita no ws da FourInf    	        		
                        		   JSONObject objJsonMot = wsM.getRequisicao(vit.getNumRequisicao(), vit.getChaveAcesso());

                        		   if (objJsonMot != null ) {                        			
                        			
                        			JSONArray arr =  (JSONArray) objJsonMot.getJSONObject("dadosResposta").get("requisicao");
                        				                			
                        			for ( int b=0; b < arr.length(); b++){
                        				
                        				int r1 = arr.getJSONObject(b).opt("resultado").toString().length();
                        				
                        				if(r1 > 0){
                   							vit.setStWsStatus("CONCLUIDA");
                            				vit.setDtWsConsulta(new Date());
                        					//JSONObject objRegioes  =  new JSONObject(arr.getJSONObject(b).get("resultado").toString()) ;
                        					//JSONArray arrRegioes  =  (JSONArray) objRegioes.getJSONObject("resultado").get("ufBo");
                        					
                        					if ( vit.getUfBo().length() == 2){
                        						Object ob = arr.getJSONObject(b).get("resultado");
                            					
                            					arr.getJSONObject(b).get("resultado");
                            					vit.setXmlRetorno((String) ob);
                            					
                        					} else {
                        						                        						
                        							JSONObject objZ =  new JSONObject(arr.getJSONObject(b).get("resultado").toString());
                                					
                                					int rX = objZ.getJSONArray("ufBo").length();
                                					
                                					JSONArray arrY = (JSONArray) objZ.getJSONArray("ufBo"); 
                                					int rY = arrY.length();
                                				
                                					Object obR = arr.getJSONObject(b).get("resultado");
                        						    String baseTxt = "";            					
                            						for (int z=0; z < rY ; ++z){
                            							if(arrY.getJSONObject(z).opt("oculto").toString().trim() == "false"){
                                							baseTxt +=  "<b> Estado: " + arrY.getJSONObject(z).opt("uf") + "</b>\r\n";
                                							baseTxt += "Resultado: " + arrY.getJSONObject(z).opt("resultado")+ "\r\n\r\n";	
                            							}                    							
                            						}
                            						vit.setXmlRetorno(baseTxt);
                            						
                        						}
                        					vitService.update(vit);
                        				}      
                        				
                        			}                		                			
                        				
                        		   }
                        		   
            	        		}  // final else  of vit.getStWsStatus().equals("CONCLUIDA")
            	        	
            	        	
                			
            			} // Final for
            			//htmlRetornoVitimologia();
            		} // final if result
            		
            		else {
            			// Finaliza o processo 
            		   	@SuppressWarnings("deprecation")
                		Criteria criteriaG = vitService.crud().getSession().createCriteria(Vitimologia.class);
                    	criteriaG.add(Restrictions.eq("analiseCadastral", id));
                    	criteriaG.add(Restrictions.isNull("numRequisicao")); 
                    	criteriaG.add(Restrictions.eq("stWsStatus", "SOLICITADA"));
                    	
                    	int resultG = criteriaG.list().size();
                    	
                    	if ( resultG > 0 ){
                    		
                    		listVitPorAnalise = criteriaG.list();
                    		
                    		for (int i=0; i < resultG; i++){
                    			Vitimologia vit = listVitPorAnalise.get(i);
                    				
                	        	vit.setStWsStatus("CONCLUIDA");
                    			vit.setDtWsConsulta(new Date());
                    			
                    			vitService.update(vit);
                    		}       		            		
                    	}
            		}
            		
        		   	@SuppressWarnings("deprecation")
            		Criteria criteriaC = vitService.crud().getSession().createCriteria(Vitimologia.class);
                	criteriaC.add(Restrictions.eq("analiseCadastral", id));
                	criteriaC.add(Restrictions.isNotNull("numRequisicao"));
                	criteriaC.add(Restrictions.eq("stWsStatus", "CONCLUIDA"));
                	
//                	this.setStatusVit("Ok");
//                	this.setStatusVitIcon(35); // green-ok
                	
                	int resultC = criteriaC.list().size();
                	
                	if ( resultC > 0){
                		listVitPorAnalise = criteriaC.list();
                	}
            		
                	listVitPorAnalise = criteria.list();    		
        	}
        }	
    	
    }
    
    
    public void htmlRetornoFichaFinanceira(){
    	
    	//1º - Saber se o cliente tem o produto ativo em contrato id, produto 15(cadastro), status (650,767)
        Contrato ctr = ctrService.crud().eq("area.id", anacmot.getAcCadastro().getClienteArea().getId())
        								.eq("produtoTipo.id", 15)
        								.addCriterion(Restrictions.between("statusContrato.id", 645, 649)).find();
        
        // 2º - Se caso encontrou o contrato do cliente
        if(ctr != null){
        	
        	// 2.1º - Pesquisa o contrato produto, e verifica se Ficha Financeira está ativa 
        	ContratoProduto ctrPrd = ctrPrdService.crud().eq("contrato", ctr.getId())
        												 .eq("produto", 1)
        												 .eq("vlr4Unitario", BigDecimal.ONE)
        												 .eq("produtoAtivo", true).find();
        	
        	// 2.2º - Se caso encontrou o produto (isto é o cliente tem o produto ficha financeira ativo)
        	if(ctrPrd != null){
            	// Consultar status da Ficha Financeira
            	/**
            	 * Caso todas as vitimologia detalhes estiverem CONCLUIDAS então o statusWS = CONCLUIDAS
            	 * Caso contrário falta alguma vitimologia está pendente de de conclusão e se isso existir
            	 * o sistema deverá consultar essa requisição com sua chave de acesso.
            	 */    	
            	@SuppressWarnings("deprecation")          	
            	Criteria criteria = fFinancService.crud().getSession().createCriteria(FichaFinanceiraMotorista.class);
            	criteria.add(Restrictions.eq("analiseCadastral", id));
            	criteria.add(Restrictions.isNotNull("numRequisicao")); 
            	
             	int result = criteria.list().size();
            	
            		if( result > 0 ){
            		
            			DateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            			
            			listFFinancPorAnalise = criteria.list();
            			
            			for(int i = 0 ; i < result ; i++){
            	        	
            				FichaFinanceiraMotorista fFichaMot = listFFinancPorAnalise.get(i);
            				
            	        	if (fFichaMot.getStWsStatus().equals("CONCLUIDA")){
            	        		String g = "CONCLUIDA";

            	        	} else {
            	        		
            	        		 // Acesso ao ws da FourInf
                        		   //MotoristaFourInf wsM = new MotoristaFourInf();            	        		
                        		   MotoristaInfFinanceiraFourInf wsM = new MotoristaInfFinanceiraFourInf();
                        		   
            	        		// Retorno em JSON da pesquisa feita no ws da FourInf    	        		
                        		   JSONObject objJsonMot = wsM.getRequisicao(fFichaMot.getNumRequisicao(), fFichaMot.getChaveAcesso());

                        		   if (objJsonMot != null ) {                        			
                        			
                        			JSONArray arr =  (JSONArray) objJsonMot.getJSONObject("dadosResposta").get("requisicao");
                        				                			
                        			for ( int b=0; b < arr.length(); b++){
                        				
                        				int r1 = arr.getJSONObject(b).opt("resultado").toString().length();
                        				
                        				if(r1 > 0){
                        					fFichaMot.setStWsStatus("CONCLUIDA");
                        					fFichaMot.setDtWsConsulta(new Date());
                        					
                        					Object ob = arr.getJSONObject(b).get("resultado");
                        					
                        					arr.getJSONObject(b).get("resultado");
                        					fFichaMot.setXmlRetorno((String) ob);                        					
                        			
                        					fFinancService.update(fFichaMot);
                        					
                        				}      
                        				
                        			}                		                			
                        				
                        		   }
                        		   
            	        		}  // final else  of vit.getStWsStatus().equals("CONCLUIDA")
            	        	            	        	
                			
            			} // Final for
            			
            		} // final if result
            		
            		else {
            			// Finaliza o processo 
            		   	@SuppressWarnings("deprecation")
            		   	Criteria criteriaG = fFinancService.crud().getSession().createCriteria(FichaFinanceiraMotorista.class);
                    	criteriaG.add(Restrictions.eq("analiseCadastral", id));
                    	criteriaG.add(Restrictions.isNull("numRequisicao")); 
                    	criteriaG.add(Restrictions.eq("stWsStatus", "SOLICITADA"));
                    	
                    	int resultG = criteriaG.list().size();
                    	
                    	if ( resultG > 0 ){
                    		                    		                    		
                    		listFFinancPorAnalise = criteriaG.list();
                    		
                    		for (int i=0; i < resultG; i++){
                    			FichaFinanceiraMotorista fFinac = listFFinancPorAnalise.get(i);                     			
                    				
                    			fFinac.setStWsStatus("CONCLUIDA");
                    			fFinac.setDtWsConsulta(new Date());
                    			
                    			fFinancService.update(fFinac);
                    		}       		            		
                    	}
            		}
            		
        		   	@SuppressWarnings("deprecation")
            		Criteria criteriaC = fFinancService.crud().getSession().createCriteria(FichaFinanceiraMotorista.class);
                	criteriaC.add(Restrictions.eq("analiseCadastral", id));
                	criteriaC.add(Restrictions.isNotNull("numRequisicao"));
                	//criteriaC.add(Restrictions.eq("stWsStatus", "CONCLUIDA"));
                	
                	int resultC = criteriaC.list().size();
                	
                	if ( resultC > 0){
                		listFFinancPorAnalise = criteriaC.list();
                	}
            		
                	listFFinancPorAnalise = criteria.list();    		
        	}
        }	
    	
    }
    
    
    public String testHtml(String recurso){
    	if ( recurso != ""){
        	String html = recurso.replace("\r\n\r\n", "<br/><br/>");
        	
        	String htmlA = html.replace("\r\n","<br/>");
        	htmlA.toString();
            System.lineSeparator();
        	org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(recurso);
        	return htmlA.toString();    		
    	}
    	return "";
    }
    
    public String checarVitimologiaCliente(){
//    	RequestContext context = RedquestContext.getCurrentInstance();
    	
    	if (id != null){
            String SQL = 
            		"select ac.anacoid from java.analise_cadastral ac, java.analise_cadastral_vitimologia acv " + 
            		" where ac.anacoid = acv.acv_anacoid and " +
            		"		ac.anacoid = :idAnacoid" +
            		"		group by ac.anacoid ";
            
            Query query = vitService.crud().getEntityManager().createNativeQuery(SQL);
            query.setParameter("idAnacoid", id);
        	
            try {
                Object valor = query.getSingleResult();
                if (valor != null){
                	openVitimologia = true;
//                	context.update("formMotorista:formVitimologia");
                	int result = query.getResultList().size();
                	if (result > 0){
                		
                		for(int i=0; i < result; i++){
                			// Faça a pesquisa na analise_cadastral_vitimologia
                			
                			//teste(id);
//                			 Criminal = anacmot.getStatusCriminal().getId();
//                			anacmot = motService.crud().eq("acCadastro.id", idPesquisa).list().get(0);
                			int regi = (int) valor;
                			
                			vitmot = new Vitimologia();
                			vitmot.setAvaliacao(new ReferenciasAvaliacao());
                			
                			vitmot = vitService.crud().eq("tipoMov","G").eq("analiseCadastral", regi).find();
                			
                			if (vitmot.getId() != null){
                    			
                					//vitmot = vitService.crud().eq("tipoMov","G").eq("analiseCadastral", regi ).find();
	            					//vitService.crud().get(0).getAnaliseCadastral();
	            			
	                    			//vitmot.setAvaliacao(new ReferenciasAvaliacao());
	            			
	                    			Vitimo = vitmot.getAvaliacao().getId() ;
	                    			lstReferenciasVitimo = refService.listAll();
	                    			
	                    			Hibernate.initialize(vitmot);
                				
                			}
                			
                		}
                	}
                	// Consultar o Status do processo:
                	
                	
                	return "col-lg-3 col-xs-3";                	
                } 
                
            } catch (NoResultException nre) {
                
            }
    		
    	}
    	
//    	openVitimologia = false;
    	this.setOpenVitimologia(false);
    
//    	RequestContext context = RequestContext.getCurrentInstance();
//        context.update("formMotorista");
    	
    	return "col-lg-3 col-xs-3";
    }

    public String checarFichaFinanceira(){
    	
    	if (id != null){
    		
            String SQL =
            		"select ac.anacoid from java.analise_cadastral ac, java.analise_cadastral_ficha_financeira acff " +  
            		 " where ac.anacoid = acff.acf_anacoid and " +
            			"	ac.anacoid = :idAnacoid " +
            			"	group by ac.anacoid "; 
            			            
            Query query = fFinancService.crud().getEntityManager().createNativeQuery(SQL);
            query.setParameter("idAnacoid", id);
        	
            try {
                
            	Object valor = query.getSingleResult();
                
                if (valor != null){
                	openFichaFinanceira = true;
                	
                	int result = query.getResultList().size();
                	if (result > 0){
                		
                		for(int i=0; i < result; i++){
                			// Faça a pesquisa na ficha financeira
                			
                			int regi = (int) valor;
                			
                			fichaFinanc = new FichaFinanceiraMotorista();                			
                			fichaFinanc.setAvaliacao(new ReferenciasAvaliacao());
                			
                			fichaFinanc = fFinancService.crud().eq("analiseCadastral", regi).find();
                			                			
                			
                			if (fichaFinanc.getId() != null){                				                			
                					fichaFinacIntAvaliacao = fichaFinanc.getAvaliacao().getId();                					
                					lstReferenciasFichaFinac = refService.listAll();
                					
                					Hibernate.initialize(fichaFinanc);
                					
                			}
                			
                		}
                	}
                	// Consultar o Status do processo:            	
                	
                	return "col-lg-3 col-xs-3";                	
                } 
                
            } catch (NoResultException nre) {
                
            }
    		
    	}
    	
    	this.setOpenFichaFinanceira(false);    	
        	
    	return "col-lg-3 col-xs-3";
    }
    
    
    private void teste(Integer id){
		
		String query = "select acvoid as id, acv_anacoid as analiseCadastral, "
				+ " acv_n_consulta as numConsulta , acv_n_requisicao as numRequisicao,"
				+ " acv_chave_acesso as chaveAcesso, acv_st_ws_status as stWsStatus " 
				+ " from java.analise_cadastral_vitimologia "
				+ " where acv_anacoid = " + id;

		EntityManager em = vitService.crud().getEntityManager();
						
		List<acVitimologia> List =  em.createNativeQuery(query, "ListaAcVitimologiaMapping").getResultList();		
		
			int resList = List.size();
		
		if( resList > 0){
			for (int v=0; v < resList; v++ ){
				Vitimologia vit =  vitService.crud().get(List.get(v).getId());
    			if(vit.getNumRequisicao() != null){
    				if(vit.getStWsStatus().equals("SOLICITADA")){
    					// Faça nova pesquisa no ws Four Info
    					// Acesso ao ws da FourInf
                		MotoristaFourInf wsM = new MotoristaFourInf();
                		// Retorno em JSON da pesquisa feita no ws da FourInf
                		   JSONObject objJsonMot = wsM.getRequisicao(vit.getNumRequisicao(), vit.getChaveAcesso());
                		   
                		// Recebeu dados validos do ws FourInf com Status </> SOLICITADA
                   		   if (objJsonMot != null){

                   			JSONArray arr =  (JSONArray) objJsonMot.getJSONObject("dadosResposta").get("requisicao");
	                   			
                   			for ( int bv=0; bv < arr.length(); bv++){
                   					
                   				int r1 = arr.getJSONObject(bv).opt("resultado").toString().length();
                   					
                   					if(r1 > 0){
                   						if ( vit.getUfBo().length() == 2){
                    						Object ob = arr.getJSONObject(bv).get("resultado");
                        					
                        					arr.getJSONObject(bv).get("resultado");
                        					vit.setXmlRetorno((String) ob);
                        					
                    					} else {
                    							JSONObject objZ =  new JSONObject(arr.getJSONObject(bv).get("resultado").toString());
                            					
                            					int rX = objZ.getJSONArray("ufBo").length();
                            					
                            					JSONArray arrY = (JSONArray) objZ.getJSONArray("ufBo"); 
                            					int rY = arrY.length();
                            				
                            					Object obR = arr.getJSONObject(bv).get("resultado");
                    						    String baseTxt = "";            					
                        						for (int z=0; z < rY ; ++z){
                        							if(arrY.getJSONObject(z).opt("oculto").toString().trim() == "false"){
                            							baseTxt +=  "<b> Estado: " + arrY.getJSONObject(z).opt("uf") + "</b>\r\n";
                            							baseTxt += "Resultado: " + arrY.getJSONObject(z).opt("resultado")+ "\r\n\r\n";	
                        							}                    							
                        						}
                        						vit.setXmlRetorno(baseTxt);
                    						}
                   					}
	                   			}
                   			                      			
                   				vit.setStWsStatus("CONCLUIDA");
                    			vit.setDtWsConsulta(new Date());
                    	
                    			vitService.update(vit);
                    			
                   			
                   		   } else {
                   			   // Caso contrario altera o status da Vitimologia para Pendente e o icone para Stop.
                   			   // Vou ter que sair do loop para alterar o status... pois se o ultimo registro consultado
                   			   // não for SOLICITADA então trará o resultado Concluido.
//                   			   this.setStatusVit("Pendente");
//                   			   this.setStatusVitIcon(39); //38 - flag blue - 39 sing_ban
                   		   }

    				}
    			}		
			}
		}
    }
    
    public void verStatusVit(){
    	
    	String v = this.getStatusVit();
    	Integer va = this.getStatusVitIcon();
    	
    	String vs = v;
    	Integer vai = va;
    }
    
    public String pathFlag(Integer id) {

	    return AppUtils.pathFlag(id);

    }	

	private void validarVeiculoPropriedadeEmpresa(){
		// Deve ser comparado o cnpj do proprietario do veiculo com os cnpj da Matriz e Filiais (unidades de negócio)
		//anacmot.acCadastro.clienteArea.pessoaResponsavel.documento1;
		if(anacmot != null){
			if(this.getDocPropVei().toString().length()>0){
				if(this.getDocMtzFil().toString().trim().equals(this.getDocPropVei().toString().trim())){
					// Aprovado
					this.setResIconVeiPropEmp(8);
				}else{
					// Reprovado
					this.setResIconVeiPropEmp(9);
				}	
			} else {
				this.setResIconVeiPropEmp(8);				
			}
						
		}			
	}
	
	private void validarVeiculoPropriedadeMotorista(){
		if(anacmot.getMotorista().getDoc1().toString().length()>0){
			if(docPropVei.toString().length()>0){
				if(docPropVei.toString().trim() == anacmot.getMotorista().getDoc1().toString().trim()){
					// Aprovado					
					this.setResIconVeiPropMot(8);
				} else {
					// Reprovado
					this.setResIconVeiPropMot(9);
				}
			}else{
				this.setResIconVeiPropMot(8);
			}
		}
	}
	
	private void validarUfRgUfPlacas(){
		String bs = "bas";
		String bc = bs;
		
		int vUfCav = 0;
		int vUfRb1 = 0;
		int vUfRb2 = 0;
		int vUfRb3 = 0;
		
		// Cavalo se existir então deve iniciar a validar
		if (this.getUfPlaca().toString().length() > 0){
			
			// Valida a Uf Cavalo com a Uf Rg Motorista
			if(this.getUfPlaca() == anacmot.getMotorista().getDoc2()){
				// 1º verificação - aprovado
				vUfCav = 1;
				
				if(this.getUfPlacaReb1().toString().length()>0){
					if(this.getUfPlacaReb1() == anacmot.getMotorista().getDoc2()){
						// 2º verificação - aprovado
						vUfRb1 = 1;
					} else {
						// Reprovado na 2º verificação
						vUfRb1 = -1;
					}
				}
				
				if(this.getUfPlacaReb2().toString().length()>0){
					if(this.getUfPlacaReb2() == anacmot.getMotorista().getDoc2()){
						// 3º verificação - aprovado
						vUfRb2 = 1;
					} else {
						// Reprovado na 3º verificação
						vUfRb2 = -1;
					}
				}
				
				if(this.getUfPlacaReb3().toString().length()>0){
					if(this.getUfPlacaReb3() == anacmot.getMotorista().getDoc2()){
						// 4º verificação - aprovado
						vUfRb3 = 1;
					} else {
						// Reprovado na 4º verificação
						vUfRb3 = -1;
					}
				}
				
			} else {
				// Reprovado já no 1º verificação
				vUfCav = -1;
			}
			
			// Verificar quais foram validados e apresentar o resultado
			if(vUfCav >= 0 && vUfRb1 >= 0 && vUfRb2 >= 0 && vUfRb3 >= 0){
				// Aprovado
				this.setResIconUfRGUfPlaca(8);
			} else {
				// Reprovado
				this.setResIconUfRGUfPlaca(9);			
			}
			
		} else {
			// Sem cavalo
			// Verificar reboques
			if(this.getUfPlacaReb1().toString().length()>0){
				if(this.getUfPlacaReb1() == anacmot.getMotorista().getDoc2()){
					// 2º verificação - aprovado
					vUfRb1 = 1;
				} else {
					// Reprovado na 2º verificação
					vUfRb1 = -1;
				}
			}else{
				vUfRb1 = 1;
			}
			
			if(this.getUfPlacaReb2().toString().length()>0){
				if(this.getUfPlacaReb2() == anacmot.getMotorista().getDoc2()){
					// 3º verificação - aprovado
					vUfRb2 = 1;
				} else {
					// Reprovado na 3º verificação
					vUfRb2 = -1;
				}
			}else{
				vUfRb2 = 1;
			}
			
			if(this.getUfPlacaReb3().toString().length()>0){
				if(this.getUfPlacaReb3() == anacmot.getMotorista().getDoc2()){
					// 4º verificação - aprovado
					vUfRb3 = 1;
				} else {
					// Reprovado na 4º verificação
					vUfRb3 = -1;				
				}
			}else{
				vUfRb3 = 1;
			}
			
			// Verificar quais foram validados e apresentar o resultado
			if(vUfRb1 >= 0 && vUfRb2 >= 0 && vUfRb3 >= 0){
				// Aprovado
				this.setResIconUfRGUfPlaca(8);
			} else {
				// Reprovado
				this.setResIconUfRGUfPlaca(9);			
			}

			
		}
		
		

	}
	
    public void uploadFoto(FileUploadEvent event){
    	
    	this.setImgFile(event.getFile());    	
    	this.setChangeImgFile(true);
        if (this.getImgFile() != null) {
        	FacesContext.getCurrentInstance().addMessage(null, 
        			new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Seu arquivo será atualizado após salva a ficha. Arquivo enviado: " + this.getImgFile().getFileName()));
        }
    }
	
	public void onRowUnselect(UnselectEvent event) {
		anacmot = new acMotorista();
	}

	public Filter<acMotorista> getFilter() {
		return filter;
	}

	public void setFilter(Filter<acMotorista> filter) {
		this.filter = filter;
	}

	public List<acMotorista> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<acMotorista> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setMotList(LazyDataModel<acMotorista> motList) {
		this.motList = motList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public acMotorista getAnacmot() {
		return anacmot;
	}

	public void setAnacmot(acMotorista anacmot) {
		this.anacmot = anacmot;
	}

	public MotoristaService getMotService() {
		return motService;
	}

	public void setMotService(MotoristaService motService) {
		this.motService = motService;
	}

    public List<ReferenciasAvaliacao> getLstReferencias() {
        return lstReferencias;
    }

    public void setLstReferencias(List<ReferenciasAvaliacao> lstReferencias) {
        this.lstReferencias = lstReferencias;
    }

    public ReferenciasAvaliacaoService getRefService() {
        return refService;
    }

    public void setRefService(ReferenciasAvaliacaoService refService) {
        this.refService = refService;
    }

    public List<acReferencia> getLstReferenciasPessoais() {
        return lstReferenciasPessoais;
    }

    public void setLstReferenciasPessoais(List<acReferencia> lstReferenciasPessoais) {
        this.lstReferenciasPessoais = lstReferenciasPessoais;
    }

    public ReferenciaService getReferenciaService() {
        return referenciaService;
    }

    public void setReferenciaService(ReferenciaService referenciaService) {
        this.referenciaService = referenciaService;
    }

    public PerguntasService getPerguntasService() {
        return perguntasService;
    }

    public void setPerguntasService(PerguntasService perguntasService) {
        this.perguntasService = perguntasService;
    }

    public List<acPerguntas> getLstPerguntas() {
        return lstPerguntas;
    }

    public void setLstPerguntas(List<acPerguntas> lstPerguntas) {
        this.lstPerguntas = lstPerguntas;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public List<ReferenciasCategoria> getLstCategorias() {
        return lstCategorias;
    }

    public void setLstCategorias(List<ReferenciasCategoria> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }

    public ReferenciasCategoriaService getRefCatService() {
        return refCatService;
    }

    public void setRefCatService(ReferenciasCategoriaService refCatService) {
        this.refCatService = refCatService;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getTextoReferencia() {
        return textoReferencia;
    }

    public void setTextoReferencia(String textoReferencia) {
        this.textoReferencia = textoReferencia;
    }

    public Integer getIdRef() {
        return idRef;
    }

    public void setIdRef(Integer idRef) {
        this.idRef = idRef;
    }

    public ConsultaPessoa getConsultaPessoa() {
        return consultaPessoa;
    }

    public void setConsultaPessoa(ConsultaPessoa consultaPessoa) {
        this.consultaPessoa = consultaPessoa;
    }

    public ConsultaPessoaService getConPessoaService() {
        return conPessoaService;
    }

    public void setConPessoaService(ConsultaPessoaService conPessoaService) {
        this.conPessoaService = conPessoaService;
    }

    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public List<acReferencia> getLstReferenciasComerciais() {
        return lstReferenciasComerciais;
    }

    public void setLstReferenciasComerciais(List<acReferencia> lstReferenciasComerciais) {
        this.lstReferenciasComerciais = lstReferenciasComerciais;
    }

    public Integer getCriminal() {
        return Criminal;
    }

    public void setCriminal(Integer criminal) {
        Criminal = criminal;
    }

    public Integer getPessoal() {
        return Pessoal;
    }

    public void setPessoal(Integer pessoal) {
        Pessoal = pessoal;
    }

    public acReferencia getRefPessoal() {
        return refPessoal;
    }

    public void setRefPessoal(acReferencia refPessoal) {
        this.refPessoal = refPessoal;
    }

    public Integer getStatusCNH() {
        return statusCNH;
    }

    public void setStatusCNH(Integer statusCNH) {
        this.statusCNH = statusCNH;
    }

    public Integer getValidadeCNH() {
        return validadeCNH;
    }

    public void setValidadeCNH(Integer validadeCNH) {
        this.validadeCNH = validadeCNH;
    }

    public Integer getPontosCNH() {
        return pontosCNH;
    }

    public void setPontosCNH(Integer pontosCNH) {
        this.pontosCNH = pontosCNH;
    }

    public Integer getCategoriaCNH() {
        return categoriaCNH;
    }

    public void setCategoriaCNH(Integer categoriaCNH) {
        this.categoriaCNH = categoriaCNH;
    }

    public boolean isComercial() {
        return comercial;
    }

    public void setComercial(boolean comercial) {
        this.comercial = comercial;
    }

    public CNH getCnh() {
        return cnh;
    }

    public void setCnh(CNH cnh) {
        this.cnh = cnh;
    }

    public CNHService getCnhService() {
        return cnhService;
    }

    public void setCnhService(CNHService cnhService) {
        this.cnhService = cnhService;
    }

    public String getCenterGeoMap() {
        return centerGeoMap;
    }

    public void setCenterGeoMap(String centerGeoMap) {
        this.centerGeoMap = centerGeoMap;
    }

    public MapModel getGeoModel() {
        return geoModel;
    }

    public void setGeoModel(MapModel geoModel) {
        this.geoModel = geoModel;
    }

    public MapModel getRevGeoModel() {
        return revGeoModel;
    }

    public void setRevGeoModel(MapModel revGeoModel) {
        this.revGeoModel = revGeoModel;
    }

    public String getZoomMap() {
        return zoomMap;
    }

    public void setZoomMap(String zoomMap) {
        this.zoomMap = zoomMap;
    }

	public boolean isTipoArqCnhPdf() {
		return tipoArqCnhPdf;
	}

	public void setTipoArqCnhPdf(boolean tipoArqCnhPdf) {
		this.tipoArqCnhPdf = tipoArqCnhPdf;
	}

	public boolean isAnaliseVitimologia() {
		return analiseVitimologia;
	}

	public void setAnaliseVitimologia(boolean analiseVitimologia) {
		this.analiseVitimologia = analiseVitimologia;
	}

	public Integer getFichaVit() {
		return fichaVit;
	}

	public String getUfVit() {
		return ufVit;
	}

	public String getReqVit() {
		return reqVit;
	}

	public String getChaveVit() {
		return chaveVit;
	}

	public VitimologiaService getVitService() {
		return vitService;
	}

	public void setFichaVit(Integer fichaVit) {
		this.fichaVit = fichaVit;
	}

	public void setUfVit(String ufVit) {
		this.ufVit = ufVit;
	}

	public void setReqVit(String reqVit) {
		this.reqVit = reqVit;
	}

	public void setChaveVit(String chaveVit) {
		this.chaveVit = chaveVit;
	}

	public void setVitService(VitimologiaService vitService) {
		this.vitService = vitService;
	}

	public String getStatusVit() {
		return statusVit;
	}

	public void setStatusVit(String statusVit) {
		this.statusVit = statusVit;
	}

	public String getDateWsVit() {
		return dateWsVit;
	}

	public void setDateWsVit(String dateWsVit) {
		this.dateWsVit = dateWsVit;
	}

	public List<Vitimologia> getListVitPorAnalise() {
		return listVitPorAnalise;
	}

	public void setListVitPorAnalise(List<Vitimologia> listVitPorAnalise) {
		this.listVitPorAnalise = listVitPorAnalise;
	}

	public List<FichaFinanceiraMotorista> getListFFinancPorAnalise() {
		return listFFinancPorAnalise;
	}

	public void setListFFinancPorAnalise(List<FichaFinanceiraMotorista> listFFinancPorAnalise) {
		this.listFFinancPorAnalise = listFFinancPorAnalise;
	}

	public boolean isOpenVitimologia() {
		return openVitimologia;
	}

	public void setOpenVitimologia(boolean openVitimologia) {
		this.openVitimologia = openVitimologia;
	}

	public boolean isOpenFichaFinanceira() {
		return openFichaFinanceira;
	}

	public void setOpenFichaFinanceira(boolean openFichaFinanceira) {
		this.openFichaFinanceira = openFichaFinanceira;
	}

	public acMercadoria getAnacmerc() {
		return anacmerc;
	}

	public void setAnacmerc(acMercadoria anacmerc) {
		this.anacmerc = anacmerc;
	}

	public MercadoriaService getAcMercService() {
		return acMercService;
	}

	public void setAcMercService(MercadoriaService acMercService) {
		this.acMercService = acMercService;
	}

	public br.com.global5.manager.service.geral.MercadoriaService getMerService() {
		return merService;
	}

	public void setMerService(br.com.global5.manager.service.geral.MercadoriaService merService) {
		this.merService = merService;
	}

	public Integer getStatusVitIcon() {
		return statusVitIcon;
	}

	public void setStatusVitIcon(Integer statusVitIcon) {
		this.statusVitIcon = statusVitIcon;
	}

	public RegraSeguradoraMotorista getRegSegMot() {
		return regSegMot;
	}

	public void setRegSegMot(RegraSeguradoraMotorista regSegMot) {
		this.regSegMot = regSegMot;
	}

	public Integer getIconValidaEmpresa() {
		return iconValidaEmpresa;
	}

	public void setIconValidaEmpresa(Integer iconValidaEmpresa) {
		this.iconValidaEmpresa = iconValidaEmpresa;
	}

	public Integer getIconUfRGUfPlaca() {
		return iconUfRGUfPlaca;
	}

	public void setIconUfRGUfPlaca(Integer iconUfRGUfPlaca) {
		this.iconUfRGUfPlaca = iconUfRGUfPlaca;
	}

	public Integer getIconVeiPropMot() {
		return iconVeiPropMot;
	}

	public void setIconVeiPropMot(Integer iconVeiPropMot) {
		this.iconVeiPropMot = iconVeiPropMot;
	}

	public Integer getIconVeiPropEmp() {
		return iconVeiPropEmp;
	}

	public void setIconVeiPropEmp(Integer iconVeiPropEmp) {
		this.iconVeiPropEmp = iconVeiPropEmp;
	}

	public Integer getIconComprovRes() {
		return iconComprovRes;
	}

	public void setIconComprovRes(Integer iconComprovRes) {
		this.iconComprovRes = iconComprovRes;
	}

	public String getUfPlaca() {
		return ufPlaca;
	}

	public void setUfPlaca(String ufPlaca) {
		this.ufPlaca = ufPlaca;
	}

	public String getUfPlacaReb1() {
		return ufPlacaReb1;
	}

	public void setUfPlacaReb1(String ufPlacaReb1) {
		this.ufPlacaReb1 = ufPlacaReb1;
	}

	public String getUfPlacaReb2() {
		return ufPlacaReb2;
	}

	public void setUfPlacaReb2(String ufPlacaReb2) {
		this.ufPlacaReb2 = ufPlacaReb2;
	}

	public String getUfPlacaReb3() {
		return ufPlacaReb3;
	}

	public void setUfPlacaReb3(String ufPlacaReb3) {
		this.ufPlacaReb3 = ufPlacaReb3;
	}

	public String getDocPropVei() {
		return docPropVei;
	}

	public void setDocPropVei(String docPropVei) {
		this.docPropVei = docPropVei;
	}

	public Integer getQtdRecMin() {
		return qtdRecMin;
	}

	public void setQtdRecMin(Integer qtdRecMin) {
		this.qtdRecMin = qtdRecMin;
	}

	public Integer getRecMinMeses() {
		return recMinMeses;
	}

	public void setRecMinMeses(Integer recMinMeses) {
		this.recMinMeses = recMinMeses;
	}

	public Integer getResQtdRecMin() {
		return resQtdRecMin;
	}

	public void setResQtdRecMin(Integer resQtdRecMin) {
		this.resQtdRecMin = resQtdRecMin;
	}

	public Integer getResIconValidaEmpresa() {
		return resIconValidaEmpresa;
	}

	public void setResIconValidaEmpresa(Integer resIconValidaEmpresa) {
		this.resIconValidaEmpresa = resIconValidaEmpresa;
	}

	public Integer getResIconUfRGUfPlaca() {
		return resIconUfRGUfPlaca;
	}

	public void setResIconUfRGUfPlaca(Integer resIconUfRGUfPlaca) {
		this.resIconUfRGUfPlaca = resIconUfRGUfPlaca;
	}

	public Integer getResIconVeiPropMot() {
		return resIconVeiPropMot;
	}

	public void setResIconVeiPropMot(Integer resIconVeiPropMot) {
		this.resIconVeiPropMot = resIconVeiPropMot;
	}

	public Integer getResIconVeiPropEmp() {
		return resIconVeiPropEmp;
	}

	public void setResIconVeiPropEmp(Integer resIconVeiPropEmp) {
		this.resIconVeiPropEmp = resIconVeiPropEmp;
	}

	public String getDocMtzFil() {
		return docMtzFil;
	}

	public void setDocMtzFil(String docMtzFil) {
		this.docMtzFil = docMtzFil;
	}

	public UploadedFile getImgFile() {
		return imgFile;
	}

	public void setImgFile(UploadedFile imgFile) {
		this.imgFile = imgFile;
	}

	public boolean isChangeImgFile() {
		return changeImgFile;
	}

	public void setChangeImgFile(boolean changeImgFile) {
		this.changeImgFile = changeImgFile;
	}

	public boolean isBlockDownComprovRes() {
		return blockDownComprovRes;
	}

	public void setBlockDownComprovRes(boolean blockDownComprovRes) {
		this.blockDownComprovRes = blockDownComprovRes;
	}

	public Integer getIconMercadoria() {
		return iconMercadoria;
	}

	public void setIconMercadoria(Integer iconMercadoria) {
		this.iconMercadoria = iconMercadoria;
	}

	public Integer getResIconMercadoria() {
		return resIconMercadoria;
	}

	public void setResIconMercadoria(Integer resIconMercadoria) {
		this.resIconMercadoria = resIconMercadoria;
	}

	public boolean isBoolVinculoMotorista3() {
		return boolVinculoMotorista3;
	}

	public void setBoolVinculoMotorista3(boolean boolVinculoMotorista3) {
		this.boolVinculoMotorista3 = boolVinculoMotorista3;
	}

	public boolean isBoolFichaComVeiculo() {
		return boolFichaComVeiculo;
	}

	public void setBoolFichaComVeiculo(boolean boolFichaComVeiculo) {
		this.boolFichaComVeiculo = boolFichaComVeiculo;
	}

	public Integer getVitimo() {
		return Vitimo;
	}

	public void setVitimo(Integer vitimo) {
		Vitimo = vitimo;
	}

	public Vitimologia getVitmot() {
		return vitmot;
	}

	public void setVitmot(Vitimologia vitmot) {
		this.vitmot = vitmot;
	}

	public FichaFinanceiraMotorista getFichaFinanc() {
		return fichaFinanc;
	}

	public void setFichaFinanc(FichaFinanceiraMotorista fichaFinanc) {
		this.fichaFinanc = fichaFinanc;
	}

	public List<ReferenciasAvaliacao> getLstReferenciasVitimo() {
		return lstReferenciasVitimo;
	}

	public void setLstReferenciasVitimo(List<ReferenciasAvaliacao> lstReferenciasVitimo) {
		this.lstReferenciasVitimo = lstReferenciasVitimo;
	}

	public List<ReferenciasAvaliacao> getLstReferenciasFichaFinac() {
		return lstReferenciasFichaFinac;
	}

	public void setLstReferenciasFichaFinac(List<ReferenciasAvaliacao> lstReferenciasFichaFinac) {
		this.lstReferenciasFichaFinac = lstReferenciasFichaFinac;
	}

	public ReferenciasAvaliacaoService getRefVitService() {
		return refVitService;
	}

	public void setRefVitService(ReferenciasAvaliacaoService refVitService) {
		this.refVitService = refVitService;
	}

	public FichaFinanceiraMotoristaService getfFinancService() {
		return fFinancService;
	}

	public void setfFinancService(FichaFinanceiraMotoristaService fFinancService) {
		this.fFinancService = fFinancService;
	}

	public Integer getFichaFinacIntAvaliacao() {
		return fichaFinacIntAvaliacao;
	}

	public void setFichaFinacIntAvaliacao(Integer fichaFinacIntAvaliacao) {
		this.fichaFinacIntAvaliacao = fichaFinacIntAvaliacao;
	}

	public Integer getIdStatusFichaFinac() {
		return idStatusFichaFinac;
	}

	public void setIdStatusFichaFinac(Integer idStatusFichaFinac) {
		this.idStatusFichaFinac = idStatusFichaFinac;
	}

	public String getDescPendFichaFinac() {
		return descPendFichaFinac;
	}

	public void setDescPendFichaFinac(String descPendFichaFinac) {
		this.descPendFichaFinac = descPendFichaFinac;
	}

	public Integer getIdStatusVitimologia() {
		return idStatusVitimologia;
	}

	public void setIdStatusVitimologia(Integer idStatusVitimologia) {
		this.idStatusVitimologia = idStatusVitimologia;
	}

	public String getDescPendVitimologia() {
		return descPendVitimologia;
	}

	public void setDescPendVitimologia(String descPendVitimologia) {
		this.descPendVitimologia = descPendVitimologia;
	}
	
	
	
	
	
}