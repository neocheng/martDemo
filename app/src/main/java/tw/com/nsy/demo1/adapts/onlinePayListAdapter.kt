package tw.com.nsy.demo1.adapts

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_shop_car.view.*
import tw.com.nsy.demo1.R
import tw.com.nsy.demo1.connectNetFunc.dataModel.Data2
import tw.com.nsy.demo1.connectNetFunc.dataModel.ShopCarModel
import tw.com.nsy.demo1.formatStrings.toPrice
import java.util.Locale.filter
import kotlin.collections.ArrayList


class onlinePayListAdapter(
    val ctx: Activity,
    val caseList: MutableList<Data2>
) : RecyclerView.Adapter<onlinePayListAdapter.ViewHolder>(), Filterable {

    var photosListFiltered: ArrayList<Data2> = ArrayList()

    init {
        photosListFiltered = caseList as ArrayList<Data2>
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.item_shop_car, p0, false)
        return ViewHolder(v)
    }

    interface IOnItemClickListener<T> {
        fun onItemClick(t: T)
    }

    private lateinit var itemClickListener: IOnItemClickListener<String>

    fun setOnItemClickListener(itemClickListener: IOnItemClickListener<String>) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return photosListFiltered.size
    }


    fun getItemloanNo(position: Int): String {
        return "${caseList.get(position).martId};${caseList.get(position).martShortName};${
            caseList.get(
                position
            ).price
        };${caseList.get(position).imageUrl}"
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        (p0 as ViewHolder).bind(photosListFiltered.get(p1));
        p0.tv_shopcat_title.text = caseList[p1].martName
        p0.tv_shopcat_money.text = toPrice(caseList[p1].price.toString())

        Glide.with(ctx)
            .load(caseList[p1].imageUrl)
            .into(p0.img_shopcar_photo);
    }



    //獲得事件回撥介面
    var item: ItemClick? = null;

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_shopcat_title = itemView.findViewById<TextView>(R.id.tv_shopcat_title)!!
        val tv_shopcat_money = itemView.findViewById<TextView>(R.id.tv_shopcat_money)!!
        val img_shopcar_photo = itemView.findViewById<ImageView>(R.id.img_shopcar_photo)!!

        fun bind(model: Data2): Unit {
            itemView.tv_shopcat_title.text = model.martName
        }

        init {
            itemView.setOnClickListener(View.OnClickListener {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(getItemloanNo(adapterPosition))
                }

            })
        }



    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    photosListFiltered = caseList as ArrayList<Data2>
                } else {
                    val resultList = ArrayList<Data2>()
                    for (row in caseList) {
                        if (row.martName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    photosListFiltered = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = photosListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                photosListFiltered = results?.values as ArrayList<Data2>
                notifyDataSetChanged()
            }
        }
    }
}