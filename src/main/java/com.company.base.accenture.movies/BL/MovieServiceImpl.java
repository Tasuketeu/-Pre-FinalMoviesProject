package com.company.base.accenture.movies.BL;

import com.company.base.accenture.movies.Interfaces.IContainMovies;
import com.company.base.accenture.movies.Interfaces.MovieAccessService;
import com.company.base.accenture.movies.Interfaces.ReviewRepository;
import com.company.base.accenture.movies.ObjModelClass.Movie;
import com.company.base.accenture.movies.ObjModelClass.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.base.accenture.movies.BL.UserServiceImpl.activeUser;

@Repository
@Component
@RestController
public class MovieServiceImpl implements Runnable, IContainMovies {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private MovieAccessService icm;

    private static List<Movie> moviesList = new ArrayList<>();
    static String searchResult = "";

    static Pattern pattern;
    static Matcher titleMatcher;
    static Matcher yearMatcher;
    static boolean ended = false;
    static boolean foundByImdb = false;

    private static List<Movie> foundMovies = new ArrayList<>();

    public void getMoviesFromDB() {
        moviesList= icm.getAllMovies();
    }

    @Override
    @POST
    @RequestMapping("/movie/view/{fromYear}/{toYear}/{fromRating}/{toRating}")
    public List<String> searchFilm(@QueryParam("search") String search,
                                   @PathVariable(required = false) String fromYear,
                                   @PathVariable(required = false) String toYear,
                                   @PathVariable(required = false) String fromRating,
                                   @PathVariable(required = false) String toRating
    ) {
        foundMovies.clear();
        List<String> foundMovie = new ArrayList<>();
            for (Movie movie : moviesList) {
                double rating = movie.getRating();
                String year = movie.getYear();
                if((rating>=Double.parseDouble(fromRating) && (rating<=Double.parseDouble(toRating)))
                        &&(Integer.parseInt(year)>=Integer.parseInt(fromYear) && Integer.parseInt(year)<=Integer.parseInt(toYear))
                ){
                    foundMovies.add(movie);
                }
            }
            if(!foundMovies.isEmpty()) {
                for (Movie movie : foundMovies) {
                    foundMovie.add(movie.getNotFullInfo());
                }
            }
        return foundMovie; //film info
    }

    @Override
    @POST
    @RequestMapping("/movie/view")
    public List<String> searchFilm(@QueryParam("search") String search) {

        foundMovies.clear();
        List<String> foundMovie = new ArrayList<>();
        searchResult = search;
        String imdb=null;

        ended=false;

        pattern = Pattern.compile(searchResult.toLowerCase() + ".++"); //{search}.++  greedy matching

        new Thread(this).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        ended = true;

            for (Movie movie : moviesList) {
                String[] movieInfo = movie.getMovieInfo().split(" ");
                imdb = movieInfo[0];
                String title = movieInfo[2].toLowerCase();
                String year = movie.getYear();
                titleMatcher = pattern.matcher(title);
                yearMatcher = pattern.matcher(year);

                if (titleMatcher.matches() || yearMatcher.matches()
                        || searchResult.toLowerCase().equals(title) || searchResult.equals(year)) {
                    foundMovies.add(movie);
                }
                if (searchResult.equals(imdb)) {
                    foundMovies.add(movie);
                    foundByImdb=true;
                }
            }

        if(!searchResult.equals(imdb)) {
            for (Movie movie : foundMovies) {
                foundMovie.add(movie.getNotFullInfo());//film info
            }
            return foundMovie;
        }
        foundMovies.clear();
        foundByImdb=false;
        return foundMovie;
    }

    @Override
    @GET
    @RequestMapping("/movie/{id}")
    public void getFilmInfo(@PathVariable("id") String search) {
        searchFilm(search);
        if(foundByImdb){
            for (Movie movie : foundMovies) {

                String[] movieInfo = movie.getMovieInfo().split(" ");

                System.out.println(movie.getNotFullInfo()); //film info

                System.out.println("\n");


                if (searchResult.equals(movieInfo[0])) {

                    System.out.println(movie.getFullInfo()); //film full info

                    if (!movie.getReviewsList().isEmpty()) {
                        for (Review review : movie.getReviewsList()) {
                            String[] reviewInfo = review.getReviewInfo().split(" ");
                            System.out.println(reviewInfo[3]); //date

                            System.out.println(reviewInfo[2]); //login
                            System.out.println(reviewInfo[0]); //review

                            System.out.println(reviewInfo[1]); //rating

                            System.out.println("\n");
                        }
                    }
                }
            }
        }
        else {
            System.out.println("Фильм не найден!");
            foundMovies.clear();
        }
    }

    @Override
    @POST
    @RequestMapping("/movie/{id}/review")
    @Transactional
    public Review addReview(@PathVariable("id") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating) {
        Review rev = null;
        if (activeUser!=null&&!UserServiceImpl.adminMode) {
            LocalDate date = LocalDate.now();
            boolean exists = reviewRepo.existsByLoginAndImdb(activeUser, imdb);
            if (!UserServiceImpl.adminMode) {
                searchFilm(imdb);
                if (foundByImdb) {
                    if (!exists) {
                        rev = reviewRepo.save(new Review(review, rating, activeUser, date, imdb));
                    }
                }
            }
        }
        return rev;
    }

    @Override
    @PUT
    @RequestMapping("/review")
    @Transactional
    public String editReview(@QueryParam("imdb") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating, @QueryParam("login") String login) {
        if (activeUser!=null) {
            LocalDate date = LocalDate.now();

            if (!UserServiceImpl.adminMode) {
                login = activeUser;
            }

            Review oldRev = reviewRepo.findByLoginAndImdb(login, imdb);
            if (oldRev != null) {
                oldRev.setReview(review);
                oldRev.setRating(rating);
                oldRev.setDate(date);
                reviewRepo.save(oldRev);
            } else {
                return "Обзор на фильм отсутствует!";
            }
            return String.format("Обзор пользователя %s на фильм с imdb %s изменён!", login, imdb);
        }
        return "Вы не в системе и не можете изменить обзор!";
    }

    @Override
    @DELETE
    @RequestMapping("/review/{id}")
    @Transactional
    public String deleteReview(@PathVariable("id") String imdb, @QueryParam("login") String login) {
        if (activeUser!=null) {
            if (!UserServiceImpl.adminMode) {
                login = activeUser;
            }
            searchFilm(imdb);
            if (!foundByImdb) {
                return "Некорректный imdb!";
            }
            reviewRepo.deleteByLoginAndImdb(login, imdb);
            return String.format("Обзор пользователя %s на фильм с imdb %s удалён!", login, imdb);
        }
        return "Вы не в системе и не можете удалить обзор!";
    }

    @Override
    @POST
    @RequestMapping("/review/view")
    @Transactional
    public List<Review> getMyReviews(){
        List<Review> reviews=new ArrayList<>();
        reviewRepo.findByLogin(activeUser).forEach(reviews::add);
        return reviews;
    }

    @Override
    public void run() {
        String temp = "";
        while (!ended) {
            temp += ".";
            if (temp.length() == 6) {
                temp = ".";
            }
            System.out.println("Пожалуйста, подождите, выполняется поиск" + temp);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

