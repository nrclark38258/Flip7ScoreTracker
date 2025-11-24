package com.flip7.scoretracker

data class Round(
    val roundNumber: Int,
    val scores: MutableMap<String, Int> = mutableMapOf()
) {
    fun getScoreForPlayer(playerName: String): Int {
        return scores[playerName] ?: 0
    }

    fun setScoreForPlayer(playerName: String, score: Int) {
        scores[playerName] = score
    }
}
