package com.flip7.scoretracker

data class GameState(
    val players: MutableList<Player> = mutableListOf(),
    var currentRound: Int = 1,
    var isGameOver: Boolean = false,
    var winner: Player? = null
) {
    fun addRoundScores(scores: Map<Player, Int>) {
        scores.forEach { (player, score) ->
            player.addRoundScore(score)
        }

        checkForWinner()

        if (!isGameOver) {
            currentRound++
        }
    }

    private fun checkForWinner() {
        val playersOver200 = players.filter { it.totalScore >= 200 }

        if (playersOver200.isNotEmpty()) {
            isGameOver = true
            winner = if (playersOver200.size == 1) {
                playersOver200.first()
            } else {
                playersOver200.maxByOrNull { it.totalScore }
            }
        }
    }

    fun reset() {
        players.forEach { player ->
            player.totalScore = 0
            player.roundScores.clear()
        }
        currentRound = 1
        isGameOver = false
        winner = null
    }
}
