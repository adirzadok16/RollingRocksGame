package com.example.task1_adirzadok209134782

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class EndGame :  AppCompatActivity() {

    private lateinit var score_LBL_result: MaterialTextView
    private lateinit var score_BTN_restart : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_end)
        findviews()
        initViews()
    }

    private fun findviews() {
        score_LBL_result = findViewById(R.id.score_LBL_result)
        score_BTN_restart = findViewById(R.id.score_BTN_restart)

    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val message = bundle?.getString("status")

        score_LBL_result.text = buildString {
            append(message)
        }

        score_BTN_restart.setOnClickListener { v -> restart()}
    }

    private fun restart() {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
        finish()
    }


}