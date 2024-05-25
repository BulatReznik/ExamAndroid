package com.example.exam;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.example.exam.databinding.FragmentItemBinding;
import java.util.List;
import DbModels.User;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private AdapterView.OnItemLongClickListener mListener;

    public MyUserRecyclerViewAdapter(List<User> items, AdapterView.OnItemLongClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = mValues.get(position);

        holder.mItem = user;
        holder.mIdView.setText(user.getFirstName());
        holder.mContentView.setText(user.getLastName());

        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(null, v, holder.getAdapterPosition(), user.getId());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public User getItem(int position) {
        return mValues.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public User mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
