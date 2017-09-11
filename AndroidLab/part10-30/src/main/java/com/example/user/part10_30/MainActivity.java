package com.example.user.part10_30;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,SurfaceHolder.Callback {

    Button soundMediaBtn;
    Button soundPoolBtn;
    Button videoMediaBtn;
    Button videoViewBtn;

    SurfaceView surfaceView;
    VideoView videoView;

    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundMediaBtn=(Button)findViewById(R.id.lab1_sound_media);
        soundPoolBtn=(Button)findViewById(R.id.lab1_sound_pool);
        videoMediaBtn=(Button)findViewById(R.id.lab1_video_media);
        videoViewBtn=(Button)findViewById(R.id.lab1_video_view);
        surfaceView=(SurfaceView)findViewById(R.id.lab1_surface);
        videoView=(VideoView)findViewById(R.id.lab1_video);

        soundMediaBtn.setOnClickListener(this);
        soundPoolBtn.setOnClickListener(this);
        videoMediaBtn.setOnClickListener(this);
        videoViewBtn.setOnClickListener(this);

        holder = surfaceView.getHolder();
        holder.addCallback(this);

    }


    @Override
    public void onClick(View v) {
        //add~~~~~~~~~~~~~~~
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }

        if(v==soundMediaBtn){
            try{
                mediaPlayer=MediaPlayer.create(this, R.raw.sound);
                mediaPlayer.start();

            }catch (Exception e){}
        }else if(v==soundPoolBtn){
            SoundPool soundPool=null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                soundPool=new SoundPool.Builder().setMaxStreams(10).build();
            }else {
                soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
            }
            soundPool.load(this, R.raw.sound, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(sampleId, 1f, 1f, 0, 0, 1f);
                }
            });
        }else if(v==videoMediaBtn){
            try{
                mediaPlayer=MediaPlayer.create(this, R.raw.video);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(v==videoViewBtn){
            MediaController mediaController=new MediaController(this);
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
        }

        
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
