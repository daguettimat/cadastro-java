package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.*;
import br.com.global5.manager.chamado.Chamado;
import br.com.global5.manager.chamado.ChamadoResposta;
import br.com.global5.manager.chamado.ChamadoTipo;
import br.com.global5.manager.chamado.service.ChamadoRespostaService;
import br.com.global5.manager.chamado.service.ChamadoService;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.manager.model.auxiliar.Endereco;
import br.com.global5.manager.model.auxiliar.TipoEndereco;
import br.com.global5.manager.model.cadastro.*;
import br.com.global5.manager.model.ect.Cidade;
import br.com.global5.manager.model.ect.Logradouro;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.model.enums.*;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.auxiliar.CorService;
import br.com.global5.manager.service.cadastro.*;
import br.com.global5.manager.service.ect.CidadeService;
import br.com.global5.manager.service.ect.LogradouroService;
import br.com.global5.manager.service.ect.PaisService;
import br.com.global5.manager.service.ect.UFService;
import br.com.global5.manager.service.enums.*;
import br.com.global5.manager.service.geral.MercadoriaService;
import br.com.global5.manager.service.geral.MotoristaService;
import br.com.global5.template.exception.BusinessException;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.CDL;
import org.json.JSONArray;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@SessionScoped
public class EnviarFichaMB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5531039144682031121L;


    private ViaCEPEndereco endereco;
    private String cep;
    private List<ReferenciasPessoais> referenciasPessoais;
    private List<ReferenciasComerciais> referenciasComerciais;
    private List<ReferenciaVeiculos> referenciasVeiculos;
    private List<MotoristaVinculo> lstTipoMotorista;
    private List<VeiculoTipo> lstTipoVeiculo;
    private List<VeiculoCategoria> lstVeiculoCategoria;
    private List<TelefoneTipo> lstTipoTelefone;
    private List<ReferenciasTipoContato> lstTipoContatoPessoal;
    private List<ReferenciasTipoContato> lstTipoContatoComercial;
    private List<Mercadoria> lstMercadorias;
    private List<Pais> lstPais;
    private List<UF> lstUF;
    private List<UF> lstUFOrigem;
    private List<UF> lstUFDestino;
    private List<Cidade> lstCidadeOrigem;
    private List<Cidade> lstCidadeDestino;
    private List<Marca> lstMarca;
    private List lstModelo;
    private List<Cor> lstCor;
    private List<CNHCategoria> lstCategoriaCNH;
    private int tipoMotorista;
    private UploadedFile imgFile = null;
    private UploadedFile imgCnhFrenteFile = null;
    private UploadedFile imgCnhVersoFile = null;
    
    private String stsImgFile = "";
    private StreamedContent scImgFile = null;
    private String stsImgCnhFrenteFile = "";
    private String stsImgCnhVersoFile = "";
    
    private boolean changeImgFile = false;
    private boolean changeImgFileCnhFrente = false;
    private boolean changeImgFileCnhVerso = false;
    

    private ReferenciasPessoais refPessoal;
    private ReferenciasComerciais refComercial;
    private ReferenciaVeiculos refVeiculo;

    private UploadedFile file;
    private FichaCliente fichaCliente;

    private boolean foundEndereco;
    private boolean foundBairro;
    private boolean foundCidade;
    private boolean foundUF;
    private boolean habilitaUF;
    private boolean habilitaCidade;
    private boolean painelMotorista;
    private boolean painelVeiculo;
    private boolean disable;
    private boolean veiculoNacional = true;
    private Usuario usuario;
    private Area area = new Area();
    private Area areaFilial = new Area();
    private Integer idMercadoria;

    private boolean foundCadMot;
    private String  urlFotoMot;
    private String  urlFotoCnhF;
    private String  urlFotoCnhV;
    
    private boolean showRefPessoal;
    private boolean showRefComercial;

    private boolean success;
    private boolean limitadorVeiculo;

    private Integer pais;
    private Integer idCadastral;

    private Integer paisOrigem;
    private Integer ufOrigem;
    private Integer cidOrigem;

    private Integer paisDestino;
    private Integer ufDestino;
    private Integer cidDestino;

    private String docProprietario;
    private String nomProprietario;
    private String centroCusto;
    
    private Boolean validoDocProprietarioNacional = false;
    private Boolean renavamDuplicado = false;
    private Boolean renavamVerificado = false;
    private String  msgResultPesquisaPlaca = "";
    private Boolean btnSalvarVeiculo = false;
    private Boolean placaEncontrada  = false;
    
    private String msgPanelPlaca = "";
    private String msg1PanelPlaca = "";
    private String msg2PanelPlaca = "";
    private boolean atualizaPlaca = false;
    private boolean aplicarAtualizaPlaca = false;
    private boolean abrirChamadoPlaca = false;
    private boolean requisitarAtendimento = false;
    private String  numRenavamAtualizaPlaca = "";
    private String  numRenavamInformaCliente = "";
    private String  numPlacaAnterior = "";
    private String  msgChamadoCliente = "";
    private String  placaMercosulConvertidaChamado = "";
    private String  placaMercosulPesquisadaChamado = "";
    
    private List<UploadedFile> uploadedFiles;
    
    private Motorista motorista;

    @Inject
    private MotoristaService motService;

    @Inject
    private LocalizadorService locService;

    @Inject
    private MotoristaVinculoService motVinculoService;
    
//    @Inject
//    private FichaClienteService fichaClienteService;

    @Inject
    private Crud<FichaCliente> fichaClienteCrud;

    @Inject
    private FichaClienteService fichaService;

    @Inject
    private LogradouroService logrService;

    @Inject
    private VeiculoTipoService veiTipoService;

    @Inject
    private VeiculoService veiculoService;

    @Inject
    private MarcaService marcaService;

    @Inject
    private ModeloService modeloService;

    @Inject
    private CorService corService;

//    @Inject
//    private AreaFuncaoService funcaoService;
//
//    @Inject
//    private ProprietarioService propService;

    @Inject
    private VeiculoCategoriaService veiCatService;

    @Inject
    private AreaService areaService;

