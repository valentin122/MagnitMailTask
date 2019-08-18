package ru.vzhigalov.dao;

import ru.vzhigalov.servise.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    private String url;
    private String user;
    private String pwd;

    static final String CREATE = "CREATE TABLE IF NOT EXISTS TEST (field varchar)";
    static final String CLEAN = "TRUNCATE TEST";
    static final String INSERT = "INSERT INTO TEST (field) VALUES (?)";
    static final String SELECT = "SELECT field FROM TEST";

    Config config = new Config();

    void setUrl() {
        this.url = config.get("jdbc.url");
    }

    void setLogin() {
        this.user = config.get("jdbc.user");
    }

    void setPassword() {
        this.pwd = config.get("jdbc.pwd");
    }

    public void initConfigConnectionToDb() {
        config.init();
        setUrl();
        setLogin();
        setPassword();
    }

    public void dbCreateAndClear() {
        System.out.println(url);
        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            executeQuery(connection, CREATE, "db can't create");
            executeQuery(connection, CLEAN, "db cleaning failed");
        } catch (SQLException e) {
            System.out.println("db cleaning failed");
            e.printStackTrace();
        }
    }

    void executeQuery(Connection connection, String sqlQuery, String errorMessage) {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(errorMessage);
            e.printStackTrace();
        }
    }

    public void dbInsert(List<String> subjects) {
        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
                connection.setAutoCommit(false);
                for (int i = 0; i < subjects.size(); i++) {
                    preparedStatement.setString(1, subjects.get(i));
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                System.out.println("db inserted!");

                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("db inserted failed");
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    System.out.println("rollback failed");
                    e.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("db connection failed");
            e.printStackTrace();
        }
    }

    public List<String> getDataFromDb() throws SQLException {
        List<String> result = new ArrayList<>();
        ResultSet rs;
        try (Connection connection = DriverManager.getConnection(url, user, pwd)) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT)) {
                rs = statement.executeQuery();
                while (rs.next()) {
                    result.add(rs.getString(1));
                }
            } catch (SQLException e) {
                System.out.println("Select failed");
                e.printStackTrace();
            }
        }
        System.out.println("Data has been read from DB");
        return result;
    }
}
