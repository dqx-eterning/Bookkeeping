package com.dangqx.bookkeeping;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.Income;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeEditActivity extends BaseActivity implements View.OnClickListener {

    private TimePickerView pvTime;
    private TextView start_day;

    private Income income;

    private EditText editMoney,editDescription;
    private Button butAdd,butBack,butDel;
    private String money;
    private String date;
    private String description;
    private int userId;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_edit);

        //toolbar代替actionbar，同时设置返回图标
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        editMoney = findViewById(R.id.edit_money_in);
        editDescription = findViewById(R.id.edit_description_in);

        butAdd = findViewById(R.id.button_add_in);
        butDel = findViewById(R.id.button_del_in);
        butBack = findViewById(R.id.button_back_in);

        butAdd.setOnClickListener(this);
        butDel.setOnClickListener(this);
        butBack.setOnClickListener(this);
        //接收传入的用户id
        if (getIntent().getIntExtra("userId",0) != 0){
            userId = getIntent().getIntExtra("userId",0);
        }
        //如果是准备添加则隐藏删除按钮
        butDel.setVisibility(View.GONE);
        if (getIntent().getSerializableExtra("income") != null){
            butDel.setVisibility(View.VISIBLE);
            income =(Income) getIntent().getSerializableExtra("income");
            editMoney.setText(String.valueOf(income.getMoney()));
            editDescription.setText(income.getDescription());
            userId = income.getUserId();
            //Log.d("从卡片点进去", "onCreate: "+cost.getId()+cost.getDescription());
        }
        calendarDemo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_in:
                if (income != null){
                    money = editMoney.getText().toString();
                    description = editDescription.getText().toString();
                    date = start_day.getText().toString();
                    if (date.equals("") || date.length() == 0){
                        Toast.makeText(this, "日期不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        Income income1 = new Income();
                        income1.setUserId(userId);
                        income1.setMoney(Integer.parseInt(money));
                        income1.setDescription(description);
                        income1.setDate(date);
                        income1.saveOrUpdate("id = ? ",String.valueOf(income.getId()));
                        Intent intent = new Intent(IncomeEditActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                }else{
                    money = editMoney.getText().toString();
                    description = editDescription.getText().toString();
                    date = start_day.getText().toString();
                    if (date.equals("") || date.length() == 0){
                        Toast.makeText(this, "日期不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        Income income1 = new Income();
                        income1.setUserId(userId);
                        income1.setMoney(Integer.parseInt(money));
                        income1.setDescription(description);
                        income1.setDate(date);
                        income1.save();
                        Intent intent = new Intent(IncomeEditActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
            case R.id.button_back_in:
                finish();
                break;
            case R.id.button_del_in:
                showDialog();
                break;
            default:
                break;
        }
    }
    /**
     * 顶部导航栏返回图标的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }
    /**
     * 日历选择器
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calendarDemo(){
        start_day = findViewById(R.id.income_day);
        start_day .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击组件的点击事件
                pvTime.show(start_day);
            }
        });
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
            }
        }).setType(new boolean[]{true,true,true,false,false,false})
                .setLabel("年", "月", "日","时","分","秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();


    }

    /**
     * date转string方法
     * @param date
     * @return
     */
    private String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 弹出提示框方法
     */
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_dialog);
        builder.setTitle("您确定要删除么？");
        builder.setMessage("点击确定删除，点击方框外可取消");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(EditActivity.this, "你确定了", Toast.LENGTH_SHORT).show();
                        LitePal.deleteAll(Income.class,"id = ?",String.valueOf(income.getId()));
                        Intent intent = new Intent(IncomeEditActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}