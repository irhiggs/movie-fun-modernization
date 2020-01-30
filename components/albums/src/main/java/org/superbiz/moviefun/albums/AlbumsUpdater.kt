package org.superbiz.moviefun.albums

import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.superbiz.moviefun.CsvUtils
import org.superbiz.moviefun.albums.Album
import org.superbiz.moviefun.blobstore.BlobStore
import java.io.IOException

@Service
class AlbumsUpdater(private val blobStore: BlobStore, private val albumsRepository: AlbumsRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val objectReader: ObjectReader
    @Throws(IOException::class)
    fun update() {
        val maybeBlob = blobStore["albums.csv"]
        if (!maybeBlob.isPresent) {
            logger.info("No albums.csv found when running AlbumsUpdater!")
            return
        }
        val albumsToHave = CsvUtils.readFromCsv<Album>(objectReader, maybeBlob.get().inputStream)
        val albumsWeHave = albumsRepository.albums
        createNewAlbums(albumsToHave, albumsWeHave)
        deleteOldAlbums(albumsToHave, albumsWeHave)
        updateExistingAlbums(albumsToHave, albumsWeHave)
    }

    private fun createNewAlbums(albumsToHave: List<Album>, albumsWeHave: List<Album>) {
        val albumsToCreate = albumsToHave
                .stream()
                .filter { album: Album -> albumsWeHave.stream().noneMatch { other: Album? -> album.isEquivalent(other!!) } }
        albumsToCreate.forEach { album: Album? -> albumsRepository.addAlbum(album) }
    }

    private fun deleteOldAlbums(albumsToHave: List<Album>, albumsWeHave: List<Album>) {
        val albumsToDelete = albumsWeHave
                .stream()
                .filter { album: Album -> albumsToHave.stream().noneMatch { other: Album? -> album.isEquivalent(other!!) } }
        albumsToDelete.forEach { album: Album? -> albumsRepository.deleteAlbum(album) }
    }

    private fun updateExistingAlbums(albumsToHave: List<Album>, albumsWeHave: List<Album>) {
        val albumsToUpdate = albumsToHave
                .stream()
                .map { album: Album -> addIdToAlbumIfExists(albumsWeHave, album) }
                .filter { obj: Album -> obj.hasId() }
        albumsToUpdate.forEach { album: Album? -> albumsRepository.updateAlbum(album!!) }
    }

    private fun addIdToAlbumIfExists(existingAlbums: List<Album>, album: Album): Album {
        val maybeExisting = existingAlbums.stream().filter { other: Album? -> album.isEquivalent(other!!) }.findFirst()
        maybeExisting.ifPresent { (id) -> album.id = id }
        return album
    }

    init {
        val schema = CsvSchema.builder()
                .addColumn("artist")
                .addColumn("title")
                .addColumn("year", CsvSchema.ColumnType.NUMBER)
                .addColumn("rating", CsvSchema.ColumnType.NUMBER)
                .build()
        objectReader = CsvMapper().readerFor(Album::class.java).with(schema)
    }
}