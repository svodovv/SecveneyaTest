package com.example.secveneyatest.presentation.ui.film.list.recycler

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.secveneyatest.R
import com.example.secveneyatest.databinding.CategoryItemBinding
import com.example.secveneyatest.databinding.FilmHeaderBinding
import com.example.secveneyatest.databinding.FilmItemBinding
import com.example.secveneyatest.presentation.ui.film.list.FilmListViewModel


class FilmRecyclerViewAdapter(
    private val items: List<FilmItem>,
    private val viewModel: FilmListViewModel,
    private val navigate: Navigate
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_FILM = 1
        private const val VIEW_TYPE_HEADER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is FilmItem.Category -> VIEW_TYPE_CATEGORY
            is FilmItem.Film -> VIEW_TYPE_FILM
            is FilmItem.Header -> VIEW_TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_FILM -> {
                val binding = FilmItemBinding.inflate(inflater, parent, false)
                FilmViewHolder(binding)
            }

            VIEW_TYPE_CATEGORY -> {
                val binding = CategoryItemBinding.inflate(inflater, parent, false)
                CategoryViewHolder(binding)
            }

            VIEW_TYPE_HEADER -> {
                val binding = FilmHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view argument")
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(items[position] as FilmItem.Category)
            is FilmViewHolder -> holder.bind(items[position] as FilmItem.Film)
            is HeaderViewHolder -> holder.bind(items[position] as FilmItem.Header)
        }
    }

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: FilmItem.Category) {
            val selectedCategory = viewModel.state.value.selectedCategory

            with(binding){
                tvCategory.text = category.name

                if (selectedCategory == category.name)
                    llCategoryName.background = ContextCompat.getDrawable(itemView.context, R.color.peach)

                llCategoryName.setOnClickListener{
                    viewModel.getFilmsByCategories(category.name)
                }
            }
        }
    }

    inner class FilmViewHolder(private val binding: FilmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filmModel: FilmItem.Film) {

            Glide.with(binding.root)
                .load(filmModel.film.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().centerCrop().transform(RoundedCorners(8)))
                .into(
                    object : CustomTarget<Drawable>() {

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            binding.ivFilm.setImageDrawable(resource)
                            binding.ivFilm.visibility = View.VISIBLE
                            binding.flError.visibility = View.GONE
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            binding.ivFilm.visibility = View.GONE
                            binding.flError.visibility = View.VISIBLE
                        }
                    }
                )

            with(binding){
                tvFilmName.text = filmModel.film.name

                llFilm.setOnClickListener {
                    navigate.to(filmModel.film.id)
                }

            }



        }
    }

    class HeaderViewHolder(private val binding: FilmHeaderBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(headerName: FilmItem.Header){
            binding.tvFilmListHeader.text = headerName.name }
    }


    interface Navigate{
        fun to(filmId: Int)
    }

}