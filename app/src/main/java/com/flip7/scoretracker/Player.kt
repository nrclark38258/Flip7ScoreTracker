package com.flip7.scoretracker

data class Player(
    val name: String,
    var totalScore: Int = 0,
    val roundScores: MutableList<Int> = mutableListOf()
) {
    fun addRoundScore(score: Int) {
        roundScores.add(score)
        totalScore += score
    }

    fun getPointsToWin(): Int {
        return maxOf(0, 200 - totalScore)
    }

    fun hasWon(): Boolean {
        return totalScore >= 200
    }
}
