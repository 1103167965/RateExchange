package com.example.rateexchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static HashMap<String, Float> rate = new HashMap<>();
    public static MainActivity ma;
    public static String date;
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 5) {
                String str = (String) msg.obj;
                //Log.i("TAG", str);
               /* Pattern pattern = Pattern.compile(".*美元(.*?</td>){5}.*?<td>(.*?)</td>.*");//(\\D*)(\\d+)(.*)");
                Matcher matcher = pattern.matcher(str);
                if (matcher.matches())
                    rate.put("dollar", Float.parseFloat(matcher.group(2))/100);
                pattern = Pattern.compile(".*欧元(.*?</td>){5}.*?<td>(.*?)</td>.*");//(\\D*)(\\d+)(.*)");
                matcher = pattern.matcher(str);
                if (matcher.matches())
                    rate.put("euro", Float.parseFloat(matcher.group(2))/100);
                pattern = Pattern.compile(".*韩元(.*?</td>){5}.*?<td>(.*?)</td>.*");//(\\D*)(\\d+)(.*)");
                matcher = pattern.matcher(str);
                if (matcher.matches())
                    rate.put("won", Float.parseFloat(matcher.group(2))/100);*/
                Document dt = Jsoup.parse(str);
                rate.put("dollar", Float.parseFloat(dt.getElementsByTag("table").get(0).getElementsByTag("tr").get(26).getElementsByTag("td").get(5).text())/100);
                rate.put("euro", Float.parseFloat(dt.getElementsByTag("table").get(0).getElementsByTag("tr").get(7).getElementsByTag("td").get(5).text())/100);
                rate.put("won", Float.parseFloat(dt.getElementsByTag("table").get(0).getElementsByTag("tr").get(13).getElementsByTag("td").get(5).text())/100);
                date=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                Toast.makeText(ma, "获取汇率成功", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putFloat("dollar", rate.get("dollar"));
                ed.putFloat("euro", rate.get("euro"));
                ed.putFloat("won", rate.get("won"));
                ed.putString("date", date);
                ed.apply();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ma=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        if(sp.getString("date", "-").equalsIgnoreCase("-")){
            date=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        }else{
            date=sp.getString("date", "-");
        }
        if(compareDate(date))
            getrate();
        rate.put("dollar", sp.getFloat("dollar", 0.1477f));
        rate.put("euro", sp.getFloat("euro", 0.1256f));
        rate.put("won", sp.getFloat("won", 171.3421f));
    }

    public void open(View v) {
        Intent second = new Intent(this, MainActivity2.class);
        second.putExtra("dollar", rate.get("dollar"));
        second.putExtra("euro", rate.get("euro"));
        second.putExtra("won", rate.get("won"));
        startActivityForResult(second, 1);
    }

    public void change(View view) {
        EditText et = findViewById(R.id.editTextTextPersonName);
        Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher matcher = pattern.matcher(et.getText());
        if (matcher.matches()) {
            if(compareDate(date))
                getrate();
            et.setText(String.valueOf(Float.parseFloat(matcher.group()) * rate.get(view.getTag().toString())));
        } else {
            Toast.makeText(this, "输入金额", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            rate.put("dollar", data.getFloatExtra("dollar", 0f));
            rate.put("euro", data.getFloatExtra("euro", 0f));
            rate.put("won", data.getFloatExtra("won", 0f));
            SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putFloat("dollar", data.getFloatExtra("dollar", 0f));
            ed.putFloat("euro", data.getFloatExtra("euro", 0f));
            ed.putFloat("won", data.getFloatExtra("won", 0f));
            date=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            ed.putString("date", date);
            ed.apply();
        }
    }

    public void getrate(View view) {
        Thread t = new Thread(new Runnable() {
            InputStream is = null;
            BufferedReader br = null;
            HttpURLConnection http = null;

            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.usd-cny.com/bankofchina.htm");
                    http = (HttpURLConnection) url.openConnection();
                    is = http.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, "gb2312"));
                    StringBuffer sb = new StringBuffer();
                    String rl = br.readLine();
                    while (rl != null) {
                        sb.append(rl);
                        rl = br.readLine();
                    }
                    handler.sendMessage(handler.obtainMessage(5, sb.toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) br.close();
                        if (is != null) is.close();
                        if (http != null) http.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

    public void getrate() {
        Thread t = new Thread(new Runnable() {
            InputStream is = null;
            BufferedReader br = null;
            HttpURLConnection http = null;

            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.usd-cny.com/bankofchina.htm");
                    http = (HttpURLConnection) url.openConnection();
                    is = http.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, "gb2312"));
                    StringBuffer sb = new StringBuffer();
                    String rl = br.readLine();
                    while (rl != null) {
                        sb.append(rl);
                        rl = br.readLine();
                    }
                    handler.sendMessage(handler.obtainMessage(5, sb.toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) br.close();
                        if (is != null) is.close();
                        if (http != null) http.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showrate(View view) {
        startActivity(new Intent(this, RateListActivity.class));
    }


    public static boolean compareDate(String date2) {
        String da1[] = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()).split("-");
        if (Integer.parseInt(da1[3]) < 8) {
            Date beforeDay = getBeforeDay(new Date());
            da1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(beforeDay).split("-");
        }
        da1[3] = "8";
        da1[4] = "0";
        da1[5] = "0";
        String da2[] = date2.split("-");
        for (int i = 0; i < da1.length; i++)
            if (Integer.parseInt(da1[i]) > Integer.parseInt(da2[i]))
                return true;
            else if (Integer.parseInt(da1[i]) == Integer.parseInt(da2[i]))
                continue;
            else return false;
        return false;
    }

    public static Date getBeforeDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }
}