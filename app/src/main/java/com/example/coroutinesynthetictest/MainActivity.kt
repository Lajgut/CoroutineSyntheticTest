package com.example.coroutinesynthetictest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var list: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = ArrayList()
        for (i in 0 until 1000) {
            list.add(i + Random().nextInt(10000))
        }

        list.sort()

        tvOneCoroutine.setOnClickListener { sortWithCoroutine() }
        tvManyCoroutines.setOnClickListener { sortWithManyCoroutines() }
        tvOneThread.setOnClickListener { sortWithOneThread() }
        tvManyThreads.setOnClickListener { sortWithManyThreads() }

    }

    private fun sortWithManyThreads() {
        val startTime = Date().time

        for (i in 0 until 1000) {
            Thread {
                list.sort()
            }.start()
        }

        val resultTime = checkTime(startTime)
        tvManyThreads.text = resultTime.toString()
    }

    private fun sortWithOneThread() {
        val startTime = Date().time

        Thread {
            for (i in 0 until 1000) {
                list.sort()
            }
            val resultTime: Long = checkTime(startTime)
            runOnUiThread({tvOneThread.text = resultTime.toString()})
        }.start()
    }

    private fun sortWithManyCoroutines() {
        val startTime = Date().time
        launch(UI) {
                for (i in 0 until 1000) {
                    async {
                        list.sort()
                        d("tag", "sort $i")
                    }.await()

                    if (i == 999) {
                        val resultTime = checkTime(startTime)
                        tvManyCoroutines.text = resultTime.toString()
                    }
                }
        }
    }

    private fun sortWithCoroutine() {
        val startTime = Date().time
        launch(UI) {
            async {
                for (i in 0 until 1000) {
                        list.sort()
                }
            }.await()
            val resultTime = checkTime(startTime)
            tvOneCoroutine.text = resultTime.toString()
        }
    }

    private fun checkTime(startTime: Long) =
        Date().time - startTime

}
