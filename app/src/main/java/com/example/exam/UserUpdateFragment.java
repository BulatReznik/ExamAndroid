package com.example.exam;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import DbModels.User;
import Repositories.IUserRepository;
import Repositories.UserRepositoryRoom;

public class UserUpdateFragment extends Fragment {

    private static final String ARG_USER_ID = "user-id";
    private UserAddFragment.OnUserAddedListener listener;
    private IUserRepository userRepository;
    private ExecutorService executorService;
    private User user;

    public UserUpdateFragment() {
    }

    public static UserUpdateFragment newInstance(int userId) {
        UserUpdateFragment fragment = new UserUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRepository = new UserRepositoryRoom(getContext());
        executorService = Executors.newSingleThreadExecutor();

        if (getArguments() != null) {
            int userId = getArguments().getInt(ARG_USER_ID);
            executorService.execute(() -> {
                user = userRepository.getUser(userId);
                if (user == null && isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        // Handle user not found case
                    });
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);

        EditText firstNameEditText = view.findViewById(R.id.editTextText3);
        EditText lastNameEditText = view.findViewById(R.id.editTextText4);
        EditText ageEditText = view.findViewById(R.id.editTextText6);

        if (user != null) {
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            ageEditText.setText(String.valueOf(user.getAge()));
        }

        Button addButton = view.findViewById(R.id.button2);
        addButton.setText("Update");

        addButton.setOnClickListener(v -> {
            try {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());

                executorService.execute(() -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setAge(age);
                    userRepository.updateUser(user.id, user);

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
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserAddFragment.OnUserAddedListener) {
            listener = (UserAddFragment.OnUserAddedListener) context;
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
