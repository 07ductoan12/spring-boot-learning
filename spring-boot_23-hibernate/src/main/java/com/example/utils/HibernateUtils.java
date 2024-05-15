package com.example.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/** HibernateUtil */
public class HibernateUtils {
    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            final Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return sessionFactory;
    }
}
