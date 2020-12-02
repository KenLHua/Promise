package com.lokoheimer.promise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.promise_list)
        var testObject = JSONObject()
        testObject.put("date", "123")
        testObject.put("note", "note")
        var testArr = JSONArray()
        testArr.put(testObject)
        val layoutManager = LinearLayoutManager(this)
        val mAdapter = RecyclerAdapter(testArr)
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);





        findViewById<ExtendedFloatingActionButton>(R.id.exfab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        //val mAdapter = MyAdapter(arr, listener)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // Implement settings
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Log.d("Kenneth", "Default case")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

