package br.com.preco.perdeu.perdeupreco.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ronan.lima on 26/02/16.
 */
public class Usuario {
    private String sexo;
    @SerializedName("idade")
    private int faixaEtaria;
    private String email;

    public Usuario() {
    }

    public Usuario(String sexo, int faixaEtaria, String email){
        setSexo(sexo);
        setFaixaEtaria(faixaEtaria);
        setEmail(email);
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(int faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
