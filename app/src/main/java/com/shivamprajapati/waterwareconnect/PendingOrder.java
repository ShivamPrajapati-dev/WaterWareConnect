package com.shivamprajapati.waterwareconnect;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PendingOrder extends AppCompatActivity implements PendingOrderRecyclerViewAdapter.OnRecyclerViewListener {


    DatabaseReference databaseReference;
    List<CompletedorderList> lists;
    List<pendingOrderList> pendingOrderLists;
    pendingOrderList list;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView,recyclerView2;
    PendingOrderRecyclerViewAdapter adapter;
    CardView cardViewOne,cardViewTwo;
    ConnectivityManager connectivityManager;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==1) {

            assert data != null;
            int i = data.getIntExtra("pos", 0);
            int size = data.getIntExtra("size", 0);
            pendingOrderLists.remove(i);
            recyclerView2.removeViewAt(i);
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, size);
            adapter.notifyDataSetChanged();
            recyclerView2.invalidate();

            Gson gson=new Gson();
            Type type=new TypeToken<ArrayList<CompletedorderList>>(){}.getType();

            if(SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this,"co",null)!=null){
                lists=gson.fromJson(SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this,"co",null),type);

                cardViewOne.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                CompletedOrderRecyclerViewAdapter adapter=new CompletedOrderRecyclerViewAdapter(lists);
                recyclerView.setAdapter(adapter);
            }

            databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("admin").child("pending").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    pendingOrderLists = new ArrayList<>();
                    adapter=new PendingOrderRecyclerViewAdapter(pendingOrderLists,PendingOrder.this);


                    if (dataSnapshot.exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("status").getValue().toString().equals("Yes") && snapshot.child("adminCheck").getValue().toString().equals("no") && snapshot.child("shopId").getValue().toString().equals(SharedPref.readSharedSettingsShopName(PendingOrder.this, "shopName", ""))) {

                                list = new pendingOrderList();
                                list.setName(snapshot.child("name").getValue().toString().trim());
                                list.setuId(snapshot.child("uid").getValue().toString().trim());
                                pendingOrderLists.add(list);



                            }else {
                                recyclerView2.setVisibility(View.GONE);
                                cardViewTwo.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                                recyclerView2.invalidate();

                            }
                        }

                        if (pendingOrderLists.size()==0){
                            recyclerView2.setVisibility(View.GONE);
                            cardViewTwo.setVisibility(View.VISIBLE);
                        }else{
                            cardViewTwo.setVisibility(View.INVISIBLE);
                            recyclerView2.setVisibility(View.VISIBLE);
                            recyclerView2.setAdapter(adapter);
                        }
                    }else {
                        adapter.notifyDataSetChanged();
                        recyclerView2.invalidate();
                        refreshLayout.setRefreshing(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    refreshLayout.setRefreshing(false);
                }
            });
        ;


}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        refreshLayout=findViewById(R.id.swl);
        cardViewOne=findViewById(R.id.cardEmptyOne);
        cardViewTwo= findViewById(R.id.cardEmptyTwo);
        refreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_dark),getResources().getColor(android.R.color.holo_green_dark),getResources().getColor(android.R.color.holo_orange_dark));

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {


                connectivityManager = (ConnectivityManager) PendingOrder.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


                    refreshLayout.setRefreshing(true);
                    recyclerView = findViewById(R.id.rec);
                    recyclerView2 = findViewById(R.id.rec2);
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(PendingOrder.this));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrder.this, LinearLayout.HORIZONTAL, true));

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CompletedorderList>>() {
                    }.getType();

                    if (SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this, "co", null) != null) {
                        lists = gson.fromJson(SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this, "co", null), type);

                        cardViewOne.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        CompletedOrderRecyclerViewAdapter adapter = new CompletedOrderRecyclerViewAdapter(lists);
                        recyclerView.setAdapter(adapter);


                    }

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("admin").child("pending").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            pendingOrderLists = new ArrayList<>();

                            adapter = new PendingOrderRecyclerViewAdapter(pendingOrderLists, PendingOrder.this);

                            if (dataSnapshot.exists()) {
                                refreshLayout.setRefreshing(true);
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("status").getValue().toString().equals("Yes") && snapshot.child("adminCheck").getValue().toString().equals("no") && snapshot.child("shopId").getValue().toString().equals(SharedPref.readSharedSettingsShopName(PendingOrder.this, "shopName", ""))) {

                                        list = new pendingOrderList();
                                        list.setName(snapshot.child("name").getValue().toString().trim());
                                        list.setuId(snapshot.child("uid").getValue().toString().trim());
                                        pendingOrderLists.add(list);


                                    } else {

                                        adapter.notifyDataSetChanged();
                                        recyclerView2.invalidate();
                                        refreshLayout.setRefreshing(false);
                                    }
                                }

                                if (pendingOrderLists.size() == 0) {
                                    recyclerView2.setVisibility(View.GONE);
                                    cardViewTwo.setVisibility(View.VISIBLE);
                                } else {
                                    cardViewTwo.setVisibility(View.INVISIBLE);
                                    recyclerView2.setVisibility(View.VISIBLE);
                                    recyclerView2.setAdapter(adapter);
                                }


                                refreshLayout.setRefreshing(false);
                            } else {
                                adapter.notifyDataSetChanged();
                                recyclerView2.invalidate();
                                refreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            refreshLayout.setRefreshing(false);
                        }
                    });
                }else{
                    Toast.makeText(PendingOrder.this,"No internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                connectivityManager = (ConnectivityManager) PendingOrder.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


                    refreshLayout.setRefreshing(true);
                    recyclerView = findViewById(R.id.rec);
                    recyclerView2 = findViewById(R.id.rec2);
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(PendingOrder.this));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrder.this, LinearLayout.HORIZONTAL, true));

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CompletedorderList>>() {
                    }.getType();

                    if (SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this, "co", null) != null) {
                        lists = gson.fromJson(SharedPref.readSharedSettingsCompletedOrders(PendingOrder.this, "co", null), type);

                        cardViewOne.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        CompletedOrderRecyclerViewAdapter adapter = new CompletedOrderRecyclerViewAdapter(lists);
                        recyclerView.setAdapter(adapter);


                    }

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("admin").child("pending").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            pendingOrderLists = new ArrayList<>();

                            adapter = new PendingOrderRecyclerViewAdapter(pendingOrderLists, PendingOrder.this);

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("status").getValue().toString().equals("Yes") && snapshot.child("adminCheck").getValue().toString().equals("no") && snapshot.child("shopId").getValue().toString().equals(SharedPref.readSharedSettingsShopName(PendingOrder.this, "shopName", ""))) {

                                        list = new pendingOrderList();
                                        list.setName(snapshot.child("name").getValue().toString().trim());
                                        list.setuId(snapshot.child("uid").getValue().toString());
                                        pendingOrderLists.add(list);


                                    } else {

                                        adapter.notifyDataSetChanged();
                                        recyclerView2.invalidate();
                                        refreshLayout.setRefreshing(false);
                                    }
                                }
                                if (pendingOrderLists.size() == 0) {
                                    recyclerView2.setVisibility(View.GONE);
                                    cardViewTwo.setVisibility(View.VISIBLE);
                                } else {
                                    cardViewTwo.setVisibility(View.INVISIBLE);
                                    recyclerView2.setVisibility(View.VISIBLE);
                                    recyclerView2.setAdapter(adapter);
                                }
                                refreshLayout.setRefreshing(false);
                            } else {
                                adapter.notifyDataSetChanged();
                                recyclerView2.invalidate();
                                refreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(PendingOrder.this,"No internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    @Override
    public void recyclerViewListener(int position, int size,String id) {
        Intent x=new Intent(PendingOrder.this,OrderDeatails.class);
        x.putExtra("IdOfUser",id);
        x.putExtra("ss",SharedPref.readSharedSettingsShopName(PendingOrder.this,"shopName",""));
        x.putExtra("position",position);
        x.putExtra("size",size);
        startActivityForResult(x,1);

    }
}
