package Repositories.Car;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import DbModels.Car;

public class CarRepository implements ICarRepository {
    private static final String FILE_NAME = "cars.json";
    private List<Car> cars;
    private Context context;

    public CarRepository(Context context) {
        this.context = context;
        this.cars = new ArrayList<>();
        loadCars();
    }

    public List<Car> getCars() {
        return cars;
    }

    @Override
    public Car getCar(int id) {
        return null;
    }

    public void addCar(Car car) {
        cars.add(car);
        saveCars();
    }

    public void updateCar(int index, Car car) {
        cars.set(index, car);
        saveCars();
    }

    public void removeCar(int index) {
        cars.remove(index);
        saveCars();
    }

    public void saveCars() {
        Gson gson = new Gson();
        String json = gson.toJson(cars);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCars() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            createEmptyCarsFile();
        }

        try (FileInputStream fis = context.openFileInput(FILE_NAME)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json = new String(data);
            Gson gson = new Gson();
            Type carListType = new TypeToken<ArrayList<Car>>(){}.getType();
            cars = gson.fromJson(json, carListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createEmptyCarsFile() {
        String emptyJson = "[]";
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(emptyJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
