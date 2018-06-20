package com.keyeswest.mvvmtoy.ui;

import android.app.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.keyeswest.mvvmtoy.DataRepository;
import com.keyeswest.mvvmtoy.FilterActivity;
import com.keyeswest.mvvmtoy.MainApp;
import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.SortActivity;
import com.keyeswest.mvvmtoy.SortPreferenceEnum;
import com.keyeswest.mvvmtoy.adapters.TripClickListener;
import com.keyeswest.mvvmtoy.adapters.TripListAdapter;
import com.keyeswest.mvvmtoy.databinding.ListFragmentBinding;
import com.keyeswest.mvvmtoy.db.DataGenerator;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.utilities.SortResult;
import com.keyeswest.mvvmtoy.viewmodel.TripListViewModel;
import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;


import java.util.List;
import java.util.Objects;


import timber.log.Timber;

import static com.keyeswest.mvvmtoy.utilities.SnackbarHelper.showSnackbar;

public class TripListFragment extends Fragment implements TripClickListener {
    public static final String TAG = "TripListFragment";

    private static final String FILTER_STATE_EXTRA = "filterStateExtra";
    private static final int REQUEST_SORT_PREFERENCES = 10;
    private static final int REQUEST_FILTER_PREFERENCES = 20;

    private ListFragmentBinding mBinding;
    private TripListAdapter mTripListAdapter;
    private TripListViewModel mTripListViewModel;
    private boolean mListFiltered = false;

    private View mFragmentView;
    private DataRepository mDataRepository;


    private View.OnClickListener fabListener = (View v) -> {
        Timber.d("Fab clicked insert random trip");
        List<TripEntity> trips = DataGenerator.generateTrips(1);

        // Later this will start a new activity

        ((MainApp) Objects.requireNonNull(getActivity()).getApplication())
                .getRepository().insert(trips.get(0));

    };

    private Menu mMainMenu;



    // *********************  Lifecycle Event Handlers ***********************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Timber.d("Restoring mSelectedSegments and filter state after config change");

