package com.shivamprajapati.waterwareconnect;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    Spinner spinner;
    EditText phone;
    List<String> shops;

    Button button;
    String selectedShop;
    TextInputLayout textInputLayout;
    ConnectivityManager connectivityManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(SharedPref.readSharedSettingsIsLoggedIn(this, "setLogin", null) != null){
            finish();
            startActivity(new Intent(MainActivity.this,PendingOrder.class));
        }


        databaseReference= FirebaseDatabase.getInstance().getReference();
        spinner=(Spinner) findViewById(R.id.selectShop);
        phone=(EditText) findViewById(R.id.phoneNumber);
        button=findViewById(R.id.next);
        textInputLayout=findViewById(R.id.pn);
        shops=new ArrayList<>();
        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.button_disabled);


        databaseReference.child("admin").child("shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        shops.add(snapshot.getKey());


                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,shops);
                        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedShop= (String) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                connectivityManager=(ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED) {


                    databaseReference.child("admin").child("shops").child(selectedShop).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                if (!TextUtils.isEmpty(phone.getText().toString().trim())) {

                                    if (dataSnapshot.child("phoneZero").exists() && dataSnapshot.child("phoneZero").getValue().toString().equals(phone.getText().toString().trim())) {


                                        FirebaseMessaging.getInstance().subscribeToTopic(dataSnapshot.child("shopId").getValue().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this, EnterOtp.class);
                                                    intent.putExtra("phone", phone.getText().toString().trim());
                                                    SharedPref.saveSharedSettingsShopName(MainActivity.this, "shopName", dataSnapshot.child("shopId").getValue().toString().trim());
                                                    Toast.makeText(MainActivity.this, SharedPref.readSharedSettingsShopName(MainActivity.this, "shopName", ""), Toast.LENGTH_LONG).show();

                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });


                                    } else if (dataSnapshot.child("phoneOne").exists() && dataSnapshot.child("phoneOne").getValue().toString().equals(phone.getText().toString().trim())) {

                                        FirebaseMessaging.getInstance().subscribeToTopic(dataSnapshot.child("shopId").getValue().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this, EnterOtp.class);
                                                    intent.putExtra("phone", phone.getText().toString().trim());
                                                    SharedPref.saveSharedSettingsShopName(MainActivity.this, "shopName", dataSnapshot.child("shopId").getValue().toString().trim());
                                                    Toast.makeText(MainActivity.this, SharedPref.readSharedSettingsShopName(MainActivity.this, "shopName", ""), Toast.LENGTH_LONG).show();

                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });

                                    } else if (dataSnapshot.child("phoneTwo").exists() && dataSnapshot.child("phoneTwo").getValue().toString().equals(phone.getText().toString().trim())) {


                                        FirebaseMessaging.getInstance().subscribeToTopic(dataSnapshot.child("shopId").getValue().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this, EnterOtp.class);
                                                    intent.putExtra("phone", phone.getText().toString().trim());
                                                    SharedPref.saveSharedSettingsShopName(MainActivity.this, "shopName", dataSnapshot.child("shopId").getValue().toString().trim());
                                                    Toast.makeText(MainActivity.this, SharedPref.readSharedSettingsShopName(MainActivity.this, "shopName", ""), Toast.LENGTH_LONG).show();

                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });

                                    } else if (dataSnapshot.child("phoneThree").exists() && dataSnapshot.child("phoneThree").getValue().toString().equals(phone.getText().toString().trim())) {


                                        FirebaseMessaging.getInstance().subscribeToTopic(dataSnapshot.child("shopId").getValue().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this, EnterOtp.class);
                                                    intent.putExtra("phone", phone.getText().toString().trim());
                                                    SharedPref.saveSharedSettingsShopName(MainActivity.this, "shopName", dataSnapshot.child("shopId").getValue().toString().trim());
                                                    Toast.makeText(MainActivity.this, SharedPref.readSharedSettingsShopName(MainActivity.this, "shopName", ""), Toast.LENGTH_LONG).show();

                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });

                                    } else {
                                        textInputLayout.setErrorEnabled(true);
                                        textInputLayout.setError("This phone number is not registered under the shop name");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

                if(TextUtils.isEmpty(s)){
                    textInputLayout.setErrorEnabled(false);
                }
                if(s.toString().length()<10){
                    button.setEnabled(false);
                    button.setBackgroundResource(R.drawable.button_disabled);


                }else {
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.button);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
