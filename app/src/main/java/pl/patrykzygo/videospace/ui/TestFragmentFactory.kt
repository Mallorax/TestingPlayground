package pl.patrykzygo.videospace.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsFragment

class TestFragmentFactory(val navController: NavController): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            DefaultListsFragment::class.java.name -> DefaultListsFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever{
                    if (it != null){
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever{
                    if (it != null){
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}