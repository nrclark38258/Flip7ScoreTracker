package com.flip7.scoretracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class EditableScore(
    val playerName: String,
    var score: Int,
    var cardSelection: CardSelection? = null,
    var manualOverride: Int? = null
)

class EditScoreAdapter(
    private val scores: MutableList<EditableScore>,
    private val onScoreFieldClick: (String, CardSelection?, Int?) -> Unit
) : RecyclerView.Adapter<EditScoreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameText: TextView = itemView.findViewById(R.id.playerNameText)
        val scoreEditText: EditText = itemView.findViewById(R.id.scoreEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edit_score, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val editableScore = scores[position]
        holder.playerNameText.text = editableScore.playerName
        holder.scoreEditText.setText(editableScore.score.toString())

        holder.scoreEditText.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val current = scores[currentPosition]
                onScoreFieldClick(current.playerName, current.cardSelection, current.manualOverride)
            }
        }
    }

    override fun getItemCount(): Int = scores.size

    fun setScoreForPlayer(playerName: String, total: Int, selection: CardSelection?, manualOverride: Int?) {
        val index = scores.indexOfFirst { it.playerName == playerName }
        if (index == -1) return
        scores[index].score = total
        scores[index].cardSelection = selection
        scores[index].manualOverride = manualOverride
        notifyItemChanged(index)
    }

    fun getUpdatedScores(): Map<String, Int> {
        return scores.associate { it.playerName to it.score }
    }

    fun getUpdatedCardSelections(): Map<String, CardSelection?> {
        return scores.associate { it.playerName to it.cardSelection }
    }

    fun getUpdatedManualOverrides(): Map<String, Int?> {
        return scores.associate { it.playerName to it.manualOverride }
    }
}
