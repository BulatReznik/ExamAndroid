package com.example.exam;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.example.exam.databinding.FragmentItemBinding;
import java.util.List;

import DbModels.Car;

public class MyCarRecyclerViewAdapter extends RecyclerView.Adapter<MyCarRecyclerViewAdapter.ViewHolder> {

    private final List<Car> mValues;
    private AdapterView.OnItemLongClickListener mListener;

    public MyCarRecyclerViewAdapter(List<Car> items, AdapterView.OnItemLongClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Car car = mValues.get(position);

        holder.mItem = car;
        holder.mContentView.setText(car.toString());

        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onItemLongClick(null, v, holder.getAdapterPosition(), car.getId());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Car getItem(int position) {
        return mValues.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Car mItem;

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
