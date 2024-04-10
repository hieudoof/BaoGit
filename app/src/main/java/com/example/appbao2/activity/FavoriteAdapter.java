package com.example.appbao2.activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbao2.R;
import com.example.appbao2.SelectListener;
import com.example.appbao2.models.NewsHeadlines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbao2.R;
import com.example.appbao2.models.NewsHeadlines;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbao2.R;
import com.example.appbao2.SelectListener;
import com.example.appbao2.models.NewsHeadlines;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<NewsHeadlines> favoriteList;
    private Context context;
    private SelectListener selectListener;

    public FavoriteAdapter(Context context, List<NewsHeadlines> favoriteList, SelectListener selectListener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsHeadlines headline = favoriteList.get(position);
        holder.textTitle.setText(headline.getTitle());
        holder.textSource.setText((CharSequence) headline.getSource());

        if (headline.getUrlToImage() != null) {
            Glide.with(context)
                    .load(headline.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.icnews)
                    .into(holder.imgHeadline);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListener.OnNewsClicked(headline);
            }
        });

        holder.imgFavorite.setVisibility(View.GONE); // Hide favorite icon for favorite list
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSource;
        ImageView imgHeadline, imgFavorite;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.fav_text_title);
            textSource = itemView.findViewById(R.id.text_source);
            imgHeadline = itemView.findViewById(R.id.fav_img_headline);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            cardView = itemView.findViewById(R.id.main_container);
        }
    }
}
