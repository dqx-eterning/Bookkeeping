package com.dangqx.bookkeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dangqx.bookkeeping.db.Income;

import java.util.List;

/**
 * Created by dang on 2020-12-22.
 * Time will tell.
 *
 * @description
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder>{

    private Context mContext;
    private List<Income> mIncomeList;
    public IncomeAdapter(List<Income> incomeList){
        mIncomeList = incomeList;
    }
    private Income income;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.income_item,parent,false);
        final IncomeAdapter.ViewHolder holder = new IncomeAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "跳转到详细情况页面", Toast.LENGTH_SHORT).show();
                income = mIncomeList.get(holder.getLayoutPosition());
                Intent intent = new Intent(mContext,IncomeEditActivity.class);
                intent.putExtra("income", income);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Income income1 = mIncomeList.get(position);
        holder.textMoney.setText(String.valueOf(income1.getMoney())+"元");
        holder.textDate.setText(income1.getDate());
        holder.textDescription.setText(income1.getDescription());
    }

    @Override
    public int getItemCount() {
        return mIncomeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    TextView textMoney,textDate,textDescription;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = (CardView) itemView;
        textMoney = itemView.findViewById(R.id.tv_money_in);
        textDate = itemView.findViewById(R.id.tv_date_in);
        textDescription = itemView.findViewById(R.id.tv_description_in);
    }
}
}
