package br.com.global5.manager.bean.analise;


import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.analise.acPendencias;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.enums.*;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.PendService;
import br.com.global5.manager.service.enums.PendenciasService;
import br.com.global5.manager.service.enums.PendenciasTipoService;
import br.com.global5.manager.service.enums.TipoParteService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class PendenciasMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<acPendencias> lista;
	private List<Pendencias> lstPendencias;
	private List<PendenciasTipo> lstTipoPendencias;
	private List<TipoParte> lstTipoParte;
    private List<String> images;
    private List<String> pdfFiles;
    private List<String> pdfFileNames;
    private List<acPendencias> filteredValue;
    private List<acPendencias> lstPrincipal;

    private Integer id;
    private int idKey;
    private Integer line;
	private acPendencias pendencias;
    private Area areaCliente;
	private Filter<acPendencias> filter = new Filter<>(new acPendencias());

	private int idPendencia;
	private int idRequisito;
	private int idTipoParte;
	private String obsRequisicao;
	private String respostaCliente;
	private String observacaoCliente;
	private boolean operacional = true;
	private acCadastro cadastro;
	private String optReturn = "dlgPendencia";



    private List<UploadedFile> uploadedFiles;

	private Usuario usuario;

	@Inject
    private PendenciasService penService;

	@Inject
	private PendService pendenciasService;

	@Inject
    private PendenciasTipoService pendTipoService;

	@Inject
    private TipoParteService tipoParteService;

	@Inject
    private CadastroService cadastroService;

	@PostConstruct
	public void init() {

		clear();
        lstPendencias = penService.crud().listAll();
        lstTipoParte = tipoParteService.crud().listAll();


	}

    public void clear() {

        usuario = checkUsuario.valid();

        pendencias = new acPendencias();
        filter = new Filter<>(new acPendencias());
        uploadedFiles = new ArrayList<UploadedFile>();
        idPendencia = 0;
        idRequisito = 0;
        obsRequisicao = "";
        respostaCliente = "";
        idTipoParte = 0;

    }

    public void enviarPendencia() {

        usuario = checkUsuario.valid();

        cadastro = cadastroService.crud().get(pendencias.getCadastro().getId());

        if( cadastro.getStatus().getId() == 8 && cadastro.getStatus().getId() == 9 ) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso!","Essa pendência não pode ser mais resolvida, esta ficha esta fechada!"));
            return;
        }

        salvarUpload();

        cadastro.setStatus(new FichaStatus(219));
        pendencias.setObservacaoCliente((pendencias.getObservacaoCliente() == null ? pendencias.getObservacaoCliente() : "" ) + " " + respostaCliente);
        pendencias.setDtResposta(new Date());
        pendencias.setStatus(new FichaStatus(219));
        pendencias.setPendenciasStatus(new PendenciasStatus(209));
        pendencias.setUsuarioResposta(usuario);
        pendenciasService.crud().update(pendencias);
        cadastroService.crud().update(cadastro);

        try {
            clear();
            RequestContext.getCurrentInstance().execute("PF(" + optReturn + ").hide()");
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void findById(Integer id) {
        if (id == null) {
            throw new BusinessException("O id da acPendencias é obrigatório");
        }
        pendencias = pendenciasService.crud().get(id);
        if (pendencias == null) {
            throw new BusinessException("Pendencias não foi encontrado pelo id: " + id);
        }
        idPendencia = pendencias.getTipoPendencia().getPendencias().getId();

        lstTipoPendencias = new ArrayList<PendenciasTipo>();

        lstTipoPendencias = pendTipoService.crud().criteria().eq("pendencias.id", idPendencia)
                .list();

        idRequisito = pendencias.getTipoPendencia().getId();
        obsRequisicao = pendencias.getObservacaoRequisicao();

        try {

            observacaoCliente = pendencias.getObservacaoCliente().replace("null", "");
        } catch (NullPointerException e) {
            observacaoCliente = "";
        }
        idTipoParte = pendencias.getTipoParte().getId();

        cadastro = cadastroService.crud().get(pendencias.getCadastro().getId());

        this.id = id;

        try {
            loadImages();
            loadPdfs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formPendencias");
    }


    private void loadImages() throws IOException {

        Path dir = FileSystems.getDefault().getPath( AppUtils.dirImagens +
                AppUtils.formataCompetencia(pendencias.getDtCriacao()) + "/" );

        DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "DPC" + cadastro.getId() + " - "
        		+ pendencias.getId() + "*.{jpg,jpeg,png,gif,bmp,JPG,JPEG,PNG,GIF,BMP}" );

        //+ pendencias.getId()
        
        images = new ArrayList<>();
        
        for (Path path : stream) {
        	
            images.add( AppUtils.imageName(  "/" + AppUtils.formataCompetencia(pendencias.getDtCriacao()) +
                    "/"  + path.getFileName().toString(), true));

        }
        
        stream.close();

    }

    private void loadPdfs() throws IOException {
    	    	
    	Path dir = FileSystems.getDefault().getPath(AppUtils.dirImagens + AppUtils.formataCompetencia(pendencias.getDtCriacao()) + "/" );
    	    	
    	pdfFiles = new ArrayList<>();
    	pdfFileNames = new ArrayList<>();   	
     	
	   DirectoryStream<Path> stream = Files.newDirectoryStream(dir,"DPC" + cadastro.getId() + " - "
		                + pendencias.getId() + "*.{pdf,PDF}" );
		
    
    	for (Path path : stream){
    		
    		pdfFiles.add(AppUtils.pdfName(  "/" + AppUtils.formataCompetencia(pendencias.getDtCriacao()) + "/"  + path.getFileName().toString(), true));
    		
    		pdfFileNames.add(path.getFileName().toString());
    		
    		/*pdfFiles.add(AppUtils.pdfName(  "/" + AppUtils.formataCompetencia(pendencias.getDtCriacao()) +
                    "/"  + path.getFileName().toString(), true));*/
    		System.out.println(path.getFileName());
    		String t = String.valueOf(path.getFileName());       		
 
    	}
    	
    	
    }
    
    public void btnBack() {
        try {
            clear();
            RequestContext.getCurrentInstance().execute("PF(" + optReturn + ").hide()");
            if(optReturn.equalsIgnoreCase("dlgPenCliente")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
        }
    }

    public void dialog(int syscal) {

	    switch (syscal) {
            case 0:
                optReturn = "dlgPenCliente";
                break;
            default:
                optReturn = "dlgPendencia";
                break;
        }

    }


    public List<String> getImages() {
        return images;
    }

	public void carregarTipos(AjaxBehaviorEvent event) {

        Integer idPendencia = (Integer) ((UIOutput)event.getSource()).getValue();

        lstTipoPendencias = new ArrayList<>();

        lstTipoPendencias = pendTipoService.crud().criteria().eq("pendencias.id", idPendencia)
                .list();

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formPendencias:tabPendencia:tipoRequisito");
    }



	public void referencia(int id, Usuario usuario) {
	    pendencias = new acPendencias();
		this.id = id;
        this.usuario = usuario;
		filter.getEntity().setCadastro(new acCadastro(id));
		filter.addParam("cadastro.id", id);

	}

	public LazyDataModel<acPendencias> getLista() {
		if( lista == null ) {

			lista = new LazyDataModel<acPendencias>() {

				@SuppressWarnings("unchecked")
				@Override
				public List<acPendencias> load(int first, int pageSize,
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

					List<acPendencias> list = pendenciasService.paginate(filter);
					setRowCount(pendenciasService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public acPendencias getRowData(String key) {
					return pendenciasService.findById(new Integer(key));
				}
			};

		}
		return lista;
	}

	public void listaPrincipal(Usuario usuario) {

        usuario = checkUsuario.valid();

        if( usuario.isInterno() ) {
            lstPrincipal = pendenciasService.crud().findWithNamedQuery(acPendencias.FIND_ADMIN);

        } else {

            if (usuario.getPessoa().getFuncao().getArea().getRoot() != null) {
                lstPrincipal = pendenciasService.crud().findByNamedQuery(acPendencias.FIND_BY_FILIAL,
                        AppUtils.NamedParams("id", usuario.getPessoa().getFuncao().getArea().getId().toString()));
            } else {
                lstPrincipal = pendenciasService.crud().findByNamedQuery(acPendencias.FIND_BY_TRANSPORTADORA,
                        AppUtils.NamedParams("id", usuario.getPessoa().getFuncao().getArea().getId().toString()));
            }
        }

    }



	public void remove() {
		if( pendencias != null && pendencias.getId() != null) {
			pendenciasService.remove(pendencias);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pendencia removido com sucesso",null));
			clear();
		}
	}


    public void finalizar() {

        pendencias = pendenciasService.crud().get(idKey);
        pendencias.setPendenciasStatus(new PendenciasStatus(212));
        pendenciasService.crud().update(pendencias);

        if( pendenciasService.crud().ne("pendenciasStatus.id", 212).count() == 0 ) {
            acCadastro cadastro = cadastroService.crud().get(id);
            if (cadastro.getStatus().getId() == 6) {
                cadastro.setStatus(new FichaStatus(5));
                cadastroService.update(cadastro);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Pendência Finalizada com Sucesso!"));

        RequestContext.getCurrentInstance().execute("PF('dlgPendencia').hide()");


    }

    public void onRowSelect(SelectEvent event) {
        pendencias = (acPendencias) event.getObject();

        try {
            idKey = pendencias.getId();
            idPendencia = pendencias.getTipoPendencia().getPendencias().getId();
            findById(idKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


	public void update() {
        String msg;


        pendencias.setTipoPendencia(new PendenciasTipo(idRequisito));
        pendencias.setObservacaoRequisicao(obsRequisicao);

        pendencias.setTipoParte(new TipoParte(idTipoParte));

        if (pendencias.getId() == null) {

            //
            // Altera o status da Ficha
            // Para Aguardando Cliente
            //
            acCadastro cadastro = cadastroService.crud().get(id);
            cadastro.setStatus(new FichaStatus(6));
            cadastroService.crud().saveOrUpdate(cadastro);
            pendencias.setCadastro(cadastro);
            pendencias.setUsuarioCriacao(new Usuario(usuario.getId()));
            pendencias.setDtCriacao(new Date());
            pendencias.setDtRegistro(new Date());
            pendencias.setPendenciasStatus(new PendenciasStatus(205));
            try {
                pendenciasService.insert(pendencias);
            } catch (Exception e) {
                if( ! e.getLocalizedMessage().contains("Batch update returned unexpected row count from") ) {
                    e.printStackTrace();
                }
            }
            msg = "Pendencia criado com sucesso!";


        } else {
//
            // Altera o status da Ficha
            // Para Aguardando Cliente
            //
            pendencias.setUsuarioCriacao(new Usuario(usuario.getId()));
            pendencias.setDtResposta(new Date());
            try {
                pendenciasService.update(pendencias);
            } catch (Exception e) {
                if( ! e.getLocalizedMessage().contains("Batch update returned unexpected row count from") ) {
                    e.printStackTrace();
                }
            }
            int qtdPendencias = pendenciasService.crud().eq("dtTermino", null).count();

            if( qtdPendencias == 0 ) {
                //
                // Altera o status da Ficha
                // Para Aguardando Cliente
                //
                acCadastro cadastro = cadastroService.crud().get(id);
                cadastro.setStatus(new FichaStatus(5));
                cadastroService.crud().saveOrUpdate(cadastro);

            }
            msg = "Pendencia alterada com sucesso!";

        }

        clear();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));

        RequestContext.getCurrentInstance().execute("PF('dlgPendencia').hide()");


	}

    public void upload(FileUploadEvent event) {
        uploadedFiles.add(event.getFile());
    }

    public void salvarUpload() {

//	    int cliente = usuario.getPessoa().getFuncao().getArea().getRoot().getId();
//        int filial = usuario.getPessoa().getFuncao().getArea().getId();

    // Saber a quantidade de arquivos
    	
    	
    // Salvar o Arquivo...
	    String dir = AppUtils.dirImagens + AppUtils.formataCompetencia(pendencias.getDtCriacao() ) + "/" ; 
	    // + (cliente > 0 ? cliente + "/" : "") + (filial > 0 ? filial + "/" : "");
	    	    
        File files = new File(dir);
        
	    // Saber a quantia de arquivos dentro da pasta
    		File[] filesQtd = files.listFiles();
    			
    			int aux = 0;
    			
    				if ( filesQtd != null ) {
    					
    					int length = filesQtd.length;
    					
    						for ( int i = 0; i < length; i++) {
    							
    							File f = filesQtd[i];
    								
    								if ( f.isFile() ) {
    									aux++;
    								}    							
    						}
    					
    					
    				}

        // Outras funçõs do salvar
        
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Diretorios Criados com sucesso! (" + dir + ")");
            } else {
                System.out.println("Não foi possível criar diretorio! (" + dir + ")");
            }
        }

	    int idx = 1;
        //String filename = "DPC" + pendencias.getCadastro().getId() + " - " + pendencias.getId();
	    String filename = "DPC" + pendencias.getCadastro().getId() + " - " +  pendencias.getId() + " " + aux;
	    
        for (UploadedFile uploadedFile : uploadedFiles) {
        	
            AppUtils.saveImage(dir, uploadedFile, idx++, filename);
        }

        uploadedFiles.clear();

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setLista(LazyDataModel<acPendencias> lista) {
        this.lista = lista;
    }

    public List<Pendencias> getLstPendencias() {
        return lstPendencias;
    }

    public void setLstPendencias(List<Pendencias> lstPendencias) {
        this.lstPendencias = lstPendencias;
    }

    public List<PendenciasTipo> getLstTipoPendencias() {
        return lstTipoPendencias;
    }

    public void setLstTipoPendencias(List<PendenciasTipo> lstTipoPendencias) {
        this.lstTipoPendencias = lstTipoPendencias;
    }

    public List<acPendencias> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<acPendencias> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acPendencias getPendencias() {
        return pendencias;
    }

    public void setPendencias(acPendencias pendencias) {
        this.pendencias = pendencias;
    }

    public Filter<acPendencias> getFilter() {
        return filter;
    }

    public void setFilter(Filter<acPendencias> filter) {
        this.filter = filter;
    }

    public int getIdPendencia() {
        return idPendencia;
    }

    public void setIdPendencia(int idPendencia) {
        this.idPendencia = idPendencia;
    }

    public int getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(int idRequisito) {
        this.idRequisito = idRequisito;
    }

    public String getObsRequisicao() {
        return obsRequisicao;
    }

    public void setObsRequisicao(String obsRequisicao) {
        this.obsRequisicao = obsRequisicao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TipoParte> getLstTipoParte() {
        return lstTipoParte;
    }

    public void setLstTipoParte(List<TipoParte> lstTipoParte) {
        this.lstTipoParte = lstTipoParte;
    }

    public int getIdTipoParte() {
        return idTipoParte;
    }

    public void setIdTipoParte(int idTipoParte) {
        this.idTipoParte = idTipoParte;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Area getAreaCliente() {
        return areaCliente;
    }

    public void setAreaCliente(Area areaCliente) {
        this.areaCliente = areaCliente;
    }

    public List<acPendencias> getLstPrincipal() {
        return lstPrincipal;
    }

    public void setLstPrincipal(List<acPendencias> lstPrincipal) {
        this.lstPrincipal = lstPrincipal;
    }

    public String getRespostaCliente() {
        return respostaCliente;
    }

    public void setRespostaCliente(String respostaCliente) {
        this.respostaCliente = respostaCliente;
    }

    public boolean isOperacional() {
        return operacional;
    }

    public void setOperacional(boolean operacional) {
        this.operacional = operacional;
    }

    public String getOptReturn() {
        return optReturn;
    }

    public void setOptReturn(String optReturn) {
        this.optReturn = optReturn;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

	public List<String> getPdfFiles() {
		return pdfFiles;
	}

	public void setPdfFiles(List<String> pdfFiles) {
		this.pdfFiles = pdfFiles;
	}

	public List<String> getPdfFileNames() {
		return pdfFileNames;
	}

	public void setPdfFileNames(List<String> pdfFileNames) {
		this.pdfFileNames = pdfFileNames;
	}
    
    
}
