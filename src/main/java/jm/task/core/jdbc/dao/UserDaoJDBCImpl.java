package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    private String sqlCommand;
    private Statement statement;
    private PreparedStatement preparedStatement;

    private void anyStatementClose() {
        if (statement != null) {
            try {
                statement.close();
//                System.out.println("statement closed");
            } catch (SQLException e) {
                System.err.println("statement.close problem");
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
//                System.out.println("preparedStatement closed");
            } catch (SQLException e) {
                System.err.println("preparedStatement.close problem");
            }
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        sqlCommand = "CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45), lastName VARCHAR(45), age TINYINT) charset = utf8mb3";
        statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            System.out.println("Table Users has been created or already exists!");
        } catch (SQLException e) {
            System.err.println("Table creation problem");
        } finally {
            this.anyStatementClose();
        }
    }

    public void dropUsersTable() {
        sqlCommand = "DROP TABLE users";
        statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            System.out.println("Table Users has been deleted!");
        } catch (SQLException e) {
            System.err.println("Table deletion problem");
        } finally {
            this.anyStatementClose();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        preparedStatement = null;
        sqlCommand = "insert into users (name, lastName, age) values(?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sqlCommand);
//            preparedStatement.setLong(1, id); // autoincrement
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.anyStatementClose();
        }
    }

    public void removeUserById(long id) {
        //  сперва нужно подавать команду, так как по ней видно тип запроса statement
        sqlCommand = "delete from users where ID=?";
        String sql2 = "SELECT * FROM users where ID=?";
        preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.anyStatementClose();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        sqlCommand = "SELECT * FROM users";
        statement = null;
        try {
            statement = connection.createStatement();
            // ResultSet это поезд с данными по запросу executeQuery(sql)
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                System.out.println(user);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.anyStatementClose();
        }
        return userList;
    }

    public void cleanUsersTable() {
        sqlCommand = "DELETE FROM users";
        statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCommand);
            System.out.println("Database has been cleared!");
        } catch (SQLException e) {
            System.err.println("Database clearing problem");
        } finally {
            this.anyStatementClose();
        }
    }
}
