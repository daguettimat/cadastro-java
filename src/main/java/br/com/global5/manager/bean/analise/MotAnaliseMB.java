package br.com.global5.manager.bean.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.analise.acPerguntas;
import br.com.global5.manager.model.analise.acReferencia;
import br.com.global5.manager.model.cadastro.CNH;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.ReferenciasCategoria;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PerguntasService;
import br.com.global5.manager.service.analise.ReferenciaService;
import br.com.global5.manager.service.cadastro.CNHService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import br.com.global5.manager.service.enums.ReferenciasCategoriaService;
import br.com.global5.manager.service.externos.ConsultaPessoaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
@Named
@ViewAccessScoped
public class MotAnaliseMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private LazyDataModel<acMotorista> motList;
	private List<acMotorista> filteredValue;

	private acMotorista anacmot;
	private ConsultaPessoa consultaPessoa;
    private acReferencia refPessoal;

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

	@Inject
	MotoristaService motService;

	@Inject
    ReferenciasAvaliacaoService refService;

	@Inject
	ReferenciaService referenciaService;

	@Inject
	PerguntasService perguntasService;

	@Inject
    ReferenciasCategoriaService refCatService;

	@Inject
    ConsultaPessoaService conPessoaService;

	@Inject
    CNHService cnhService;

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

        loadParameters();

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
                findByCadastro(id);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml");
                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,"acMotorista " + getId()
                                    + " não pode ser carregado. Informe ao suporte técnico.",null));
                }
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
            if (anacmot == null) {
                throw new BusinessException("Análise Cadastral não foi encontrado pelo id: " + idPesquisa);
            }
            Hibernate.initialize(anacmot.getMotorista());
            Hibernate.initialize(anacmot.getAcCadastro());
            Hibernate.initialize(anacmot.getConsultaPessoa());
            Hibernate.initialize(anacmot.getMotorista().getLocalizador());
            Hibernate.initialize(anacmot.getAcCadastro().getUsuarioCriacao());
            Hibernate.initialize(anacmot.getCnh());
           

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
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml?id=" + id);
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

    public String pathFlag(Integer id) {

	    return AppUtils.pathFlag(id);

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
    
    
}
