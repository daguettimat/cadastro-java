package br.com.global5.manager.bean.analise;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.*;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.ReferenciasCategoria;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PerguntasService;
import br.com.global5.manager.service.analise.ReferenciaService;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import br.com.global5.manager.service.enums.ReferenciasCategoriaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Named
@SessionScoped
public class RefAnaliseMB implements Serializable {

    private static final long serialVersionUID = 1L;

    private acMotorista anacmot;
    private acVeiculos anacvei;

    private List<ReferenciasAvaliacao> lstReferencias;
    private List<acReferencia> lstReferenciasPessoais;
    private List<acPerguntas> lstPerguntas;
    private List<ReferenciasCategoria> lstCategorias;
    private Map<String, Object> options;
    private String textoReferencia;

    private Integer idRef;
    private Integer img;
    private Integer id;
    private Integer TipoReferencia;

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
    VeiculosService veiService;


    @PostConstruct
    public void init() {

        options = new HashMap<String, Object>();
        options.put("draggable", true);
        options.put("modal", true);


        options.put("contentWidth", "98%");
        options.put("height", "70%");
        options.put("contentheight", "90%");

        options.put("resizable", false);
        options.put("closable", true);

        lstReferencias = refService.listAll();
        lstCategorias = refCatService.listAll();
        try {
            Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            id = Integer.valueOf(requestParams.get("id").toString());
            if (id != null) {
                findByCadastro(id);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseMotorista.xhtml");
                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "acMotorista " + getId()
                                    + " não pode ser carregado. Informe ao suporte técnico.", null));
                }
            }
            TipoReferencia = Integer.valueOf(requestParams.get("tiporeferencia").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String printTexto(String resposta) {
        return onResposta(resposta, 1);
    }

    public String printFlag(String resposta) {
        return onResposta(resposta, 2);
    }

    public String onResposta(String resposta, int tipo) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String[] pairs = resposta.replace("{", "").replace("}", "").replace(" ", "").split(",");
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
                    flag = pathFlag(Integer.valueOf(map.get("Avaliacao")));
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
        options.put("width", "70%");
        RequestContext.getCurrentInstance().openDialog("dlgAnaliseReferencias", options, null);

    }

    public void openDlgPerguntas(Integer idReferencia) {
        options.put("width", "70%");
        options.put("heigth", "70%");

        idRef = idReferencia;

        Hibernate.initialize(lstPerguntas);

        if (TipoReferencia == 31) {
            //
            // acReferencia Pessoal
            //
            anacmot.setStatusPessoal(refService.crud().get(138));
        } else {
            if (TipoReferencia == 32) {
                //
                // acReferencia Comercial
                //
                anacmot.setStatusComercial(refService.crud().get(138));
            } else {
                if (TipoReferencia == 33) {
                    //
                    // Referencias Proprietarios
                    //
                    anacvei.setReferenciaProprietario(refService.crud().get(138));
                } else {
                    if (TipoReferencia == 34) {
                        //
                        // acReferencia Arrendatario
                        //
                        anacvei.setReferenciaArrendatario(refService.crud().get(138));
                    }
                }
            }
        }

        lstPerguntas = perguntasService.crud()
                .eq("referencia.id", idReferencia)
                .and()
                .eq("categoria.id", TipoReferencia)
                .list();


        if( TipoReferencia == 31 || TipoReferencia == 32 ) {
            motService.crud().saveOrUpdate(anacmot);
        } else {
            if( TipoReferencia == 33 || TipoReferencia == 34 ) {
                veiService.crud().saveOrUpdate(anacvei);
            }
        }


        RequestContext.getCurrentInstance().openDialog("dlgAnalisePerguntas", options, null);
    }

    public String mostraTextoReferencia() {

        return referenciaService.crud().get(idRef).getInformativo();
    }

    public void onReturnDlgClose(SelectEvent event) {
        RequestContext.getCurrentInstance().closeDialog(event.getObject());
    }

    public void updateStatusGeralMotorista() {
        List<Integer> motStatus = new ArrayList<>();
        motStatus.add(0, anacmot.getStatusComercial().getId());
        motStatus.add(1, anacmot.getStatusPessoal().getId());
        motStatus.add(2, anacmot.getStatusCNH().getId());
        motStatus.add(3, anacmot.getStatusCriminal().getId());

        anacmot.setStatusGeral(refService.crud().get(AppUtils.statusRecord(motStatus)));
        motService.crud().saveOrUpdate(anacmot);
    }

    public void salvarDlgPerguntas() {

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


        //
        // Ficha acMotorista
        //
        if( TipoReferencia == 31 || TipoReferencia == 32 ) {
            //
            // Atualizando o StatusPendencias para outros usuários
            //
            anacmot.setStatusPessoal(refService.crud().get(AppUtils.statusRecord(iStatus)));

            //
            // Atualizando o StatusPendencias geral da Ficha
            //
            updateStatusGeralMotorista();

            //
            // Atualizando a acReferencia
            //
            acReferencia refPessoal = referenciaService.crud().get(idRef);
            refPessoal.setAvaliacao(refService.crud().get(AppUtils.statusRecord(iStatus)));
            refPessoal.setDtRegistro(new Date());

            referenciaService.crud().saveOrUpdate(refPessoal);

            lstReferenciasPessoais = referenciaService.crud().eq("acCadastro.id", id).list();
        }

        //
        // Proprietario
        //
        if( TipoReferencia == 33 ) {
            anacvei.setReferenciaProprietario(refService.crud().get(AppUtils.statusRecord(iStatus)));
        }

        //
        // Arrendatario
        //
        if( TipoReferencia == 34 ) {
            anacvei.setReferenciaArrendatario(refService.crud().get(AppUtils.statusRecord(iStatus)));
        }

        FacesContext.getCurrentInstance().addMessage(
                "acPerguntas Gravadas!",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "As perguntas ao contato foram gravadas! "
                        , null));

        //RequestContext.getCurrentInstance().closeDialog("dlgAnalisePerguntas");
    }

    public void updateCriminal() {

        String msg = "Analise do resultado da pesquisa do criminal de " + anacmot.getMotorista().getNome() + " atualizada com sucesso!";

        //
        // Atualizando o StatusPendencias geral da Ficha
        //
        updateStatusGeralMotorista();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", msg));


    }



    public void clear() {
        anacmot = new acMotorista();
        anacvei = new acVeiculos();
        id = null;
    }

    public void findByCadastro(Integer id) {
        if (id == null) {
            throw new BusinessException("O id da Analise Cadastral é obrigatório");
        }
        anacmot = motService.crud().eq("acCadastro.id", id).find();
        if (anacmot == null) {
            throw new BusinessException("Análise Cadastral não foi encontrado pelo id: " + id);
        }
        Hibernate.initialize(anacmot.getMotorista());
        Hibernate.initialize(anacmot.getAcCadastro());
        Hibernate.initialize(anacmot.getConsultaPessoa());
        Hibernate.initialize(anacmot.getMotorista().getLocalizador());
        Hibernate.initialize(anacmot.getAcCadastro().getUsuarioCriacao());

        lstReferenciasPessoais = referenciaService.crud().eq("acCadastro.id", id).list();

    }

    public void onRowSelect(SelectEvent event) {
        this.id = Integer.valueOf(((acCadastro) event.getObject()).getId());

        findByCadastro(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseMotorista.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "acMotorista " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

    public void btnBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseMenu.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista do cadastro de motoristas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    public String pathImage() {
        String path = "";
        try {

            switch (anacmot.getStatusGeral().getId()) {
                case 35:
                    path = "images/blue-ok.png";
                    break;
                case 36:
                    path = "images/yellow-ok.png";
                    break;
                case 37:
                    path = "images/red-cross.png";
                    break;
                case 38:
                    path = "images/blue-ok.png";
                    break;
                case 39:
                    path = "images/red-ok.png";
                    break;
                case 138:
                    path = "images/status.png";
                    break;
                case 139:
                    path = "images/blue-cross.png";
                    break;
            }
            return path;
        } catch (Exception e) {
            return "images/reference.png";
        }
    }

    public String pathFlag(Integer id) {
        String path = "";
        try {

            switch (id) {
                case 1:
                    path = "images/cabinet.png";
                    break;
                case 2:
                    path = "images/question.png";
                    break;
                case 3:
                    path = "images/ref_sign_sync.png";
                    break;
                case 4:
                    path = "images/ref_dell_keyboard.png";
                    break;
                case 5:
                    path = "images/windows_blue_screen_3.png";
                    break;
                case 6:
                    path = "images/ref_client.png";
                    break;
                case 8:
                    path = "images/ref_sign_check.png";
                    break;
                case 9:
                    path = "images/ref_sign_error.png";
                    break;
                case 28:
                    path = "images/ref_sign_sync.png";
                    break;
                case 35:
                    path = "images/green-ok.png";
                    break;
                case 36:
                    path = "images/windows_blue_screen_1.jpg";
                    break;
                case 37:
                    path = "images/red-cross.png";
                    break;
                case 38:
                    path = "images/blueflag.png";
                    break;
                case 39:
                    path = "images/ref_sign_ban.png";
                    break;
                case 138:
                    path = "images/status.png";
                    break;
                case 139:
                    path = "images/ref_sign_info.png";
                    break;
                default:
                    path = "images/ref_sign_question.png";
                    break;
            }
            return path;
        } catch (Exception e) {
            return "images/reference.png";
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        anacmot = new acMotorista();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
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


}
