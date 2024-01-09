package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;
import java.util.Properties;

public class Util {
    private static String URL = "jdbc:mysql://localhost:3306/mystest";
    private static final String USERNANE = "root";
    private static final String PASSWORD = "root";
    //private static final String PASSWORD = "1604javaSQL2023!";
    private static Connection connection = null;

    // соединение для JDBC
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Driver driver = new com.mysql.cj.jdbc.Driver();
                DriverManager.registerDriver(driver);
            } catch (SQLException e) {
                System.err.println("Nope driver loading!?");
            }

            try {
                connection = DriverManager.getConnection(URL, USERNANE, PASSWORD);
                if (!connection.isClosed()) {
                    System.out.println("DB connection executed");
                }
                if (connection.isClosed()) {
                    System.out.println("DB connection closed");
                }
            } catch (SQLException e) {
                System.err.println("Nope connection!?");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("connection closed");
            } catch (SQLException e) {
                System.err.println("connection.close problem");
            }
        }
    }

    // соединение для Hibernate
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/mystest");
                settings.put(Environment.USER, USERNANE);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.err.println("Problem with getting sessionFactory!?");
            }
            System.out.println("new sessionFactory opened");
            return sessionFactory;
        } else {
            System.out.println("return existing sessionFactory");
            return sessionFactory;
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                System.out.println("sessionFactory closed");
            } catch (HibernateException e) {
                System.err.println("sessionFactory.close problem");
            }
        }
    }
}