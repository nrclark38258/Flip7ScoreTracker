package com.flip7.scoretracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerSetupAdapter(
    private val players: MutableList<String>,
    private val onRemovePlayer: (Int) -> Unit
) : RecyclerView.Adapter<PlayerSetupAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameText: TextView = itemView.findViewById(R.id.playerNameText)
        val removeButton: ImageButton = itemView.findViewById(R.id.removePlayerButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_setup, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val playerName = players[position]
        holder.playerNameText.text = playerName
        holder.removeButton.setOnClickListener {
            onRemovePlayer(position)
        }
    }

    override fun getItemCount(): Int = players.size

    fun addPlayer(name: String) {
        players.add(name)
        notifyItemInserted(players.size - 1)
    }

    fun removePlayer(position: Int) {
        players.removeAt(position)
        notifyItemRemoved(position)
    }
}
