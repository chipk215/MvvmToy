package com.keyeswest.mvvmtoy.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.mvvmtoy.MainApp;
import com.keyeswest.mvvmtoy.adapters.TripClickListener;
import com.keyeswest.mvvmtoy.adapters.TripListAdapter;
import com.keyeswest.mvvmtoy.databinding.ListFragmentBinding;

import android.databinding.DataBindingUtil;

import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.db.DataGenerator;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.model.Trip;
import com.keyeswest.mvvmtoy.viewmodel.TripListViewModel;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class TripListFragment extends Fragment {
    public static final String TAG = "TripListFragment";

    private ListFragmentBinding mBinding;
    private TripListAdapter mTripListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container,
                false);

        mBinding.tripsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTripListAdapter = new TripListAdapter(getContext(), null,  mTripClickListener);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(Objects
                .requireNonNull(getActivity()), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(),
                R.drawable.custom_list_divider)));

        mBinding.tripsList.addItemDecoration(itemDecorator);
        mBinding.tripsList.setAdapter(mTripListAdapter);

        mBinding.fab.setOnClickListener(fabListener);

        return mBinding.getRoot();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TripListViewModel viewModel =
                ViewModelProviders.of(this).get(TripListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(TripListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getTrips().observe(this, new Observer<List<TripEntity>>() {
            @Override
            public void onChanged(@Nullable List<TripEntity> trips) {
                if (trips != null) {
                    Timber.d("onChanged Executed");
                    mBinding.setIsLoading(false);
                    mTripListAdapter.setTripList(trips);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }


    private final TripClickListener mTripClickListener = new TripClickListener() {
        @Override
        public void onItemChecked(TripEntity trip) {

        }

        @Override
        public void onItemUnchecked(TripEntity trip) {

        }

        @Override
        public void onDeleteClick(TripEntity trip) {
            Timber.d("on delete clicked");
            Timber.d("Trip distance= %s", trip.getDistanceMiles());

            ((MainApp) Objects.requireNonNull(getContext())
                    .getApplicationContext()).getRepository().delete(trip);


        }

        @Override
        public void onFavoriteClick(TripEntity trip, boolean selected) {

        }
    };


    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Timber.d("Fab clicked insert random trip");
            List<TripEntity> trips = DataGenerator.generateTrips(1);
          //  ((MainApp) Objects.requireNonNull(getContext())
           //         .getApplicationContext()).getRepository().insert(trips.get(0));

            ((MainApp) Objects.requireNonNull(getActivity()).getApplication())
                    .getRepository().insert(trips.get(0));

        }
    };




}
