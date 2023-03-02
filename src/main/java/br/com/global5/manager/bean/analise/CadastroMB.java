package br.com.global5.manager.bean.analise;

import br.com.global5.infra.atualiza_motvei_chp.MotoristaClient;
import br.com.global5.infra.atualiza_motvei_chp.VeiculoClient;
import br.com.global5.infra.atualiza_motvei_chp.acMotoristaCadT;
import br.com.global5.infra.atualiza_motvei_chp.acVeiculoCadT;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.model.TipoValor;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.bean.geral.MotoristaMB;
import br.com.global5.manager.model.analise.*;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.model.enums.FichaTipo;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PendService;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.enums.FichaStatusService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import br.com.global5.manager.service.geral.UsuarioService;

import org.json.JSONObject;
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
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private List<acMotoristaCadT> listaAcMotoristaParaTrafegus;
    private List<acVeiculoCadT> listaAcVeiculoParaTrafegus;
    
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
    private br.com.global5.manager.service.geral.MotoristaService motServ;
    
    @Inject
    private VeiculoService veiculoService;
    
    @Inject
    private UsuarioService usuService;
    
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
            
            	// Pesquisa o motorista no servidor da Chp @author: Francis            	
            	Integer idMot = motorista.getMotorista().getId();		// id do motorista
            	String cpfMot = motorista.getMotorista().getDoc1();		// cpf do motorista
            	
            	if(idMot != null && cpfMot != "" && motorista.getMotorista().isNacional() == true){
//            		STOP FRANCIS 19 08 2022 - 2022-08-19 -> Liberado em 05 09 2022 
            		pesquisarMotoristaServidorChp(idMot, cpfMot);
            	}
            
        }

        if( veiService.crud().eq("acCadastro.id", cadastro.getId()).count() > 0 ) {
            List<acVeiculos> acVeiculosList = veiService.crud().eq("acCadastro.id", cadastro.getId()).list();
            if(acVeiculosList.size() > 0) {
                for (acVeiculos veiculos : acVeiculosList) {
                    ficStatus.add(idx++, veiculos.getStatusGeral().getId());
                    
                    // Pesquisa o veiculo no servidor da Chp @author: Francis
                    Integer idVei = veiculos.getVeiculo().getId();
                    String placaVei = veiculos.getVeiculo().getPlaca().toString().trim();
                    
                    if( idVei != null && placaVei != ""){
//                		STOP FRANCIS 19 08 2022 - 2022-08-19  -> Liberado em 05 09 2020              	
                    	pesquisarVeiculoServidorChp(idVei, placaVei);
                    }
                    
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
    
    /**
     * @author Francis Bueno
     * @since 2021-10-29 11:28
     * @param idMotorista
     * @param cpfMotorista
     * Método faz a pesquisa do motorista no servidor da Chp. Caso exista o motorista
     * será requisitado uma atualização do nosso dados ao sistema da chp.
     * Caso não exista será atualizado o nosso cadastro de motorista informando que 
     * os dados do motorista foi checado no servidor da chp;
     */
    private void pesquisarMotoristaServidorChp(Integer idMotorista, String cpfMotorista){
    	
    	MotoristaClient wsM = new MotoristaClient();

    	// Pesquisa se o cpf existe na base da chp/trafegus
    	JSONObject objJsonMot = wsM.getMotorista(cpfMotorista.trim());

    	if( objJsonMot != null){
    		
    		String idMotoristaChp = "";
    		
    		idMotoristaChp = objJsonMot.get("codigo").toString();
    		
    		// Dados coletado do ws 192.168.64.101/ws_rest/public/api/motorista/{cpf}
    		objJsonMot.get("codigo").toString();			// id do motorista na chp
    		objJsonMot.get("cpf_motorista").toString();		// cpf do motorista na chp
    		objJsonMot.get("nome").toString();				// 1 nome
    		objJsonMot.get("rg").toString();				// 2 rg
    		objJsonMot.get("nro_cnh").toString();			// 4 cnh
    		objJsonMot.get("categoria_cnh").toString();		// 5 categoria cnh
    		objJsonMot.get("validade_cnh").toString();		// 6 validade cnh
    		objJsonMot.get("cnh_uf").toString();			// 7 uf cnh
    		objJsonMot.get("cnh_seg").toString();			// 8 codigo seguranca cnh    		
    		    		
    		// Atualiza dados do motorista no sistema Trafegus
    		atualizarMotoristaNoServidorChp(cpfMotorista, idMotoristaChp);
    				
    	} else {
    		// Atualiza o registro no cadastro, informando que consultou ws mais não teve nada a ser atualizado na dtAtualizaWS
			 Motorista mot = motServ.findById(idMotorista);
			 //mot.setDtAtualizaWs(null);
			 mot.setDtVerificacaoWs(new Date());
			 motServ.crud().update(mot);
			 motServ.commit();
    	}
    	
    }
    
    /**
     * @author Francis Bueno
     * @since 2021-10-28 11:28
     * @param cpf
     * @param idMotoristaChp
     * Envia os dados do motorista para ser atualizado no sistema Chp; 
     */
    private void atualizarMotoristaNoServidorChp(String cpf, String idMotoristaChp){
    	
   	EntityManager em = usuService.crud().getEntityManager();
    	
    	String query = "select m.motoid as id,  m.mot_documento1 as cpf, m.mot_nome as nome, m.mot_documento2 as rg,"
    			+ " c.mcnh_registro as cnh, e.enum_nome as categoriaCnh, c.mcnh_uf as ufCnh, c.mcnh_validacao as validacaoCnh, c.mcnh_dt_vencimento as dtVenctoCnh, "
    			+ " m.mot_documento2_uf as ufRgEmissor , m.mot_dt_nascimento as dtNascimento, c.mcnh_primeira_emissao as dtPriEmissao,  "
    			+ " m.mot_nomepai as nomePai, m.mot_nomemae as nomeMae, l.loc_logradouro as logradouro, l.loc_cep as cep, l.loc_numero as numEndereco, "
    			+ " l.loc_uf as ufEndereco, l.loc_pais as paisEndereco, l.loc_bairro as bairroEndereco, l.loc_complemento as complEndereco, l.loc_cidade as cidadeEndereco "
    			+ " from motorista m, motorista_cnh c ,  enum_cnh_categoria e , localizador l"     			
    			+ " where m.mot_mcnhoid = c.mcnhoid and  m.mot_locoid = l.locoid and "
    			+ "       c.mcnh_categoria = e.enumoid and m.mot_dt_exclusao is null and m.mot_documento1 is not null and "
    			+ "       m.mot_documento1_tipo = 10 and m.mot_documento1 = '" + cpf + "\'";    	
    	  	
    	listaAcMotoristaParaTrafegus = em.createNativeQuery(query, "ListaMotoristaCadTMapping").getResultList();
    
    	MotoristaClient wsM = new MotoristaClient();
    	
    	Integer idMotCad = null;
    	String cpfMot = "", nomeMot = "" , rgMot = "", cnhMot = "", cnhCategoriaMot = "";
    	String cnhDtValidadeMot = null;
    	String cnhUfMot = "", cnhSegurancaMot = "";
    	
    	String ufRgMot = "", dtNascMot = "" , cnhDtPrimEmissao = "", nomePaiMot= "", nomeMaeMOT = "", logradouroMot = "";
    	String cepMot = "", numEnderecoMot = "", ufEnderecoMot = "",  paisEnderecoMot = "", bairroEndereco = "", complEndereco = "", cidadeEndereco = "";
    	
    	if( listaAcMotoristaParaTrafegus.size() == 1 ){
    		
    		// Carga de Variaveis para serem atualizadas no sistema Trafegus
    		idMotCad= listaAcMotoristaParaTrafegus.get(0).getId();    		
    		cpfMot  = listaAcMotoristaParaTrafegus.get(0).getCpf().toString();
    		
    		if( listaAcMotoristaParaTrafegus.get(0).getNome() != null){
    			nomeMot = listaAcMotoristaParaTrafegus.get(0).getNome().toString();
    		}    			    		
    		if (listaAcMotoristaParaTrafegus.get(0).getRg() != null ){
    			rgMot   = listaAcMotoristaParaTrafegus.get(0).getRg().toString();
    		}    		    		
    		if ( listaAcMotoristaParaTrafegus.get(0).getCnh() != null){
    			cnhMot	= listaAcMotoristaParaTrafegus.get(0).getCnh().toString();
    		}    		
    		if( listaAcMotoristaParaTrafegus.get(0).getCategoriaCnh() != null){
    			cnhCategoriaMot  = listaAcMotoristaParaTrafegus.get(0).getCategoriaCnh().toString();    		
    		}    		
    		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 00:00:00");    		    	    		    		
    		if(listaAcMotoristaParaTrafegus.get(0).getDtVenctoCnh() != null){
    			cnhDtValidadeMot = dateFormat.format(listaAcMotoristaParaTrafegus.get(0).getDtVenctoCnh());    		
    		}    		
    		if (listaAcMotoristaParaTrafegus.get(0).getUfCnh() != null ){
    			cnhUfMot	= listaAcMotoristaParaTrafegus.get(0).getUfCnh().toString();
    		}    		
    		if (listaAcMotoristaParaTrafegus.get(0).getValidacaoCnh() != null ){
    			cnhSegurancaMot	= listaAcMotoristaParaTrafegus.get(0).getValidacaoCnh().toString();
    		}    		    		
    		if (listaAcMotoristaParaTrafegus.get(0).getUfRgEmissor() != null ){
    			ufRgMot = listaAcMotoristaParaTrafegus.get(0).getUfRgEmissor().toString();
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getDtNascimento() != null){
    			dtNascMot = dateFormat.format(listaAcMotoristaParaTrafegus.get(0).getDtNascimento());    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getDtPriEmissao() != null){
    			cnhDtPrimEmissao = dateFormat.format(listaAcMotoristaParaTrafegus.get(0).getDtPriEmissao());    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getNomePai() != null){
    			nomePaiMot = listaAcMotoristaParaTrafegus.get(0).getNomePai();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getNomeMae() != null){
    			nomeMaeMOT = listaAcMotoristaParaTrafegus.get(0).getNomeMae();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getLogradouro() != null){
    			logradouroMot =listaAcMotoristaParaTrafegus.get(0).getLogradouro();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getCep() != null){
    			cepMot = listaAcMotoristaParaTrafegus.get(0).getCep();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getNumEndereco() != null){
    			numEnderecoMot =  listaAcMotoristaParaTrafegus.get(0).getNumEndereco().toString();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getUfEndereco() != null){
    			ufEnderecoMot = listaAcMotoristaParaTrafegus.get(0).getUfEndereco();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getPaisEndereco() != null){
    			paisEnderecoMot = listaAcMotoristaParaTrafegus.get(0).getPaisEndereco();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getBairroEndereco() != null){
    			bairroEndereco = listaAcMotoristaParaTrafegus.get(0).getBairroEndereco();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getComplEndereco() != null){
    			complEndereco = listaAcMotoristaParaTrafegus.get(0).getComplEndereco();    		
    		}
    		if(listaAcMotoristaParaTrafegus.get(0).getCidadeEndereco() != null){
    			cidadeEndereco = listaAcMotoristaParaTrafegus.get(0).getCidadeEndereco();    		
    		}    		
    		// Envia dados para atualizar o sistema Trafegus via ws | put
    		boolean result = wsM.putMotorista(idMotoristaChp, cpfMot, nomeMot, rgMot, cnhMot, cnhCategoriaMot, cnhDtValidadeMot, 
    					cnhUfMot, cnhSegurancaMot, 
    					ufRgMot, dtNascMot, cnhDtPrimEmissao, nomePaiMot, nomeMaeMOT, logradouroMot, cepMot,  numEnderecoMot, 
    					ufEnderecoMot, paisEnderecoMot, bairroEndereco, complEndereco, cidadeEndereco);
    		 
    		 if (result == true){    			 
    			 // Atualiza o banco de dados motorista
    			 Motorista mot = motServ.findById(idMotCad);    			 
    			 mot.setDtAtualizaWs(new Date());
    			 mot.setDtVerificacaoWs(new Date());
    			 motServ.crud().update(mot);
    			 motServ.commit();
    		 }    		 
    	}    	
    }
    
    /**
     * @author Francis Bueno
     * @since 2021-10-29 14:02
     * @param idPlaca
     * @param placa
     * Método faz a pesquisa do veiculo no servidor da Chp. Caso exista o veiculo
     * será requisitado uma atualização do nosso dados ao sistema da chp.
     * Caso não exista será atualizado o nosso cadastro de motorista informando que 
     * os dados do veiculo foi checado no servidor da chp;
     */
    private void pesquisarVeiculoServidorChp(Integer idPlaca, String placa){
    	
    	boolean checarHifen = placa.contains("-");
    	String placaChecada = "";
    	
    	if ( checarHifen = true ){
    		placaChecada = placa.replace("-", "").toString().trim();
    	} else {
    		placaChecada = placa;
    	}
    	
    	VeiculoClient wsV = new VeiculoClient();
    	
    	 JSONObject objJsonVei = wsV.getVeiculo(placaChecada);
    	 
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
    
    /**
     * @author Francis Bueno
     * @since 2021-10-28 14:06
     * @param idPLaca
     * @param idMotoristaChp
	 * @param idVeiculoChp
     * Envia os dados do veiculo para ser atualizado no sistema Chp; 
     */
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
    				  + " veioid = " + idPlaca ;
    	    	
    	listaAcVeiculoParaTrafegus = em.createNativeQuery(query, "ListaVeiculoCadTMapping").getResultList();
    	
    	VeiculoClient wsV = new VeiculoClient();
    	
    	// Variaveis do método    	
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
    			Veiculo vei = veiculoService.findById(idVeiculoCad);
    			vei.setDtVerificacaoWs(new Date());
    			vei.setDtAtualizacaoWs(new Date());
    			veiculoService.crud().update(vei);
    			veiculoService.commit();
    		}
    		
    	}
    	
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


	public List<acMotoristaCadT> getListaAcMotoristaParaTrafegus() {
		return listaAcMotoristaParaTrafegus;
	}


	public List<acVeiculoCadT> getListaAcVeiculoParaTrafegus() {
		return listaAcVeiculoParaTrafegus;
	}


	public void setListaAcMotoristaParaTrafegus(List<acMotoristaCadT> listaAcMotoristaParaTrafegus) {
		this.listaAcMotoristaParaTrafegus = listaAcMotoristaParaTrafegus;
	}


	public void setListaAcVeiculoParaTrafegus(List<acVeiculoCadT> listaAcVeiculoParaTrafegus) {
		this.listaAcVeiculoParaTrafegus = listaAcVeiculoParaTrafegus;
	}
    
}
