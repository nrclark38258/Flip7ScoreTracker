package com.flip7.scoretracker

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameScoreAdapter(
    private val players: List<Player>
) : RecyclerView.Adapter<GameScoreAdapter.GameScoreViewHolder>() {

    private val roundScores = mutableMapOf<Player, Int>()

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

        holder.roundScoreInput.setText("")
        holder.roundScoreInput.removeTextChangedListener(holder.roundScoreInput.tag as? TextWatcher)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val score = s?.toString()?.toIntOrNull() ?: 0
                roundScores[player] = score
            }
        }

        holder.roundScoreInput.addTextChangedListener(textWatcher)
        holder.roundScoreInput.tag = textWatcher
    }

    override fun getItemCount(): Int = players.size

    fun getRoundScores(): Map<Player, Int> = roundScores.toMap()

    fun clearRoundScores() {
        roundScores.clear()
        notifyDataSetChanged()
    }

    fun updateScores() {
        notifyDataSetChanged()
    }
}
