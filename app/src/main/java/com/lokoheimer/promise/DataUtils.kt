
package com.lokoheimer.promise

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.StringBuilder

object DataUtils {
    private var instance: DataUtils? = null
    // Entry contains = {"date", "title", "note", "history" JSONArr [{"type", "date}]}
    private var entries = JSONArray()
    private var dir: File? = null
    var refreshRequired: Boolean = false


    init{}
    fun initializeData(context: Context) {
        if(dir != null)
            return
        this.dir = File(context!!.filesDir, Constant.FOLDER_PROMISE_DATA)
        loadEntries(this.dir!!)

    }

    fun getInstance(): DataUtils {
        val i = instance;
        if(i != null)
            return i
        else {
            instance = this
            return instance as DataUtils
        }
    }

    fun loadEntries(dir: File){
        var gpxfile = File(dir, Constant.FILE_NAME)
        var sb = StringBuilder();
        if(gpxfile.exists()){
            var reader = BufferedReader(FileReader(gpxfile));
            var line: String? = null;
            while ({ line = reader.readLine(); line }() != null)
                sb.append(line)
            entries = JSONArray(sb.toString())
            Log.d("Kenneth", "Saved Data array created")
        }
        // No previous data
        else{
            entries = JSONArray();
            Log.d("Kenneth", "New Data array created")
        }
    }
    fun getEntries(): JSONArray {
        return entries!!
    }
    fun createTestData(){
        var testObject = JSONObject()
        testObject.put("name", "testtitle")
        testObject.put("date", "123")
        testObject.put("note", "note")
        var testObjectRecord = JSONObject()
        testObjectRecord.put("type", "strike")
        testObjectRecord.put("date", "July 09 2020")
        var testObjectHistory = JSONArray()
        testObjectHistory.put(testObjectRecord)
        testObject.put("history", testObjectHistory)

        var testObject2 = JSONObject()
        testObject2.put("name", "testtitle2")
        testObject2.put("date", "123")
        testObject2.put("note", "note")
        var testArr = JSONArray()
        testArr.put(testObject)
        testArr.put(testObject2)
        entries = testArr;
        getInstance().saveEntries();

    }
    fun saveEntries(){
        if(!dir!!.exists())
            dir!!.mkdir()

        try{
            var gpxfile = File(dir, Constant.FILE_NAME)
            var writer = FileWriter(gpxfile);
            writer.append(entries.toString());
            writer.flush();
            writer.close();
            refreshRequired = true;
            Log.d("Kenneth", "Saved")
        }catch (e: Exception){
            Log.e("DataUtils Saving", e.toString(), e)
        }
    }
    fun deleteEntries(){
        try{
            var gpxfile = File(dir, Constant.FILE_NAME)
            if(gpxfile.exists())
                gpxfile.delete()
        }catch(e: Exception){
            Log.e("DataUtils Delete", e.toString(), e)
        }
    }
    fun addEntry(name: String, desc: String, date: String, time: String, notify: String, freq: String, color: String){
        var entry = JSONObject()
        entry.put("name", name)
        entry.put("desc",desc)
        entry.put("date",date)
        entry.put("time",time)
        entry.put("notify",notify)
        entry.put("freq",freq)
        entry.put("color", color)
        entries.put(entry)
        refreshRequired = true
    }

}
