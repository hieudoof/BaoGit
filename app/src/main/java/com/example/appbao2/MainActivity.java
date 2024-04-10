package com.example.appbao2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.appbao2.BroadcastReciver.Internet;
import com.example.appbao2.models.NewsHeadlines;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Internet internet;

    private List<NewsHeadlines> favoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navProfile) {
                    loadFragment(new ProfileFragment(), false);
                } else { //nav favourite
                    loadFragment(new favouriteFragment(), false);
                }
                return true;
            }
        });

        loadFragment(new HomeFragment(), true);
        // Khởi tạo và đăng ký BroadcastReceiver
        internet = new Internet();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internet, filter);


    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout,fragment);

        }else {
            fragmentTransaction.replace(R.id.frameLayout,fragment);
        }
        fragmentTransaction.commit();

    }

    public void addFavorite(NewsHeadlines newsHeadlines) {
        // Kiểm tra xem bài báo đã tồn tại trong danh sách yêu thích chưa
        if (!favoriteList.contains(newsHeadlines)) {
            // Thêm bài báo vào danh sách yêu thích
            favoriteList.add(newsHeadlines);

            // Lấy instance của FavoriteFragment từ FragmentManager
            favouriteFragment favoriteFragment = (favouriteFragment) getSupportFragmentManager().findFragmentByTag("favoriteFragment");

            // Kiểm tra xem FavoriteFragment đã được tạo hay chưa
            if (favoriteFragment != null && favoriteFragment.isVisible()) {
                // Nếu FavoriteFragment đang hiển thị, cập nhật giao diện người dùng
                favoriteFragment.updateFavoriteList(favoriteList);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Hủy đăng ký BroadcastReceiver
        unregisterReceiver(internet);
    }
}