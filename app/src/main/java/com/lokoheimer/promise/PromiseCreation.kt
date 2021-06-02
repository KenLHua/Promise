package com.lokoheimer.promise

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import dev.sasikanth.colorsheet.ColorSheet
import org.w3c.dom.Text
import java.util.*


class PromiseCreation : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.promise_creation)


        var circle = findViewById<View>(R.id.circle)
        var title = findViewById<TextView>(R.id.title)
        var routine = findViewById<TextView>(R.id.routine)
        var nameBox = findViewById<EditText>(R.id.nameText)
        var descBox = findViewById<EditText>(R.id.descText)
        var dateBox = findViewById<EditText>(R.id.dateText)
        var timeBox = findViewById<EditText>(R.id.timeText)
        var notifBox = findViewById<CheckBox>(R.id.checkbox)
        var periodButtons = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
        var notifFreq = 0
        var button = findViewById<Button>(R.id.exfab)
        var cardColor = resources.getColor(R.color.pastel_blue, null)


        circle.setOnClickListener { view ->
            snackbar("hello")
            ColorSheet().colorPicker(resources.getIntArray(R.array.colors), listener = { color ->
                cardColor = color
                circle.background.colorFilter = BlendModeColorFilter(cardColor, BlendMode.SRC_ATOP)
                //  Change colors of activity along with changing the color
                title.setTextColor(cardColor)
                routine.setTextColor(cardColor)
                button.setBackgroundColor(cardColor)
                notifBox.compoundDrawables[0].setTint(cardColor)
            }).show(supportFragmentManager)
        }

        // Show finish button if nameBox contains text
        nameBox.addTextChangedListener (
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, after: Int, count: Int) {
                    if(s.isEmpty())
                        button.visibility = GONE
                    else
                        button.visibility = VISIBLE
                }
                // Nothing needed
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
            }
        )

        // Show frequency of notifcation buttons only if box is checked
        notifBox.setOnCheckedChangeListener {view, isChecked ->
            if(isChecked) {
                periodButtons.visibility = VISIBLE
                notifFreq = 1
            }
            else{
                periodButtons.visibility = GONE
                periodButtons.clearChecked()
                notifFreq = 0
            }
        }
        periodButtons.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                when(checkedId){
                    R.id.button1 -> notifFreq = 1
                    R.id.button2 -> notifFreq = 2
                    R.id.button3 -> notifFreq = 7
                    R.id.button4 -> notifFreq = 30
                }
            }
            else
                notifFreq = 0

        }

        button.setOnClickListener {view ->
            var uniqueKey = DataUtils.nextUID()
            DataUtils.getInstance().addEntry(uniqueKey.toString(), nameBox.text.toString(),descBox.text.toString(),
                    dateBox.text.toString(), timeBox.text.toString(), notifBox.isChecked.toString(), notifFreq.toString(), cardColor.toString() )
            this.finish()
        }




    }
    fun snackbar(string: String?) {
        Snackbar.make(window.decorView.rootView, string as CharSequence, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }
}






