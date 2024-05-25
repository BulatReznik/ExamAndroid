package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import DbModels.User;
import Repositories.IUserRepository;
import Repositories.UserRepository;
import Repositories.UserRepositoryRoom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserAddFragment extends Fragment {

    private OnUserAddedListener listener;
    private IUserRepository userRepository;
    private ExecutorService executorService;

    public UserAddFragment() {
        // Required empty public constructor
    }

    public interface OnUserAddedListener {
        void onUserAdded();
    }

    public static UserAddFragment newInstance() {
        return new UserAddFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isDb = true; // Установите значение в true или false в зависимости от выбранного источника данных

        if (isDb) {
            userRepository = new UserRepositoryRoom(getContext());
        } else {
            userRepository = new UserRepository(getContext());
        }

        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);

        Button addButton = view.findViewById(R.id.button2);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Получаем данные
                    EditText firstNameEditText = view.findViewById(R.id.editTextText3);
                    EditText lastNameEditText = view.findViewById(R.id.editTextText4);
                    EditText ageEditText = view.findViewById(R.id.editTextText6);

                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    int age = Integer.parseInt(ageEditText.getText().toString());

                    // Добавляем пользователя в фоновом потоке
                    executorService.execute(() -> {
                        userRepository.addUser(new User(firstName, lastName, age));

                        if (isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                if (listener != null) {
                                    listener.onUserAdded();
                                }
                            });
                        }
                    });

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserAddedListener) {
            listener = (OnUserAddedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserAddedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
