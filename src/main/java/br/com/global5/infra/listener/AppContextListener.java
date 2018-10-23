package br.com.global5.infra.listener;

import br.com.global5.infra.Crud;
import br.com.global5.infra.model.EnumControl;
import br.com.global5.manager.model.geral.Motorista;
import org.hibernate.search.FullTextSession;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zielinski on 06/04/17.
 */
public class AppContextListener implements ServletContextListener {

//    private Map<Integer, EnumControl> enumMap;
//    @Inject
//    private Crud<EnumControl> crudEnum;
//
//    @Inject
//    private Crud<Motorista> motCrud;
//
//    private FullTextSession fullTextSession;

    public void contextInitialized(ServletContextEvent event) {

//        if (enumMap == null) {
//            enumMap = new HashMap<Integer, EnumControl>();
//            List list =  crudEnum.rawQuery("select relname as  root,enumoid as id ,enum_nome as descricao from enum_sistema E,pg_class P where E.tableoid=P.oid");
//            Iterator iter = list.iterator();
//            while (iter.hasNext()) {
//                Object[] objEnum = (Object[]) iter.next();
//                enumMap.put((int) objEnum[1], new EnumControl((int) objEnum[1]
//                                                      , String.valueOf(objEnum[0])
//                                                      , String.valueOf(objEnum[2])));
//            }

            //
            // Gerando indice de pesquisa de motoristas
            //

//            fullTextSession = Search.getFullTextSession(motCrud.getSession());
//
//            try {
//                fullTextSession.createIndexer()
//                        .batchSizeToLoadObjects( 25000 )
//                        .threadsToLoadObjects( 20 )
//                        .cacheMode(CacheMode.NORMAL) // defaults to CacheMode.IGNORE
//                        .startAndWait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//
//        }

//        event.getServletContext().setAttribute("enum", enumMap);

    }

    public void contextDestroyed(ServletContextEvent event) {
//        enumMap = (Map) event.getServletContext().getAttribute(
//                "enum");
//        try {
//            event.getServletContext().removeAttribute("enum");
//        } finally {
//            enumMap.clear();
//        }
    }

//    If you wish to retrieve the object in a JSF managed bean, you can get it from the ExternalContext:
//
//            FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("enum");

}
