package com.indie.apps.pannypal.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.R;

import java.util.ArrayList;
import java.util.List;

public class SearchContactFromNewEntryAdapter extends RecyclerView.Adapter<SearchContactFromNewEntryAdapter.MyViewHolder>{

    List<suggestContactData> dataList;
    private OnItemClickListener listener;


    public SearchContactFromNewEntryAdapter(List<suggestContactData> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        View v;
        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            v = view;
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion_contact_entry, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchContactFromNewEntryAdapter.MyViewHolder holder, int position) {

        suggestContactData data = dataList.get(position);
        holder.txtName.setText(data.getName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(suggestContactData item);

    }

}
