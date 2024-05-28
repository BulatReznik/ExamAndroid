package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DbModels.Car;
import DbModels.CarStatus;
import Repositories.Car.CarRepositoryRoom;
import Repositories.Car.ICarRepository;

public class CarUpdateFragment extends Fragment {

    private static final String ARG_CAR_ID = "car-id";
    private CarUpdateFragment.OnCarUpdatedListener listener;
    private ICarRepository carRepository;
    private ExecutorService executorService;
    private Car car;

    public CarUpdateFragment() {
    }

    public static CarUpdateFragment newInstance(int carId) {
        CarUpdateFragment fragment = new CarUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAR_ID, carId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carRepository = new CarRepositoryRoom(getContext());
        executorService = Executors.newSingleThreadExecutor();

        if (getArguments() != null) {
            int carId = getArguments().getInt(ARG_CAR_ID);
            executorService.execute(() -> {
                car = carRepository.getCar(carId);
                if (car == null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                    });
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_update, container, false);

        EditText modelEditText = view.findViewById(R.id.editTextTextModel);
        EditText customerEditText = view.findViewById(R.id.editTextCustomer);
        Spinner statusSpinner = view.findViewById(R.id.spinnerStatus);
        Button updateButton = view.findViewById(R.id.buttonUpdate);

        String[] statuses = {"Добавлена", "Выдана", "Списать"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        if (car != null) {
            modelEditText.setText(car.getModel());
            if (car.getStatus() == CarStatus.RENTED) {
                customerEditText.setText(car.getCustomer());
                // Задаем статус "Выдана" в spinnerStatus
                statusSpinner.setSelection(adapter.getPosition("Выдана"));
            }
            if(car.getStatus() == CarStatus.WRITTEN_OFF){
                customerEditText.setText(car.getCustomer());
                statusSpinner.setSelection(adapter.getPosition("Списана"));
            }
        }

        updateButton.setOnClickListener(v -> {
            try {
                String model = modelEditText.getText().toString();
                String customer = customerEditText.getText().toString();
                CarStatus status;

                if (statusSpinner.getSelectedItem().toString().equals("Выдана")) {
                    status = CarStatus.RENTED;
                }else if (statusSpinner.getSelectedItem().toString().equals("Списать")) {
                    status = CarStatus.WRITTEN_OFF;
                } else {
                    status = CarStatus.ACCEPTED;
                }

                executorService.execute(() -> {
                    car.setModel(model);
                    car.setCustomer(customer);
                    car.setStatus(status);
                    carRepository.updateCar(car.getId(), car);

                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            if (listener != null) {
                                listener.onCarUpdated();
                            }
                        });
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCarUpdatedListener) {
            listener = (OnCarUpdatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCarUpdatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnCarUpdatedListener {
        void onCarUpdated();
    }
}
