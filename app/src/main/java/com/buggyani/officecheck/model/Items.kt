package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Items(@SerializedName("item") var item: ArrayList<Item>) : Serializable