package com.example.newsapp;

import android.content.Context;
import android.widget.Toast;

import com.example.newsapp.Models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder() //Retrofit é um cliente REST para Java e Android que permite recuperar e carregar JSON
            .baseUrl("https://newsapi.org/v2/") //url da api
            .addConverterFactory(GsonConverterFactory.create()) //analisa automaticamente a resposta HTTP
            .build(); //para construir

    //metodo para gerir as chamadas da Api
    // chamei o metodo OnFetchDataListener e a categoria e a query que quero
    public void getNewsHeadlines(OnFetchDataListener listener, String category, String query)
    {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        //call objet =>  request to a webserver and returns a response
        Call<NewsApiResponse> call = callNewsApi.CallHeadlines("pt",category,query,context.getString(R.string.api_key)); //passar os paremtros declarados no CallHeadlines
        //try para permitir o bloco de código ser testado quanto a erros enquanto está sendo executado.
        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                        if(!response.isSuccessful()){ //se nao for bem sucedido
                            Toast.makeText(context, "Erro, não foi possível buscar os dados", Toast.LENGTH_SHORT).show();
                        }
                        listener.onFecthdata(response.body().getArticles(), response.message()); //se tudo correr bem ele vai buscar os dados e o vai passar para o listener
                }

                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                        listener.onError("O pedido falhou!"); //msg de erro no caso de falha
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public RequestManager(Context context) {
        this.context = context;
    }

    public  interface CallNewsApi {
        @GET("top-headlines") //A chamada de API, Get pq e um Get meetodo
        //paramentros que sao pedidos para usar o Api
        Call<NewsApiResponse> CallHeadlines ( //call request e retorna a resposta do web server
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_Key
        );
    }
}
