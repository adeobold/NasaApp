package com.android1.nasaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android1.nasaapp.databinding.MainActivityBinding
import com.android1.nasaapp.view.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}