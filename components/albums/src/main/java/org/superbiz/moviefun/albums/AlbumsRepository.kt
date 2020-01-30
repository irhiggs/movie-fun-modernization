package org.superbiz.moviefun.albums

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.superbiz.moviefun.albums.Album
import javax.persistence.EntityManager

@Repository
class AlbumsRepository(val entityManager: EntityManager) {

    @Transactional
    fun addAlbum(album: Album?) = entityManager.persist(album)

    fun find(id: Long): Album = entityManager.find(Album::class.java, id)

    val albums: List<Album>
        get() {
            val cq = entityManager.criteriaBuilder.createQuery(Album::class.java)
            cq.select(cq.from(Album::class.java))
            return entityManager.createQuery(cq).resultList
        }

    @Transactional
    fun deleteAlbum(album: Album?) = entityManager.remove(album)

    @Transactional
    fun updateAlbum(album: Album): Album = entityManager.merge(album)
}