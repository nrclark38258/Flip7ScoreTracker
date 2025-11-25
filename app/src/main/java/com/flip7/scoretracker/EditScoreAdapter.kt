package com.flip7.scoretracker

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class EditableScore(
    val playerName: String,
    var score: Int
)

class EditScoreAdapter(
    private val scores: MutableList<EditableScore>
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

        holder.scoreEditText.removeTextChangedListener(holder.scoreEditText.tag as? TextWatcher)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val newScore = s?.toString()?.toIntOrNull() ?: 0
                    scores[currentPosition].score = newScore
                }
            }
        }

        holder.scoreEditText.addTextChangedListener(textWatcher)
        holder.scoreEditText.tag = textWatcher
    }

    override fun getItemCount(): Int = scores.size

    fun getUpdatedScores(): Map<String, Int> {
        return scores.associate { it.playerName to it.score }
    }
}
