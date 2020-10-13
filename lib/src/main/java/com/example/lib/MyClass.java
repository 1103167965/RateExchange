package com.example.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyClass {
    public static void main(String[] args) {
        String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        System.out.println(compareDate("2020-10-13-8-49-14"));
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