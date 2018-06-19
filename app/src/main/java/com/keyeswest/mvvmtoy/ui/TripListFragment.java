package com.keyeswest.mvvmtoy.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
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
import com.keyeswest.mvvmtoy.R;

import com.keyeswest.mvvmtoy.adapters.TripListAdapter;
import com.keyeswest.mvvmtoy.databinding.ListFragmentBinding;
import com.keyeswest.mvvmtoy.db.DataGenerator;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.viewmodel.TripListViewModel;
import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class TripListFragment extends Fragment {
    public static final String TAG = "TripListFragment";

    private ListFragmentBinding mBinding;
    private TripListAdapter mTripListAdapter;
    private TripListViewModel mTripListViewModel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Timber.d("OnCreateView");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container,
                false);

        mBinding.tripsList.setLayoutManager(new LinearLayoutManager(getActivity()));



        DividerItemDecoration itemDecorator = new DividerItemDecoration(Objects
                .requireNonNull(getActivity()), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(),
                R.drawable.custom_list_divider)));

        mBinding.tripsList.addItemDecoration(itemDecorator);


        return mBinding.getRoot();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("OnActivityCreated");
        mTripListViewModel =
                ViewModelProviders.of(this).get(TripListViewModel.class);

        mTripListAdapter = new TripListAdapter(mTripListViewModel);


        mBinding.tripsList.setAdapter(mTripListAdapter);

        mBinding.fab.setOnClickListener(fabListener);

        subscribeUi(mTripListViewModel);
    }

    private TripViewModel findModelMatch(TripEntity trip){
        TripViewModel result = null;
        for (TripViewModel model : mTripListViewModel.getModels()){
            if (model.tripEntity.getId().equals(trip.getId())){
                result = model;
                break;
            }
        }

        return result;
    }


    private void subscribeUi(TripListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getTrips().observe(this, new Observer<List<TripEntity>>() {
            @Override
            public void onChanged(@Nullable List<TripEntity> trips) {
                if (trips != null) {
                    Timber.d("onChanged Executed");
                    mBinding.setIsLoading(false);

                    List<TripViewModel> newList = new ArrayList<>();
                    for (TripEntity trip : trips){
                        // determine if we have an existing model corresponding to a trip
                        TripViewModel match = findModelMatch(trip);
                        if (match != null){
                            newList.add(match);
                        }else{
                            TripViewModel model = new TripViewModel(trip, false);
                            newList.add(model);
                        }
                    }

                    viewModel.setModels(newList);

                    mTripListAdapter.setTripModels(newList);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }


    private View.OnClickListener fabListener = (View v) -> {
        Timber.d("Fab clicked insert random trip");
        List<TripEntity> trips = DataGenerator.generateTrips(1);

        // Later this will start a new activity

        ((MainApp) Objects.requireNonNull(getActivity()).getApplication())
                .getRepository().insert(trips.get(0));

    };



}
