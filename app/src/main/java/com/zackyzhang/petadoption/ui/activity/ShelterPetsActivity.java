package com.zackyzhang.petadoption.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zackyzhang.petadoption.EndlessRecyclerViewScrollListener;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetListAdapter;
import com.zackyzhang.petadoption.ui.base.BasePresenterActivity;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.base.ShelterPetsContract;
import com.zackyzhang.petadoption.ui.presenter.ShelterPetsPresenter;
import com.zackyzhang.petadoption.ui.presenter.ShelterPetsPresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class ShelterPetsActivity
        extends BasePresenterActivity<ShelterPetsPresenter, ShelterPetsContract.View>
        implements ShelterPetsContract.View,
        PetListAdapter.OnPetClickListener,
        PetListAdapter.OnHeaderClickListener {
    private static final String TAG = "ShelterPetsActivity";
    private static final String EXTRA_SHELTER_ID = "com.zackyzhang.petadoption.ui.activity.shelterid";
    private static final String EXTRA_SHELTER_NAME = "com.zackyzhang.petadoption.ui.activity.sheltername";
    private static final String EXTRA_SHELTER_PHONE = "com.zackyzhang.petadoption.ui.activity.shelterphone";
    private static final String EXTRA_SHELTER_EMAIL = "com.zackyzhang.petadoption.ui.activity.shelteremail";
    private static final String EXTRA_SHELTER_LAT = "com.zackyzhang.petadoption.ui.activity.shelterlat";
    private static final String EXTRA_SHELTER_LNG = "com.zackyzhang.petadoption.ui.activity.shelterlng";
    private static final String EXTRA_SHELTER_ADDRESS = "com.zackyzhang.petadoption.ui.activity.shelteraddress";

    private String mShelterId;
    private String mShelterName;
    private String mShelterPhone;
    private String mShelterEmail;
    private String mShelterLat;
    private String mShelterLng;
    private String mShelterAddress;
    private List<PetBean> mList;
    private PetListAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ShelterPetsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_shelter_pets)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_pets_found)
    TextView mNoPets;

    public static Intent newIntent(Context context, String id, String name, String phone, String email, String lat, String lng, String address) {
        Intent intent = new Intent(context, ShelterPetsActivity.class);
        intent.putExtra(EXTRA_SHELTER_ID, id);
        intent.putExtra(EXTRA_SHELTER_NAME, name);
        intent.putExtra(EXTRA_SHELTER_PHONE, phone);
        intent.putExtra(EXTRA_SHELTER_EMAIL, email);
        intent.putExtra(EXTRA_SHELTER_LAT, lat);
        intent.putExtra(EXTRA_SHELTER_LNG, lng);
        intent.putExtra(EXTRA_SHELTER_ADDRESS, address);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_pets);
        ButterKnife.bind(this);

        mShelterId = getIntent().getStringExtra(EXTRA_SHELTER_ID);
        mShelterName = getIntent().getStringExtra(EXTRA_SHELTER_NAME);
        mShelterPhone = getIntent().getStringExtra(EXTRA_SHELTER_PHONE);
        mShelterEmail = getIntent().getStringExtra(EXTRA_SHELTER_EMAIL);
        mShelterLat = getIntent().getStringExtra(EXTRA_SHELTER_LAT);
        mShelterLng = getIntent().getStringExtra(EXTRA_SHELTER_LNG);
        mShelterAddress = getIntent().getStringExtra(EXTRA_SHELTER_ADDRESS);
        Timber.tag(TAG).d(mShelterId + " " + mShelterName + " " + mShelterPhone + " " + mShelterEmail + " " + mShelterLat + " " + mShelterLng);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mShelterName);

        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setShelterId(mShelterId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PetListAdapter(this, true);
        mAdapter.setHeader(mShelterPhone, mShelterEmail, mShelterLat, mShelterLng, mShelterAddress);
        mAdapter.setOnPetClickListener(this);
        mAdapter.setOnHeaderClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchMoreData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    protected String tag() {
        return "ShelterPetsActivity";
    }

    @NonNull
    @Override
    protected PresenterFactory<ShelterPetsPresenter> getPresenterFactory() {
        return new ShelterPetsPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull ShelterPetsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadData(List<PetBean> pets) {
        if (pets == null) {
            mNoPets.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mList = pets;
        mAdapter.setData(pets);
    }

    @Override
    public void onItemClick(PetBean pet) {
        Intent intent = PetDetailActivity.newIntent(this, pet);
        startActivity(intent);
    }

    @Override
    public void onCallClick(String number) {
        Uri call = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(intent);
    }

    @Override
    public void onEmailClick(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onDirectionClick(String lat, String lng, String address) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