            mListFiltered = savedInstanceState.getByte(FILTER_STATE_EXTRA) != 0;
        }

        mDataRepository = ((MainApp) Objects.requireNonNull(getActivity()).getApplication())
                .getRepository();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Timber.d("OnCreateView");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container,
                false);

        mFragmentView = mBinding.getRoot();

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

        mTripListAdapter = new TripListAdapter(this);

        mBinding.tripsList.setAdapter(mTripListAdapter);

        mBinding.fab.setOnClickListener(fabListener);

        subscribeUi(mTripListViewModel);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_trip_list, menu);
        mMainMenu = menu;


        // Restore filter icon on configuration change
        if (mListFiltered) {
            Timber.d("Restoring filter icon on config change");
            MenuItem filterItem = mMainMenu.findItem(R.id.filter);
            filterItem.setIcon(R.drawable.filter_remove_outline);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState invoked");

        // save the filter state of the list
        savedInstanceState.putByte(FILTER_STATE_EXTRA, (byte) (mListFiltered ? 1 : 0));

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {

            case R.id.sort:

                intent = SortActivity.newIntent(getContext());
                startActivityForResult(intent, REQUEST_SORT_PREFERENCES);
                return true;
            case R.id.filter:
                intent = FilterActivity.newIntent(getContext(), mListFiltered);
                startActivityForResult(intent, REQUEST_FILTER_PREFERENCES);
                return true;

            case R.id.share:
                // shareTripList();
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_FILTER_PREFERENCES){
            boolean filterChanged = FilterActivity.getFilterChangedResult(data);
            if (filterChanged){
                MenuItem filterItem = mMainMenu.findItem(R.id.filter);
                boolean filtersCleared = FilterActivity.getFiltersClearedResult(data);
                if (filtersCleared) {
                    Timber.d("Filter result:  filters cleared");
                    mListFiltered = false;
                    filterItem.setIcon(R.drawable.filter_outline);
                    mTripListAdapter.setTripModels(mTripListViewModel.getAllTrips());
                } else{
                    Timber.d("Filter result:  filters set");
                    filterItem.setIcon(R.drawable.filter_remove_outline);
                    mListFiltered = true;
                    showFilterStatus(data);
                    mTripListAdapter.setTripModels(mTripListViewModel.filterTrips());
                }


            }
        } else if (requestCode == REQUEST_SORT_PREFERENCES){
            SortResult sortResult = SortActivity.getSortChangedResult(data);

            if (sortResult.isSortChanged()) {


                mTripListAdapter.setTripModels(mTripListViewModel.sort());
                showSnackbar(mFragmentView, getSortMessage(sortResult.getSelectedSort()),
                        Snackbar.LENGTH_SHORT);
            }

        }
    }


    // *********************  UI  Event Handlers ***********************************

    @Override
    public void onDeleteClick(TripEntity trip) {
        Timber.d("on delete clicked");

        mDataRepository.delete(trip);
    }

    @Override
    public void onTripClicked(TripViewModel model) {
        Timber.d("on trip clicked");
        mTripListViewModel.handleTripSelected(model);
    }

    @Override
    public void onFavoriteClick(TripEntity trip) {
        // flip the favorite status
        Timber.d("Favorite clicked");
        Timber.d("Current status %s", Boolean.toString(trip.isFavorite()));
        trip.setFavorite(!trip.isFavorite());
        Timber.d("New status %s", Boolean.toString(trip.isFavorite()));

        //update database which will update view
        mDataRepository.update(trip);

    }


    // *********************  private fragment methods ***********************************

    private String getSortMessage(SortPreferenceEnum selectedSort) {
        String result = getString(R.string.most_recent_sort);
        switch (selectedSort) {
            case NEWEST:
                result = getString(R.string.most_recent_sort);
                break;
            case OLDEST:
                result = getString(R.string.trips_ordered_oldest);
                break;
            case LONGEST:
                result = getString(R.string.trips_ordered_longest);
                break;
            case SHORTEST:
                result = getString(R.string.trips_ordered_shortest);
                break;
        }

        return result;
    }

    private void subscribeUi(TripListViewModel tripListViewModel) {
        // Update the list when the data changes


        // Note: this initial call to getTrips returns null because there are no observers
        // associated with the list of trips. An observer is registered with the observe method
        tripListViewModel.getTrips().observe(this, new Observer<List<TripEntity>>() {
            @Override
            public void onChanged(@Nullable List<TripEntity> trips) {
                if (trips != null) {
                    Timber.d("Updated trip data is available");

                    mBinding.setIsLoading(false);

                    //TODO This seems very inefficient, a better way perhaps?
                    List<TripViewModel> updatedList = tripListViewModel.updateTrips(trips);

                    // Assumption - this fragment is the view controller whose job is to update
                    // views, hence the call to the list adapter.

                    // TripListViewModel holds the data to be displayed in the list view but is
                    // not responsible for loading the adapter. Also why tis fragment handles the
                    // click event but then invokes TripListViewModel to update the UI views

                    //update the list view with the new trips
                    mTripListAdapter.setTripModels(updatedList);

                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }


    private void showFilterStatus(Intent data) {
        boolean dateFilter = FilterActivity.isDateFilterSet(data);
        boolean favoriteFilter = FilterActivity.isFavoriteFilterSet(data);
        String message = null;
        if (dateFilter && favoriteFilter) {
            message = getString(R.string.favorite_date_filter);
        } else if (dateFilter) {
            message = getString(R.string.date_filter);
        } else if (favoriteFilter) {
            message = getString(R.string.favorite_filter);
        }
        if (message != null) {
            showSnackbar(mFragmentView, message,
                    Snackbar.LENGTH_LONG);
        }
    }

}
