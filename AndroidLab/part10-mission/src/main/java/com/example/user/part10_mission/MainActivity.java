package com.example.user.part10_mission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
    ImageView recordBtn;
    ImageView videoBtn;
    ImageView pictureBtn;

    boolean isVideoBtnEnable=false;
    boolean isPictureBtnEnable=true;
    boolean isRecording;

    Camera camera;
    List<Camera.Size> supportedPreviewSizes;
    Camera.Size previewSize;

    SurfaceTexture surface;

    MediaRecorder mediaRecorder;

    Drawable recordNormalDr;
    Drawable recordActiveDr;
    Drawable videoDisableDr;
    Drawable videoEnableDr;
    Drawable pictureDisableDr;
    Drawable pictureEnableDr;

    int result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO}, 200);
        }else {
            initLayout();
        }

        recordNormalDr= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_btn_normal, null);
        recordActiveDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_btn_recording, null);
        videoDisableDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video_disable, null);
        videoEnableDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video, null);
        pictureDisableDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_picture_disable, null);
        pictureEnableDr=ResourcesCompat.getDrawable(getResources(), R.drawable.ic_picture, null);

    }

    private void initLayout(){
        setContentView(R.layout.activity_main);

        textureView=(TextureView)findViewById(R.id.lab1_textureview);
        recordBtn=(ImageView)findViewById(R.id.lab1_record_btn);
        videoBtn=(ImageView)findViewById(R.id.lab1_video_btn);
        pictureBtn=(ImageView)findViewById(R.id.lab1_picture_btn);

        textureView.setSurfaceTextureListener(this);
        recordBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        pictureBtn.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                initLayout();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        camera=Camera.open();

        Camera.Parameters parameters=camera.getParameters();
        supportedPreviewSizes=parameters.getSupportedPreviewSizes();
        if(supportedPreviewSizes != null){
            previewSize=CameraUtil.getOptimalPreviewSize(supportedPreviewSizes, width, height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }

        int result=CameraUtil.setCameraDisplayOrientation(this, 0);
        this.result=result;
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setRotation(result);

        camera.setParameters(parameters);
        camera.setDisplayOrientation(result);

        try{
            camera.setPreviewTexture(surface);
        }catch (Exception e){}
        camera.startPreview();

        this.surface=surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v==pictureBtn){
            pictureBtn.setImageDrawable(pictureEnableDr);
            videoBtn.setImageDrawable(videoDisableDr);

            isVideoBtnEnable=false;
            isPictureBtnEnable=true;
        }else if(v==videoBtn){
            pictureBtn.setImageDrawable(pictureDisableDr);
            videoBtn.setImageDrawable(videoEnableDr);

            isVideoBtnEnable=true;
            isPictureBtnEnable=false;
        }else if(v==recordBtn){
            if(camera != null){
                if(isPictureBtnEnable){
                    recordBtn.setImageDrawable(recordActiveDr);
                    recordPicture();
                }else if(isVideoBtnEnable){
                    recordVideo();
                }
            }
        }
    }

    private void recordPicture(){
        camera.takePicture(null, null, new Camera.PictureCallback(){
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
                recordBtn.setImageDrawable(recordNormalDr);
            }
        });
    }
    private void recordVideo(){
        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder=null;
            isRecording=false;
            recordBtn.setImageDrawable(recordNormalDr);
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
            recordBtn.setImageDrawable(recordActiveDr);
        }
    }
}
