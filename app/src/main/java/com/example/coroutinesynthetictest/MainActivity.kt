package com.example.coroutinesynthetictest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
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
        for (i in 0..1000) {
            list.add(i + Random().nextInt(10000))
        }

        tvOneCoroutine.setOnClickListener { sortWithCoroutine() }
        tvManyCoroutines.setOnClickListener { sortWithManyCoroutines() }
        tvOneThread.setOnClickListener { sortWithOneThread() }
        tvManyThreads.setOnClickListener { sortWithManyThreads() }

    }

    private fun sortWithRx() {
        val startTime = Date().time



        val resultTime = checkTime(startTime)
    }

    private fun sortWithManyThreads() {
        val startTime = Date().time

        for (i in 0..1000) {
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
            for (i in 0..1000) {
                list.sort()
            }
            val resultTime: Long = checkTime(startTime)
            runOnUiThread({tvOneThread.text = resultTime.toString()})
        }.start()
    }

    private fun sortWithManyCoroutines() {
        val startTime = Date().time
        launch(UI) {
            val result = async {
                for (i in 0..1000) {
                    launch(CommonPool) {
                        list.sort()
                    }
                }
            }.await()
            val resultTime = checkTime(startTime, result)
            tvManyCoroutines.text = resultTime.toString()
        }
    }

    private fun sortWithCoroutine() {
        val startTime = Date().time
        launch(UI) {
            val result = async {
                for (i in 0..1000) {
                        list.sort()
                }
            }.await()
            val resultTime = checkTime(startTime, result)
            tvOneCoroutine.text = resultTime.toString()
        }
    }

    private fun checkTime(startTime: Long, result: Unit? = null) =
        Date().time - startTime
}
