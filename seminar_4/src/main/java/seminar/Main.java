package seminar;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "", "");
                 Statement statement = connection.createStatement()) {
                statement.executeQuery("select * from persons");
            }
        } catch (Exception e) {
//            System.err.println(e);
        }
    }
}