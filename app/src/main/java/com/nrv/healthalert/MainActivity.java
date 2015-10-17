package com.nrv.healthalert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Button connectbtn=null;
    Button startbtn=null;
    static TextView spo2data=null;
    static TextView pulsedata=null;
    static TextView lblview=null;
    getFromService getService=null;
DBLink dbLink=null;
    public static void getDataFromCallback(String data){
        //spo2data.setText("hii");
    //Toast.makeText(con,data,Toast.LENGTH_SHORT).show();
        t.append("\n"+data);

    }

     static TextView t;
    static Context refContext;
    Intent Serviceintent3;
    static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbLink=new DBLink(this);
        spo2data=(TextView)findViewById(R.id.lbl_data_spo2precentage);
        pulsedata=(TextView)findViewById(R.id.lbl_data_pulserate);
refContext=MainActivity.this;

        t=(TextView)findViewById(R.id.textView);

        //lblview.setText("lbl");

        //t.setText("p");
        getService=new getFromService();
        IntentFilter getdatafilter=new IntentFilter("Send.data.service");
        registerReceiver(getService,getdatafilter);

        con=getApplicationContext();

       // MediaPlayer mp=new MediaPlayer();


        lblview=(TextView)findViewById(R.id.lbl_data);
        //Toast.makeText(getApplicationContext(),""+dbLink.getHighOxyLevel()+" "+dbLink.getHighPulLevel()+" "+dbLink.getLowOxyLevel()+" "+dbLink.getLowPulseLevel(),Toast.LENGTH_SHORT).show();
        Serviceintent3 = new Intent(MainActivity.this, HelathAlertCallback.class);
        startService(Serviceintent3);
        //bindService(Serviceintent3,)
        connectbtn=(Button)findViewById(R.id.connect_btn);
        connectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setText("Connection Log");
                Intent intent2 = new Intent();
                intent2.setAction("Get.data.Service");
                intent2.putExtra("Type", "Connect");
                sendBroadcast(intent2);


                //HelathAlertCallback.startdata();
            }
        });

        startbtn=(Button)findViewById(R.id.start_btn);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelathAlertCallback.setCont(MainActivity.this);
                Intent intent2 = new Intent();
                intent2.setAction("Get.data.Service");
                intent2.putExtra("Type", "Start");
                sendBroadcast(intent2);
//                Notify("pp","ok");
//HelathAlertCallback.setContext(MainActivity.this);


                //HelathAlertCallback.startdata();
            }
        });
    Button resetBtn=(Button)findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(Serviceintent3);
                startService(Serviceintent3);
                AlertDialog.isShown=false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings=new Intent(MainActivity.this,Settings.class);
            startActivity(settings);
            return true;
        }
        if (id == R.id.action_log) {
            Intent settings=new Intent(MainActivity.this,ViewLog.class);
            startActivity(settings);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public static void getDataFromCallback2(String spdata,String prate){
        //t.setText(data);
        try {
            spo2data.setText(spdata);
            pulsedata.setText(prate);

        }catch (Exception e){
           Toast.makeText(con,""+(lblview==null),Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(con,data,Toast.LENGTH_LONG).show();

    }


public class getFromService extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {


    }
}



}
