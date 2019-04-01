package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TermData(
    @SerializedName("month") var month: Int,
    @SerializedName("in") var inpeople: Int,
    @SerializedName("out") var outpeople: Int
) : Serializable