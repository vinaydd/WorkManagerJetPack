package com.hp.myapplication

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

public  class DownloadingWorker(context: Context, params: WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        try {
            for (i:Int in  0..3000){
                Log.i("My_Tag","filtering $i")
            }
            val   time  = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
            val   currentDate  =  time.format(Date())
            Log.i("My_Tag","complite $currentDate")

            return  Result.success()
        } catch (ext: Exception) {
            return  Result.failure()
        }
    }


}