package com.example.part11_34

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lab34_2.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
class Lab34_2Activity : AppCompatActivity() {

    var stringBuilder: SpannableStringBuilder?=null
    val reqWidth:Int
    val reqHeight: Int

    init {
        val metrics= Resources.getSystem().displayMetrics
        reqWidth=metrics.widthPixels
        reqHeight=metrics.heightPixels
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab34_2)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100 && grantResults.size>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sendIntentMultipleImage(this, 20)
                } else {
                    sendIntentSingleImage(this, 30)
                }
            } else {
                val toast = Toast.makeText(this, "no permission", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 20 && resultCode == Activity.RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (data.clipData != null) {
                val clipData = data.clipData
               for (i in 0 until clipData!!.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri = item.uri
                    if ("com.android.providers.media.documents" == uri.authority && Build.VERSION.SDK_INT >= 19) {
                        val filePath = getFilePathFromDocumentUri(this, uri)
                        if (filePath != null) {
                            insertImageToEditText(filePath)
                        }
                    } else if ("external" == uri.pathSegments[0]) {
                        val filePath = getFilePathFromUriSegment(this, uri)
                        if (filePath != null) {
                            insertImageToEditText(filePath)
                        }
                    }
                }
            } else {
                val uri = data.data
                val filePath = getFilePathFromDocumentUri(this, uri)
                if (filePath != null) {
                    insertImageToEditText(filePath)
                }
            }
        } else if (requestCode == 30 && resultCode == Activity.RESULT_OK) {
            val filePath = getSigleFilePath(this, data)
            insertImageToEditText(filePath)
        }
    }

    private fun insertImageToEditText(filePath: String) {
        if (filePath != "") {
            val file = File(filePath)
            val fileName = file.name

            stringBuilder = SpannableStringBuilder(blogEdit.text)
            stringBuilder!!.insert(blogEdit.selectionStart, "\n [[$fileName]] \n")

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            try {
                //변수명 in이 예약어여서
                var `in`: InputStream? = FileInputStream(filePath)
                BitmapFactory.decodeStream(`in`, null, options)
                `in`!!.close()
                `in` = null
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (width > reqWidth) {
                val widthRadio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = widthRadio
            }
            val imgOptions = BitmapFactory.Options()
            imgOptions.inSampleSize = inSampleSize
            val bitmap = BitmapFactory.decodeFile(filePath, imgOptions)

            val result = stringBuilder.toString()
            val start = result.indexOf("[[$fileName]]")
            val end = start + "[[$fileName]]".length

            stringBuilder!!.setSpan(ImageSpan(this, bitmap, ImageSpan.ALIGN_BASELINE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            blogEdit.setText(stringBuilder)

            blogEdit.setSelection(end + 1)

            blogEdit.requestFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            blogEdit.postDelayed({ inputMethodManager.showSoftInput(blogEdit, 0) }, 30)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_lab2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_gallery) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sendIntentMultipleImage(this, 20)
                } else {
                    sendIntentSingleImage(this, 30)
                }
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}





























