package tw.com.nsy.demo1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import tw.com.nsy.demo1.adapts.onlinePayListAdapter
import tw.com.nsy.demo1.connectNetFunc.dataModel.Data2
import tw.com.nsy.demo1.connectNetFunc.dataModel.ShopCarModel
import tw.com.nsy.demo1.connectNetFunc.repository.HttpResult
import java.net.SocketTimeoutException


class MainActivity : BaseActivity() {

    private lateinit var cats_recyclerView: RecyclerView


    var adapter: onlinePayListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarColored(this@MainActivity)
        actBarHide()

        cats_recyclerView = findViewById<RecyclerView>(R.id.cats_recyclerView)
        cats_recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

        var shopString = getShopSharedPreferences().getString("ShopData", "0")
        println(shopString)

        if (shopString.equals("0")) {
            getShopCarListApi()
        } else {
            var gson = Gson()
            var testModel = gson.fromJson(shopString, ShopCarModel::class.java)
            adapter = onlinePayListAdapter(this@MainActivity, testModel.data )

            cats_recyclerView.adapter = adapter
            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.orientation = RecyclerView.VERTICAL
            cats_recyclerView.layoutManager = layoutManager

            adapter?.setOnItemClickListener(object :
                onlinePayListAdapter.IOnItemClickListener<String> {
                override fun onItemClick(t: String) {
                    // 當點擊 item 時，這裡可以做一些事情...
//                                Toast.makeText(this@MainActivity, t, Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this@MainActivity, ShopDetailActivity::class.java)
                            .putExtra("martDel", t)
                    )
                }
            })
        }

        val searchView = findViewById<SearchView>(R.id.editTextTextPersonName)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("onQueryTextChange", "query: " + query)
                adapter?.filter?.filter(query)
                return true
            }
        })
    }

    fun getShopCarListApi() {
        CoroutineScope(Dispatchers.IO).async {
            val result = GetMartapi()
            // Switch to Android mainThread
            withContext(Dispatchers.Main) {

                when (result) {
                    is HttpResult.Success -> {

                        var gson = Gson()
                        var jsonString = gson.toJson(result.data)
                        getShopSharedPreferencesEdit().putString("ShopData", jsonString).apply()
//                        var testModel = gson.fromJson(jsonString, ShopCarModel::class.java)

                        adapter = onlinePayListAdapter(this@MainActivity, result.data.data)

                        cats_recyclerView.adapter = adapter
                        val layoutManager = LinearLayoutManager(this@MainActivity)
                        layoutManager.orientation = RecyclerView.VERTICAL
                        cats_recyclerView.layoutManager = layoutManager

                        adapter?.setOnItemClickListener(object :
                            onlinePayListAdapter.IOnItemClickListener<String> {
                            override fun onItemClick(t: String) {
                                // 當點擊 item 時，這裡可以做一些事情...
//                                Toast.makeText(this@MainActivity, t, Toast.LENGTH_SHORT).show()
                                startActivity(
                                    Intent(this@MainActivity, ShopDetailActivity::class.java)
                                        .putExtra("martDel", t)
                                )
                            }
                        })
                    }
                    is HttpResult.Error -> {
                        Log.e("login", Log.getStackTraceString(result.exception))
                        Toast.makeText(this@MainActivity, "請確認 是否正確", Toast.LENGTH_SHORT)
                            .show()
                        if (result.exception is SocketTimeoutException) {
                            // Handle Timeout
                            //                            listener?.onNetworkTimeout()
                        } else {
                            // Handle other Exceptions
                            //                            val message =
                            //                                result.exception.localizedMessage ?: "GENERIC_NETWORK_ISSUE"
                        }
                    }
                }
            }
        }
    }

}