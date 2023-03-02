package br.com.global5.infra.node;

/**
 * Created by zielinski on 16/03/17.
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

public class NodePrinter {

    public static Connection conexao;

    static void printRootNode(Node root) {
        printNodes(root, 0);
    }

    static void printNodes(Node node, int indentLevel) {

        printNode(node, indentLevel);
        // recursividade
        for (Node child : node.getChildren()) {
            printNodes(child, indentLevel + 1);
        }
    }

    static void printNode(Node node, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            sb.append("\t");
        }
        sb.append(node);

        System.out.println(sb.toString());
    }

    public static void criaConexao() throws ClassNotFoundException, SQLException {
        String endereco = "172.20.20.21";
        String porta = "5432";
        String banco = "cadastro";
        String usuario = "postgres";
        String senha = "integracao";
        try {
            if (conexao != null  && conexao.isValid(0)) {
                return;
            }
            Class.forName("org.postgresql.driver");
            conexao = DriverManager.getConnection("jdbc:postgresql://" + endereco
                    + ":" + porta + "/" + banco + "?user=" + usuario
					+ "&password=" + senha);
        } catch (ClassNotFoundException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, JspException, IOException {

        criaConexao();

        StringBuilder sql = new StringBuilder();

        sql.append("select distinct ")
                .append(		"t007.a007_codarea, ")
                .append(		"t007.a007_nome, ")
                .append(		"t007.a007_numordem, ")
                .append(		"t007.a007_url, ")
                .append(		"t007.a007_tipo, ")
                .append(		"t007.a007_codareasitemae ")
                .append(		"from t007_area t007 ")
                .append(			"inner join t009_perfilarea t009 on t007.a007_codarea=t009.a007_codarea ")
                .append(			"inner join t008_perfil t008 on t009.a008_codperfil=t008.a008_codperfil ")
                .append(			"inner join t016_perfilusuario t016 on t008.a008_codperfil=t016.a008_codperfil ")
                .append(			"inner join t015_usuario t015 on t016.a015_codusuario=t015.a015_codusuario ")
                .append("where upper(t007.a007_tipo)='M' ")
                .append("  and t015.a015_codusuario=1;");

        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql.toString());
        List<Map<String, String>> resultSet = new ArrayList<Map<String, String>>();
        while( rs.next() ) {
            resultSet.add(newMap(rs.getString("t007.a007_codarea"), rs.getString("t007.a007_nome"), rs.getString("t007.a007_codareasitemae"),"true",rs.getString("t007.a007_url"),rs.getString("t007.a007_numordem")));
        }

        Node root = NodeBuilder.build(resultSet);
        System.out.println(doMenu(root));
        //printNode(root,0);


    }

    // facilitador da inclusao de registros
    private static Map<String, String> newMap(String id, String name, String parentId, String selectable, String link, String order) {
        Map<String, String> row = new HashMap<String, String>();
        row.put("id", id);
        row.put("name", name);
        row.put("parent", parentId);
        row.put("selectable", selectable);
        row.put("link", link);
        row.put("order", order);
        return row;
    }

    @SuppressWarnings("unchecked")
    public static String doMenu(Node node) throws JspException, IOException {

        StringBuilder html = new StringBuilder();
        if (node != null) {


            Iterator<Node> i = Util.sortedIterator(node.getChildren().iterator(), new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Node n1 = (Node) o1;
                    Node n2 = (Node) o2;
                    if (n1.getOrder() < n2.getOrder()) {
                        return -1;
                    }
                    if (n1.getOrder() > n2.getOrder()) {
                        return 1;
                    }
                    return 0;

                }
            });
            while (i.hasNext()) {
                Node area = i.next();
                html.append("<li class=\"dropdown menu-large\">");
                html.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
                html.append(area.getName()
                        + "&nbsp;&nbsp;<b class=\"caret\"></b></a>");
                html.append("<ul class=\"dropdown-menu row\">");
                if(area.getChildren() != null ) {
                    html.append(makeSubMenu(area, area.getId()).toString());
                }
                html.append("</ul></li>");
            }
            return html.toString();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static StringBuilder makeSubMenu(final Node area, final Integer Integer) {

        StringBuilder auxA = new StringBuilder();
        if (area.getChildren() != null) {
            Iterator<Node> i = Util.sortedIterator(area.getChildren().iterator(), new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Node n1 = (Node) o1;
                    Node n2 = (Node) o2;
                    if (n1.getOrder() < n2.getOrder()) {
                        return -1;
                    }
                    if (n1.getOrder() > n2.getOrder()) {
                        return 1;
                    }
                    return 0;

                }
            });
            while (i.hasNext()) {
                Node areaAux = i.next();

                if (areaAux.getLink() == null && areaAux.getChildren() != null) {
                    if(areaAux.getChildren() != null ) {
                        if(areaAux.getName().equals("---")) {
                            auxA.append("<li role='presentation' class='divider'></li>");
                        } else {
                            auxA.append("<li><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">"
                                    + areaAux.getName() + "<b class=\"caret\"></b></a>");
                            auxA.append("<ul class=\"dropdown-menu\">");
                            auxA.append(makeSubMenu(areaAux, areaAux.getId()));
                            auxA.append("</ul>");
                            auxA.append("</li>");
                        }
                    }
                } else {
                    if( areaAux.getName().equals("---")) {
                        auxA.append("<li role='presentation' class='divider'></li>");
                    } else {
                        if(areaAux.getLink() != null) {
                            auxA.append("<li><a href=\"javascript:$('#map').hide();$('#conteudo').show();$.Plugins.atualizar({url:'"
                                    + areaAux.getLink() + "' , frame:'" + areaAux.getLink().replace("!", "").replace(".", "")
                                    + "' , title:'" + areaAux.getName()
                                    + "', block:true})\">"
                                    + areaAux.getName() + "</a></li>");
                        } else {
                            auxA.append("<li><a href=\"javascript:$('#map').hide();$('#conteudo').show();$.Plugins.atualizar({url:'"
                                    + areaAux.getLink()
                                    + "', block:true})\">"
                                    + areaAux.getName() + "</a></li>");

                        }
                    }
                }
            }
            return auxA;
        } else {
            return auxA;
        }
    }
}