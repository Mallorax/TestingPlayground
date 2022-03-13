package pl.patrykzygo.videospace.repository.genre_paging

import androidx.paging.PagingSource
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.data.network.MovieResponse
import pl.patrykzygo.videospace.others.SortOptions

//abstraction that is meant to make testing easier
abstract class GenrePagingSource(): PagingSource<Int, MovieResponse>() {

    abstract fun setParameters(request: DiscoverMovieRequest, sortingOption: String = SortOptions.POPULARITY_DESC)
}