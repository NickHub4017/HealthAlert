package com.nrv.healthalert;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
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
    View v ;
    Context basecon;
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
        int dt[]={dataFrame.spo2Percentage,dataFrame.pulseRate};

    //    sendDataArray("data",dt);
        //sendData("data",);
        //MainActivity.getDataFromCallback2(dataFrame.toString()+" - "+dataFrame.pulseRate+" - "+dataFrame.spo2Percentage);
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
        sendData("Start", "NA");

        cms50FWBluetoothConnectionManager = new CMS50FWBluetoothConnectionManager(CMS50FW_BLUETOOTH_DEVICE_NAME);
        CMS50FWConnectionListener cms50fwCallbacks = new HelathAlertCallback();
        cms50FWBluetoothConnectionManager.setCMS50FWConnectionListener(cms50fwCallbacks);


        try {
            cms50FWBluetoothConnectionManager.connect(getBaseContext());
        } catch (BluetoothNotAvailableException e) {
            e.printStackTrace();
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void sendData(String type,String data){
        //MainActivity.getDataFromCallback(data);
        Intent intent2 = new Intent();
        intent2.setAction("Get.data.Intent");
        intent2.putExtra("Type", type);
        intent2.putExtra("data", data);
        sendBroadcast(intent2);

    }
    public void sendData(String data) {
        MainActivity.getDataFromCallback(data);
    }
    public void sendDataArray(String type,int[] data){
        //MainActivity.getDataFromCallback(data);
        Intent intent2 = new Intent();
        intent2.setAction("Get.data.Intent");
        intent2.putExtra("Type", type);
        intent2.putExtra("data", data);
        sendBroadcast(intent2);

    }

    /*public static void startdata(){
        cms50FWBluetoothConnectionManager.startData();
    }*/

    public void processdata(DataFrame dataFrame){

    }


    public class ActToBroadcast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String y=intent.getStringExtra("Type");
            if(y.equals("Connect")){
                try {
                    cms50FWBluetoothConnectionManager.connect(basecon);
                } catch (BluetoothNotAvailableException e) {

                    e.printStackTrace();
                } catch (BluetoothNotEnabledException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    sendData("log",e.getMessage());
                }
            }
            else if(y.equals("Start")){
                cms50FWBluetoothConnectionManager.startData();
            }

        }
    }
}
