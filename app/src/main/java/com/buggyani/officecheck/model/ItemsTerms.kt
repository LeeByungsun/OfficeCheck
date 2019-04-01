package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ItemsTerms(@SerializedName("item") var item: Item) : Serializable