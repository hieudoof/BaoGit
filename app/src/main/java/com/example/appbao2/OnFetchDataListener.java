package com.example.appbao2;

import com.example.appbao2.models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<N> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);

}
