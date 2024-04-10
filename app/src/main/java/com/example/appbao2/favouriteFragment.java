    package com.example.appbao2;

    import android.annotation.SuppressLint;
    import android.app.Activity;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Handler;
    import android.provider.MediaStore;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.appbao2.activity.CustomAdapter;
    import com.example.appbao2.activity.DetailsActivity;
    import com.example.appbao2.activity.EditProfileActivity;
    import com.example.appbao2.activity.LoginActivity;
    import com.example.appbao2.models.NewsHeadlines;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.EventListener;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.FirebaseFirestoreException;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Objects;


    public class favouriteFragment extends Fragment {


        private TextView txtTitle, txtAuthor, txtTime, txtDetails, txtContent;
        private ImageView imgHeadline;
        private RecyclerView recyclerView;

        // Firebase
        private FirebaseAuth mAuth;
        private FirebaseDatabase mDatabase;
        private DatabaseReference mFavouritesRef;

        private List<NewsHeadlines> favoriteList = new ArrayList<>();
        private List<NewsHeadlines> newsList = new ArrayList<>();
        private CustomAdapter adapter;
        private DatabaseReference mFavoritesRef;
        private String userId;
        private  List<NewsHeadlines> favoriteArticles = new ArrayList<>();
        private TextView textViewArticles;
        private TextView textViewEmpty;


        @SuppressLint("MissingInflatedId")
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_favourite, container, false);

            // Ánh xạ các thành phần giao diện
            txtTitle = root.findViewById(R.id.text_detail_title);
            txtAuthor = root.findViewById(R.id.text_details_author);
            txtTime = root.findViewById(R.id.text_details_time);
            txtDetails = root.findViewById(R.id.text_details_details);
            txtContent = root.findViewById(R.id.text_details_content);
            imgHeadline = root.findViewById(R.id.img_headline);
            recyclerView = root.findViewById(R.id.recycler_favorite);
            textViewArticles = root.findViewById(R.id.text_view_articles);
            textViewEmpty = root.findViewById(R.id.text_view_empty);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // Khởi tạo Firebase
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mFavouritesRef = mDatabase.getReference("favourites").child(mAuth.getCurrentUser().getUid());


            return root;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Khởi tạo RecyclerView và adapter
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CustomAdapter(getContext(), favoriteList, new SelectListener () {
                @Override
                public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                    View root = inflater.inflate(R.layout.fragment_favourite, container, false);

                    // Thực hiện ánh xạ các thành phần giao diện và xử lý logic khác nếu cần

                    return root;
                }
                @Override
                public void OnNewsClicked(NewsHeadlines headlines) {
                    // Xử lý sự kiện khi bài báo được bấm
                    // Mở DetailsActivity và chuyển dữ liệu bài báo đến đó để hiển thị nội dung chi tiết
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("data", headlines);
                    startActivity(intent);
                }
                @Override
                public void OnFavoriteClicked(NewsHeadlines headlines) {
                    // Xử lý sự kiện khi hình trái tim được bấm
                    // Ví dụ: Thêm bài báo vào danh sách yêu thích và cập nhật giao diện
                    if (headlines != null) {
                        // Thêm bài báo vào danh sách yêu thích của người dùng (ví dụ: Firebase Realtime Database)
                        // Cập nhật giao diện người dùng để hiển thị trạng thái đã thêm vào danh sách yêu thích
                        // Hiển thị thông báo cho người dùng để xác nhận rằng bài báo đã được thêm vào danh sách yêu thích thành công
                        Toast.makeText(getContext(), "Bài báo đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        addFavoriteArticleToDatabase(headlines);
                    } else {
                        Toast.makeText(getContext(), "Không thể thêm bài báo vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            recyclerView.setAdapter(adapter);

            // Tải danh sách yêu thích từ Firebase
            loadFavoriteArticles();

        }

        private void loadFavoriteArticles() {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favorites");
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                mFavoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.getUid());
                // Lắng nghe sự kiện thay đổi trên danh sách yêu thích
                mFavoritesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        favoriteList.clear();
                        // Duyệt qua danh sách yêu thích và thêm các mục vào favoriteList
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            NewsHeadlines headlines = snapshot.getValue(NewsHeadlines.class);
                            favoriteList.add(headlines);
                        }
                        // Cập nhật UI sau khi tải danh sách yêu thích
                        updateUI();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
            }
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favoriteList.clear();
                    // Duyệt qua danh sách yêu thích và thêm các mục vào favoriteList
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NewsHeadlines headlines = snapshot.getValue(NewsHeadlines.class);
                        favoriteList.add(headlines);
                    }
                    // Cập nhật UI sau khi tải danh sách yêu thích
                    updateUI();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });

        }


        private void fetchFavoriteArticlesDetails(List<String> articleIds) {
            // Khởi tạo một danh sách để lưu trữ thông tin chi tiết của các bài báo yêu thích
            List<NewsHeadlines> favoriteArticles = new ArrayList<>();

            // Thực hiện truy vấn để lấy thông tin chi tiết của các bài báo dựa trên danh sách các ID đã cho
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for (String articleId : articleIds) {
                // Thực hiện truy vấn để lấy thông tin chi tiết của bài báo có ID tương ứng từ Firestore
                DocumentReference docRef = db.collection("articles").document(articleId);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Lấy thông tin từ DocumentSnapshot và tạo đối tượng NewsHeadlines
                            NewsHeadlines news = new NewsHeadlines();
                            news.setAuthor(documentSnapshot.getString("author"));
                            news.setContent(documentSnapshot.getString("content"));
                            news.setDescription(documentSnapshot.getString("description"));
                            news.setPublishedAt(documentSnapshot.getString("publishedAt"));
                            news.setTitle(documentSnapshot.getString("title"));
                            news.setUrlToImage(documentSnapshot.getString("urlToImage"));

                            // Thêm bài báo vào danh sách các bài báo yêu thích
                            favoriteArticles.add(news);
                            // Cập nhật giao diện nếu đã lấy được thông tin của tất cả các bài báo
                            if (favoriteArticles.size() == articleIds.size()) {
                                updateUI();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu có
                    }
                });
            }
        }

        private void updateUI() {
            if (favoriteList.isEmpty()) {
                // Hiển thị thông báo cho người dùng nếu không có bài báo yêu thích
                Toast.makeText(getContext(), "Không có bài báo yêu thích", Toast.LENGTH_SHORT).show();
                textViewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                textViewEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                // Cập nhật dữ liệu trong adapter và gọi notifyDataSetChanged()
                adapter.notifyDataSetChanged();
            }
        }



        private void addFavouriteArticle() {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if ( !userId.isEmpty()) {
                // Tạo một đối tượng NewsHeadlines
                NewsHeadlines favouriteArticle = new NewsHeadlines();
                favouriteArticle.setTitle("Titles");
                favouriteArticle.setAuthor("Author");
                favouriteArticle.setPublishedAt("Published at");
                favouriteArticle.setDescription("Description");
                favouriteArticle.setContent("Content");
                favouriteArticle.setUrlToImage("Image");


                // Thêm đối tượng vào Firebase Realtime Database
                mFavouritesRef.push().setValue(favouriteArticle);
            } else {
                // In ra log nếu không tìm thấy userId hoặc userId rỗng
                Log.e("favouriteFragment", "Không tìm thấy giá trị cho userId hoặc userId rỗng");
            }
        }


            public void updateFavoriteList(List<NewsHeadlines> favoriteList) {
                this.favoriteList.clear();
                this.favoriteList.addAll(favoriteList);
                adapter.notifyDataSetChanged();
            }
        private void addFavoriteArticleToDatabase(NewsHeadlines news) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mFavoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);

            // Tạo một node mới trong Firebase với một ID duy nhất
            String articleId = mFavoritesRef.push().getKey();

            // Kiểm tra xem articleId có tồn tại hay không
            if (articleId != null) {
                // Tạo một map để lưu trữ thông tin của bài báo
                Map<String, Object> articleValues = new HashMap<>();
                articleValues.put("title", news.getTitle());
                articleValues.put("author", news.getAuthor());
                articleValues.put("publishedAt", news.getPublishedAt());
                articleValues.put("description", news.getDescription());
                articleValues.put("content", news.getContent());
                articleValues.put("urlToImage", news.getUrlToImage());
                articleValues.put("userId", userId); // Thêm userId vào dữ liệu bài báo

                // Thêm bài báo vào Realtime Database
                mFavoritesRef.child(articleId).setValue(articleValues)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Hiển thị thông báo cho người dùng khi thêm bài báo thành công
                                Toast.makeText(getContext(), "Bài báo đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi thêm bài báo thất bại
                                Toast.makeText(getContext(), "Không thể thêm bài báo vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

//        private void removeFavoriteArticleFromDatabase(NewsHeadlines news) {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            DatabaseReference mFavoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);
//
//            // Tìm kiếm và xóa bài báo khỏi danh sách yêu thích
//            Query query = mFavoritesRef.orderByChild("title").equalTo(news.getTitle());
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        snapshot.getRef().removeValue();
//                    }
//                    // Hiển thị thông báo cho người dùng khi xóa bài báo khỏi danh sách yêu thích thành công
//                    Toast.makeText(getContext(), "Bài báo đã được xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Xử lý khi có lỗi xảy ra
//                    Toast.makeText(getContext(), "Không thể xóa bài báo khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }


    }