//    @Inject
//    private TelefoneService telefoneService;

    @Inject
    private TelefoneTipoService telefoneTipoService;

    @Inject
    private CNHService cnhService;

    @Inject
    private CNHCategoriaService cnhCategoriaService;

    @Inject
    private PaisService paisService;

    @Inject
    private UFService ufService;

    @Inject
    private CidadeService cidService;

    @Inject
    private ReferenciasTipoContatoService refTipoContatoService;

    @Inject
    private MercadoriaService merService;
    
    @Inject
    private ChamadoService chamadoService;
    
    @Inject
    private ChamadoRespostaService chamadoRespostaService;
    
    private boolean habilitaCidadeDestino;
    private boolean habilitaUFDestino;
    private boolean showMercadoria;
    private boolean showCNH;

    private boolean habilitaArea;
    private boolean habilitaFilial;


    @PostConstruct
    public void init() {

        checkUsuario.valid();

        referenciasPessoais = new ArrayList<>();
        referenciasComerciais = new ArrayList<>();
        referenciasVeiculos = new ArrayList<>();
        endereco = new ViaCEPEndereco();
        refPessoal = new ReferenciasPessoais();
        refComercial = new ReferenciasComerciais();
        refVeiculo = new ReferenciaVeiculos();
        fichaCliente = new FichaCliente();
        fichaCliente.setNacional(true);
        fichaCliente.setTipoTelefone(new TelefoneTipo(79));
        fichaCliente.setCategoria(0);

        uploadedFiles = new ArrayList<UploadedFile>();

        pais = 31;

        paisOrigem = 0;
        ufOrigem = 0;
        cidOrigem = 0;

        paisDestino = 0;
        ufDestino = 0;
        cidDestino = 0;

        painelMotorista = true;
        painelVeiculo = true;
        veiculoNacional = true;

        habilitaCidade = false;
        habilitaUF = true;

        tipoMotorista = 0;
        idMercadoria = 0;
        showMercadoria = false;

        atualizaPlaca = false;
        aplicarAtualizaPlaca = false;
        numPlacaAnterior = "";
        
        lstTipoMotorista = motVinculoService.crud().isNull("exclusao").list();
        lstTipoContatoPessoal = refTipoContatoService.crud().isNull("exclusao").and().eq("referenciasTipo.id", 31).list();
        lstTipoContatoComercial = refTipoContatoService.crud().isNull("exclusao").and().eq("referenciasTipo.id", 32).list();

        lstMercadorias = merService.crud().isNull("dtExclusao").list();

        Collections.sort(lstMercadorias, new Comparator<Mercadoria>() {
            @Override
            public int compare(Mercadoria merc1, Mercadoria merc2) {
                return merc1.getDescricao().compareTo(merc2.getDescricao());
            }
        });

        lstUF = ufService.crud().listAll();

        Collections.sort(lstUF, new Comparator<UF>() {
            @Override
            public int compare(UF uf1, UF uf2) {
                return uf1.getSigla().compareTo(uf2.getSigla());
            }
        });

        lstTipoVeiculo = veiTipoService.crud().isNull("exclusao").list();
        Collections.sort(lstTipoVeiculo, new Comparator<VeiculoTipo>() {
            @Override
            public int compare(VeiculoTipo vt1, VeiculoTipo vt2) {
                return vt1.getDescricao().compareTo(vt2.getDescricao());
            }
        });

        lstMarca = marcaService.crud().isNull("dtExclusao").list();
        Collections.sort(lstMarca, new Comparator<Marca>() {
            @Override
            public int compare(Marca m1, Marca m2) {
                return m1.getNome().compareTo(m2.getNome());
            }
        });

        lstCor = corService.crud().isNull("dtExclusao").list();
        Collections.sort(lstCor, new Comparator<Cor>() {
            @Override
            public int compare(Cor c1, Cor c2) {
                return c1.getNome().compareTo(c2.getNome());
            }
        });

        lstTipoTelefone = telefoneTipoService.crud().isNull("exclusao").list();

        lstCategoriaCNH = cnhCategoriaService.crud().isNull("exclusao").list();

        lstPais = paisService.crud().listAll();

        Collections.sort(lstPais, new Comparator<Pais>() {
            @Override
            public int compare(Pais p1, Pais p2) {
                return p1.getNome().compareTo(p2.getNome());
            }
        });

        foundEndereco = false;
        showRefComercial = true;
        showRefPessoal = true;
        showCNH = true;
        
        foundCadMot = false;
        urlFotoMot = "";
        urlFotoCnhF = "";
        urlFotoCnhV = "";
        stsImgFile = "";
        stsImgCnhFrenteFile = "";
        stsImgCnhVersoFile = "";
        
        
        imgFile = null;
        imgCnhFrenteFile = null;
        imgCnhVersoFile = null;
        scImgFile = null;
        
        msgResultPesquisaPlaca = ""; 
        
        success = false;

        limitadorVeiculo = false;

        usuario = checkUsuario.valid();

        if (usuario.isInterno() || usuario.getTipo().equals("A") || getUsuario().getTipo().equals("H")) {
            habilitaArea = true;
            habilitaFilial = true;
            disable = true;
        } else {
            if (usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2) {
                area = usuario.getPessoa().getFuncao().getArea();
                habilitaArea = false;
                habilitaFilial = true;
                disable = false;
            } else {
                if (usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 3) {
                    area = usuario.getPessoa().getFuncao().getArea().getRoot();
                    areaFilial = usuario.getPessoa().getFuncao().getArea();
                    habilitaFilial = false;
                    habilitaArea = false;
                    disable = false;
                }
            }
        }

    }

    @PreDestroy
    public void closeAll() {
        cnhService.crud().getSession().close();
    }

    public String foto(String id) {
        if (id.length() == 0) {
            return AppUtils.imageName("foto_3x4.png", true);
        }
        try {
        	
            motorista = motService.crud().get(Integer.valueOf(id));
            return AppUtils.foto(motorista.getId(), motorista.getUrlFoto());
        	
        } catch (Exception e) {
            return AppUtils.imageName("foto_3x4.png", true);
        }


    }

    public void showDialog(String dialog) {
        RequestContext.getCurrentInstance().execute("Top();PF('" + dialog + "').show();");
    }



    public void removeTerceiro() {
        try {
            lstTipoMotorista = motVinculoService.crud().isNull("exclusao").list();
            if (painelMotorista && !painelVeiculo) {
                for (int a = 0; a <= lstTipoMotorista.size(); a++) {
                    for (Iterator<MotoristaVinculo> iter = lstTipoMotorista.listIterator(); iter.hasNext(); ) {
                        MotoristaVinculo mv = iter.next();
                        if (mv.getId() == 93 ) {
                            iter.remove();
                        }
                    }
                }
            }

            if (painelMotorista && painelVeiculo) {

                for (Iterator<MotoristaVinculo> iter = lstTipoMotorista.listIterator(); iter.hasNext(); ) {
                    MotoristaVinculo mv = iter.next();
                    if (mv.getId() == 94 || mv.getId() == 96 || mv.getId() == 98 ) {
                        iter.remove();
                    }
                }
            }

        } catch (Exception e) {}
    }

    public void nacional() {

        RequestContext context = RequestContext.getCurrentInstance();

        context.update("form0");

        if (fichaCliente.isNacional()) {
            context.execute("cnhSHOW();");
            showCNH = true;

        } else {        	
        	
            //lstTipoMotorista = new ArrayList<MotoristaVinculo>();
        	
            //setLstTipoMotorista(motVinculoService.crud().criteria().between("id", 92, 93).list());
            //context.update("form0:tipoMotorista");
            
        	context.execute("cnhHIDE();");
            showCNH = false;            
        }

       
    }

    public void nacionalb() {

        RequestContext context = RequestContext.getCurrentInstance();
      
        if (fichaCliente.isNacional()) {
      

        } else {        	
        	
            lstTipoMotorista = new ArrayList<MotoristaVinculo>();
        	
            lstTipoMotorista = motVinculoService.crud().criteria().between("id", 92, 93).list();
            context.update("form0:tipoMotorista");
            
        }


    }
    
    public void veiNacional() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (veiculoNacional) {
        	
            //context.execute("veiNacionalShow();");
        	String resultFormatoPlaca = validaFormatoPlacaNacional();
        		
        		if (resultFormatoPlaca.equals("erro")){
        			
        			this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + " não encontrada!  Formato inválido para a inclusão do veículo  tipo nacional.");
        			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atenção","Formato da placa inválida para tipo Nacional!"));
        			
        		} else {
        			
        			if (this.getPlacaEncontrada()== true){
        				
        				this.setMsgResultPesquisaPlaca("Encontrado registro para a Placa : " + refVeiculo.getPlaca() + " , edição de dados aberta para uso. " ) ;
        				
        			} else {
        				
        				this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + " não encontrada!  Formato válido para a inclusão do veículo  tipo nacional.");
        				
        			}
        			
                    veiculoNacional = true;
                    refVeiculo.setNacional(true);        			
        		}

        } else {
        	
            //context.execute("veiNacionalHide();");
			if (this.getPlacaEncontrada()== true){
				
				this.setMsgResultPesquisaPlaca("Encontrado registro para a Placa : " + refVeiculo.getPlaca() + " , edição de dados aberta para uso. " ) ;
				
			} else {
				
				this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + " não encontrada!  Formato válido para a inclusão do veículo  tipo estrangeiro.");
				
			}
			
            veiculoNacional = false;
            refVeiculo.setNacional(false);
            
        }

       // refVeiculo.setPlaca("");
        context.update("formVeiculo");

    }

    public String fotoCNH(String id, boolean tipo) {
        String result = null;
        CNH cnh = null;
        try {
            if (id.length() == 0) {
                return AppUtils.imageName(tipo ? "cnh-frente.jpg" : "cnh-verso.png", true);
            }
            if (cnhService.crud().eq("motorista.id", Integer.valueOf(id)).count() > 1) {
                cnh = cnhService.crud().eq("motorista.id", Integer.valueOf(id)).addOrderDesc("dtCadastro").list().get(0);
            } else {
                cnh = cnhService.crud().eq("motorista.id", Integer.valueOf(id)).find();
            }
        } catch (Exception e) {
            return null;
        }

        if (cnh == null) {
            return AppUtils.imageName(tipo ? "cnh-frente.jpg" : "cnh-verso.png", true);
        }

        if (cnh.getUrlCnhFrente() == null && tipo) {
            result = AppUtils.imageName("cnh-frente.jpg", true);
        }

        if (cnh.getUrlCnhVerso() == null && !tipo) {
            result = AppUtils.imageName("cnh-verso.png", true);
        }

        if (cnh.getUrlCnhFrente() != null && tipo) {
            result = AppUtils.imageName(cnh.getUrlCnhFrente(), true);
        }

        if (cnh.getUrlCnhVerso() != null && !tipo) {
            result = AppUtils.imageName(cnh.getUrlCnhVerso(), true);
        }

        return result;
    }

    public void escolherAcaoMotorista(AjaxBehaviorEvent event) {

        Integer selecao = (int) ((UIOutput) event.getSource()).getValue();

        switch (selecao) {
            case 92: // Agregado
                showRefPessoal = true;
                showRefComercial = false;
                showMercadoria = false;
                break;
            case 94: // Funcionario (Outras Funcoes)
            case 95: // Funcionario acMotorista
            case 98: // Ajudante (Funcionario)
            case 97: // Consulta
                showRefPessoal = false;
                showRefComercial = false;
                showMercadoria = false;
                break;
            case 93: // Terceiro
                showRefPessoal = true;
                showRefComercial = true;
                showMercadoria = true;
                break;
            default: // Outros
                showRefComercial = true;
                showRefPessoal = true;
                showMercadoria = true;
                break;
        }
        
        RequestContext context = RequestContext.getCurrentInstance();

        context.update("form0:btnRefRC");
        context.update("form0:btnReferencias");
        context.update("form0");

        switch (selecao) {
            case 94:
            case 96:
            case 98:
            case 99:
            case 100:
                context.execute("cnhHIDE();");
                showCNH = false;
                break;
            default:
                context.execute(fichaCliente.isNacional() ? "cnhSHOW();" : "cnhHIDE();");
                showCNH = fichaCliente.isNacional();
                break;
        }

    }

    public String buscaTipoContato(int tipo, int Categoria) {

        try {
            if( tipo == 0 && Categoria == 0 ) {
               return "";
            }
            ReferenciasTipoContato refContato = new ReferenciasTipoContato();
            refContato = refTipoContatoService.crud().eq("referenciasTipo.id", Categoria).eq("id", tipo).find();
            if( refContato != null ) {
                return refContato.getDescricao();
            } else {
                return "";
            }
        } catch (NullPointerException e) {
            return "";
        }

    }


    public void addReferencia() {

        if (refPessoal != null) {
            refPessoal.setId(referenciasPessoais.size() + 1);
            referenciasPessoais.add(refPessoal);
            refPessoal = new ReferenciasPessoais();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:PanRefPessoal");
        }

    }

    public void addReferenciaComercial() {

        success = refComercial.getContato().length() > 2;

        if (refComercial.getDdd().length() <= 1) {
            success = false;
        }

        if (refComercial.getTelefone().length() <= 7) {
            success = false;
        }


        if (success) {
            RequestContext.getCurrentInstance().execute("PF('dialogRC').hide()");
        } else {
            return;
        }
        if (refComercial != null) {
            refComercial.setId(referenciasComerciais.size() + 1);

            referenciasComerciais.add(refComercial);
            refComercial = new ReferenciasComerciais();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:PanRefComercial");
        }
    }

    public void delReferenciaPessoal(Integer id) {
        if (refPessoal != null) {
            referenciasPessoais.remove(id - 1);
            int idx = 1;
            for (ReferenciasPessoais refPessoal : referenciasPessoais) {
                refPessoal.setId(idx++);
            }
            refPessoal = new ReferenciasPessoais();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:PanRefPessoal");
        }
    }

    public void delReferenciaComercial(Integer id) {
        if (refComercial != null) {
            referenciasComerciais.remove(id - 1);
            int idx = 1;
            for (ReferenciasComerciais refCom : referenciasComerciais) {
                refCom.setId(idx++);
            }
            refComercial = new ReferenciasComerciais();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:PanRefComercial");
        }
    }

    public void clearPessoal() {
        if( ! validaCampos() ) {
            return ;
        }

        showDialog("dialog");

        refPessoal = new ReferenciasPessoais();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formRefPessoal");

    }


    public void clearComercial() {

        if( ! validaCampos() ) {
            return ;
        }

        if( ! validaReferenciaPessoal() ) {
            return ;
        }

        showDialog("dialogRC");

        refComercial = new ReferenciasComerciais();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formRefComercial");

    }

    public void clearVeiculos() {

        if( painelMotorista ) {
            if (!validaCampos()) {
                return;
            }

            if (!validaReferenciaPessoal()) {
                return;
            }

            if (!validaReferenciaComercial()) {
                return;
            }
        }

        veiculoNacional = true;
        refVeiculo = new ReferenciaVeiculos();
        refVeiculo.setProprietario(new br.com.global5.manager.model.auxiliar.Proprietario());
        refVeiculo.setNacional(true);
        docProprietario = "";
        nomProprietario = "";
        msgResultPesquisaPlaca = "";
        placaEncontrada = false;

        showDialog("dlgVeiculo");

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo");


    }

    public void delReferenciaVeiculos(Integer id) {
        if (refVeiculo != null) {
            try {
                referenciasVeiculos.remove(id - 1);
                int idx = 1;
                for (ReferenciaVeiculos refVeiculos : referenciasVeiculos) {
                    refVeiculos.setId(idx++);

                }
            } catch (Exception e) {
                e.printStackTrace();
                referenciasVeiculos.clear();
            }
            refVeiculo = new ReferenciaVeiculos();

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:panelVeiculos");
        }
        if (referenciasVeiculos.size() <= 3) {
            limitadorVeiculo = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:btnRefVeiculo");
        }
    }
    
    
    /**
     * Method: record the values of vehicles in list at internal class 'ReferenciaVeiculos' for soon will by used at procedure for persistence at database. 
     * 
     * created at: not information. 
     * 
     * @author Francis Bueno
     * 
     * modifications:
     * 
     * change date: 25/10/2018
     *  
     * 1 - The conditions for variable 'veiculoNacional = true' 
     * -----1.1 - include validation at 'validaFormatoPlacaNacional' for national vehicle. Not allowed invalid format, allowed (ABC-123,ABC-1234 and ABC1D23);
     * -----1.2 - include validation at 'ValidaBrasil.validateRENAVAM' not allowed for invalid number at vehicle document.   
     * 
     * 2 - The condition for variable 'validoDocProprietarioNacional = false'
     * -----2.1 - include validation at 'verificaDocNacional' not allowed for invalid number at personal document.
     * 
     */
    public void addReferenciaVeiculos() {

        success = refVeiculo.getRenavam().length() > 6;
        
        if (refVeiculo.getTipo() == null) {
            success = false;

        }

        if (nomProprietario.length() == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta Nome Proprietário!",
                            "O nome do proprietário é obrigatório no cadastro de veículos!"));
            success = false;
        }
        
        if (veiculoNacional) {

        	String resultFormatoPlaca = validaFormatoPlacaNacional();
        		
        		if (resultFormatoPlaca.equals("erro")){
        			
        			FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!",
                                    "Não foi possível gravar esse registro. Verifique o número da placa. Formato inválido!"));
        			
        			this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + " não encontrada!  Formato inválido para a inclusão do veículo  tipo nacional.");
        			
        			success = false;
        			
        		}
        		
        		  if( refVeiculo.getRenavam().length() == 0  || ! ValidaBrasil.validateRENAVAM(refVeiculo.getRenavam()))  {
        	            FacesContext.getCurrentInstance().addMessage(
        	                    null,
        	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esse RENAVAM não pode ser "
        	                            + " validado confira e digite novamente!", "Erro"));
        	            success = false;
        	        }

        }
                
        if (validoDocProprietarioNacional == false) {
        	
        	String checaDoc = verificaDocNacional();
        	
        	if( checaDoc.equals("cpfErro") || checaDoc.equals("cnpjErro")){
        		validoDocProprietarioNacional = false;
        		
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF/CNPJ!",
                        "O Número do Documento CPF/CPNJ do proprietário está inválido, redigite!"));
        		
        		success = false;
        	}        	
        	
        }
        
        if ( renavamDuplicado == true && placaEncontrada == false ) {
        	        	         	
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Salvar",
                    "O Renavam informado já encontra-se registrado para outro veículo, redigite!"));
    		
    		success = false;
    		
        }
        
        if (docProprietario.length() == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta Número Documento Proprietário!",
                            "O Número do Documento do proprietário é obrigatório no cadastro de veículos!"));
            success = false;
        }


        if (success) {
            RequestContext.getCurrentInstance().execute("PF('dlgVeiculo').hide()");
        } else {
            return;
        }

        if (refVeiculo != null) {
        	
            refVeiculo.setId(referenciasVeiculos.size() + 1);
            refVeiculo.setNomeproprietario(nomProprietario);
            refVeiculo.getProprietario().setNome(nomProprietario);
            refVeiculo.getProprietario().setDocumento(docProprietario);
            
            referenciasVeiculos.add(refVeiculo);
            
            refVeiculo = new ReferenciaVeiculos();
            
            refVeiculo.setProprietario(new br.com.global5.manager.model.auxiliar.Proprietario());
            refVeiculo.getProprietario().setEndereco(new Endereco());
            nomProprietario = "";        
            
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:panelVeiculos");
            
            if (referenciasVeiculos.size() == 4) {
                limitadorVeiculo = true;
                context.update("form0:btnRefVeiculo");
            }

        }

    }

    public Motorista pesquisa(String contexto) {

        if (AppUtils.isInteger(contexto)) {

            List retorno = motService.crud().criteria().findByNamedQuery(
                    Motorista.FIND_BY_DOC1,
                    AppUtils.NamedParams("doc1", contexto));
            if (retorno.size() != 1) {
                motService.crud().criteria().findByNamedQuery(
                        Motorista.FIND_BY_DOC2,
                        AppUtils.NamedParams("doc2", contexto));
            }

            if (retorno.size() > 0) {
                Motorista motFound = (Motorista) retorno.get(0);
                // motService.crud().getSession().update(motFound);
                Hibernate.initialize(motFound.getLocalizador());
                Hibernate.initialize(motFound.getUsuarioCriacao());
                Hibernate.initialize(motFound.getUsuarioAlteracao());

                foto(motFound.getId().toString());
                return motFound;
            }
        }

        return null;
    }

    public void findModelo(AjaxBehaviorEvent event) {

        Integer marca = (Integer) ((UIOutput) event.getSource()).getValue();

        lstModelo = new ArrayList<Modelo>();

        lstModelo = modeloService.crud().criteria().eq("marca.id", marca)
                .list();

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiModelo");

    }

    public void findCategoria(AjaxBehaviorEvent event) {

        Integer categoria = (Integer) ((UIOutput) event.getSource()).getValue();

        lstVeiculoCategoria = new ArrayList<>();

        lstVeiculoCategoria = veiCatService.crud().eq("tipo", (categoria == 59 ? "C" : "R")).isNull("exclusao").list();

        Collections.sort(lstVeiculoCategoria, new Comparator<VeiculoCategoria>() {
            @Override
            public int compare(VeiculoCategoria catVeic1, VeiculoCategoria catVeic2) {
                return catVeic1.getDescricao().compareTo(catVeic2.getDescricao());
            }
        });

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiCategoria");

    }
    
    
    /**
     * Method: Your function it is validate the number of document the  truck  owner from  type vehicle National at moment at change value
     * created at: 25/10/2018
     * 
     * @author Francis Bueno
     * 
     * @param event	
     * 
     */
    public void verificaDoc(AjaxBehaviorEvent event) {

        String doc = (String) ((UIOutput) event.getSource()).getValue();
        
        doc = (Normalizer
                .normalize(doc, Normalizer.Form.NFD)
                .replaceAll("[\\W]", ""));

        if(refVeiculo.isNacional()){
        	
        	if(doc.length() < 13 && ! ValidaBrasil.validateCPF(doc)){
        		validoDocProprietarioNacional = false;
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção","CPF inválido verifique e digite novamente."));
        		//throw new BusinessException("CPF Inválido!!! Por favor confira e digite novamente!");
        	} else if(doc.length() < 13 && ValidaBrasil.validateCPF(doc)){
        		validoDocProprietarioNacional = true;
        	}
        	
    		if(doc.length() > 12 && ! ValidaBrasil.validateCNPJ(doc) ){
    			validoDocProprietarioNacional = false;
    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção","CNPJ inválido verifique e digite novamente."));
    		} else if(doc.length() > 12 &&  ValidaBrasil.validateCNPJ(doc) ){
    			validoDocProprietarioNacional = true;
    		}
    		
        } else {
        	
        	 validoDocProprietarioNacional = false;
        }
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiPropDoc");
        
    }
    
    
    /**
     * Method: Your function it is validate the number of document the  truck  owner from  type vehicle National   
     * 
     * created at: 25/10/2018
     *  
     * @author Francis Bueno
     * 
     * @return string by erros 'cpfErro' to CPF , 'cnpjErro' to CNPJ. String by right 'cpfCnpjOk' 
     *  
     */
    public String verificaDocNacional(){
    	
    	String doc = "";
    	
    	if( docProprietario != null && refVeiculo.isNacional() == true){
    		
    		doc = (Normalizer
                    .normalize(docProprietario, Normalizer.Form.NFD)
                    .replaceAll("[\\W]", ""));
    		
        	if(doc.length() < 13 && ! ValidaBrasil.validateCPF(doc)){
        		validoDocProprietarioNacional = false;
        		return "cpfErro";
        	} else if(doc.length() < 13 && ValidaBrasil.validateCPF(doc)){ 
        		validoDocProprietarioNacional = true;
        	}
    		
    		if(doc.length() > 12 && ! ValidaBrasil.validateCNPJ(doc) ){
    			validoDocProprietarioNacional = false;
    			return "cnpjErro";    			
    		} else if(doc.length() > 12 && ValidaBrasil.validateCNPJ(doc) ){
    			validoDocProprietarioNacional = true;
    		}
        	
    	} else {
    		validoDocProprietarioNacional = false;
    	}
    	return "cpfCnpjOk";
    }

    public void findMotorista(AjaxBehaviorEvent event) {

        String cpf = (String) ((UIOutput) event.getSource()).getValue();

        if (fichaCliente.isNacional()) {
            if (cpf.length() < 10 || ! ValidaBrasil.validateCPF(cpf)) {
                throw new BusinessException("CPF Inválido!!! Por Favor confira e digite novamente!");
            }
            cpf = (Normalizer
                    .normalize(cpf, Normalizer.Form.NFD)
                    .replaceAll("[\\W]", ""));
            fichaCliente.setCpf(cpf);
            
        }

        Motorista mot = pesquisa(cpf);
        if (mot == null) {
            mot = new Motorista();
            mot.setUsuarioCriacao(usuario);
            mot.setUsuarioAlteracao(usuario);
            this.setFoundCadMot(false);
        } else {
        	 this.setFoundCadMot(true);
            //
            // Dados da CNH
            //

            if (fichaCliente.isNacional()) {
                CNH cnh = new CNH();
                if (cnhService.crud().eq("motorista.id", mot.getId()).eq("dtSubstituicao", null).count() > 0) {
                    List<CNH> cnhlst = cnhService.crud().eq("motorista.id", mot.getId()).eq("dtSubstituicao", null).list();
                    cnh = cnhlst.get(cnhlst.size()-1);
                }

                if (cnh.getId() != null) {
                    fichaCliente.setCnh(cnh.getDocumento());
                    fichaCliente.setNumSegCnh(cnh.getValidacao());
                    fichaCliente.setDtEmissaoCNH(cnh.getDtAtualizacao());
                    fichaCliente.setDtValidadeCNH(cnh.getDtVencimento());
                    fichaCliente.setCategoria(cnh.getCategoria().getId());
                    fichaCliente.setDtPrimeiraEmissaoCNH(cnh.getDtPrimeiraEmissao());
                    fichaCliente.setUfCNH(cnh.getUf());
                    fichaCliente.setRegistroCNH(cnh.getRegistro());
                }

            }

            //
            //  Dados do acMotorista
            //
            fichaCliente.setNome(mot.getNome());
            fichaCliente.setDtNascimento(mot.getDtNascimento());
            fichaCliente.setFic_rg(mot.getDoc2());

            fichaCliente.setDtEmissaoRG(mot.getDoc2_emissao());
            fichaCliente.setUfRg(mot.getDoc2_uf());
            fichaCliente.setCidadeNascimento(mot.getNaturalidade());
            fichaCliente.setPai(mot.getPai());
            fichaCliente.setMae(mot.getMae());
            fichaCliente.setNome(mot.getNome());
            fichaCliente.setEndereco(mot.getLocalizador().getLogradouro());
            fichaCliente.setBairro(mot.getLocalizador().getBairro());
            fichaCliente.setCidade(mot.getLocalizador().getCidade());
            fichaCliente.setUf(mot.getLocalizador().getUf());
            fichaCliente.setCep(mot.getLocalizador().getCep());
            fichaCliente.setNumero(mot.getLocalizador().getNumero());
            fichaCliente.setMotorista(mot);
            fichaCliente.getMotorista().setUrlFoto(mot.getUrlFoto());
            //fichaCliente.motorista.id	

            if (fichaCliente.getCep() != null) {
                if (fichaCliente.getCep().equals("00000-000")) {
                    fichaCliente.setCep("");
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.update("form0:cepMotorista");
                }
            }
        }
        RequestContext.getCurrentInstance().reset("form0");

    }
    
    public void selectTransp(SelectEvent event) {
        area = (Area) event.getObject();
        // areaFilial = null;
    }

    public void selectFilial(SelectEvent event) {
        areaFilial = (Area) event.getObject();
        disable = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form0:btnMotorista");
        context.update("form0:btnVeiculos");
        context.update("form0:btnCompleta");
    }

    public List<Area> listFilial(String query) {

        return areaService.listNome(query, 3, area.getId());

    }

    public String showTipo(Integer id) {
        try {
            return veiTipoService.crud().criteria().get(id).getDescricao();
        } catch (Exception e){
            return "";
        }

    }

    public String showMarca(Integer id) {
        try {
            return marcaService.crud().criteria().get(id).getNome();
        } catch (Exception e) {
            return "";
        }

    }

    public String showModelo(Integer id) {
        try {
            return modeloService.crud().criteria().get(id).getNome();
        } catch (Exception e){
            return "";
        }
    }
    
    
    /**
     * Method: used at validation for national vehicle. Not allowed invalid format. Allowed (ABC-123, ABC-1234 and ABC1D23)  
     *  
     * created at : 25/10/2018
     * 
     * @author Francis Bueno
     * 
     * @return 'erro' to denied and 'valido' to allowed 
     * 
     */
    public String validaFormatoPlacaNacional(){
    	
    	Integer qtdCaracteresPlaca = null;
    	String  caracterPosicao = "";
    	String  caracterPosHifen = "";
    	
    	if( refVeiculo.getPlaca() != null){
    		
    		qtdCaracteresPlaca = refVeiculo.getPlaca().length();

    		// Nacional Antiga 7 caracteres ABC-123 (hifem na contagem)    	    		
    		// Nacional Atual 8 caracteres ABC-1234 (hifem na contagem , placa anterior a padrão Mercosul)    		
    		// Nacional Padrao Mercosul 7 caracteres ABC1D34 		
    		
    		if( qtdCaracteresPlaca == 7 || qtdCaracteresPlaca == 8  ){
    			
    			caracterPosicao = refVeiculo.getPlaca().substring(3,4); 
    			
    			// Placa tipo padrao antigo (ABC-1234)
    			if( caracterPosicao.equals("-") ){
    				
    				caracterPosHifen = refVeiculo.getPlaca().substring(4,qtdCaracteresPlaca);    				
    				
    				Boolean checkAlfPosHifen = checarStringAlfabeto(caracterPosHifen);
    				
    				if( checkAlfPosHifen == true ) {    					
    					return "erro";
    				}  else {
    					return "valido";
    					//return "oldPlaca";
    				}
    				
    			} else {
    				
    				if ( qtdCaracteresPlaca == 7) {    					
    					//Padrão Mercosul
    					
    					// localiza se o codigo tem hifem (caso exista não permite a inclusão)
    					int findHifen = refVeiculo.getPlaca().indexOf("-");					// senão encontrar apresenta -1 senão retorna a posição;
    					
    					if (findHifen == -1) {
    						//caso a placa não tenha o hifen
    						List<Character> numbers = new ArrayList<>();
    						List<Character> letters = new ArrayList<>();
    						
    						for(char ch: refVeiculo.getPlaca().toCharArray()) {
    							
    							if( Character.isDigit(ch)){
    								numbers.add(ch);
    							} else {
    								letters.add(ch);
    							}
    						}
    						
    						//Placa Nacional Convencional
    						if(letters.size() == 3 && numbers.size() == 4) {
    							return "erro";
    						}

    						//Placa Nacional padrão Mercosul
    						if(letters.size() == 4 && numbers.size() == 3) {
    							return "valido";
    						}
    						
    					} else {
    						
    						return "erro";	
    					}    					
    					
    				} 
    				
    				if ( qtdCaracteresPlaca == 6 ) {
    					// não permite placa com 6 caracteres para tipo nacional
    					return "erro";
    				}
    				
    			}
    			
    		}
    		
    	}
    	return "erro";
    }
    
    
    public String conversaoPlacaAntesTipoMercosul(){
    	
    	String caracterPosicao = "";
    	String curingaAlfaNumber = "";
    	
    	if( refVeiculo.getPlaca() != null){
    		
    		caracterPosicao = refVeiculo.getPlaca().substring(4,5);
    		
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
    			
    		//int teste = listaConvert.indexOf(caracterPosicao);
    		//String cht = listaConvert.get(teste);
    		//String chb = Integer.toString(teste); 
    		
    		String digConversao = Integer.toString(listaConvert.indexOf(caracterPosicao));
    		
    		String placaOld, placaOldP1, placaOldP2 = "";
    		
    		// Recebe a placa digitada formato Mercosul
    		StringBuilder myString = new StringBuilder(refVeiculo.getPlaca());
    		// troca o caractere da 4ª posição pelo id da listaConvert baseado na regra de conversão
    		myString.setCharAt(4, digConversao.charAt(0));			
    		
			// Montagem da placa anterior ao formato Mercosul
    		placaOldP1 = myString.substring(0, 3)+"-";
			placaOldP2 = myString.substring(3, 7);
			placaOld = placaOldP1 + placaOldP2;
    		
    		//curingaAlfaNumber = listaConvert.get(Integer.parseInt(caracterPosicao));
    		
    		String b = "a";
    		
    		return placaOld;
    	}
    		
    	
    	
    	return "conversaoNull";
    }
    
    
    /**
     * created at: 2019/02/21
     * @author Francis Bueno
     * 
     */
    
    public void consultaRenavam(){

    	// 
    	//String numRenavam = (String) ((UIOutput) event.getSource()).getValue();
    	
    	if(refVeiculo != null) {
    		if( numRenavamAtualizaPlaca.equals(numRenavamInformaCliente)) {
    			
    			String msg1a = "Placa verificada: " + refVeiculo.getPlaca() + " . Está válida para o veículo cadastrado em nosso sistema com a placa: " + numPlacaAnterior +
    					".  Deseja realizar a conversão para o formato padrão Mercosul?" ;

    			this.setMsg1PanelPlaca(msg1a);
    			this.setMsgPanelPlaca("");
    			this.setMsg2PanelPlaca("");
    			
    			aplicarAtualizaPlaca = true;				// Parece que não esta fazendo atualização VERIFICAR
    			abrirChamadoPlaca = false;
    			requisitarAtendimento = false;
    			
    	    	//atualizaPlaca = false;
    	    	
    	    	RequestContext context = RequestContext.getCurrentInstance();
    			context.update("formVeiculo:idPnAtualizaPlaca");
    			//context.update("formVeiculo:IdPnVeiculos");		
    		
    		} else {
    			
    			String msg1a = "O Renavam informado não corresponde com a nossa base de dados para o veículo de placa: "  + refVeiculo.getPlaca() + "." + 
    							" Você pode nos enviar uma cópia da foto do documento do veículo para o registro de chamado automático no sistema. Para isso clique no botão Chamado";

    			this.setMsg2PanelPlaca(msg1a);
    			this.setMsgPanelPlaca("");
    			this.setMsg1PanelPlaca("");
    			
    			abrirChamadoPlaca = true;
    			
    			aplicarAtualizaPlaca = false;    							// Parece que não esta fazendo atualização VERIFICAR
    	    	
    	    	RequestContext context = RequestContext.getCurrentInstance();
    			context.update("formVeiculo:idPnAtualizaPlaca");   			
    	    	
    		}
    	}
		
    }
    
    /**
     * created at: 2019/02/22
     * @author Francis Bueno
     */
    public void habilitarSuporte(){
    	
    	if(refVeiculo != null) {
    		
    		requisitarAtendimento = true;
    		
	    	RequestContext context = RequestContext.getCurrentInstance();
			context.update("formVeiculo:idPnAtualizaPlaca");   			
    	}
    }
    
    /**
     * created at: 2019/02/22
     * @author Francis Bueno
     */
    public void aplicarAlteracaoPadraoMercosul(){
    	
    	
    	Veiculo veiAtualiza = new Veiculo();
    	
    	veiAtualiza = veiculoService.crud().eq("placa", numPlacaAnterior.toUpperCase()).find();
    	
    	veiAtualiza.setPlaca(refVeiculo.getPlaca());
    	veiAtualiza.setPlacaAnterior(numPlacaAnterior);
    	veiAtualiza.setDtPlacaConversao(new Date());
    	
    	veiculoService.update(veiAtualiza);


    	atualizaPlaca = false;
    	aplicarAtualizaPlaca = false;
    	msgPanelPlaca = "";
    	numRenavamInformaCliente = "";
    	//refVeiculo.setPlaca("");
    	
    	RequestContext context = RequestContext.getCurrentInstance();
    	
    	context.update("formVeiculo:idPnAtualizaPlaca");
    	context.update("formVeiculo:IdPnVeiculos");

    	//RequestContext.getCurrentInstance().reset("formVeiculo");
    	
    	//msgRetorno();
    	
    	findVeiculoPlaca(refVeiculo.getPlaca());
    }
    
    public void teste(){
    	atualizaPlaca = false;
    	aplicarAtualizaPlaca = false;
    	msgPanelPlaca = "";
    	numRenavamInformaCliente = "";
    	//refVeiculo.setPlaca("");
    	
    	RequestContext context = RequestContext.getCurrentInstance();
    	
    	context.update("formVeiculo:idPnAtualizaPlaca");
    	context.update("formVeiculo:IdPnVeiculos");
    	
	findVeiculoPlaca("DTC-9534");

    }
    
    public void msgRetorno(){
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "A placa do veículo foi atualizada para o formato padrão Mercosul! Entre e adicione a placa"));
    	return;
    }
    
    /**
     * created at: 2019/02/22
     * @author Francis Bueno
     */
    public void naoAplicaPadraoMercosul(){
    	
    	atualizaPlaca = false;
    	aplicarAtualizaPlaca = false;
    	abrirChamadoPlaca = false;
    	requisitarAtendimento = false;
    	
    	msgPanelPlaca = "";
    	msg1PanelPlaca = "";
    	msg2PanelPlaca = "";
    	
    	numRenavamInformaCliente = "";
    	//refVeiculo.setPlaca("");
    	
    	RequestContext context = RequestContext.getCurrentInstance();
    	
    	context.update("formVeiculo:idPnAtualizaPlaca");
    	context.update("formVeiculo:IdPnVeiculos");
    	
    	
    }
    
    
    
    /**
     * created at: 2019/02/19
     * @author Francis Bueno
     * @param placa
     * @return
     */
    public String checarTipoDaPlaca(String placa){
    		

        Integer qtdCaracteresPlaca = null;
        String caracterPosicao = "";

        qtdCaracteresPlaca = refVeiculo.getPlaca().length();
        
        if( refVeiculo.getPlaca() != null){

            if( qtdCaracteresPlaca == 7 || qtdCaracteresPlaca == 8  ){ 

                caracterPosicao = refVeiculo.getPlaca().substring(3,4);


                    if( !caracterPosicao.equals("-") ){
                        
                        if ( qtdCaracteresPlaca == 7) { 

                            // localiza se o codigo tem hifem (caso exista não permite a inclusão)
                            // senão encontrar apresenta -1 senão retorna a posição;
                            int findHifen = refVeiculo.getPlaca().indexOf("-");
                            
                            if (findHifen == -1) {
        						//caso a placa não tenha o hifen tipo Mercosul
        						List<Character> numbers = new ArrayList<>();
        						List<Character> letters = new ArrayList<>();
        						
        						for(char ch: refVeiculo.getPlaca().toCharArray()) {
        							
        							if( Character.isDigit(ch)){
        								numbers.add(ch);
        							} else {
        								letters.add(ch);
        							}
        						}
        						
        						//Placa Nacional Tipo Convencional
        						if(letters.size() == 3 && numbers.size() == 4) {
        							return "tipoAnterior";
        						}

        						//Placa Nacional padrão Mercosul
        						if(letters.size() == 4 && numbers.size() == 3) {
        							return "tipoMercosul";
        						}
        						
        					} else {
        						
        						return "erro";	
        					} 

                        }

                    }

            }

        }
    	
    	return "outrosTpPlaca";
    }
    
    /**
     * Method: check if string received contains alphabet or not 
     * 
     * created at: 25/10/2018
     * 
     * @author Francis Bueno
     * @param textoPlaca	receive one slice the code of license plate
     *  
     * @return 'true' when the parameter is all alphabet and 'false' when some character not is one alphabet
     *  
     */
    public Boolean checarStringAlfabeto(String textoPlaca){
    
    	for (int i = 0; i < textoPlaca.length(); i++) {
    	      if (Character.isAlphabetic(textoPlaca.charAt(i))) 
    	         return true;
    	   }
    	   return false;
    }
  
    public void upload(FileUploadEvent event){
    	uploadedFiles.add(event.getFile());
    }
    
    /**
     * @author francis
     */
    public void RequisitarAtendimento2(){
    	String m = "n";
    	String n = m;
    }
    
    /**
     * created at: 2019/02/19 
     * @author francis2577
     */
    public void RequisitarAtendimento(){
    	
    	// Area que pertence o usuario
    	Integer areaUsu  = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();
    	 
    	
    	// Persistir chamado para ter id do diretorio que reserva a imagem
    	Chamado cha = new Chamado();
    		
    		cha.setTipoChamado(new ChamadoTipo(688));    		
    		cha.setArea(new Area(areaUsu));
    		cha.setDescricao("-");
    		cha.setDtAbertura(new Date());
    		cha.setUsuAbertura(checkUsuario.valid());
    		
    		chamadoService.crud().save(cha);
    		
    	// Persistir chamadoResposta 
    	ChamadoResposta chaRes = new ChamadoResposta();
    		
    	chaRes.setDtMensagem(new Date());
    		chaRes.setMensagem(this.getMsgChamadoCliente());
    		chaRes.setChamado(cha);    		
    		chaRes.setUsuInterno(false);
    		chaRes.setUsuario(checkUsuario.valid());
    	
    	chamadoRespostaService.crud().save(chaRes);
    	    	
    	/* Registro do anexo no servidor */
    	// Diretorio de armazenamento das imagens do chamado
    	
    	String dir = AppUtils.dirImagensChamados + cha.getId() + "/"; //"idChamado/idseqchamoid-arquivo" 
    	
    	File files = new File(dir);
    	
    	if( !files.exists()){
    		if(files.mkdirs()){
    			System.out.println("Diretorio criado com sucesso! ( " + dir + " ) ");
    		} else {
    			System.out.println("Não foi possível criar o diretório ! ( " + dir + " ) ");
    		}
    	}
    	
    	// Nome do arquivo que será armazenado
    	String tipo = "CRV";
    	String fileName = "";
    	
    	for ( UploadedFile uploadedFile : uploadedFiles ){
    		
    		fileName = AppUtils.saveImagemChamado(dir, uploadedFile, chaRes.getId(), tipo);
    		
    	}
    	
    	// Persistencia - atualização ChamadoResposta 
    	chaRes.setArquivoEnviado(fileName);
    	
    	chamadoRespostaService.crud().update(chaRes);
    
    	//"9 - Renavam não coincide com placa: Original para conversão Mercosul, placa: xxxxx"
    	
    	String assuntoChamado = "Placa Mercosul | Renavam digitado " + numRenavamInformaCliente + 
    							" não coincide com a placa: " + placaMercosulConvertidaChamado  + 
    							" para a conversão formato Mercosul, placa: " +  refVeiculo.getPlaca() ; 
    	
    	cha.setDescricao(assuntoChamado);
    	chamadoService.crud().update(cha);
    	
    	
    	uploadedFiles.clear();

    	requisitarAtendimento = false;				// disable tela chamado
    	abrirChamadoPlaca = false;					// disable btn chamado
    	numRenavamInformaCliente = "";				// clear input renavam pesquisa
    	msgChamadoCliente = "";						// clear input msg do cliente no chamado
    	placaMercosulConvertidaChamado = "";
    	
    	RequestContext context = RequestContext.getCurrentInstance();
    	context.update("formVeiculo:fragAtualizaPlaca");
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Chamado aberto com sucesso. Aguarde o contato. Nº Atendimento: " + cha.getId()));
    	
    }

    /**    	   
     * Method: find vehicle by code license plate and case found add record at class 'ref Veiculo' and report the informations for  client side requester
     * 
     * created at: not information.
     * 
     * @author Jose Roberto
     * 
     * @param event
     * 
     * 
     * modifications:
     * 
     * @author Francis Bueno
     * 
     * change date: 25/10/2018
     * --restructured algorithm; 
     * --message added about result the searched code license plate; and,
     * --control activation for button save. 
     * 
     */
    public void findVeiculo(AjaxBehaviorEvent event) {
        String placa = (String) ((UIOutput) event.getSource()).getValue();
        findVeiculoPlaca(placa);
    }
    public void findVeiculoPlaca(String placa) {
    	
    	String formatoPlaca = "";
    	
    	numRenavamInformaCliente = ""; 
    	aplicarAtualizaPlaca = false;
    	abrirChamadoPlaca = false;



        if (placa.length() > 0) {

        	Veiculo veiculo = null;
        	
        	String resultadoFormatoPlaca = validaFormatoPlacaNacional();
        	        	
           if ( resultadoFormatoPlaca.equals("valido") && veiculoService.crud().eq("placaAnterior", placa.toUpperCase()).count() == 1 ){
        			// Encontrado apenas uma placa - veiculo trocou a placa p padrao Mercosul
        			String msg = "Consta que a placa " + placa + " está no novo formato padrão Mercosul. " + 
        						 "Para continuar informe a nova placa Mercosul" ;
        			
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", msg ));
        			return;
        	} else 
        		if ( resultadoFormatoPlaca.equals("valido") && veiculoService.crud().eq("placaAnterior", placa.toUpperCase()).count() > 1 ){
        			// Encontrado mais de uma placa
        		
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Placa Duplicada, Erro"));
        			return;
        	}
        	
        	    	
            if (veiculoService.crud().eq("placa", placa.toUpperCase()).count() <= 1) {

    	    	// Se Placa Mercosul não foi encontrada então fazer varedura da placa anterior mercosul no campo placa(vei_placa)
    	    	// Se encontrar 1º solicitar o Renavam para averiguação
    	    	// se for igual ao cadastro então fazer processo de update conversão de placas
    	    	// se nao for igual ao cadastro então não continuar com a inclusão do veículo gerar aviso ao usuario sobre o fato e 
    	    	// permitir que seja possível adicionar um chamado de suporte ao atendimento do cadastro com a upção de enviar arquivo
    	    	// em anexo.  
            		
            	if(veiculoService.crud().eq("placa", placa.toUpperCase()).count() == 0 ){
            		
            		String tipoPlaca = checarTipoDaPlaca(placa);            		
            		
            		if ( tipoPlaca.equals("tipoMercosul")){
            			
            			String placaMercosulConvertida = conversaoPlacaAntesTipoMercosul();
            			
            			// Checar placa anterior para verificar a existência da conversão 
            			if( ! placaMercosulConvertida.equals("conversaoNull") ) {
            				
            				if (veiculoService.crud().eq("placa", placaMercosulConvertida.toUpperCase()).count() == 1) {
            					
            					placaMercosulConvertidaChamado = placaMercosulConvertida; 		// var para uso do chamado.
            					
//          						 " Porém o cadastro deste veículo em nosso sistema não foi atualizado. Placa: " + placaMercosulConvertida + "\r\n" +            					
                    			String msg1 = " Esta placa " + placa + " ,está no formato padrão Mercosul e ainda  não foi atualizada em nosso sistema!" + "\n" +
                    				 "  Por gentileza nos informe o número do Renavam deste veículo para verificação. " ;
                    			
                    			msgResultPesquisaPlaca = "<<< Digite uma outra placa para uma nova inclusão.";
                    			
                    			Veiculo vei = new Veiculo();
                    			
                    			vei = veiculoService.crud().eq("placa", placaMercosulConvertida.toUpperCase()).find();
                    			
                    			numRenavamAtualizaPlaca = vei.getRenavam();
                    			numPlacaAnterior = placaMercosulConvertida;
                    			
                    			this.setAtualizaPlaca(true);

                    			RequestContext context = RequestContext.getCurrentInstance();
                    			context.update("formVeiculo:idPnAtualizaPlaca");
                    			context.update("formVeiculo:IdPnVeiculos");
                    			
                    			this.setMsgPanelPlaca(msg1);
                    			msg1PanelPlaca="";
                    			msg2PanelPlaca="";
            					
//                    			String msg = "Está placa " + placa + " está formato padrão Mercosul. " + 
//               						 " Porém o cadastro deste veículo em nosso sistema não foi atualizado. Placa: " + placaMercosulConvertida + " Por gentileza nos informe o número do  Renavam para realizarmos a atualização! " ;
//               			
//                    			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", msg ));
                    			return;
                    			
            				} else {
            					
            					this.setAtualizaPlaca(false);

                    			RequestContext context = RequestContext.getCurrentInstance();
                    			context.update("formVeiculo:idPnAtualizaPlaca");
                    			context.update("formVeiculo:IdPnVeiculos");
            					
            				}
            				
            			}
            			//Conversão da placa 
            			
            		}
            		
            	}
            	
            	if(veiculoNacional) {
            		
            		//Valida o formato da placa para o veiculo tipo Nacional            		
            	    String resultFormatoPlaca = validaFormatoPlacaNacional();
            	    
            	    if (resultFormatoPlaca.equals("valido")){
            	    
            	    	veiculo = veiculoService.crud().eq("placa", placa.toUpperCase()).find();
            	    	
            	    	formatoPlaca = "valido";
            	    	
            	    } else {
            	    	
            	    	formatoPlaca = "invalido";
            	    	
            	    }            		
            		
            	} else {
            		
            		//Veiculo Estrangeiro
            		veiculo = veiculoService.crud().eq("placa", placa.toUpperCase()).find();
            		formatoPlaca = "valido";
            	}
                
            } else {
            	
                veiculo = veiculoService.crud().eq("placa", placa.toUpperCase()).list().get(0);
            }
            
            
            
            if (veiculo != null) {
            	
            	this.setPlacaEncontrada(true);
            	
            	this.setMsgResultPesquisaPlaca("Encontrado registro para a Placa : " + refVeiculo.getPlaca() + " , edição de dados aberta para uso. " ) ;           	
            	
                // veiculoNacional = ValidaBrasil.validateRENAVAM(veiculo.getRenavam());
            	if(veiculo.isNacional() == true) {
            		
            		this.setBtnSalvarVeiculo(true);            		
            		veiculoNacional = veiculo.isNacional();
            	    refVeiculo.setRenavam("00000000000".substring(veiculo.getRenavam().length()) + veiculo.getRenavam());
                    refVeiculo.setNacional(veiculoNacional);
                    
            	} else {
            		refVeiculo.setRenavam(veiculo.getRenavam());
            		refVeiculo.setNacional(false);
            		veiculoNacional = false;
            		this.setBtnSalvarVeiculo(true);
            	}
                               
                refVeiculo.setMarca(veiculo.getMarca().getId());
                refVeiculo.setModelo(veiculo.getModelo().getId());
                refVeiculo.setCor(veiculo.getCor().getId());
                refVeiculo.setProprietario(getProprietario(veiculo.getProprietario()));
                refVeiculo.setTipo(veiculo.getTipo().getId());
                refVeiculo.setChassi(veiculo.getChassi());
                              
                refVeiculo.setMunicipio(veiculo.getMunicipio());
                refVeiculo.setUf(veiculo.getUf());
                refVeiculo.setDocumento(veiculo.getDocumento());
                refVeiculo.setIdVeiculo(veiculo.getId());

                lstModelo = new ArrayList<Modelo>();
                lstModelo = modeloService.crud().criteria().eq("marca.id", veiculo.getMarca().getId())
                        .list();
                lstVeiculoCategoria = new ArrayList<>();
                lstVeiculoCategoria = veiCatService.crud().eq("tipo", (veiculo.getTipo().getId() == 59 ? "C" : "R")).isNull("exclusao").list();

                Collections.sort(lstVeiculoCategoria, new Comparator<VeiculoCategoria>() {
                    @Override
                    public int compare(VeiculoCategoria catVeic1, VeiculoCategoria catVeic2) {
                        return catVeic1.getDescricao().compareTo(catVeic2.getDescricao());
                    }
                });

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formVeiculo:veiModelo");
                context.update("formVeiculo:veiCategoria");
            	context.update("formVeiculo:btnSalvar");

                return;
                
            } else {
            	
            	if ( formatoPlaca.equals("valido") ) {

            		
                	this.setPlacaEncontrada(false);   	
                	this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + " não encontrada , continue com a inclusão ou realize uma nova pesquisa.");
                	
                	this.setBtnSalvarVeiculo(true);
                	
                    refVeiculo = new ReferenciaVeiculos();
                    refVeiculo.setPlaca(placa.toUpperCase());
                    refVeiculo.setNacional(true);
                    refVeiculo.setProprietario(new br.com.global5.manager.model.auxiliar.Proprietario());
                    refVeiculo.getProprietario().setEndereco(new Endereco());
                    refVeiculo.getProprietario().setDocumento("");
                    
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.update("formVeiculo:veiModelo");
                    context.update("formVeiculo:veiCategoria");
                	context.update("formVeiculo:btnSalvar");

                    
            	} else 
            		
            		if ( formatoPlaca.equals("invalido")){
            			
            			this.setMsgResultPesquisaPlaca("Placa : " + refVeiculo.getPlaca() + ". Não permitida a inclusão com formato inválido para o tipo nacional. Permitido, Ex.: ABC-123 , ABC-1234 ou ABC1D23(Padrão Mercosul). Redigite o numero da placa.");
            	        this.setBtnSalvarVeiculo(false);
            	        
                        refVeiculo = new ReferenciaVeiculos();
                        refVeiculo.setPlaca("");
                        refVeiculo.setNacional(true);
                        refVeiculo.setProprietario(new br.com.global5.manager.model.auxiliar.Proprietario());
                        refVeiculo.getProprietario().setEndereco(new Endereco());
                        refVeiculo.getProprietario().setDocumento("");                        

            	}
            	
            }
            
        } else {
        
        this.setMsgResultPesquisaPlaca("Informe uma placa com formato válido para prosseguir! Ex.: ABC-123 , ABC-1234 ou ABC1D23 (Mercosul). Veículo Estrangeiro formato livre. ");
        
        setBtnSalvarVeiculo(false);
        
        }

    }

 
    public Endereco getEndereco(Localizador end) {

        Endereco endereco = new Endereco();

        endereco.setId(end.getId());
        endereco.setTipoEndereco(end.getTipoEndereco().getId());
        endereco.setLogradouro(end.getLogradouro());
        endereco.setNumero(end.getNumero());
        endereco.setComplemento(end.getComplemento());
        endereco.setBairro(end.getBairro());
        endereco.setCidade(end.getCidade());
        endereco.setUf(end.getUf());
        endereco.setPais(end.getPais());
        endereco.setCep(end.getCep());

        return endereco;
    }

    public Localizador setEndereco(Endereco end) {

        Localizador endereco = new Localizador();

        endereco.setId(end.getId());
        endereco.setTipoEndereco(new TipoEndereco(end.getTipoEndereco()));
        endereco.setLogradouro(end.getLogradouro());
        endereco.setNumero(end.getNumero());
        endereco.setComplemento(end.getComplemento());
        endereco.setBairro(end.getBairro());
        endereco.setCidade(end.getCidade());
        endereco.setUf(end.getUf());
        endereco.setPais(end.getPais());
        endereco.setCep(end.getCep());

        return endereco;

    }


    private br.com.global5.manager.model.auxiliar.Proprietario getProprietario(Proprietario proprietario) {

        br.com.global5.manager.model.auxiliar.Proprietario prop = new br.com.global5.manager.model.auxiliar.Proprietario();

        prop.setId(proprietario.getId());
        prop.setDocumento(proprietario.getDocumento());
        prop.setEndereco(getEndereco(proprietario.getEndereco()));
        prop.setNome(proprietario.getNome().toUpperCase());
        prop.setNacional(proprietario.isNacional());

        prop.setUsuCriacao(proprietario.getUsuCriacao().getId());
        prop.setDtCriacao(proprietario.getDtCriacao());

        if (proprietario.getUsuAlteracao() == null) {
            prop.setUsuAlteracao(proprietario.getUsuAlteracao().getId());
            prop.setDtAlteracao(proprietario.getDtAlteracao());
        }

        if (proprietario.getUsuExclusao() != null) {
            prop.setUsuExclusao(proprietario.getUsuExclusao().getId());
            prop.setDtExclusao(proprietario.getDtExclusao());
        }


        return prop;
    }

    public boolean validaCampos() {

        if( fichaCliente.getCpf() == null ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("onTopScreen();");
            return false;
        }

        try {

            AppUtils.checkValues(fichaCliente);

        } catch (Exception e) {}

        boolean allok = true;

        if (painelMotorista) {

            if (fichaCliente.isNacional()) {
                if (fichaCliente.getCpf().length() < 10 || ! ValidaBrasil.validateCPF(fichaCliente.getCpf())) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "O CPF informado não é válido, verifique o digite novamente!",
                                    ""));

                    allok = false;
                }

                if( fichaCliente.getNome() == null ) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Não foi informado o nome do motorista, verifique o digite novamente!",
                                    ""));

                    allok = false;

                }

                if( fichaCliente.getDdd() == null || fichaCliente.getTelefone() == null ) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Não foi informado um telefone válido, verifique o digite novamente!",
                                    ""));

                    allok = false;

                }

                if( fichaCliente.getTempo() == null || fichaCliente.getTempoTipo() == null ) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Não foi informado o tempo de trabalho com o motorista, verifique o digite novamente!",
                                    ""));

                    allok = false;

                }



            }



            if( showCNH ) {
                if (fichaCliente.getDtPrimeiraEmissaoCNH() == null) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar data de primeira emissão da CNH!",
                                    ""));
                    allok = false;

                }
                if (fichaCliente.getDtEmissaoCNH() == null) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar data de emissão da CNH!",
                                    ""));
                    allok = false;
                }

                if (fichaCliente.getCnh() == null) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar número da CNH!",
                                    ""));
                    allok = false;
                }

                if (fichaCliente.getDtValidadeCNH() == null) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar data validade da CNH!",
                                    ""));

                    allok = false;
                }

                if (fichaCliente.getCategoria() == 0) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar a categoria da CNH!",
                                    ""));
                    allok = false;
                }

                if (fichaCliente.getNumSegCnh().length() == 0) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Aviso",
                                    "Fata informar o número de segurança!"));
                    allok = false;
                }

                if (fichaCliente.getRegistroCNH().length() == 0) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Falta informar número de registro da CNH!",
                                    ""));
                    allok = false;
                }
            }

            if (tipoMotorista == 0) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Falta informar qual o tipo do motorista/ajudante!",
                                ""));
                allok = false;
            }



        }


        if( ! allok  ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("onTopScreen();");
        }

        return allok;


    }


    public boolean validaReferenciaPessoal() {

        boolean allok = true;

        if (showRefPessoal) {
            if (referenciasPessoais.size() >= 1) {
                fichaCliente.setReferenciaPessoal(referenciasPessoais);
                fichaCliente.setRefPessoalCSV(CDL.toString(new JSONArray(new Gson().toJson(referenciasPessoais))).replace(":", ";"));
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "As Referências Pessoais são obrigatórias para a ficha do motorista. ",
                                ""));
                allok = false;
            }
        }

        return allok;
    }

    public boolean validaReferenciaComercial() {

        boolean allok = true;

        if (showRefComercial) {
            if (referenciasComerciais.size() >= 1) {
                fichaCliente.setReferenciaComercial(referenciasComerciais);
                fichaCliente.setRefComercialCSV(CDL.toString(new JSONArray(new Gson().toJson(referenciasComerciais))).replace(":", ";"));
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "As Referências Comerciais são obrigatórias para a ficha do motorista.",
                                ""));
                allok = false;
            }
        }

        return allok;
    }

    public boolean validaReferenciaVeiculos() {

        boolean allok = true;

        if (painelVeiculo) {

            if( referenciasVeiculos.size() > 0 ) {
                int cavalo = 0;
                HashMap<String, Integer> hmap = new HashMap<>();
                for (ReferenciaVeiculos refVeiculos : referenciasVeiculos) {
                    if (hmap.get(refVeiculos.getPlaca()) == null) {
                        hmap.put(refVeiculos.getPlaca(), refVeiculos.getTipo());
                    } else {
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Veículo Duplicado! Verifique a placa : "
                                                + refVeiculos.getPlaca(), ""));
                        allok = false;
                    }

                    if ((refVeiculos.getRenavam().length() == 0 ||
                            !ValidaBrasil.validateRENAVAM(refVeiculos.getRenavam())) &&
                            veiculoNacional) {
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "RENAVAM do veículo " + refVeiculos.getPlaca() + " não pode ser "
                                                + " validado confira e digite novamente!", "Erro de lançamento!"));
                        allok = false;
                    }
                    if (refVeiculos.getTipo() == 59) {
                        cavalo++;
                        if (cavalo > 1) {
                            FacesContext.getCurrentInstance().addMessage(
                                    null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                            "Foi encontrado mais de um Cavalo na sessão de veículos!",
                                            ""));
                            allok = false;
                        }
                    }

                }

                if( painelMotorista ) {
                    if( tipoMotorista == 93 && cavalo < 1) {
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "A Ficha de Motoristas (Terceiros) deve conter 1 veículo (CAVALO) " +
                                                "confira e digite novamente!", "Erro de lançamento!"));
                        allok = false;

                    }
                }

                StringBuilder str = new StringBuilder();
                str.append(referenciasVeiculos.get(0).toCSVHeader() + "\n");

                for (ReferenciaVeiculos refVeiculos : referenciasVeiculos) {
                    str.append(refVeiculos.toCSVString() + "\n");
                }

                fichaCliente.setVeiculosCSV(str.toString());
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Nesta ficha é obrigatório pelo menos 1 veículo, confira digite novamente!", ""));
                allok = false;
            }

        }
        return allok;
    }


    public void salvarFichaNew() {

        try {

            if( painelMotorista ) {
                if (!validaCampos()) {
                    return;
                }

                if( ! validaReferenciaPessoal() ) {
                    return;
                }

                if( ! validaReferenciaComercial() ) {
                    return;
                }

                fichaCliente.setTipoMotorista(motVinculoService.crud().get(tipoMotorista));
            }

            if( painelVeiculo ) {
                if( !validaReferenciaVeiculos() ) {
                    return;
                }
            }

            if (paisOrigem > 0) {

                fichaCliente.setMercadoria(new Mercadoria(idMercadoria));

                fichaCliente.setMercPaisOrigem(paisOrigem);
                fichaCliente.setMercUFOrigem(ufOrigem);
                fichaCliente.setMercCidadeOrigem(cidOrigem);

                fichaCliente.setMercPaisDestino(paisDestino);
                fichaCliente.setMercUFDestino(ufDestino);
                fichaCliente.setMercCidadeDestino(cidDestino);
            }

            if (areaFilial != null) {
                fichaCliente.setTransportadora(new Area(areaFilial.getId()));
            }

            if (centroCusto.length() > 0) {
                fichaCliente.setCentroCusto(centroCusto);
            }

            fichaCliente.setPais(new Pais(pais));
            fichaCliente.setStatus(1);
            fichaCliente.setUsuarioInclusao(new Usuario(usuario.getId()));
            fichaClienteCrud.save(fichaCliente);
            fichaClienteCrud.commit();

            StoredProcedureQuery query = fichaClienteCrud.getEntityManager().createNamedStoredProcedureQuery("ficha_cliente_prepare");
            query.setParameter("ficoid", fichaCliente.getId());
            query.execute();
            
            List<Object[]> rows = (List<Object[]>) query.getResultList();
            
            
            int idRetorno = fichaCliente.getId();  //(int) query.getOutputParameterValue("ficoid");
            

            try {
                List retorno  = fichaService.crud().criteria().rawQuery("update ficha_cliente set fic_status=99 where ficoid=" + fichaCliente.getId() +  " returning (fic_anac_retorno ->> 'ficha') as anacoid; " );
                                
                idCadastral = Integer.valueOf(retorno.get(0).toString());
                RequestContext.getCurrentInstance().execute("PF('dlgFechado').show();");
            } catch(Exception erro) {
                if( erro.getMessage().contains("could not extract ResultSet")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgFechado').show();");
                    return;
                } else {
                    erro.printStackTrace();
                    RequestContext.getCurrentInstance().showMessageInDialog(
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso!",
                                    "O sistema não conseguiu gravar os dados da ficha. " +
                                            "Informe o suporte técnico"));
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ficha : " + fichaCliente.getId() + ", informe o suporte técnico.",
                            "Para enviar essa ficha novamente, aguarde instruções do suporte!"));
        }
        
        
        // adicionado para inclusão de foto do motorista
        if(painelMotorista == true){
        	try{
        		
        		//mot_url_foto
        		                                		
        		FichaCliente fichaFindMotorista = fichaService.findById(fichaCliente.getId());
        		int idMotorista = fichaFindMotorista.getMotorista().getId(); 
        		
        		if(imgFile != null){

        			saveImageMotorista(imgFile, idMotorista, "F");

        			String urlFotoN = "'"+this.getUrlFotoMot()+"'";
        			
        			this.setStsImgFile("Foto: " + urlFotoN);
        			
        			String sql = "update java.motorista set mot_url_foto =" + urlFotoN +  " where motoid =  " + idMotorista;
        			
        			EntityManagerFactory emf = Persistence.createEntityManagerFactory("localhost");
        			EntityManager entityManager = emf.createEntityManager();
        			
        			 if (null != entityManager) {
        				 
        				 EntityTransaction updateTransaction = entityManager.getTransaction();
        				 updateTransaction.begin();
        				 Query qryM = entityManager.createNativeQuery(sql);
        				 int updateMotorista = qryM.executeUpdate();
        				 updateTransaction.commit();
        				 
        			 }
        			 
        			 entityManager.close();
        			 emf.close();
        			     			
        		}
        		
        		
        	} catch (Exception e) {}
        }
        
        if ((showCNH != false) && (changeImgFileCnhFrente || changeImgFileCnhVerso != false )) {
            
        	try{
        		
        		FichaCliente fichaFindMotorista = fichaService.findById(fichaCliente.getId());
        		int idMotorista = fichaFindMotorista.getMotorista().getId(); 
        		int idMotoristaCNH = fichaFindMotorista.getIdMotoristaCnh();
        		
        		if(idMotorista > 0){        			
        			
        			saveImageCNH(imgCnhFrenteFile, idMotoristaCNH, "CF");
        			saveImageCNH(imgCnhVersoFile,  idMotoristaCNH, "CV");
        			
//        			String extesionCnhFrente = FilenameUtils.getExtension(imgCnhFrenteFile.getFileName());
//        			String extesionCnhVerso  = FilenameUtils.getExtension(imgCnhVersoFile.getFileName());
//        			
//        			boolean cnhFrentePdf, cnhVersoPdf  = false;
//        			
//        			if(extesionCnhFrente.equals("PDF") || extesionCnhFrente.equals("pdf")){
//        				cnhFrentePdf = true;
//        			}
//        			if(extesionCnhVerso.equals("PDF") || extesionCnhVerso.equals("pdf")){
//        				cnhVersoPdf = true;
//        			}
        			
        			String urlCnhF = "'"+this.getUrlFotoCnhF()+"'";
        			String urlCnhV = "'"+this.getUrlFotoCnhV()+"'";
        		
        			String sql = "update java.motorista_cnh set mcnh_urlcnhfrente =" + urlCnhF + 
        						 " ,mcnh_urlcnhverso = " + urlCnhV + " where mcnhoid = " + idMotoristaCNH + 
        						 " and mcnh_motoid = " + idMotorista ;
        			
        			EntityManagerFactory emfC = Persistence.createEntityManagerFactory("localhost");
        			EntityManager entityManagerC = emfC.createEntityManager();
        			
        			 if (null != entityManagerC) {
        				 
        				 EntityTransaction updateTransaction = entityManagerC.getTransaction();
        				 updateTransaction.begin();
        				 Query qryM = entityManagerC.createNativeQuery(sql);
        				 int updateMotorista = qryM.executeUpdate();
        				 updateTransaction.commit();
        				 
        			 }
        			 
        			 entityManagerC.close();
        			 emfC.close();
        			     			
        		}
        		
        		
        	} catch (Exception e) {}
            
        }
        

    }
    
    public void verTeste(){
    	this.setChangeImgFile(true);
    	boolean a = isChangeImgFile();
    	boolean b = a;
    }
    
    public void uploadFoto(FileUploadEvent event){
    	
    	this.setImgFile(event.getFile());    	
    	this.setChangeImgFile(true);
        if (this.getImgFile() != null) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Sua foto será atualizado após o envio da ficha. Imagem enviada: " + this.getImgFile().getFileName()));
        }
    }
    
    public void uploadFotoCNHFrente(FileUploadEvent event){
    	this.setImgCnhFrenteFile(event.getFile());
    	this.setChangeImgFileCnhFrente(true);     
    	FacesMessage message = new FacesMessage("Aviso", "CNH - lado Frente foi enviado com sucesso. Arquivo: " + event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void uploadFotoCNHVerso(FileUploadEvent event){    
    	this.setImgCnhVersoFile(event.getFile());
    	this.setChangeImgFileCnhVerso(true);
    	FacesMessage message = new FacesMessage("Aviso", "CNH - lado Verso foi enviado com sucesso. Arquivo: " + event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
      
    }
    
  
    private void saveImageCNH(UploadedFile upload, int id, String tipo) {
        if (upload == null) {
            return;
        }
        String extesion = FilenameUtils.getExtension(upload.getFileName());
        try {
            InputStream is = upload.getInputstream();
            byte[] bytes = IOUtils.toByteArray(is);
            FileOutputStream fileOuputStream = new FileOutputStream(AppUtils.dirImagens + tipo + id + "." + extesion);
            fileOuputStream.write(bytes);
            fileOuputStream.close();
            
            if(tipo.equals("CF")){
            	this.setUrlFotoCnhF(tipo + id + "." + extesion);
            } else if(tipo.equals("CV")){
            	this.setUrlFotoCnhV(tipo + id + "." + extesion);
            }
            

        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Motorista " + id
                            + " não terá sua imagem da CNH Verso atualizada por um erro não tratado.", null));

        }
    }

    
    private void saveImageMotorista(UploadedFile upload, int id, String tipo) {
        if (upload == null) {
            return;
        }
        String extesion = FilenameUtils.getExtension(upload.getFileName());
        try {
            InputStream is = upload.getInputstream();
            byte[] bytes = IOUtils.toByteArray(is);
            FileOutputStream fileOuputStream = new FileOutputStream(AppUtils.dirImagens + tipo + id + "." + extesion);
            fileOuputStream.write(bytes);
            fileOuputStream.close();

            this.setUrlFotoMot(tipo + id + "." + extesion);

        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Motorista " + id
                            + " não terá sua imagem atualizada por um erro não tratado.", null));

        }
    }

    
    public void btnBack() {
        try {
            init();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../../index.xhtml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public void cleanMotorista(AjaxBehaviorEvent event) {
//        System.out.println("> > > > > > > > >  CLEAN MOTORISTA  :: ");
//    }

    public void fichaMotorista() {
        if (areaFilial == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Dados Incompletos", "Para inserir fichas, a Filial é obrigatória!"));
            return;
        }

        painelMotorista = true;
        painelVeiculo = false;
        try {

            FacesContext.getCurrentInstance().getExternalContext().redirect("enviarFicha.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inserir nova ficha de motorista "
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }

    }

    public void fichaVeiculo() {
        if (areaFilial == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Dados Incompletos", "Para inserir fichas, a Filial é obrigatória!"));
            return;
        }

        painelMotorista = false;
        painelVeiculo = true;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("enviarFicha.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inserir nova ficha de veículo "
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("form0:panelVeiculos");
    }

    public void fichaCompleta() {

        if (areaFilial == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Dados Incompletos", "Para inserir fichas, a Filial é obrigatória!"));
            return;
        }

        painelMotorista = true;
        painelVeiculo = true;
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("enviarFicha.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inserir nova ficha completa "
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form0");
    }



    public void checkRenavam(AjaxBehaviorEvent event) {
        
    	String renavam =  (String) ((UIOutput)event.getSource()).getValue();


        if( ! veiculoNacional )
            return;

        if( renavam.length() == 0  || ! ValidaBrasil.validateRENAVAM(renavam))  {

        	renavamDuplicado = false;
        	
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esse RENAVAM não pode ser "
                            + " validado confira e digite novamente!", "Erro"));
            return;
            
        } else 
        	
        	if(renavam.length() == 0  || ValidaBrasil.validateRENAVAM(renavam)){
        		// RENAVAM - não aceita duplicidade do renavam para novos veiculos
        		if ( placaEncontrada == false) {
        			
                	Criteria criteria = veiculoService.crud().getSession().createCriteria(Veiculo.class);
                	
                	criteria.add(Restrictions.eq("renavam", renavam));
                	
                	int result = criteria.list().size();
                	
                	if( result >= 1){
                		renavamDuplicado = true;
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", "RENAVAM informado pertence a outro veículo. Por gentileza confira e digite novamente."));
                        return;	
        		}
                	
        	}
        	
        }

    }

    public void BuscaCepProprietario(AjaxBehaviorEvent event) {

        String cep = (String) ((UIOutput) event.getSource()).getValue();

        if (cep.length() == 0) {
            foundUF = false;
            foundBairro = false;
            foundCidade = false;
            foundEndereco = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formVeiculo:cepUFProprietario");
            context.update("formVeiculo:cepCidadeProprietario");
            context.update("formVeiculo:cepBairroProprietario");
            context.update("formVeiculo:cepEndProprietario");
            return;
        }

        Logradouro loc;
        if (logrService.crud().eq("cep", cep.replace("-", "")).count() <= 1) {
            loc = logrService.crud().eq("cep", cep.replace("-", "")).find();
        } else {
            loc = logrService.crud().eq("cep", cep.replace("-", "")).list().get(0);
        }

        if (loc == null) {
            ViaCEPClient client = new ViaCEPClient();
            try {
                endereco = client.getEndereco(cep.trim());
                if (endereco != null) {
                    Localizador localizador = new Localizador();
                    localizador.setCep(endereco.getCep().trim());
                    localizador.setUf(endereco.getUf().toUpperCase());
                    localizador.setCidade(endereco.getLocalidade().toUpperCase());
                    localizador.setBairro(endereco.getBairro().toUpperCase());
                    localizador.setLogradouro(endereco.getLogradouro().toUpperCase());
                    localizador.setTipoEndereco(new TipoEndereco(185));

                    locService.crud().saveOrUpdate(localizador);


                    br.com.global5.manager.model.auxiliar.Proprietario prop = refVeiculo.getProprietario();

                    prop.setEndereco(getEndereco(localizador));
                }
            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "O Cep informado não pode ser  "
                                + "validado, confira e tente novamente."));
            }

        } else {
            try {
                Endereco end = new Endereco();
                br.com.global5.manager.model.auxiliar.Proprietario prop = new br.com.global5.manager.model.auxiliar.Proprietario();


                end.setCep(loc.getCep() == null ? "00000-000" : loc.getCep());
                end.setUf(loc.getCidade().getUf().getSigla().toUpperCase());
                end.setCidade(loc.getCidade().getNome().toUpperCase());
                end.setBairro(loc.getIniBairro().getNome().toUpperCase());
                end.setLogradouro(loc.getNome().toUpperCase());

                foundCidade = loc.getCidade().getNome().length() != 0;
                foundUF = loc.getCidade().getUf().getNome().length() != 0;
                foundBairro = loc.getIniBairro().getNome().length() != 0;

                foundEndereco = loc.getNome().length() != 0;

                if (refVeiculo.getProprietario() == null) {
                    prop = new br.com.global5.manager.model.auxiliar.Proprietario();
                }

                prop.setEndereco(end);

                refVeiculo.setProprietario(prop);

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formVeiculo:cepUFProprietario");
                context.update("formVeiculo:cepCidadeProprietario");
                context.update("formVeiculo:cepBairroProprietario");
                context.update("formVeiculo:cepEndProprietario");
            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "O Cep informado não pode ser  "
                                + "validado, confira e tente novamente."));
            }

        }


    }

    public void BuscaCep(AjaxBehaviorEvent event) {

        String cep = (String) ((UIOutput) event.getSource()).getValue();

        if (cep.length() == 0) {
            foundUF = false;
            foundBairro = false;
            foundCidade = false;
            foundEndereco = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:cepUFMotorista");
            context.update("form0:cepCidadeMotorista");
            context.update("form0:cepBairroMotorista");
            context.update("form0:cepEndMotorista");
            return;
        }

        Logradouro loc;
        if (logrService.crud().eq("cep", cep.replace("-", "")).count() <= 1) {
            loc = logrService.crud().eq("cep", cep.replace("-", "")).find();
        } else {
            loc = logrService.crud().eq("cep", cep.replace("-", "")).list().get(0);
        }
        if (loc == null) {
            ViaCEPClient client = new ViaCEPClient();
            try {
                endereco = client.getEndereco(cep);
                if (endereco != null) {
                    fichaCliente.setCidade(endereco.getLocalidade().toUpperCase());
                    fichaCliente.setBairro(endereco.getBairro().toUpperCase());
                    fichaCliente.setEndereco(endereco.getLogradouro().toUpperCase());
                    fichaCliente.setUf(endereco.getUf().toUpperCase());
                } else {
                    throw new Exception("O Cep informado não é válido");
                }
            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "O Cep informado não pode ser  "
                                + "validado, confira e tente novamente."));
            }

        } else {

            fichaCliente.setCidade(loc.getCidade().getNome().toUpperCase());
            fichaCliente.setBairro(loc.getIniBairro().getNome().toUpperCase());
            fichaCliente.setEndereco(loc.getNome().toUpperCase());
            fichaCliente.setUf(loc.getCidade().getUf().getSigla().toUpperCase());
        }
        try {
            foundEndereco = (fichaCliente.getEndereco().length() != 0);
            foundBairro = !fichaCliente.getBairro().equals("NAO IDENTIFICADO") && (fichaCliente.getBairro().length() != 0);
            foundCidade = (fichaCliente.getCidade().length() != 0);
            foundUF = (fichaCliente.getUf().length() != 0);
        } catch (Exception e) {
            e.printStackTrace();

        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form0:cepUFMotorista");
        context.update("form0:cepCidadeMotorista");
        context.update("form0:cepBairroMotorista");
        context.update("form0:cepEndMotorista");

    }

