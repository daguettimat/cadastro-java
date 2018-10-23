package br.com.global5.infra.rest;


import java.io.Serializable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import br.com.global5.infra.exception.CustomException;
import br.com.global5.infra.security.CustomAuthorizer;

/**
 * Created by j r zielinski on 12/20/14.
 */
@RestSecured
@Interceptor
public class RestSecuredImpl implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4334391150214898935L;

	@Inject
    CustomAuthorizer authorizer;

    @Inject
    Instance<HttpServletRequest> request;


    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        String currentUser = request.get().getHeader("user");
         if( currentUser != null){
             authorizer.login(currentUser);
         } else{
             throw new CustomException("Access forbidden");
         }
        return context.proceed();
    }

}
