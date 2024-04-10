package com.example.appbao2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appbao2.models.NewsHeadlines;
public interface SelectListener {
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState);

    void OnNewsClicked(NewsHeadlines headlines);

    void OnFavoriteClicked(NewsHeadlines newsHeadlines);
}
