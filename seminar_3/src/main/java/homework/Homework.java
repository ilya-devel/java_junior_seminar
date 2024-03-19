package homework;

import java.sql.*;

public class Homework {

    /**
     * 0. Разобрать код с семниара
     * 1. Повторить код с семниара без подглядываний на таблице Student с полями:
     * 1.1 id - int
     * 1.2 firstName - string
     * 1.3 secondName - string
     * 1.4 age - int
     * 2.* Попробовать подключиться к другой БД
     * 3.** Придумать, как подружить запросы и reflection:
     * 3.1 Создать аннотации Table, Id, Column
     * 3.2 Создать класс, у которого есть методы:
     * 3.2.1 save(Object obj) сохраняет объект в БД
     * 3.2.2 update(Object obj) обновляет объект в БД
     * 3.2.3 Попробовать объединить save и update (сначала select, потом update или insert)
     */

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            Student.createTable(connection);
            Student john = new Student("John", "Smith", 21);
            Student mary = new Student("Mary", "Rose", 16);
            Student yang = new Student("Yang", "Xiao Long", 18);
            Student blake = new Student("Blake", "Belladonna", 18);
            blake.setId(999L);
            Student.save(connection, john);
            Student.save(connection, mary);
            Student.save(connection, yang);
            Student.save(connection, blake);
            john.setSecondName("Black");
            Student.update(connection, john);
            yang.setSecondName("Xiao");
            Student.save(connection, yang);

            Student getStudent = Student.getById(connection,2L);
            System.out.println(getStudent);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("select * from students");

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("first_name");
                    String secondName = resultSet.getString("second_name");
                    int age = resultSet.getInt("age");

                    System.out.println("id = " + id + ", name = " + name + ", second_name = " + secondName
                    + ", age = " + age);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
