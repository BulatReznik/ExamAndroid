package Repositories;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import Dao.UserDao;
import DataBase.AppDatabase;
import DbModels.User;

public class UserRepositoryRoom implements IUserRepository {
    private UserDao userDao;

    public UserRepositoryRoom(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "user-database").build();
        userDao = db.userDao();
    }

    @Override
    public List<User> getUsers() {
        return userDao.getAll();
    }

    @Override
    public User getUser(int id) {
        return userDao.findById(id);
    }

    @Override
    public void addUser(User user) {
        new Thread(() -> userDao.insertAll(user)).start();
    }

    @Override
    public void updateUser(int index, User user) {
        userDao.update(user);
    }

    @Override
    public void removeUser(int index) {
        // Assuming you have a method to get user by index from userDao
        User userToRemove = userDao.getAll().get(index);
        userDao.delete(userToRemove);
    }
}
