package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.manager.service.geral.MercadoriaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class MercadoriaMB implements Serializable {

    /**
     *
     */
    private static final Long serialVersionUID = 1L;
    private LazyDataModel<Mercadoria> mList;
    private List<Mercadoria> filteredValue;
    private Integer id;
    private Mercadoria mercadoria;
    private Filter<Mercadoria> filter = new Filter<Mercadoria>(new Mercadoria());

    @Inject
    MercadoriaService mService;

    @Inject
    Crud<Mercadoria> mCrud;

    @Inject
    CrudService<Mercadoria> mCrudService;

    public LazyDataModel<Mercadoria> getmList() {
        if (mList == null) {
            mList = new LazyDataModel<Mercadoria>() {


                @SuppressWarnings("unchecked")
                @Override
                public List<Mercadoria> load(int first, int pageSize,
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
                    List<Mercadoria> list = mService.paginate(filter);
                    setRowCount(mService.count(filter));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public Mercadoria getRowData(String key) {
                    return mService.findById(new Integer(key));
                }
            };

        }
        return mList;
    }

    public void remove() {
        if (mercadoria != null && mercadoria.getId() != null) {
            mService.remove(mercadoria);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Mercadoria " + mercadoria.getDescricao()
                            + " removida com sucesso", null));
            clear();
        }
    }

    public void update() {
        String msg;
        if (mercadoria.getId() == null) {
            mService.insert(mercadoria);
            msg = "Mercadoria " + mercadoria.getDescricao() + " criada com sucesso!";
        } else {
            mService.update(mercadoria);
            msg = "Mercadoria " + mercadoria.getDescricao() + " alterada com sucesso!";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));


        //clear();// reload car list
    }

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../man/mercadoria.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novas mercadorias " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }


    public void clear() {
        mercadoria = new Mercadoria();
        filter = new Filter<Mercadoria>(new Mercadoria());
        id = null;
    }

    public void findMercadoriaById(Integer id) {
        if (id == null) {
            throw new BusinessException("O id do Mercadoria é obrigatório");
        }
        mercadoria = mCrud.get(id);
        if (mercadoria == null) {
            throw new BusinessException("Mercadoria não foi encontrado pelo " + id);
        }
    }

    public List<String> completeDescricao(String query) {
        List<String> result = mService.getDescricao(query);
        return result;
    }

    public void onRowSelect(SelectEvent event) {
        setId(((Mercadoria) event.getObject()).getId());
        findMercadoriaById(getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../man/mercadoria.xhtml?id=" + getId());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Mercadoria " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }

    }

    public void btnBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../list/mercadoria.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista do Mercadoria " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        mercadoria = new Mercadoria();
    }

    public Filter<Mercadoria> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Mercadoria> filter) {
        this.filter = filter;
    }

    public List<Mercadoria> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Mercadoria> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setmList(LazyDataModel<Mercadoria> mList) {
        this.mList = mList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mercadoria getMercadoria() {
        return mercadoria;
    }

    public void setMercadoria(Mercadoria mercadoria) {
        this.mercadoria = mercadoria;
    }

    public MercadoriaService getmService() {
        return mService;
    }

    public void setmService(MercadoriaService mService) {
        this.mService = mService;
    }

    public Crud<Mercadoria> getmCrud() {
        return mCrud;
    }

    public void setmCrud(Crud<Mercadoria> mCrud) {
        this.mCrud = mCrud;
    }

    public CrudService<Mercadoria> getmCrudService() {
        return mCrudService;
    }

    public void setmCrudService(CrudService<Mercadoria> mCrudService) {
        this.mCrudService = mCrudService;
    }
}
