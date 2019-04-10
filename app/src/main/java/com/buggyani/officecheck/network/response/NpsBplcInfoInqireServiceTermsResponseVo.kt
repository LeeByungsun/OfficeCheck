package com.buggyani.officecheck.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NpsBplcInfoInqireServiceTermsResponseVo(@SerializedName("response") var response: ResponseTerms)

data class ResponseTerms(@SerializedName("header") var header: Header, @SerializedName("body") var body: BodyTerms) :
    Serializable

data class BodyTerms(
    @SerializedName("items") var items: ItemsTerms,
    @SerializedName("numOfRows") var numOfRows: Int,
    @SerializedName("pageNo") var pageNo: String,
    @SerializedName("totalCount") var totalCount: Int,
    @SerializedName("item") var item: Item
) : Serializable

class ItemsTerms(@SerializedName("item") var item: Item) : Serializable