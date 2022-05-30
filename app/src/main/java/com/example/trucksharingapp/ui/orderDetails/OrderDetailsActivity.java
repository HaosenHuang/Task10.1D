package com.example.trucksharingapp.ui.orderDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.model.OrderModel;

public class OrderDetailsActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView titleTextView;
    private TextView rightButton;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView weightTextView;
    private TextView typeTextView;
    private TextView widthTextView;
    private TextView heightTextView;
    private TextView lengthTextView;
    private TextView quantityTextView;
    private Button callDriver;
    private OrderModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        initData();
    }

    private void initData() {
        data = (OrderModel) getIntent().getSerializableExtra("data");
        fromTextView.setText("From sender: "+data.getSenderName()+"\nPick up time: "+data.getPickTime());
        toTextView.setText("From sender: "+data.getReceiverName()+"\nDrop off time: "+data.getPickTime());
        weightTextView.setText("Weight:\n"+data.getWeight()+" kg");
        typeTextView.setText("Type:\n"+data.getGoodType()+"");
        widthTextView.setText("Width:\n"+data.getWidth()+" m");
        heightTextView.setText("Height:\n"+data.getHeight()+" m");
        lengthTextView.setText("Length:\n"+data.getLength()+" m");
        quantityTextView.setText("Quantity:\n"+"20");
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(view -> finish());
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("Order details");
        rightButton = (TextView) findViewById(R.id.rightButton);

        fromTextView = (TextView) findViewById(R.id.fromTextView);
        toTextView = (TextView) findViewById(R.id.toTextView);
        weightTextView = (TextView) findViewById(R.id.weightTextView);
        typeTextView = (TextView) findViewById(R.id.typeTextView);
        widthTextView = (TextView) findViewById(R.id.widthTextView);
        heightTextView = (TextView) findViewById(R.id.heightTextView);
        lengthTextView = (TextView) findViewById(R.id.lengthTextView);
        quantityTextView = (TextView) findViewById(R.id.quantityTextView);
        callDriver = (Button) findViewById(R.id.callDriver);
        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, EstimateActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });
    }
}