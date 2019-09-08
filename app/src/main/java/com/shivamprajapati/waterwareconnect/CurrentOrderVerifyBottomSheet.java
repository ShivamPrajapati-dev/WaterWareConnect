package com.shivamprajapati.waterwareconnect;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class CurrentOrderVerifyBottomSheet extends BottomSheetDialogFragment {

    TextView later,sendOTP;
    Button verify;
    EditText otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String phoneNumber,uId,token,code="000000";
    Context context;
    public Context cont;

    public void setContext(Context cont){
        this.cont=cont;
    }

    public interface OnVerificationCompletedListener{
        void onVerified();
    }

    OnVerificationCompletedListener onVerificationCompletedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.verify_order_completion,container,false);
        later=view.findViewById(R.id.textView25);
        verify=view.findViewById(R.id.verifyNow);
        otp=view.findViewById(R.id.number);
        sendOTP=view.findViewById(R.id.textView24);
        context=getContext();

        assert getArguments() != null;
        phoneNumber=getArguments().getString("phoneNumber");
        uId=getArguments().getString("userId");
        token=getArguments().getString("deviceToken");

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map map=new HashMap();

                map.put("otp",RandomString.getAlphaNumericString(6));
                map.put("token",token);

                FirebaseDatabase.getInstance().getReference().child("partners").child(uId).updateChildren(map);
                Toast.makeText(context,"OTP sent successfully",Toast.LENGTH_LONG).show();



            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(otp.getText().toString())){


                    FirebaseDatabase.getInstance().getReference().child("partners").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("otp").getValue().toString().trim().equals(otp.getText().toString())){
                                Toast.makeText(context,"verified",Toast.LENGTH_LONG).show();

                                onVerificationCompletedListener= (OnVerificationCompletedListener) cont;
                                onVerificationCompletedListener.onVerified();
                                dismiss();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(context,"Enter otp first",Toast.LENGTH_LONG).show();
                }
            }
        });


        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });




        return view;
    }

    public static class RandomString {

        // function to generate a random string of length n
        static String getAlphaNumericString(int n) {

            // chose a Character random from this String
            String AlphaNumericString ="0123456789";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }

            return sb.toString();
        }
    }




}
