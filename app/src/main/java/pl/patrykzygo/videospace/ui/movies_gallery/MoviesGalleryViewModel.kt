package pl.patrykzygo.videospace.ui.movies_gallery

import androidx.lifecycle.*
import androidx.paging.*
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMoviesResponseToMovie
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource

class MoviesGalleryViewModel constructor(private val repo: MoviesPagingSource) :
    ViewModel() {

    private var _requestType = MutableLiveData<String>()
    val requestType: LiveData<String> get() = _requestType

    fun setRequestType(requestType: String, id: Int = -1) {
        repo.setMoviesRequestType(requestType, id)
        _requestType.value = requestType
    }

    fun getMovies(): LiveData<PagingData<Movie>> {
        val pager = Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { repo }
        )
            .liveData
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    mapMoviesResponseToMovie(it)
                }
            }
        return pager
    }
}