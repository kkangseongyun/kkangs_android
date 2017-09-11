package com.example.user.part8_23;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.lab1_map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

        if(map != null){
            LatLng latLng=new LatLng(37.566643, 126.978279);
            CameraPosition position=new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            markerOptions.position(latLng);
            markerOptions.title("서울시청");
            markerOptions.snippet("Tel:01-120");

            map.addMarker(markerOptions);

            MyGeocodingThread thread=new MyGeocodingThread(latLng);
            thread.start();

            String address="서울특별시 중구 서소문동 37-9";
            MyReverseGeocodingThread reverseThread=new MyReverseGeocodingThread(address);
            reverseThread.start();

        }
    }

    class MyReverseGeocodingThread extends Thread {
        String address;
        public MyReverseGeocodingThread(String address){
            this.address=address;
        }

        @Override
        public void run() {
            Geocoder geocoder=new Geocoder(MainActivity.this);
            try{
                List<Address> results=geocoder.getFromLocationName(address, 1);
                Address resultAddress=results.get(0);
                LatLng latLng=new LatLng(resultAddress.getLatitude(), resultAddress.getLongitude());

                Message msg=new Message();
                msg.what=200;
                msg.obj=latLng;
                handler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyGeocodingThread extends Thread {
        LatLng latLng;
        public MyGeocodingThread(LatLng latLng){
            this.latLng=latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder=new Geocoder(MainActivity.this);
            List<Address> addresses=null;
            String addressText="";
            try{
                addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Thread.sleep(500);
                if(addresses != null && addresses.size()>0){
                    Address address=addresses.get(0);
                    addressText=address.getAdminArea()+" "+(address.getMaxAddressLineIndex()>0 ?
                            address.getAddressLine(0) : address.getLocality())+" ";
                    String txt=address.getSubLocality();
                    if(txt != null)
                        addressText += txt+" ";
                    addressText += address.getThoroughfare()+ " "+address.getSubThoroughfare();

                    Message msg=new Message();
                    msg.what=100;
                    msg.obj=addressText;
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast toast=Toast.makeText(MainActivity.this, (String)msg.obj, Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 200:
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
                    markerOptions.position((LatLng)msg.obj);
                    markerOptions.title("서울시립미술관");
                    map.addMarker(markerOptions);
            }
        }
    };
}
