package org.superbiz.moviefun.albums

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Album(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
                 var artist: String?,
                 var title: String?,
                 var year: Int = 0,
                 var rating: Int = 0) : Serializable {

    constructor(artist: String, title: String, year: Int, rating: Int) : this(null, artist, title, year, rating)

    fun hasId(): Boolean {
        return id != null
    }

    fun isEquivalent(other: Album): Boolean {
        if (year != other.year) return false
        if (!isEqual<String?>(title, other.title)) return false
        return isEqual<String?>(artist, other.artist)
    }

    companion object {
        const val serialVersionUID = 1L

        private fun <T> isEqual(one: T?, other: T?): Boolean {
            return !if (one != null) one != other else other != null
        }
    }
}