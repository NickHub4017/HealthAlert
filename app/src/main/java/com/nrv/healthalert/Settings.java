package com.nrv.healthalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by NRV on 10/16/2015.
 */
public class Settings extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final EditText spo2min_txt=(EditText)findViewById(R.id.txt_minspo2);
        final EditText spo2max_txt=(EditText)findViewById(R.id.txt_maxspo2);
        final EditText pulsemin_txt=(EditText)findViewById(R.id.txt_minpulse);
        final EditText pulsemax_txt=(EditText)findViewById(R.id.txt_maxpulse);
        Button submitbtn=(Button)findViewById(R.id.save_btn);
        try{
            int[] p=HelathAlertCallback.getdata();
            spo2min_txt.setText(""+p[1]);
            spo2max_txt.setText(""+p[3]);
            pulsemin_txt.setText(""+p[0]);
            pulsemax_txt.setText(""+p[2]);
        }
        catch (Exception e){

        }
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spo2min=spo2min_txt.getText().toString();
                String spo2max=spo2max_txt.getText().toString();
                String pulsemin=pulsemin_txt.getText().toString();
                String pulsemax=pulsemax_txt.getText().toString();
                int minspo2_int;
                int maxspo2_int;
                int minpulse_int;
                int maxpulse_int;
                boolean go=false;
                try{
                    minspo2_int=Integer.parseInt(spo2min);
                    go=true;
                }
                catch (Exception e){
                    minspo2_int=-1;
                    go=false;
                }

                try{
                    maxspo2_int=Integer.parseInt(spo2max);
                    go=true;
                }
                catch (Exception e){
                    maxspo2_int=-1;
                    go=false;
                }

                try{
                    minpulse_int=Integer.parseInt(pulsemin);
                    go=true;
                }
                catch (Exception e){
                    minpulse_int=-1;
                    go=false;
                }

                try{
                    maxpulse_int=Integer.parseInt(pulsemax);
                    go=true;

                }
                catch (Exception e){
                    maxpulse_int=-1;
                    go=false;
                }

                if(go){
                    /*Intent intent2 = new Intent();
                    intent2.setAction("Get.data.Service");
                    intent2.putExtra("Type", "data");
                    intent2.putExtra("minspo2", minspo2_int);
                    intent2.putExtra("maxspo2", maxspo2_int);
                    intent2.putExtra("minpulse", minpulse_int);
                    intent2.putExtra("maxpulse", maxpulse_int);
                    sendBroadcast(intent2);*/
                    try {
                        HelathAlertCallback.setupdata(minspo2_int, minpulse_int, maxspo2_int, maxpulse_int);
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Update FAil",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
}
