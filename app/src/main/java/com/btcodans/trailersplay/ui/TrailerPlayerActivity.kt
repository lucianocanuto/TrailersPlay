package com.btcodans.trailersplay.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.btcodans.trailersplay.data.repository.MovieRepository
import com.btcodans.trailersplay.databinding.ActivityTrailerPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrailerPlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityTrailerPlayerBinding.inflate(layoutInflater)
    }
    private val repository = MovieRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycle.addObserver(binding.youtubePlayer)

        val movieId = intent.getIntExtra("MOVIE_ID", 0)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = repository.getMovieTrailers(movieId)
                val trailer = response.results.firstOrNull()

                if (trailer != null) {
                    startYoutube(trailer.key)
                } else {
                    binding.textInfo.text = "Nenhum trailer disponível para este filme."
                }

            } catch (_: Exception) {
                binding.textInfo.text = "Erro ao carregar o trailer."
            }
        }
    }

    private fun startYoutube(videoKey: String) {

        // Inicia o vídeo
        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                player.loadVideo(videoKey, 0f)
            }
        })

        // FULLSCREEN correto para versão 13.0.0
        binding.youtubePlayer.addFullscreenListener(object : FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

                binding.fullscreenContainer.visibility = View.VISIBLE
                binding.fullscreenContainer.addView(fullscreenView)
            }

            override fun onExitFullscreen() {

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                binding.fullscreenContainer.removeAllViews()
                binding.fullscreenContainer.visibility = View.GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
