package com.lokoheimer.promise

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract
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
import java.util.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var mAdapter: RecyclerAdapter
    lateinit var recyclerView: RecyclerView

    companion object {
        const val default_notification_channel_id = "default";
        const val NOTIFICATION_CHANNEL_ID = "10001"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Creating test data
        val recyclerView = findViewById<RecyclerView>(R.id.promise_list)

        // Entry contains = {"date", "title", "note", "history" JSONArr [{"type", "date}]}

        DataUtils.getInstance()
        DataUtils.initializeData(this@MainActivity)

        // Feeding data to the Recycler View
        val layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(DataUtils.getInstance().getEntries())
        recyclerView.adapter = mAdapter;
        recyclerView.layoutManager = layoutManager;

        // Set notifications after data is ready
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnClickListener {
            // TODO: Set notification on add automatically
            setAllNotifications()
        }

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

    private fun createNotification (title: String, content: String): Notification {
        var builder:NotificationCompat.Builder = NotificationCompat.Builder(this,
            default_notification_channel_id)
            .setSmallIcon(R.drawable.ic_add_24)
            .setContentTitle("Promise -$title")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    private fun scheduleNotification (UID: Int,notification: Notification, dayDelay: Int){
        var factor = 1000;
        var alarmUp = (PendingIntent.getBroadcast(applicationContext,UID, Intent("com.lokoheimer.promise.Promise_Notification"), PendingIntent.FLAG_NO_CREATE) != null)
        if (alarmUp){
            Log.d("Notification", "Alarm is already Active for $UID")
        }
        else{
            // TODO: Implement delay
            var calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE,1);

            var notificationIntent = Intent(this, MyNotificationPublisher::class.java)
            notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID,1)
            notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION,notification)

            var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,UID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            //var futureInMillis = SystemClock.elapsedRealtime()+(dayDelay*factor);
            var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.timeInMillis, pendingIntent)
            var delayInMinutes = 1000 * 60
            var minutes = 1;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1*delayInMinutes as Long, pendingIntent);

            Log.d("Notification", "Setting alarm for $UID for $minutes minutes")

        }

    }

    private fun setAllNotifications(){
        Log.d("Notification", "Setting all notifications")
        var entries = DataUtils.getEntries()
        for (i in 0 until entries.length()) {
            var entry = entries.getJSONObject(i)
            var notification = createNotification(entry.get(Constant.JSON_TITLE) as String, "Did you keep your promise?")
            scheduleNotification((entry.get(Constant.JSON_UID) as String).toInt(), notification, entry.get(Constant.JSON_FREQ) as Int)
        }
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

    fun snackbar(string: String?) {
        Snackbar.make(window.decorView.rootView, string as CharSequence, Snackbar.LENGTH_LONG).setAction("Action", null).show()
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

    override fun onResume() {
        super.onResume()
        attemptRefresh()
    }

    override fun onStop() {
        DataUtils.getInstance().saveEntries()
        super.onStop()
    }


}


