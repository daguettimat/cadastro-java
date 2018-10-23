package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.*;
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

import org.hibernate.Hibernate;

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
    private UploadedFile imgFile;
    private UploadedFile imgCnhFrenteFile;
    private UploadedFile imgCnhVersoFile;
    
    private String stsImgFile;
    private StreamedContent scImgFile;
    private String stsImgCnhFrenteFile;
    private String stsImgCnhVersoFile;
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
            context.execute("cnhHIDE();");
            showCNH = false;
        }


    }

    public void veiNacional() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (veiculoNacional) {
            //context.execute("veiNacionalShow();");
            veiculoNacional = true;
            refVeiculo.setNacional(true);

        } else {
            //context.execute("veiNacionalHide();");
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
            docProprietario = "";
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

    public void verificaDoc(AjaxBehaviorEvent event) {

        String doc = (String) ((UIOutput) event.getSource()).getValue();

        doc = (Normalizer
                .normalize(doc, Normalizer.Form.NFD)
                .replaceAll("[\\W]", ""));

        docProprietario = doc;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiPropDoc");


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

    public void findVeiculo(AjaxBehaviorEvent event) {

        String placa = (String) ((UIOutput) event.getSource()).getValue();

        Pattern pattern = Pattern.compile("[a-zA-Z]{3}-\\d{4,4}");
        Matcher matcher = pattern.matcher(placa);

        boolean match = matcher.find();

        if( ! veiculoNacional ) {
            match = true;
        }


        if (match && placa.length() > 0) {
            Veiculo veiculo;
            if (veiculoService.crud().eq("placa", placa.toUpperCase()).count() <= 1) {
                veiculo = veiculoService.crud().eq("placa", placa.toUpperCase()).find();
            } else {
                veiculo = veiculoService.crud().eq("placa", placa.toUpperCase()).list().get(0);
            }
            if (veiculo != null) {

                // veiculoNacional = ValidaBrasil.validateRENAVAM(veiculo.getRenavam());
                veiculoNacional = veiculo.isNacional();

                refVeiculo.setMarca(veiculo.getMarca().getId());
                refVeiculo.setModelo(veiculo.getModelo().getId());
                refVeiculo.setCor(veiculo.getCor().getId());
                refVeiculo.setProprietario(getProprietario(veiculo.getProprietario()));
                refVeiculo.setTipo(veiculo.getTipo().getId());
                refVeiculo.setChassi(veiculo.getChassi());
                if( veiculoNacional ) {
                    refVeiculo.setRenavam("00000000000".substring(veiculo.getRenavam().length()) + veiculo.getRenavam());
                } else {
                    refVeiculo.setRenavam(veiculo.getRenavam());
                }
                refVeiculo.setMunicipio(veiculo.getMunicipio());
                refVeiculo.setUf(veiculo.getUf());
                refVeiculo.setDocumento(veiculo.getDocumento());
                refVeiculo.setIdVeiculo(veiculo.getId());
                refVeiculo.setNacional(veiculoNacional);

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
                return;
            } else {
                refVeiculo = new ReferenciaVeiculos();
                refVeiculo.setPlaca(placa.toUpperCase());
                refVeiculo.setNacional(true);
                refVeiculo.setProprietario(new br.com.global5.manager.model.auxiliar.Proprietario());
                refVeiculo.getProprietario().setEndereco(new Endereco());
                refVeiculo.getProprietario().setDocumento("");

            }
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
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esse RENAVAM não pode ser "
                            + " validado confira e digite novamente!", "Erro"));
            return;
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
        return lstTipoMotorista;
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
	
	
    
}
