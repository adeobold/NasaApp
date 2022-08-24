package com.android1.nasaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android1.nasaapp.databinding.MainActivityBinding
import com.android1.nasaapp.utils.SETTINGS_THEME
import com.android1.nasaapp.view.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        if(intent.getStringExtra(SETTINGS_THEME).toString()==resources.getString(R.string.ThemeRoundText)){
            setTheme(R.style.RoundTheme)
        }else{
            setTheme(R.style.SquareTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}