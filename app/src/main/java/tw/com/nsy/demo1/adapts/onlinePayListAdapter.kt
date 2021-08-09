package tw.com.nsy.demo1.adapts

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView


class onlinePayListAdapter(
    val ctx: Activity,
    val caseList: ArrayList<LoanInfo>
) : RecyclerView.Adapter<onlinePayListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.uc_item_choose_car, p0, false)
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
        return caseList.size
    }

    fun getItem(position: Int): String {
        return caseList.get(position).carNo
    }

    fun getItemloanNo(position: Int): String {
        return caseList.get(position).loanNo
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.tv_car_title.text = caseList[p1].carNo

    }

    //獲得事件回撥介面
    var item: ItemClick? = null;

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_car_title = itemView.findViewById<TextView>(R.id.tv_car_title)!!
        val img_car_choose = itemView.findViewById<ImageView>(R.id.img_car_choose)!!

        init {
            itemView.setOnClickListener(View.OnClickListener {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(getItemloanNo(adapterPosition))
                }
                img_car_choose.background =
                    ctx.resources.getDrawable(R.drawable.single_choice_press, null)
            })
        }
    }
}