package com.dangqx.bookkeeping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.dangqx.bookkeeping.db.Cost;
import com.dangqx.bookkeeping.db.User;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.button.ButtonView;

import org.litepal.LitePal;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        EditText username = findViewById(R.id.ed_username);
        EditText password = findViewById(R.id.ed_password);
        ButtonView register = findViewById(R.id.btu_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText() == null || password.getText() == null){
                    Toast.makeText(RegisterActivity.this, "用户名与密码均不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    User user = new User();
                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.save();
                    showDialog();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }
    /**
     * 弹出提示框方法
     */
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_dialog);
        builder.setTitle("提示");
        builder.setMessage("注册成功，请登录");
        builder.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(EditActivity.this, "你确定了", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}