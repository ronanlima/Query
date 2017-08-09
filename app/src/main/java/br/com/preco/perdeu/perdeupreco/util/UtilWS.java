package br.com.preco.perdeu.perdeupreco.util;

import android.util.Log;

import com.google.gson.JsonElement;

import br.com.preco.perdeu.perdeupreco.bean.Usuario;
import br.com.preco.perdeu.perdeupreco.controller.QueryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ronan.lima on 25/02/16.
 */
public class UtilWS {

    public static void callWsToInsertUser(final Usuario user, final String activity){
        Call<JsonElement> call = QueryService.getInstance().getService().insereUsuario(user.getEmail()
                ,user.getSexo(), user.getFaixaEtaria());

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccess()) {
                    Log.d(activity, "Usuario '" + user.getEmail() + "' incluido com sucesso!");
                } else {
                    Log.d(activity, "Algum erro de mapeamento do servico ocorreu.");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d(activity, "Falha ao inserir usuário '" + user.getEmail() + "'. " + t.getMessage());
            }
        });
    }

}
