package com.example.appbao2.activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbao2.R;
public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    TextView textTitle, textSource;
    ImageView imgHeadline, imgFavorite;
    CardView cardView;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.fav_text_title);
        textSource = itemView.findViewById(R.id.fav_text_source);
        imgHeadline = itemView.findViewById(R.id.fav_img_headline);
        imgFavorite = itemView.findViewById(R.id.img_favorite);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
