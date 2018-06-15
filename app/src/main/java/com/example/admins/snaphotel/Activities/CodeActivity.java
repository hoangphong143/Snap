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


    public PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        etPhone = findViewById(R.id.et_phone_Number);
        btVery = findViewById(R.id.bt_phone_veri);
        ivBack = findViewById(R.id.iv_back);
        mAuth = FirebaseAuth.getInstance();
        final String phone = etPhone.getText().toString();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                phoneCre = phoneAuthCredential;
                Intent i= new Intent(CodeActivity.this, PhoneVerifyActivity.class);
//                i.putExtra("KEYPHONE", etPhone.getText().toString());
                startActivity(i);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "onVerificationFailed: "+e);


                    etPhone.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {


                }
                Log.d(TAG, "onVerificationFailed: "+e);

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
                Intent i = new Intent(CodeActivity.this, PhoneVerifyActivity.class);
                i.putExtra("KEYPHONE", etPhone.getText().toString());
                i.putExtra("KEY_CODE", mVerificationId);
                startActivity(i);

            }

        };

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        btVery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification(etPhone.getText().toString());


//                PhoneAuthProvider.getInstance().verifyPhoneNumber(etPhone.getText().toString(),
//                        1, TimeUnit.SECONDS,
//                        CodeActivity.this,
//                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                                Log.d(TAG, "onVerificationCompleted: ");
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(FirebaseException e) {
//                                Log.d(TAG, "onVerificationFailed: ");
//                            }
//
//                            @Override
//                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                code = verificationId;
//                                resendingToken = forceResendingToken;
//                                Intent i = new Intent(CodeActivity.this, PhoneVerifyActivity.class);
//                                i.putExtra("KEYPHONE", etPhone.getText().toString());
//                                i.putExtra("KEY_CODE", code);
//                                startActivity(i);
//                                finish();
//
//
//                                Log.d(TAG, "onCodeSent: ");
//
//
//                            }
//
//                        });


            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }

                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        //
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
