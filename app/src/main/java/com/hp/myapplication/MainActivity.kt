package com.hp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object{
        const val  KEY_COUNT_VALUES ="count_values"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
          //  setOneTimeWorkRequest()

            setPeriodicWorkRequest()
        }
    }

    private  fun  setOneTimeWorkRequest(){
        val  workManager : WorkManager  =  WorkManager.getInstance(applicationContext)



        //  set input data  from main class
        val   data : Data =  Data.Builder().putInt(KEY_COUNT_VALUES,240).build()


        // add  constrants
        val   constraints : Constraints =  Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();


        val  uploadRequest =  OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val  filteringWorker   =  OneTimeWorkRequest.Builder(FilteringWorker::class.java).build()


        var compressingWorker  = OneTimeWorkRequest.Builder(CompressingWorker::class.java).build()

        val  downloadWorker  =  OneTimeWorkRequest.Builder(DownloadingWorker::class.java).build()


        // define  for paraller task runing same time
        val   parallerWorkar =  mutableListOf<OneTimeWorkRequest>()
        parallerWorkar.add(downloadWorker)
        parallerWorkar.add(filteringWorker)


        // define  chaning with paraller work

        WorkManager.getInstance(applicationContext)
            .beginWith(parallerWorkar)
            .then(compressingWorker)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            textView.text = it.state.name

            if(it.state.isFinished){

                //  get  output data from background service
                val   data =  it.outputData
                val  message  =  data.getString(UploadWorker.WORKER_KEY)

                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()

            }
        })

    }

    private  fun setPeriodicWorkRequest(){

        // define here  periodic request
        val  periodicWorkRequest  =
            PeriodicWorkRequest.Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES).build()
           WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)


    }
}

