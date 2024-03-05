package com.indie.apps.pannypal.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indie.apps.pannypal.ContactActivity;
import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.Contacts;
import com.indie.apps.pannypal.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ContactDataAdapter extends RecyclerView.Adapter<ContactDataAdapter.MyViewHolder>{

    List<ContactData> dataList;
    Context c;
    private OnItemClickListener listener;

    boolean isSelected = false;
    TextView txtNoDataFound;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    String todayDate,yesterDayDate;

    public ContactDataAdapter(Context c, List<ContactData> dataList, TextView txtNoDataFound, OnItemClickListener listener) {
        this.c = c;
        this.dataList = dataList;
        this.listener = listener;
        this.txtNoDataFound = txtNoDataFound;

        if(this.dataList.size() >0)
        {
            this.txtNoDataFound .setVisibility(View.GONE);
        }else {
            this.txtNoDataFound .setVisibility(View.VISIBLE);
        }

        Calendar calendar = Calendar.getInstance();
        todayDate = simpleDateFormat.format(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        yesterDayDate = simpleDateFormat.format(calendar.getTimeInMillis());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layoutCradit, layoutDebit;
        RelativeLayout layoutDate;
        TextView txtDeAmt, txtCrAmt, txtDePType, txtCrPType, txtDeRemark, txtCrRemark, txtDeTime, txtCrTime, txtDate;
        CheckBox chkDeSelect, chkCrSelect;
        ImageButton btnCrMore, btnDeMore;
        View v;
        public MyViewHolder(View view) {
            super(view);
            layoutCradit = view.findViewById(R.id.layoutCradit);
            layoutDebit = view.findViewById(R.id.layoutDebit);
            layoutDate = view.findViewById(R.id.layoutDate);
            txtDate = view.findViewById(R.id.txtDate);

            txtDeAmt = view.findViewById(R.id.txtDeAmt);
            txtDePType = view.findViewById(R.id.txtDePaymentType);
            txtDeRemark = view.findViewById(R.id.txtDeRemark);
            txtDeTime = view.findViewById(R.id.txtDeTime);
            chkDeSelect = view.findViewById(R.id.chkDeSelect);
            btnDeMore = view.findViewById(R.id.btnDeMore);

            txtCrAmt = view.findViewById(R.id.txtCrAmt);
            txtCrPType = view.findViewById(R.id.txtCrPaymentType);
            txtCrRemark = view.findViewById(R.id.txtCrRemark);
            txtCrTime = view.findViewById(R.id.txtCrTime);
            chkCrSelect = view.findViewById(R.id.chkCrSelect);
            btnCrMore = view.findViewById(R.id.btnCrMore);

            v = view;

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_selected, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ContactData data = dataList.get(position);

        String currDate = simpleDateFormat.format(data.getDateTime());
        String prevDate = "";

        if(position != 0)
        {
            prevDate = simpleDateFormat.format(dataList.get(position-1).getDateTime());
        }


        if(!currDate.equals(prevDate))
        {
            holder.layoutDate.setVisibility(View.VISIBLE);

            if(currDate.equals(todayDate))
            {
                holder.txtDate.setText("Today");
            }else if(currDate.equals(yesterDayDate))
            {
                holder.txtDate.setText("Yesterday");
            }else {
                holder.txtDate.setText(currDate);
            }

        }else {
            holder.layoutDate.setVisibility(View.GONE);
        }

        if(data.getType() == 1)
        {
            holder.layoutCradit.setVisibility(View.VISIBLE);
            holder.layoutDebit.setVisibility(View.GONE);

            holder.txtCrAmt.setText("" + Globle.getFormattedValue(data.getAmount()));

            if(isSelected)
            {
                holder.chkCrSelect.setVisibility(View.VISIBLE);
                holder.btnCrMore.setVisibility(View.GONE);
            }else {
                holder.chkCrSelect.setChecked(false);
                holder.chkCrSelect.setVisibility(View.GONE);
                holder.btnCrMore.setVisibility(View.VISIBLE);
            }

            if(!data.getP_name().isEmpty())
            {
                holder.txtCrPType.setVisibility(View.VISIBLE);
                holder.txtCrPType.setText( "Received By " + data.getP_name());
            }else {
                holder.txtCrPType.setVisibility(View.GONE);
            }

            if(!data.getRemark().isEmpty())
            {
                holder.txtCrRemark.setVisibility(View.VISIBLE);
                holder.txtCrRemark.setText( "" + data.getRemark());
            }else {
                holder.txtCrRemark.setVisibility(View.GONE);
            }

            holder.txtCrTime.setText(dateFormat.format(data.getDateTime()));

        }else {
            holder.layoutCradit.setVisibility(View.GONE);
            holder.layoutDebit.setVisibility(View.VISIBLE);

            holder.txtDeAmt.setText("" + Globle.getFormattedValue(data.getAmount()));

            if(isSelected)
            {
                holder.chkDeSelect.setVisibility(View.VISIBLE);
                holder.btnDeMore.setVisibility(View.GONE);
            }else {
                holder.chkDeSelect.setChecked(false);
                holder.chkDeSelect.setVisibility(View.GONE);
                holder.btnDeMore.setVisibility(View.VISIBLE);
            }
            if(!data.getP_name().isEmpty())
            {
                holder.txtDePType.setVisibility(View.VISIBLE);
                holder.txtDePType.setText( "Spent By " + data.getP_name());
            }else {
                holder.txtDePType.setVisibility(View.GONE);
            }
            if(!data.getRemark().isEmpty())
            {
                holder.txtDeRemark.setVisibility(View.VISIBLE);
                holder.txtDeRemark.setText( "" + data.getRemark());
            }else {
                holder.txtDeRemark.setVisibility(View.GONE);
            }
            holder.txtDeTime.setText(dateFormat.format(data.getDateTime()));

        }



        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSelected)
                {
                    CheckBox checkBox;
                    if(data.getType() == 1)
                    {
                        checkBox = holder.chkCrSelect;
                    }else {
                        checkBox = holder.chkDeSelect;
                    }
                    if(checkBox.isChecked())
                    {
                        checkBox.setChecked(false);
                        listener.OnItemLongClickRemove(data,position);
                    }else{
                        checkBox.setChecked(true);
                        listener.OnItemLongClickAdd(data,position);
                    }
                }else {
                    if(data.getType() == 1)
                    {
                        listener.onItemClick(data, holder.btnCrMore, position);
                    }else {
                        listener.onItemClick(data, holder.btnDeMore, position);
                    }

                }
            }
        });

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CheckBox checkBox;
                if(data.getType() == 1)
                {
                    checkBox = holder.chkCrSelect;
                }else {
                    checkBox = holder.chkDeSelect;
                }
                checkBox.setVisibility(View.VISIBLE);


                //if(contactActivity.isLongClickAvailable())
                //{
                    if(checkBox.isChecked())
                    {
                        checkBox.setChecked(false);
                        listener.OnItemLongClickRemove(data,position);
                    }else{
                        checkBox.setChecked(true);
                        listener.OnItemLongClickAdd(data,position);
                    }

                    if(!isSelected)
                    {
                        setSelected(true);
                        //notifyDataSetChanged();
                    }

               // }

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
            if(dataList.size() >0)
            {
                txtNoDataFound.setVisibility(View.GONE);
            }else {
                txtNoDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ContactData item, View v, int pos);

        void OnItemLongClickAdd(ContactData item, int pos);
        void OnItemLongClickRemove(ContactData item, int pos);

    }

    public  void itemInsertDataChange(int pos)
    {
        if(dataList.size() > 0)
        {
            txtNoDataFound.setVisibility(View.GONE);
        }else {
            txtNoDataFound.setVisibility(View.VISIBLE);
        }
        notifyItemInserted(pos);
    }

    public  void itemEditDataChange(int pos)
    {
        if(dataList.size() > 0)
        {
            txtNoDataFound.setVisibility(View.GONE);
        }else {
            txtNoDataFound.setVisibility(View.VISIBLE);
        }
        notifyItemChanged(pos);
    }

    public  void dataChange()
    {
        if(dataList.size() > 0)
        {
            txtNoDataFound.setVisibility(View.GONE);
        }else {
            txtNoDataFound.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

}
