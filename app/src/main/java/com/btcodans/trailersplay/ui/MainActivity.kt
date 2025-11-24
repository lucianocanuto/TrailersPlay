package com.btcodans.trailersplay.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.btcodans.trailersplay.data.model.Movie
import com.btcodans.trailersplay.data.repository.MovieRepository
import com.btcodans.trailersplay.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val repository = MovieRepository()
    private lateinit var adapter: MovieAdapter
    private var fullList: List<Movie> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        searchBar()

        /*val searchEditText = binding.searchView.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(
            androidx.appcompat.R.id.search_src_text
        )

        searchEditText.setTextColor(android.graphics.Color.WHITE)
        searchEditText.setHintTextColor(0x99FFFFFF.toInt()) // dica mais visível
        searchEditText.textSize = 18f // maior para dar destaque*/

        setupRecyclerView()
        loadMovies()

        // Listener da barra de busca
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filtered = fullList.filter { movie ->
                        movie.title.contains(newText.orEmpty(), ignoreCase = true)
                    }
                    adapter.filterList(filtered)
                    return true
                }
            }
        )

    }


    @SuppressLint("RestrictedApi")
    private fun searchBar() {
        val searchText = binding.searchView.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )

        searchText.setTextColor(Color.WHITE)
        searchText.setHintTextColor(0x55FFFFFF)
        searchText.textSize = 16f

// ícone branco
        val searchIcon = binding.searchView.findViewById<ImageView>(
            androidx.appcompat.R.id.search_mag_icon
        )
        searchIcon.setColorFilter(Color.WHITE)

    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList())
        binding.recyclerMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovies.adapter = adapter
    }
    private fun loadMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getPopularMovies()
                fullList = response.results

                // Volta para a main thread para atualizar o RecyclerView
                withContext(Dispatchers.Main) {
                    adapter.updateList(fullList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*private fun loadMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = repository.getPopularMovies()
                fullList = response.results
                adapter.updateList(fullList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
}
