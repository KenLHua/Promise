package com.lokoheimer.promise

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var mAdapter: RecyclerAdapter
    lateinit var recyclerView: RecyclerView
    var default_notification_channel_id = "default";

    companion object {
        val NOTIFICATION_CHANNEL_ID = "10001"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnClickListener { v ->
            scheduleNotification(getNotification("5 sec delay"), 1000)
            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
        }


        // Creating test data
        val recyclerView = findViewById<RecyclerView>(R.id.promise_list)

        // Entry contains = {"date", "title", "note", "history" JSONArr [{"type", "date}]}

        DataUtils.getInstance()
        DataUtils.initializeData(this@MainActivity)


        // Feeding data to the Recycler View
        val layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(DataUtils.getInstance().getEntries())
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        val button = findViewById<ExtendedFloatingActionButton>(R.id.exfab)


        button.setOnClickListener { view ->
            var intent = Intent(this, PromiseCreation::class.java)
            startActivity(intent)
        }

        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            var dy = scrollY - oldScrollY
            var dx = scrollX - oldScrollX
            if (dy > 0)
                if (layoutManager.findLastCompletelyVisibleItemPosition() == DataUtils.getInstance()
                        .getEntries().length() - 1
                ) {
                    button.show()
                } else
                    button.hide()
            else
                button.show()
        }
    }

    private fun scheduleNotification (notification: Notification, delay: Int){
        var notificationIntent = Intent(this, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID,1)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION,notification)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        var futureInMillis = SystemClock.elapsedRealtime()+delay;
        var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        assert(alarmManager != null)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,futureInMillis, pendingIntent)
    }
    private fun getNotification (content: String): Notification {
        var builder:NotificationCompat.Builder = NotificationCompat.Builder(this,
            default_notification_channel_id)
            .setSmallIcon(R.drawable.ic_add_24)
            .setContentTitle("Promise - Text Holder")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    fun snackbar(string: String?) {
        Snackbar.make(window.decorView.rootView, string as CharSequence, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private fun attemptRefresh() {
        if (DataUtils.getInstance().refreshRequired) {
            mAdapter.dataSet = DataUtils.getInstance().getEntries()
            if (DataUtils.getInstance().getEntries().length() != 0) {
                //Show message that there is no data present
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        attemptRefresh()
    }

    override fun onStop() {
        DataUtils.getInstance().saveEntries()
        super.onStop()
    }


}


