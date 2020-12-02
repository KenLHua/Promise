package com.lokoheimer.promise

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

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
            holder.bind(js[Constant.JSON_DATE] as CharSequence, js[Constant.JSON_NOTE] as CharSequence)
        } catch (e: JSONException) {
            Log.e("Adapter", e.toString(), e)
        }
    }
    inner class ViewHolder(var v: View) : RecyclerView.ViewHolder(v), View.OnClickListener,
        View.OnCreateContextMenuListener {

        val date: TextView = v.findViewById<TextView>(R.id.date);
        val note: TextView = v.findViewById<TextView>(R.id.title);
        lateinit var cv: MaterialCardView

        fun bind(sDate: CharSequence?, sNote: CharSequence?) {
            date.text = sDate
            note.text = sNote
        }

        override fun onClick(v: View) {
            //mViewListener.onClick(v, adapterPosition)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo
        ) {
            val Edit = menu.add(adapterPosition, 1, 1, "Edit")
            val Delete =
                menu.add(adapterPosition, 100, 2, "Delete") //groupId, itemId, order, title
        }
        init {
            this.cv = v.findViewById<MaterialCardView>(R.id.item_card)
        }


    }


}