package tw.com.nsy.demo1.connectNetFunc

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import tw.com.nsy.demo1.connectNetFunc.OkHttpClientSSL.getUnsafeOkHttpClient


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.com.nsy.demo1.BuildConfig
import java.util.concurrent.TimeUnit

interface ConnectApi {

//    @POST("api/GetService/login")
//    fun postUserLogin(@Body userlogin: userLogin): Observable<ResponseBodyCi<userLoginResult>> //返回資料型別

    companion object {
        var mToken: String = ""
        private fun getLogInterceptor(): Interceptor? {
            val interceptor = HttpLoggingInterceptor()
            // 正式機 切 NONE ,不顯示 logcat
            if (BuildConfig.DEBUG)
                interceptor?.level = HttpLoggingInterceptor.Level.BODY
            else
                interceptor?.level = HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

        private fun <T> getService(service: Class<T>): T {
            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(getLogInterceptor())
                .readTimeout(3 * 60 * 1000, TimeUnit.SECONDS)
                .connectTimeout(3 * 60 * 1000, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                        .header(
                            "Content-Type",
                            "application/json"
                        ) // <-- this is the important line
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            var baseUrl = "https://m.senao.com.tw/"

            val retrofit = Retrofit.Builder()
                .baseUrl("$baseUrl")
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okhttpClient)
                .build()

            return retrofit.create(service)
        }

        fun getShopCarService(): serviceApi {
            return getService(serviceApi::class.java)
        }
    }
}
