package br.com.global5.manager.bean.areas;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.areas.AreaQry;
import br.com.global5.manager.model.contrato.Contrato;
import br.com.global5.manager.model.contrato.acContrato;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.cadastro.ContratoService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewAccessScoped
public class AreaMB implements Serializable {

	/**
	 *
	 */
	private static final Long serialVersionUID = 1L;
	private LazyDataModel<Area> areaList;
	private List<Area> lstAreas;
	private List<Area> filteredValue;
	private Integer id;
	private Area area;
	private Filter<Area> filter = new Filter<Area>(new Area());
	private Integer areaID;
	
	// Pesquisa
	private String nomePesq = "";
	
	private List<AreaQry> lstAreaQry;

	private List<acContrato> listaContratos;
	@Inject
	AreaService areaService;

	@Inject
	Crud<Area> areaCrud;

	@Inject
	CrudService<Area> areaCrudService;
	
	@Inject
	UsuarioService usuService;
	
	@Inject
	ContratoService contratoService;
	 
    @PostConstruct
    public void init() {
    	
        lstAreas = areaService.listAll();
   
    }

	public LazyDataModel<Area> getAreaList() {
		if( areaList == null ) {
			areaList = new LazyDataModel<Area>() {

				@SuppressWarnings("unchecked")
				@Override
				public List<Area> load(int first, int pageSize,
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
					List<Area> list = areaService.paginate(filter);
					setRowCount(areaService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Area getRowData(String key) {
					return areaService.findById(new Integer(key));
				}
			};

		}
		return areaList;
	}

	public void remove() {
		if( area != null && area.getId() != null) {
			areaService.remove(area);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Area " + area.getNome()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (area.getId() == null) {
			areaService.insert(area);
			msg = "Area " + area.getNome() + " criada com sucesso!";
		} else {
			areaService.update(area);
			msg = "Area " + area.getNome() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/areaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novas areas " + getId()
                            + " n??o pode ser carregada. Informe ao suporte t??cnico.",null));
        }
    }

	public void clear() {
		area = new Area();
		filter = new Filter<Area>(new Area());
		id = null;
		
		
	}

