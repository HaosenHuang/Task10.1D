package com.example.trucksharingapp.ui.addOrder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.utli.TimeUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddOrderActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView titleTextView;
    private TextView rightButton;
    private TextInputEditText nameEdit;
    private TextInputLayout dateEditLayout;
    private TextInputEditText dateEdit;
    private View dateClickView;
    private TextInputLayout timeEditLayout;
    private TextInputEditText timeEdit;
    private View timeClickView;
    private TextInputEditText locationEdit;
    private TextInputEditText dropOffLocation;

    private Button nextButton;
    private OrderModel data = new OrderModel();
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        initView();

    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(view -> finish());
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("New delivery");
        rightButton = (TextView) findViewById(R.id.rightButton);
        nameEdit = (TextInputEditText) findViewById(R.id.nameEdit);
        dateEditLayout = (TextInputLayout) findViewById(R.id.dateEditLayout);
        dateEdit = (TextInputEditText) findViewById(R.id.dateEdit);
        dateClickView = (View) findViewById(R.id.dateClickView);
        timeEditLayout = (TextInputLayout) findViewById(R.id.timeEditLayout);
        timeEdit = (TextInputEditText) findViewById(R.id.timeEdit);
        timeClickView = (View) findViewById(R.id.timeClickView);
        locationEdit = (TextInputEditText) findViewById(R.id.locationEdit);
        nextButton = (Button) findViewById(R.id.nextButton);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectYear, int selectMonth, int selectDay) {
                        Log.e("TAG", "onDateSet: " + month);
//                        if (selectYear >= year && selectMonth >= month && selectDay >= day) {
//                            dateEdit.setText(selectYear + "-" + (selectMonth + 1) + "-" + selectDay);
//                        } else {
//                            Toast.makeText(AddOrderActivity.this, "Please select a date greater than or equal to today", Toast.LENGTH_SHORT).show();
//                        }
                        dateEdit.setText(selectYear + "-" + (selectMonth + 1) + "-" + selectDay);
                    }
                }, year, month, day).show();
            }
        });
        int hh = Integer.parseInt(TimeUtils.timeFormat(TimeUtils.getCurrentTime(), "HH"));
        int mm = Integer.parseInt(TimeUtils.timeFormat(TimeUtils.getCurrentTime(), "mm"));

        timeClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeEdit.setText(i + ":" + i1);
                    }
                }, hh, mm, true).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropOffLocation.getText() == null || dropOffLocation.getText().toString().equals("") || nameEdit.getText() == null || nameEdit.getText().toString().equals("") || dateEdit.getText() == null || dateEdit.getText().toString().equals("") || timeEdit.getText() == null || timeEdit.getText().toString().equals("") || locationEdit.getText() == null || locationEdit.getText().toString().equals("")) {
                    Toast.makeText(AddOrderActivity.this, "Please check whether all your contents are entered!", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.setId("")
                        .setReceiverName(nameEdit.getText().toString())
                        .setPickDate(dateEdit.getText().toString())
                        .setDropOffLocation(dropOffLocation.getText().toString())
                        .setPickTime(timeEdit.getText().toString())
                        .setLocation(locationEdit.getText().toString());

                Intent intent = new Intent(AddOrderActivity.this, AddOrderStep2Activity.class);
                intent.putExtra("data", data);
                launcher.launch(intent);

            }
        });
        dropOffLocation = (TextInputEditText) findViewById(R.id.dropOffLocation);
    }
}