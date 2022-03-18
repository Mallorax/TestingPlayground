package pl.patrykzygo.videospace.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import pl.patrykzygo.videospace.data.local.GenreDao
import pl.patrykzygo.videospace.data.local.MoviesDao
import pl.patrykzygo.videospace.networking.DiscoverEntryPoint
import pl.patrykzygo.videospace.networking.GenresEntryPoint
import pl.patrykzygo.videospace.networking.MoviesEntryPoint
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSource
import pl.patrykzygo.videospace.repository.genre_paging.GenrePagingSourceImpl
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepository
import pl.patrykzygo.videospace.repository.local_store.LocalStoreRepositoryImpl
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSource
import pl.patrykzygo.videospace.repository.movies_paging.MoviesPagingSourceImpl
import pl.patrykzygo.videospace.ui.dispatchers.DispatchersProvider
import pl.patrykzygo.videospace.ui.dispatchers.StandardDispatchers

//Provides all dependencies needed for view models

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideDispatchersProvider(): DispatchersProvider {
        return StandardDispatchers()
    }

    @Provides
    fun provideLocalStoreRepository(
        moviesDao: MoviesDao,
        genreDao: GenreDao,
        moviesEntryPoint: MoviesEntryPoint,
        genresEntryPoint: GenresEntryPoint
    ): LocalStoreRepository {
        return LocalStoreRepositoryImpl(moviesDao, genreDao, moviesEntryPoint, genresEntryPoint)
    }


    @Provides
    fun provideMoviesPagingSource(moviesEntryPoint: MoviesEntryPoint): MoviesPagingSource {
        return MoviesPagingSourceImpl(moviesEntryPoint)
    }

    @Provides
    fun provideGenrePagingSource(discoverEntryPoint: DiscoverEntryPoint): GenrePagingSource {
        return GenrePagingSourceImpl(discoverEntryPoint)
    }
}