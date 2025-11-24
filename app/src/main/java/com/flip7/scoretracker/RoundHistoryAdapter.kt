package com.flip7.scoretracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RoundHistoryAdapter(
    private val rounds: List<Round>,
    private val players: List<Player>,
    private val onEditRound: (Int) -> Unit
) : RecyclerView.Adapter<RoundHistoryAdapter.RoundViewHolder>() {

    class RoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundNumberText: TextView = itemView.findViewById(R.id.roundNumberText)
        val roundScoresRecyclerView: RecyclerView = itemView.findViewById(R.id.roundScoresRecyclerView)
        val editRoundButton: ImageButton = itemView.findViewById(R.id.editRoundButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_round_history, parent, false)
        return RoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        val round = rounds[position]
        holder.roundNumberText.text = "Round ${round.roundNumber}"

        val playerScores = mutableListOf<PlayerRoundScore>()
        var runningTotal = 0

        players.forEach { player ->
            val scoreForRound = round.getScoreForPlayer(player.name)

            val totalUpToThisRound = rounds.take(position + 1).sumOf { r ->
                r.getScoreForPlayer(player.name)
            }

            playerScores.add(PlayerRoundScore(
                playerName = player.name,
                score = scoreForRound,
                runningTotal = totalUpToThisRound
            ))
        }

        val adapter = PlayerRoundScoreAdapter(playerScores)
        holder.roundScoresRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.roundScoresRecyclerView.adapter = adapter

        holder.editRoundButton.setOnClickListener {
            onEditRound(round.roundNumber)
        }
    }

    override fun getItemCount(): Int = rounds.size
}
