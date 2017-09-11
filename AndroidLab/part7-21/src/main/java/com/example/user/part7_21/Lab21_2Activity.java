package com.example.user.part7_21;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab21_2Activity extends AppCompatActivity implements View.OnClickListener{
    Button contactBtn;
    Button galleryBtn;
    LinearLayout mainContent;

    int reqWidth;
    int reqHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab21_2);

        contactBtn=(Button)findViewById(R.id.lab2_contacts);
        galleryBtn=(Button)findViewById(R.id.lab2_gallery);
        mainContent=(LinearLayout)findViewById(R.id.lab2_content);

        contactBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        reqWidth = metrics.widthPixels;
        reqHeight = metrics.heightPixels;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

    }

    private void insertImageView(String filePath) {
        if(!filePath.equals("")){
            File file=new File(filePath);
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
            final int width=options.outWidth;
            int inSampleSize=1;

            if(width>reqWidth){
                int widthRatio=Math.round((float)width / (float)reqWidth);
                inSampleSize=widthRatio;
            }

            BitmapFactory.Options imgOptions=new BitmapFactory.Options();
            imgOptions.inSampleSize=inSampleSize;
            Bitmap bitmap=BitmapFactory.decodeFile(filePath, imgOptions);

            ImageView imageView=new ImageView(this);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mainContent.addView(imageView, params);
        }
        
    }
    private String getFilePathFromDocumentUri(Context context, Uri uri){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            String docId=DocumentsContract.getDocumentId(uri);
            String[] split=docId.split(":");
            String type=split[0];
            Uri contentUri=null;
            if("image".equals(type)){
                contentUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            String selection=MediaStore.Images.Media._ID+"=?";
            String[] selectionArg=new String[]{split[1]};

            String column="_data";
            String[] projection={column};

            Cursor cursor=context.getContentResolver().query(contentUri, projection, selection, selectionArg, null);
            String filePath=null;
            if(cursor != null && cursor.moveToFirst()){
                int column_index=cursor.getColumnIndexOrThrow(column);
                filePath=cursor.getString(column_index);
            }
            cursor.close();
            return filePath;
        }else {
            return null;
        }
    }

    private String getFilePathFromUriSegment(Uri uri){
        String selection=MediaStore.Images.Media._ID+"=?";
        String[] selectionArgs=new String[]{uri.getLastPathSegment()};

        String column="_data";
        String[] projection={column};

        Cursor cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
        String filePath=null;
        if(cursor != null && cursor.moveToFirst()){
            int column_index=cursor.getColumnIndexOrThrow(column);
            filePath=cursor.getString(column_index);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onClick(View v) {
        if(v==contactBtn){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setData(Uri.parse("content://com.android.contacts/data/phones"));
            startActivityForResult(intent, 10);
        }else if(v==galleryBtn){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 30);
            }else {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 20);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode==RESULT_OK){
            String id=Uri.parse(data.getDataString()).getLastPathSegment();
            Cursor cursor=getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.Data._ID+"="+id, null, null);
            cursor.moveToFirst();
            String name=cursor.getString(0);
            String phone=cursor.getString(1);

            TextView textView=new TextView(this);
            textView.setText(name+":"+phone);
            textView.setTextSize(25);
            textView.setTypeface(null, Typeface.BOLD);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mainContent.addView(textView, params);
        }else if(requestCode==20 && resultCode==RESULT_OK){
            String[] projection={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            String filePath=cursor.getString(0);
            insertImageView(filePath);
        }else if(requestCode==30 && resultCode==RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            if(data.getClipData() != null){
                ClipData clipData=data.getClipData();
                for(int i=0; i<clipData.getItemCount(); i++){
                    ClipData.Item item=clipData.getItemAt(i);
                    Uri uri=item.getUri();
                    if("com.android.providers.media.documents".equals(uri.getAuthority()) && Build.VERSION.SDK_INT>=19){
                        String filePath=getFilePathFromDocumentUri(this, uri);
                        if(filePath != null){
                            insertImageView(filePath);
                        }
                    }else if("external".equals(uri.getPathSegments().get(0))){
                        String filPath=getFilePathFromUriSegment(uri);
                        if(filPath != null){
                            insertImageView(filPath);
                        }
                    }
                }
            }else {
                Uri uri=data.getData();
                String filePath=getFilePathFromDocumentUri(this, uri);
                if(filePath != null){
                    insertImageView(filePath);
                }
            }
        }
        
    }

    
}

