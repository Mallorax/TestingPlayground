package pl.patrykzygo.videospace.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieModalBottomSheet
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment
import javax.inject.Inject
import javax.inject.Named


class DefaultFragmentFactory
@Inject constructor(
    @Named("main_vm_factory") private val viewModelFactory: MainViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DefaultListsFragment::class.java.name -> DefaultListsFragment()
            MoviesListFragment::class.java.name -> MoviesListFragment(viewModelFactory)
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment()
            MovieModalBottomSheet::class.java.name -> MovieModalBottomSheet()
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment(viewModelFactory)
            else -> super.instantiate(classLoader, className)
        }
    }
}