package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import DbModels.Car;
import DbModels.CarStatus;
import Repositories.Car.CarRepository;
import Repositories.Car.CarRepositoryRoom;
import Repositories.Car.ICarRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarAddFragment extends Fragment {

    private OnCarAddedListener listener;
    private ICarRepository carRepository;
    private ExecutorService executorService;

    public CarAddFragment() {
        // Required empty public constructor
    }

    public interface OnCarAddedListener {
        void onCarAdded();
    }

    public static CarAddFragment newInstance() {
        return new CarAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isDb = true; // Установите значение в true или false в зависимости от выбранного источника данных

        if (isDb) {
            carRepository = new CarRepositoryRoom(getContext());
        } else {
            carRepository = new CarRepository(getContext());
        }

        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_add, container, false);

        Button addButton = view.findViewById(R.id.button2);

        addButton.setOnClickListener(v -> {
            try {
                // Получаем данные
                EditText modelNameEditText = view.findViewById(R.id.editTextTextModel);

                String modelName = modelNameEditText.getText().toString();

                // Добавляем пользователя в фоновом потоке
                executorService.execute(() -> {
                    carRepository.addCar(new Car(modelName, CarStatus.ACCEPTED ));

                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            if (listener != null) {
                                listener.onCarAdded();
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
        if (context instanceof OnCarAddedListener) {
            listener = (OnCarAddedListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnCarAddedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
