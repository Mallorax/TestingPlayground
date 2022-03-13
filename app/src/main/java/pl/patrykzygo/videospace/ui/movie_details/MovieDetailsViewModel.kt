package pl.patrykzygo.videospace.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.app.Genre
import pl.patrykzygo.videospace.data.app.Movie
import pl.patrykzygo.videospace.data.mapMovieDetailsResponseToMovie
import pl.patrykzygo.videospace.data.mapMovieToMovieEntity
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.RepositoryResponse
import pl.patrykzygo.videospace.ui.DispatchersProvider


class MovieDetailsViewModel constructor(
    private val repo: LocalStoreRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _genres = LiveEvent<List<String>>()
    val genres: LiveData<List<String>> get() = _genres

    private val _saveMovieEvent = LiveEvent<Movie>()
    val saveMovieEvent: LiveData<Movie> get() = _saveMovieEvent

    private val _searchInGenreLiveEvent = LiveEvent<DiscoverMovieRequest>()
    val searchInGenreLiveEvent get() = _searchInGenreLiveEvent

    private val _searchInGenreErrorMessage = MutableLiveData<String>()
    val searchInGenreErrorMessage get() = _searchInGenreErrorMessage


    fun setMovie(movie: Movie?) {
        if (movie != null) {
            viewModelScope.launch(dispatchersProvider.io) {
                val response = repo.getSpecificMovie(movie.id)
                val isFavourite = repo.getSpecificFavourite(movie.id).data?.isFavourite
                if (response.status == RepositoryResponse.Status.SUCCESS) {
                    val mappedMovie = mapMovieDetailsResponseToMovie(response.data!!)
                    mappedMovie.isFavourite = isFavourite ?: false
                    _movie.postValue(mappedMovie)
                    _genres.postValue(mappedMovie.genres)
                } else if (response.status == RepositoryResponse.Status.ERROR) {
                    _errorMessage.postValue(response.message!!)
                }
            }
        }
    }

    fun moveToGenreList(searchedGenre: String){
        viewModelScope.launch(dispatchersProvider.io) {
            val response = repo.getAllGenres()
            if (response.status == RepositoryResponse.Status.SUCCESS){
                val genre = response.data!!.first { t-> t.genreName == searchedGenre }
                val request = DiscoverMovieRequest(includedGenres = genre.genreId.toString())
                _searchInGenreLiveEvent.postValue(request)
            }else{
                _searchInGenreErrorMessage.postValue("Check your internet connection")
            }
        }
    }

    fun saveMovieEvent(){
        _saveMovieEvent.value = _movie.value
    }

    fun toggleFavourite(){
        val movie = _movie.value
        if (movie != null){
            movie.isFavourite = !movie.isFavourite
            _movie.value = movie
            viewModelScope.launch(dispatchersProvider.io){
                repo.insertFavourite(mapMovieToMovieEntity(movie))
            }
        }

    }

}