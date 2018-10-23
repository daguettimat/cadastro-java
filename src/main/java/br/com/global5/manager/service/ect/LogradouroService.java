package br.com.global5.manager.service.ect;


import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.infra.util.ViaCEPClient;
import br.com.global5.infra.util.ViaCEPEndereco;
import br.com.global5.manager.model.ect.*;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

/**
 * LogradouroService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class LogradouroService extends CrudService<Logradouro> {

    @Inject
    CidadeService cidadeService;

    @Inject
    UFService ufService;

    @Inject
    BairroService baiService;

    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Logradouro> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if (filter.getEntity() != null) {
            if(filter.getEntity().getNome() != null){
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getNome(String query) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .projection(Projections.property("nome")).list();
    }

    @SuppressWarnings("unchecked")
    public Logradouro getCep(String query) {
	    return crud().criteria().addCriterion(Restrictions.eq("cep", query)).find();


    }

    public boolean inserirLogradouro(String cep, ViaCEPEndereco endereco) {

        Logradouro logr = new Logradouro();
        try {
            if( crud().eq("cep",endereco.getCep().replace("-","")).count() == 0 ) {
                String nomeTMP = Normalizer
                        .normalize(endereco.getLocalidade().toUpperCase(), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");

                UF uf = ufService.crud().eq("sigla", endereco.getUf().trim()).find();
                Cidade cidade = cidadeService.crud().eq("nome", nomeTMP).and().eq("uf.id", uf.getId()).find();

                logr.setCidade(new Cidade(cidade.getId()));
                logr.setCep(cep.replace("-", ""));

                if( endereco.getBairro().length() == 0 ) {
                    logr.setIniBairro(new Bairro(-1));
                    logr.setFimBairro(new Bairro(-1));
                } else {
                    String nomeBairro = Normalizer
                            .normalize(endereco.getBairro().toUpperCase(), Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
                    Bairro bairro = baiService.crud().eq("nome", nomeBairro)
                                                   .and()
                                                    .eq("cidade.id",cidade.getId()).find();
                    if( bairro != null ) {
                       logr.setIniBairro(bairro);
                       logr.setFimBairro(bairro);
                    }
                }
                logr.setNome(endereco.getLogradouro());

                crud().saveOrUpdate(logr);
                crud().commit();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro!","Não foi possível incluir o CEP "
                          + cep  + " na base local. Informe ao suporte técnico."));
        }

	    return false;
    }



    @Override
    public void beforeInsert(Logradouro logradouro) {
        if (!logradouro.hasNome()) {
            throw new BusinessException("O nome do logradouro deve ser preenchido");
        }

        if (crud().eq("nome", logradouro.getNome()).ne("id", logradouro.getId()).count() > 0) {
            throw new BusinessException("O nome do logradouro deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Logradouro entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Logradouro logradouro) {
        super.remove(logradouro);
    }
    
}
