package seminar;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            acceptConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createTable(connection);

        insertData(connection);

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, name from person");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                System.out.println("id = " + id + ", name = " + name);
            }
        }

    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into Person(id, name) values
                    (1, 'Igor'),
                    (2, 'Person #1'),
                    (3, 'Igor2'),
                    (4, 'Sveta'),
                    (5, 'Mary')
                    """);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table Person (
                    id bigint,
                    name varchar(256)
                    )
                    """);
        }
    }
}