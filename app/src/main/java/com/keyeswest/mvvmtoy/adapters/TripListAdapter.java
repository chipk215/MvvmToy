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

import java.util.List;


public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {


    List<TripEntity> mTripList;

    private Context mContext;
    private TripClickListener mTripClickListener;


    public TripListAdapter(Context context) {
        mContext = context;

    }

    public void setUIHandlers(TripClickListener listener) {
        mTripClickListener = listener;
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TripItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.trip_item,
                        parent, false);


        return new TripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.binding.setTrip(mTripList.get(position));

        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mTripList == null ? 0 : mTripList.size();
    }


    public void setTripList(final List<TripEntity> tripList) {
        if (mTripList == null) {
            mTripList = tripList;
            notifyItemRangeInserted(0, tripList.size());
        } else {

            mTripList = tripList;
            notifyDataSetChanged();
        }

    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

        final TripItemBinding binding;

        public TripViewHolder(TripItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mTripClickListener.onTripClicked(binding.getTrip());
                    //TODO there must be a better way!
                    TripEntity trip = binding.getTrip();
                    binding.checkBox.setChecked(trip.isSelected());
                }
            });

            binding.deleteBtn.setOnClickListener(v -> {
                mTripClickListener.onDeleteClick(binding.getTrip());
            });

            binding.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TripEntity trip = binding.getTrip();
                    mTripClickListener.onFavoriteClick(trip);
                }
            });


        }
    }
}
