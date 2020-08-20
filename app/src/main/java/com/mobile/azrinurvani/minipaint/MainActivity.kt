package com.mobile.azrinurvani.minipaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//TODO: Step 1.5 Delete setContentView(R.layout.activity_main) and create instance of MyCanvasView class
        //setContentView(R.layout.activity_main)
        val myCanvasView = MyCanvasView(this)
        //TODO: Step 1.6  Request the full screen for the layout of myCanvasView. Do this by setting the SYSTEM_UI_FLAG_FULLSCREEN flag on myCanvasView.
        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        //TODO: Step 1.7 Below that, set the content view to myCanvasView.
        setContentView(myCanvasView)
    }
}
