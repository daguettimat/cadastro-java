package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.service.cadastro.VeiculoService;
import br.com.global5.manager.service.geral.MotoristaService;
import org.hibernate.Criteria;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * CadastroService ( Regras de Negócio )
 *
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class CadastroService extends CrudService<acCadastro> {

    /**
     *
     */
    private static final long serialVersionUID = 201702210824L;

    private Motorista mot;

    @Inject
    private MotoristaService motService;

    @Inject
    private VeiculoService veiService;

    @Override
    public Criteria configPagination(Filter<acCadastro> filter) {

        // see index.xhtml 'model' column facet name filter
        if (filter.getEntity() != null) {
            if (filter.getEntity().getId() != null) {
                crud().eq("id", filter.getEntity().getId());
            }

            if (filter.getEntity().getAcLiberacao().getId() != null) {
                crud().eq("liberacao.id", filter.getEntity().getAcLiberacao().getId());
            }

            if (filter.getEntity().getDtInicial() != null) {
                crud().between("dtCriacao", filter.getEntity().getDtInicial(), filter.getEntity().getDtFinal());
            }

            if (filter.getEntity().getFilialArea() != null) {
                crud().eq("filialArea.id", filter.getEntity().getFilialArea().getId());
            }

            if (filter.getEntity().getStatus().getId() != null) {
                crud().eq("status.id", filter.getEntity().getStatus().getId());
            }

            if (filter.getEntity().getTipoFicha().getId() != null) {
                crud().eq("tipoFicha.id", filter.getEntity().getTipoFicha().getId());
            }

            if (filter.getEntity().getUsuarioTermino().getId() != null) {
                crud().eq("usuarioTermino.id", filter.getEntity().getUsuarioTermino().getId());
            }

        }

        return crud().getCriteria();
    }

    public Integer findLiberacao(String cliente, String CPF, String vinculo, List<String> Placas) {

        StringBuilder json = new StringBuilder();

        if (CPF != null) {
            mot = motService.crud().eq("doc1", CPF).maxResult(1).find();
            if (mot == null) {
                return -1;
            }
        }

        json.append("{");

        if (Placas.size() == 0) {
            if (vinculo == null) {
                return -1;
            }
            json.append("\"mot_" + mot.getId() + "\" : " + vinculo);

        }
        if (Placas.size() > 0 && CPF != null) json.append(",");

        int idx = 1;
        for (String placa : Placas) {
            Veiculo vei = veiService.crud().eq("placa", placa).maxResult(1).find();
            if (vei == null) {
                return -1;
            }
            json.append("\"" + vei.getId() + "\" : 1");
            if (Placas.size() >= (idx+1)) {
                json.append(",");
                idx++;
            }
        }

        json.append("}");

        EntityManager em = this.crud().getEntityManager();

        // " '{\"201315\": 1, \"321808\": 1, \"mot_180813\": 93}' "

        String queryStr = " " +
                " SELECT anacoid, anac_aliboid " +
                "   FROM analise_cadastral " +
                "  WHERE anac_st_itens @> " +
                " '|json|' " +
                "    AND anac_status=8 " +
                "    AND (date(anac_dt_termino)+anac_dias_embarque) >= 'today' " +
                "    AND anac_cliente_areaoid = |cliente|" +
                "  ORDER BY anacoid DESC LIMIT 1;";


        queryStr = queryStr.replace("|json|", json.toString());
        queryStr = queryStr.replace("|cliente|", cliente);

        Object[] obj = (Object[]) em.createNativeQuery(queryStr).getSingleResult();
        if( obj != null ) {
            return (Integer) obj[1];
        } else {
            return -1;
        }

    }


    @Override
    public void beforeInsert(acCadastro acCadastro) {
    }

    @Override
    public void beforeUpdate(acCadastro entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(acCadastro acCadastro) {
        super.remove(acCadastro);
    }

}
