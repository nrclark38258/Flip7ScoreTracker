package com.flip7.scoretracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PlayerRoundScore(
    val playerName: String,
    val score: Int,
    val runningTotal: Int
)

class PlayerRoundScoreAdapter(
    private val scores: List<PlayerRoundScore>
) : RecyclerView.Adapter<PlayerRoundScoreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameText: TextView = itemView.findViewById(R.id.playerNameText)
        val playerScoreText: TextView = itemView.findViewById(R.id.playerScoreText)
        val playerRunningTotalText: TextView = itemView.findViewById(R.id.playerRunningTotalText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_round_score, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = scores[position]
        holder.playerNameText.text = score.playerName
        holder.playerScoreText.text = score.score.toString()
        holder.playerRunningTotalText.text = score.runningTotal.toString()
    }

    override fun getItemCount(): Int = scores.size
}
