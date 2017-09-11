package com.example.user.part8_mission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener{

    GoogleApiClient googleApiClient;
    FusedLocationProviderApi fusedLocationProviderApi;
    GoogleMap map;
    Location location;

    Marker marker;
    TextView addressView;

    String resultAddress;
    double resultLat;
    double resultLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressView=(TextView)findViewById(R.id.mission1_address);

        googleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        fusedLocationProviderApi=LocationServices.FusedLocationApi;
        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mission1_map)).getMapAsync(this);


    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            location=fusedLocationProviderApi.getLastLocation(googleApiClient);
            moveMap();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length > 0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                location=fusedLocationProviderApi.getLastLocation(googleApiClient);
                moveMap();
            }else {
                showToast("no permission...");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast("access location fail...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveListener(this);
        moveMap();
    }

    private void moveMap(){
        if(location != null && map != null){
            LatLng gpsLatLng=new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition position=new CameraPosition.Builder().target(gpsLatLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            if(marker != null) marker.remove();
            marker=map.addMarker(new MarkerOptions().position(gpsLatLng).title("Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));

            MyGeocoderThread thread=new MyGeocoderThread(gpsLatLng);
            thread.start();
        }
    }

    @Override
    public void onCameraMove() {
        marker.setPosition(map.getCameraPosition().target);
    }

    @Override
    public void onCameraIdle() {
        MyGeocoderThread thread=new MyGeocoderThread(map.getCameraPosition().target);
        thread.start();
        resultLat=map.getCameraPosition().target.latitude;
        resultLng=map.getCameraPosition().target.longitude;
    }


    class MyGeocoderThread extends Thread {
        LatLng latLng;
        public MyGeocoderThread(LatLng latLng){
            this.latLng=latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder=new Geocoder(MainActivity.this);
            List<Address> addresses= null;
            String addressText="";
            try{
                addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Thread.sleep(500);
                if(addresses != null && addresses.size()>0){
                    Address address=addresses.get(0);
                    addressText=address.getAdminArea()+" "+(address.getMaxAddressLineIndex()>0 ? address.getAddressLine(0) : address.getLocality())+" ";

                    String txt=address.getSubLocality();
                    if(txt != null){
                        addressText += txt +" ";
                    }
                    addressText += address.getThoroughfare()+" "+address.getSubThoroughfare();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            Message message=new Message();
            message.what=100;
            message.obj=addressText;
            handler.sendMessage(message);

            resultAddress=addressText;

        }
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:{
                    addressView.setText((String)msg.obj);
                }
            }
        }
    };
}









