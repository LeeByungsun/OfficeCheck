package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CompanyData(
    @SerializedName("name") var name: String,
    @SerializedName("addr") var addr: String,
    @SerializedName("regnum") var regnum: Int,
    @SerializedName("seq") var seq: Int,
    @SerializedName("date") var date: Int
) : Serializable