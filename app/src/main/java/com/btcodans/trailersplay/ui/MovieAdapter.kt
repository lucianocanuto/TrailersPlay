package com.btcodans.trailersplay.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btcodans.trailersplay.data.model.Movie
import com.btcodans.trailersplay.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    fun updateList(newList: List<Movie>) {
        movies = newList
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        val context = holder.itemView.context

        holder.binding.textTitle.text = movie.title
        holder.binding.textRating.text = String.format("%.1f", movie.vote_average)
        holder.binding.textReleaseDate.text = "Lançamento: ${movie.release_date ?: "N/A"}"



        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(holder.binding.imagePoster)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            intent.putExtra("TITLE", movie.title)
            intent.putExtra("POSTER", movie.poster_path)
            intent.putExtra("OVERVIEW", movie.overview)
            context.startActivity(intent)
        }
    }
}
