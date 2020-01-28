package org.superbiz.moviefun.moviesapi;

import org.springframework.web.client.RestOperations;

import java.util.List;

public class MoviesClient {

    private final RestOperations restOperations;
    private final String moviesUrl;

    public MoviesClient(String moviesUrl, RestOperations restOperations) {
        this.moviesUrl = moviesUrl;
        this.restOperations = restOperations;
    }

    public void addMovie(MovieInfo movieInfo) {
        restOperations.postForEntity(moviesUrl, movieInfo, MovieInfo.class);
    }

    public void deleteMovieId(Long movieId) {
        restOperations.delete(moviesUrl + "/" + movieId);
    }

    public int countAll() {
        return restOperations.getForObject(moviesUrl + "/count", Integer.class);
    }

    public int count(String field, String key) {
        return 0;
    }

    public List<MovieInfo> findAll(int start, int pageSize) {
        return null;
    }

    public List<MovieInfo> findRange(String field, String key, int start, int pageSize) {
        return null;
    }

    public Object getMovies() {
        return null;
    }
}
