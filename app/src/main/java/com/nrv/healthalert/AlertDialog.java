package com.nrv.healthalert;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;

/**
 * Created by NRV on 10/17/2015.
 */
public class AlertDialog extends ActionBarActivity {
    Button pausebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog);
        Intent p=getIntent();
        String spo2msg=p.getStringExtra("spo2msg");
        String pulsemsg=p.getStringExtra("pulsemsg");
        boolean errsp=p.getBooleanExtra("isspo2err", false);
        boolean errpulse=p.getBooleanExtra("ispulserr",false);
        TextView spbox=(TextView)findViewById(R.id.spo2Alert);
        TextView pulbox=(TextView)findViewById(R.id.pulseAlert);
        pausebtn=(Button)findViewById(R.id.alertpause);

        pausebtn.setText("Reset (10)");
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000,
                pendingIntent);

        if(errsp){
            spbox.setBackgroundColor(Color.RED);
            spbox.setText(spo2msg);
        }
        else{
            spbox.setBackgroundColor(Color.GREEN);
            spbox.setText(" No SpO2 error");
        }
        if(errpulse){
            pulbox.setBackgroundColor(Color.RED);
            pulbox.setText(pulsemsg);
        }
        else{
            pulbox.setBackgroundColor(Color.RED);
            pulbox.setText(" No Pulse error");
        }
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                alarmManager.cancel(pendingIntent);
            }
        });



    }
    public class AlarmReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            //get and send location information
            String btnlbl=pausebtn.getText().toString();
            String curtime=btnlbl.split(" ")[1].replace("(","").replace(")","");
            int tm=Integer.parseInt(curtime);
            tm--;
            pausebtn.setText("Reset ("+tm+")");
            if(tm==0){
                AlarmManager alarmManager=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intenter = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intenter, 0);
                alarmManager.cancel(pendingIntent);

            }
        }
    }

}
