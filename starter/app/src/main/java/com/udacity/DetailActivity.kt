package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("fileName")) {
                tv_file_name_description.text = bundle.getString("fileName")!!
            }

            if (bundle.containsKey("status")) {
                tv_status_description.text = bundle.getString("status")!!
            }
        }

        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
