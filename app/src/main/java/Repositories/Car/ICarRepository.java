package Repositories.Car;

import java.util.List;

import DbModels.Car;

public interface ICarRepository {
    List<Car> getCars();
    Car getCar(int id);
    void addCar(Car car);
    void updateCar(int index, Car car);
    void removeCar(int index);
}
