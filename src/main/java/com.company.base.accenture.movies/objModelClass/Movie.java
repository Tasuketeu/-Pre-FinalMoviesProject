package com.company.base.accenture.movies.ObjModelClass;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Movie")
@Table(name = "tb_movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "movie")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name="imdb")
    private String imdb;
    @Column(name="filmType")
    private String filmType;
    @Column(name="title")
    private String title;
    @Column(name="genre")
    private String genre;
    @Column(name="date")
    private String date;
    @Column(name="rating")
    private double rating;
    @Column(name="description")
    private String description;
    @Transient
    private String movieInfo;
    @Transient
    private String movieNotFullInfo;
    @Transient
    private String movieFullInfo;

    @Transient
    private String[] dateVars;

//    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @Transient
    private List<Review> reviewsList = new ArrayList<>();


    public Movie(String imdb, String filmType, String title,
                 String genre, String date, double rating, String description) {
        this.imdb = imdb;
        this.filmType = filmType;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.rating = rating;
        this.description = description;
    }

    public Movie() { }

    public Movie(String imdb) {}

    public void addReview(String review, String rating, String login, LocalDate date) {
        reviewsList.add(new Review(review, rating, login, date,imdb));
    }

    public void editReview(int i, String review, String rating, String login, LocalDate date) {
        reviewsList.set(i, new Review(review, rating, login, date,imdb));
    }

    public void deleteReview(int i) {
        reviewsList.remove(i);
    }


    public String getMovieInfo() {
        movieInfo = String.format("%s %s %s %s %s %s %s", this.imdb, this.filmType, this.title, this.genre,
                this.date, this.rating, this.description);
        return movieInfo;
    }
    public double getRating() {
        return this.rating;
    }

    public String getNotFullInfo() {
        movieNotFullInfo = String.format("%s %s %s %s %s", this.filmType, this.title, this.genre,
                this.imdb, this.rating);
        return movieNotFullInfo;
    }

    public String getFullInfo() {
        movieFullInfo = String.format("%s %s", this.date, this.description);
        return movieFullInfo;
    }

    public List<Review> getReviewsList() {
        return this.reviewsList;
    }

    public String getYear() {
        dateVars = this.date.split(" ");
        return this.dateVars[0];
    }
}
