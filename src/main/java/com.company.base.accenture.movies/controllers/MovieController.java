package controllers;

import com.company.base.accenture.movies.businessLayer.UserServiceImpl;
import com.company.base.accenture.movies.dataAccessLayer.UserAccessServiceImpl;
import com.company.base.accenture.movies.interfaces.IContainMovies;
import com.company.base.accenture.movies.objModelClass.Movie;
import com.company.base.accenture.movies.objModelClass.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.List;

@Repository
@Component
@RestController
public class MovieController {

    @Autowired
    UserServiceImpl containUsers;

    @Autowired
    IContainMovies usi;

    @POST
    @RequestMapping("/movie/view/{fromYear}/{toYear}/{fromRating}/{toRating}")
    public List<Movie> searchFilm(@QueryParam("search") String search,
                                  @PathVariable(required = false) String fromYear,
                                  @PathVariable(required = false) String toYear,
                                  @PathVariable(required = false) String fromRating,
                                  @PathVariable(required = false) String toRating
    ) {
        return usi.searchFilm(search,fromYear,toYear,fromRating,toRating);
    }

    @POST
    @RequestMapping("/movie/view")
    public List<String> searchFilm(@QueryParam("search") String search) {
        return usi.searchFilm(search);
    }

    @GET
    @RequestMapping("/movie/{id}")
    public List<String> getFilmInfo(@PathVariable("id") String search) {
        return usi.getFilmInfo(search);
    }

    @POST
    @RequestMapping("/movie/{id}/review")
    public Review addReview(@PathVariable("id") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating) {
        return usi.addReview(imdb,review,rating);
    }

    @PUT
    @RequestMapping("/review")
    public String editReview(@QueryParam("imdb") String imdb, @QueryParam("review") String review, @QueryParam("rating") String rating, @QueryParam("login") String login) {
        return usi.editReview(imdb,review,rating,login);
    }

    @DELETE
    @RequestMapping("/review/{id}")
    public String deleteReview(@PathVariable("id") String imdb, @QueryParam("login") String login) {
        return usi.deleteReview(imdb,login);
    }

    @POST
    @RequestMapping("/review/view")
    public List<Review> getMyReviews(){
        return usi.getMyReviews();
    }
}