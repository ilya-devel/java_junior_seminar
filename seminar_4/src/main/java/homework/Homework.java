package homework;


import org.hibernate.query.Query;

import java.util.List;

public class Homework {

    /**
     * 1. Создать сущность Student с полями:
     * 1.1 id - int
     * 1.2 firstName - string
     * 1.3 secondName - string
     * 1.4 age - int
     * 2. Подключить hibernate. Реализовать простые запросы: Find(by id), Persist, Merge, Remove
     * 3. Попробовать написать запрос поиска всех студентов старше 20 лет (session.createQuery)
     */

    public static void main(String[] args) {
        DBControllerStudent controller = new DBControllerStudent();
        Student student1 = new Student("Mary1", "Rose", 14);
        Student student2 = new Student("Mary2", "Rose", 14);
        Student student3 = new Student("Mary3", "Rose", 14);
        Student student4 = new Student("Mary4", "Rose", 14);
        Student student5 = new Student("Mary5", "Rose", 14);
        Student student6 = new Student("Mary6", "Rose", 23);
        Student student7 = new Student("Mary7", "Rose", 22);
        Student student8 = new Student("Mary8", "Rose", 29);

        controller.persist(student1);
        controller.persist(student2);
        controller.persist(student3);
        controller.persist(student4);
        controller.persist(student5);
        controller.persist(student6);
        controller.persist(student7);
        controller.persist(student8);

        System.out.println();
        System.out.println("Find: " + controller.findById(1L));
        System.out.println("Find: " + controller.findById(3L));
        System.out.print("IF not exist => ");
        System.out.println("Find: " + controller.findById(9L));

        System.out.println();
        System.out.println("Deleted: " + controller.remove(3));
        System.out.println("Try find deleted object =>" + controller.findById(3));

        System.out.println();
        System.out.println("Update student");
        System.out.println("Before update => " + controller.findById(4));
        student4.setFirstName("Mary");
        student4.setAge(18);
        controller.merge(student4);
        System.out.println("After update => " + controller.findById(4));

        System.out.println();
        System.out.println("Students over 20 years old:");
        String hql = "from Student where age > :paramAge";
        Query<Student> query = DBControllerStudent.getSessionFactory().openSession().createQuery(hql, Student.class);
        query.setParameter("paramAge", 20);
        List<Student> studentList = query.list();
        for (Student student: studentList) System.out.println(student);
    }

}
