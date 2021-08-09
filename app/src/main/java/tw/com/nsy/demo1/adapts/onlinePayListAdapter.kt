package tw.com.nsy.demo1.adapts

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tw.com.nsy.demo1.R
import tw.com.nsy.demo1.connectNetFunc.dataModel.ShopCarModel


class onlinePayListAdapter(
    val ctx: Activity,
    val caseList: ShopCarModel
) : RecyclerView.Adapter<onlinePayListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.item_shop_car, p0, false)
        return ViewHolder(v)
    }

    interface IOnItemClickListener<T>  {
        fun onItemClick(t : T)
    }

    private lateinit var itemClickListener: IOnItemClickListener<String>

    fun setOnItemClickListener(itemClickListener: IOnItemClickListener<String>) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return caseList.data.size
    }

//    fun getItem(position: Int): String {
//        return caseList.data.get(position).carNo
//    }
//
//    fun getItemloanNo(position: Int): String {
//        return caseList.data.get(position).loanNo
//    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.tv_shopcat_title.text = caseList.data[p1].martShortName
        p0.tv_shopcat_money.text = caseList.data[p1].price.toString()

        Glide.with(ctx)
            .load(caseList.data[p1].imageUrl)
            .into( p0.img_shopcar_photo);
    }

    //獲得事件回撥介面
    var item: ItemClick? = null;

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_shopcat_title = itemView.findViewById<TextView>(R.id.tv_shopcat_title)!!
        val tv_shopcat_money = itemView.findViewById<TextView>(R.id.tv_shopcat_money)!!
        val img_shopcar_photo = itemView.findViewById<ImageView>(R.id.img_shopcar_photo)!!

//        init {
//            itemView.setOnClickListener(View.OnClickListener {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(getItemloanNo(adapterPosition))
//                }
//
//            })
//        }
    }
}