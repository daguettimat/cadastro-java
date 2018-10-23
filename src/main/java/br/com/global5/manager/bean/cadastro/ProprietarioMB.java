package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.model.Filter;

import br.com.global5.infra.util.*;
import br.com.global5.manager.model.analise.acVeiculos;
import br.com.global5.manager.model.auxiliar.TipoEndereco;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Proprietario;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.TelefoneOrigem;
import br.com.global5.manager.model.enums.TelefoneTipo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.ect.Logradouro;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.cadastro.LocalizadorService;
import br.com.global5.manager.service.cadastro.ProprietarioService;
import br.com.global5.manager.service.cadastro.TelefoneService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.ect.LogradouroService;
import br.com.global5.manager.service.enums.TelefoneTipoService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class ProprietarioMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Proprietario> propList;
	private List<Proprietario> filteredValue;
	private List<Veiculo> lstVeiculo;
	private List<acVeiculos> lstFichas;
	private List<TelefoneTipo> lstTelefoneTipo;
	private List<Telefone> telIguaisReg;
	private Integer id;
	private Proprietario prop;
	private ViaCEPEndereco endereco;
	private ValidaBrasil validaBrasil;
	private Usuario usuario;
	private TelefoneTipo telefoneTipo;
	private Filter<Proprietario> filter = new Filter<Proprietario>(new Proprietario());
	private boolean foundEndereco;
	private boolean foundBairro;
	private boolean foundCidade;
	private boolean foundUF;

	private Integer numTipoFone = null;
	private Integer numEndOld = null;

	private boolean foundCEP = false;


	@Inject
	ProprietarioService propService;

	@Inject
	VeiculoService veiCadService;

	@Inject
	VeiculosService veiAnacService;

	@Inject
	LogradouroService logrService;

	@Inject
	LocalizadorService localizadorService;

	@Inject
	TelefoneTipoService telTipoService;

	@Inject
	TelefoneService telefoneService;

	@PostConstruct
	public void init() {

		clear();
		//foundEndereco = true;

	}

	public LazyDataModel<Proprietario> getProprietarioList() {
		if( propList == null ) {
			propList = new LazyDataModel<Proprietario>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Proprietario> load(int first, int pageSize,
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
					List<Proprietario> list = propService.paginate(filter);
					setRowCount(propService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Proprietario getRowData(String key) {
					return propService.findById(new Integer(key));
				}
			};

		}
		return propList;
	}

	public void remove() {
		if( prop != null && prop.getId() != null) {
			propService.remove(prop);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Proprietário " + prop.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}



	public void update() {
		String msg;
		String cpfCnpj = "";
		int telQtdReg = 0;
		Localizador locUp = new Localizador();

		//CPF/CNPJ ValidaBrasil
		if(prop.isNacional()){

			//CPF
			cpfCnpj = this.prop.getDocumento();

			cpfCnpj = (Normalizer.normalize(cpfCnpj, Normalizer.Form.NFD).replaceAll("[\\W]", ""));

			if(cpfCnpj.length() == 11){
				if(! validaBrasil.validateCPF(this.prop.getDocumento())){
					throw  new BusinessException("CPF Inválido!!! Por Favor confira e digite novamente!");
				}
			} else 	if(cpfCnpj.length() > 11 ){
				if(!validaBrasil.validateCNPJ(this.prop.getDocumento())){
					throw new BusinessException("CNPJ Inválido!!! Por Favor confira e digite novamente!");
				}
			} else {
				throw new BusinessException("Número inconclusivo para validação de CPF ou CNPJ!!! Por Favor confira e digite novamente!");
			}
		}

		//CEP Adicionado ou Alterado no cadastro - Realiza inclusão de dados na tabela localizador ou
		//Numero do Endereço alterado - Fazer Novo insert na tabela de Localização e Alterar cadastro
		if(foundCEP == true || this.getNumEndOld()!= null ){
			Localizador loc = new Localizador();

			//Registra Nova Localização do Proprietario
			loc.setUf(this.prop.getEndereco().getUf());
			loc.setCidade(this.prop.getEndereco().getCidade());
			loc.setBairro(this.prop.getEndereco().getBairro());
			if(this.prop.getEndereco().getTipoEndereco()!= null){
				loc.setTipoEndereco(this.prop.getEndereco().getTipoEndereco());
			}else{
				loc.setTipoEndereco(new TipoEndereco(62));
			}
			loc.setLogradouro(this.prop.getEndereco().getLogradouro());
			loc.setNumero(this.prop.getEndereco().getNumero());
			loc.setPais("BRASIL");
			loc.setCep(this.prop.getEndereco().getCep());
			localizadorService.crud().save(loc);

			//Objeto para upDate
			locUp = loc;

			//Limpa variavel find CEP Logradouro
			foundCEP=false;
		}

		Telefone telUpProprietario = new Telefone();

		telQtdReg = telefoneService.crud().
				eq("ddd", this.prop.getTelefone().getDdd()).and().
				eq("tipo", this.prop.getTelefone().getTipo()).and().
				eq("fone", this.prop.getTelefone().getFone()).count();

			//Telefone não encontrado será adicionado - Realiza inclusão de dados na tabela Telefone
		Telefone telC = new Telefone();

		if(telQtdReg == 0){

				telC.setDdd(this.prop.getTelefone().getDdd());
				if(this.getNumTipoFone() == null){
					telC.setTipo(this.prop.getTelefone().getTipo());
				}else{
					telC.setTipo(new TelefoneTipo(this.getNumTipoFone()));
				}

				telC.setFone(this.prop.getTelefone().getFone());
				telC.setOrigem(new TelefoneOrigem(71));
				telC.setDtCriacao(new Date());
				telefoneService.insert(telC);

				telUpProprietario = telC;

			}
			/*
			else {

			 // List<Telefone> fone = telefoneService.crud().eq("ddd", fichaCliente.getDdd()).and().eq("fone",fichaCliente.getTelefone()).list();
			List<Telefone> fone = telefoneService.crud().eq("ddd", this.prop.getTelefone().getDdd()).and().
					eq("tipo", this.prop.getTelefone().getTipo()).and().
					eq("fone",this.prop.getTelefone().getFone()).list();

			try {
				telC = fone.get(0);
                    if(telC != prop.getTelefone() ) {
                        prop.setTelefone(telC);
                    }

                } catch (Exception e) {}

			}
			*/
		//CADASTRO PROPRIETARIO - Id null realiza novo registro
		if (prop.getId() == null) {

			if(locUp.getId() != null){
				this.prop.setDocumento(cpfCnpj);
				this.prop.setUsuCriacao(checkUsuario.valid());
				this.prop.setDtCriacao(new Date());
				this.prop.setUsuAlteracao(checkUsuario.valid());
				this.prop.setDtAlteracao(new Date());
				this.prop.setEndereco(locUp);
				if(telUpProprietario.getId() != null){
					this.prop.setTelefone(telUpProprietario);
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Atenção", "Erro de persistência no endereço!" + " Informe a TI!"));
				return;
			}

			propService.crud().save(prop); //insert(prop);
			msg = "Proprietário " + prop.getNome() + " criado com sucesso!";

		} else {

			//Atualiza cadastro do Proprietario
			if (locUp.getId() != null){
				this.prop.setEndereco(locUp);
			}else{
				this.prop.getEndereco().setNumero(this.prop.getEndereco().getNumero());
			}
			if(this.getNumTipoFone()!=null){
				this.prop.setTelefone(telUpProprietario);
			}
			this.prop.setDocumento(cpfCnpj);
			this.prop.setUsuAlteracao(checkUsuario.valid());
			this.prop.setDtAlteracao(new Date());
			propService.update(prop);
			msg = "Proprietário " + prop.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/proprietarioman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir proprietários " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		prop = new Proprietario();

		prop.setEndereco( new Localizador());
		prop.setTelefone(new Telefone());
		prop.getTelefone().setTipo(new TelefoneTipo());
		usuario = checkUsuario.valid();
		endereco = new ViaCEPEndereco();

		filter = new Filter<Proprietario>(new Proprietario());
		lstTelefoneTipo = telTipoService.crud().isNull("exclusao").listAll();
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}
		prop = propService.crud().get(id);
		if (prop == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}

		lstVeiculo = veiCadService.crud().eq("proprietario.id", id).list();
		lstFichas = veiAnacService.crud().eq("proprietario.id", id).list();

	}

	public List<String> completeNome(String query) {
		List<String> result = propService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Proprietario) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/proprietarioman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Proprietários " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/proprietariolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista de proprietários " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onTipoTelefone(AjaxBehaviorEvent event){
		//this.numTipoFone = this.prop.getTelefone().getTipo().getId();
		this.setNumTipoFone(this.prop.getTelefone().getTipo().getId());
		if(this.getNumTipoFone()!=null){
			//
		}
	}

	public void onValueChange(AjaxBehaviorEvent event){
		//Cadastro Ativo - monitora edição do campo Número do endereço
		if(prop.getId()!=null) {

			//Objeto fichaProp (Dados antes da persistência)
			Proprietario fichaProp = propService.crud().eq("id", prop.getId()).find();

			//NumEndereço Anterior a alteração (valueOld)
			this.setNumEndOld(fichaProp.getEndereco().getNumero());

		}
	}

	public void BuscaCep(AjaxBehaviorEvent event){

		String cep = (String) ((UIOutput)event.getSource()).getValue();

		if(cep.length() == 0){
			return;
		}

		//Cadastro de CEPs - pesquisa no cadastro
		Logradouro logr = logrService.crud().eq("cep",cep.replace("-","")).find();

		if(logr == null){
			ViaCEPClient client = new ViaCEPClient();
			try {
				endereco = client.getEndereco(cep);

				prop.getEndereco().setUf(endereco.getUf().toUpperCase());
				prop.getEndereco().setCidade(endereco.getLocalidade().toUpperCase());
				prop.getEndereco().setBairro(endereco.getBairro().toUpperCase());
				prop.getEndereco().setLogradouro(endereco.getLogradouro().toUpperCase());

				foundCEP = true;

			} catch (IOException e){
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro","Não foi possível verificar" +
						" o cep via webservice. Informe ao suporte técnico."));
			}
		} else {
			foundCEP = true;

			if(prop.getEndereco().getLogradouro()!=null){
				foundEndereco = (prop.getEndereco().getLogradouro().length() != 0);
			} else {
				foundEndereco = true;
			}

			if(prop.getEndereco().getBairro() != null){
				foundBairro = (prop.getEndereco().getBairro().length() != 0);
			} else {
				foundBairro = true;
			}

			if(prop.getEndereco().getCidade() != null ){
				foundCidade = (prop.getEndereco().getCidade().length() != 0);
			} else {
				foundCidade = true;
			}

			if(prop.getEndereco().getUf() != null){
				foundUF = (prop.getEndereco().getUf().length() != 0);
			} else {
				foundUF = true;
			}

			prop.getEndereco().setUf(logr.getCidade().getUf().getSigla().toUpperCase());
			prop.getEndereco().setCidade(logr.getCidade().getNome().toUpperCase());
			prop.getEndereco().setBairro(logr.getIniBairro().getNome().toUpperCase());
			prop.getEndereco().setLogradouro(logr.getTipo().toUpperCase() + " " + logr.getNome().toUpperCase());

		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmProprietario:cepUFProprietario");
		context.update("frmProprietario:cepCidadeProprietario");
		context.update("frmProprietario:cepBairroProprietario");
		context.update("frmProprietario:cepEndProprietario");

	}


    public String flag(Integer id) {
        return AppUtils.pathFlag(id);
    }

	public void onRowUnselect(UnselectEvent event) {
		prop = new Proprietario();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setProprietarioList(LazyDataModel<Proprietario> propList) {
		this.propList = propList;
	}

	public List<Proprietario> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Proprietario> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proprietario getProprietario() {
		return prop;
	}

	public void setProprietario(Proprietario prop) {
		this.prop = prop;
	}

	public Filter<Proprietario> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Proprietario> filter) {
		this.filter = filter;
	}

	public ProprietarioService getProprietarioService() {
		return propService;
	}

	public void setProprietarioService(ProprietarioService propService) {
		this.propService = propService;
	}

	public LazyDataModel<Proprietario> getPropList() {
		return propList;
	}

	public void setPropList(LazyDataModel<Proprietario> propList) {
		this.propList = propList;
	}

	public Proprietario getProp() {
		return prop;
	}

	public void setProp(Proprietario prop) {
		this.prop = prop;
	}

	public boolean isFoundEndereco() {
		return foundEndereco;
	}

	public void setFoundEndereco(boolean foundEndereco) {
		this.foundEndereco = foundEndereco;
	}

	public ProprietarioService getPropService() {
		return propService;
	}

	public void setPropService(ProprietarioService propService) {
		this.propService = propService;
	}

    public List<Veiculo> getLstVeiculo() {
        return lstVeiculo;
    }

    public void setLstVeiculo(List<Veiculo> lstVeiculo) {
        this.lstVeiculo = lstVeiculo;
    }

    public List<acVeiculos> getLstFichas() {
        return lstFichas;
    }

    public void setLstFichas(List<acVeiculos> lstFichas) {
        this.lstFichas = lstFichas;
    }

    public VeiculoService getVeiCadService() {
        return veiCadService;
    }

    public void setVeiCadService(VeiculoService veiCadService) {
        this.veiCadService = veiCadService;
    }

    public VeiculosService getVeiAnacService() {
        return veiAnacService;
    }

    public void setVeiAnacService(VeiculosService veiAnacService) {
        this.veiAnacService = veiAnacService;
    }

	public ViaCEPEndereco getEndereco() {
		return endereco;
	}

	public void setEndereco(ViaCEPEndereco endereco) {
		this.endereco = endereco;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LogradouroService getLogrService() {
		return logrService;
	}

	public void setLogrService(LogradouroService logrService) {
		this.logrService = logrService;
	}

	public TelefoneTipoService getTelTipoService() {
		return telTipoService;
	}

	public void setTelTipoService(TelefoneTipoService telTipoService) {
		this.telTipoService = telTipoService;
	}

	public boolean isFoundCEP() {
		return foundCEP;
	}

	public void setFoundCEP(boolean foundCEP) {
		this.foundCEP = foundCEP;
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

	public Integer getNumTipoFone() {
		return numTipoFone;
	}

	public void setNumTipoFone(Integer numTipoFone) {
		this.numTipoFone = numTipoFone;
	}

	public LocalizadorService getLocalizadorService() {
		return localizadorService;
	}

	public void setLocalizadorService(LocalizadorService localizadorService) {
		this.localizadorService = localizadorService;
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

	public List<Telefone> getTelIguaisReg() {
		return telIguaisReg;
	}

	public void setTelIguaisReg(List<Telefone> telIguaisReg) {
		this.telIguaisReg = telIguaisReg;
	}

	public Integer getNumEndOld() {
		return numEndOld;
	}

	public void setNumEndOld(Integer numEndOld) {
		this.numEndOld = numEndOld;
	}
}
