package com.flip7.scoretracker

enum class ModifierCard(val label: String, val flatValue: Int) {
    PLUS_2("+2", 2),
    PLUS_4("+4", 4),
    PLUS_6("+6", 6),
    PLUS_8("+8", 8),
    PLUS_10("+10", 10),
    X2("x2", 0)
}

data class CardSelection(
    val numbers: Set<Int> = emptySet(),
    val modifiers: Set<ModifierCard> = emptySet()
) {
    fun computeTotal(): Int {
        var numberSum = numbers.sum()
        if (modifiers.contains(ModifierCard.X2)) {
            numberSum *= 2
        }
        val flatBonus = modifiers.filter { it != ModifierCard.X2 }.sumOf { it.flatValue }
        var total = numberSum + flatBonus
        if (numbers.size == 7) {
            total += 15
        }
        return total
    }
}
