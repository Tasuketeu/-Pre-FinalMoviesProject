package com.company.base.accenture.movies.DAL;

import com.company.base.accenture.movies.BL.UserServiceImpl;
import Factories.HibernateSessionFactoryUtil;
import com.company.base.accenture.movies.Interfaces.UserAccessService;
import com.company.base.accenture.movies.objModelClass.User;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

import static com.company.base.accenture.movies.BL.UserServiceImpl.activeUser;

@Component
public class UserAccessServiceImpl implements UserAccessService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void registerUsers(String regName, String regLogin, String regPassword, String admin) {

            UserServiceImpl.exist=false;

        List<User> login=getByLogin(regLogin);
        if(!login.isEmpty()) {
                    UserServiceImpl.exist = true;
                    return;
                }

        User user = new User(regName, regLogin, regPassword, admin);
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.flush();
        session.close();
    }

    private List<User> getByLogin(String login){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);
        q.select(c).where(cb.equal(c.get("regLogin"),login));
        Query query = session.createQuery(q);
        List<User> result = query.getResultList();
        return result;
    }

    private List<Tuple> getAll(String login){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> q = cb.createTupleQuery();

        Root<User> c = q.from(User.class);
        q.multiselect(c.get("regName"),c.get("regPassword"),c.get("admin")).where(cb.equal(c.get("regLogin"),login));
        Query query = session.createQuery(q);
        List<Tuple> tupleResult = query.getResultList();
        return tupleResult;
    }

    @Override
    public void searchUser(String name, String login, String password) {
        List<User> foundUser=getByLogin(login);

        if(foundUser.isEmpty()) {
            return;
        }
        List<Tuple> tupleList=getAll(login);
        for (Tuple t:
                tupleList) {
            if(t.get(0).equals(name)&&
                    t.get(1).equals(password))
            {
                activeUser=login;
                if (t.get(2).equals("true")) {
                    UserServiceImpl.adminMode = true;
                }
            }
        }
    }
}
