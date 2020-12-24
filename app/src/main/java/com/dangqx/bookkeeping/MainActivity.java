package com.dangqx.bookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.Income;
import com.dangqx.bookkeeping.db.User;
import com.dangqx.bookkeeping.util.TimeUtil;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private List<Cost> costs;
    private List<Income> incomes;
    private User user;

    private TextView textUser;
    private TextView textToday,textWeek,textMonth,textYear;
    private TextView textTodayIn,textWeekIn,textMonthIn,textYearIn;
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

        textTodayIn = findViewById(R.id.tv_todayIn);
        textWeekIn = findViewById(R.id.tv_weekIn);
        textMonthIn = findViewById(R.id.tv_monthIn);
        textYearIn = findViewById(R.id.tv_yearIn);

        user = (User) getIntent().getSerializableExtra("user");
        textUser.setText("欢迎您！"+user.getUsername());
        getDBInfoAndSet();
        setClick();
    }

    /**
     * 绑定点击事件的方法
     */
    private void setClick(){
        textToday.setOnClickListener(this);
        textWeek.setOnClickListener(this);
        textMonth.setOnClickListener(this);
        textYear.setOnClickListener(this);

        textTodayIn.setOnClickListener(this);
        textMonthIn.setOnClickListener(this);
        textWeekIn.setOnClickListener(this);
        textYearIn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_today:
                toIntentCost("1");
                break;
            case R.id.tv_week:
                toIntentCost("2");
                break;
            case R.id.tv_month:
                toIntentCost("3");
                break;
            case R.id.tv_year:
                toIntentCost("4");
                break;
            case R.id.tv_todayIn:
                toIntentIncome("1");
                break;
            case R.id.tv_weekIn:
                toIntentIncome("2");
                break;
            case R.id.tv_monthIn:
                toIntentIncome("3");
                break;
            case R.id.tv_yearIn:
                toIntentIncome("4");
                break;
            default:
                break;
        }
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

    private String countToday(boolean flag){
        String times = timeUtil.getTimes();
        int money;
        if (flag){
            money = LitePal.where("date = ? and userId = ? ", times, String.valueOf(user.getId()))
                    .sum(Income.class, "money", int.class);
        }else{
            money = LitePal.where("date = ? and userId = ? ", times, String.valueOf(user.getId()))
                    .sum(Cost.class, "money", int.class);
        }
        return String.valueOf(money);

    }
    private String countWeek(boolean flag){
        int count = 0;
        Date firstInWeek = timeUtil.findFirstInWeek();
        Date lastInWeek = timeUtil.findLastInWeek();
        if (flag){
            for (Income income : incomes) {
                if (timeUtil.stringToDate(income.getDate()).compareTo(lastInWeek) <= 0 &&
                        firstInWeek.compareTo(timeUtil.stringToDate(income.getDate())) <= 0){
                    count +=income.getMoney();
                }
            }
        }else{
            for (Cost cost : costs) {
                if (timeUtil.stringToDate(cost.getDate()).compareTo(lastInWeek) <=0 &&
                        firstInWeek.compareTo(timeUtil.stringToDate(cost.getDate())) <= 0){
                    count += cost.getMoney();
                }
            }
        }
        //Log.d("本周花费", "countWeek: "+count);
        return String.valueOf(count);
    }
    private String countMonth(boolean flag){
        int count = 0;
        String month = timeUtil.getTimes().substring(5, 7);
        //Log.d("本月", "countMonth: "+month);
        if (flag){
            for (Income income : incomes) {
                if (income.getDate().substring(5,7).equals(month)){
                    count += income.getMoney();
                }
            }
        }else{
            for (Cost cost : costs) {
                if (cost.getDate().substring(5,7).equals(month)){
                    count += cost.getMoney();
                }
            }
        }
        return String.valueOf(count);
    }
    private String countYear(boolean flag){
        int count = 0;
        String year = timeUtil.getTimes().substring(0,4);
        if (flag){
            for (Income income : incomes) {
                if (income.getDate().substring(0,4).equals(year)){
                    count += income.getMoney();
                }
            }
        }else{
            for (Cost cost : costs) {
                if (cost.getDate().substring(0,4).equals(year)){
                    count += cost.getMoney();
                }
            }
        }
        return String.valueOf(count);
    }

    /**
     * 获取并且设置数据
     */
    private  void getDBInfoAndSet(){
        costs = LitePal.where("userId = ?", String.valueOf(user.getId())).find(Cost.class);
        incomes = LitePal.where("userId = ? ", String.valueOf(user.getId())).find(Income.class);
        textToday.setText("支出"+countToday(false)+"元");
        textWeek.setText("支出"+countWeek(false)+"元");
        textMonth.setText("支出"+countMonth(false)+"元");
        textYear.setText("支出"+countYear(false)+"元");

        textTodayIn.setText("收入"+countToday(true)+"元");
        textWeekIn.setText("收入"+countWeek(true)+"元");
        textMonthIn.setText("收入"+countMonth(true)+"元");
        textYearIn.setText("收入"+countYear(true)+"元");

    }

    /**
     * 抽取的方法，携带数据进入ContentActivity
     * @param tag
     */
    private void toIntentCost(String tag){
        Intent intent = new Intent(MainActivity.this,ContentActivity.class);
        intent.putExtra("userId",user.getId());
        intent.putExtra("tag",tag);
        startActivity(intent);
    }
    /**
     * 抽取的方法，携带数据进入IncomeContentActivity
     * @param tag
     */
    private void toIntentIncome(String tag){
        Intent intent = new Intent(MainActivity.this, IncomeContentActivity.class);
        intent.putExtra("userId",user.getId());
        intent.putExtra("tag",tag);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDBInfoAndSet();
    }


}