package com.flip7.scoretracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flip7.scoretracker.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: GameScoreAdapter
    private lateinit var gameState: GameState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeGame()
        setupRecyclerView()
        setupButtons()
        updateUI()
    }

    private fun initializeGame() {
        val playerNames = intent.getStringArrayListExtra("player_names") ?: arrayListOf()
        val players = playerNames.map { Player(it) }.toMutableList()
        gameState = GameState(players)
    }

    private fun setupRecyclerView() {
        adapter = GameScoreAdapter(gameState.players)
        binding.gameScoresRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.gameScoresRecyclerView.adapter = adapter
    }

    private fun setupButtons() {
        binding.submitRoundButton.setOnClickListener {
            submitRound()
        }

        binding.newGameButton.setOnClickListener {
            showNewGameDialog()
        }
    }

    private fun submitRound() {
        val roundScores = adapter.getRoundScores()

        if (roundScores.size != gameState.players.size) {
            Toast.makeText(this, R.string.error_invalid_score, Toast.LENGTH_SHORT).show()
            return
        }

        gameState.addRoundScores(roundScores)
        adapter.clearRoundScores()
        adapter.updateScores()
        updateUI()

        if (gameState.isGameOver) {
            showWinnerDialog()
        }
    }

    private fun updateUI() {
        binding.roundNumberText.text = getString(R.string.round_label, gameState.currentRound)
        binding.submitRoundButton.isEnabled = !gameState.isGameOver
    }

    private fun showWinnerDialog() {
        val winner = gameState.winner ?: return

        AlertDialog.Builder(this)
            .setTitle("Game Over!")
            .setMessage(getString(R.string.winner_message, winner.name, winner.totalScore))
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showNewGameDialog() {
        AlertDialog.Builder(this)
            .setTitle("New Game")
            .setMessage("Start a new game with different players?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("Reset Current Game") { _, _ ->
                resetGame()
            }
            .setNeutralButton("Cancel", null)
            .show()
    }

    private fun resetGame() {
        gameState.reset()
        adapter.clearRoundScores()
        adapter.updateScores()
        updateUI()
    }

    override fun onBackPressed() {
        showNewGameDialog()
    }
}
