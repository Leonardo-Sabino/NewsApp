package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.newsapp.Models.CustomAdapter;
import com.example.newsapp.Models.NewsApiResponse;
import com.example.newsapp.Models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{

    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    String category = "general";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializando os butoes e tambem um onclick para cada um deles
        b1 = findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4 = findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5 = findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6 = findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7 = findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

        //inicializando o swipeRefresh
        swipeRefreshLayout = findViewById(R.id.swipeRefreshlayout);

        //criado o on refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, category,null);
                Toast.makeText(MainActivity.this, "refresh "+ category, Toast.LENGTH_SHORT).show();

                //para notificar o recycle view que os dados foram modificados
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false); //para terminar o refresh
            }
        });

        //inicializando o serachView
        searchView = findViewById(R.id.serach_view);

        //on query text listener e ele cria este metodos onQueryTextSubmit e onQueryTextChange
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // se o texto for submetido
                dialog.setTitle("Carregado as notícias de " + query); //query para apanhar o texto que o user escreveu no serachView
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query); //query para apanhar o texto que o user escreveu no serachView
                return true;

                //depois fazer um map para quando pesquisar por negocio ele retornar bussiness
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        dialog = new ProgressDialog(this); //instanciando o progressDialog
        dialog.setTitle("Carregando as notícias...");
        dialog.show();

        //para os dados aparecerem no mainActivity
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener,"general",null); //categorias que estao api: business, entertainment, general, health, science, sports,technology, query null para a primeira vez que abrirmos o app
    }
    //
    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFecthdata(List<NewsHeadlines> list, String message) {

            if (list.isEmpty()) {
                Toast.makeText(MainActivity.this, "Não foi possível encontrar notícias!", Toast.LENGTH_SHORT).show();
            }else {
                showNews(list); //chamando a funcao que mostra a lista das noticias
                dialog.dismiss(); //para o dialog sair apos mostrar as noticias
            }
        }

        @Override
        public void onError(String message) {
            //para caso houver um erro
            Toast.makeText(MainActivity.this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
        }
    };

    //funcao para mostrar a listada noticias nesta activity
    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true); //para o tamanho fixo
        recyclerView.setLayoutManager(new GridLayoutManager(this,1)); // 1 quantas noticias eu quero que apareca ao clicar
        adapter = new CustomAdapter(this,list,this);//
        recyclerView.setAdapter(adapter); //passando o adapter ao recycleView
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        //para ir a activity DetailsActivity
        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
                .putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        //para os cliques nos butoes
        Button button = (Button) v; // v = view, ele vai apanhar o view que foi clicado
         category = button.getText().toString();
        dialog.setTitle("Carregando as notícias de " + category);
        dialog.show();
        //para apresentar as noticias de acordo com a categoriad desejada pelo user (foi declarada no getNewsHeadlines)
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null); //categorias que estao api: business, entertainment, general, health, science, sports,technology, query null para a primeira vez que abrirmos o app
    }
}