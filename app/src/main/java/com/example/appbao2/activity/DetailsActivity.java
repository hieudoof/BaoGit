package com.example.appbao2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbao2.R;
import com.example.appbao2.models.NewsHeadlines;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    NewsHeadlines headlines;
    TextView txt_title, txt_author, txt_time, txt_detail, txt_content;
    ImageView img_news;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txt_title = findViewById(R.id.text_detail_title);
        txt_author = findViewById(R.id.text_details_author);
        txt_time = findViewById(R.id.text_details_time);
        txt_detail = findViewById(R.id.text_details_details);
        txt_content = findViewById(R.id.text_details_content);
        img_news = findViewById(R.id.img_headline);

        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        txt_title.setText(headlines.getTitle());
        txt_author.setText(headlines.getAuthor());
        txt_time.setText(headlines.getPublishedAt());
        txt_detail.setText(headlines.getDescription());
        txt_content.setText(headlines.getContent());
        Picasso.get().load(headlines.getUrlToImage()).into(img_news);

        if (headlines.getUrlToImage() != null && !headlines.getUrlToImage().isEmpty()) {
            Picasso.get().load(headlines.getUrlToImage()).into(img_news);
        } else {
            // Xử lý trường hợp không có URL hình ảnh hoặc URL hình ảnh là null
            // Ví dụ: Hiển thị một hình ảnh mặc định hoặc ẩn ImageView
            img_news.setImageResource(R.drawable.img);
            // hoặc
            // img_news.setVisibility(View.GONE);
        }
    }
}