package com.example.appbao2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.appbao2.activity.EditProfileActivity;
import com.example.appbao2.activity.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private Button logoutButton,resetpasswordButton,changeprofileImage;
    TextView fullName,textEmail,textPhone;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userid;
    ProgressBar progressBar;
    FirebaseUser user;
    ImageView profileImage;

    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });
        changeprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        getDataUser();
        resetPassword();
        ChangeProfileImage();
       // Dang nhap vao se load anh
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });


        return view;
    }

    //Nut thay doi anh
    private void ChangeProfileImage() {
        changeprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goi den Galler
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("Editemail",textEmail.getText().toString());
                intent.putExtra("Editfullname",fullName.getText().toString());
                intent.putExtra("Editphonenumber",textPhone.getText().toString());
                startActivity(intent);
//                Intent openGallerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGallerIntent,1000);
            }
        });
    }


    public void getDataUser(){
        DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (documentSnapshot != null) {
                    textPhone.setText(documentSnapshot.getString("Editphonenumber"));
                    fullName.setText(documentSnapshot.getString("Editfullname"));
                    textEmail.setText(documentSnapshot.getString("Editemail"));
                }

            }
        });
    }

    private void initViews(View view) {

        logoutButton = view.findViewById(R.id.btnLog_out);
        textPhone = view.findViewById(R.id.user_phone);
        fullName = view.findViewById(R.id.user_name);
        textEmail = view.findViewById(R.id.user_details);
        progressBar = view.findViewById(R.id.progress_bar);
        resetpasswordButton=view.findViewById(R.id.btnResetPassword);
        profileImage = view.findViewById(R.id.imageView);
        changeprofileImage = view.findViewById(R.id.btnChangeProfile);
        profileImage = view.findViewById(R.id.imageView);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        user = fAuth.getCurrentUser();
        userid = fAuth.getCurrentUser().getUid();
    }


    public void resetPassword() {
        resetpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Password >= 6 Charactor");

                final EditText resetPassword = new EditText(v.getContext());
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = String.valueOf(resetPassword.getText());

                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }

}

