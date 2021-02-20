package com.dangqx.bookkeeping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dangqx.bookkeeping.db.Cost;

import java.util.List;

/**
 * Created by dang on 2020-12-14.
 * Time will tell.
 *
 * @description
 */
public class SelectFragment extends Fragment {
    private List<Cost> mCostList;
    private View view;
    public SelectFragment(List<Cost> costList) {
        mCostList = costList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.select_list,null);
        ListView listView = view.findViewById(R.id.lv);
        myBaseAdapter adapter = new myBaseAdapter();
        listView.setAdapter(adapter);
        return view;
    }

    class myBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mCostList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCostList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(SelectFragment.this.getContext(),R.layout.select_item,null);
            TextView costInfo = view.findViewById(R.id.cost_info);
            String money = String.valueOf(mCostList.get(position).getMoney());
            String date = mCostList.get(position).getDate();
            String category = mCostList.get(position).getCategory();
            String description = mCostList.get(position).getDescription();
            costInfo.setText("金额: "+money+"¥"+",日期: "+date+",分类: "+category+",描述: "+description);
            return view;
        }
    }
}
