package com.example.admins.snaphotel.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by Admins on 6/12/2018.
 */

public class CodeActivity extends AppCompatActivity {
    private static final String TAG = CodeActivity.class.toString();
    EditText etPhone;
    Button btVery;

    private String mVerificationId;
    ImageView ivBack;
    FirebaseAuth mAuth;
    private String code;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public static PhoneAuthCredential phoneCre;
    public static String phone;

    public PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        etPhone = findViewById(R.id.et_phone_Number);
        btVery = findViewById(R.id.bt_phone_veri);
        ivBack = findViewById(R.id.iv_back);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        btVery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(etPhone.getText().toString(),
                        15, TimeUnit.SECONDS,
                        CodeActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                Log.d(TAG, "onVerificationCompleted: ");

                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.d(TAG, "onVerificationFailed: ");
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                code = verificationId;
                                resendingToken = forceResendingToken;
                                phone = etPhone.getText().toString();

                                Intent i = new Intent(CodeActivity.this, PhoneVerifyActivity.class);
                                i.putExtra("KEYPHONE", etPhone.getText().toString());
                                i.putExtra("KEY_CODE", code);
                                startActivity(i);
                                finish();


                                Log.d(TAG, "onCodeSent: ");


                            }

                        });


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
