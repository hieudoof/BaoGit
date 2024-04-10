package com.example.appbao2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.appbao2.activity.CustomAdapter;
import com.example.appbao2.activity.DetailsActivity;
import com.example.appbao2.models.NewsApiResponse;
import com.example.appbao2.models.NewsHeadlines;
import com.example.appbao2.models.RequestManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeFragment extends Fragment implements SelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    private RequestManager requestManager;

    private  View view;

    private ImageView imgFavorite;


    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        // Khởi tạo ProgressDialog
        dialog = new ProgressDialog(requireContext());
        dialog.setTitle("Fetching news articles...");
        dialog.setCancelable(false);
        dialog.show();

        // Khởi tạo RequestManager
        RequestManager requestManager = new RequestManager(getContext());

        // Xử lý sự kiện khi nhập từ khóa tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of " + query);
                dialog.show();
                requestManager.getNewsHeadlines(listener, "general", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Gọi API để lấy dữ liệu
        requestManager.getNewsHeadlines(listener, "general", null);



        return view;
    }


    private void initViews(View view){
        b1 = view.findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2 = view.findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3 = view.findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4 = view.findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5 = view.findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6 = view.findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7 = view.findViewById(R.id.btn_7);
        b7.setOnClickListener(this);
        searchView = view.findViewById(R.id.search_view);
        imgFavorite = view.findViewById(R.id.img_favorite);
    }

    private final OnFetchDataListener<NewsApiResponse> listener= new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();

            }
            else{
                showNews(list);
                dialog.dismiss();
            }
        }


        @Override
        public void onError(String message) {
            // Ghi log để ghi lại thông báo lỗi
            Log.e("MainActivity", "Error: " + message);

            // Hiển thị một hộp thoại thông báo lỗi chi tiết cho người dùng
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error");
            builder.setMessage("An error occurred: " + message);
            builder.setPositiveButton("OK", null);
            builder.show();
        }

    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = view.findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new CustomAdapter(getContext(), list, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        // Kiểm tra xem headlines có null không trước khi sử dụng để tránh NullPointerException
        if (headlines != null) {
            startActivity(new Intent(getActivity(), DetailsActivity.class)
                    .putExtra("data", headlines));
        } else {
            Toast.makeText(getContext(), "Clicked item is null", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void OnFavoriteClicked(NewsHeadlines newsHeadlines) {
        if (newsHeadlines != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites").push();
            databaseReference.setValue(newsHeadlines)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Bài báo đã được thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Không thể thêm bài báo vào mục yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Bài báo không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        dialog.setTitle("Fetching news articles of " + category);
        dialog.show();
        RequestManager manager = new RequestManager(getContext());
        manager.getNewsHeadlines(listener, category,null);
    }

}