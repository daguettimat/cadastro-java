package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * UsuarioService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-01 15:50
 */

public class UsuarioService extends CrudService<Usuario> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Usuario> filter) {
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
	

    @Override
    public void beforeInsert(Usuario Usuario) {
        if (!Usuario.hasNome()) {
            throw new BusinessException("O nome do usuário é obrigatório");
        }

        if (crud().eq("nome", Usuario.getNome()).ne("id", Usuario.getId()).count() > 0) {
            throw new BusinessException("O nome do usuário fornecido já existe no cadastro com outro ID...");
        }

        if (crud().eq("login", Usuario.getLogin()).ne("id", Usuario.getId()).count() > 0) {
            throw new BusinessException("O login do usuário que foi escolhido já esta em uso");
        }

    }
	
    @Override
    public void beforeUpdate(Usuario entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Usuario usuario) {
        super.remove(usuario);
    }

    // Verifica se usuário existe ou se pode logar
    public Usuario isUsuarioReadyToLogin(String login, String senha) {
        try {
            login = login.toLowerCase().trim();
            List retorno = crud().criteria().findByNamedQuery(
                    Usuario.FIND_BY_LOGIN_SENHA,
                    AppUtils.NamedParams("login", login.toUpperCase().trim()
                              , "senha", AppUtils.toMd5(login.toLowerCase() + senha)));

            if (retorno.size() == 1) {
                Usuario userFound = (Usuario) retorno.get(0);
                return userFound;
            }

            return null;
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar o login!");
        }
    }
 
    // Verifica se usuário existe ou se pode logar
    public Usuario isUsuarioReadyToLogin(String login, String senha, String novaSenha) {
        try {
            Usuario usuario = isUsuarioReadyToLogin(login, senha);

            if( usuario != null ) {
                usuario.setPasswd(AppUtils.toMd5(login.toLowerCase() + novaSenha));
                usuario.setSenha(novaSenha);
                usuario.setDtPasswd(new Date());
                crud().update(usuario);

                login = login.toLowerCase().trim();
                List retorno = crud().criteria().findByNamedQuery(
                        Usuario.FIND_BY_LOGIN_SENHA,
                        AppUtils.NamedParams("login", login.toUpperCase().trim()
                                , "senha", AppUtils.toMd5(login.toLowerCase() + novaSenha)));

                if (retorno.size() == 1) {
                    Usuario userFound = (Usuario) retorno.get(0);
                    return userFound;
                }
            }

            return null;
        } catch (Exception e) {
            throw new BusinessException("Não foi possível realizar o login!");
        }
    }


    // Verifica se usuário é interno
    public List<Usuario> listaUsuariosInternos() {
        try {

            List retorno = crud().criteria().findWithNamedQuery(
                    Usuario.FIND_INTERNOS);

            if (retorno.size() > 0) {
                return retorno;
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }


    
}
