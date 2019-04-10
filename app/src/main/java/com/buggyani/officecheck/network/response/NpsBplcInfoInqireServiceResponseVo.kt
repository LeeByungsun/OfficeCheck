package com.buggyani.officecheck.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NpsBplcInfoInqireServiceResponseVo(@SerializedName("response") var response: Response)

data class Response(@SerializedName("header") var header: Header, @SerializedName("body") var body: Body) : Serializable

data class Body(
    @SerializedName("items") var items: Items,
    @SerializedName("numOfRows") var numOfRows: Int,
    @SerializedName("pageNo") var pageNo: String,
    @SerializedName("totalCount") var totalCount: Int,
    @SerializedName("item") var item: Item
) : Serializable

class Items(@SerializedName("item") var item: ArrayList<Item>) : Serializable