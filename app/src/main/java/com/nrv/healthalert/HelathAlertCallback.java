package com.nrv.healthalert;

import android.app.FragmentManager;
import android.app.IntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.albertcbraun.cms50fwlib.BluetoothNotAvailableException;
import com.albertcbraun.cms50fwlib.BluetoothNotEnabledException;
import com.albertcbraun.cms50fwlib.CMS50FWBluetoothConnectionManager;
import com.albertcbraun.cms50fwlib.CMS50FWConnectionListener;
import com.albertcbraun.cms50fwlib.DataFrame;

import java.util.Set;

/**
 * Created by NRV on 10/1/2015.
 */

public class HelathAlertCallback extends Service implements CMS50FWConnectionListener {
   static int minspo2=0;
    static int maxspo2=0;
    static int minpulse=0;
    static  int maxpulse=0;
    static String tel="";
    boolean issmsgone=false;
    long startedtime=0,prev=0;
    View v ;
DBLink dbLink=null;
   static Context basecon,actstartcon;
    boolean isscreengone=false;
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
                String spo2message="SPO2 Level is Good";
                String pulseratemessage="Pulse Rate is Good";
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
                    Log.d("Error", "PulseLOW");
                }

                if((isspo2error || ispulseerror)&& indanger && !isscreengone ){
//Notify("Alert",spo2message + " "+pulseratemessage);
                    startedtime=dataFrame.time;
                    startAlert(startedtime,spo2message,pulseratemessage);


                }


                if(isscreengone){
                    Intent j=new Intent();
                    j.setAction("Send.data.notify");
                    j.putExtra("data", dataFrame.time);
                    basecon.sendBroadcast(j);
                    long gap=((dataFrame.time/1000) - (startedtime/1000));

                    if(gap>10 && !issmsgone && AlertDialog.gosmsalert){
                        //sendsms
                        issmsgone=true;

                        String phoneNo = tel;
                        String msg = "Health Alert....";
                        if(isspo2error && !ispulseerror) {
                            msg = msg + spo2message;
                        }

                        if(ispulseerror && !isspo2error){
                            msg = msg + pulseratemessage;
                        }
                        if(ispulseerror && isspo2error){
                            msg = msg +" "+pulseratemessage+ " "+spo2message;
                        }

                        try {

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                            //Toast.makeText(getApplicationContext(),"Your submission is send to head office. Please check your email/sms inbox." , Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                        //    Toast.makeText(getApplicationContext(),
                          //          ex.getMessage().toString(),
                            //        Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }


                    }
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
        dbLink=new DBLink(getBaseContext());

        maxspo2=dbLink.getHighOxyLevel();
        maxpulse=dbLink.getHighPulLevel();
        minpulse=dbLink.getLowPulseLevel();
        minspo2=dbLink.getLowOxyLevel();
        tel=dbLink.getphoneno();
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
    public static void setupdata(int minsp,int minpul,int maxsp,int maxpul,String telno){
        minpulse=minpul;
        minspo2=minsp;
        maxpulse=maxpul;
        maxspo2=maxsp;
        tel=telno;
        MainActivity.getDataFromCallback("Update data Successfully");
    }
    public static int[] getdata(){
        int data[]={minpulse,minspo2,maxpulse,maxspo2};
        return data;

    }
    public static void setCont(Context p){
        actstartcon=p;
    }
    public void startAlert(long y,String spmsg,String pulmsg){
        isscreengone=true;
        Log.d("Error", "Indanger");
        //indanger=false;
        //MainActivity.getDataFromCallback("Alert");
        Intent alertIntent=new Intent(actstartcon,AlertDialog.class);
        alertIntent.putExtra("spo2msg",spmsg);
        alertIntent.putExtra("pulsemsg",pulmsg);
        alertIntent.putExtra("time",y);
        alertIntent.putExtra("isspo2err", true);
        alertIntent.putExtra("ispulserr", true);
       alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        basecon.startActivity(alertIntent);

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
