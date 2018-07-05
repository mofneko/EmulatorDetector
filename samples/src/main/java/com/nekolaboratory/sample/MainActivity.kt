package com.nekolaboratory.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.nekolaboratory.EmulatorDetector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var check_text = this.findViewById<TextView>(R.id.check_text)
        check_text.setText("isEmulator: " + EmulatorDetector.isEmulator(this))
    }
}