//    public void Evento() {
//        System.out.println("======>>>>>>>>>Uploaded File Name Is :: "+file.getFileName()+" :: Uploaded File Size :: "+file.getSize());
//    }

    public void escolherUFOrigem(AjaxBehaviorEvent event) {

        try {
            Integer idPais = (int) ((UIOutput) event.getSource()).getValue();
            lstUFOrigem = ufService.crud().eq("pais.id", idPais).list();
            Collections.sort(lstUFOrigem, new Comparator<UF>() {
                @Override
                public int compare(UF c1, UF c2) {
                    return c1.getSigla().compareTo(c2.getSigla());
                }
            });
            habilitaUF = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:UFOrigem");
        } catch (Exception e) {
            habilitaUF = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:UFOrigem");
        }

    }

    public void escolherUFDestino(AjaxBehaviorEvent event) {

        try {
            Integer idPais = (int) ((UIOutput) event.getSource()).getValue();
            lstUFDestino = ufService.crud().eq("pais.id", idPais).list();
            Collections.sort(lstUFDestino, new Comparator<UF>() {
                @Override
                public int compare(UF c1, UF c2) {
                    return c1.getSigla().compareTo(c2.getSigla());
                }
            });
            habilitaUFDestino = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:UFDestino");
        } catch (Exception e) {
            habilitaUFDestino = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:UFDestino");
        }

    }

    public void escolherCidadeOrigem(AjaxBehaviorEvent event) {

        try {
            Integer idUF = (int) ((UIOutput) event.getSource()).getValue();
            lstCidadeOrigem = cidService.crud().eq("uf.id", idUF).list();

            Collections.sort(lstCidadeOrigem, new Comparator<Cidade>() {
                @Override
                public int compare(Cidade c1, Cidade c2) {
                    return c1.getNome().compareTo(c2.getNome());
                }
            });
            habilitaCidade = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:cidOrigem");
        } catch (Exception e) {
            habilitaCidade = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:cidOrigem");
        }

    }

    public void escolherCidadeDestino(AjaxBehaviorEvent event) {

        try {
            Integer idUF = (int) ((UIOutput) event.getSource()).getValue();

            lstCidadeDestino = cidService.crud().eq("uf.id", idUF).list();

            Collections.sort(lstCidadeDestino, new Comparator<Cidade>() {
                @Override
                public int compare(Cidade c1, Cidade c2) {
                    return c1.getNome().compareTo(c2.getNome());
                }
            });

            habilitaCidadeDestino = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:cidDestino");

        } catch (Exception e) {
            habilitaCidadeDestino = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form0:cidDestino");
        }

    }

    public ViaCEPEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(ViaCEPEndereco endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<ReferenciasPessoais> getReferenciasPessoais() {
        return referenciasPessoais;
    }

    public void setReferenciasPessoais(List<ReferenciasPessoais> referenciasPessoais) {
        this.referenciasPessoais = referenciasPessoais;
    }

    public ReferenciasPessoais getRefPessoal() {
        return refPessoal;
    }

    public void setRefPessoal(ReferenciasPessoais refPessoal) {
        this.refPessoal = refPessoal;
    }


    public List<ReferenciasComerciais> getReferenciasComerciais() {
        return referenciasComerciais;
    }

    public void setReferenciasComerciais(List<ReferenciasComerciais> referenciasComerciais) {
        this.referenciasComerciais = referenciasComerciais;
    }

    public ReferenciasComerciais getRefComercial() {
        return refComercial;
    }

    public void setRefComercial(ReferenciasComerciais refComercial) {
        this.refComercial = refComercial;
    }

    public List<ReferenciaVeiculos> getReferenciasVeiculos() {
        return referenciasVeiculos;
    }

    public void setReferenciasVeiculos(List<ReferenciaVeiculos> referenciasVeiculos) {
        this.referenciasVeiculos = referenciasVeiculos;
    }

    public ReferenciaVeiculos getRefVeiculo() {
        return refVeiculo;
    }

    public void setRefVeiculo(ReferenciaVeiculos refVeiculo) {
        this.refVeiculo = refVeiculo;
    }

    public FichaCliente getFichaCliente() {
        return fichaCliente;
    }

    public void setFichaCliente(FichaCliente fichaCliente) {
        this.fichaCliente = fichaCliente;
    }

    public List<MotoristaVinculo> getLstTipoMotorista() {
    	

        
        if (fichaCliente.isNacional()) {
      
        	  lstTipoMotorista = new ArrayList<MotoristaVinculo>();
          	
              //lstTipoMotorista = motVinculoService.crud().criteria().between("id", 92, 98).list();
        	  lstTipoMotorista = motVinculoService.crud().criteria().isNull("exclusao").between("id", 92, 98).list() ;
              
        	return lstTipoMotorista;

        } else {        	
        	
            lstTipoMotorista = new ArrayList<MotoristaVinculo>();
            	            
            lstTipoMotorista = motVinculoService.crud().criteria().isNull("exclusao").between("id", 92, 96).ne("id",94).ne("id",95).list(); 
         
            return lstTipoMotorista;
            
            
        }
    	

    }

    public void setLstTipoMotorista(List<MotoristaVinculo> lstTipoMotorista) {
        this.lstTipoMotorista = lstTipoMotorista;
    }

    public boolean isFoundEndereco() {
        return foundEndereco;
    }

    public void setFoundEndereco(boolean foundEndereco) {
        this.foundEndereco = foundEndereco;
    }

    public boolean isPainelMotorista() {
        return painelMotorista;
    }

    public void setPainelMotorista(boolean painelMotorista) {
        this.painelMotorista = painelMotorista;
    }

    public boolean isPainelVeiculo() {
        return painelVeiculo;
    }

    public void setPainelVeiculo(boolean painelVeiculo) {
        this.painelVeiculo = painelVeiculo;
    }

    public int getTipoMotorista() {
        return tipoMotorista;
    }

    public void setTipoMotorista(int tipoMotorista) {
        this.tipoMotorista = tipoMotorista;
    }

    public List<VeiculoTipo> getLstTipoVeiculo() {
        return lstTipoVeiculo;
    }

    public void setLstTipoVeiculo(List<VeiculoTipo> lstTipoVeiculo) {
        this.lstTipoVeiculo = lstTipoVeiculo;
    }

    public List<Marca> getLstMarca() {
        return lstMarca;
    }

    public void setLstMarca(List<Marca> lstMarca) {
        this.lstMarca = lstMarca;
    }

    public List getLstModelo() {
        return lstModelo;
    }

    public List<Cor> getLstCor() {
        return lstCor;
    }

    public void setLstCor(List<Cor> lstCor) {
        this.lstCor = lstCor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getAreaFilial() {
        return areaFilial;
    }

    public void setAreaFilial(Area areaFilial) {
        this.areaFilial = areaFilial;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public void setLstModelo(List lstModelo) {
        this.lstModelo = lstModelo;
    }

    public boolean isFoundBairro() {
        return foundBairro;
    }

    public void setFoundBairro(boolean foundBairro) {
        this.foundBairro = foundBairro;
    }

    public boolean isFoundCidade() {
        return foundCidade;
    }

    public void setFoundCidade(boolean foundCidade) {
        this.foundCidade = foundCidade;
    }

    public boolean isFoundUF() {
        return foundUF;
    }

    public void setFoundUF(boolean foundUF) {
        this.foundUF = foundUF;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isShowRefPessoal() {
        return showRefPessoal;
    }

    public void setShowRefPessoal(boolean showRefPessoal) {
        this.showRefPessoal = showRefPessoal;
    }

    public boolean isShowRefComercial() {
        return showRefComercial;
    }

    public void setShowRefComercial(boolean showRefComercial) {
        this.showRefComercial = showRefComercial;
    }

    public UploadedFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(UploadedFile imgFile) {
        this.imgFile = imgFile;
    }

    public List<VeiculoCategoria> getLstVeiculoCategoria() {
        return lstVeiculoCategoria;
    }

    public void setLstVeiculoCategoria(List<VeiculoCategoria> lstVeiculoCategoria) {
        this.lstVeiculoCategoria = lstVeiculoCategoria;
    }

    public List<TelefoneTipo> getLstTipoTelefone() {
        return lstTipoTelefone;
    }

    public void setLstTipoTelefone(List<TelefoneTipo> lstTipoTelefone) {
        this.lstTipoTelefone = lstTipoTelefone;
    }

    public List<CNHCategoria> getLstCategoriaCNH() {
        return lstCategoriaCNH;
    }

    public void setLstCategoriaCNH(List<CNHCategoria> lstCategoriaCNH) {
        this.lstCategoriaCNH = lstCategoriaCNH;
    }

    public List<UF> getLstUF() {
        return lstUF;
    }

    public void setLstUF(List<UF> lstUF) {
        this.lstUF = lstUF;
    }

    public UploadedFile getImgCnhFrenteFile() {
        return imgCnhFrenteFile;
    }
    
    public void setImgCnhFrenteFile(UploadedFile imgCnhFrenteFile) {
        this.imgCnhFrenteFile = imgCnhFrenteFile;
    }

    public UploadedFile getImgCnhVersoFile() {
        return imgCnhVersoFile;
    }

    public void setImgCnhVersoFile(UploadedFile imgCnhVersoFile) {
        this.imgCnhVersoFile = imgCnhVersoFile;
    }

    public List<ReferenciasTipoContato> getLstTipoContatoPessoal() {
        return lstTipoContatoPessoal;
    }

    public void setLstTipoContatoPessoal(List<ReferenciasTipoContato> lstTipoContatoPessoal) {
        this.lstTipoContatoPessoal = lstTipoContatoPessoal;
    }

    public List<ReferenciasTipoContato> getLstTipoContatoComercial() {
        return lstTipoContatoComercial;
    }

    public void setLstTipoContatoComercial(List<ReferenciasTipoContato> lstTipoContatoComercial) {
        this.lstTipoContatoComercial = lstTipoContatoComercial;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isLimitadorVeiculo() {
        return limitadorVeiculo;
    }

    public void setLimitadorVeiculo(boolean limitadorVeiculo) {
        this.limitadorVeiculo = limitadorVeiculo;
    }

    public List<Mercadoria> getLstMercadorias() {
        return lstMercadorias;
    }

    public void setLstMercadorias(List<Mercadoria> lstMercadorias) {
        this.lstMercadorias = lstMercadorias;
    }

    public List<Pais> getLstPais() {
        return lstPais;
    }

    public void setLstPais(List<Pais> lstPais) {
        this.lstPais = lstPais;
    }

    public boolean isHabilitaUF() {
        return habilitaUF;
    }

    public void setHabilitaUF(boolean habilitaUF) {
        this.habilitaUF = habilitaUF;
    }

    public boolean isHabilitaCidade() {
        return habilitaCidade;
    }

    public void setHabilitaCidade(boolean habilitaCidade) {
        this.habilitaCidade = habilitaCidade;
    }

    public Integer getIdMercadoria() {
        return idMercadoria;
    }

    public void setIdMercadoria(Integer idMercadoria) {
        this.idMercadoria = idMercadoria;
    }

    public boolean isHabilitaCidadeDestino() {
        return habilitaCidadeDestino;
    }

    public void setHabilitaCidadeDestino(boolean habilitaCidadeDestino) {
        this.habilitaCidadeDestino = habilitaCidadeDestino;
    }

    public boolean isHabilitaUFDestino() {
        return habilitaUFDestino;
    }

    public void setHabilitaUFDestino(boolean habilitaUFDestino) {
        this.habilitaUFDestino = habilitaUFDestino;
    }

    public boolean isShowMercadoria() {
        return showMercadoria;
    }

    public void setShowMercadoria(boolean showMercadoria) {
        this.showMercadoria = showMercadoria;
    }

    public List<UF> getLstUFOrigem() {
        return lstUFOrigem;
    }

    public void setLstUFOrigem(List<UF> lstUFOrigem) {
        this.lstUFOrigem = lstUFOrigem;
    }

    public List<UF> getLstUFDestino() {
        return lstUFDestino;
    }

    public void setLstUFDestino(List<UF> lstUFDestino) {
        this.lstUFDestino = lstUFDestino;
    }

    public List<Cidade> getLstCidadeOrigem() {
        return lstCidadeOrigem;
    }

    public void setLstCidadeOrigem(List<Cidade> lstCidadeOrigem) {
        this.lstCidadeOrigem = lstCidadeOrigem;
    }

    public List<Cidade> getLstCidadeDestino() {
        return lstCidadeDestino;
    }

    public void setLstCidadeDestino(List<Cidade> lstCidadeDestino) {
        this.lstCidadeDestino = lstCidadeDestino;
    }

    public Integer getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(Integer paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public Integer getUfOrigem() {
        return ufOrigem;
    }

    public void setUfOrigem(Integer ufOrigem) {
        this.ufOrigem = ufOrigem;
    }

    public Integer getCidOrigem() {
        return cidOrigem;
    }

    public void setCidOrigem(Integer cidOrigem) {
        this.cidOrigem = cidOrigem;
    }

    public Integer getPaisDestino() {
        return paisDestino;
    }

    public void setPaisDestino(Integer paisDestino) {
        this.paisDestino = paisDestino;
    }

    public Integer getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(Integer ufDestino) {
        this.ufDestino = ufDestino;
    }

    public Integer getCidDestino() {
        return cidDestino;
    }

    public void setCidDestino(Integer cidDestino) {
        this.cidDestino = cidDestino;
    }

    public String getDocProprietario() {
        return docProprietario;
    }

    public void setDocProprietario(String docProprietario) {
        this.docProprietario = docProprietario;
    }

    public String getNomProprietario() {
        return nomProprietario;
    }

    public void setNomProprietario(String nomProprietario) {
        this.nomProprietario = nomProprietario;
    }

    public boolean isHabilitaArea() {
        return habilitaArea;
    }

    public void setHabilitaArea(boolean habilitaArea) {
        this.habilitaArea = habilitaArea;
    }

    public boolean isHabilitaFilial() {
        return habilitaFilial;
    }

    public void setHabilitaFilial(boolean habilitaFilial) {
        this.habilitaFilial = habilitaFilial;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Integer getPais() {
        return pais;
    }

    public void setPais(Integer pais) {
        this.pais = pais;
    }

    public Integer getIdCadastral() {
        return idCadastral;
    }

    public void setIdCadastral(Integer idCadastral) {
        this.idCadastral = idCadastral;
    }

    public boolean isVeiculoNacional() {
        return veiculoNacional;
    }

    public void setVeiculoNacional(boolean veiculoNacional) {
        this.veiculoNacional = veiculoNacional;
    }

    public boolean isShowCNH() {
        return showCNH;
    }

    public void setShowCNH(boolean showCNH) {
        this.showCNH = showCNH;
    }

	public boolean isFoundCadMot() {
		return foundCadMot;
	}

	public void setFoundCadMot(boolean foundCadMot) {
		this.foundCadMot = foundCadMot;
	}

	public String getUrlFotoMot() {
		return urlFotoMot;
	}

	public void setUrlFotoMot(String urlFotoMot) {
		this.urlFotoMot = urlFotoMot;
	}

	public String getUrlFotoCnhF() {
		return urlFotoCnhF;
	}

	public void setUrlFotoCnhF(String urlFotoCnhF) {
		this.urlFotoCnhF = urlFotoCnhF;
	}

	public String getUrlFotoCnhV() {
		return urlFotoCnhV;
	}

	public void setUrlFotoCnhV(String urlFotoCnhV) {
		this.urlFotoCnhV = urlFotoCnhV;
	}

	public String getStsImgFile() {
		return stsImgFile;
	}

	public void setStsImgFile(String stsImgFile) {
		this.stsImgFile = stsImgFile;
	}

	public String getStsImgCnhFrenteFile() {
		return stsImgCnhFrenteFile;
	}

	public void setStsImgCnhFrenteFile(String stsImgCnhFrenteFile) {
		this.stsImgCnhFrenteFile = stsImgCnhFrenteFile;
	}

	public String getStsImgCnhVersoFile() {
		return stsImgCnhVersoFile;
	}

	public void setStsImgCnhVersoFile(String stsImgCnhVersoFile) {
		this.stsImgCnhVersoFile = stsImgCnhVersoFile;
	}

	public boolean isChangeImgFile() {
		return changeImgFile;
	}

	public void setChangeImgFile(boolean changeImgFile) {
		this.changeImgFile = changeImgFile;
	}

	public StreamedContent getScImgFile() {
		return scImgFile;
	}

	public void setScImgFile(StreamedContent scImgFile) {
		this.scImgFile = scImgFile;
	}

	public boolean isChangeImgFileCnhFrente() {
		return changeImgFileCnhFrente;
	}

	public void setChangeImgFileCnhFrente(boolean changeImgFileCnhFrente) {
		this.changeImgFileCnhFrente = changeImgFileCnhFrente;
	}

	public boolean isChangeImgFileCnhVerso() {
		return changeImgFileCnhVerso;
	}

	public void setChangeImgFileCnhVerso(boolean changeImgFileCnhVerso) {
		this.changeImgFileCnhVerso = changeImgFileCnhVerso;
	}

	public Boolean getValidoDocProprietarioNacional() {
		return validoDocProprietarioNacional;
	}

	public void setValidoDocProprietarioNacional(Boolean validoDocProprietarioNacional) {
		this.validoDocProprietarioNacional = validoDocProprietarioNacional;
	}

	public String getMsgResultPesquisaPlaca() {
		return msgResultPesquisaPlaca;
	}

	public void setMsgResultPesquisaPlaca(String msgResultPesquisaPlaca) {
		this.msgResultPesquisaPlaca = msgResultPesquisaPlaca;
	}

	public Boolean getBtnSalvarVeiculo() {
		return btnSalvarVeiculo;
	}

	public void setBtnSalvarVeiculo(Boolean btnSalvarVeiculo) {
		this.btnSalvarVeiculo = btnSalvarVeiculo;
	}

	public Boolean getPlacaEncontrada() {
		return placaEncontrada;
	}

	public void setPlacaEncontrada(Boolean placaEncontrada) {
		this.placaEncontrada = placaEncontrada;
	}

	public String getMsgPanelPlaca() {
		return msgPanelPlaca;
	}

	public void setMsgPanelPlaca(String msgPanelPlaca) {
		this.msgPanelPlaca = msgPanelPlaca;
	}

	public boolean isAtualizaPlaca() {
		return atualizaPlaca;
	}

	public void setAtualizaPlaca(boolean atualizaPlaca) {
		this.atualizaPlaca = atualizaPlaca;
	}

	public boolean isAplicarAtualizaPlaca() {
		return aplicarAtualizaPlaca;
	}

	public void setAplicarAtualizaPlaca(boolean aplicarAtualizaPlaca) {
		this.aplicarAtualizaPlaca = aplicarAtualizaPlaca;
	}

	public String getNumRenavamAtualizaPlaca() {
		return numRenavamAtualizaPlaca;
	}

	public void setNumRenavamAtualizaPlaca(String numRenavamAtualizaPlaca) {
		this.numRenavamAtualizaPlaca = numRenavamAtualizaPlaca;
	}

	public String getNumPlacaAnterior() {
		return numPlacaAnterior;
	}

	public void setNumPlacaAnterior(String numPlacaAnterior) {
		this.numPlacaAnterior = numPlacaAnterior;
	}

	public String getNumRenavamInformaCliente() {
		return numRenavamInformaCliente;
	}

	public void setNumRenavamInformaCliente(String numRenavamInformaCliente) {
		this.numRenavamInformaCliente = numRenavamInformaCliente;
	}

	public String getMsg1PanelPlaca() {
		return msg1PanelPlaca;
	}

	public void setMsg1PanelPlaca(String msg1PanelPlaca) {
		this.msg1PanelPlaca = msg1PanelPlaca;
	}

	public String getMsg2PanelPlaca() {
		return msg2PanelPlaca;
	}

	public void setMsg2PanelPlaca(String msg2PanelPlaca) {
		this.msg2PanelPlaca = msg2PanelPlaca;
	}

	public boolean isAbrirChamadoPlaca() {
		return abrirChamadoPlaca;
	}

	public void setAbrirChamadoPlaca(boolean abrirChamadoPlaca) {
		this.abrirChamadoPlaca = abrirChamadoPlaca;
	}

	public boolean isRequisitarAtendimento() {
		return requisitarAtendimento;
	}

	public void setRequisitarAtendimento(boolean requisitarAtendimento) {
		this.requisitarAtendimento = requisitarAtendimento;
	}

	public Boolean getRenavamDuplicado() {
		return renavamDuplicado;
	}

	public void setRenavamDuplicado(Boolean renavamDuplicado) {
		this.renavamDuplicado = renavamDuplicado;
	}

	public Boolean getRenavamVerificado() {
		return renavamVerificado;
	}

	public void setRenavamVerificado(Boolean renavamVerificado) {
		this.renavamVerificado = renavamVerificado;
	}

	public List<UploadedFile> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public String getMsgChamadoCliente() {
		return msgChamadoCliente;
	}

	public void setMsgChamadoCliente(String msgChamadoCliente) {
		this.msgChamadoCliente = msgChamadoCliente;
	}
		
}
