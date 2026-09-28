package com.flip7.scoretracker

data class Round(
    val roundNumber: Int,
    val scores: MutableMap<String, Int> = mutableMapOf(),
    val cardSelections: MutableMap<String, CardSelection> = mutableMapOf(),
    val manualOverrides: MutableMap<String, Int> = mutableMapOf()
) {
    fun getScoreForPlayer(playerName: String): Int {
        return scores[playerName] ?: 0
    }

    fun setScoreForPlayer(playerName: String, score: Int) {
        scores[playerName] = score
    }

    fun getCardSelectionForPlayer(playerName: String): CardSelection? {
        return cardSelections[playerName]
    }

    fun setCardSelectionForPlayer(playerName: String, selection: CardSelection?) {
        if (selection == null) {
            cardSelections.remove(playerName)
        } else {
            cardSelections[playerName] = selection
        }
    }

    fun getManualOverrideForPlayer(playerName: String): Int? {
        return manualOverrides[playerName]
    }

    fun setManualOverrideForPlayer(playerName: String, manualOverride: Int?) {
        if (manualOverride == null) {
            manualOverrides.remove(playerName)
        } else {
            manualOverrides[playerName] = manualOverride
        }
    }
}
