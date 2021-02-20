package com.dangqx.bookkeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.User;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;
import org.w3c.dom.ls.LSInput;

import java.util.List;

public class SelectActivity extends BaseActivity {

    private List<Cost> costList;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        //toolbar替换actionbar
        Toolbar toolbar = findViewById(R.id.toolbar_select);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        userId = getIntent().getStringExtra("userId");

        EditText dateEdit = findViewById(R.id.select_date);
        EditText categoryEdit = findViewById(R.id.select_category);
        //EditText moneyEdit = findViewById(R.id.select_money);
        Button selectButton = findViewById(R.id.button_select);



        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEdit.getText().toString();
                String category = categoryEdit.getText().toString();
                /*String lMoney = moneyEdit.getText().toString().split(",")[0];
                String rMoney = moneyEdit.getText().toString().split(",")[1];*/
                //Log.d("获取的数据", "onClick: "+date+","+category+","+lMoney+","+rMoney);

                if (date.length() == 0 && category.length() == 0){
                    Toast.makeText(SelectActivity.this, "查询条间为空！", Toast.LENGTH_SHORT).show();
                }else {
                    if ( category.length() == 0){
                        costList = LitePal.where("date = ? and userId = ? ", date,userId).find(Cost.class);
                    }else if (date.length() == 0 ){
                        costList = LitePal.where("category = ? and userId = ? ", category,userId).find(Cost.class);
                    }else {
                        costList = LitePal.where("date = ? and category = ? and userId = ?",date,category,userId).find(Cost.class);
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content,new SelectFragment(costList));
                    fragmentTransaction.commit();
                }
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
}