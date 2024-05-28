package Repositories.Car;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import Dao.CarDao;
import DataBase.AppDatabase;
import DbModels.Car;

public class CarRepositoryRoom implements ICarRepository {
    private CarDao carDao;

    public CarRepositoryRoom(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "car-database").build();
        carDao = db.carDao();
    }

    @Override
    public List<Car> getCars() {
        return carDao.getAll();
    }

    @Override
    public Car getCar(int id) {
        return carDao.findById(id);
    }

    @Override
    public void addCar(Car car) {
        new Thread(() -> carDao.insertAll(car)).start();
    }

    @Override
    public void updateCar(int index, Car car) {
        carDao.update(car);
    }

    @Override
    public void removeCar(int index) {
        Car carToRemove = carDao.getAll().get(index);
        carDao.delete(carToRemove);
    }
}
