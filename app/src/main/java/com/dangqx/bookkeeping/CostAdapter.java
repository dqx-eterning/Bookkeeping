package com.dangqx.bookkeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dangqx.bookkeeping.db.Cost;

import java.util.List;

/**
 * Created by dang on 2020-12-15.
 * Time will tell.
 *
 * @description
 */
public class CostAdapter extends RecyclerView.Adapter<CostAdapter.ViewHolder> {

    private Context mContext;
    private List<Cost> mCostList;
    public CostAdapter(List<Cost> costList){
        mCostList = costList;
    }
    private  Cost cost;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cost_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "跳转到详细情况页面", Toast.LENGTH_SHORT).show();
                cost = mCostList.get(holder.getLayoutPosition());
                Intent intent = new Intent(mContext,EditActivity.class);
                intent.putExtra("cost",cost);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cost cost1 = mCostList.get(position);
        holder.textMoney.setText(String.valueOf(cost1.getMoney())+"元");
        holder.textCategory.setText(cost1.getCategory());
        holder.textDate.setText(cost1.getDate());
        holder.textDescription.setText(cost1.getDescription());
    }

    @Override
    public int getItemCount() {
        return mCostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textMoney,textCategory,textDate,textDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textMoney = itemView.findViewById(R.id.tv_money);
            textCategory = itemView.findViewById(R.id.tv_category);
            textDate = itemView.findViewById(R.id.tv_date);
            textDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
