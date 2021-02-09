package com.lokoheimer.promise

import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class RecyclerAdapter constructor(var dataSet: JSONArray): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  dataSet.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val js = dataSet[position] as JSONObject
            holder.bind(js[Constant.JSON_DATE] as CharSequence, js[Constant.JSON_NAME] as CharSequence, js[Constant.JSON_FREQ] as CharSequence,js[Constant.JSON_COLOR] as CharSequence?)
        } catch (e: JSONException) {
            Log.e("Adapter", e.toString(), e)
        }
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener,
        View.OnCreateContextMenuListener {

        val date: TextView = v.findViewById<TextView>(R.id.date);
        val name: TextView = v.findViewById<TextView>(R.id.name);
        val freq: TextView = v.findViewById<TextView>(R.id.freq);
        var cv: MaterialCardView = v.findViewById<MaterialCardView>(R.id.item_card)


        fun bind(sDate: CharSequence?, sName: CharSequence?, sFreq: CharSequence?, sColor: CharSequence?) {
            date.text = sDate
            name.text = sName
            freq.text = sFreq
            cv.setCardBackgroundColor(sColor.toString().toInt())
            cv.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            var intent = Intent(v.context, InspectActivity::class.java)
            var promiseJSON = DataUtils.getInstance().getEntries()[adapterPosition].toString()
            intent.putExtra(Constant.JSON_PROMISE, promiseJSON)
            v.context.startActivity(intent)
            Log.d("Listener", "hello ken")
        }

        override fun onCreateContextMenu(
                menu: ContextMenu,
                v: View,
                menuInfo: ContextMenu.ContextMenuInfo
        ) {
            val Edit = menu.add(adapterPosition, 1, 1, "Edit")
            val Delete =
                menu.add(adapterPosition, 100, 2, "Delete") //groupId, itemId, order, name
        }


    }


}