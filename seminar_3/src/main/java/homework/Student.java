package homework;

import homework.annotations.Column;
import homework.annotations.Id;
import homework.annotations.Table;

import java.sql.*;

@Table(name = "Students")
public class Student {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "age")
    private int age;

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Student(String firstName, String secondName, int age) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    public Student(long id, String firstName, String secondName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    public static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                    CREATE TABLE students (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      first_name VARCHAR(256),
                      second_name VARCHAR(256),
                      age TINYINT
                    )
                    """);
        }
    }

    public static void save(Connection connection, Student student) throws SQLException {
        Student checkStudent = Student.getById(connection, student.getId());
        if (checkStudent == null) {
            try (PreparedStatement stmt = connection.prepareStatement("insert into students (first_name, second_name, age) values (?,?,?)")) {
                stmt.setString(1, student.getFirstName());
                stmt.setString(2, student.getSecondName());
                stmt.setInt(3, student.getAge());
                stmt.executeUpdate();
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery("select * from students order by id desc limit 1");

                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        student.setId(id);
                    }
                }
            }
        } else {
            Student.update(connection, student);
        }
    }


    public static void update(Connection connection, Student student) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "update students set first_name = ?, second_name = ?, age = ? where id = ?")
        ) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getSecondName());
            stmt.setInt(3, student.getAge());
            stmt.setLong(4, student.getId());

            stmt.executeUpdate();
        }
    }

    public static Student getById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "select * from students where id = ?")
        ) {
            stmt.setLong(1, id);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long idStudent = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                int age = resultSet.getInt("age");
                return new Student(idStudent, firstName, secondName, age);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", age=" + age +
                '}';
    }
}
