package com.example.admins.snaphotel.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admins.snaphotel.Model.UserModel;
import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    EditText etEmail, etName;
    EditText etPassword;
    EditText etConfirmPass;
    FirebaseAuth firebaseAuth;
    TextView tvCheckEmail, tvCheckPass, tvCheckConPass, tvCheckName;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    UserModel userModel;
    AVLoadingIndicatorView av;
    FirebaseUser firebaseUser;

    Button btRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();
    }

    private void setupUI() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_emailDK);
        etPassword = findViewById(R.id.et_passDK);
        etConfirmPass = findViewById(R.id.et_confirmPassDK);
        btRegister = findViewById(R.id.bt_register);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        tvCheckEmail = findViewById(R.id.tv_check_email);
        tvCheckName = findViewById(R.id.tv_checkName);
        tvCheckPass = findViewById(R.id.tv_checkPass);
        tvCheckConPass = findViewById(R.id.tv_checkConPass);
        TextView tvLogin4= findViewById(R.id.tv_login4);
        tvLogin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


//        if (etName.getText().toString().equals("")) {
//            tvCheckName.setVisibility(View.VISIBLE);
//            check = true;
//        } else {
//            tvCheckName.setVisibility(View.INVISIBLE);
//            check = false;
//        }
//        if (etPassword.getText().toString().length() < 6) {
//            tvCheckPass.setTextColor(Color.RED);
//            check = true;
//        } else {
//            tvCheckConPass.setTextColor(Color.WHITE);
//            check = false;
//        }
//        if (!etConfirmPass.getText().toString().equals(etPassword.getText().toString())) {
//            tvCheckConPass.setVisibility(View.VISIBLE);
//            check = true;
//        } else {
//            tvCheckConPass.setVisibility(View.INVISIBLE);
//            check = false;
//        }


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    Log.d(TAG, "onClick: ");
                    tvCheckName.setVisibility(View.GONE);
                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                            etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Xác thực tài khoản qua Email", Toast.LENGTH_SHORT).show();
                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                    databaseReference = firebaseDatabase.getReference("users");
                                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                    final UserModel userModel = new UserModel(firebaseUser.getDisplayName());
                                                    UserProfileChangeRequest user = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(etName.getText().toString())
                                                            .setPhotoUri(Uri.parse("https://scontent.xx.fbcdn.net/v/t1.0-1/c29.0.100.100/p100x100/10354686_10150004552801856_220367501106153455_n.jpg?oh=049ecfece14dfe681a2cc083eeaabc6f&oe=5AA0FC77")).build();

                                                    firebaseAuth.getCurrentUser().updateProfile(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userModel);
                                                                Intent ii = new Intent(RegisterActivity.this, MainActivity.class);
                                                                Log.d(TAG, "onComplete: " + firebaseAuth.getCurrentUser().getDisplayName());
                                                                pushData();
                                                                startActivity(ii);
                                                            }
                                                            //thêm dòng này lúc đăng kí vs email k tạo đc push đc user vào
                                                            //FirebaseAuth.getInstance().signOut();
                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                tvCheckEmail.setVisibility(View.VISIBLE);
                                tvCheckEmail.setTextColor(Color.RED);

                            }
                        }
                    });
                } else {
                    tvCheckName.setVisibility(View.VISIBLE);
                    tvCheckName.setTextColor(Color.RED);
                }

                if (etPassword.getText().toString().length() < 5) {
                    Log.d(TAG, "onClick: " + etPassword.getText().toString().length());
                    tvCheckPass.setTextColor(Color.RED);

                } else {
                    tvCheckPass.setTextColor(Color.BLACK);

                }
                if (!etConfirmPass.getText().toString().equals(etPassword.getText().toString())) {
                    tvCheckConPass.setVisibility(View.VISIBLE);
                    tvCheckConPass.setTextColor(Color.RED);

                } else {
                    tvCheckConPass.setVisibility(View.GONE);

                }


            }
        });
    }

    private void pushData() {
        Log.d(TAG, "pushData: " + firebaseAuth.getCurrentUser());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        userModel = new UserModel(firebaseUser.getDisplayName(), firebaseUser.getUid(), "https://scontent.xx.fbcdn.net/v/t1.0-1/c29.0.100.100/p100x100/10354686_10150004552801856_220367501106153455_n.jpg?oh=049ecfece14dfe681a2cc083eeaabc6f&oe=5AA0FC77");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userModel);


    }

}
