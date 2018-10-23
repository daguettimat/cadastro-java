package br.com.global5.infra.filter;

/**
 * Created by zielinski on 07/04/17.
 */

import br.com.global5.manager.bean.geral.LogonMB;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class LoginFilter implements Filter {

    public void destroy() {

    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        //Captura o ManagedBean chamado “usuarioMB”
        LogonMB logonMB = (LogonMB)
                ((HttpServletRequest) request)
                        .getSession().getAttribute("logonMB");

        //Verifica se nosso ManagedBean ainda não
        //foi instanciado ou caso a
        //variável loggedIn seja false, assim saberemos que
        // o usuário não está logado
        if (logonMB == null || !logonMB.isLoggedIn()) {
            String contextPath = ((HttpServletRequest) request)
                    .getContextPath();
            //Redirecionamos o usuário imediatamente
            //para a página de login.xhtml
            ((HttpServletResponse) response).sendRedirect
                    (contextPath + "/login.xhtml");
        } else {
            //Caso ele esteja logado, apenas deixamos
            //que o fluxo continue
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
