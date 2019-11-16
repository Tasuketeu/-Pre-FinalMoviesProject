package com.company.base.accenture.movies.Interfaces;


import com.company.base.accenture.movies.ObjModelClass.Review;

import java.util.List;

public interface IContainMovies {

    public List<String> searchFilm(String search, String fromYear,
                                   String toYear,
                                   String fromRating,
                                   String toRating);

    public List<String> searchFilm(String search);
    public void getFilmInfo(String search);
    public Review addReview(String imdb, String review, String rating);
    public String editReview(String imdb, String review, String rating, String login);
    public String deleteReview(String imdb, String login);
    public List<Review> getMyReviews();
}
