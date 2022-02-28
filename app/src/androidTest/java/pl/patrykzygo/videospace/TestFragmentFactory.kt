package pl.patrykzygo.videospace

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import pl.patrykzygo.videospace.ui.DefaultListsFragment
import pl.patrykzygo.videospace.ui.MainViewModelFactory
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryFragment
import pl.patrykzygo.videospace.util.provideMovieWithIdUi
import javax.inject.Inject

class TestFragmentFactory @Inject constructor(
    val navController: TestNavHostController,
    val viewModelFactory: MainViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DefaultListsFragment::class.java.name -> DefaultListsFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment(viewModelFactory).also { fragment ->
                fragment.movie = provideMovieWithIdUi(1)
                fragment.viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MoviesGalleryFragment::class.java.name -> MoviesGalleryFragment().also { fragment ->
                fragment.viewModelFactory = viewModelFactory
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}