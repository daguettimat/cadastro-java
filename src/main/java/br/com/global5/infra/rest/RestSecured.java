package br.com.global5.infra.rest;

/**
 * Created by j r zielinski on 12/20/14.
 */

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Inherited
@InterceptorBinding
public @interface RestSecured {

}
