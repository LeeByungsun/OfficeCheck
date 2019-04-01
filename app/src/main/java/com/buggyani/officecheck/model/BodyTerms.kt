package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BodyTerms(
    @SerializedName("items") var items: ItemsTerms,
    @SerializedName("numOfRows") var numOfRows: Int,
    @SerializedName("pageNo") var pageNo: String,
    @SerializedName("totalCount") var totalCount: Int,
    @SerializedName("item") var item: Item
) : Serializable