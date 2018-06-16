package com.keyeswest.mvvmtoy.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.databinding.TripItemBinding;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.model.Trip;
import com.keyeswest.mvvmtoy.utilities.MathHelper;

import java.util.List;
import java.util.Objects;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private final static int MAX_SELECTED_TRIPS = 4;

    List< ? extends Trip> mTripList;

    private Context mContext;
    private TripClickListener mTripClickListener;
    private List<TripEntity> mInitialSelectedTrips;
    private boolean mSelectionsFrozen;

    public interface TripClickListener {
        void onItemChecked(TripEntity segment);

        void onItemUnchecked(TripEntity segment);

        void onDeleteClick(TripEntity segment);

        void onFavoriteClick(TripEntity segment, boolean selected);
    }

    public TripListAdapter(Context context, List<TripEntity> selectedTrips,
                           TripClickListener listener){
        mContext = context;
        mTripClickListener = listener;
        mInitialSelectedTrips = selectedTrips;
        if ( (selectedTrips == null) || (selectedTrips.size() <  MAX_SELECTED_TRIPS)){
            mSelectionsFrozen = false;
        }else{
            mSelectionsFrozen = true;
        }

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
        return  mTripList == null ? 0 : mTripList.size();
    }

    public void setTripList(final List< ? extends Trip> tripList){
        if (mTripList == null){
            mTripList = tripList;
            notifyItemRangeInserted(0, tripList.size());
        }else{
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mTripList.size();
                }

                @Override
                public int getNewListSize() {
                    return tripList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mTripList.get(oldItemPosition).getId()
                            .equals(tripList.get(newItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Trip newTrip = tripList.get(newItemPosition);
                    Trip oldTrip = mTripList.get(oldItemPosition);
                    return  Objects.equals(newTrip.getId(), oldTrip.getId()) &&
                            Objects.equals(newTrip.getTime(), oldTrip.getTime()) &&
                            Objects.equals(newTrip.getDate(), oldTrip.getDate()) &&
                            Objects.equals(newTrip.getDistanceMiles(),
                                    oldTrip.getDistanceMiles()) &&
                            MathHelper.areSame(newTrip.getMaxLatitude(),
                                    oldTrip.getMaxLatitude()) &&
                            MathHelper.areSame(newTrip.getMinLatitude(),
                                    oldTrip.getMinLatitude()) &&
                            MathHelper.areSame(newTrip.getMinLongitude(),
                                    oldTrip.getMinLongitude()) &&
                            MathHelper.areSame(newTrip.getMaxLongitude(),
                                    oldTrip.getMaxLongitude()) &&
                            newTrip.isFavorite() == oldTrip.isFavorite() &&
                            newTrip.getDuration() == oldTrip.getDuration();

                }
            });

            mTripList = tripList;
            result.dispatchUpdatesTo(this);
        }

    }

    static class TripViewHolder extends RecyclerView.ViewHolder{

        final TripItemBinding binding;

        public TripViewHolder(TripItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
