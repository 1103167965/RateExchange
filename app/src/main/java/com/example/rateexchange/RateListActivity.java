package com.example.rateexchange;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<HashMap<String, String>> li = new ArrayList<>();
        for (String i : MainActivity.rate.keySet()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", i);
            map.put("detail", String.valueOf(MainActivity.rate.get(i)));
            li.add(map);
        }
        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
        ListView lv = (ListView) findViewById(R.id.mylist);
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        myAdapter ma = new myAdapter(this, R.layout.activity_list, li);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(ma);
    }
}

class myAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
    public myAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list, parent, false);
        }
        Map<String, String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView detail = (TextView) itemView.findViewById(R.id.detail);
        title.setText("Title:" + map.get("title"));
        detail.setText("detail:" + map.get("detail"));
        return itemView;

    }

    String TAG="lalala:";
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Object itemAtPosition = parent.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("title");
        String detailStr = map.get("detail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView detail = (TextView) view.findViewById(R.id.detail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);
    }
}
