package br.com.global5.manager.chamado.service;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.chamado.ChamadoResposta;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

public class ChamadoRespostaService extends CrudService<ChamadoResposta> {

    private static final long serialVersionUID = 201902271638L;

    @Override
    public Criteria configPagination(Filter<ChamadoResposta> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if (filter.getEntity() != null) {
            if(filter.getEntity().getMensagem() != null){
                crud().ilike("mensagem", filter.getEntity().getMensagem(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }

    @SuppressWarnings("unchecked")
    public List<String> getMensagem(String query) {
        return crud().criteria().ilike("mensagem", query, MatchMode.ANYWHERE)
                .projection(Projections.property("mensagem")).list();
    }
}
