package DbModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car")
public class Car {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String model;
    private CarStatus status;
    private String customer;

    public Car(String model, CarStatus status) {
        this.model = model;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", status=" + status +
                ", customer='" + customer + '\'' +
                '}';
    }
}

