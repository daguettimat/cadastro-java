package br.com.global5.infra.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zielinski on 16/03/17.
 */
public class Node implements Comparable<Node> {

    private int id = -1;
    private String name;
    private int root;
    private int order;

    private boolean selectable;
    private String link;
    private List<Node> children;
    private Node parent = null;

    public Node() {}


    public Node(Node parent, int id, String name, int root, boolean selectable, String link, int order) {
        this.parent = parent;
        this.children = new ArrayList<Node>();
        this.name = name;
        this.id = id;
        this.root = root;
        this.selectable = selectable;
        this.link = link;
        this.order = order;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    @Override
    public String toString() {
        return "{ \"key\" :" + id + ", \"order\" :" + order + ", \"name\" :\"" + name +  "\", \"root\" : " + root +", \"selectable\" : " + selectable + ", \"link\" :\"" + link +  "\", \"sub\" : " + children.toString() + "}";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOrder() {
        return order;
    }


    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public int compareTo(Node object) {
        if (this.order < object.order) {
            return -1;
        }
        if (this.order > object.order) {
            return 1;
        }
        return 0;
    }



}
