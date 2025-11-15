package com.btcodans.trailersplay.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.btcodans.trailersplay.data.repository.MovieRepository
import com.btcodans.trailersplay.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val repository = MovieRepository()
    private lateinit var adapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView( binding.root)

        setupRecyclerView()
        loadMovies()
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList())
        binding.recyclerMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovies.adapter = adapter
    }

    private fun loadMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = repository.getPopularMovies()
                adapter.updateList(response.results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
