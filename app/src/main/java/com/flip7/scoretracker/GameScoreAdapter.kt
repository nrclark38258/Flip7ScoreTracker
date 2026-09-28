package com.flip7.scoretracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameScoreAdapter(
    private val players: List<Player>,
    private val onScoreFieldClick: (Player, CardSelection?, Int?) -> Unit
) : RecyclerView.Adapter<GameScoreAdapter.GameScoreViewHolder>() {

    private val roundScores = mutableMapOf<Player, Int>()
    private val cardSelections = mutableMapOf<Player, CardSelection>()
    private val manualOverrides = mutableMapOf<Player, Int>()

    class GameScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameText: TextView = itemView.findViewById(R.id.playerNameText)
        val roundScoreInput: EditText = itemView.findViewById(R.id.roundScoreInput)
        val totalScoreText: TextView = itemView.findViewById(R.id.totalScoreText)
        val pointsToWinText: TextView = itemView.findViewById(R.id.pointsToWinText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_score, parent, false)
        return GameScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameScoreViewHolder, position: Int) {
        val player = players[position]
        holder.playerNameText.text = player.name
        holder.totalScoreText.text = player.totalScore.toString()
        holder.pointsToWinText.text = player.getPointsToWin().toString()

        val total = roundScores[player]
        holder.roundScoreInput.setText(total?.toString() ?: "")

        holder.roundScoreInput.setOnClickListener {
            onScoreFieldClick(player, cardSelections[player], manualOverrides[player])
        }
    }

    override fun getItemCount(): Int = players.size

    fun setScoreForPlayer(player: Player, total: Int, selection: CardSelection?, manualOverride: Int?) {
        roundScores[player] = total
        if (selection == null) {
            cardSelections.remove(player)
        } else {
            cardSelections[player] = selection
        }
        if (manualOverride == null) {
            manualOverrides.remove(player)
        } else {
            manualOverrides[player] = manualOverride
        }
        notifyItemChanged(players.indexOf(player))
    }

    fun getRoundScores(): Map<Player, Int> = roundScores.toMap()

    fun getCardSelections(): Map<Player, CardSelection> = cardSelections.toMap()

    fun getManualOverrides(): Map<Player, Int> = manualOverrides.toMap()

    fun clearRoundScores() {
        roundScores.clear()
        cardSelections.clear()
        manualOverrides.clear()
        notifyDataSetChanged()
    }

    fun updateScores() {
        notifyDataSetChanged()
    }
}
