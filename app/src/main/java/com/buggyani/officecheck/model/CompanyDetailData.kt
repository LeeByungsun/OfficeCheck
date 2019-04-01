package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CompanyDetailData(
    @SerializedName("name") var name: String,
    @SerializedName("addr") var address: String,
    @SerializedName("people") var people: Int,
    @SerializedName("amount") var amount: Int
) : Serializable