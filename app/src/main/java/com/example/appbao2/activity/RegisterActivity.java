package com.example.appbao2.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbao2.MainActivity;
import com.example.appbao2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG1 = "TAG";
    EditText Editemail,Editpassword,Editfullname,Editphone;
    Button btnRegister;
    TextView btnBacktoLogin;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    String userID;


    ProgressBar progressBar;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Khai bao id
        initID();
        btnBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Editemail.getText().toString().trim();
                String password = Editpassword.getText().toString().trim();
                String fullname = Editfullname.getText().toString().trim();
                String phonenumber = Editphone.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Editemail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Editpassword.setError("Password is Required");
                    return;
                }if (password.length()<6){
                    Editpassword.setError("Password must be >= 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // dang ky firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(RegisterActivity.this,"Account Created",Toast.LENGTH_SHORT).show();


                                    // Get the current user's ID
                                    String userID = mAuth.getCurrentUser().getUid();

                                    // Get the reference to the user's document in the "users" collection
                                    DocumentReference documentReference = fStore.collection("users").document(userID);

                                    // Create a new user object with the updated fields
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Editfullname", fullname);
                                    user.put("Editemail", email);
                                    user.put("Editphonenumber", phonenumber);


                                    // Update the user document in the "users" collection
                                    documentReference.set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure " + e.toString());

                                                }
                                            });



                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this,"Creat Account Fail",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    public void initID(){
        Editfullname = findViewById(R.id.editFullName);
        Editphone = findViewById(R.id.editPhoneNumber);
        Editemail = findViewById(R.id.editEmail);
        Editpassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBacktoLogin = findViewById(R.id.btnBacktoLogin);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
    }
}