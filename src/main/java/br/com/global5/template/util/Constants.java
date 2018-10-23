package br.com.global5.template.util;

/**
 * Created by rmpestano on 07/01/17.
 */
public interface Constants {

    String DEFAULT_INDEX_PAGE = "index.xhtml";
    String DEFAULT_LOGIN_PAGE = "login.xhtml";
    String DEFAULT_ERROR_PAGE = "500.xhtml";
    String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    String DEFAULT_PAGE_FORMAT = "xhtml";

    interface InitialParams {
        String DISABLE_FILTER = "admin.DISABLE_FILTER";
        String LOGIN_PAGE = "admin.LOGIN_PAGE";
        String ERROR_PAGE = "admin.ERROR_PAGE";
        String INDEX_PAGE = "admin.INDEX_PAGE";
    }
}
