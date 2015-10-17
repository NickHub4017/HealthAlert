package com.nrv.healthalert;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewLog extends ActionBarActivity {
    DBLink dbLink=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);
        TextView log=(TextView)findViewById(R.id.log);
        log.setText("No data to Show");

        dbLink=new DBLink(this);

        Cursor c=dbLink.getLog();

        if(c.moveToFirst()){
            log.setText(" DATE_TIME             SPO2Value          Pulse_Rate");
            do{
                log.append(c.getString(0)+ "     "+c.getString(1)+ "    "+c.getString(2)+"\n");
            }while (c.moveToNext());
        }

    }


}
