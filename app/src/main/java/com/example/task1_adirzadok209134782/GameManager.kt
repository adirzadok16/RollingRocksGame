package com.example.task1_adirzadok209134782

class GameManager(private val lifeCount: Int = 3) {

//    var score: Int = 0
//        private set

    var wrongAnswers: Int = 0
        private set

    val isGameLost: Boolean
        get() = wrongAnswers == lifeCount

    fun onCollision() {
        wrongAnswers++
        //toast and vibrate
    }


//    companion object{
//        private const val ANSWER_POINTS = 10
//    }
}