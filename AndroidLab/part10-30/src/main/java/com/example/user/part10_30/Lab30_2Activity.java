package com.example.user.part10_30;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab30_2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener{

    TextureView textureView;
    ImageView recordBtn;

    //카메라 객체
    Camera camera;
    //카메라에서 지원하는 preview size 목록
    List<Camera.Size> supportedPreviewSizes;
    //최종 결정된 preview size
    Camera.Size previewSize;

    SurfaceTexture surface;

    MediaRecorder mediaRecorder;
    boolean isRecording;

    Drawable normalDr;
    Drawable activeDr;
    //촬영 방향 각도
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 200);

        }else {
            setContentView(R.layout.activity_lab30_2);

            textureView = (TextureView)findViewById(R.id.lab2_textureview);
            recordBtn=(ImageView)findViewById(R.id.lab2_btn);

            textureView.setSurfaceTextureListener(this);
            recordBtn.setOnClickListener(this);

        }

        normalDr= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_btn_normal, null);
        activeDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_btn_recording, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200 && grantResults.length>0) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED) {
                setContentView(R.layout.activity_lab30_2);

                textureView = (TextureView)findViewById(R.id.lab2_textureview);
                textureView.setSurfaceTextureListener(this);

                recordBtn=(ImageView)findViewById(R.id.lab2_btn);
                recordBtn.setOnClickListener(this);
            }
        }
    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //카메라 점유
        camera = Camera.open();

        Camera.Parameters parameters = camera.getParameters();
        supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        if (supportedPreviewSizes != null) {
            previewSize = CameraUtil.getOptimalPreviewSize(supportedPreviewSizes, width, height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }

        int result=CameraUtil.setCameraDisplayOrientation(this, 0);
        this.result=result;
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        //사진 촬영시 방향이 안맞는 데이터 전달
        parameters.setRotation(result);

        camera.setParameters(parameters);
        //화면에 출력되는 형상의 방향
        camera.setDisplayOrientation(result);

        try {
            camera.setPreviewTexture(surface);
        } catch (IOException t) {
        }

        camera.startPreview();

        this.surface=surface;

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, the Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
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
        if (camera != null) {
            //add~~~~~~~~~~~~~~~~~~~~~~~~~
            if(isRecording){
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder=null;

                isRecording=false;

                recordBtn.setImageDrawable(normalDr);
            }else {
                try{
                    File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp");
                    if(!dir.exists()){
                        dir.mkdir();
                    }
                    File file=File.createTempFile("VIDEO-",".3gp", dir);
                    mediaRecorder=new MediaRecorder();
                    camera.unlock();

                    mediaRecorder.setCamera(camera);
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                    mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                    mediaRecorder.setOutputFile(file.getAbsolutePath());
                    mediaRecorder.setOrientationHint(result);
                    mediaRecorder.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
                mediaRecorder.start();
                isRecording=true;
                recordBtn.setImageDrawable(activeDr);
            }


        }


    }

}

