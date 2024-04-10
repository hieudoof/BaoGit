package com.example.appbao2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.appbao2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendaActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView textDate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calenda);

        calendarView = findViewById(R.id.CalendaView);
        textDate = findViewById(R.id.textDate);
        //Lấy thời gian của hệ thống
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0, nên cần cộng thêm 1
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = dayOfMonth + "/" + month + "/" + year;
        textDate.setText(currentDate);
        // Click vào ClendaView để hiển thị ngày tháng năm đã chọn
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Lấy ngày đã chọn và hiển thị trên TextView
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                textDate.setText(selectedDate);
            }
        });

    }
}