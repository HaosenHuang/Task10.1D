package com.example.trucksharingapp.ui.loginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.db.AppDataBase;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.model.UserModel;
import com.example.trucksharingapp.ui.MainActivity;
import com.example.trucksharingapp.utli.KVUtils;
import com.example.trucksharingapp.utli.TimeUtils;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText account;
    private EditText password;
    private Button btnRegister;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        if (!KVUtils.getBoolean("isAddFalseData")) {
            initData();
        }
    }

    private void initData() {
        //add False data
        AppDataBase.getInstance().orderDao().insert(new OrderModel()
                .setId((TimeUtils.getCurrentTime() + 1) + "")
                .setUserId("falseDataUser")
                .setSenderName("Tom")
                .setReceiverName("Jeck")
                .setPickDate("2022-05-18")
                .setPickTime("09:15")
                .setLocation("北京")
                .setDropOffLocation("沈阳")
                .setGoodType("Building material")
                .setWeight(2)
                .setWidth(5)
                .setLength(6)
                .setHeight(8)
                .setVehicleType("Van"));

        AppDataBase.getInstance().orderDao().insert(new OrderModel()
                .setId((TimeUtils.getCurrentTime() + 2) + "")
                .setUserId("falseDataUser")
                .setSenderName("Patton")
                .setReceiverName("Edwin")
                .setPickDate("2022-05-28")
                .setPickTime("05:45")
                .setLocation("上海")
                .setDropOffLocation("杭州")
                .setGoodType("Nicole")
                .setWeight(2)
                .setWidth(5)
                .setLength(6)
                .setHeight(8)
                .setVehicleType("Van"));
        AppDataBase.getInstance().orderDao().insert(new OrderModel()
                .setId((TimeUtils.getCurrentTime() + 3) + "")
                .setUserId("falseDataUser")
                .setSenderName("Anthony")
                .setReceiverName("Zane")
                .setPickDate("2022-06-06")
                .setPickTime("11:25")
                .setLocation("天津")
                .setDropOffLocation("河南")
                .setGoodType("Furniture")
                .setWeight(2)
                .setWidth(5)
                .setLength(6)
                .setHeight(8)
                .setVehicleType("Van"));

        AppDataBase.getInstance().orderDao().insert(new OrderModel()
                .setId((TimeUtils.getCurrentTime() + 4) + "")
                .setUserId("falseDataUser")
                .setSenderName("Venus")
                .setReceiverName("Starlight")
                .setPickDate("2022-07-08")
                .setPickTime("09:08")
                .setLocation("周口")
                .setDropOffLocation("武汉")
                .setGoodType("Dry goods")
                .setWeight(2)
                .setWidth(5)
                .setLength(6)
                .setHeight(8)
                .setVehicleType("Van"));

        KVUtils.putBoolean("isAddFalseData", true);
    }

    private void initView() {
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                submit();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void submit() {
        // validate
        String accountString = account.getText().toString().trim();
        if (TextUtils.isEmpty(accountString)) {
            Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "Please input a password!", Toast.LENGTH_SHORT).show();
            return;
        }
        login();
    }

    private void login() {
        String accountString = account.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        UserModel user = AppDataBase.getInstance().userDao().getByAccount(accountString);

        if (user != null) {
            if (user.getPassword().equals(passwordString)) {
                ///
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                KVUtils.putString("accountString", user.getUserId());
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Password error!", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "This account has not been registered, please use it after registration!", Toast.LENGTH_SHORT).show();
        }


    }
}