package Repositories;

import java.util.List;

import DbModels.User;

public interface IUserRepository {
    List<User> getUsers();
    User getUser(int id);
    void addUser(User user);
    void updateUser(int index, User user);
    void removeUser(int index);
}
