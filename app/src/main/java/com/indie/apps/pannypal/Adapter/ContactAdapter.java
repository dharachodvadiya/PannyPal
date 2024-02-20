package com.indie.apps.pannypal.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indie.apps.pannypal.ContactActivity;
import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{

    List<Contacts> dataList;
    Context c;
    ContactActivity contactActivity;
    private OnItemClickListener listener;

    boolean isSelected = false;

    public ContactAdapter(ContactActivity activity,Context c,List<Contacts> dataList, OnItemClickListener listener) {
        this.c = c;
        this.contactActivity = activity;
        this.dataList = dataList;
        this.listener = listener;
        setSelected(false);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,txtPhno,txtAmt,txtLimitAmt;
        CheckBox chkSelect;
        View v;
        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtPhno = view.findViewById(R.id.txtPhno);
            txtAmt = view.findViewById(R.id.txtAmt);
            txtLimitAmt = view.findViewById(R.id.txtLimitAmt);
            chkSelect = view.findViewById(R.id.chkSelect);
            v = view;

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        Contacts data = dataList.get(position);

        if(isSelected)
        {
            holder.chkSelect.setVisibility(View.VISIBLE);
        }else {
            holder.chkSelect.setVisibility(View.GONE);
        }

        holder.txtName.setText(data.getName());

        if(data.getPhno().isEmpty())
        {
            holder.txtPhno.setText("Phone Number Not added");
        }else {
            holder.txtPhno.setText("+" + data.getPhno());
        }

        if(data.getIsLimit() == 0)
        {
            holder.txtLimitAmt.setText("Not set");
        }else {
            holder.txtLimitAmt.setText(data.getLimitAmt() +"");
        }

        if(data.getTotalAmt() >=0)
        {
            holder.txtAmt.setText("+" + Globle.getFormattedValue(data.getCreditAmt()));
            holder.txtAmt.setTextColor(c.getResources().getColor(R.color.credit));
        }else {
            holder.txtAmt.setText("" + Globle.getFormattedValue(data.getTotalAmt()));
            holder.txtAmt.setTextColor(c.getResources().getColor(R.color.debit));
        }
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelected)
                {
                    if(holder.chkSelect.isChecked())
                    {
                        holder.chkSelect.setChecked(false);
                        listener.OnItemLongClickRemove(data);
                    }else{
                        holder.chkSelect.setChecked(true);
                        listener.OnItemLongClickAdd(data);
                    }
                }else {
                    listener.onItemClick(data);
                }
            }
        });

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               /* holder.chkSelect.setVisibility(View.VISIBLE);
               */

                if(contactActivity.isLongClickAvailable())
                {
                    if(holder.chkSelect.isChecked())
                    {
                        holder.chkSelect.setChecked(false);
                        listener.OnItemLongClickRemove(data);
                    }else{
                        holder.chkSelect.setChecked(true);
                        listener.OnItemLongClickAdd(data);
                    }

                    if(!isSelected)
                    {
                        setSelected(true);
                        notifyDataSetChanged();
                    }

                }

                return false;
            }
        });
    }

    public void setSelected(boolean isSelect)
    {
        if(isSelect != isSelected)
        {
            isSelected = isSelect;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Contacts item);

        void OnItemLongClickAdd(Contacts item);
        void OnItemLongClickRemove(Contacts item);

    }

}
