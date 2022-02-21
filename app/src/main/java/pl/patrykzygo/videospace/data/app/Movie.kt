package pl.patrykzygo.videospace.data.app

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val adult: Boolean,
    val backdropPath: String,
    val genres: List<String>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    var isFavourite: Boolean = false,
    var isOnWatchLater: Boolean = false
) : Parcelable {

}