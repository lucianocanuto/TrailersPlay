package com.btcodans.trailersplay.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.btcodans.trailersplay.R
import com.btcodans.trailersplay.data.repository.MovieRepository
import com.btcodans.trailersplay.databinding.ActivityNewPlayerTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch

class NewPlayerTrailerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewPlayerTrailerBinding.inflate(layoutInflater)
    }
    private val repository = MovieRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val movieId = intent.getIntExtra("MOVIE_ID",0)
        Log.d("movieId","$movieId")

        lifecycle.addObserver(binding.youtubePlayerView)

        lifecycleScope.launch {
            try {
            val response = repository.getMovieTrailers(movieId)
                val youtubekey = response.results.firstOrNull{
                    it.site == "YouTube" && it.type =="Trailer"
                }?.key
                if (youtubekey != null){
                    binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(youtubekey, 0f)
                        }
                    })

                } else {
                    Log.d("Trailer", "Nenhum trailer encontardo")
                }
            } catch (e: Exception){
                Log.d("Trailer", "Erro ao buscar trailer: ${e.message}")
            }
        }


        }

    }



