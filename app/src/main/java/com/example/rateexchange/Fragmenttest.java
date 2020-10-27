package com.example.rateexchange;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Fragmenttest extends FragmentActivity {
    homefragment[] fragments;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenttest);
        fragments = new homefragment[3];
        fragmentManager = getSupportFragmentManager();
        fragments[0]= (homefragment) fragmentManager.findFragmentById(R.id.fragment);
        fragments[1]= (homefragment) fragmentManager.findFragmentById(R.id.fragment2);
        fragments[2]= (homefragment) fragmentManager.findFragmentById(R.id.fragment3);
        fragmentTransaction = fragmentManager.beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);
        fragmentTransaction.show(fragments[0]).commit();
        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction fra = fragmentManager.beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);
                String s="";
                s= String.valueOf(findViewById(i).getTag());
                switch (i){
                    case R.id.radioButton:
                        i=R.id.fragment;
                        break;
                    case R.id.radioButton2:
                        i=R.id.fragment2;
                        break;
                    case R.id.radioButton3:
                        i=R.id.fragment3;
                        break;
                }
                homefragment fragment=(homefragment) fragmentManager.findFragmentById(i);
                fragment.setTest(s);
                fra.show(fragment).commit();
            }
        });
    }

}
