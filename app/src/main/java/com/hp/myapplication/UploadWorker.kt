package com.hp.myapplication

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

public  class UploadWorker(context: Context ,params:WorkerParameters) : Worker(context,params) {

    companion object {

        const val  WORKER_KEY =  "worker_key"
    }
    override fun doWork(): Result {
        try {

            val count : Int = inputData.getInt(MainActivity.KEY_COUNT_VALUES,0)
            for (i:Int in  0 until count){
                Log.i("My_Tag","uploading $i")
            }
            val   time  = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
            val   currentDate  =  time.format(Date())
            val   outputValuesData  =  Data.Builder().putString(WORKER_KEY,currentDate).build()
            return  Result.success(outputValuesData)
        } catch (ext: Exception) {
            return  Result.failure()
        }
    }


}