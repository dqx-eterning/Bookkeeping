package com.dangqx.bookkeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;


import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.util.TimeUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ContentActivity extends BaseActivity {
    /**
     * 字符1 代表只查询今天
     * 字符2 代表查询本周
     * 字符3 代表查询本月
     * 字符4 代表查询本年
     */
    private RecyclerView recyclerView;
    private CostAdapter adapter;
    private int userId;
    private List<Cost> costs;

    TimeUtil timeUtil = new TimeUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        userId = getIntent().getIntExtra("userId", 0);
        //Log.d("用户id++++", "onCreate: "+userId);
        costs = LitePal.where("userId = ?", String.valueOf(userId)).find(Cost.class);


        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent().getStringExtra("tag").equals("1")){
            List<Cost> todayCosts = todayCosts();
            adapter = new CostAdapter(todayCosts);
            recyclerView.setAdapter(adapter);
        }else if (getIntent().getStringExtra("tag").equals("2")){
            List<Cost> weekCosts = weekCosts();
            adapter = new CostAdapter(weekCosts);
            recyclerView.setAdapter(adapter);
        }else if (getIntent().getStringExtra("tag").equals("3")){
            List<Cost> monthCosts = monthCosts();
            adapter = new CostAdapter(monthCosts);
            recyclerView.setAdapter(adapter);
        }else if (getIntent().getStringExtra("tag").equals("4")){
            List<Cost> yearCosts = yearCosts();
            adapter = new CostAdapter(yearCosts);
            recyclerView.setAdapter(adapter);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处写添加记事的逻辑
                Intent intent = new Intent(ContentActivity.this,EditActivity.class);
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
    private List<Cost> todayCosts(){
        List<Cost> costList = new ArrayList<>();
        String times = timeUtil.getTimes();
        for (Cost cost : costs) {
            if (cost.getDate().equals(times)){
                costList.add(cost);
            }
        }
        return costList;
    }

    /**
     * 查询本周的记录
     * @return
     */
    private List<Cost> weekCosts(){
        List<Cost> costList = new ArrayList<>();
        Date firstInWeek = timeUtil.findFirstInWeek();
        Date lastInWeek = timeUtil.findLastInWeek();
        for (Cost cost : costs) {
            if (timeUtil.stringToDate(cost.getDate()).compareTo(lastInWeek) <=0 &&
                    firstInWeek.compareTo(timeUtil.stringToDate(cost.getDate())) <= 0){
                costList.add(cost);
            }
        }
        return costList;
    }

    /**
     * 查询本月的记录
     * @return
     */
    private List<Cost> monthCosts(){
        List<Cost> costList = new ArrayList<>();
        String month = timeUtil.getTimes().substring(5, 7);
        for (Cost cost : costs) {
            if (cost.getDate().substring(5,7).equals(month)){
                costList.add(cost);
            }
        }
        return costList;
    }

    /**
     * 查询本年的记录
     * @return
     */
    private List<Cost> yearCosts(){
        List<Cost> costList = new ArrayList<>();
        String month = timeUtil.getTimes().substring(0, 4);
        for (Cost cost : costs) {
            if (cost.getDate().substring(0,4).equals(month)){
                costList.add(cost);
            }
        }
        return costList;
    }
}