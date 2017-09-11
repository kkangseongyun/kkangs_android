package com.example.user.part10_28;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener{

    TextureView textureView;
    ImageView imageView;

    //add~~~~~~~~~~~~~
    Camera camera;
    List<Camera.Size> supportedPreviewSizes;
    Camera.Size previewSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

        }else {
            setContentView(R.layout.activity_main);

            textureView = (TextureView)findViewById(R.id.lab1_textureview);
            imageView=(ImageView)findViewById(R.id.lab1_btn);

            textureView.setSurfaceTextureListener(this);
            imageView.setOnClickListener(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200 && grantResults.length>0) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                setContentView(R.layout.activity_main);

                textureView = (TextureView)findViewById(R.id.lab1_textureview);
                textureView.setSurfaceTextureListener(this);

                imageView=(ImageView)findViewById(R.id.lab1_btn);
                imageView.setOnClickListener(this);
            }
        }
    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //add~~~~~~~~~~~~~~~~~~~~~~~~~
        camera=Camera.open();

        Camera.Parameters parameters=camera.getParameters();
        supportedPreviewSizes=parameters.getSupportedPreviewSizes();
        if(supportedPreviewSizes != null){
            previewSize=CameraUtil.getOptimalPreviewSize(supportedPreviewSizes, width, height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }
        int result=CameraUtil.setCameraDisplayOrientation(this, 0);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setRotation(result);

        camera.setParameters(parameters);

        camera.setDisplayOrientation(result);
        try{
            camera.setPreviewTexture(surface);
        }catch (Exception e){
        }

        camera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //add~~~~~~~~~~~~~~~~~~
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
    }

    @Override
    public void onClick(View v) {
        //add~~~~~~~~~~~~~~~~~~~~~~~~
        if(camera != null){
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    FileOutputStream fos;
                    try{
                        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp");
                        if(!dir.exists()){
                            dir.mkdir();
                        }
                        File file=File.createTempFile("IMG-",".jpg", dir);
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        fos=new FileOutputStream(file);
                        fos.write(data);
                        fos.flush();
                        fos.close();
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                    camera.startPreview();
                }
            });
        }

    }

}
