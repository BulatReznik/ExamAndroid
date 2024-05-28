package DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import Dao.CarDao;
import Dao.UserDao;
import DbModels.Car;
import DbModels.User;

@Database(entities = {Car.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CarDao carDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "car_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}