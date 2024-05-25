package Repositories;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import DbModels.User;

public class UserRepository implements IUserRepository {
    private static final String FILE_NAME = "users.json";
    private List<User> users;
    private Context context;

    public UserRepository(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
        loadUsers();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void updateUser(int index, User user) {
        users.set(index, user);
        saveUsers();
    }

    public void removeUser(int index) {
        users.remove(index);
        saveUsers();
    }

    public void saveUsers() {
        Gson gson = new Gson();
        String json = gson.toJson(users);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            createEmptyUsersFile();
        }

        try (FileInputStream fis = context.openFileInput(FILE_NAME)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json = new String(data);
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(json, userListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createEmptyUsersFile() {
        String emptyJson = "[]";
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(emptyJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}