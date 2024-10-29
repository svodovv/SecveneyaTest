package com.example.secveneyatest.presentation.ui.film.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.secveneyatest.R
import com.example.secveneyatest.databinding.FragmentFilmListBinding
import com.example.secveneyatest.presentation.ui.film.list.recycler.FilmItem
import com.example.secveneyatest.presentation.ui.film.list.recycler.FilmRecyclerViewAdapter
import com.example.secveneyatest.utils.Constants
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val FILM_HEADER = "Фильмы"
private const val CATEGORY_HEADER = "Жанры"

class FilmListFragment : Fragment(), FilmRecyclerViewAdapter.Navigate {

    private val viewModel by viewModel<FilmListViewModel>()

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmListBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            viewModel.state.collect { result ->

                when {
                    result.loading -> loadingHandler()
                    result.error -> errorMassageHandler()
                    else -> filmsHandler(result)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun errorMassageHandler() {
        binding.pbProgressBar.isVisible = false
        binding.llErrorBottomBar.isVisible = true
        binding.rvFilms.isVisible = false

        binding.tvReloadFilms.setOnClickListener {
            viewModel.loadFilms()
        }
    }

    private fun loadingHandler() {
        binding.pbProgressBar.isVisible = true
        binding.llErrorBottomBar.isVisible = false
        binding.rvFilms.isVisible = false
    }

    private fun filmsHandler(result: FilmListState) {

        val filmItem = result.filmList.map { FilmItem.Film(it) }
        val categoryItem = result.categoryList.map { FilmItem.Category(it.category) }
        val categoryHeader = listOf(FilmItem.Header(CATEGORY_HEADER))
        val filmHeader = listOf(FilmItem.Header(FILM_HEADER))

        val items = categoryHeader + categoryItem + filmHeader + filmItem


        val recyclerView = binding.rvFilms
        val layoutManager = GridLayoutManager(this.context, 4)
        val adapter = FilmRecyclerViewAdapter(
            items = items,
            viewModel = viewModel,
            this
        )

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        binding.pbProgressBar.isVisible = false
        binding.llErrorBottomBar.isVisible = false
        binding.rvFilms.isVisible = true

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return when (items[position]) {
                    is FilmItem.Header -> 4
                    is FilmItem.Category -> 4
                    is FilmItem.Film -> if (isLandscape) 1 else 2
                }
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun to(filmId: Int) {
        val bundle = Bundle().apply {
            putInt(Constants.FILM_ID_KEY, filmId)
        }
        findNavController().navigate(R.id.filmDetailFragment, bundle)
    }

}