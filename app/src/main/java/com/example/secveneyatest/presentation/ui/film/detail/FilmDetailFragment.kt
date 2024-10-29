package com.example.secveneyatest.presentation.ui.film.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.secveneyatest.R
import com.example.secveneyatest.databinding.FragmentFilmDetailBinding
import com.example.secveneyatest.databinding.FragmentFilmListBinding
import com.example.secveneyatest.domain.model.FilmDetailModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel



class FilmDetailFragment : Fragment() {

    private var _binding: FragmentFilmDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FilmDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            viewModel.state.collect{ film ->
                    filmHandle(film)
            }
        }

        return view
    }

    private fun filmHandle(film: FilmDetailModel?){
        with(binding){
            ibBack.setOnClickListener {
                findNavController().popBackStack()
            }
            if (film == null) {
                tvFilmNotLoad.visibility = View.VISIBLE
                svFilmDetail.visibility = View.GONE
            }else{
                val filmYear = film.year.toString() + " год"
                val filmCategoryPlusYear =
                    if (film.genres.isEmpty())
                        filmYear
                else
                    film.genres.joinToString(
                        separator = ", ", postfix = ", "
                    ) + filmYear

                tvFilmNotLoad.visibility = View.GONE
                svFilmDetail.visibility = View.VISIBLE


                tvFilmNameToolbar.text = film.name

                tvFilmName.text = film.localizedName
                tvFilmCategory.text = filmCategoryPlusYear

                if (film.rating == null){
                    tvFilmRating.visibility = View.GONE
                }
                else
                    tvFilmRating.text = film.rating.toString()


                tvFilmDescription.text = film.description

                Glide.with(root)
                    .load(film.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions().centerCrop().transform(RoundedCorners(8)))
                    .into(
                        object : CustomTarget<Drawable>() {

                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                ivFilm.setImageDrawable(resource)
                                ivFilm.visibility = View.VISIBLE
                                binding.flError.visibility = View.GONE
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                ivFilm.visibility = View.GONE
                                flError.visibility = View.VISIBLE
                            }
                        }
                    )


            }
        }
    }
}