package com.shivamprajapati.waterwareconnect;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;




public class OrderDeatails extends AppCompatActivity implements CurrentOrderVerifyBottomSheet.OnVerificationCompletedListener {

    DatabaseReference databaseReference;
    String selectedShop, review, id, address, phoneNumber,token;
    TextView n, phone, date, add, day, from, to,mCleaning,mVisiting,mTotal;
    Button  done;
    List<CompletedorderList> lists;

    CompletedorderList completedorderList;
    ConnectivityManager connectivityManager;
    float rating;
    int position;
    public static final int RESULT_OK = 1;
    int size;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deatails);



        n = findViewById(R.id.nameOfUser);
        phone = findViewById(R.id.phoneNumberUser);
        date = findViewById(R.id.orderDate);
        add = findViewById(R.id.id2);
        day = findViewById(R.id.days);
        from = findViewById(R.id.timeFrom);
        to = findViewById(R.id.timeTo);
        mCleaning=findViewById(R.id.Rs300);
        mVisiting=findViewById(R.id.Rs50);
        mTotal=findViewById(R.id.total);


        done = findViewById(R.id.done);



        final Intent intent = getIntent();
        selectedShop = intent.getStringExtra("ss");
        position = intent.getIntExtra("position", 0);
        size = intent.getIntExtra("size", 0);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.CONNECTED||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED) {


            databaseReference.child("admin").child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("status").getValue().toString().equals("Yes") && snapshot.child("adminCheck").getValue().toString().equals("no") && snapshot.child("shopId").getValue().toString().equals(selectedShop) && snapshot.child("uid").getValue().toString().equals(intent.getStringExtra("IdOfUser"))) {

                            String name = snapshot.child("name").getValue().toString();
                            phoneNumber = snapshot.child("phone").getValue().toString().trim();
                            address = snapshot.child("address").getValue().toString().trim();
                            String cleaning = snapshot.child("cleaning").getValue().toString();
                            String visiting = snapshot.child("visiting").getValue().toString();
                            String total = snapshot.child("total").getValue().toString();
                            token=snapshot.child("myToken").getValue().toString().trim();
                            String mDate = snapshot.child("date").getValue().toString();
                            String fromTime = snapshot.child("fromTime").getValue().toString();
                            String toTime = snapshot.child("toTime").getValue().toString();
                            String sun = snapshot.child("SUNDAY").getValue().toString();
                            String mon = snapshot.child("MONDAY").getValue().toString();
                            String tue = snapshot.child("TUESDAY").getValue().toString();
                            String wed = snapshot.child("WEDNESDAY").getValue().toString();
                            String thu = snapshot.child("THURSDAY").getValue().toString();
                            String fri = snapshot.child("FRIDAY").getValue().toString();
                            String sat = snapshot.child("SATURDAY").getValue().toString();

                            id = snapshot.child("uid").getValue().toString().trim();
                            rating = Float.valueOf(snapshot.child("rating").getValue().toString());
                            review = snapshot.child("review").getValue().toString();
                            String[] days = new String[]{sun, mon, tue, wed, thu, fri, sat};

                            n.setText(name.trim());
                            phone.setText(phoneNumber.trim());
                            add.setText(address.trim());

                            String d = "";
                            for (int x = 0; x < days.length; x++) {
                                if (!days[x].equals("false")) {
                                    d = d + days[x] + "\n";

                                }
                            }

                            day.setText(d);
                            from.setText(fromTime);
                            to.setText(toTime);
                            date.setText(mDate);
                            mCleaning.setText(cleaning);
                            mVisiting.setText(visiting);
                            mTotal.setText(total);


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
        {
            Toast.makeText(OrderDeatails.this,"No internet connection",Toast.LENGTH_LONG).show();

        }


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                connectivityManager = (ConnectivityManager) OrderDeatails.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDeatails.this);
                    builder.setTitle("We need to verify the request completion")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {



                                    CurrentOrderVerifyBottomSheet bottomSheet=new CurrentOrderVerifyBottomSheet();
                                    bottomSheet.setContext(OrderDeatails.this);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("phoneNumber",phoneNumber);
                                    bundle.putString("userId",id);
                                    bundle.putString("deviceToken",token);
                                    bottomSheet.setArguments(bundle);
                                    bottomSheet.show(getSupportFragmentManager(),"TAG");


                                }
                            }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();


                }else{
                    Toast.makeText(OrderDeatails.this,"No internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });


    }



    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("pos", position);
        intent.putExtra("size", size);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onVerified() {




        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CompletedorderList>>() {
        }.getType();

        if (SharedPref.readSharedSettingsCompletedOrders(OrderDeatails.this, "co", null) != null) {
            lists = gson.fromJson(SharedPref.readSharedSettingsCompletedOrders(OrderDeatails.this, "co", null), type);

        } else {
            lists = new ArrayList<>();
        }
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d hh:mm aaa");
        final String d = simpleDateFormat.format(date);


        completedorderList = new CompletedorderList();
        completedorderList.setName(n.getText().toString().trim());
        completedorderList.setAddress(add.getText().toString().trim());
        completedorderList.setDate(d);
        completedorderList.setPhone(phone.getText().toString().trim());
        completedorderList.setRating(rating);
        completedorderList.setReview(review);
        lists.add(completedorderList);

        String json = gson.toJson(lists);
        SharedPref.saveSharedSettingsCompletedOrders(OrderDeatails.this, "co", json);
        finish();

        FirebaseDatabase.getInstance().getReference().child("admin").child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("status").getValue().toString().equals("Yes") && snapshot.child("adminCheck").getValue().toString().equals("no") && snapshot.child("shopId").getValue().toString().equals(selectedShop)) {
                            if (snapshot.child("uid").getValue().toString().equals(id)) {
                                snapshot.child("status").getRef().setValue("Done");
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("admin").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child("waiting").getValue().toString().equals("yes")) {
                    dataSnapshot.child("waiting").getRef().setValue("no");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
