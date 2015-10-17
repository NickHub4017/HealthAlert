package com.nrv.healthalert;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.bluetooth.BluetoothHealthCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;

/**
 * Created by NRV on 10/17/2015.
 */
public class AlertDialog extends ActionBarActivity {
    Button pausebtn;
    static boolean gosmsalert=true;
    static boolean isShown=false;
    long tm=0;
    MediaPlayer mp=new MediaPlayer();

    @Override
    protected void onResume() {
        super.onResume();
        isShown=true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog);/**/

        Intent p=getIntent();

            final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.mipmap.palert);
            mp.setLooping(true);
            mp.start();



        String spo2msg=p.getStringExtra("spo2msg");
        String pulsemsg=p.getStringExtra("pulsemsg");
        boolean errsp=p.getBooleanExtra("isspo2err", false);
        boolean errpulse=p.getBooleanExtra("ispulserr",false);
        tm=p.getLongExtra("time",0)/1000;
        TextView spbox=(TextView)findViewById(R.id.spo2Alert);
        TextView pulbox=(TextView)findViewById(R.id.pulseAlert);
        pausebtn=(Button)findViewById(R.id.alertpause);

        pausebtn.setText("Reset (10)");

        AlarmReceiver alertdialog=new AlarmReceiver();
        IntentFilter getdatafilter=new IntentFilter("Send.data.notify");
        registerReceiver(alertdialog, getdatafilter);

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
               gosmsalert=false;
                HelathAlertCallback.reset();
                //if(mp!=null){


                    mp.pause();
                    mp.setLooping(false);
                    mp.stop();
                //}
            }
        });



    }
    public class AlarmReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            long k=intent.getLongExtra("data",0)/1000;
            long timegap=10-(k-tm);
            if(timegap>=0) {
                pausebtn.setText("Reset (" + timegap + ")");
            }

        }
    }


}
