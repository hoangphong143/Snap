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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenducanhit.hotelhunter2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by Admins on 6/12/2018.
 */

public class PhoneVerifyActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = PhoneVerifyActivity.class.toString();
    TextView tvPhoneNumber;
    EditText etcode;
    Button btPhoneVerify;
    TextView tvResendCode;
    String code1;
    FirebaseAuth auth;
    public static String phone;
    public String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        auth = FirebaseAuth.getInstance();


        tvPhoneNumber = findViewById(R.id.tv_verify_phoneNumber);
        etcode = findViewById(R.id.et_code);
        btPhoneVerify = findViewById(R.id.bt_verify_verifyPhone);
        tvResendCode = findViewById(R.id.tv_resend_code);


        if(CodeActivity.phoneCre!=null){
            signInWithPhone(CodeActivity.phoneCre);
        } else {

            code1 = getIntent().getStringExtra("KEY_CODE");
            Log.d(TAG, "onCreate: "+code1);
        }
        phone = getIntent().getStringExtra("KEYPHONE");
        tvPhoneNumber.setText(phone);


        btPhoneVerify.setOnClickListener(this);
        tvResendCode.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_verify_verifyPhone:
                code = etcode.getText().toString();
                if (code1 != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code1, code);
                    signInWithPhone(credential);
                    Log.d(TAG, "onClick: ");


                }


                break;
            case R.id.tv_resend_code:
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        tvPhoneNumber.getText().toString(),
                        1,
                        TimeUnit.SECONDS,
                        this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() { // OnVerificationStateChangedCallbacks

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                                Toast.makeText(PhoneVerifyActivity.this, "sent!" + phoneAuthCredential, Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onVerificationCompleted: sent!" + phoneAuthCredential);
                                //  signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.i(TAG, "onVerificationFailed: fail cmnr!" + e.getMessage());
                                Toast.makeText(PhoneVerifyActivity.this, "fail! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeAutoRetrievalTimeOut(String s) {
                                Log.i(TAG, "onCodeAutoRetrievalTimeOut: code time out!: " + s);
                                Toast.makeText(PhoneVerifyActivity.this, "time out" + s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Log.i(TAG, "onCodeSent: code sent!" + verificationId);
                                // Toast.makeText(PhoneVerifyActivity.this, "code sent! "+ verificationId, Toast.LENGTH_SHORT).show();

                            }
                        });

                break;
        }
    }


    private void signInWithPhone(PhoneAuthCredential credential) {
        final String name= auth.getCurrentUser().getDisplayName();

        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: " + task.toString());
            }
        });

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(PhoneVerifyActivity.this, AddHotelActivity.class);
                            intent.putExtra("KEY_VERIFYEDPHONE", phone);
                            startActivity(intent);
                            Log.d(TAG, "onComplete:ll ");
//                            UserProfileChangeRequest user = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
//                            auth.getCurrentUser().updateProfile(user);

                            Toast.makeText(PhoneVerifyActivity.this," Xác thực thành công!", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(PhoneVerifyActivity.this," Xảy ra lỗi xác thực", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException().getMessage());
                        }
                    }

                });


    }
}
