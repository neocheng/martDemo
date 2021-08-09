package tw.com.nsy.demo1.connectNetFunc.OkHttpClientSSL

import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun getUnsafeOkHttpClient(): OkHttpClient {
    val x509TrustManager = object: X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    val trustManagers = arrayOf<TrustManager>(x509TrustManager)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagers, null)

    val builder = OkHttpClient.Builder()
    builder.sslSocketFactory(sslContext.socketFactory, x509TrustManager)
    builder.hostnameVerifier { _, _ -> true }

    return builder.build()
}