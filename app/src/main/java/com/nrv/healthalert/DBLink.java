package com.nrv.healthalert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NRV on 10/1/2015.
 */
public class DBLink extends SQLiteOpenHelper {
    public DBLink(Context context) {
        super(context, "healthlog", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String logTable_create="CREATE TABLE logdata(\n" +

                "    logat DATETIME DEFAULT CURRENT_TIMESTAMP PRIMARY KEY\n" +
                "    oxlevel INTEGER ,\n" +
                "    circulate INTEGER,\n" +
                ");";
        sqLiteDatabase.execSQL(logTable_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
