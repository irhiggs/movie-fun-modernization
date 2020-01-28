package org.superbiz.moviefun.moviesapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class AlbumsClient {

    private final RestOperations restOperations;
    private final String albumsUrl;

    public AlbumsClient(String albumsUrl, RestOperations restOperations) {
        this.restOperations = restOperations;
        this.albumsUrl = albumsUrl;
    }

    private static ParameterizedTypeReference<List<AlbumInfo>> albumListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public void addAlbum(AlbumInfo albumInfo) {
        restOperations.postForEntity(albumsUrl, albumInfo, AlbumInfo.class);
    }

    public List<AlbumInfo> getAlbums() {
        return restOperations.exchange(albumsUrl, GET, null, albumListType).getBody();
    }

    public AlbumInfo find(long albumId) {
        return restOperations.exchange(albumsUrl + "/" + albumId, GET, null, AlbumInfo.class).getBody();
    }
}
