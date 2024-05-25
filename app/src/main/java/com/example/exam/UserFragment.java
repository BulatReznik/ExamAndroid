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
import DbModels.User;
import Repositories.IUserRepository;
import Repositories.UserRepositoryRoom;

public class UserFragment extends Fragment implements UserAddFragment.OnUserAddedListener, AdapterView.OnItemLongClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private IUserRepository userRepository;
    private MyUserRecyclerViewAdapter adapter;
    private ExecutorService executorService;
    private int mColumnCount = 1;

    public UserFragment() {
    }

    @SuppressWarnings("unused")
    public static UserFragment newInstance(int columnCount) {
        UserFragment fragment = new UserFragment();
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

            userRepository = new UserRepositoryRoom(getContext());
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

            loadUsers(recyclerView);
        }
        return view;
    }

    private void loadUsers(RecyclerView recyclerView) {
        executorService.execute(() -> {
            List<User> users = userRepository.getUsers();
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    adapter = new MyUserRecyclerViewAdapter(users, this);
                    recyclerView.setAdapter(adapter);
                });
            }
        });
    }

    @Override
    public void onUserAdded() {
        if (getView() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) getView();
            loadUsers(recyclerView);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        User user = adapter.getItem(position);
        openUpdateFragment(user.getId());
        return true;
    }

    private void openUpdateFragment(int userId) {
        UserUpdateFragment updateFragment = UserUpdateFragment.newInstance(userId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, updateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
