package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.enumerator.TipoProduto;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.model.enums.ProdutoTipo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.cadastro.ProdutoService;
import br.com.global5.manager.service.enums.ProdutoTipoService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class ProdutoMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Produto> prodList;
	private List<Produto> filteredValue;
	private Integer id;
	private Integer numTipoProduto;

	private Produto produto;
	private Filter<Produto> filter = new Filter<Produto>(new Produto());
	private List<AreaFuncao> lstFuncao;
	private Usuario usuario;
	private List<ProdutoTipo> lstProdutoTipo;


	@Inject
	ProdutoService prodService;

	@Inject
	Crud<Produto> prodCrud;

	@Inject
	CrudService<Produto> prodCrudService;

	@Inject
	ProdutoTipoService prdTipoService;

	@PostConstruct
	public void init() {
		lstProdutoTipo = prdTipoService.crud().isNull("exclusao").listAll();
		clear();
	}

	public LazyDataModel<Produto> getProdList() {
		if( prodList == null ) {
			prodList = new LazyDataModel<Produto>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Produto> load(int first, int pageSize,
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
					List<Produto> list = prodService.paginate(filter);
					setRowCount(prodService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Produto getRowData(String key) {
					return prodService.findById(new Integer(key));
				}
			};

		}
		return prodList;
	}

	public void remove() {
		if( produto != null && produto.getId() != null) {
			prodService.remove(produto);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Produto " + produto.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (produto.getId() == null) {

			this.produto.setUsuarioCriacao(checkUsuario.valid());
			this.produto.setDtCriacao(new Date());
			this.produto.setUsuarioAlteracao(checkUsuario.valid());
			this.produto.setDtAlteracao(new Date());
			this.produto.setTipo(new ProdutoTipo(numTipoProduto));
			prodService.insert(produto);
			msg = "Cadastro da Produto " + produto.getNome() + " criado com sucesso!";
		} else {
			this.produto.setUsuarioAlteracao(checkUsuario.valid());
			this.produto.setDtAlteracao(new Date());
			//this.produto.setTipo(this.produto.getTipo());
			this.produto.setTipo(new ProdutoTipo(numTipoProduto));
			prodService.update(produto);
			msg = "Cadastro da Produto " + produto.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/produtoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir produtos " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		produto = new Produto();
		produto.setTipo(new ProdutoTipo());
		usuario = checkUsuario.valid();

		filter = new Filter<Produto>(new Produto());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Produto é obrigatório");
		}
		produto = prodCrud.get(id);
		if (produto == null) {
			throw new BusinessException("Produto não foi encontrado pelo id: " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = prodService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Produto) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/produtoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Produto " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}
	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/produtolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do cadastro de produtos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onTipoProduto(){
		this.numTipoProduto = this.produto.getTipo().getId();
	}

	public SelectItem[] getTipoValues() {
		SelectItem[] items = new SelectItem[TipoProduto.values().length];
		int i = 0;
		for(TipoProduto g: TipoProduto.values()) {
			items[i++] = new SelectItem(g, g.toValue());
		}
		return items;
	}

	public void onRowUnselect(UnselectEvent event) {
		produto = new Produto();
	}

	public List<ProdutoTipo> getLstProdutoTipo() {
		return lstProdutoTipo;
	}

	public void setLstProdutoTipo(List<ProdutoTipo> lstProdutoTipo) {
		this.lstProdutoTipo = lstProdutoTipo;
	}

	public ProdutoTipoService getPrdTipoService() {
		return prdTipoService;
	}

	public void setPrdTipoService(ProdutoTipoService prdTipoService) {
		this.prdTipoService = prdTipoService;
	}

	public Filter<Produto> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Produto> filter) {
		this.filter = filter;
	}

	public List<Produto> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Produto> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setProdList(LazyDataModel<Produto> prodList) {
		this.prodList = prodList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<AreaFuncao> getLstFuncao() {
		return lstFuncao;
	}

	public void setLstFuncao(List<AreaFuncao> lstFuncao) {
		this.lstFuncao = lstFuncao;
	}

	public ProdutoService getProdService() {
		return prodService;
	}

	public void setProdService(ProdutoService prodService) {
		this.prodService = prodService;
	}

	public Crud<Produto> getProdCrud() {
		return prodCrud;
	}

	public void setProdCrud(Crud<Produto> prodCrud) {
		this.prodCrud = prodCrud;
	}

	public CrudService<Produto> getProdCrudService() {
		return prodCrudService;
	}

	public void setProdCrudService(CrudService<Produto> prodCrudService) {
		this.prodCrudService = prodCrudService;
	}

	public Integer getNumTipoProduto() {
		return numTipoProduto;
	}

	public void setNumTipoProduto(Integer numTipoProduto) {
		this.numTipoProduto = numTipoProduto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
