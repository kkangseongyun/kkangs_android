package com.example.user.part10_28;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Util {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    //사이즈 비교 로직

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }
    //preview 화면 size 결정을 위해 가능한 size중 가장 작은 size를 선택하는 로직
    //choices : 디바이스에서 제공하는 size 목록
    //width, height : surface의 사이즈
    //surface의 사이즈 보다 큰것중 가장 작은것을 획득 목적

    static Size chooseOptimalSize(Activity context, CameraCharacteristics cameraCharacteristics, int width,
                                  int height  ) {

        StreamConfigurationMap info = cameraCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        Size[] choices = info.getOutputSizes(SurfaceTexture.class);

        Point displaySize = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(displaySize);
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;

        Size largest = Collections.max(
                Arrays.asList(info.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());



        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = largest.getWidth();
        int h = largest.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxPreviewWidth && option.getHeight() <= maxPreviewHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= width &&
                        option.getHeight() >= height) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e("kkang", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static Size getLargestImageSize(CameraCharacteristics cameraCharacteristics){
        StreamConfigurationMap info = cameraCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        return Collections.max(
                Arrays.asList(info.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());
    }


    public static int getOrientation(int rotation, CameraCharacteristics cameraCharacteristics) {

        int mSensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }


}
