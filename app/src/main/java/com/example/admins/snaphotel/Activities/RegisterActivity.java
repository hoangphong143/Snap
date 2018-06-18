package com.example.admins.snaphotel.Activities;

import android.app.ProgressDialog;
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
        TextView tvLogin4 = findViewById(R.id.tv_login4);
        tvLogin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().equals("")) {
                    tvCheckName.setVisibility(View.VISIBLE);
                    tvCheckEmail.setVisibility(View.GONE);
                    tvCheckPass.setVisibility(View.GONE);
                    tvCheckConPass.setVisibility(View.GONE);
                    tvCheckName.setTextColor(Color.RED);
                    return;
                } else if (etEmail.getText().toString().equals("")) {
                    tvCheckEmail.setVisibility(View.VISIBLE);
                    tvCheckName.setVisibility(View.GONE);
                    tvCheckPass.setVisibility(View.GONE);
                    tvCheckConPass.setVisibility(View.GONE);
                    tvCheckEmail.setTextColor(Color.RED);
                    return;
                } else if (etPassword.getText().toString().equals("")) {
                    tvCheckPass.setVisibility(View.VISIBLE);
                    tvCheckEmail.setVisibility(View.GONE);
                    tvCheckName.setVisibility(View.GONE);
                    tvCheckConPass.setVisibility(View.GONE);
                    tvCheckPass.setTextColor(Color.RED);
                    return;
                } else if (!etConfirmPass.getText().toString().equals(etPassword.getText().toString())) {
                    tvCheckConPass.setVisibility(View.VISIBLE);
                    tvCheckEmail.setVisibility(View.GONE);
                    tvCheckPass.setVisibility(View.GONE);
                    tvCheckName.setVisibility(View.GONE);
                    tvCheckConPass.setTextColor(Color.RED);
                    return;
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Đang đăng kí...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                            etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("users");
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                final UserModel userModel = new UserModel(firebaseUser.getDisplayName());
                                UserProfileChangeRequest user = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(etName.getText().toString())
                                        .setPhotoUri(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQN3uY-nRPoNFROMEb4lpUrnAaMCPczs4tmSamFSHHVtL8OPv2kWQ")).build();

                                firebaseAuth.getCurrentUser().updateProfile(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userModel);
                                            Intent ii = new Intent(RegisterActivity.this, MainActivity.class);
                                            pushData();
                                            progressDialog.hide();
                                            startActivity(ii);
                                        }
                                        //thêm dòng này lúc đăng kí vs email k tạo đc push đc user vào
                                        //FirebaseAuth.getInstance().signOut();
                                    }
                                });


                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }

                        }
                    });
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
