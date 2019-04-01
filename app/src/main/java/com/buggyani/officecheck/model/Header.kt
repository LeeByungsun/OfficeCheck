package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Header(
    @SerializedName("resultCode") var resultCode: String,
    @SerializedName("resultMsg") var resultMsg: String
): Serializable