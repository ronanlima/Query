package br.com.preco.perdeu.perdeupreco.model;

import io.realm.RealmObject;

/**
 * Created by brunolemgruber on 07/01/16.
 */
public class BarCode extends RealmObject {

    private String codigoBarra;

    private String nomeProduto;

    private String preco;


    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
