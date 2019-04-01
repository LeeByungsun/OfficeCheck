package com.buggyani.officecheck

import android.app.Application
import android.content.Context
import com.buggyani.officecheck.BuildConfig.DEBUG
import com.buggyani.officecheck.network.APIInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit

class OfficeApplication : Application() {
    private val TAG = javaClass.simpleName
    private var context: Context? = null


    override fun onCreate() {
        super.onCreate()
        instance = this
        this.context = applicationContext
        setRetrofitServer(DEBUG)
    }

    /**
     * retrofit setting
     */
    fun setRetrofitServer(debug: Boolean) {
        retrofit_Server = if (debug) {
            val httpClient =
                OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(30, TimeUnit.SECONDS)
            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY
//        logging.level = HttpLoggingInterceptor.Level.HEADERS
            httpClient.addInterceptor(logging)
            Retrofit.Builder().baseUrl(APIInfo.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        } else {
            Retrofit.Builder().baseUrl(APIInfo.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        api_Server = retrofit_Server!!.create(APIInfo::class.java)
    }

    companion object {
        var instance: OfficeApplication? = null
            private set
        var retrofit_Server: Retrofit? = null
        lateinit var api_Server: APIInfo
        var lableCount = 0
    }

}