package com.example.exam;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DbModels.Car;
import DbModels.CarStatus;
import Repositories.Car.CarRepositoryRoom;
import Repositories.Car.ICarRepository;

public class ReportActivity extends AppCompatActivity {

    private ICarRepository carRepository;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        carRepository = new CarRepositoryRoom(this);
        executorService = Executors.newSingleThreadExecutor();

        TextView textViewAccepted = findViewById(R.id.textViewAccepted);
        TextView textViewAcceptedList = findViewById(R.id.textViewAcceptedList);
        TextView textViewRented = findViewById(R.id.textViewRented);
        TextView textViewRentedList = findViewById(R.id.textViewRentedList);
        TextView textViewWrittenOff = findViewById(R.id.textViewWrittenOff);
        TextView textViewWrittenOffList = findViewById(R.id.textViewWrittenOffList);

        executorService.execute(() -> {
            List<Car> cars = carRepository.getCars();

            final int[] acceptedCount = {0};
            final int[] rentedCount = {0};
            final int[] writtenOffCount = {0};

            StringBuilder acceptedList = new StringBuilder();
            StringBuilder rentedList = new StringBuilder();
            StringBuilder writtenOffList = new StringBuilder();

            for (Car car : cars) {
                if (car.getStatus() == CarStatus.ACCEPTED) {
                    acceptedCount[0]++;
                    acceptedList.append(car.getModel()).append("\n");
                } else if (car.getStatus() == CarStatus.RENTED) {
                    rentedCount[0]++;
                    rentedList.append(car.getModel()).append("\n");
                } else if (car.getStatus() == CarStatus.WRITTEN_OFF) {
                    writtenOffCount[0]++;
                    writtenOffList.append(car.getModel()).append("\n");
                }
            }

            runOnUiThread(() -> {
                textViewAccepted.setText(getString(R.string.accepted_count, acceptedCount[0]));
                textViewAcceptedList.setText(acceptedList.toString().trim());

                textViewRented.setText(getString(R.string.rented_count, rentedCount[0]));
                textViewRentedList.setText(rentedList.toString().trim());

                textViewWrittenOff.setText(getString(R.string.written_off_count, writtenOffCount[0]));
                textViewWrittenOffList.setText(writtenOffList.toString().trim());
            });
        });
    }
}
