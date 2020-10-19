package com.example.rateexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        //intent.getFloatExtra(名称，取不到时值);
        EditText et = findViewById(R.id.editTextTextPersonName5);
        et.setText(String.valueOf(intent.getFloatExtra("dollar", 0f)));
        et = findViewById(R.id.editTextTextPersonName6);
        et.setText(String.valueOf(intent.getFloatExtra("euro", 0f)));
        et = findViewById(R.id.editTextTextPersonName7);
        et.setText(String.valueOf(intent.getFloatExtra("won", 0f)));
    }

    public void ReturnFarther(View view) {
        Intent data = getIntent();
        EditText et = findViewById(R.id.editTextTextPersonName5);
        Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher matcher = pattern.matcher(et.getText());
        if (!matcher.matches()) {
            Toast.makeText(this, "输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        data.putExtra("dollar",Float.parseFloat(matcher.group()));

        et = findViewById(R.id.editTextTextPersonName6);
        matcher = pattern.matcher(et.getText());
        if (!matcher.matches()) {
            Toast.makeText(this, "输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        data.putExtra("euro",Float.parseFloat(matcher.group()));

        et = findViewById(R.id.editTextTextPersonName7);
        matcher = pattern.matcher(et.getText());
        if (!matcher.matches()) {
            Toast.makeText(this, "输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        data.putExtra("won",Float.parseFloat(matcher.group()));
        setResult(2, data);
        finish();
    }
}