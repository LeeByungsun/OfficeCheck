package com.buggyani.officecheck.chart

import android.util.Log
import com.buggyani.officecheck.OfficeApplication
import com.buggyani.officecheck.OfficeApplication.Companion.lableCount
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

import java.text.DecimalFormat

class MyXAxisValueFormatter(private val mValues: Array<String>) : ValueFormatter() {
    private val TAG = javaClass.simpleName
    var count = 1
    var oldValue: String? = null
    lateinit var newValue: String
    /**
     * this is only needed if numbers are returned, else return 0
     */
    val decimalDigits: Int
        get() = 0

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[value.toInt()]
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        Log.e(TAG, "mValues size = ${mValues.size}")


        if (lableCount == (mValues.size)) {
            lableCount = 0
        }
        val valueString = mValues[lableCount]
        Log.e(TAG, "value = {$valueString}")
        Log.e(TAG, "value = {$lableCount}")
        lableCount++
        return valueString;
    }
}
