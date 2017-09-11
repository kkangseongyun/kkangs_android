package com.example.part11_34

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

//utility 함수임으로 top-level로 선언
fun sendIntentSingleImage(context: Activity, requestCode: Int){
    val intent = Intent(Intent.ACTION_PICK)
    //데이터 설정이 목적임으로 함수가 아닌 프로퍼티를 이용
    intent.type=MediaStore.Images.Media.CONTENT_TYPE
    intent.data=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    context.startActivityForResult(intent, requestCode)
}
fun sendIntentMultipleImage(context: Activity, requestCode: Int){
    val intent=Intent()
    intent.type="image/*"
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    intent.action=Intent.ACTION_GET_CONTENT
    context.startActivityForResult(intent, requestCode)
}
fun getSigleFilePath(context: Context, data: Intent): String {
    val projection= arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(data.data, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED+" desc")
    cursor.moveToFirst()
    return cursor.getString(0)
}
//null이 리턴될 가능성이 있기 때문에 ?로 리턴타입 명시
@TargetApi(Build.VERSION_CODES.KITKAT)
fun getFilePathFromDocumentUri(context: Context, uri: Uri): String? {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
        val docId=DocumentsContract.getDocumentId(uri)
        val split=docId.split(":")
        val type=split[0]
        //null 대입이 가능하려면 타입에 ? 명시. 이후 값이 대입될 것임으로 var 선언
        var contentUri: Uri?=null
        if("image" == type){
            contentUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val selection=MediaStore.Images.Media._ID+"=?"
        val selectionArgs= arrayOf(split[1])
        val column="_data"
        val projection= arrayOf(column)

        val cursor=context.contentResolver.query(contentUri, projection, selection, selectionArgs, null)
        var filePath: String?=null
        if(cursor != null && cursor?.moveToFirst()){
            val column_index=cursor.getColumnIndexOrThrow(column)
            filePath=cursor!!.getString(column_index)
        }
        cursor!!.close()
        return filePath
    }else {
        return null
    }
}
fun getFilePathFromUriSegment(context: Context, uri: Uri): String? {
    val selection=MediaStore.Images.Media._ID+"=?"
    val selectionArgs= arrayOf(uri.lastPathSegment)
    val column="_data"
    val projection= arrayOf(column)
    val cursor=context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)
    var filePath: String?=null
    if(cursor != null && cursor.moveToFirst()){
        val column_index=cursor.getColumnIndex(column)
        filePath=cursor.getString(column_index)
    }
    cursor.close()
    return filePath
}




























