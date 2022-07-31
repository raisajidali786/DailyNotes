package com.rsa.daily.notes.view

import android.content.Intent
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    fun showToast(msg: String) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show()
    }
}