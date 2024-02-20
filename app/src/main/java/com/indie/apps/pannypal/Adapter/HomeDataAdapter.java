package com.indie.apps.pannypal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indie.apps.pannypal.Globle;
import com.indie.apps.pannypal.Model.ContactData;
import com.indie.apps.pannypal.Model.suggestContactData;
import com.indie.apps.pannypal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.MyViewHolder>{

    List<ContactData> dataList;
    private OnItemClickListener listener;

    Context c;

    Double totalPerDay= 0.0;

    MyViewHolder currDayViewHolder;
    String todayDate,yesterDayDate;

    boolean isSearch;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public HomeDataAdapter(Context c, List<ContactData> dataList, boolean isSearch, OnItemClickListener listener) {
        this.c = c;
        this.dataList = dataList;
        this.listener = listener;
        this.isSearch = isSearch;

        Calendar calendar = Calendar.getInstance();
        todayDate = simpleDateFormat.format(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        yesterDayDate = simpleDateFormat.format(calendar.getTimeInMillis());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutDate;
        TextView txtDate,txtTotalAmt,txtRs,txtName,txtDesc;
        ImageView imgCrDr;

        RelativeLayout layoutLine;
        View v;
        public MyViewHolder(View view) {
            super(view);
            layoutDate = view.findViewById(R.id.layoutDate);
            txtDate = view.findViewById(R.id.txtDate);
            txtTotalAmt = view.findViewById(R.id.txtTotalAmt);
            txtRs = view.findViewById(R.id.txtRs);
            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            imgCrDr = view.findViewById(R.id.imgCrDr);
            layoutLine = view.findViewById(R.id.layoutLine);
            v = view;
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDataAdapter.MyViewHolder holder, int position) {

        ContactData data = dataList.get(position);
        holder.txtName.setText(data.getC_name());

        if(!data.getP_name().isEmpty())
        {
            holder.txtDesc.setText( "By " + data.getP_name());
        }else {
            holder.txtDesc.setText("Payment type not added");
        }

        if(data.getType() == 1)
        {
            holder.imgCrDr.setImageResource(R.drawable.credit);
            holder.txtRs.setText("+" +Globle.getFormattedValue(data.getAmount()));
            totalPerDay += data.getAmount();
        }else {
            holder.imgCrDr.setImageResource(R.drawable.debit);
            holder.txtRs.setText("-" +Globle.getFormattedValue(data.getAmount()));
            totalPerDay -= data.getAmount();
        }


        if(isSearch)
        {
            holder.layoutDate.setVisibility(View.GONE);
        }else {
            String currDate = simpleDateFormat.format(data.getDateTime());
            String prevDate = "";
            String nextDate = "";

            if(position != 0)
            {
                prevDate = simpleDateFormat.format(dataList.get(position-1).getDateTime());
            }

            if(position != dataList.size()-1)
            {
                nextDate = simpleDateFormat.format(dataList.get(position+1).getDateTime());
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


                currDayViewHolder = holder;

            }else {
                holder.layoutDate.setVisibility(View.GONE);
            }


            if(!currDate.equals(nextDate))
            {
                holder.layoutLine.setVisibility(View.GONE);

                currDayViewHolder.txtTotalAmt.setText("" + Globle.getFormattedValue(totalPerDay));
                if(totalPerDay >=0)
                {
                    currDayViewHolder.txtTotalAmt.setTextColor(c.getResources().getColor(R.color.credit));
                }else {
                    currDayViewHolder.txtTotalAmt.setTextColor(c.getResources().getColor(R.color.debit));
                }

                totalPerDay = 0.0;

            }else {
                holder.layoutLine.setVisibility(View.VISIBLE);
            }
        }


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
        void onItemClick(ContactData item);

    }

}
