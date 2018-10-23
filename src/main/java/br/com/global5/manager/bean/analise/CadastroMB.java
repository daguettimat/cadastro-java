package br.com.global5.manager.bean.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.analise.*;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.model.enums.FichaTipo;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PendService;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.enums.FichaStatusService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Named
@SessionScoped
public class CadastroMB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyDataModel<acCadastro> cadList;

    private List<acCadastro> filteredValue;
    private List<ReferenciasAvaliacao> lstReferencias;

    private acCadastro cadastro;
    private acMotorista motorista;
    private acLiberacao liberacao;
    private acVeiculos veiculos;
    private acMercadoria mercadoria;

    private Usuario usuario;

    private Filter<acCadastro> filter;

    private Integer id;
    private String finalizarComo;
    private Integer analiseEspecial;

    private Area areaFilial = new Area();

    @Inject
    private CadastroService cadService;

    @Inject
    private MotoristaService motService;

    @Inject
    private VeiculosService veiService;

    @Inject
    private FichaStatusService fichaStatusService;

    @Inject
    private AreaService areaService;

    @Inject
    private PendService pendenciasService;

    @Inject
    ReferenciasAvaliacaoService refService;


    @PostConstruct
    public void init() {

        cadastro = new acCadastro();
        cadastro.setAcLiberacao(new acLiberacao());
        cadastro.setClienteArea(new Area());

        checkUsuario.isOption(true);

        clear();

    }


    public String  flag(int id) {

        return AppUtils.pathFlag(id);
    }

    public String flagOperador(int id) {

        acCadastro ac = cadService.crud().get(id);

        if( ac.getStatus().getId() == 8 ) {
            return "images/lock_green_icon.png";
        }

        if( ac.getStatus().getId() == 9 ) {
            return "images/lock_red_icon.png";
        }

        if( AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) >= 0 &&
                    AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) <= 10    ) {
            return "images/user_red_icon.png";
        }

        if( AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) >= 11 &&
                AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) <= 20    ) {
            return "images/user_yellow_icon.png";
        }

        if( AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) >= 21 &&
                AppUtils.getDateDiff(ac.getDtAlteracao(), new Date(), TimeUnit.MINUTES) <= 30    ) {
            return "images/user_green_icon.png";
        }


        return "/images/user_blue_icon.png";
    }

    public void update() {

        try {
            String msg = "Observações relativas a ficha " + cadastro.getId() + " foram atualizadas com sucesso!";
            cadService.crud().merge(cadastro);
            cadService.crud().commit();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", msg));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso","cadastro " + getId()
                            + " não pode ser salvo. Informe ao suporte técnico :" + e.getMessage() ));
        }
    }

    public String typeFlag(int id) {
        return AppUtils.typeFlag(id);
    }

    public void delete() {

        if( cadastro.getObservacaoExclusao().isEmpty() ) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A observação do motivo da exclusão é obrigatória."));
            return;
        }
        update();

        cadastro = cadService.crud().get(cadastro.getId());


        if( cadastro.getStatus().getId() == 7 || cadastro.getStatus().getId() == 8 || cadastro.getStatus().getId() == 9 ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgFechado').show();onTop('dlgFechado');");
            return;
        }

        try {
            cadastro.setStatus(new FichaStatus(7));
            cadastro.setDtTermino(new Date());
            cadastro.setDtExclusao(new Date());
            cadastro.setUsuarioExclusao(usuario);
            cadastro.setUsuarioTermino(usuario);
            cadastro.setValor(0D);
            cadService.crud().update(cadastro);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgExcluida').show();onTop('dlgExcluida');");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível excluir. Informe ao suporte técnico."));
        }
    }

    public void updateOperador(int id, Usuario usuario) {

        try {
            cadastro = cadService.crud().get(id);
            if( cadastro.getStatus().getId() != 7 && cadastro.getStatus().getId() != 8 && cadastro.getStatus().getId() != 9) {
                cadastro.setUsuarioAlteracao(usuario);
                cadastro.setDtAlteracao(new Date());
                cadService.crud().update(cadastro);
            }

            this.usuario = usuario;

        } catch (Exception e) {}

    }


    public void showAnalise(int id) {
        try {
            this.id = id;
            FacesContext.getCurrentInstance().getExternalContext().redirect( "../analise/analiseDetalhes.xhtml?id=" + id +  "&faces-redirect=true");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Análise Cadastral ID: " + id
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }

    }

    public void showAnaliseViaPendencias(int id) {
        try {
            this.id = id;
            FacesContext.getCurrentInstance().getExternalContext().redirect( "pages/analise/analiseDetalhes.xhtml?id=" + id +  "&faces-redirect=true");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Análise Cadastral ID: " + id
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }

    }

    public boolean disableButton(int status) {

        if( pendenciasService.crud().eq("cadastro.id", id).count() == 0 ) {
            status = 8;
        }

        switch ( status ) {
            case 8:
            case 9:
                return false;
            default:
                return true;
        }
    }

    public void unlock(int id) {
        try {
            this.id = id;

            if( pendenciasService.crud().eq("cadastro.id", id).count() == 0) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", "Análise Cadastral ID: " + id
                                + " não possui pendências cadastradas. "));
                return;

            }

            cadastro = cadService.crud().get(id);
            cadastro.setStatus(new FichaStatus(6));
            cadService.crud().update(cadastro);

            FacesContext.getCurrentInstance().getExternalContext().redirect( "../analise/analiseCadastral.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Análise Cadastral ID: " + id
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }

    }

    public void finalizarFicha() {

        int fichaStatus;
        String msg;

        cadastro = cadService.crud().get(cadastro.getId());

        if( cadastro.getStatus().getId() == 7 || cadastro.getStatus().getId() == 8 || cadastro.getStatus().getId() == 9 ) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgFechado').show();onTop('dlgFechado');");
            return;
        }

        List<Integer> ficStatus = new ArrayList<>();
        int idx = 0;

        if( motService.crud().eq("acCadastro.id", cadastro.getId()).count() > 0 ) {
            motorista = motService.crud().eq("acCadastro.id", cadastro.getId()).find();
            ficStatus.add(idx++, motorista.getStatusGeral().getId());
        }

        if( veiService.crud().eq("acCadastro.id", cadastro.getId()).count() > 0 ) {
            List<acVeiculos> acVeiculosList = veiService.crud().eq("acCadastro.id", cadastro.getId()).list();
            if(acVeiculosList.size() > 0) {
                for (acVeiculos veiculos : acVeiculosList) {
                    ficStatus.add(idx++, veiculos.getStatusGeral().getId());
                }
            }
        }

        ficStatus.add(idx++,cadastro.getAnaliseEspecialStatus());

        fichaStatus = AppUtils.statusFinal(ficStatus);

        cadastro = cadService.crud().get(cadastro.getId());

        if( fichaStatus == 143 ) {
            cadastro.setStatus(new FichaStatus(143));
            cadastro.setUsuarioTermino(new Usuario(usuario.getId()));
            cadastro.setDtTermino(new Date());
            cadService.crud().saveOrUpdate(cadastro);
            msg = "A ficha não foi recomendada, o cliente será notificado.";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "FICHA NÃO RECOMENDADA!", msg));
            return;
        }

        if( fichaStatus == 142 ) {
            // acCadastro cadastro, Date dtRegistroFicha, Date dtLiberacao, String certificado, String chave

            StoredProcedureQuery query = cadService.crud().getEntityManager().createNamedStoredProcedureQuery("stpLiberacao");
            query.setParameter("ficha", cadastro.getId());
            query.execute();
            int liberacao = (int) query.getOutputParameterValue("id");

            cadastro.setAcLiberacao(new acLiberacao(liberacao));
            cadastro.setStatus(new FichaStatus(142));
            cadastro.setUsuarioTermino(usuario);
            cadastro.setDtTermino(new Date());

            msg = " Ficha finalizada! A ficha foi recomendada, o cliente será notificado";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", msg));

        } else {

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgFinalizar').show();onTop('dlgFinalizar');");

        }


        cadService.crud().saveOrUpdate(cadastro);

    }

    public String pathFlag(Integer id) {

        return AppUtils.pathFlag(id);

    }

    public void usuarioFinaliza() {

        List<Integer> ficStatus = new ArrayList<>();
        int idx = 0;



        if( motService.crud().eq("acCadastro.id", cadastro.getId()).count() > 0 ) {
            motorista = motService.crud().eq("acCadastro.id", cadastro.getId()).find();
            ficStatus.add(idx++, motorista.getStatusGeral().getId());
        }

        if( veiService.crud().eq("acCadastro.id", cadastro.getId()).count() > 0 ) {
            List<acVeiculos> acVeiculosList = veiService.crud().eq("acCadastro.id", cadastro.getId()).list();
            if(acVeiculosList.size() > 0) {
                for (acVeiculos veiculos : acVeiculosList) {
                    ficStatus.add(idx++, veiculos.getStatusGeral().getId());
                }
            }
        }

        ficStatus.add(idx++,cadastro.getAnaliseEspecialStatus());

        Integer fichaStatus = AppUtils.statusFinal(ficStatus);

        if( fichaStatus != 142 ) {
            fichaStatus = 143;
        }

        finalizarComo =  fichaStatusService.crud().get(fichaStatus).getDescricao();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlgAviso').show();onTop('dlgAviso');");

    }


    public LazyDataModel<acCadastro> getCadList() {

        //
        // Validação do Usuário
        //

        if (cadList == null ) {
            cadList = new LazyDataModel<acCadastro>() {
                @Override
                public List<acCadastro> load(int first, int pageSize,
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
                    List<acCadastro> list = cadService.paginate(filter);
                    setRowCount(cadService.count(filter));
                    return list;
                }


                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public acCadastro getRowData(String key) {
                    return cadService.findById(new Integer(key));
                }

            };

        }
        return cadList;
    }

    public void remove() {
        if (cadastro != null && cadastro.getId() != null) {
            cadService.remove(cadastro);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso" , "cadastro " + cadastro.getId()
                            + " removido com sucesso"));
            clear();
        }
    }

    public void clear() {
        cadastro = new acCadastro();
        filter = new Filter<>(new acCadastro());
        filter.setEntity(new acCadastro());
        filter.getEntity().setAcLiberacao(new acLiberacao());
        filter.getEntity().setClienteArea(new Area());
        filter.getEntity().setFilialArea(new Area());
        filter.getEntity().setStatus(new FichaStatus());
        filter.getEntity().setUsuarioTermino(new Usuario());
        filter.getEntity().setUsuarioAlteracao(new Usuario());
        filter.getEntity().setUsuarioCriacao(new Usuario());
        filter.getEntity().setFilialArea(new Area());
        filter.getEntity().setTipoFicha(new FichaTipo());

        Calendar now = Calendar.getInstance();

        now.add(Calendar.DATE, -1);

        filter.getEntity().setDtInicial(now.getTime());

        now.add(Calendar.DATE, 2);
        filter.getEntity().setDtFinal(now.getTime());

        id = null;

        lstReferencias = refService.listAll();
    }

    public List<Area> listFilial(String query) {

        return areaService.listNome(query, 3);

    }


    public void selectFilial(SelectEvent event) {

        Area area = (Area) event.getObject();
        filter.getEntity().setFilialArea(area);
    }

    public void findById(Integer id) {
        if (id == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "O número da Ficha é obrigatório! "));
            return;
        }
        cadastro = cadService.crud().get(id);
        if (cadastro == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!", "Cadastro não existe. "));
        }
    }

    public void loadScreen(Integer id) {
//        findById(id);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml?id=" + id );
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro!", "cadastro " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico."));
        }



    }

    public void dummy() {

    }

    public void onRowSelect(SelectEvent event) {
        this.id = ((acCadastro) event.getObject()).getId();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseDetalhes.xhtml?id=" + this.id );
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "cadastro " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

    public void btnBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseCadastral.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Análise Cadastral " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    public String formatJSON(String texto) {

        if( texto == null ) {
            return "SEM RESPOSTA";
        }

        return AppUtils.formatJSON(texto);

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setCadList(LazyDataModel<acCadastro> cadList) {
        this.cadList = cadList;
    }

    public List<acCadastro> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<acCadastro> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acCadastro getCadastro() {
        if( cadastro == null ) {
            cadastro = new acCadastro();
        }
        return cadastro;
    }

    public void setCadastro(acCadastro cadastro) {
        this.cadastro = cadastro;
    }

    public Filter<acCadastro> getFilter() {
        return filter;
    }

    public void setFilter(Filter<acCadastro> filter) {
        this.filter = filter;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public acMotorista getMotorista() {
        return motorista;
    }

    public void setMotorista(acMotorista motorista) {
        this.motorista = motorista;
    }

    public acLiberacao getLiberacao() {
        return liberacao;
    }

    public void setLiberacao(acLiberacao liberacao) {
        this.liberacao = liberacao;
    }

    public acVeiculos getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(acVeiculos veiculos) {
        this.veiculos = veiculos;
    }

    public acMercadoria getMercadoria() {
        return mercadoria;
    }

    public void setMercadoria(acMercadoria mercadoria) {
        this.mercadoria = mercadoria;
    }

    public String getFinalizarComo() {
        return finalizarComo;
    }

    public void setFinalizarComo(String finalizarComo) {
        this.finalizarComo = finalizarComo;
    }

    public Area getAreaFilial() {
        return areaFilial;
    }

    public void setAreaFilial(Area areaFilial) {
        this.areaFilial = areaFilial;
    }

    public Integer getAnaliseEspecial() {
        return analiseEspecial;
    }

    public void setAnaliseEspecial(Integer analiseEspecial) {
        this.analiseEspecial = analiseEspecial;

    }

    public List<ReferenciasAvaliacao> getLstReferencias() {
        return lstReferencias;
    }

    public void setLstReferencias(List<ReferenciasAvaliacao> lstReferencias) {
        this.lstReferencias = lstReferencias;
    }
}
