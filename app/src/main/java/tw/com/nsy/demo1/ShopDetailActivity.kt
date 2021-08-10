package tw.com.nsy.demo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.uc_white_actbar_item_blue.*

class ShopDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)
        initView()
    }

    fun initView() {
        setStatusBarColored(this@ShopDetailActivity)
        actBarHide()
        imgActbarBackBlueClick(true)
        tv_actbar_title_blue.visibility = View.VISIBLE
        tv_actbar_title_blue.text = "商品資訊"

        var shops = intent.getStringExtra("martDel")
        Glide.with(this@ShopDetailActivity)
            .load(shops.split(";")[3])
            .into( img_product_photo);

        tv_product_number.text=shops.split(";")[0]
        tv_product_name.text=shops.split(";")[1]
        tv_product_price.text=toPrice(shops.split(";")[2])
    }

}