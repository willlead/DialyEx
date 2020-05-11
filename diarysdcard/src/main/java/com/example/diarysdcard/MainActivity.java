package com.example.diarysdcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnSave;
    DatePicker dPicker;
    EditText edtDiary;
    String fileName;
    int cYear, cMonth, cDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSave = (Button)findViewById(R.id.btnSave);
        dPicker = (DatePicker)findViewById(R.id.dPicker);
        edtDiary = (EditText)findViewById(R.id.edtDiary);

        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH);
        cDay = cal.get(Calendar.DAY_OF_MONTH);

        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        }
        else
        {
            sdcardProcess();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fout = new FileOutputStream(fileName);
                    String str = edtDiary.getText().toString();
                    fout.write(str.getBytes());
                    fout.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sdcardProcess();
    }

    void sdcardProcess()
    {
        final String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myDir3";
        //final String sdcardPath = "/sdcard/";
        final File myDir = new File(sdcardPath);
        if(!myDir.isDirectory())
            myDir.mkdir();

        fileName = String.format(myDir+"/%d_%d_%d.txt", cYear, cMonth+1, cDay);
        edtDiary.setText(readDiary(fileName));
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = String.format(myDir+"/%d_%d_%d.txt", year, monthOfYear+1, dayOfMonth);
                File file = new File(fileName);
                edtDiary.setText(readDiary(fileName));
            }
        });
    }

    String readDiary(String fileName)
    {
        String diaryStr = null;
        try {
            FileInputStream fIn = new FileInputStream(fileName);
            byte[] txt = new byte[fIn.available()];
            fIn.read(txt);
            diaryStr = new String(txt).trim();
            fIn.close();
            btnSave.setText("Modify");
        }catch (IOException e) {
            edtDiary.setHint("No Diary");
            btnSave.setText("Save");
        }
        return diaryStr;
    }

    void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
