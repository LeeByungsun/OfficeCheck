package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(@SerializedName("header") var header: Header, @SerializedName("body") var body: Body) : Serializable