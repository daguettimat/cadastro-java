package br.com.global5.manager.bean.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.*;
import br.com.global5.infra.util.ValidaBrasil;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.auxiliar.TipoEndereco;
import br.com.global5.manager.model.cadastro.CNH;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.ect.Logradouro;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.model.enums.CNHCategoria;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.model.enums.TelefoneOrigem;
import br.com.global5.manager.model.enums.TelefoneTipo;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.cadastro.CNHService;
import br.com.global5.manager.service.cadastro.LocalizadorService;
import br.com.global5.manager.service.cadastro.TelefoneService;
import br.com.global5.manager.service.ect.LogradouroService;
import br.com.global5.manager.service.ect.UFService;
import br.com.global5.manager.service.enums.CNHCategoriaService;
import br.com.global5.manager.service.enums.DocumentoTipoService;
import br.com.global5.manager.service.enums.TelefoneTipoService;
import br.com.global5.manager.service.geral.MotoristaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.*;
import java.util.*;

@Named
@ViewAccessScoped
public class MotoristaMB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyDataModel<Motorista> motList;
    private List<CNH> lstCNH;
    private List<acMotorista> motAList;
    private List<Motorista> filteredValue;
    private Integer id;
    private Motorista motorista;
    private CNH cnhMotorista;
    private CNH cnhCompare;
    private List<DocumentoTipo> lstDocumentoTipo;
    private List<TelefoneTipo> lstTelefoneTipo;
    private List<CNHCategoria> lstCategoriaCNH;
    private List<UF> lstUF;
    private Filter<Motorista> filter = new Filter<Motorista>(new Motorista());

    private UploadedFile imgFile;

    private boolean foundEndereco;
    private boolean foundBairro;
    private boolean foundCidade;
    private boolean foundUF;
    private boolean foundCEP = false;
    private boolean edit;
    private boolean showBtnEnviar;

    private Integer numTipoFone = null;
    private Integer numTipoDoc1 = null;
    private Integer numTipoDoc2 = null;
    private Integer numEndOld = null;
    private Integer cnhID;
    private ViaCEPEndereco endereco;
    private Usuario usuario;
    private TelefoneTipo telefoneTipo;
    private ValidaBrasil validaBrasil;

    private String documento;
    private String nome;
    private String mae;
    private boolean mostrarExcluidos = false;

    private Date dtInicial;
    private Date dtFinal;


    @Inject
    MotoristaService motService;

    @Inject
    CNHService cnhService;

    @Inject
    CNHCategoriaService cnhCategoriaService;

    @Inject
    DocumentoTipoService docTipoService;

    @Inject
    LogradouroService logrService;

    @Inject
    TelefoneTipoService telTipoService;

    @Inject
    TelefoneService telefoneService;

    @Inject
    br.com.global5.manager.service.analise.MotoristaService motAService;

    @Inject
    LocalizadorService localizadorService;

    @Inject
    private UFService ufService;

    @Inject
    private br.com.global5.manager.service.analise.MotoristaService acMotService;


    @PostConstruct
    public void init()  {

        clear();

        showBtnEnviar = false;
       // edit = true;

    }

    public String foto() {
        try {
            return AppUtils.foto(motorista.getId(), motorista.getUrlFoto());
        } catch ( Exception e ) {
            return "";
        }
    }

    public void btnPesquisar() {

        filter.getEntity().setId(id);
        filter.getEntity().setDoc1(documento);
        filter.getEntity().setNome(nome);
        filter.getEntity().setMae(mae);
        filter.getEntity().setDtInicial(dtInicial);
        filter.getEntity().setDtFinal(dtFinal);
        filter.getEntity().setMostrarExcluidos(mostrarExcluidos);
        RequestContext.getCurrentInstance().execute("PF('motoristaTable').filter()");

    }

    public void upload() {

        String extesion = FilenameUtils.getExtension(imgFile.getFileName());

        try{
            InputStream is = imgFile.getInputstream();
            byte[] bytes = IOUtils.toByteArray(is);
            FileOutputStream fileOuputStream = new FileOutputStream( "/var/www/fotos/F" + motorista.getId() + "." + extesion);
            fileOuputStream.write(bytes);
            fileOuputStream.close();

            motorista.setUrlFoto("F"+ motorista.getId() + "." + extesion);
            motorista.setDtFoto(new Date());
            motService.crud().saveOrUpdate(motorista);
            motService.crud().commit();


            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!", "Foto de " + motorista.getNome()
                            + " atualizada com sucesso"));


        }
        catch(IOException e){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista " + motorista.getNome()
                            + " removido com sucesso", null));

        }


    }

    public void handleFileUpload(FileUploadEvent event) {

        UploadedFile file = event.getFile();
        showBtnEnviar = file != null;


    }


    public LazyDataModel<Motorista> getMotList() {
        if (motList == null) {
            motList = new LazyDataModel<Motorista>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public List<Motorista> load(int first, int pageSize,
                                            String sortField, SortOrder sortOrder,
                                            Map<String, Object> filters) {

                    br.com.global5.infra.enumerator.SortOrder order = null;
                    if (sortOrder != null) {
                        order = sortOrder.equals(SortOrder.ASCENDING) ? br.com.global5.infra.enumerator.SortOrder.ASCENDING
                                : sortOrder.equals(SortOrder.DESCENDING) ? br.com.global5.infra.enumerator.SortOrder.DESCENDING
                                : br.com.global5.infra.enumerator.SortOrder.UNSORTED;
                    }
                    filter.setFirst(first).setPageSize(pageSize)
                            .setSortField(sortField).setSortOrder(order)
                            .setParams(filters);
                    List<Motorista> list = motService.paginate(filter);
                    setRowCount(motService.count(filter));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public Motorista getRowData(String key) {
                    Motorista motorista = motService.findById(new Integer(key));
                    Hibernate.initialize(motorista.getDoc1_tipo());
                    Hibernate.initialize(motorista.getDoc2_tipo());
                    Hibernate.initialize(motorista.getLocalizador());
                    Hibernate.initialize(motorista.getTelefone());
                    Hibernate.initialize(motorista.getTelefone().getTipo());
                    return motorista;
                }
            };

        }
        return motList;
    }


    public void remove() {
        if (motorista != null && motorista.getId() != null) {
            motService.remove(motorista);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista " + motorista.getNome()
                            + " removido com sucesso", null));
            clear();
        }
    }

    public void update() {

        String msg;



        //TIPO 10 = CPF / 11 = CNPF
        if(this.motorista.getDoc1_tipo().getId()==10){
            if(!validaBrasil.validateCPF(this.motorista.getDoc1())){
                throw new BusinessException("CPF Inválido!!! Por Favor confira e digite novamente!");
            }
        }
        if(this.motorista.getDoc1_tipo().getId()==11){
            if(!validaBrasil.validateCNPJ(this.motorista.getDoc1())){
                throw new BusinessException("CNPJ Inválido!!! Por Favor confira e digite novamente!");
            }
        }

        if(this.motorista.getDoc2_tipo().getId()==10 ){
            if(!validaBrasil.validateCPF(this.motorista.getDoc2())){
                throw new BusinessException("CPF Inválido!!! Por Favor confira e digite novamente!");
            }
        }
        if(this.motorista.getDoc2_tipo().getId()==11){
            if(!validaBrasil.validateCNPJ(this.motorista.getDoc2())){
                throw new BusinessException("CNPJ Inválido!!! Por Favor confira e digite novamente!");
            }
        }

        Localizador locUp = new Localizador();

        //CEP Adicionado ou Alterado no cadastro - Realiza inclusão de dados na tabela localizador
        //Numero do Endereço alterado - Fazer Novo insert na tabela de Localização e Alterar cadastro
        if(foundCEP == true || this.getNumEndOld() != null){
             Localizador loc = new Localizador();

             //Registra Nova Localização do Motorista
             loc.setUf(this.motorista.getLocalizador().getUf());
             loc.setCidade(this.motorista.getLocalizador().getCidade());
             loc.setBairro(this.motorista.getLocalizador().getBairro());
             if(this.motorista.getLocalizador().getTipoEndereco()!= null){
                  loc.setTipoEndereco(this.motorista.getLocalizador().getTipoEndereco());
                }else{
                  loc.setTipoEndereco(new TipoEndereco(62));
             }
             loc.setLogradouro(this.motorista.getLocalizador().getLogradouro());
             loc.setNumero(this.motorista.getLocalizador().getNumero());
             loc.setPais("BRASIL");
             loc.setCep(this.motorista.getLocalizador().getCep());
             localizadorService.crud().save(loc);

             //Objeto para upDate
             locUp = loc;

             //Limpa variavel find CEP Logradouro
             foundCEP=false;
         }

        Telefone telUpMotorista = new Telefone();

        //TELEFONE - Find id do Telefone do motorista (para uso insert/update)

        Telefone tel = telefoneService.crud().
                eq("ddd", this.motorista.getTelefone().getDdd()).
                eq("tipo", this.motorista.getTelefone().getTipo()).
                eq("fone", this.motorista.getTelefone().getFone()).maxResult(1).find();

        //Telefone não encontrado será adicionado - Realiza inclusão de dados na tabela Telefone
        if(tel == null){
            Telefone telC = new Telefone();

            telC.setDdd(this.motorista.getTelefone().getDdd());
            telC.setTipo(new TelefoneTipo(numTipoFone));
            telC.setFone(this.motorista.getTelefone().getFone());
            telC.setOrigem(new TelefoneOrigem(70));
            telC.setDtCriacao(new Date());

            telefoneService.crud().saveOrUpdate(telC);

            //Objeto p update
            telUpMotorista = telC;
        }

        //CADASTRO MOTORISTA - Id null realiza novo registro
        if (motorista.getId() == null) {

            //Persiste dados
            if(locUp.getId() != null ){
                this.motorista.setUsuarioCriacao(usuario);
                this.motorista.setDtCriacao(new Date());
                this.motorista.setUsuarioAlteracao(usuario);
                this.motorista.setDtAlteracao(new Date());
                this.motorista.setLocalizador(locUp);
                if(telUpMotorista.getId() != null){
                    this.motorista.setTelefone(telUpMotorista);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Erro de persistência à referência ao endereço"));
                return;
            }

            motService.crud().save(motorista);
            msg = "Motorista " + motorista.getNome() + " criado com sucesso!";
        } else {
            //ATUALIZA CADASTRO MOTORISTA

            if(locUp.getId() != null ){
                this.motorista.setLocalizador(locUp);
            } else {
                this.motorista.getLocalizador().setNumero(this.motorista.getLocalizador().getNumero());
            }

            if(telUpMotorista.getId() != null){
                this.motorista.setTelefone(telUpMotorista);
            }

            this.motorista.setUsuarioAlteracao(usuario);
            this.motorista.setDtAlteracao(new Date());

            motService.crud().update(motorista);
            msg = "Motorista " + motorista.getNome() + " alterado com sucesso!";
        }



        if( cnhCompare != null ) {
            if( cnhCompare.getDtVencimento().getTime() != cnhMotorista.getDtVencimento().getTime()  ||
                    ! cnhCompare.getDocumento().equals(cnhMotorista.getDocumento()) ||
                    ! cnhCompare.getUf().equals(cnhMotorista.getUf()) ||
                    cnhCompare.getCategoria().getId() != cnhMotorista.getCategoria().getId()) {
            	
                cnhMotorista.setId(null);
                cnhMotorista.setMotorista(new Motorista(motorista.getId()));
                cnhService.crud().save(cnhMotorista);

                if( cnhMotorista.getId() != null ) {

                    EntityManager em = acMotService.crud().getEntityManager();

                    String SQL = "SELECT acm.*" +
                                 "  FROM analise_cadastral_motorista acm, analise_cadastral ac" +
                                 " WHERE acm.anacm_motoid = " + motorista.getId() +
                                 "   AND (ac.anac_status = 4 OR ac.anac_status = 5)" +
                                 "   AND acm.anacm_anacoid = ac.anacoid;";

                    List<acMotorista> lstMotoristas = em.createNativeQuery( SQL, acMotorista.class).getResultList();

                    for(acMotorista ac : lstMotoristas) {
                        ac.setCnh(new CNH(cnhMotorista.getId()));
                        acMotService.update(ac);
                    }

                }
            } else {
                cnhService.crud().update(cnhMotorista);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro " , msg));

    }

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/motoristaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inserir novos motoristas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    public void onTipoTelefone(){
        this.numTipoFone = this.motorista.getTelefone().getTipo().getId();
    }

    public void onTipoDocumento(){
        this.numTipoDoc1 = this.motorista.getDoc1_tipo().getId();
        this.numTipoDoc2 = this.motorista.getDoc2_tipo().getId();

    }

    public void onValueChangeNumEndereco(AjaxBehaviorEvent event ){
        //Cadastro ativo - monitora edição do campo Número do endereço
        if(motorista.getId() != null ){
            //Objeto cadMot (Dados antes da persistência)
            Motorista cadMot = motService.crud().eq("id", motorista.getId()).find();

            //NumEndereço Anterior a alteração (valueOld)
            this.setNumEndOld(cadMot.getLocalizador().getNumero());
        }
    }


    public void clear() {
        motorista = new Motorista();
        motorista.setDoc1_tipo(new DocumentoTipo());
        motorista.setDoc2_tipo(new DocumentoTipo());
        motorista.setLocalizador(new Localizador());
        motorista.setTelefone(new Telefone());
        motorista.getTelefone().setTipo(new TelefoneTipo());
        usuario = checkUsuario.valid();
        endereco = new ViaCEPEndereco();

        filter = new Filter<Motorista>(new Motorista());
        id = null;
        lstDocumentoTipo = docTipoService.crud().listAll();
        lstTelefoneTipo = telTipoService.crud().isNull("exclusao").listAll();
        lstCategoriaCNH = cnhCategoriaService.crud().isNull("exclusao").list();
        lstUF = ufService.crud().isNull("exclusao").listAll();

        edit = true;
    }

    public void findById(Integer id) {
        if (id == null) {
            throw new BusinessException("O id do acMotorista é obrigatório");
        }
        motorista = motService.crud().get(id);

        if (motorista == null) {
            throw new BusinessException("Motorista não foi encontrado pelo " + id);
        }

        Hibernate.initialize(motorista.getLocalizador());
        Hibernate.initialize(motorista.getDoc1_tipo());
        Hibernate.initialize(motorista.getDoc2_tipo());
        Hibernate.initialize(motorista.getTelefone().getTipo());

        try {

            cnhMotorista = new CNH();


            List<CNH> cnhList = cnhService.crud().eq("motorista.id", motorista.getId()).list();

            Collections.sort(cnhList, new Comparator<CNH>() {
                @Override
                public int compare(CNH o1, CNH o2) {
                    return o1.getDtVencimento().compareTo(o2.getDtVencimento());
                }
            });

            cnhMotorista = cnhList.get(cnhList.size()-1);
            cnhCompare = new CNH(cnhMotorista.getDocumento(), cnhMotorista.getDtPrimeiraEmissao(), cnhMotorista.getUf(),
                    cnhMotorista.getRegistro(), cnhMotorista.getDtRegistro(), cnhMotorista.getCategoria(),
                    cnhMotorista.getValidacao(), cnhMotorista.getDtCadastro(),cnhMotorista.getDtAtualizacao(),
                    cnhMotorista.getDtSubstituicao(), cnhMotorista.getMotorista(), cnhMotorista.getDtVencimento(),
                    cnhMotorista.getUrlCnhFrente(), cnhMotorista.getUrlCnhVerso());

            Hibernate.initialize(cnhMotorista.getCategoria());
            if( cnhMotorista.getCategoria().getId() == null ) {
                cnhMotorista.setCategoria(new CNHCategoria(54));
            }
            cnhID = cnhMotorista.getId();

        } catch (Exception e) {
            cnhMotorista = new CNH();
            cnhMotorista.setCategoria(new CNHCategoria(54));
        }

        motAList = motAService.crud().eq("motorista.id",motorista.getId()).list();
        Collections.sort(motAList, new Comparator<acMotorista>() {

            @Override
            public int compare(acMotorista o1, acMotorista o2) {

                return -o1.getAcCadastro().getId().compareTo(o2.getAcCadastro().getId());

            }
        });


    }

    public List<String> completeDescricao(String query) {
        List<String> result = motService.getNome(query);
        return result;
    }

    public void onRowSelect(SelectEvent event) {
        this.id = Integer.valueOf(((Motorista) event.getObject()).getId());

        findById(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/motoristaman.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

    public void showFicha(Integer id) {
        this.id = id;
        edit = false;

        findById(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/motoristaman.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Motorista " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

    public void btnBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/motoristalst.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista de Motoristas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    public void BuscaCep(AjaxBehaviorEvent event) {

        String cep =  (String) ((UIOutput)event.getSource()).getValue();

        if( cep.length() == 0 ) {
            return;
        }

        //Cadastro de CEPs - pesquisa no cadastro
        Logradouro logr = logrService.crud().eq("cep", cep.replace("-","")).find();

        if( logr == null ) {
            ViaCEPClient client = new ViaCEPClient();
            try {
                endereco = client.getEndereco(cep);

                motorista.getLocalizador().setUf(endereco.getUf().toUpperCase());
                motorista.getLocalizador().setCidade(endereco.getLocalidade().toUpperCase());
                motorista.getLocalizador().setBairro(endereco.getBairro().toUpperCase());
                motorista.getLocalizador().setLogradouro(endereco.getLogradouro().toUpperCase());

                foundCEP = true;

            } catch (IOException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,"Erro!","Não foi possível verificar "
                                + " o cep via webservice. Informe ao suporte técnico."));
            }
        } else {

            foundCEP = true;

            if(motorista.getLocalizador().getLogradouro()!= null){
                foundEndereco = (motorista.getLocalizador().getLogradouro().length() != 0);
            } else {
                foundEndereco = true;
            }

            if(motorista.getLocalizador().getBairro() != null){
                if (motorista.getLocalizador().getBairro().equals("NAO IDENTIFICADO")) {
                    foundBairro = false;
                } else {
                    foundBairro = (motorista.getLocalizador().getBairro().length() != 0);
                }
            } else {
                foundBairro = true;
            }

            if(motorista.getLocalizador().getCidade()!= null){
                foundCidade = (motorista.getLocalizador().getCidade().length() != 0);
            } else {
                foundCidade = true;
            }

            if(motorista.getLocalizador().getUf()!=null){
                foundUF = (motorista.getLocalizador().getUf().length() != 0);
            } else {
                foundUF = true;
            }

            motorista.getLocalizador().setUf(logr.getCidade().getUf().getSigla().toUpperCase());
            motorista.getLocalizador().setCidade(logr.getCidade().getNome().toUpperCase());
            motorista.getLocalizador().setBairro(logr.getIniBairro().getNome().toUpperCase());
            motorista.getLocalizador().setLogradouro(logr.getTipo().toUpperCase() + " " + logr.getNome().toUpperCase());
           // motorista.getLocalizador().setTipoEndereco(new TipoEndereco(62));

        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("frmMotorista:cepUFMotorista");
        context.update("frmMotorista:cepCidadeMotorista");
        context.update("frmMotorista:cepBairroMotorista");
        context.update("frmMotorista:cepEndMotorista");
    }

    public String flag(Integer id) {

        return AppUtils.pathFlag(id);
    }

    public void onRowUnselect(UnselectEvent event) {
        motorista = new Motorista();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setMotList(LazyDataModel<Motorista> motList) {
        this.motList = motList;
    }

    public List<Motorista> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Motorista> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public List<DocumentoTipo> getLstDocumentoTipo() {
        return lstDocumentoTipo;
    }

    public void setLstDocumentoTipo(List<DocumentoTipo> lstDocumentoTipo) {
        this.lstDocumentoTipo = lstDocumentoTipo;
    }

    public Filter<Motorista> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Motorista> filter) {
        this.filter = filter;
    }


    public boolean isFoundEndereco() {
        return foundEndereco;
    }

    public void setFoundEndereco(boolean foundEndereco) {
        this.foundEndereco = foundEndereco;
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

    public ViaCEPEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(ViaCEPEndereco endereco) {
        this.endereco = endereco;
    }

    public MotoristaService getMotService() {
        return motService;
    }

    public void setMotService(MotoristaService motService) {
        this.motService = motService;
    }

    public DocumentoTipoService getDocTipoService() {
        return docTipoService;
    }

    public void setDocTipoService(DocumentoTipoService docTipoService) {
        this.docTipoService = docTipoService;
    }

    public LogradouroService getLogrService() {
        return logrService;
    }

    public void setLogrService(LogradouroService logrService) {
        this.logrService = logrService;
    }

    public List<acMotorista> getMotAList() {
        return motAList;
    }

    public void setMotAList(List<acMotorista> motAList) {
        this.motAList = motAList;
    }

    public br.com.global5.manager.service.analise.MotoristaService getMotAService() {
        return motAService;
    }

    public void setMotAService(br.com.global5.manager.service.analise.MotoristaService motAService) {
        this.motAService = motAService;
    }

    public UploadedFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(UploadedFile imgFile) {
        this.imgFile = imgFile;
    }

    public boolean isShowBtnEnviar() {
        return showBtnEnviar;
    }

    public void setShowBtnEnviar(boolean showBtnEnviar) {
        this.showBtnEnviar = showBtnEnviar;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public LocalizadorService getLocalizadorService() {
        return localizadorService;
    }

    public void setLocalizadorService(LocalizadorService localizadorService) {
        this.localizadorService = localizadorService;
    }

    public TelefoneTipoService getTelTipoService() {
        return telTipoService;
    }

    public void setTelTipoService(TelefoneTipoService telTipoService) {
        this.telTipoService = telTipoService;
    }

    public TelefoneService getTelefoneService() {
        return telefoneService;
    }

    public void setTelefoneService(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
    }

    public List<TelefoneTipo> getLstTelefoneTipo() {
        return lstTelefoneTipo;
    }

    public void setLstTelefoneTipo(List<TelefoneTipo> lstTelefoneTipo) {
        this.lstTelefoneTipo = lstTelefoneTipo;
    }

    public Integer getNumTipoFone() {
        return numTipoFone;
    }

    public void setNumTipoFone(Integer numTipoFone) {
        this.numTipoFone = numTipoFone;
    }

    public TelefoneTipo getTelefoneTipo() {
        return telefoneTipo;
    }

    public void setTelefoneTipo(TelefoneTipo telefoneTipo) {
        this.telefoneTipo = telefoneTipo;
    }

    public ValidaBrasil getValidaBrasil() {
        return validaBrasil;
    }

    public void setValidaBrasil(ValidaBrasil validaBrasil) {
        this.validaBrasil = validaBrasil;
    }

    public Integer getNumTipoDoc1() {
        return numTipoDoc1;
    }

    public void setNumTipoDoc1(Integer numTipoDoc1) {
        this.numTipoDoc1 = numTipoDoc1;
    }

    public Integer getNumTipoDoc2() {
        return numTipoDoc2;
    }

    public void setNumTipoDoc2(Integer numTipoDoc2) {
        this.numTipoDoc2 = numTipoDoc2;
    }

    public Integer getNumEndOld() {
        return numEndOld;
    }

    public void setNumEndOld(Integer numEndOld) {
        this.numEndOld = numEndOld;
    }

    public List<CNH> getLstCNH() {
        return lstCNH;
    }

    public void setLstCNH(List<CNH> lstCNH) {
        this.lstCNH = lstCNH;
    }

    public CNH getCnhMotorista() {
        return cnhMotorista;
    }

    public void setCnhMotorista(CNH cnhMotorista) {
        this.cnhMotorista = cnhMotorista;
    }

    public boolean isFoundCEP() {
        return foundCEP;
    }

    public void setFoundCEP(boolean foundCEP) {
        this.foundCEP = foundCEP;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isMostrarExcluidos() {
        return mostrarExcluidos;
    }

    public void setMostrarExcluidos(boolean mostrarExcluidos) {
        this.mostrarExcluidos = mostrarExcluidos;
    }

    public Date getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public CNHService getCnhService() {
        return cnhService;
    }

    public void setCnhService(CNHService cnhService) {
        this.cnhService = cnhService;
    }

    public List<CNHCategoria> getLstCategoriaCNH() {
        return lstCategoriaCNH;
    }

    public void setLstCategoriaCNH(List<CNHCategoria> lstCategoriaCNH) {
        this.lstCategoriaCNH = lstCategoriaCNH;
    }

    public CNHCategoriaService getCnhCategoriaService() {
        return cnhCategoriaService;
    }

    public void setCnhCategoriaService(CNHCategoriaService cnhCategoriaService) {
        this.cnhCategoriaService = cnhCategoriaService;
    }

    public List<UF> getLstUF() {
        return lstUF;
    }

    public void setLstUF(List<UF> lstUF) {
        this.lstUF = lstUF;
    }

    public UFService getUfService() {
        return ufService;
    }

    public void setUfService(UFService ufService) {
        this.ufService = ufService;
    }
}
