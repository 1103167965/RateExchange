package com.example.rateexchange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DB_NAME="myrate.db";
    private static final String TB_NAME="tb_rates";
    public DBHelper(Context context){
        this(context,TB_NAME,null,VERSION);
    }
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TB_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,KIND TEXT,RATE TEXT)");
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void add(HashMap<String,Float> hm){
        SQLiteDatabase wb = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        for (String i : hm.keySet()) {
            values.put("KIND", i);
            values.put("RATE", String.valueOf(hm.get(i)));
            wb.insert(TB_NAME,null,values);
        }
        wb.close();
    }
    public HashMap<String,Float> listAll(){
        HashMap<String,Float> hm= new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, null);//(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null)
        if(cursor!=null){
            while(cursor.moveToNext()){
                hm.put(cursor.getString(cursor.getColumnIndex("KIND")),Float.parseFloat(cursor.getString(cursor.getColumnIndex("RATE"))));
                Log.i(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))), cursor.getString(cursor.getColumnIndex("KIND"))+Float.parseFloat(cursor.getString(cursor.getColumnIndex("RATE"))));
            }
            cursor.close();
        }
        db.close();
        return hm;
    }
}
