package com.lokoheimer.promise

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class InspectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspect_layout)
        // Get promise's data
        var jsonString :String = intent.extras!!.get(Constant.JSON_PROMISE) as String
        var promiseJSON = JSONObject(jsonString)
        var color = (promiseJSON["color"] as String).toInt()

        var routine = findViewById<TextView>(R.id.routine)
        var titleBox = findViewById<TextView>(R.id.cardTitle)
        //var descBox = findViewById<EditText>(R.id.descText)
        var dateBox = findViewById<TextView>(R.id.cardDate)
        var timeBox = findViewById<TextView>(R.id.cardDuration)
        var notifBox = findViewById<CheckBox>(R.id.checkbox)
        // TODO
        //var notifFreqButtons = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
        var card = findViewById<MaterialCardView>(R.id.item_card)

        // Filling card with relevant data
        titleBox.text = promiseJSON["title"] as CharSequence?
        //descBox.text = promiseJSON["desc"] as CharSequence?
        dateBox.text = promiseJSON["date"] as CharSequence?

        // Themeing page
        routine.setTextColor(color)
        notifBox.compoundDrawables[0].setTint(color)
        card.setCardBackgroundColor(color)

        notifBox.setOnCheckedChangeListener { buttonView, isChecked ->

        }




    }
    fun snackbar(string: String?) {
        Snackbar.make(findViewById(R.id.root_layout), string as CharSequence, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }


}