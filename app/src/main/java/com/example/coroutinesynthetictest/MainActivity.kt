package com.example.coroutinesynthetictest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var list: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..1000) {
            list.add(i + Random().nextInt(10000))
        }
    }

    private fun sortList() {
        for (i in 0..100) {
            list.sort()
        }
    }

    private fun d(log: String) {
        d("TagSyntheticTest", log)
    }

    private fun sortWithCoroutine() {
        val startTime = Date().time
        d("coroutine start")
        launch(UI) {
            val result = async { sortList() }.await()
            d("result = $result")
            checkTime(startTime, result)
        }
    }

    private fun checkTime(startTime: Long, result: Unit) {
        val resultTime = Date().time - startTime
        d("result")
        d("finish\n it takes$resultTime millis")
        textView.text = resultTime.toString()
    }
}
