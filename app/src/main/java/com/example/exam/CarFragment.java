package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DbModels.Car;
import Repositories.Car.CarRepositoryRoom;
import Repositories.Car.ICarRepository;

public class CarFragment extends Fragment implements CarAddFragment.OnCarAddedListener,
        AdapterView.OnItemLongClickListener,
        CarUpdateFragment.OnCarUpdatedListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private ICarRepository carRepository;
    private MyCarRecyclerViewAdapter adapter;
    private ExecutorService executorService;
    private int mColumnCount = 1;

    public CarFragment() {
    }

    @SuppressWarnings("unused")
    public static CarFragment newInstance(int columnCount) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (getArguments() != null) {
                mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            }

            carRepository = new CarRepositoryRoom(getContext());
            executorService = Executors.newSingleThreadExecutor();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            loadCars(recyclerView);
        }
        return view;
    }

    private void loadCars(RecyclerView recyclerView) {
        executorService.execute(() -> {
            List<Car> cars = carRepository.getCars();
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    adapter = new MyCarRecyclerViewAdapter(cars, this);
                    recyclerView.setAdapter(adapter);
                });
            }
        });
    }

    @Override
    public void onCarAdded() {
        if (getView() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) getView();
            loadCars(recyclerView);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Car car = adapter.getItem(position);
        openUpdateFragment(car.getId());
        return true;
    }

    private void openUpdateFragment(int carId) {
        CarUpdateFragment updateFragment = CarUpdateFragment.newInstance(carId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, updateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCarUpdated() {
        // Выполнить обновление списка автомобилей после успешного обновления данных
        if (getView() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) getView();
            loadCars(recyclerView);
        }
    }
}
