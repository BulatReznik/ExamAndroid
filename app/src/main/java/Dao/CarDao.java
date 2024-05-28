package Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import DbModels.Car;

@Dao
public interface CarDao {
    @Query("Select * FROM car")
    List<Car> getAll();

    @Query("SELECT * FROM car WHERE id IN (:carIds)")
    List<Car> loadAllByIds(int[] carIds);

    @Query("SELECT * FROM car WHERE id = :carId")
    Car findById(int carId);

    @Insert
    void insertAll(Car... cars);

    @Delete
    void delete(Car car);

    @Update
    void update(Car car);
}
