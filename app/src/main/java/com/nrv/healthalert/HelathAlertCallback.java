package com.nrv.healthalert;

import android.app.FragmentManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.albertcbraun.cms50fwlib.BluetoothNotAvailableException;
import com.albertcbraun.cms50fwlib.BluetoothNotEnabledException;
import com.albertcbraun.cms50fwlib.CMS50FWBluetoothConnectionManager;
import com.albertcbraun.cms50fwlib.CMS50FWConnectionListener;
import com.albertcbraun.cms50fwlib.DataFrame;

/**
 * Created by NRV on 10/1/2015.
 */

public class HelathAlertCallback extends Service implements CMS50FWConnectionListener {
   static int minspo2=100;
    static int maxspo2=110;
    static int minpulse=50;
    static  int maxpulse=90;
    View v ;
   static Context basecon;
    CMS50FWBluetoothConnectionManager cms50FWBluetoothConnectionManager = null;
ActToBroadcast actToBroadcast=new ActToBroadcast();

    private static final String CMS50FW_BLUETOOTH_DEVICE_NAME = "SpO202";
    @Override
    public void onConnectionAttemptInProgress() {
        //sendData("ConnectAttempt","NA");
    }

    @Override
    public void onConnectionEstablished() {
//sendData("onConnectionEstablished","NA");

    }

    @Override
    public void onDataReadAttemptInProgress() {
  //      sendData("onDataReadAttemptInProgress","NA");
    }

    @Override
    public void onDataFrameArrived(DataFrame dataFrame) {
//        int dt[]={dataFrame.spo2Percentage,dataFrame.pulseRate};
        //MainActivity.getDataFromCallback2(dataFrame.toString());
       //sendData("data","dataFrame.toString()");
        //sendData("data",);
        try {
            if(!dataFrame.isFingerOutOfSleeve && dataFrame.spo2Percentage<=100) {
                MainActivity.getDataFromCallback2(" " + dataFrame.spo2Percentage, "              " + dataFrame.pulseRate);
                String spo2message="";
                String pulseratemessage="";
                boolean indanger=true;
                boolean isspo2error=false;
                boolean ispulseerror=false;
                if(maxspo2<=dataFrame.spo2Percentage){
                    spo2message="SPO2 Level is higer. ( "+dataFrame.spo2Percentage +" ).";
                    isspo2error=true;
                    Log.d("Error","SPO2High");

                }
                if(maxpulse<=dataFrame.pulseRate){
                    pulseratemessage="Pulse Rate is higer. ( "+dataFrame.pulseRate +" ).";
                    ispulseerror=true;
                    Log.d("Error","PulseHigh");
                }
                if(minspo2>=dataFrame.spo2Percentage){
                    spo2message="SPO2 Level is lower. ( "+dataFrame.spo2Percentage +" ).";
                    isspo2error=true;
                    Log.d("Error","SPO2LOW");
                }
                if(minpulse>=dataFrame.pulseRate){
                    pulseratemessage="Pulse Rate is lower. ( "+dataFrame.pulseRate +" ).";
                    ispulseerror=true;
                    Log.d("Error","PulseLOW");
                }
                if((isspo2error || ispulseerror)&& indanger ){
                    startAlert();

                }
                else{
                    Log.d("Error", "SPO2 Pulse " + dataFrame.pulseRate + " " + dataFrame.spo2Percentage);

                }


            }
            else{
                MainActivity.getDataFromCallback2(" NA", "              NA");
            }

        }catch (Exception e){
            e.printStackTrace();
//MainActivity.getDataFromCallback(e.getMessage());
        }
    }

    @Override
    public void onDataReadStopped() {
      //  sendData("onDataReadStopped","NA");
    }

    @Override
    public void onBrokenConnection() {
     //       sendData("onBrokenConnection","NA");
    }

    @Override
    public void onConnectionReset() {
//sendData("onConnectionReset","NA");
    }

    @Override
    public void onLogEvent(long timeMs, String message) {
  //      sendData("log",message);
        //sendData("onlog "+message);
        sendData(message);


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.data.Service");
        registerReceiver(actToBroadcast, movementFilter);

basecon=getBaseContext();
        int res= super.onStartCommand(intent, flags, startId);
        Log.d("Service", "Start");


        cms50FWBluetoothConnectionManager = new CMS50FWBluetoothConnectionManager(CMS50FW_BLUETOOTH_DEVICE_NAME);
        CMS50FWConnectionListener cms50fwCallbacks = new HelathAlertCallback();
        cms50FWBluetoothConnectionManager.setCMS50FWConnectionListener(cms50fwCallbacks);

/*
        try {
            cms50FWBluetoothConnectionManager.connect(getBaseContext());
        } catch (BluetoothNotAvailableException e) {
            e.printStackTrace();
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
        }*/
        return START_STICKY;
    }
    public static void setupdata(int minsp,int minpul,int maxsp,int maxpul){
        minpulse=minpul;
        minspo2=minsp;
        maxpulse=maxpul;
        maxspo2=maxsp;
        MainActivity.getDataFromCallback("Update data Successfully");
    }
    public static int[] getdata(){
        int data[]={minpulse,minspo2,maxpulse,maxspo2};
        return data;

    }
    public void startAlert(){
        Log.d("Error", "Indanger");
        //indanger=false;
        //MainActivity.getDataFromCallback("Alert");
        Intent alertIntent=new Intent(this,AlertDialog.class);
        alertIntent.putExtra("spo2msg","++++++");
        alertIntent.putExtra("pulsemsg","-------");
        alertIntent.putExtra("isspo2err",true);
        alertIntent.putExtra("ispulserr", true);
        alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(alertIntent);

    }

    public void sendData(String data) {
        MainActivity.getDataFromCallback(data);

    }


    public class ActToBroadcast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String y=intent.getStringExtra("Type");
            if(y.equals("Connect")){
                try {
                    cms50FWBluetoothConnectionManager.connect(basecon);
//                    startAlert();
                } catch (BluetoothNotAvailableException e) {

                    e.printStackTrace();
                } catch (BluetoothNotEnabledException e) {
                    e.printStackTrace();
                }
                catch (Exception e){

                }
            }
            else if(y.equals("Start")){
                cms50FWBluetoothConnectionManager.startData();
            }

            else if(y.equals("data")){
                cms50FWBluetoothConnectionManager.stopData();
                /*minspo2=intent.getIntExtra("minspo2",40);
                maxspo2=intent.getIntExtra("maxspo2",90);
                minpulse=intent.getIntExtra("minpulse",50);
                maxspo2=intent.getIntExtra("maxpulse",90);
*/
            }

        }
    }
}
