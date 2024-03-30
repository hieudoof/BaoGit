package com.example.appbao2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbao2.ProfileFragment;
import com.example.appbao2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserActivity extends AppCompatActivity  {

    private Button logoutButton,resetpasswordButton;
    TextView fullName,textEmail,textPhone;
    FirebaseUser user;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userid;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        intitID();
        //ConnectFragment();
        resetPassword();
        Logout();

    }
    public void getDataUser(){
        DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                textPhone.setText(documentSnapshot.getString("Editphonenumber"));
                fullName.setText(documentSnapshot.getString("Editfullname"));
                textEmail.setText(documentSnapshot.getString("Editemail"));
            }
        });
    }

    private void intitID() {
        resetpasswordButton=findViewById(R.id.btnResetPassword);
        logoutButton = findViewById(R.id.btnLog_out);
        textPhone = findViewById(R.id.user_phone);
        fullName = findViewById(R.id.user_name);
        textEmail = findViewById(R.id.user_details);
        progressBar = findViewById(R.id.progress_bar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userid = fAuth.getCurrentUser().getUid();
    }

    public void Logout(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }
        });
    }
    public void resetPassword() {
        resetpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        Toast.makeText(UserActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserActivity.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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