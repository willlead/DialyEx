package com.example.dialyex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DatePicker dPicker;
    EditText edtDiary;
    Button btnSave;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dPicker = (DatePicker)findViewById(R.id.dPicker);
        edtDiary = (EditText)findViewById(R.id.edtDiary);
        btnSave = (Button)findViewById(R.id.btnSave);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        fileName = cYear + "_"+(cMonth+1)+"_"+cDay +".txt";
        edtDiary.setText(readDiary(fileName));


        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = year + "_"+(monthOfYear+1)+"_"+dayOfMonth +".txt";
                edtDiary.setText(readDiary(fileName));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fout = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str = edtDiary.getText().toString();
                    fout.write(str.getBytes());
                    fout.close();
                    showToast(fileName+"이 저장되었습니다. ");
                } catch (IOException e) {
                    showToast("파일을 저장할 수 없습니다.");
                }
            }
        });
    }

    String readDiary(String fileName)
    {
        String diaryStr = null;

        try {
            FileInputStream fin = openFileInput(fileName);
            byte[] txt = new byte[fin.available()];
            fin.read(txt);
            diaryStr = new String(txt).trim();
            fin.close();
            btnSave.setText("수정하기");
        } catch (IOException e) {
            edtDiary.setHint("열기 없음");
            btnSave.setText("새로 저장");
        }
        return diaryStr;
    }

    void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
