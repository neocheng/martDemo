package tw.com.nsy.demo1.connectNetFunc

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import tw.com.nsy.demo1.connectNetFunc.dataModel.ShopCarModel

interface serviceApi {

    @GET("/apis2/test/marttest.jsp") //  查詢既有客戶
    fun getMarttest( ): Deferred<ShopCarModel>


}