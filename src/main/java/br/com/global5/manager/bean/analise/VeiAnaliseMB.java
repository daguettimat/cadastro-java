package br.com.global5.manager.bean.analise;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.acVeiculos;
import br.com.global5.manager.model.enums.ANTTSituacao;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.ReferenciasTipo;
import br.com.global5.manager.model.enums.TipoParte;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.enums.ANTTService;
import br.com.global5.manager.service.enums.ReferenciasAvaliacaoService;
import br.com.global5.manager.service.enums.TipoParteService;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Hibernate;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class VeiAnaliseMB implements Serializable {

	private static final long serialVersionUID = 1L;

    @Inject
    TipoParteService refTipoService;

    @Inject
    private VeiculosService veiService;

    private acVeiculos veiculo;

    @Inject
    ReferenciasAvaliacaoService refService;

    @Inject
    ReferenciasTipo referenciasTipo;

    @Inject
    ANTTService anttService;

    private List<acVeiculos> lstVeiculos;
    private List<TipoParte> lstRefTipo;
    private List<ReferenciasAvaliacao> lstReferencias;
    private List<ANTTSituacao> lstAnttSituacao;

    private Map<String,Object> options;
    private Integer id;
    private Integer idCadastro;
    private Integer countVeiculos;

    //
    // Combos
    //

    private Integer anoFabricacao;
    private Integer antt;
    private Integer anttSituacao;
    private Integer renavam;
    private Integer documentoProprietario;
    private Integer documentoArrendatario;
    private Integer tipoParte;
    private Integer dtVDocumento;

    //
    // Maps
    //

    private MapModel geoModel;
    private MapModel revGeoModel;

    private String centerGeoMap = "-15.7801, -54.9292";
    private String zoomMap = "2";

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

        lstVeiculos = new ArrayList<acVeiculos>();
        lstReferencias = refService.listAll();

        lstRefTipo = refTipoService.crud().listAll();

        lstAnttSituacao = anttService.crud().listAll();

        veiculo = new acVeiculos();

        geoModel = new DefaultMapModel();
        zoomMap = "2";

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

	public String flag(Integer idFlag) {

	    return AppUtils.pathFlag(idFlag);
    }

	public void clear() {

		id = null;
	}

	public void findByCadastro(Integer id, Integer idCadastro) {
		if (id == null) {
			throw new BusinessException("O id da Analise Cadastral é obrigatório");
		}

		System.out.println("acCadastro ID :" + idCadastro + " ID Ficha: " + id);

		veiculo = veiService.crud().eq("acCadastro.id", idCadastro).and().eq("id", id).find();
		if (veiculo == null) {
			throw new BusinessException("Análise Cadastral não foi encontrado pelo id: " + id);
		}

        Hibernate.initialize(veiculo.getVeiculo());
        Hibernate.initialize(veiculo.getVeiculo().getMarca());
        Hibernate.initialize(veiculo.getVeiculo().getModelo());
        Hibernate.initialize(veiculo.getVeiculo().getProprietario());
        Hibernate.initialize(veiculo.getTipo());

        anoFabricacao = veiculo.getAno().getId();
        antt = veiculo.getAntt().getId();
        anttSituacao = veiculo.getAnttSituacao().getId();
        renavam = veiculo.getRenavam().getId();
        documentoProprietario = veiculo.getDocumentoProprietario().getId();
        documentoArrendatario = veiculo.getDocumentoArrendatario().getId();
        tipoParte = veiculo.getTipo().getId();
        dtVDocumento = veiculo.getEmissaoDocumentoAvaliacao().getId();


	}

	public String nomeByIdCadastro(Integer id,  Integer item) {

        acVeiculos veiCad  = selectByCadastro(id, item);

        if( veiCad == null ) {
            return "";
        }

        return veiCad.getVeiculo().getPlaca() + " " +
               veiCad.getVeiculo().getModelo().getNome();


    }

    public acVeiculos selectByCadastro(Integer id, Integer idx) {
        try {
            if (id == null) {
                Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                if( requestParams.get("id") != null ) {
                    id = Integer.valueOf(requestParams.get("id"));
                } else {
                    id = this.id;
                }

            }
            veiculo = veiService.crud().eq("acCadastro.id", id).list().get(idx);
            Hibernate.initialize(veiculo.getVeiculo());
            Hibernate.initialize(veiculo.getAcCadastro());

            return veiculo;

        } catch (Exception e) {

//            FacesContext.getCurrentInstance().addMessage(
//                    null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!","Lista de Veículos na Análise Cadastral " + getId()
//                            + " não pode ser carregada. Informe ao suporte técnico."));
        }

        return null;
    }

    public void loadScreen(Integer id, Integer idx) {

        selectByCadastro(id, idx);

        findByCadastro(veiculo.getId(),veiculo.getAcCadastro().getId());

        if (lstVeiculos == null) {
            throw new BusinessException("Lista de Veículos na Análise Cadastral não foi encontrado pelo id: " + id);
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseVeiculo.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!","Lista de Veículos na Análise Cadastral " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico."));
        }
    }

    public void updateStatusGeral() {
        List<Integer> veiStatus = new ArrayList<>();
        veiStatus.add(0, anoFabricacao);
        veiStatus.add(1, antt);
        veiStatus.add(2, renavam);
        veiStatus.add(3, documentoArrendatario);
        veiStatus.add(4, documentoProprietario);

        veiculo.setStatusGeral(refService.crud().get(AppUtils.statusRecord(veiStatus)));

    }

    public void update() {
        try {
            veiculo.setAno(refService.crud().get(anoFabricacao));
            veiculo.setAntt(refService.crud().get(antt));
            veiculo.setAnttSituacao(anttService.crud().get(anttSituacao));
            veiculo.setRenavam(refService.crud().get(renavam));
            veiculo.setDocumentoArrendatario(refService.crud().get(documentoArrendatario));
            veiculo.setDocumentoProprietario(refService.crud().get(documentoProprietario));
            veiculo.setEmissaoDocumentoAvaliacao(refService.crud().get(dtVDocumento));
            veiculo.setTipo(refTipoService.crud().get(tipoParte));

            updateStatusGeral();
            veiService.crud().update(veiculo);

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!","Análise Cadastral (Veículo)" + getId()
                            + " não pode ser salva. Informe ao suporte técnico."));
        }
    }

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/analiseMenu.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!","Lista do cadastro de motoristas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico."));
		}
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public VeiculosService getVeiService() {
        return veiService;
    }

    public void setVeiService(VeiculosService veiService) {
        this.veiService = veiService;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<acVeiculos> getLstVeiculos() {
        return lstVeiculos;
    }

    public void setLstVeiculos(List<acVeiculos> lstVeiculos) {
        this.lstVeiculos = lstVeiculos;
    }

    public Integer getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(Integer idCadastro) {
        this.idCadastro = idCadastro;
    }

    public Integer getCountVeiculos() {
        return countVeiculos;
    }

    public void setCountVeiculos(Integer countVeiculos) {
        this.countVeiculos = countVeiculos;
    }

    public acVeiculos getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(acVeiculos veiculo) {
        this.veiculo = veiculo;
    }

    public TipoParteService getRefTipoService() {
        return refTipoService;
    }

    public void setRefTipoService(TipoParteService refTipoService) {
        this.refTipoService = refTipoService;
    }

    public List<TipoParte> getLstRefTipo() {
        return lstRefTipo;
    }

    public void setLstRefTipo(List<TipoParte> lstRefTipo) {
        this.lstRefTipo = lstRefTipo;
    }

    public ReferenciasAvaliacaoService getRefService() {
        return refService;
    }

    public void setRefService(ReferenciasAvaliacaoService refService) {
        this.refService = refService;
    }

    public List<ReferenciasAvaliacao> getLstReferencias() {
        return lstReferencias;
    }

    public void setLstReferencias(List<ReferenciasAvaliacao> lstReferencias) {
        this.lstReferencias = lstReferencias;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Integer getAntt() {
        return antt;
    }

    public void setAntt(Integer antt) {
        this.antt = antt;
    }

    public Integer getAnttSituacao() {
        return anttSituacao;
    }

    public void setAnttSituacao(Integer anttSituacao) {
        this.anttSituacao = anttSituacao;
    }

    public Integer getRenavam() {
        return renavam;
    }

    public void setRenavam(Integer renavam) {
        this.renavam = renavam;
    }

    public Integer getDocumentoProprietario() {
        return documentoProprietario;
    }

    public void setDocumentoProprietario(Integer documentoProprietario) {
        this.documentoProprietario = documentoProprietario;
    }

    public Integer getDocumentoArrendatario() {
        return documentoArrendatario;
    }

    public void setDocumentoArrendatario(Integer documentoArrendatario) {
        this.documentoArrendatario = documentoArrendatario;
    }

    public Integer getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(Integer tipoParte) {
        this.tipoParte = tipoParte;
    }

    public ReferenciasTipo getReferenciasTipo() {
        return referenciasTipo;
    }

    public void setReferenciasTipo(ReferenciasTipo referenciasTipo) {
        this.referenciasTipo = referenciasTipo;
    }

    public ANTTService getAnttService() {
        return anttService;
    }

    public void setAnttService(ANTTService anttService) {
        this.anttService = anttService;
    }

    public List<ANTTSituacao> getLstAnttSituacao() {
        return lstAnttSituacao;
    }

    public void setLstAnttSituacao(List<ANTTSituacao> lstAnttSituacao) {
        this.lstAnttSituacao = lstAnttSituacao;
    }

    public Integer getDtVDocumento() {
        return dtVDocumento;
    }

    public void setDtVDocumento(Integer dtVDocumento) {
        this.dtVDocumento = dtVDocumento;
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

    public String getCenterGeoMap() {
        return centerGeoMap;
    }

    public void setCenterGeoMap(String centerGeoMap) {
        this.centerGeoMap = centerGeoMap;
    }

    public String getZoomMap() {
        return zoomMap;
    }

    public void setZoomMap(String zoomMap) {
        this.zoomMap = zoomMap;
    }


}
