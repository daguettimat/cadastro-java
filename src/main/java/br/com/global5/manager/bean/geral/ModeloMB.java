package br.com.global5.manager.bean.geral;


import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.ConectionGrLog;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areacliente.Virtual;
import br.com.global5.manager.model.cadastro.Marca;
import br.com.global5.manager.model.cadastro.Modelo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.areacliente.VirtualService;
import br.com.global5.manager.service.cadastro.MarcaService;
import br.com.global5.manager.service.cadastro.ModeloService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.persistence.StoredProcedureQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class ModeloMB implements Serializable {

	/**
	 *
	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Modelo> modeloList;
	private List<Modelo> filteredValue;
	private List<Marca> lstMarca;
	private Integer id;
	private Modelo modelo;
	private Filter<Modelo> filter = new Filter<Modelo>(new Modelo());
	private Usuario usuario;

	
	// Teste 
	private List<Virtual> tableVirtual;	
		@Inject
		VirtualService virtualService;
		@Inject
		private Crud<Virtual> virtualCrud;
		private Date dtPesquisa = null;
		
	@Inject
	ModeloService modeloService;

	@Inject
	Crud<Modelo> modeloCrud;

	@Inject
	CrudService<Modelo> modeloCrudService;

	@Inject
	MarcaService marcaService;

	@PostConstruct
	public void init() {

		clear();

		lstMarca = marcaService.listAll();

	}

	public LazyDataModel<Modelo> getModeloList() {
		if( modeloList == null ) {
			modeloList = new LazyDataModel<Modelo>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Modelo> load(int first, int pageSize,
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
					List<Modelo> list = modeloService.paginate(filter);
					setRowCount(modeloService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Modelo getRowData(String key) {
					return modeloService.findById(new Integer(key));
				}
			};

		}
		return modeloList;
	}

	public void remove() {
		if( modelo != null && modelo.getId() != null) {
			modeloService.remove(modelo);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Modelo " + modelo.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (modelo.getId() == null) {
			this.modelo.setMarca(new Marca (this.modelo.getMarca().getId()));
			this.modelo.setUsuCriacao(checkUsuario.valid());
			this.modelo.setDtCriacao(new Date());
			this.modelo.setUsuAlteracao(checkUsuario.valid());
			this.modelo.setDtAlteracao(new Date());
			modeloService.insert(modelo);
			msg = "Cadastro do Modelo " + modelo.getNome() + " criado com sucesso!";
		} else {
			this.modelo.setMarca(new Marca (this.modelo.getMarca().getId()));
			this.modelo.setUsuAlteracao(checkUsuario.valid());
			this.modelo.setDtAlteracao(new Date());
			modeloService.update(modelo);

			msg = "Cadastro do Modelo " + modelo.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/modeloman.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir modelos " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}
	}

	public void clear() {
		modelo = new Modelo();
		modelo.setMarca(new Marca());
		usuario = checkUsuario.valid();
		filter = new Filter<Modelo>(new Modelo());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id da Modelo é obrigatório");
		}
		modelo = modeloCrud.get(id);
		if (modelo == null) {
			throw new BusinessException("Modelo não foi encontrado pelo id: " + id);
		}
	}
	
	public void testeGR() {
//		"jdbc:sqlserver://{host}[:{port}][;databaseName={database}]"
		//"jdbc:sqlserver://localhost:1433;" +	"databaseName=AdventureWorks;integratedSecurity=true;
		
		ConectionGrLog.conectar();
			
	}
	
	public void testaCertificado(){
		try { 
			
			String caminhoDoCertificadoDaEmpresa = "/var/www/certificado/pilotoIss.pfx";
			String senhaDoCertificado = "teste123";
			
			InputStream entrada = new FileInputStream(caminhoDoCertificadoDaEmpresa);
			KeyStore ks = KeyStore.getInstance("pkcs12");
			
			
			try{
				
				ks.load(entrada, senhaDoCertificado.toCharArray());
				
			} catch (Exception e) {
					
				throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido!");	
				
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public List<String> completeNome(String query) {
		List<String> result = modeloService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Modelo) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/modeloman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Modelo " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/modelolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do cadastro de modelos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	// TESTE
	public void acesso(){
		
		String nomeProcedureChamada = "usuario_mensal_monitoramento";

        
		StoredProcedureQuery queryPv = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamada);
		
		queryPv.setParameter("dt_mes", this.getDtPesquisa());
		queryPv.setParameter("id_area"  , 1947);
		
		queryPv.execute();
		
		String text = queryPv.getResultList().toString();
		String textb = queryPv.getResultList().get(0).toString();
		
		JSONObject obj = new  JSONObject(textb);
		
		obj.opt("consumo_viagens");
		
		JSONArray arrayJson = obj.getJSONArray("consumo_viagens");
	
		//JSONArray objJsonArray 
		
		
		String b = "a";
		String a = b;
		
		
	}
	
	
	public void onRowUnselect(UnselectEvent event) {
		modelo = new Modelo();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setModeloList(LazyDataModel<Modelo> modeloList) {
		this.modeloList = modeloList;
	}

	public List<Modelo> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Modelo> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public Filter<Modelo> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Modelo> filter) {
		this.filter = filter;
	}

	public ModeloService getModeloService() {
		return modeloService;
	}

	public void setModeloService(ModeloService modeloService) {
		this.modeloService = modeloService;
	}

	public Crud<Modelo> getModeloCrud() {
		return modeloCrud;
	}

	public void setModeloCrud(Crud<Modelo> modeloCrud) {
		this.modeloCrud = modeloCrud;
	}

	public CrudService<Modelo> getModeloCrudService() {
		return modeloCrudService;
	}

	public void setModeloCrudService(CrudService<Modelo> modeloCrudService) {
		this.modeloCrudService = modeloCrudService;
	}

	public List<Marca> getLstMarca() {
		return lstMarca;
	}

	public void setLstMarca(List<Marca> lstMarca) {
		this.lstMarca = lstMarca;
	}

	public MarcaService getMarcaService() {
		return marcaService;
	}

	public void setMarcaService(MarcaService marcaService) {
		this.marcaService = marcaService;
	}

	public List<Virtual> getTableVirtual() {
		return tableVirtual;
	}

	public void setTableVirtual(List<Virtual> tableVirtual) {
		this.tableVirtual = tableVirtual;
	}

	public Date getDtPesquisa() {
		return dtPesquisa;
	}

	public void setDtPesquisa(Date dtPesquisa) {
		this.dtPesquisa = dtPesquisa;
	}
	
	
}
