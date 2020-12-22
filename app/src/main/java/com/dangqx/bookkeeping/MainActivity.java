package com.dangqx.bookkeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.User;
import com.dangqx.bookkeeping.util.TimeUtil;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Cost> costs;
    private User user;

    private TextView textUser;
    private TextView textToday,textWeek,textMonth,textYear;
    TimeUtil timeUtil = new TimeUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar替换actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textUser = findViewById(R.id.user_name);
        textToday = findViewById(R.id.tv_today);
        textWeek = findViewById(R.id.tv_week);
        textMonth = findViewById(R.id.tv_month);
        textYear = findViewById(R.id.tv_year);

        user = (User) getIntent().getSerializableExtra("user");
        getDBInfoAndSet();


        textToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("userId",user.getId());
                intent.putExtra("tag","1");
                startActivity(intent);
            }
        });
        textWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("userId",user.getId());
                intent.putExtra("tag","2");
                startActivity(intent);
            }
        });
        textMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                intent.putExtra("userId",user.getId());
                intent.putExtra("tag","3");
                startActivity(intent);
            }
        });
        textYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtra("userId",user.getId());
                intent.putExtra("tag","4");
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                Intent intent = new Intent("com.example.bookkeeping.FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
            case R.id.setting:
                Toast.makeText(this, "这是设置，不知道该干啥", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private String countToday(){
        String times = timeUtil.getTimes();
        //Log.d("今日日期", "countToday: "+times);
        int money = LitePal.where("date = ? and userId = ? ", times,String.valueOf(user.getId()))
                .sum(Cost.class, "money", int.class);
        return String.valueOf(money);
    }
    private String countWeek(){
        int count = 0;
        Date firstInWeek = timeUtil.findFirstInWeek();
        Date lastInWeek = timeUtil.findLastInWeek();
        for (Cost cost : costs) {
            if (timeUtil.stringToDate(cost.getDate()).compareTo(lastInWeek) <=0 &&
                    firstInWeek.compareTo(timeUtil.stringToDate(cost.getDate())) <= 0){
                count += cost.getMoney();
            }
        }
        //Log.d("本周花费", "countWeek: "+count);
        return String.valueOf(count);
    }
    private String countMonth(){
        int count = 0;
        String month = timeUtil.getTimes().substring(5, 7);
        //Log.d("本月", "countMonth: "+month);
        for (Cost cost : costs) {
            if (cost.getDate().substring(5,7).equals(month)){
                count += cost.getMoney();
            }
        }
        return String.valueOf(count);
    }
    private String countYear(){
        int count = 0;
        String year = timeUtil.getTimes().substring(0,4);
        for (Cost cost : costs) {
            if (cost.getDate().substring(0,4).equals(year)){
                count += cost.getMoney();
            }
        }
        return String.valueOf(count);
    }

    private  void getDBInfoAndSet(){
        costs = LitePal.where("userId = ?", String.valueOf(user.getId())).find(Cost.class);
        textToday.setText("支出"+countToday()+"元");
        textWeek.setText("支出"+countWeek()+"元");
        textMonth.setText("支出"+countMonth()+"元");
        textYear.setText("支出"+countYear()+"元");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getDBInfoAndSet();
    }
}