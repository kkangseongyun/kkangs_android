package com.example.user.part7_mission;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Mission2Activity extends AppCompatActivity {

    EditText contentView;

    SpannableStringBuilder stringBuilder;

    int reqWidth;
    int reqHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission2);

        contentView=(EditText)findViewById(R.id.mission2_edit);

        DisplayMetrics metrics=getResources().getDisplayMetrics();
        reqWidth=metrics.widthPixels;
        reqHeight=metrics.heightPixels;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                    GalleryAppUtil.sendIntentMultipleImage(this, 20);
                }else {
                    GalleryAppUtil.sendIntentSingleImage(this, 30);
                }
            }else {
                Toast toast=Toast.makeText(this, "no permission", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==20 && resultCode==RESULT_OK && Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            if(data.getClipData() != null){
                ClipData clipData=data.getClipData();
                for(int i=0; i<clipData.getItemCount(); i++){
                    ClipData.Item item=clipData.getItemAt(i);
                    Uri uri=item.getUri();
                    if("com.android.providers.media.documents".equals(uri.getAuthority()) && Build.VERSION.SDK_INT>= 19){
                        String filePath=GalleryAppUtil.getFilePathFromDocumentUri(this, uri);
                        if(filePath != null){
                            insertImageToEditText(filePath);
                        }
                    }else if("external".equals(uri.getPathSegments().get(0))){
                        String filePath=GalleryAppUtil.getFilePathFromUriSegment(this, uri);
                        if(filePath != null){
                            insertImageToEditText(filePath);
                        }
                    }
                }
            }else {
                Uri uri=data.getData();
                String filePath=GalleryAppUtil.getFilePathFromDocumentUri(this, uri);
                if(filePath != null){
                    insertImageToEditText(filePath);
                }
            }
        }else if(requestCode==30 && resultCode==RESULT_OK){
            String filePath=GalleryAppUtil.getSigleFilePath(this, data);
            insertImageToEditText(filePath);
        }
    }

    private void insertImageToEditText(String filePath){
        if(!filePath.equals("")){
            File file=new File(filePath);
            String fileName=file.getName();

            stringBuilder=new SpannableStringBuilder(contentView.getText());
            stringBuilder.insert(contentView.getSelectionStart(), "\n [["+fileName+"]] \n");

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            try{
                InputStream in=new FileInputStream(filePath);
                BitmapFactory.decodeStream(in, null, options);
                in.close();
                in=null;
            }catch (Exception e){
                e.printStackTrace();
            }
            final int height=options.outHeight;
            final int width=options.outWidth;
            int inSampleSize=1;
            if(width>reqWidth){
                int widthRadio=Math.round((float)width / (float)reqWidth);
                inSampleSize=widthRadio;
            }
            BitmapFactory.Options imgOptions=new BitmapFactory.Options();
            imgOptions.inSampleSize=inSampleSize;
            Bitmap bitmap=BitmapFactory.decodeFile(filePath, imgOptions);

            String result=stringBuilder.toString();
            int start=result.indexOf("[["+fileName+"]]");
            int end=start+new String("[["+fileName+"]]").length();

            stringBuilder.setSpan(new ImageSpan(this, bitmap, ImageSpan.ALIGN_BASELINE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentView.setText(stringBuilder);

            contentView.setSelection(end+1);

            contentView.requestFocus();
            final InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            contentView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputMethodManager.showSoftInput(contentView, 0);
                }
            }, 30);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mission2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_mission2){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    GalleryAppUtil.sendIntentMultipleImage(this, 20);
                }else {
                    GalleryAppUtil.sendIntentSingleImage(this, 30);
                }
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
