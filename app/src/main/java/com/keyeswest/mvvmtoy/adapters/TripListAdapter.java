package com.keyeswest.mvvmtoy.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.databinding.TripItemBinding;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;

import java.util.List;


public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private List<TripViewModel> mTripModels;

    private TripClickListener mTripClickListener;


    public TripListAdapter(TripClickListener listener){
        mTripClickListener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TripItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.trip_item,
                        parent, false);

        return new TripViewHolder(binding, mTripClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.setModel(mTripModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mTripModels == null ? 0 : mTripModels.size();
    }


    public void setTripModels(final List<TripViewModel> tripModels) {
        if (mTripModels == null) {
            mTripModels = tripModels;
            notifyItemRangeInserted(0, tripModels.size());
        } else {
            mTripModels = tripModels;
            notifyDataSetChanged();
        }

    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        final TripItemBinding binding;

        void setModel(TripViewModel model){
            binding.setModel(model);
            binding.executePendingBindings();
        }

        TripViewHolder(TripItemBinding binding, TripClickListener itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;


            this.itemView.setOnClickListener(v -> {

                itemClickListener.onTripClicked(binding.getModel());
               //TODO there must be a better way!

               binding.checkBox.setChecked(binding.getModel().isSelected());

            });

            binding.deleteBtn.setOnClickListener(v -> {
                itemClickListener.onDeleteClick(binding.getModel().getTripEntity());
            });

            binding.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TripEntity trip = binding.getModel().getTripEntity();
                    itemClickListener.onFavoriteClick(trip);
                }
            });

        }
    }
}
