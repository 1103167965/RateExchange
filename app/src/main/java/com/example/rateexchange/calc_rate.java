package com.example.rateexchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class calc_rate extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_rate);
        Intent intent = getIntent();
        String money = intent.getStringExtra("money");
        String rate = intent.getStringExtra("rate");
        TextView tv=findViewById(R.id.title);
        tv.setText(money);
        EditText et=findViewById(R.id.money);
        et.setTag(rate);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TextView tv=findViewById(R.id.result);
                tv.setText(String.valueOf(Float.parseFloat(charSequence.toString())*Float.parseFloat(findViewById(R.id.money).getTag().toString())));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
