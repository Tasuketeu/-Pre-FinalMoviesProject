package com.company.base.accenture.movies.DAL;

import Factories.HibernateSessionFactoryUtil;
import com.company.base.accenture.movies.Interfaces.MovieAccessService;
import com.company.base.accenture.movies.ObjModelClass.Movie;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class MovieAccessServiceImpl implements MovieAccessService {
    @Autowired
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Movie> getAllMovies(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Movie> q = cb.createQuery(Movie.class);
        Root<Movie> c = q.from(Movie.class);

        q.select(c);

        q.orderBy(cb.desc(c.get("rating")));

        Query query = session.createQuery(q);
        List<Movie> result = query.getResultList();
        return result;
    }
}
