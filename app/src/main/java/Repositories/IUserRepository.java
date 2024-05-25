package Repositories;

import java.util.List;

import DbModels.User;

public interface IUserRepository {
    List<User> getUsers();
    void addUser(User user);
    void updateUser(int index, User user);
    void removeUser(int index);
}
