package com.company.base.accenture.movies.Interfaces;

import com.company.base.accenture.movies.ObjModelClass.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long> {
    boolean existsByLoginAndImdb(String login,String imdb);
    List<Review> findByLogin(String login);
    Review findByLoginAndImdb(String login,String imdb);
    void deleteByLoginAndImdb(String login,String imdb);
}
