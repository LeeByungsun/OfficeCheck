package com.buggyani.officecheck.network.response

import com.buggyani.officecheck.model.Response
import com.google.gson.annotations.SerializedName

data class NpsBplcInfoInqireServiceResponseVo(@SerializedName("response") var response: Response)