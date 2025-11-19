package com.btcodans.trailersplay.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.btcodans.trailersplay.databinding.ActivityMovieDetailsBinding
import com.bumptech.glide.Glide

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receber dados da MainActivity
        val movieId = intent.getIntExtra("MOVIE_ID", 0)
        val title = intent.getStringExtra("TITLE") ?: ""
        val poster = intent.getStringExtra("POSTER") ?: ""
        val overview = intent.getStringExtra("OVERVIEW") ?: ""
        val vote = intent.getDoubleExtra("VOTE_AVERAGE", 0.0)
        val releaseDate = intent.getStringExtra("RELEASE_DATE") ?: ""
        val releaseDateFormatter = if (releaseDate.isNotEmpty()){
            try {
                val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val date = parser.parse(releaseDate)
                formatter.format(date!!)
            } catch (e: Exception) {
                releaseDate
        }
        }else{
            "N/A"
        }

        // Preencher views
        binding.textTitle.text = title
        binding.textOverview.text = overview
        Log.d("MovieDetails", "Vote Average: $vote")
        binding.textRating.text = String.format("%.1f", vote)

        binding.textReleaseDate.text = "Lançamento: $releaseDateFormatter"



        // Carregar poster com Glide
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$poster")
            .into(binding.imagePoster)

        // Botão trailer
        binding.buttonTrailer.setOnClickListener {
            val intent = Intent(this, NewPlayerTrailerActivity::class.java)
            intent.putExtra("MOVIE_ID", movieId)
            startActivity(intent)
        }
    }
}
