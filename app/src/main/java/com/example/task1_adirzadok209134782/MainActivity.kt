package com.example.task1_adirzadok209134782

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NUM_ROWS = 7  // number of rows
        private const val NUM_COLS = 3  // number of cols
        private const val DELAY: Long = 1000L
    }

    //private lateinit var main_IMG_firstLineOfStones: Array<ShapeableImageView>
//    private lateinit var main_IMG_secondLineOfStones: Array<ShapeableImageView>
//    private lateinit var main_IMG_thiredLineOfStones: Array<ShapeableImageView>
//    private lateinit var main_IMG_fourthLineOfStones: Array<ShapeableImageView>
//    private lateinit var main_IMG_fifthLineOfStones: Array<ShapeableImageView>
    private val TAG: String = "MainActivity"
    private var anotherRock = true;
    private lateinit var main_IMG_hearts: Array<ShapeableImageView>
    private var RocksOnApp = Array(NUM_ROWS) { arrayOfNulls<ShapeableImageView>(NUM_COLS) }
    private lateinit var main_IMG_carLine: Array<ShapeableImageView>
    private var currentCarPosition = 1
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton
    private lateinit var gameManager: GameManager
    private lateinit var timerJob: Job
    private var gameStart = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        startGame()
    }

    private fun initViews() {
        main_BTN_left.setOnClickListener { v -> moveLeft() }
        main_BTN_right.setOnClickListener { v -> moveRight() }
    }

    private fun moveRight(): Boolean {
        if (currentCarPosition < 2) {
            updateCarPosition(currentCarPosition, currentCarPosition + 1)
            currentCarPosition++
            return true
        }
        return false
    }

    private fun moveLeft(): Boolean {
        if (currentCarPosition > 0) {
            updateCarPosition(currentCarPosition, currentCarPosition - 1)
            currentCarPosition--
            return true
        }
        return false
    }

    private fun updateCarPosition(oldPosition: Int, newPosition: Int) {
        main_IMG_carLine[oldPosition].setImageResource(R.drawable.greybackground)
        main_IMG_carLine[newPosition].setImageResource(R.drawable.car)
    }

    private fun generateRock() {
        val num = Random.nextInt(0, 3)
        RocksOnApp[0][num]?.visibility = View.VISIBLE
    }

    private fun startGame() {
        if (!gameStart) {
            gameStart = true
            timerJob = lifecycleScope.launch {
                while (gameStart) {
                    checkCollision()
                    moveRocks()
                    if (anotherRock) {
                        generateRock()
                    }
                    anotherRock = !anotherRock
                    delay(DELAY)
                }
            }
        }
    }

    private fun checkCollision() {
        if (RocksOnApp[NUM_ROWS - 2][currentCarPosition]?.visibility == View.VISIBLE) {
            gameManager.onCollision()
            if (gameManager.wrongAnswers != 0) {
                main_IMG_hearts[main_IMG_hearts.size - gameManager.wrongAnswers].visibility =
                    View.INVISIBLE
                toastAndVibrate()
            }
            if (gameManager.isGameLost) {
                //move to end game;
                changeActivity()
                Log.d(TAG, "you lost")
            }

        }
    }


    private fun changeActivity() {
         timerJob.cancel()
        val intent = Intent(this, EndGame::class.java);
        var bundle = Bundle()
        bundle.putString("status", "😭Game Over!")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun toastAndVibrate() {
        toast()
        vibrate()
    }

    private fun toast() {
        Toast
            .makeText(
                this,
                "You Crashed!",
                Toast.LENGTH_SHORT
            ).show()
    }


    private fun vibrate() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Vibrate for VIBRATE_DURATION milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    300,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            //deprecated in API 26
            v.vibrate(300)
        }

    }


    private fun moveRocks() {
        for (i in NUM_ROWS - 2 downTo 0) {
            for (j in 0 until NUM_COLS) {
                if (RocksOnApp[i][j]?.visibility == View.VISIBLE) {
                    RocksOnApp[i + 1][j]?.visibility = View.VISIBLE
                    RocksOnApp[i][j]?.visibility = View.INVISIBLE
                }
            }
        }

    }

    private fun findViews() {

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        for (i in 0 until NUM_ROWS - 1) {
            for (j in 0 until NUM_COLS) {
                // Create a unique identifier for each ImageView
                var resourceName = "main_IMG_rock${i}${j}"

                // Assuming you have the IDs set in the XML layout
                var resId = resources.getIdentifier(resourceName, "id", packageName)

                // Initialize each ShapeableImageView in the matrix
                RocksOnApp[i][j] = findViewById(resId)
            }
        }

//        main_IMG_firstLineOfStones = arrayOf(
//            findViewById(R.id.main_IMG_rock01),
//            findViewById(R.id.main_IMG_rock02),
//            findViewById(R.id.main_IMG_rock03)
//        )
//
//        main_IMG_secondLineOfStones = arrayOf(
//            findViewById(R.id.main_IMG_rock11),
//            findViewById(R.id.main_IMG_rock12),
//            findViewById(R.id.main_IMG_rock13)
//        )
//
//        main_IMG_thiredLineOfStones= arrayOf(
//            findViewById(R.id.main_IMG_rock21),
//            findViewById(R.id.main_IMG_rock22),
//            findViewById(R.id.main_IMG_rock23)
//        )
//
//        main_IMG_fourthLineOfStones = arrayOf(
//            findViewById(R.id.main_IMG_rock31),
//            findViewById(R.id.main_IMG_rock32),
//            findViewById(R.id.main_IMG_rock33)
//        )
//
//        main_IMG_fifthLineOfStones = arrayOf(
//            findViewById(R.id.main_IMG_rock41),
//            findViewById(R.id.main_IMG_rock42),
//            findViewById(R.id.main_IMG_rock43)
//        )
//
        main_IMG_carLine = arrayOf(
            findViewById(R.id.main_IMG_carleft),
            findViewById(R.id.main_IMG_carmiddle),
            findViewById(R.id.main_IMG_carright)
        )

    }
}


