package pl.patrykzygo.videospace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.patrykzygo.videospace.di.LocalRepoImplQualifier
import pl.patrykzygo.videospace.di.MoviesPagingSourceImplQualifier
import pl.patrykzygo.videospace.repository.LocalStoreRepository
import pl.patrykzygo.videospace.repository.MoviesPagingSource
import pl.patrykzygo.videospace.ui.movie_details.MovieDetailsViewModel
import pl.patrykzygo.videospace.ui.movie_dialogs.MovieBottomSheetViewModel
import pl.patrykzygo.videospace.ui.movies_gallery.MoviesGalleryViewModel
import pl.patrykzygo.videospace.ui.movies_list.MoviesListViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    @LocalRepoImplQualifier val localRepo: LocalStoreRepository,
    @MoviesPagingSourceImplQualifier val moviesPagingSource: MoviesPagingSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return modelClass.getConstructor(LocalStoreRepository::class.java)
                .newInstance(localRepo)
        }
        if (modelClass.isAssignableFrom(MoviesGalleryViewModel::class.java)) {
            return modelClass.getConstructor(MoviesPagingSource::class.java)
                .newInstance(moviesPagingSource)
        }
        if (modelClass.isAssignableFrom(MovieBottomSheetViewModel::class.java)) {
            return modelClass.getConstructor(LocalStoreRepository::class.java)
                .newInstance(localRepo)
        }
        if (modelClass.isAssignableFrom(MoviesListViewModel::class.java))
            return modelClass.getConstructor()
                .newInstance()
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}