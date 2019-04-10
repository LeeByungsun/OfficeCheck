package com.buggyani.officecheck

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import com.buggyani.officecheck.OfficeApplication.Companion.api_Server
import com.buggyani.officecheck.adapter.CompanyListAdapter
import com.buggyani.officecheck.adapter.RecyclerItemClickListener
import com.buggyani.officecheck.databinding.ActivityMainBinding
import com.buggyani.officecheck.model.CompanyData
import com.buggyani.officecheck.network.response.Item
import com.buggyani.officecheck.network.APIInfo.Companion.API_KEY
import com.buggyani.officecheck.network.response.NpsBplcInfoInqireServiceResponseVo
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import java.util.HashSet
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private var itemList = ArrayList<CompanyData>()
    private var disposable: Disposable? = null
    private var companyName: String? = null
    private var isScrolling = false
    private var currentItem = 0
    private var totalItem = 0
    private var scrollOutItems = 0
    private var binding: ActivityMainBinding? = null
    private var regNumber = 0

    private lateinit var companyDataAdpter: CompanyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUI()

        searchBtn.setOnClickListener {
            if (!companyEditText.text.toString().isEmpty()) {
                getCompanyData(false)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
        listView.visibility = View.VISIBLE
    }

    private fun initUI() {
        initRecyclerView(itemList)
    }


    fun getCompanyData(setName: Boolean) {
        if (!setName) {
            companyName = companyEditText.text.toString().trim()
            companyEditText.text.clear()
            companyEditText.clearFocus()
            hideKeyboard()

        }
        startProgress()
        Log.e(TAG, "|$companyName|")
        if (setName) {
            disposable = api_Server.getCompanyData(companyName!!, regNumber, 1, API_KEY, 1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: String? ->
                    Log.e(TAG, data)
                    val jsonObj: JSONObject
                    try {
                        jsonObj = XML.toJSONObject(data)
                        converToJson(jsonObj.toString(), setName)
                        stopProgress()
                    } catch (e: JSONException) {
                        Log.e(TAG, e.message)
                        e.printStackTrace()
                    } finally {
                        stopProgress()
                    }
                }, { t: Throwable? ->
                    t!!.printStackTrace()
                    Log.d(TAG, "----------------------Error--------------------")
                    stopProgress()
                })
        } else {
            disposable = api_Server.getCompanyData(companyName!!, 1, API_KEY, 1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: String? ->
//                    Log.e(TAG, data)
                    val jsonObj: JSONObject
                    try {
                        jsonObj = XML.toJSONObject(data)
                        converToJson(jsonObj.toString(), setName)
                        stopProgress()
                    } catch (e: JSONException) {
                        Log.e(TAG, e.message)
                        e.printStackTrace()
                    } finally {
                        stopProgress()
                    }
                }, { t: Throwable? ->
                    t!!.printStackTrace()
                    Log.d(TAG, "----------------------Error--------------------")
                    stopProgress()
                })
        }

    }

    private fun converToJson(data: String, setName: Boolean) {
        val gson = Gson()
        val rsp = gson.fromJson(data, NpsBplcInfoInqireServiceResponseVo::class.java)
        val item: ArrayList<Item> = rsp.response.body.items.item
        var itemTempList = ArrayList<CompanyData>()
        item.forEach {
            val regNum: Int = it.bzowrRgstNo.replace("*", "").toInt()
            if (setName) {
                itemTempList.add(CompanyData(it.wkplNm, it.wkplRoadNmDtlAddr, regNum, it.seq, it.dataCrtYm))
            } else {
                itemTempList.add(CompanyData(it.wkplNm, "", regNum, 0, 0))
            }

        }
        val deleteDupData: HashSet<CompanyData> = HashSet(itemTempList)
        itemTempList = ArrayList(deleteDupData);
        itemList.clear()
        itemTempList.forEach {
            itemList.add(it)
        }
        if (setName) {
            itemList.sortByDescending { it.date }
            itemList.forEach {
                println(it)
            }
            getCompanyDetailData(itemList[0].seq, itemList[0].date)
        } else {
            runOnUiThread {
                companyDataAdpter.notifyDataSetChanged()
            }


        }

    }


    private fun initRecyclerView(data: ArrayList<CompanyData>) {
        companyDataAdpter = CompanyListAdapter(data)
        listView.adapter = companyDataAdpter
        listView.layoutManager = LinearLayoutManager(applicationContext)
        listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e(TAG, "State = $newState")
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager: LinearLayoutManager = listView.layoutManager as LinearLayoutManager
                currentItem = manager.childCount
                totalItem = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()

//                Log.e(TAG, "currentItem = $currentItem")
//                Log.e(TAG, "scrollOutItems = $scrollOutItems")
//                Log.e(TAG, "totalItem = $totalItem")
                if (isScrolling && (currentItem + scrollOutItems == totalItem)) {
                    Log.e(TAG, "Bottom")
                    isScrolling = false
                }
            }
        })
        listView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                listView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemLongClick(view: View?, position: Int) {
                    }

                    override fun onItemClick(view: View, position: Int) {
                        Log.e(TAG, "onItemClick = $position")
                        regNumber = itemList[position].regnum
                        companyName = itemList[position].name
//                        itemList.clear()
//                        companyDataAdpter.notifyDataSetChanged()
                        listView.visibility = View.INVISIBLE
                        getCompanyData(true)
                    }
                })
        )
    }


    private fun getCompanyDetailData(seq: Int, date: Int) {

        Log.e(TAG, "|$seq|")
        startProgress()
        disposable = api_Server.getDetailCompanyData(seq, API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data: String? ->
                Log.e(TAG, data)
                val jsonObj: JSONObject
                try {
                    jsonObj = XML.toJSONObject(data)
                    Log.d(TAG, jsonObj.toString())
                    val item = converToJsonDetail(jsonObj.toString())
                    println(seq)
                    println(date)

                    sendActivity(item)
                } catch (e: JSONException) {
                    Log.e(TAG, e.message)
                    e.printStackTrace()
                } finally {
                    stopProgress()
                }
            }, { t: Throwable? ->
                t!!.printStackTrace()
                getCompanyDetailData(seq, date)
            })
    }

    private fun converToJsonDetail(data: String): Item {
        val gson = Gson()
        val rsp = gson.fromJson(data, NpsBplcInfoInqireServiceResponseVo::class.java)
        return  rsp.response.body.item
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
    }

    private fun startProgress() {
//        progress_layout.invalidate()
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

    private fun sendActivity(item: Item) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("companydetail", item)
        intent.putExtra("seqlist", itemList)
        stopProgress()
        startActivity(intent)
        itemList.clear()
        companyDataAdpter.notifyDataSetChanged()
    }
}

