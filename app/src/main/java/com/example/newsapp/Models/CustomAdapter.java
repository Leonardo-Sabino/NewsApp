package com.example.newsapp.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.CustomViewHolder;
import com.example.newsapp.R;
import com.example.newsapp.SelectListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<NewsHeadlines> headlines;
    private SelectListener listener; //criei um objeto para o listener criado e adicionado ao construtor abaixo

    //criando o construtor
    public CustomAdapter(Context context, List<NewsHeadlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_list_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.text_title.setText(headlines.get(position).getTitle());
        holder.text_source.setText(headlines.get(position).getSource().name); //pk o nome esta dentro do source no app como esta na Api
        if(headlines.get(position).getUrlToImage() != null){ //se a imagem da noticia nao for nula
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.img_headline); //para apanhar a imagem e o colocar no meu layput
        }
        //para colocar o onclick no cardView (que contem as noticias)
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.OnNewsClicked(headlines.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {

        return headlines.size();
    }
}
