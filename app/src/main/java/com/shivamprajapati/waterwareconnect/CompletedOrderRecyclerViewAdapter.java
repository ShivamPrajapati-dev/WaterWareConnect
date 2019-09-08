package com.shivamprajapati.waterwareconnect;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CompletedOrderRecyclerViewAdapter extends RecyclerView.Adapter<CompletedOrderRecyclerViewAdapter.ViewHolder> {

    private List<CompletedorderList> completedorderLists;

    CompletedOrderRecyclerViewAdapter(List<CompletedorderList> completedorderLists) {
        this.completedorderLists = completedorderLists;
    }

    @NonNull
    @Override
    public CompletedOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderRecyclerViewAdapter.ViewHolder viewHolder, int i) {

        viewHolder.name.setText(completedorderLists.get(i).getName());
        viewHolder.phone.setText(completedorderLists.get(i).getPhone());
        viewHolder.date.setText(completedorderLists.get(i).getDate());
        viewHolder.address.setText(completedorderLists.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return completedorderLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone,address,date;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameOfUser);
            phone=itemView.findViewById(R.id.phoneNumberUser);
            date=itemView.findViewById(R.id.orderDate);
            address=itemView.findViewById(R.id.id2);



        }
    }
}
