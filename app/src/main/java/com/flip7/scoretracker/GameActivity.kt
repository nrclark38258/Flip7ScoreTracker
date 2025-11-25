package com.flip7.scoretracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flip7.scoretracker.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: GameScoreAdapter
    private lateinit var historyAdapter: RoundHistoryAdapter
    private lateinit var gameState: GameState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeGame()
        setupRecyclerView()
        setupButtons()
        setupBackPressHandler()
        updateUI()
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showNewGameDialog()
            }
        })
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

        historyAdapter = RoundHistoryAdapter(
            rounds = emptyList(),
            players = gameState.players,
            onEditRound = { roundNumber -> showEditRoundDialog(roundNumber) }
        )
        binding.roundHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.roundHistoryRecyclerView.adapter = historyAdapter
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
        updateRoundHistory()
        updateUI()

        if (gameState.isGameOver) {
            showWinnerDialog()
        }
    }

    private fun updateUI() {
        binding.roundNumberText.text = getString(R.string.round_label, gameState.currentRound)
        binding.submitRoundButton.isEnabled = !gameState.isGameOver
    }

    private fun updateRoundHistory() {
        historyAdapter = RoundHistoryAdapter(
            rounds = gameState.getAllRounds(),
            players = gameState.players,
            onEditRound = { roundNumber -> showEditRoundDialog(roundNumber) }
        )
        binding.roundHistoryRecyclerView.adapter = historyAdapter
        if (historyAdapter.itemCount > 0) {
            binding.roundHistoryRecyclerView.scrollToPosition(historyAdapter.itemCount - 1)
        }
    }

    private fun showEditRoundDialog(roundNumber: Int) {
        val round = gameState.getAllRounds().getOrNull(roundNumber - 1) ?: return

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_round, null)
        val dialogTitle = dialogView.findViewById<android.widget.TextView>(R.id.dialogTitle)
        val recyclerView = dialogView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.editScoresRecyclerView)

        dialogTitle.text = "Edit Round $roundNumber"

        val editableScores = gameState.players.map { player ->
            EditableScore(player.name, round.getScoreForPlayer(player.name))
        }.toMutableList()

        val editAdapter = EditScoreAdapter(editableScores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = editAdapter

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedScores = editAdapter.getUpdatedScores()
                gameState.updateRound(roundNumber, updatedScores)
                adapter.updateScores()
                updateRoundHistory()
                updateUI()
                Toast.makeText(this, "Round $roundNumber updated", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
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
        updateRoundHistory()
        updateUI()
    }
}
