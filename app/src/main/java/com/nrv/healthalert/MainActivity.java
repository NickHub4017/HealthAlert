package com.nrv.healthalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    TextView spo2data=null;
    TextView pulsedata=null;
    public static void getDataFromCallback(String data){
    Toast.makeText(con,data,Toast.LENGTH_LONG).show();

    }
    public static void getDataFromCallback2(String data){
        //t.setText(data);
        Toast.makeText(con,data,Toast.LENGTH_LONG).show();

    }
     static TextView t;

    dataRecv dataRecv = new dataRecv();
    static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t=(TextView)findViewById(R.id.textView);
        con=getApplicationContext();
        setContentView(R.layout.activity_main);
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.data.Intent");
        registerReceiver(dataRecv, movementFilter);

        Intent Serviceintent3 = new Intent(MainActivity.this, HelathAlertCallback.class);
        startService(Serviceintent3);

        connectbtn=(Button)findViewById(R.id.connect_btn);
        connectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent2 = new Intent();
                intent2.setAction("Get.data.Service");
                intent2.putExtra("Type", "Start");
                sendBroadcast(intent2);

                //HelathAlertCallback.startdata();
            }
        });

        spo2data=(TextView)findViewById(R.id.lbl_data_spo2precentage);
        pulsedata=(TextView)findViewById(R.id.lbl_data_pulserate);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class dataRecv extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
           // Toast.makeText(getApplicationContext(), "dyn got it.." + intent.getStringExtra("Type"), Toast.LENGTH_LONG).show();
            String type=intent.getStringExtra("Type");
            if(type.equals("Start")){
                //enable buttons
            }
            else if(type.equals("ConnectAttempt")){
                //
            }
            else if(type.equals("onConnectionEstablished")){
                //
            }
            else if(type.equals("onDataReadAttemptInProgress")){
                //
            }
            else if(type.equals("onDataReadStopped")){
                //
            }
            else if(type.equals("onBrokenConnection")){
                //
            }
            else if(type.equals("onConnectionReset")){
                //
            }
            else if(type.equals("data")){
                int[] data=intent.getIntArrayExtra("data");
                spo2data.setText(""+data[0]);
                pulsedata.setText(""+data[1]);
                //
            }
            else if(type.equals("log")){
                String data=intent.getStringExtra("data");
               Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                //
            }

        }
    }



}
