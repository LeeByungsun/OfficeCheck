package com.buggyani.officecheck.adapter

/**
 * Created by bslee on 2019-03-10.
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buggyani.officecheck.R
import com.buggyani.officecheck.model.CompanyData
import kotlinx.android.synthetic.main.item_company.view.*

class CompanyListAdapter(postsData: ArrayList<CompanyData>) :


    RecyclerView.Adapter<CompanyListAdapter.CompnayViewHolder>() {
    override fun onBindViewHolder(holder: CompnayViewHolder, position: Int) {
        val companyData = postsList!![position]
        companyData.run {
//            Log.e(TAG, "company = $name")
//            Log.e(TAG, "usderId = $addr")
            holder.compnay.text = name
            holder.addr.text = addr
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CompnayViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_company, p0, false)
        return CompnayViewHolder(view)
    }

    private val TAG = javaClass.simpleName
    private var postsList: MutableList<CompanyData>? = postsData


    override fun getItemCount(): Int {
        return postsList!!.size
    }


    class CompnayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var compnay = view.company!!
        var addr = view.addr!!
    }
}