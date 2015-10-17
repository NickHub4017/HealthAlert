package com.nrv.healthalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NRV on 10/1/2015.
 */
public class DBLink extends SQLiteOpenHelper {
    public DBLink(Context context) {
        super(context, "healthlog,db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String logmainTable_create="CREATE TABLE logmaindata(\n" +

                "    titl TEXT PRIMARY KEY,\n" +
                "    data TEXT \n" +
                ");";
        sqLiteDatabase.execSQL(logmainTable_create);

        ContentValues dummydata=new ContentValues();
        dummydata.put("titl","lowoxy");
        dummydata.put("data",50);
        sqLiteDatabase.insert("logmaindata", null, dummydata);
        dummydata.put("titl", "highoxy");
        dummydata.put("data",99);
        sqLiteDatabase.insert("logmaindata", null, dummydata);
        dummydata.put("titl", "lowpul");
        dummydata.put("data",50);
        sqLiteDatabase.insert("logmaindata",null,dummydata);
        dummydata.put("titl","highpul");
        dummydata.put("data",80);
        sqLiteDatabase.insert("logmaindata",null,dummydata);

        dummydata.put("titl","telno");
        dummydata.put("data","0719720470");
        sqLiteDatabase.insert("logmaindata",null,dummydata);

        String logTable_create="CREATE TABLE logdata(\n" +

                "    logat DATETIME DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,\n" +
                "    oxlevel INTEGER ,\n" +
                "    pullevel INTEGER\n" +
                ");";
        sqLiteDatabase.execSQL(logTable_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void setLowOxyLevel(int level){
        SQLiteDatabase p=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("data",level);
        p.update("logmaindata", v, "titl" + " = ?", new String[]{"lowoxy"});
    }
    public void setHighOxyLevel(int level){
        SQLiteDatabase p=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("data",level);
        p.update("logmaindata", v, "titl" + " = ?", new String[]{"highoxy"});
    }
public void addLog(int oxlevel,int pul){
    SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    ContentValues dummydata=new ContentValues();
    dummydata.put("oxlevel",oxlevel);
    dummydata.put("pullevel",pul);
    sqLiteDatabase.insert("logmaindata", null, dummydata);


}
    public void setLowPulseLevel(int level){
        SQLiteDatabase p=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("data",level);
        p.update("logmaindata",v,"titl"+" = ?",new String[] {"lowpul"});
    }
    public void setHighPulLevel(int level){
        SQLiteDatabase p=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("data",level);
        p.update("logmaindata", v, "titl" + " = ?", new String[]{"highpul"});
    }
    public void setTelno(String tel){
        SQLiteDatabase p=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("data",tel);
        p.update("logmaindata", v, "titl" + " = ?", new String[]{"telno"});
    }

    public int getLowOxyLevel(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='lowoxy'";
        Cursor c=p.rawQuery(q, null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex("data"));
        }
        return 0;
    }
    public String getphoneno(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='telno'";
        Cursor c=p.rawQuery(q, null);
        if(c.moveToFirst()){
            return c.getString(c.getColumnIndex("data"));
        }
        return null;
    }
    public int getHighOxyLevel(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='highoxy'";
        Cursor c=p.rawQuery(q,null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex("data"));
        }
        return 0;
    }
    public Cursor getLog(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='highoxy'";
        Cursor c=p.rawQuery(q,null);
        return c;
    }

    public int getLowPulseLevel(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='lowpul'";
        Cursor c=p.rawQuery(q,null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex("data"));
        }
        return 0;


    }
    public int getHighPulLevel(){
        SQLiteDatabase p=this.getReadableDatabase();
        String q="Select * from logmaindata where titl='highpul'";
        Cursor c=p.rawQuery(q,null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex("data"));
        }
        return 0;

    }




}
