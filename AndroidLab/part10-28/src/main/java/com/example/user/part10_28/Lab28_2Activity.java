package com.example.user.part10_28;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Lab28_2Activity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    SurfaceHolder holder;

    HandlerThread thread;
    Handler handler;

    SurfaceView surfaceView;
    ImageView imageView;

    CameraManager manager;
    CameraDevice camera;

    //surface에서 사진데이터 추출
    ImageReader reader;
    //surface 화면을 preview 로 찍거나 사진추출 기능 제공..
    CameraCaptureSession session;
    //카메라 정보
    CameraCharacteristics characteristics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab28_2);

        surfaceView = (SurfaceView) findViewById(R.id.lab2_surface);
        imageView = (ImageView) findViewById(R.id.lab2_btn);
        imageView.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

        } else {
            holder = surfaceView.getHolder();
            holder.addCallback(this);

            thread = new HandlerThread("background");
            thread.start();
            handler = new Handler(thread.getLooper());

            manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                holder = surfaceView.getHolder();

                holder.addCallback(this);

                thread = new HandlerThread("background");
                thread.start();
                handler = new Handler(thread.getLooper());

                manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            }
        }
    }

    //개발자 함수.. button click 시 호출.. 사진 촬영
    @Override
    public void onClick(View v) {
        if (session != null) {
            try {
                //add~~~~~~~~~~~~~~~~~~~~
                CaptureRequest.Builder requester=camera.createCaptureRequest(camera.TEMPLATE_STILL_CAPTURE);
                requester.addTarget(reader.getSurface());

                int rotation=getWindowManager().getDefaultDisplay().getRotation();
                requester.set(CaptureRequest.JPEG_ORIENTATION, Camera2Util.getOrientation(rotation, characteristics));

                session.capture(requester.build(), null, null);

            } catch (Exception ex) {}
        } else {
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera == null) {
            try {

                for (String cameraId : manager.getCameraIdList()) {
                    characteristics = manager.getCameraCharacteristics(cameraId);
                    if (characteristics.get(characteristics.LENS_FACING) ==
                            CameraCharacteristics.LENS_FACING_BACK) {

                        //add~~~~~~~~~~~~~
                        Size largestSize=Camera2Util.getLargestImageSize(characteristics);
                        reader=ImageReader.newInstance(largestSize.getWidth(), largestSize.getHeight(), ImageFormat.JPEG, 2);
                        reader.setOnImageAvailableListener(captureListener, handler);

                        Size optimalSize=Camera2Util.chooseOptimalSize(this, characteristics, width, height);
                        holder.setFixedSize(optimalSize.getWidth(), optimalSize.getHeight());

                        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                            manager.openCamera(cameraId, stateCallback, handler);
                        }


                        return;
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
        try {
            // 초기화
            holder.setFixedSize(/*width*/0, /*height*/0);
            if (session != null) {
                session.close();
                session = null;
            }
        } finally {
            if (camera != null) {
                camera.close();
                camera = null;
            }
        }
        if (reader != null) reader.close();
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Lab28_2Activity.this.camera = camera;
            try {
                //add~~~~~~~~~~~~~~
                List<Surface> outputs=Arrays.asList(holder.getSurface(), reader.getSurface());
                camera.createCaptureSession(outputs, sessionListener, handler);
            } catch (Exception ex) {
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
        }

        @Override
        public void onError(CameraDevice camera, int error) {
        }
    };

    CameraCaptureSession.StateCallback sessionListener = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            Lab28_2Activity.this.session = session;
            if (holder != null) {
                try {
                    //add~~~~~~~~~~~~~
                    CaptureRequest.Builder requestBuilder=camera.createCaptureRequest(camera.TEMPLATE_PREVIEW);
                    requestBuilder.addTarget(holder.getSurface());
                    CaptureRequest previewRequest=requestBuilder.build();
                    try{
                        session.setRepeatingRequest(previewRequest, null, null);
                    }catch (CameraAccessException e){}
                } catch (CameraAccessException ex) {
                }
            } else {
            }
        }

        @Override
        public void onClosed(CameraCaptureSession session) {
            session = null;
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
        }
    };

    ImageReader.OnImageAvailableListener captureListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //add~~~~~~~~~~~~~
            handler.post(new CapturedImageSaver(reader.acquireNextImage()));

        }
    };

    class CapturedImageSaver implements Runnable {
        private Image mCapture;

        public CapturedImageSaver(Image capture) {
            mCapture = capture;
        }

        @Override
        public void run() {
            File file = null;
            try {
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myApp");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                file = File.createTempFile("IMG", ".jpg", dir);
                FileOutputStream ostream = new FileOutputStream(file);

                ByteBuffer buffer = mCapture.getPlanes()[0].getBuffer();
                byte[] jpeg = new byte[buffer.remaining()];
                buffer.get(jpeg);

                ostream.write(jpeg);
                ostream.flush();

                Toast t = Toast.makeText(Lab28_2Activity.this, "file write ok : " + file.getAbsolutePath(), Toast.LENGTH_SHORT);
                t.show();

            } catch (Exception ex) {
            } finally {
                mCapture.close();
            }
        }
    }
}
