package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.ValidaBrasil;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.analise.acVeiculos;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.manager.model.cadastro.Marca;
import br.com.global5.manager.model.cadastro.Modelo;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.VeiculoCategoria;
import br.com.global5.manager.model.enums.VeiculoTipo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.auxiliar.CorService;
import br.com.global5.manager.service.cadastro.MarcaService;
import br.com.global5.manager.service.cadastro.ModeloService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.enums.VeiculoCategoriaService;
import br.com.global5.manager.service.enums.VeiculoTipoService;
import br.com.global5.template.exception.BusinessException;
import jdk.nashorn.internal.runtime.ECMAErrors;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
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
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Named
@ViewAccessScoped
public class VeiculoMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Veiculo> veiculoList;

	private Usuario usuario;

	private List<Modelo> lstModelo;
	private List<Marca> lstMarca;
    private List<Cor> lstCor;
    private List<VeiculoTipo> lstTipoVeiculo;
    private List<VeiculoCategoria> lstCategoriaVeiculo;

    private List<acVeiculos> lstacVeiculos;

	private List<Veiculo> filteredValue;
	private Integer id;
	private Integer numMarca = null;
	private Integer numModelo = null;
	private Integer numCor = null;
	private Integer numTipo = null;
	private boolean edit;

	private Veiculo veiculo;
	private Filter<Veiculo> filter = new Filter<Veiculo>(new Veiculo());
	
	//Variaveis para SelectItens
	private Integer numCodModelo;
	

	@Inject
	VeiculoService veiculoService;

	@Inject
    ModeloService modeloService;

	@Inject
    MarcaService marcaService;

	@Inject
    CorService corService;

	@Inject
    VeiculoTipoService veiculoTipoService;

	@Inject
    VeiculoCategoriaService veiculoCategoriaService;

	@Inject
    VeiculosService veiculosService;

	@Inject
	Crud<Veiculo> veiculoCrud;

	@Inject
	CrudService<Veiculo> veiculoCrudService;

	@PostConstruct
	public void init() {
		clear();

        lstMarca = marcaService.crud().listAll();
        lstCor = corService.crud().listAll();
        lstTipoVeiculo = veiculoTipoService.crud().listAll();
        lstCategoriaVeiculo = veiculoCategoriaService.crud().listAll();
        numCodModelo = null;

	}

	public LazyDataModel<Veiculo> getVeiculoList() {
		if( veiculoList == null ) {
			veiculoList = new LazyDataModel<Veiculo>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Veiculo> load(int first, int pageSize,
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
					List<Veiculo> list = veiculoService.paginate(filter);
					setRowCount(veiculoService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Veiculo getRowData(String key) {
					return veiculoService.findById(new Integer(key));
				}
			};

		}
		return veiculoList;
	}

	public void remove() {
		if( veiculo != null && veiculo.getId() != null) {
			veiculoService.remove(veiculo);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Veículo " + veiculo.getPlaca()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void onModelo(AjaxBehaviorEvent event){
		this.setNumModelo(this.veiculo.getModelo().getId());
	}
	
	public void update() {
		String msg;

		if (veiculo.getId() == null) {
			veiculo.setTipo(this.veiculo.getTipo());
			veiculoService.insert(veiculo);
			msg = "Veículo " + veiculo.getPlaca() + " criado com sucesso!";
		} else {
		    if(this.getNumMarca()!=null){
                this.veiculo.setMarca(new Marca(this.getNumMarca()));
            }
		    if(this.getNumModelo()!=null){
                this.veiculo.setModelo(new Modelo(this.getNumModelo()));
            }
		    if(this.getNumCor()!=null){
                this.veiculo.setCor(new Cor(this.getNumCor()));
            }
            if(this.getNumTipo()!=null){
                this.veiculo.setTipo(new VeiculoTipo(this.getNumTipo()));
            }

			veiculoService.update(veiculo);
			msg = "Veículo " + veiculo.getPlaca() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();

            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir veículo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		veiculo = new Veiculo();
		filter = new Filter<Veiculo>(new Veiculo());

		veiculo.setMarca(new Marca());
		veiculo.setModelo(new Modelo());
		veiculo.setCor(new Cor());
		veiculo.setTipo(new VeiculoTipo());

		usuario = checkUsuario.valid();

		id = null;
		edit = true;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}

		veiculo = veiculoCrud.get(id);
		if (veiculo == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}

        Hibernate.initialize(veiculo.getProprietario());
        Hibernate.initialize(veiculo.getProprietario().getEndereco());
        Hibernate.initialize(veiculo.getProprietario().getTelefone());

        try {
            Hibernate.initialize(veiculo.getArrendatario());
            Hibernate.initialize(veiculo.getArrendatario().getEndereco());
            Hibernate.initialize(veiculo.getArrendatario().getTelefone());
        } catch (Exception e) {}

        Hibernate.initialize(veiculo.getMarca());
        Hibernate.initialize(veiculo.getModelo());

		lstModelo = modeloService.crud().eq("marca.id", veiculo.getMarca().getId()).list();

        lstacVeiculos = veiculosService.crud().eq("veiculo.id",veiculo.getId()).list();
        Collections.sort(lstacVeiculos, new Comparator<acVeiculos>() {

            @Override
            public int compare(acVeiculos o1, acVeiculos o2) {

                return -o1.getAcCadastro().getId().compareTo(o2.getAcCadastro().getId());

            }
        });

//
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("formVeiculo:veiModelo");
	}

    public void showFicha(Integer id) {
        this.id = id;
        edit = false;
        findById(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Veículo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }

    }

	public Veiculo findPlaca(String placa) {
		veiculo = veiculoCrud.find().placa(placa);
		if( veiculo != null) {
			return veiculo;
		} else {
			return null;
		}
	}



    public void findModelo(AjaxBehaviorEvent event) {

        Integer marca = (Integer) ((UIOutput)event.getSource()).getValue();
        
        this.setNumMarca(marca);

        lstModelo = new ArrayList<Modelo>();

        lstModelo = modeloService.crud().criteria().eq("marca.id", marca)
                .list();

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formVeiculo:veiModelo");

    }
    
    public void onCor(){this.setNumCor(this.veiculo.getCor().getId());}
    public void onTipo(){this.setNumTipo(this.veiculo.getTipo().getId());}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Veiculo) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Veículos " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/veiculolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista de veículos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		veiculo = new Veiculo();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setVeiculoList(LazyDataModel<Veiculo> veiculoList) {
		this.veiculoList = veiculoList;
	}

	public List<Veiculo> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Veiculo> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Filter<Veiculo> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Veiculo> filter) {
		this.filter = filter;
	}

	public VeiculoService getVeiculoService() {
		return veiculoService;
	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	public Crud<Veiculo> getVeiculoCrud() {
		return veiculoCrud;
	}

	public void setVeiculoCrud(Crud<Veiculo> veiculoCrud) {
		this.veiculoCrud = veiculoCrud;
	}

	public CrudService<Veiculo> getVeiculoCrudService() {
		return veiculoCrudService;
	}

	public void setVeiculoCrudService(CrudService<Veiculo> veiculoCrudService) {
		this.veiculoCrudService = veiculoCrudService;
	}

    public List<Modelo> getLstModelo() {
        return lstModelo;
    }

    public void setLstModelo(List<Modelo> lstModelo) {
        this.lstModelo = lstModelo;
    }

    public List<Marca> getLstMarca() {
        return lstMarca;
    }

    public void setLstMarca(List<Marca> lstMarca) {
        this.lstMarca = lstMarca;
    }

    public List<Cor> getLstCor() {
        return lstCor;
    }

    public void setLstCor(List<Cor> lstCor) {
        this.lstCor = lstCor;
    }

    public List<VeiculoTipo> getLstTipoVeiculo() {
        return lstTipoVeiculo;
    }

    public void setLstTipoVeiculo(List<VeiculoTipo> lstTipoVeiculo) {
        this.lstTipoVeiculo = lstTipoVeiculo;
    }

    public List<VeiculoCategoria> getLstCategoriaVeiculo() {
        return lstCategoriaVeiculo;
    }

    public void setLstCategoriaVeiculo(List<VeiculoCategoria> lstCategoriaVeiculo) {
        this.lstCategoriaVeiculo = lstCategoriaVeiculo;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }


	public ModeloService getModeloService() {
        return modeloService;
    }

    public void setModeloService(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    public MarcaService getMarcaService() {
        return marcaService;
    }

    public void setMarcaService(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    public CorService getCorService() {
        return corService;
    }

    public void setCorService(CorService corService) {
        this.corService = corService;
    }

    public VeiculoTipoService getVeiculoTipoService() {
        return veiculoTipoService;
    }

    public void setVeiculoTipoService(VeiculoTipoService veiculoTipoService) {
        this.veiculoTipoService = veiculoTipoService;
    }

    public VeiculoCategoriaService getVeiculoCategoriaService() {
        return veiculoCategoriaService;
    }

    public void setVeiculoCategoriaService(VeiculoCategoriaService veiculoCategoriaService) {
        this.veiculoCategoriaService = veiculoCategoriaService;
    }

    public List<acVeiculos> getLstacVeiculos() {
        return lstacVeiculos;
    }

    public void setLstacVeiculos(List<acVeiculos> lstacVeiculos) {
        this.lstacVeiculos = lstacVeiculos;
    }

    public VeiculosService getVeiculosService() {
        return veiculosService;
    }

    public void setVeiculosService(VeiculosService veiculosService) {
        this.veiculosService = veiculosService;
    }

	public Integer getNumCodModelo() {
		return numCodModelo;
	}

	public void setNumCodModelo(Integer numCodModelo) {
		this.numCodModelo = numCodModelo;
	}

	public Integer getNumMarca() {
		return numMarca;
	}

	public void setNumMarca(Integer numMarca) {
		this.numMarca = numMarca;
	}

	public Integer getNumModelo() {
		return numModelo;
	}

	public void setNumModelo(Integer numModelo) {
		this.numModelo = numModelo;
	}

	public Integer getNumCor() {
		return numCor;
	}

	public void setNumCor(Integer numCor) {
		this.numCor = numCor;
	}

	public Integer getNumTipo() {
		return numTipo;
	}

	public void setNumTipo(Integer numTipo) {
		this.numTipo = numTipo;
	}

    
}
