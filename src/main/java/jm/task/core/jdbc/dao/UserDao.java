package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;

public interface UserDao {

    void createUsersTable();

    void dropUsersTable();

    //Create or Update
    void saveUser(String name, String lastName, byte age);

    //Delete
    void removeUserById(long id);

    //Read All
    List<User> getAllUsers();

    void cleanUsersTable();
}
