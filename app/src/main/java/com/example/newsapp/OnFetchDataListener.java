package com.example.newsapp;

import com.example.newsapp.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener <NewApiResponse>{
    void onFecthdata(List<NewsHeadlines> list, String message);
    void onError(String message);

}
