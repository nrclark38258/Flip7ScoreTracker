package com.flip7.scoretracker

data class GameState(
    val players: MutableList<Player> = mutableListOf(),
    var currentRound: Int = 1,
    var isGameOver: Boolean = false,
    var winner: Player? = null,
    private val rounds: MutableList<Round> = mutableListOf()
) {
    fun addRoundScores(
        scores: Map<Player, Int>,
        cardSelections: Map<Player, CardSelection> = emptyMap(),
        manualOverrides: Map<Player, Int> = emptyMap()
    ) {
        val round = Round(currentRound)
        scores.forEach { (player, score) ->
            player.addRoundScore(score)
            round.setScoreForPlayer(player.name, score)
        }
        cardSelections.forEach { (player, selection) ->
            round.setCardSelectionForPlayer(player.name, selection)
        }
        manualOverrides.forEach { (player, manualOverride) ->
            round.setManualOverrideForPlayer(player.name, manualOverride)
        }
        rounds.add(round)

        checkForWinner()
        sortPlayersByScore()

        if (!isGameOver) {
            currentRound++
        }
    }

    fun getAllRounds(): List<Round> = rounds.toList()

    fun updateRound(
        roundNumber: Int,
        newScores: Map<String, Int>,
        newCardSelections: Map<String, CardSelection?> = emptyMap(),
        newManualOverrides: Map<String, Int?> = emptyMap()
    ) {
        if (roundNumber < 1 || roundNumber > rounds.size) return

        val roundIndex = roundNumber - 1
        val round = rounds[roundIndex]

        newScores.forEach { (playerName, newScore) ->
            round.setScoreForPlayer(playerName, newScore)
        }
        newCardSelections.forEach { (playerName, selection) ->
            round.setCardSelectionForPlayer(playerName, selection)
        }
        newManualOverrides.forEach { (playerName, manualOverride) ->
            round.setManualOverrideForPlayer(playerName, manualOverride)
        }

        recalculateTotals()
        checkForWinner()
        sortPlayersByScore()
    }

    private fun sortPlayersByScore() {
        players.sortByDescending { it.totalScore }
    }

    private fun recalculateTotals() {
        players.forEach { player ->
            player.totalScore = 0
            player.roundScores.clear()
        }

        rounds.forEach { round ->
            players.forEach { player ->
                val score = round.getScoreForPlayer(player.name)
                player.addRoundScore(score)
            }
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
        rounds.clear()
        currentRound = 1
        isGameOver = false
        winner = null
    }
}
