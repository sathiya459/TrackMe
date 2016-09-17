package com.example.sathya_g.trackme;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sathya-G on 26-Jul-16.
 */
public class AsyncClass extends AsyncTask<String, Void, String>{

    //String ip="www.androidelectriccontrol.netne.net",resp="";
    String ip="www.sampledomain.site88.net",resp="";
    private void callMyToast(String s) {
        System.out.println(s);
    }





    @Override
    protected String doInBackground(String... params) {
        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        System.out.println(simpleDateFormat.format(d));

        //System.out.println(simpleDateFormat.format("2000-01-01-01-01-00")-simpleDateFormat.format("2000-01-01-01-01-00"));
       // MapsActivity.responceFromAsync(resp);


        //String url="http://www.androidelectriccontrol.netne.net/getstatus.php?";
        String url=params[0];
        //String url="http://www.sampledomain.site88.net/index.php";
        try {
            URL call= new URL(url);
            HttpURLConnection con=(HttpURLConnection)call.openConnection();
            con.setConnectTimeout(5000);
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String ss="";
            while((ss=reader.readLine()) != null){
                resp=resp+ss;

            }
             callMyToast("response : " + url);
            callMyToast("response = "+resp);
        } catch (MalformedURLException e) {
            callMyToast("MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            callMyToast("IOException");
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {
            callMyToast("NetworkOnMainThreadException");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
        MapsActivity.responceFromAsync(resp);


    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
