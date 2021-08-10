package tw.com.nsy.demo1

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.uc_white_actbar_item_blue.*
import tw.com.nsy.demo1.connectNetFunc.ConnectApi
import tw.com.nsy.demo1.connectNetFunc.dataModel.ShopCarModel
import tw.com.nsy.demo1.connectNetFunc.repository.HttpResult
import java.security.MessageDigest
import java.text.DecimalFormat

abstract class BaseActivity : AppCompatActivity() {
    lateinit var thisView: View


    fun getShopSharedPreferences(): SharedPreferences {
        return getSharedPreferences("shopCarProfile", Context.MODE_PRIVATE)
    }

    fun getShopSharedPreferencesEdit(): SharedPreferences.Editor {
        return getSharedPreferences("shopCarProfile", Context.MODE_PRIVATE).edit()
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thisView = window.decorView

        csivPermission()
    }

    fun actBarHide() {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.hide()
    }

    fun actBarTitle(title: String) {
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = title
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    fun message(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }


    fun csivPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun String.encodeMD5(salt: String?): String {
        val saltedString = salt?.let { this + it } ?: this
        val md5MessageDigest = MessageDigest.getInstance("MD5")
        val digest = md5MessageDigest.digest(saltedString.toByteArray())
        val stringBuilder = StringBuilder()
        for (byte in digest) {
            val value = byte.toInt() and 0xff
            var hexString = Integer.toHexString(value)
            if (hexString.length < 2) {
                hexString = "0" + hexString
            }

            stringBuilder.append(hexString)
        }
        return stringBuilder.toString()
    }

    // Photo

    fun permissionPhoto() {
        val permissionList = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        var size = permissionList.size
        var i = 0
        while (i < size) {
            if (ActivityCompat.checkSelfPermission(
                    this@BaseActivity,
                    permissionList[i]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.removeAt(i)
                i -= 1
                size -= 1
            }
            i += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions(
            this@BaseActivity,
            permissionList.toArray(array),
            0
        )
    }

    var saveUri: Uri? = null

    private companion object {
        val PHOTO_FROM_GALLERY = 0
        val PHOTO_FROM_CAMERA = 1
    }


    fun dailog() {
        AlertDialog.Builder(this@BaseActivity)
            .setTitle("提醒")
            .setMessage("相機功能將無法使用")
    }

    @TargetApi(28)
    private fun hasDisplayCutout(window: Window): Boolean {
        val displayCutout: DisplayCutout?
        val rootView = window.decorView
        val insets = rootView.rootWindowInsets
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets != null) {
            displayCutout = insets.displayCutout
            if (displayCutout != null) {
                if (displayCutout.boundingRects != null && displayCutout.boundingRects.size > 0 && displayCutout.safeInsetTop > 0) {
                    return true
                }
            }
        }
        return false
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {

        super.onBackPressed()
        return

    }

    fun setStatusBarColored(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = context.window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            val statusBarHeight = getStatusBarHeight(context)
            val view = View(context)
            view.setLayoutParams(
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            view.getLayoutParams().height = statusBarHeight
            (w.decorView as ViewGroup).addView(view)
            var myColor = Color.parseColor("#00000000")
            view.setBackground(ColorDrawable(myColor))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE// 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) // 表示我們的 UI 是 LIGHT 的 style，icon 就會呈現深色系。

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
        }
    }

    fun setStatusBarColoredWhite(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = context.window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            val statusBarHeight = getStatusBarHeight(context)
            val view = View(context)
            view.setLayoutParams(
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            view.getLayoutParams().height = statusBarHeight
            (w.decorView as ViewGroup).addView(view)
            var myColor = Color.parseColor("#00000000")
            view.setBackground(ColorDrawable(myColor))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE// 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
//                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        ) // 表示我們的 UI 是 LIGHT 的 style，icon 就會呈現深色系。
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
        }
    }

    fun getStatusBarHeight(context: Activity): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    fun imgActbarBackBlueClick(_isShow2sec: Boolean) {
        this.doubleBackToExitPressedOnce = _isShow2sec
        img_actbar_back_key_blue.setOnClickListener {
            onBackPressed()
        }

    }

    //region API Member
    suspend fun GetMartapi(): HttpResult<ShopCarModel> {
        return try {
            val result = ConnectApi.getShopCarService().getMarttest().await()
            HttpResult.Success(result)
        } catch (e: Throwable) {
            HttpResult.Error(e)
        }
    }

    fun toPrice(iPayment:String):String{
//        val df = DecimalFormat("$#,##0.00")
        val df = DecimalFormat("$#,###")
//         return df.format(1234567.2)
        return df.format(iPayment.toInt())
    }

}