package org.superbiz.moviefun.albums;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AlbumRestController {

    private final AlbumRepository albumRepository;

    public AlbumRestController(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @PostMapping
    public void addAlbum(Album album) {
        albumRepository.addAlbum(album);
    }

    @GetMapping
    public List<Album> getAlbums() {
        return albumRepository.getAlbums();
    }

    @GetMapping("/{albumId}")
    public Album find(@PathVariable Long albumId){
        return albumRepository.find(albumId);
    }
}
