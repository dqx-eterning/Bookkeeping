package com.dangqx.bookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dangqx.bookkeeping.db.User;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        TextView username = findViewById(R.id.et_username);
        TextView password = findViewById(R.id.et_password);
        Button login = findViewById(R.id.bt_go);
        TextView register = findViewById(R.id.register);
        /*User user = new User();
        user.setUsername("dang");
        user.setPassword("123");
        user.save();*/
        //LitePal.deleteAll(User.class,"username = ?","wang");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String name = username.getText().toString();
                String pass = password.getText().toString();
                List<User> list = LitePal.where("username = ? ", name).find(User.class);
                //Log.d("数量", "onClick: "+list.size());
                for (User user1 : list) {
                    user.setId(user1.getId());
                    user.setUsername(user1.getUsername());
                    user.setPassword(user1.getPassword());
                }
                //Log.d("用户名和密码", "onCreate: "+dbPassword);
                //Log.d("我在这里", "onClick: "+user.getUsername());
                if (pass.equals(user.getPassword())){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    //Log.d("我在这里", "onClick: "+user.getUsername());
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}