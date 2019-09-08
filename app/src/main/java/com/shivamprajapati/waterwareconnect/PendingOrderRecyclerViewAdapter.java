package com.shivamprajapati.waterwareconnect;


import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class PendingOrderRecyclerViewAdapter extends RecyclerView.Adapter<PendingOrderRecyclerViewAdapter.ViewHolder> {

    public  List<pendingOrderList> list;
    Context context;

    int code=1;
    public PendingOrderRecyclerViewAdapter(List<pendingOrderList> list,Context activityContext) {
        this.list = list;
        recyclerViewListener=(OnRecyclerViewListener)activityContext;
    }

    public  interface OnRecyclerViewListener{
        void recyclerViewListener(int position,int size,String id);
    }

    OnRecyclerViewListener recyclerViewListener;

    @NonNull
    @Override
    public PendingOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_order_rec_layout,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderRecyclerViewAdapter.ViewHolder viewHolder,final int i) {

        viewHolder.counter.setText(""+(i+1));
        viewHolder.name.setText(list.get(i).getName());

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recyclerViewListener!=null)
                recyclerViewListener.recyclerViewListener(i,list.size(),list.get(i).getuId());
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView counter,name;
        Button button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            counter=itemView.findViewById(R.id.counter);
            name=itemView.findViewById(R.id.pending);
            button=itemView.findViewById(R.id.button);
        }
    }


}


