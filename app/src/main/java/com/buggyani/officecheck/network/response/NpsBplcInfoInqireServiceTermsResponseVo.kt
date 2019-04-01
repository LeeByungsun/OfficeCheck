package com.buggyani.officecheck.network.response

import com.buggyani.officecheck.model.Response
import com.buggyani.officecheck.model.ResponseTerms
import com.google.gson.annotations.SerializedName

data class NpsBplcInfoInqireServiceTermsResponseVo(@SerializedName("response") var response: ResponseTerms)