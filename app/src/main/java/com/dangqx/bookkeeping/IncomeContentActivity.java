package com.dangqx.bookkeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.Income;
import com.dangqx.bookkeeping.util.TimeUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncomeContentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private IncomeAdapter adapter;
    private int userId;
    private List<Income> incomes;

    TimeUtil timeUtil = new TimeUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_content);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        userId = getIntent().getIntExtra("userId", 0);
        Log.d("用户id++++", "onCreate: "+userId);
        incomes = LitePal.where("userId = ?", String.valueOf(userId)).find(Income.class);

        recyclerView = findViewById(R.id.recycler_view_in);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        String extra = getIntent().getStringExtra("tag");

        if (extra.equals("1")){
            List<Income> incomes = todayIncomes();
            adapter = new IncomeAdapter(incomes);
            recyclerView.setAdapter(adapter);
        }else if (extra.equals("2")){
            List<Income> incomes = weekIncomes();
            adapter = new IncomeAdapter(incomes);
            recyclerView.setAdapter(adapter);
        }else if (extra.equals("3")){
            List<Income> incomes = monthIncomes();
            adapter = new IncomeAdapter(incomes);
            recyclerView.setAdapter(adapter);
        }else if (extra.equals("4")){
            List<Income> incomes = yearIncomes();
            adapter = new IncomeAdapter(incomes);
            recyclerView.setAdapter(adapter);
        }

        FloatingActionButton fab = findViewById(R.id.fab_in);
        FloatingActionButton fabIncome = findViewById(R.id.fab_income_in);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处写添加支出的逻辑
                Intent intent = new Intent(IncomeContentActivity.this,EditActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
        fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处为添加收入
                Intent intent = new Intent(IncomeContentActivity.this,IncomeEditActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

    }

    /**
     * 返回按钮的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    /**
     * 查询今天的记录
     * @return
     */
    private List<Income> todayIncomes(){
       List<Income> incomeList = new ArrayList<>();
        String times = timeUtil.getTimes();
        for (Income income : incomes) {
           if (income.getDate().equals(times)){
               incomeList.add(income);
           }
        }
        return incomeList;
    }

    /**
     * 查询本周的记录
     * @return
     */
    private List<Income> weekIncomes(){
        List<Income> incomeList = new ArrayList<>();
        Date firstInWeek = timeUtil.findFirstInWeek();
        Date lastInWeek = timeUtil.findLastInWeek();
        for (Income income : incomes) {
            if (timeUtil.stringToDate(income.getDate()).compareTo(lastInWeek) <=0 &&
                    firstInWeek.compareTo(timeUtil.stringToDate(income.getDate())) <= 0){
               incomeList.add(income);
            }
        }
        return incomeList;
    }

    /**
     * 查询本月的记录
     * @return
     */
    private List<Income> monthIncomes(){
        List<Income> incomeList = new ArrayList<>();
        String month = timeUtil.getTimes().substring(5, 7);
        for (Income income : incomes) {
            if (income.getDate().substring(5,7).equals(month)){
               incomeList.add(income);
            }
        }
        return incomeList;
    }

    /**
     * 查询本年的记录
     * @return
     */
    private List<Income> yearIncomes(){
        List<Income> incomeList = new ArrayList<>();
        String month = timeUtil.getTimes().substring(0, 4);
        for (Income income : incomes) {
            if (income.getDate().substring(0,4).equals(month)){
                incomeList.add(income);
            }
        }
        return incomeList;
    }
}