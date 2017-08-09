package br.com.preco.perdeu.perdeupreco.controller;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.List;

import br.com.preco.perdeu.perdeupreco.bean.Estabelecimento;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ronan.lima on 25/02/16.
 */
public interface Service {
    @POST("cliente/cadastrar/{email}/{sexo}/{faixa_etaria}")
    Call<JsonElement> insereUsuario(@Path("email") String email, @Path("sexo") String sexo
            , @Path("faixa_etaria") int faixaEtaria);

    @POST("localizacao/recuperarEstabelecimento/{id_estabelecimento}/{latitude}/{longitude}")
    public Call<Estabelecimento> recuperarEstabelecimento(@Path("id_estabelecimento") Integer id
            , @Path("latitude") double latitude, @Path("longitude") double longitude);


}
