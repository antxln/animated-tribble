package com.example.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> =
            listOf(box_one_text, box_two_text, box_three_text,
                box_four_text, box_five_text, constraint_layout,
                red_button, yellow_button, green_button)
        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }

    private fun makeColored(view: View) {
        when (view.id) {
            // Color resources
            R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)
            // Android color resources
            R.id.box_three_text -> view.setBackgroundResource(android.R.color.holo_red_dark)
            R.id.box_four_text -> view.setBackgroundResource(android.R.color.holo_orange_dark)
            R.id.box_five_text -> view.setBackgroundResource(android.R.color.holo_green_dark)

            // Custom colors
            R.id.red_button -> box_three_text.setBackgroundResource(R.color.red)
            R.id.yellow_button -> box_four_text.setBackgroundResource(R.color.yellow)
            R.id.green_button -> box_five_text.setBackgroundResource(R.color.green)

            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}
