package com.dangqx.bookkeeping;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bigkoo.pickerview.TimePickerView;
import com.dangqx.bookkeeping.db.Cost;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditActivity extends BaseActivity implements View.OnClickListener {
    TimePickerView pvTime;
    TextView start_day;

    private EditText editMoney,editDescription;
    private Button butAdd,butBack,butDel;
    private Cost cost;
    private String category;
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
        setContentView(R.layout.activity_edit);

        //toolbar代替actionbar，同时设置返回图标
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        editMoney = findViewById(R.id.edit_money);
        editDescription = findViewById(R.id.edit_description);
        butAdd = findViewById(R.id.button_add);
        butBack = findViewById(R.id.button_back);
        butDel = findViewById(R.id.button_del);

        butAdd.setOnClickListener(this);
        butBack.setOnClickListener(this);
        butDel.setOnClickListener(this);
        //接收传入的用户id
        if (getIntent().getIntExtra("userId",0) != 0){
            userId = getIntent().getIntExtra("userId",0);
        }
        //如果是准备添加则隐藏删除按钮
        butDel.setVisibility(View.GONE);
        if (getIntent().getSerializableExtra("cost") != null){
            butDel.setVisibility(View.VISIBLE);
            cost =(Cost)getIntent().getSerializableExtra("cost");
            editMoney.setText(String.valueOf(cost.getMoney()));
            editDescription.setText(cost.getDescription());
            userId = cost.getUserId();
            //Log.d("从卡片点进去", "onCreate: "+cost.getId()+cost.getDescription());
        }
        //Log.d("编辑页面的用户id", "onCreate: "+userId);

        RadioGroup group = findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                category = radioButton.getText().toString();
                //Log.d("测试输入框和复选框", "onCheckedChanged: "+radioButton.getText().toString());
            }
        });
        //选择日期的方法
        calendarDemo();
        //Log.d("测试日期", "onCreate: "+date)
    }

    /**
     * Button的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add:
                //添加事件
                if (cost != null){
                    money = editMoney.getText().toString();
                    date = start_day.getText().toString();
                    description = editDescription.getText().toString();
                    Cost cost1 = new Cost();
                    cost1.setUserId(userId);
                    cost1.setMoney(Integer.parseInt(money));
                    cost1.setCategory(category);
                    cost1.setDate(date);
                    cost1.setDescription(description);
                    cost1.saveOrUpdate("id = ?",String.valueOf(cost.getId()));
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                }else{
                    money = editMoney.getText().toString();
                    date = start_day.getText().toString();
                    description = editDescription.getText().toString();
                    Cost cost = new Cost();
                    cost.setUserId(userId);
                    cost.setMoney(Integer.parseInt(money));
                    cost.setCategory(category);
                    cost.setDate(date);
                    cost.setDescription(description);
                    cost.save();
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.button_back:
                finish();
                break;
            case R.id.button_del:
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
        start_day = findViewById(R.id.start_day);
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
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true,true,true,false,false,false})
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
                        LitePal.deleteAll(Cost.class,"id = ?",String.valueOf(cost.getId()));
                        Intent intent = new Intent(EditActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    /**
     * 点击空白隐藏输入键盘
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}