	public void findAreaById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Area ?? obrigat??rio");
		}
		area = areaCrud.get(id);

		if (area == null) {
			throw new BusinessException("??rea n??o foi encontrado pelo " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = areaService.getNome(query);
		return result;
	}

	public List<Area> listArea(String query) {
    	return areaService.listNome(query,2);
	}
	public List<Area> listFilial(String query) {
		return areaService.listNome(query,3);
	}


	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Area) event.getObject()).getId());

		findAreaById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/areaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Area " + getId()
							+ " n??o pode ser carregada. Informe ao suporte t??cnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/arealst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de ??reas " + getId()
							+ " n??o pode ser carregada. Informe ao suporte t??cnico.",null));
		}
	}
	

	public void btnPespCliente(){
		
		boolean temCriteriosPesq;
		
		try{
		
			EntityManager em = areaService.crud().getEntityManager();
			
			String parametrosSql = " ";
			
			try{
				// Nome do Cliente a ser pesquisado
				if( nomePesq == ""){
					temCriteriosPesq = false;
				} else {
					parametrosSql += " AND area_nome ~* '" + nomePesq + "' AND ";
					temCriteriosPesq = true;
				}
			} catch ( Exception e) {
				e.printStackTrace();
				temCriteriosPesq = false;
			}
		
			parametrosSql += " 0 = 0 " ;
			
			EntityManager emPes = usuService.crud().getEntityManager();
			
			/*"  where pesoid in (select area_pesoid_sacado from area where area_anvloid = 2 )  and   pes_pessoa_fisica = false "*/						
			
			String query = 	" Select area.areaoid as idcliente, area.area_nome as razao " +
					" from java.contrato, java.contrato_produto , java.area " +
					" where " +
					"      areaoid = con_areaoid and " +
					"      con_enumoid_status not in (650,767)  and " +
					"      conoid = conp_conoid and conp_produto_ativo is true and " +
					"      conp_prodoid = 22 and " +
					"      con_enumoid_produto_tipo = 16 "
					+ parametrosSql ;
		
	
		lstAreaQry =  emPes.createNativeQuery(query,"ListAreaClienteMapping").getResultList();
		
		Integer registrosArea = lstAreaQry.size();
		
		if(registrosArea == 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Nenhum registro encontrado." ));
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formPesqCliente:tablePesqCli");
		}
		try {
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formPesqCliente:tablePesqCli");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			temCriteriosPesq = false;
		}
		
	}

	/**
	 * @author Francis Bueno
	 * @param nome
	 * M??todo utilizado pela tela areaman.xhtml pela dialog dlgPesquisa
	 * Recebe o nome que sera pesquisado
	 */
	public void checarNomePesquisa(String nome){
		this.setNomePesq(nome);
	}
	
	

	public List<acContrato> buscarListaContratos(Integer nivel){
		
		List<acContrato> ListRecebidaB = null;
		
		if(nivel != null) {
			
			Integer idNivelRecebido = nivel;
									
			String nomePesquisa = "";
			
			if( !nomePesq.equals("")){
				nomePesquisa = nomePesq.toString();
			}
			
			if (nivel == 9) {
				
				pesquisarContratos();
				
				ListRecebidaB = getListaContratos();
				
			} 
			
			return ListRecebidaB;
			
		} else {
			
		  return ListRecebidaB=null;
		  
		}
		
	}



	public void pesquisarContratos(){

		boolean haveParameters = false;
		
		try{

			EntityManager em = areaService.crud().getEntityManager();
			
			String from = " ";
			String parameters = " ";
			
			if (nomePesq.isEmpty() != true) {
				parameters += " a.area_nome ILIKE '%" + nomePesq + "%' AND ";
				haveParameters = true;
			}

			
			try{
				
				String query = "select a.areaoid as id, a.areaoid as area, p.pes_documento1 as cnpj, a.area_nome as nome , "
						+ " 	(select  '(' || telddd || ')' || ' ' || telfone as telefone from java.telefone where teloid = p.pes_teloid) as telefone, "
						+ "  	coalesce((select enum_nome || ' - empresa: ' || con_empoid "
						+ "			from administrativo.enum_contrato_status, java.contrato "
						+ "				where con_enumoid_status = enumoid and con_areaoid = areaoid and con_enumoid_produto_tipo = 15 order by conoid desc limit 1),'Inexistente') as statusCadastro,"
						+ "  	coalesce((select enum_nome || ' - empresa: ' || con_empoid "
						+ "			from administrativo.enum_contrato_status, java.contrato "
						+ "				where con_enumoid_status = enumoid and con_areaoid = areaoid and con_enumoid_produto_tipo = 16 order by conoid desc limit 1),'Inexistente') as statusMonitoramento "
						+ " from java.area a, java.pessoa p " + from
						+ "		where " + parameters  
						+ "			  a.area_dt_exclusao is null and"
						+ "           a.area_pesoid_responsavel = p.pesoid and a.area_anvloid = 2";
				
				listaContratos = em.createNativeQuery(query, "ListaContratosMapping").getResultList();							
								
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			haveParameters = false;
		}
		
	}



	
	
    public List<Area> getLstAreas() {
        return lstAreas;
    }

    public void setLstAreas(List<Area> lstAreas) {
        this.lstAreas = lstAreas;
    }

    public void onRowUnselect(UnselectEvent event) {
		area = new Area();
	}

	public Filter<Area> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Area> filter) {
		this.filter = filter;
	}

	public List<Area> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Area> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static Long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setAreaList(LazyDataModel<Area> areaList) {
		this.areaList = areaList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public AreaService getAreaService() {
		return areaService;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	public Crud<Area> getAreaCrud() {
		return areaCrud;
	}

	public void setAreaCrud(Crud<Area> areaCrud) {
		this.areaCrud = areaCrud;
	}

	public CrudService<Area> getAreaCrudService() {
		return areaCrudService;
	}

	public void setAreaCrudService(CrudService<Area> areaCrudService) {
		this.areaCrudService = areaCrudService;
	}

    public Integer getAreaID() {
        return areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }

	public List<AreaQry> getLstAreaQry() {
		return lstAreaQry;
	}

	public void setLstAreaQry(List<AreaQry> lstAreaQry) {
		this.lstAreaQry = lstAreaQry;
	}

	public ContratoService getContratoService() {
		return contratoService;
	}

	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}

	public String getNomePesq() {
		return nomePesq;
	}

	public void setNomePesq(String nomePesq) {
		this.nomePesq = nomePesq;
	}

	public List<acContrato> getListaContratos() {
		return listaContratos;
	}

	public void setListaContratos(List<acContrato> listaContratos) {
		this.listaContratos = listaContratos;
	}

	
}
