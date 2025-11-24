package com.flip7.scoretracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flip7.scoretracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PlayerSetupAdapter
    private val playerNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = PlayerSetupAdapter(playerNames) { position ->
            removePlayer(position)
        }
        binding.playersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.playersRecyclerView.adapter = adapter
    }

    private fun setupButtons() {
        binding.addPlayerButton.setOnClickListener {
            addPlayer()
        }

        binding.startGameButton.setOnClickListener {
            startGame()
        }
    }

    private fun addPlayer() {
        val name = binding.playerNameInput.text?.toString()?.trim()

        if (name.isNullOrEmpty()) {
            Toast.makeText(this, R.string.error_player_name_empty, Toast.LENGTH_SHORT).show()
            return
        }

        adapter.addPlayer(name)
        binding.playerNameInput.text?.clear()

        updateStartButtonState()
    }

    private fun removePlayer(position: Int) {
        adapter.removePlayer(position)
        updateStartButtonState()
    }

    private fun updateStartButtonState() {
        binding.startGameButton.isEnabled = playerNames.size >= 2
    }

    private fun startGame() {
        if (playerNames.size < 2) {
            Toast.makeText(this, R.string.error_min_players, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, GameActivity::class.java)
        intent.putStringArrayListExtra("player_names", ArrayList(playerNames))
        startActivity(intent)
    }
}
