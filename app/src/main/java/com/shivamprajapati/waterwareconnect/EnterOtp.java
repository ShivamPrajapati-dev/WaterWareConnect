package com.shivamprajapati.waterwareconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {

    EditText editText;
    FirebaseAuth mAuth;
    TextView textView5;
    Button button;
    ProgressDialog progressDialog;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    String verificationID,phone;
    int i=29;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        editText=findViewById(R.id.editText42);
        textView5=findViewById(R.id.textView5);
        button=findViewById(R.id.cardView4);

        mAuth=FirebaseAuth.getInstance();

        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");



        mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                String code=phoneAuthCredential.getSmsCode();

                if (code!=null){
                    editText.setText(code);
                    verify(code);
                }


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(EnterOtp.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationID=s;
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone,30, TimeUnit.SECONDS,this,mCallBack);

        startCountDown();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText = (EditText) findViewById(R.id.editText42);

                String otp = editText.getText().toString().trim();
                if (TextUtils.isEmpty(otp) || otp.length() < 6) {

                    Toast.makeText(EnterOtp.this, "Enter valid OTP", Toast.LENGTH_LONG).show();
                } else {
                    verify(otp);
                }
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone,30, TimeUnit.SECONDS,EnterOtp.this,mCallBack);
                startCountDown();
            }
        });



    }

    private void verify(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        signInWithPhoneAuthCredential(credential);



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    SharedPref.saveSharedSettingsIsLoggedIn(EnterOtp.this,"setLogin", "yes");
                    Intent intent=new Intent(EnterOtp.this,PendingOrder.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(EnterOtp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startCountDown(){

        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

                textView5.setClickable(false);
                textView5.setText("00:"+check(i));
                i--;
                ;
            }

            @Override
            public void onFinish() {
                textView5.setText("Resend OTP");
                textView5.setClickable(true);
                i=30;
            }
        }.start();


    }

    private String check(int i) {

        if(i<=9)
            return "0"+String.valueOf(i);
        else
            return String.valueOf(i);
    }
}
