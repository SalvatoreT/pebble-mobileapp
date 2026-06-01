package dev.sal.libpebble3consumer

import android.app.Activity
import android.os.Bundle
import android.util.Log

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LibPebble3Consumer", LibPebbleUsage.describe())
    }
}
