package com.buggyani.officecheck.chart

import com.github.mikephil.charting.formatter.ValueFormatter

import java.text.DecimalFormat

/**
 * Predefined value-formatter that formats large numbers in a pretty way.
 * Outputs: 856 = 856; 1000 = 1k; 5821 = 5.8k; 10500 = 10k; 101800 = 102k;
 * 2000000 = 2m; 7800000 = 7.8m; 92150000 = 92m; 123200000 = 123m; 9999999 =
 * 10m; 1000000000 = 1b; Special thanks to Roman Gromov
 * (https://github.com/romangromov) for this piece of code.
 *
 * @author Philipp Jahoda
 * @author Oleksandr Tyshkovets <olexandr.tyshkovets></olexandr.tyshkovets>@gmail.com>
 */
class LargeValueFormatter() : ValueFormatter() {

    private var mSuffix = arrayOf("", "k", "m", "b", "t")
    private var mMaxLength = 5
    private val mFormat: DecimalFormat
    private var mText = ""

    val decimalDigits: Int
        get() = 0

    init {
        mFormat = DecimalFormat("###E00")
    }

    /**
     * Creates a formatter that appends a specified text to the result string
     *
     * @param appendix a text that will be appended
     */
    constructor(appendix: String) : this() {
        mText = appendix
    }

    override fun getFormattedValue(value: Float): String {
        return makePretty(value.toDouble()) + mText
    }

    /**
     * Set an appendix text to be added at the end of the formatted value.
     *
     * @param appendix
     */
    fun setAppendix(appendix: String) {
        this.mText = appendix
    }

    /**
     * Set custom suffix to be appended after the values.
     * Default suffix: ["", "k", "m", "b", "t"]
     *
     * @param suffix new suffix
     */
    fun setSuffix(suffix: Array<String>) {
        this.mSuffix = suffix
    }

    fun setMaxLength(maxLength: Int) {
        this.mMaxLength = maxLength
    }

    /**
     * Formats each number properly. Special thanks to Roman Gromov
     * (https://github.com/romangromov) for this piece of code.
     */
    private fun makePretty(number: Double): String {

        var r = mFormat.format(number)

        val numericValue1 = Character.getNumericValue(r[r.length - 1])
        val numericValue2 = Character.getNumericValue(r[r.length - 2])
        val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)

        r = r.replace("E[0-9][0-9]".toRegex(), mSuffix[combined / 3])

        while (r.length > mMaxLength || r.matches("[0-9]+\\.[a-z]".toRegex())) {
            r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
        }

        return r
    }
}
