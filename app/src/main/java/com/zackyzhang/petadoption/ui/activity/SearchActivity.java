package com.zackyzhang.petadoption.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.EndlessRecyclerViewScrollListener;
import com.zackyzhang.petadoption.PetAdoptionPreferences;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.RecentQuery;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetListAdapter;
import com.zackyzhang.petadoption.ui.QueryListAdapter;
import com.zackyzhang.petadoption.ui.base.BasePresenterActivity;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.base.SearchContract;
import com.zackyzhang.petadoption.ui.presenter.SearchPresenter;
import com.zackyzhang.petadoption.ui.presenter.SearchPresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class SearchActivity extends BasePresenterActivity<SearchPresenter, SearchContract.View>
        implements SearchContract.View, PlaceSelectionListener, PetListAdapter.OnPetClickListener {

    private static final String TAG = "SearchActivity";

    private PetAdoptionPreferences myPreferences;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private PetListAdapter mSearchAdapter;
    private QueryListAdapter mQueryListAdapter;
    private SearchPresenter presenter;
    private String mZipCode;
    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.rv_container)
    ViewGroup resultsContainer;
    @BindView(R.id.pet_list)
    RecyclerView recyclerView;
    @BindView(R.id.rv_recent_search)
    RecyclerView recentSearchList;
    @BindView(R.id.place_autocomplete_search_input)
    EditText searchInput;
    @BindView(R.id.place_autocomplete_clear_button)
    View clearButton;
    @BindView(R.id.place_autocomplete_search_button)
    View searchButton;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_no_pets_found)
    TextView mNoPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupPlaceAutoComplete();
        setupRecyclerView();
        resultsContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getRecentQuery();
    }

    @NonNull
    @Override
    protected String tag() {
        return "SearchActivity";
    }

    @NonNull
    @Override
    protected PresenterFactory<SearchPresenter> getPresenterFactory() {
        myPreferences = PetAdoptionPreferences.getInstance(this);
        return new SearchPresenterFactory(myPreferences);
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull SearchPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPlaceSelected(Place place) {
        if (place == null || place.getLatLng() == null) {
            Toast.makeText(this, "no result from search!", Toast.LENGTH_SHORT).show();
        }
        Timber.tag(TAG).d(String.valueOf(place.getAddress()));
        mZipCode = ApiUtils.getZipCodeFromAddress(String.valueOf(place.getAddress()));
        presenter.setZipCode(mZipCode);
        mSearchAdapter.clearAdapter();
        loadingPets(String.valueOf(place.getName()));
    }

    @Override
    public void onError(Status status) {
        Timber.tag(TAG).d("onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadData(List<PetBean> pets) {
        if (pets == null) {
            Timber.tag(TAG).d("loadData() pets is null");
            mNoPets.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            clearButton.setVisibility(View.VISIBLE);
            return;
        }
        resultsContainer.setVisibility(View.VISIBLE);
        mSearchAdapter.setData(pets);
        clearButton.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    @Override
    public void loadRecentQuery(List<RecentQuery> recentQueryList) {
        mQueryListAdapter.setQueryList(recentQueryList);
    }

    private void loadingPets(String locationName) {
        resultsContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        presenter.loadPets();
        presenter.setRecentQuery(locationName, mZipCode);
    }

    private void loadingPetsFromQuery(String locationName, String type, String size, String sex, String age) {
        resultsContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        presenter.loadPetsFromQuery(mZipCode, type, size, sex, age);
        presenter.setRecentQuery(locationName, mZipCode);
    }

    private void setupPlaceAutoComplete() {
        placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchButton.setVisibility(View.GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchAdapter.clearAdapter();
                placeAutocompleteFragment.setText("");
                clearButton.setVisibility(View.GONE);
                resultsContainer.setVisibility(View.GONE);
                presenter.getRecentQuery();
            }
        });
        placeAutocompleteFragment.setHint(getString(R.string.auto_complete_hint));
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("US")
                .build();
        placeAutocompleteFragment.setFilter(typeFilter);
        placeAutocompleteFragment.setOnPlaceSelectedListener(this);

    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSearchAdapter = new PetListAdapter(this, false);
        mSearchAdapter.setOnPetClickListener(this);
        recyclerView.setAdapter(mSearchAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchMoreData();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        recentSearchList.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recentSearchList.addItemDecoration(itemDecoration);
        mQueryListAdapter = new QueryListAdapter(this);
        recentSearchList.setAdapter(mQueryListAdapter);
    }

    public void clickRecentQuery(String address, String zipCode, String type, String size, String sex, String age) {
        mZipCode = zipCode;
        loadingPetsFromQuery(address, type, size, sex, age);
        clearButton.setVisibility(View.VISIBLE);
        searchInput.setText(address);
    }

    @OnClick(R.id.id_filter)
    public void filter() {
        startActivity(new Intent(this, SearchFilterActivity.class));
    }

    @OnClick({ R.id.id_searchback })
    protected void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @OnClick(R.id.clear_recent_query)
    protected void clearRecentSearch() {
        presenter.clearRecentSearch();
        mQueryListAdapter.clearQueryList();
    }

    @Override
    public void onItemClick(PetBean pet) {
        Intent intent = PetDetailActivity.newIntent(this, pet);
        startActivity(intent);
    }

}
