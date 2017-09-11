package com.example.user.part7_20_aidl;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class PlayService extends Service {

    public static final int MEDIA_STATUS_STOP=0;
    public static final int MEDIA_STATUS_RUNNING=1;
    public static final int MEDIA_STATUS_COMPLETED=2;

    int status=MEDIA_STATUS_STOP;

    MediaPlayer player;
    String filePath;

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        filePath=intent.getStringExtra("filePath");
        return new IPlayService.Stub(){
            @Override
            public int currentPosition() throws RemoteException {
                if(player.isPlaying())
                    return player.getCurrentPosition();
                else
                    return 0;
            }

            @Override
            public int getMaxDuration() throws RemoteException {
                if(player.isPlaying())
                    return player.getDuration();
                else
                    return 0;
            }

            @Override
            public void start() throws RemoteException {
                player=new MediaPlayer();
                try{
                    player.setDataSource(filePath);
                    player.prepare();
                    player.start();
                }catch (Exception e){
                    e.printStackTrace();
                }

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        status=MEDIA_STATUS_COMPLETED;
                    }
                });

                status=MEDIA_STATUS_RUNNING;
            }

            @Override
            public void stop() throws RemoteException {
                if(player.isPlaying())
                    player.stop();
                status=MEDIA_STATUS_STOP;
            }

            @Override
            public int getMediaStatus() throws RemoteException {
                return status;
            }
        };
    }
}
