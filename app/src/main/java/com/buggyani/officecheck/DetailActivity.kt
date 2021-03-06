package com.buggyani.officecheck

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.buggyani.officecheck.OfficeApplication.Companion.lableCount
import com.buggyani.officecheck.chart.MyMarkerView
import com.buggyani.officecheck.databinding.ActivityDetailBinding
import com.buggyani.officecheck.model.CompanyData
import com.buggyani.officecheck.network.response.Item
import com.buggyani.officecheck.model.TermData
import com.buggyani.officecheck.network.APIInfo
import com.buggyani.officecheck.network.response.NpsBplcInfoInqireServiceTermsResponseVo
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONException
import org.json.JSONObject
import org.json.XML


class DetailActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private var binding: ActivityDetailBinding? = null
    private var itemList = ArrayList<CompanyData>()
    private lateinit var item: Item
    private var disposable: Disposable? = null
    private var termDataList = ArrayList<TermData>()
    private var index = 0
    private var lableMonth = ArrayList<String>()
    private var inPeople = ArrayList<BarEntry>()
    private var outPeople = ArrayList<BarEntry>()
    private var useDataList = ArrayList<CompanyData>()

    private lateinit var set1: BarDataSet
    private lateinit var set2: BarDataSet

    private val groupSpace = 0.08f
    private val barSpace = 0.03f // x4 DataSet
    private val barWidth = 0.2f // x4 DataSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        startProgress()
        itemList = intent.getSerializableExtra("seqlist") as ArrayList<CompanyData>
        item = intent.getSerializableExtra("companydetail") as Item
        initUI()


    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun initUI() {
        companyName.text = item.wkplNm
        employNumber.text = item.jnngpCnt.toString()
        useDataList.clear()

        for (i in 0 until itemList.size) {
            useDataList.add(itemList[i])
        }

        useDataList.forEach {
            println(it)
        }
        sendTermData()

    }

    private fun sendTermData() {
        useDataList.size
        getTermData(useDataList[index].seq, useDataList[index].date)

    }

    private fun getTermData(seq: Int, date: Int) {

        Log.e(TAG, "|$seq|")
         disposable = OfficeApplication.api_Server.getTerm(seq, date, APIInfo.API_KEY)

             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({ data: String? ->
                 //                Log.e(TAG, data)
                 val jsonObj: JSONObject
                 try {
                     jsonObj = XML.toJSONObject(data)
                     Log.d(TAG, jsonObj.toString())
                     val item = converToJsonDetail(jsonObj.toString())
                     termDataList.add(TermData(date, item.nwAcqzrCnt, item.lssJnngpCnt))
                     println("termDataListSize = " + termDataList.size)
                     println("index = $index")
                     index++
                     if (useDataList.size > termDataList.size) {
                         getTermData(useDataList[index].seq, useDataList[index].date)
                     }

                } catch (e: JSONException) {
                    Log.e(TAG, e.message)
                    e.printStackTrace()
                }
            }, { t: Throwable? ->
                t!!.printStackTrace()
                stopProgress()
            }, {
                 Log.e(TAG, "complit")
                 if (useDataList.size == termDataList.size) {
                     setChartData()
                 }
            })


      /*  Observable.just(useDataList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(5000, TimeUnit.MILLISECONDS)
            .flatMapIterable { x -> x }
            .flatMap { it ->
                Log.e(TAG, "map ${it.seq} ${it.date} ")
                Observable.just(OfficeApplication.api_Server.getTerm(it.seq, it.date, APIInfo.API_KEY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data: String ->
                        var jsonObj: JSONObject
                        jsonObj = XML.toJSONObject(data)
                        //                Log.d(TAG, jsonObj.toString())
                        var item = converToJsonDetail(jsonObj.toString())
                        termDataList.add(
                            TermData(
                                date,
                                item.nwAcqzrCnt,
                                item.lssJnngpCnt
                            )
                        )
                    }))


            }
            *//*.map { t ->
                Log.e(TAG, "map ${t.seq} ${t.date} ")
                OfficeApplication.api_Server.getTerm(t.seq, t.date, APIInfo.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data: String ->
                        var jsonObj: JSONObject
                        jsonObj = XML.toJSONObject(data)
                        //                Log.d(TAG, jsonObj.toString())
                        var item = converToJsonDetail(jsonObj.toString())
                        termDataList.add(
                            TermData(
                                date,
                                item.nwAcqzrCnt,
                                item.lssJnngpCnt
                            )
                        )
                    })
            }*//*
            .subscribe(
                {
                    Log.e(TAG, "subscribe")

                },
                { t: Throwable? ->
                    t!!.printStackTrace()
                    stopProgress()
                }, {
                    Log.e(TAG, "onComplate")
                    stopProgress()
                    setChartData()
                })*/
    }

    private fun converToJsonDetail(data: String): Item {
        val gson = Gson()
        val rsp = gson.fromJson(data, NpsBplcInfoInqireServiceTermsResponseVo::class.java)
        return rsp.response.body.items.item
    }

    private fun initChart() {
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)
        chart.setDrawValueAboveBar(true)
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.chartView = chart // For bounds control
        chart.marker = mv // Set the marker to the chart

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f

        val xAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.setCenterAxisLabels(true)
        xAxis.labelRotationAngle = -90F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(lableMonth.toTypedArray())
        chart.setViewPortOffsets(110F, 50F, 10F, 60F)
        Log.e(TAG, "item list size = ${itemList.size}")

        val leftAxis = chart.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
    }

    private fun setChartData() {
        termDataList.reverse()
        termDataList.forEach {
            Log.e(TAG, "${it.inpeople} ${it.outpeople} ${it.month}")
            inPeople.add(BarEntry(it.month.toFloat(), it.inpeople.toFloat()))
            outPeople.add(BarEntry(it.month.toFloat(), it.outpeople.toFloat()))
            lableMonth.add(it.month.toString())
        }
        initChart()
        stopProgress()
        set1 = BarDataSet(inPeople, "in")
        set1.color = Color.rgb(104, 241, 175)
        set2 = BarDataSet(outPeople, "out")
        set2.color = Color.rgb(255, 102, 0)
        val data = BarData(set1, set2)
        chart.data = data
        chart.barData.barWidth = barWidth
        lableCount = 0
        chart.xAxis.setDrawLabels(true)
        chart.xAxis.axisMinimum = lableMonth[0].toFloat()
        chart.xAxis.axisMaximum = lableMonth[0].toFloat() + chart.barData.getGroupWidth(groupSpace, barSpace) *
                lableMonth.size
        Log.e(TAG, "${chart.barData.getGroupWidth(groupSpace, barSpace)}  ${lableMonth.size}")

        chart.groupBars(lableMonth[0].toFloat(), groupSpace, barSpace)

        Log.e(TAG, "min max :${chart.xAxis.axisMinimum}  ${chart.xAxis.axisMaximum}")
        runOnUiThread {
            // specify the width each bar should have
            chart.invalidate()
        }
        Log.e(TAG, "size = ${lableMonth.size}  ")
        chart.xAxis.textColor = Color.BLACK
        runOnUiThread {
            // specify the width each bar should have
            chart.invalidate()
        }
    }

    private fun startProgress() {
        runOnUiThread {
            progress_layout.visibility = View.VISIBLE
            progress_circular.visibility = View.VISIBLE
        }
    }

    private fun stopProgress() {
        runOnUiThread {
            progress_layout.visibility = View.GONE
            progress_circular.visibility = View.GONE
        }
    }
}







