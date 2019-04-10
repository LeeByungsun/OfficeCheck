package com.buggyani.officecheck.network

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.internal.operators.observable.ObservableSingleSingle
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInfo {

    companion object {
        //server Url
        val BASE_URL = "http://apis.data.go.kr/B552015/NpsBplcInfoInqireService/"
        val API_KEY = "evyplqMQyRBlhVQ3e854Hojiu4NwxckhhsINJ1%2BVTO88YjQzTgherg4ncUyjq7OspRGxri4F8NGzuNxBW%2BAznQ%3D%3D"
    }

    //사업장 정보조회
    @GET("getBassInfoSearch")
    fun getCompanyData(
        @Query(value = "wkpl_nm", encoded = false) companyName: String, @Query(value = "pageNo") pageNo: Int, @Query(
            value = "serviceKey",
            encoded = true
        ) serviceKey: String, @Query(value = "numOfRows") numOfRows: Int
    ): Observable<String>

    @GET("getBassInfoSearch")
    fun getCompanyData(
        @Query(
            value = "wkpl_nm",
            encoded = false
        ) companyName: String, @Query(value = "bzowr_rgst_no") bzowr_rgst_no: Int, @Query(value = "pageNo") pageNo: Int, @Query(
            value = "serviceKey",
            encoded = true
        ) serviceKey: String, @Query(value = "numOfRows") numOfRows: Int
    ): Observable<String>

    //상세정보 조회
    @GET("getDetailInfoSearch")
    fun getDetailCompanyData(
        @Query(value = "seq") seq: Int, @Query(
            value = "serviceKey",
            encoded = true
        ) serviceKey: String
    ): Observable<String>

    //기간별 정보조회
//    @GET("getPdAcctoSttusInfoSearch")
//    fun getTerm(
//        @Query(value = "seq") start: Int, @Query(value = "data_crt_ym") limit: Int, @Query(
//            value = "serviceKey",
//            encoded = true
//        ) serviceKey: String
//    ): Single<String>

    @GET("getPdAcctoSttusInfoSearch")
    fun getTerm(
        @Query(value = "seq") start: Int, @Query(value = "data_crt_ym") limit: Int, @Query(
            value = "serviceKey",
            encoded = true
        ) serviceKey: String
    ): Observable<String>

}