package com.example.trucksharingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.model.UserModel;
import com.example.trucksharingapp.ui.addOrder.AddOrderActivity;
import com.example.trucksharingapp.ui.fragment.ContentFragment;
import com.example.trucksharingapp.utli.KVUtils;

public class MainActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView titleTextView;
    private TextView rightButton;
    private FrameLayout frameLayout;
    private ContentFragment homeFragment = new ContentFragment(true, this);
    private ContentFragment myOrderFragment = new ContentFragment(false, this);
    private ImageView addButton;
    public UserModel currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        //Get the currently logged in user
        currentUserData = KVUtils.getLoginUser();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, homeFragment, "1");
        transaction.add(R.id.frameLayout, myOrderFragment, "2");
        transaction.show(homeFragment);
        transaction.hide(myOrderFragment);
        transaction.commit();


    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightButton = (TextView) findViewById(R.id.rightButton);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        addButton = (ImageView) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddOrderActivity.class));
            }
        });
    }

    public void changeFragment(boolean isHome) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isHome) {
            transaction.hide(myOrderFragment);
            transaction.show(homeFragment);
        } else {
            transaction.hide(homeFragment);
            transaction.show(myOrderFragment);
        }
        transaction.commit();
    }
}