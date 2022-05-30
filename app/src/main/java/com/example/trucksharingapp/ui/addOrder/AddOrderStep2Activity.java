package com.example.trucksharingapp.ui.addOrder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.db.AppDataBase;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.model.UserModel;
import com.example.trucksharingapp.utli.KVUtils;
import com.example.trucksharingapp.utli.MessageEvent;
import com.example.trucksharingapp.utli.TimeUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;

public class AddOrderStep2Activity extends AppCompatActivity {

    private ImageView backButton;
    private TextView titleTextView;
    private TextView rightButton;
    private RadioButton type1;
    private RadioButton type2;
    private RadioButton type3;
    private RadioButton type4;
    private RadioButton type5;
    private EditText goodTypeOtherEdit;
    private RadioButton vType1;
    private RadioButton vType2;
    private RadioButton vType3;
    private RadioButton vType4;
    private RadioButton vType5;
    private EditText vehicleTypeOtherEdit;
    private RadioGroup goodGroup;
    private RadioGroup vehicleGroup;
    private OrderModel data;
    private Button nextButton;
    private TextInputEditText weight;
    private TextInputEditText width;
    private TextInputEditText length;
    private TextInputEditText height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_step2);
        initView();
        data = (OrderModel) getIntent().getSerializableExtra("data");
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("New delivery");
        rightButton = (TextView) findViewById(R.id.rightButton);
        type1 = (RadioButton) findViewById(R.id.type1);
        type2 = (RadioButton) findViewById(R.id.type2);
        type3 = (RadioButton) findViewById(R.id.type3);
        type4 = (RadioButton) findViewById(R.id.type4);
        type5 = (RadioButton) findViewById(R.id.type5);
        goodTypeOtherEdit = (EditText) findViewById(R.id.goodTypeOtherEdit);
        vType1 = (RadioButton) findViewById(R.id.vType1);
        vType2 = (RadioButton) findViewById(R.id.vType2);
        vType3 = (RadioButton) findViewById(R.id.vType3);
        vType4 = (RadioButton) findViewById(R.id.vType4);
        vType5 = (RadioButton) findViewById(R.id.vType5);
        vehicleTypeOtherEdit = (EditText) findViewById(R.id.vehicleTypeOtherEdit);
        goodGroup = (RadioGroup) findViewById(R.id.goodGroup);
        vehicleGroup = (RadioGroup) findViewById(R.id.vehicleGroup);
        goodTypeOtherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    goodGroup.clearCheck();
                    type5.setChecked(true);
                }
            }
        });
        vehicleTypeOtherEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    vehicleGroup.clearCheck();
                    vType5.setChecked(true);
                }
            }
        });

        vehicleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (vType5.isChecked()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vehicleTypeOtherEdit.getWindowToken(), 0);
                    imm.showSoftInputFromInputMethod(vehicleTypeOtherEdit.getWindowToken(), 0);
                    //切换软键盘的显示与隐藏
                    imm.toggleSoftInputFromWindow(vehicleTypeOtherEdit.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    vehicleTypeOtherEdit.clearFocus();
                    vType5.setChecked(false);
                }
            }
        });

        goodGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (type5.isChecked()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(goodTypeOtherEdit.getWindowToken(), 0);
                    imm.showSoftInputFromInputMethod(goodTypeOtherEdit.getWindowToken(), 0);
                    //切换软键盘的显示与隐藏
                    imm.toggleSoftInputFromWindow(goodTypeOtherEdit.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    goodTypeOtherEdit.clearFocus();
                    type5.setChecked(false);
                }
            }
        });
        weight = (TextInputEditText) findViewById(R.id.weight);
        width = (TextInputEditText) findViewById(R.id.width);
        length = (TextInputEditText) findViewById(R.id.length);
        height = (TextInputEditText) findViewById(R.id.height);
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weight.getText() == null || weight.getText().toString().equals("")
                        || width.getText() == null || width.getText().toString().equals("")
                        || length.getText() == null || length.getText().toString().equals("")
                        || height.getText() == null || height.getText().toString().equals("")) {
                    Toast.makeText(AddOrderStep2Activity.this, "Please check whether all your contents are entered!", Toast.LENGTH_SHORT).show();
                    return;
                }


                data.setWeight(Double.parseDouble(weight.getText().toString()));
                data.setWidth(Double.parseDouble(width.getText().toString()));
                data.setLength(Double.parseDouble(length.getText().toString()));
                data.setHeight(Double.parseDouble(height.getText().toString()));

                if (type1.isChecked()){
                    data.setGoodType(type1.getText().toString());
                }else if (type2.isChecked()){
                    data.setGoodType(type2.getText().toString());
                }else if (type3.isChecked()){
                    data.setGoodType(type3.getText().toString());
                }else if (type4.isChecked()){
                    data.setGoodType(type4.getText().toString());
                }else if (type5.isChecked()){
                    String type = "Other";
                    if (goodTypeOtherEdit.getText()!=null&&!goodTypeOtherEdit.getText().toString().equals("")){
                        type =goodTypeOtherEdit.getText().toString();
                    }
                    data.setGoodType(type);
                }
                if (vType1.isChecked()){
                    data.setVehicleType(vType1.getText().toString());
                }else if (vType2.isChecked()){
                    data.setVehicleType(vType2.getText().toString());
                }else if (vType3.isChecked()){
                    data.setVehicleType(vType3.getText().toString());
                }else if (vType4.isChecked()){
                    data.setVehicleType(vType4.getText().toString());
                }else if (vType5.isChecked()){
                    String type = "Other";
                    if (vehicleTypeOtherEdit.getText()!=null&&!vehicleTypeOtherEdit.getText().toString().equals("")){
                        type =vehicleTypeOtherEdit.getText().toString();
                    }
                    data.setVehicleType(type);
                }
                data.setId(TimeUtils.getCurrentTime()+"");
                data.setCreateTime(TimeUtils.getCurrentTime());
                UserModel loginUser = KVUtils.getLoginUser();
                data.setUserId(loginUser.getUserId());
                data.setSenderName(loginUser.fullName);
                AppDataBase.getInstance().orderDao().insert(data);
                setResult(RESULT_OK);
                finish();
                EventBus.getDefault().post(new MessageEvent("add goods"));
            }
        });

    }
}