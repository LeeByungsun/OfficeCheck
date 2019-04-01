package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Body(@SerializedName("items") var items: Items,
                @SerializedName("numOfRows") var numOfRows: Int,
                @SerializedName("pageNo") var pageNo: String,
                @SerializedName("totalCount") var totalCount: Int,
                @SerializedName("item") var item:Item) : Serializable