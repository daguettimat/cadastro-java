package br.com.global5.infra.model;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zielinski on 05/04/17.
 */
@Entity

public class EnumControl implements BaseEntity {

    @Id
    private Integer id;

    private String  root;
    private String  descricao;

    public EnumControl(Integer id, String root, String descricao) {
        this.id = id;
        this.root = root;
        this.descricao = descricao;
    }

    public EnumControl() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }



    public static void main(String[] args) {


        System.out.println("Banco : f556cb9763617c1c4d55f2ca7ee6a8bf\nCalculado :" + AppUtils.toMd5("DILNEI458"));


    }
}
