package br.com.preco.perdeu.perdeupreco.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe singleton para gerenciar as chamadas ao webservice retrofit.
 * Created by Ronan.lima on 25/02/16.
 */
public class QueryService {
    private static QueryService instance;
    private static final String BASE_URL = "http://45.79.95.61/query/";

    private static Service service;

    private QueryService(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                //Customize the request
                Request request = original.newBuilder()
                        .header("Accept","application/json")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);
                return response;
            }
        });

        OkHttpClient client = okHttpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.service = retrofit.create(Service.class);
    }

    public static synchronized QueryService getInstance(){
        if (instance == null){
            instance = new QueryService();
        }
        return instance;
    }

    public Service getService() {
        return service;
    }

    public static void setService(Service service) {
        QueryService.service = service;
    }
}
