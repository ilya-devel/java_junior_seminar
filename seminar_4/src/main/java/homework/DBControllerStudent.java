package homework;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DBControllerStudent implements IDBControllerStudent {
    private static SessionFactory sessionFactory;

    @Override
    public Student findById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void persist(Student student) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(student);
        tx.commit();
        System.out.println("Saved: " + student);
        session.close();
    }

    @Override
    public void merge(Student student) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(student);
        transaction.commit();
        session.close();
    }

    @Override
    public Student remove(long id) {
        Student student = findById(id);
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.remove(student);
        tx.commit();
        session.close();
        return student;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Student.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
