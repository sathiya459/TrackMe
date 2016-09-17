package com.example.sathya_g.trackme;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {
    MarkerOptions markerOptions;
    public static int Zoom = 18;
    public static int diff = 0;
    public static int count = 10;
    public static Double latitude;
    public static Double longitude;
    public static int UpdateInterval = 10;
    public static int i = UpdateInterval;

    public static String SqlUpdateString = "http://www.androidelectriccontrol.netne.net/mylocation/updateInterval.php?interval=";
    public static String SqlGetString = "http://www.androidelectriccontrol.netne.net/mylocation/getmylocationtoandroid.php";
    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Button ZoomIn = (Button) findViewById(R.id.button);
        Button ZoomOut = (Button) findViewById(R.id.button2);
        Button IncFreq = (Button) findViewById(R.id.button3);
        Button DecFreq = (Button) findViewById(R.id.button4);
        final EditText countText = (EditText) findViewById(R.id.editText);
        ZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Zoom<20)Zoom++;
                    addMarker(latitude, longitude);
            }
        });
        ZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Zoom>0)Zoom--;
                addMarker(latitude, longitude);

            }
        });
        IncFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff=5;
                if(UpdateInterval<=55){
                    new AsyncClass().execute(SqlUpdateString+""+(UpdateInterval+diff));
                }

            }
        });
        DecFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff=-5;
                if(UpdateInterval>=10){
                    new AsyncClass().execute(SqlUpdateString+""+(UpdateInterval+diff));
                }
            }
        });

        Thread nThread = new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("thread started..");

                    while(true) {
                        i--;
                        if(i<0) {
                            i = UpdateInterval;
                            new AsyncClass().execute(SqlGetString);
                            sleep(2000);
                        }
                            countText.post(new Runnable() {
                                @Override
                                public void run() {
                                    countText.setText("Updates in " + i + " secs...");
                                }
                            });
                            sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



        };
        nThread.start();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private static void setUpMap() {
        latitude=0.0;
        longitude=0.0;
        mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title(""));

        latitude=11.91090132;
        longitude=77.71337752;
        addMarker(latitude,longitude);


    }



    public static void responceFromAsync(String resp) {
        System.out.println(resp);
        String[] result= resp.split("`");
        if(result[0].equals("success")){
            System.out.println("Marker will be added......................");
            latitude = Double.parseDouble(result[1]);
            longitude = Double.parseDouble(result[2]);
            addMarker(latitude, longitude);
            //setUpMap();

        }else if(result[0].equals("updated")){
            System.out.println("Change request completed......................");

            UpdateInterval=Integer.parseInt(result[1]);

            i=UpdateInterval;
        }
    }

    static void addMarker(Double latitude, Double longitude) {
        clearAllMarkers();
        LatLng newLatLng = new LatLng(latitude, longitude);
        MarkerOptions mMarkerOptions = new MarkerOptions().position(newLatLng).title("Sathya");
        //mMap.addMarker(mMarkerOptions);
        Marker marker  = mMap.addMarker(mMarkerOptions);
        marker.showInfoWindow();
        moveCamara(newLatLng);
    }

    private static void clearAllMarkers() {
        mMap.clear();
    }
    private static void moveCamara(LatLng mLatLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, Zoom);
        mMap.animateCamera(cameraUpdate);

    }

}
