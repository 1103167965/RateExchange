package com.example.lib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyClass {
    public static void main(String[] args) {

        System.out.println("helloworld");
        InputStream is = null;
        BufferedReader br = null;
        HttpURLConnection http = null;
        StringBuffer sb = null;
        try {
            URL url = new URL("https://www.usd-cny.com/bankofchina.htm");
            http = (HttpURLConnection) url.openConnection();
            is = http.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "gb2312"));
            sb = new StringBuffer();
            String rl = br.readLine();
            while (rl != null) {
                sb.append(rl);
                rl = br.readLine();
            }
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
        Document dt = Jsoup.parse(sb.toString());
        System.out.println(Float.parseFloat(dt.getElementsByTag("table").get(0).getElementsByTag("tr").get(26).getElementsByTag("td").get(5).text())/100);
        Elements es=dt.getElementsByTag("table").get(0).getElementsByTag("tr");
        for(int i=1;i<es.size();i++){
            Element e=es.get(i);
            System.out.println(e.getElementsByTag("td").get(0).text()+"==>"+Float.parseFloat(e.getElementsByTag("td").get(5).text()));//+es.getElementsByTag("td").get(5).text());
        }
    }


}