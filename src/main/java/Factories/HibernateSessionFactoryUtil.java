package Factories;
import com.company.base.accenture.movies.ObjModelClass.Movie;
import com.company.base.accenture.movies.ObjModelClass.Review;
import com.company.base.accenture.movies.ObjModelClass.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HibernateSessionFactoryUtil {

    private HibernateSessionFactoryUtil() {}

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                Map<String, String> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "org.h2.Driver");
                settings.put(Environment.URL, "jdbc:h2:tcp://localhost/./test");
                settings.put(Environment.USER, "sa");
                settings.put(Environment.PASS, "1");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

                registryBuilder.applySettings(settings);

                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);
                sources.addAnnotatedClass(User.class);
                sources.addAnnotatedClass(Movie.class);
                sources.addAnnotatedClass(Review.class);
                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void close() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
