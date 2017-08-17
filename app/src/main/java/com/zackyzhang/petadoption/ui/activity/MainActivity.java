package com.zackyzhang.petadoption.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.base.BasePresenterActivity;
import com.zackyzhang.petadoption.ui.base.MainActivityContract;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.fragment.FavoriteFragment;
import com.zackyzhang.petadoption.ui.fragment.SheltersFragment;
import com.zackyzhang.petadoption.ui.fragment.ViewPagerFragment;
import com.zackyzhang.petadoption.ui.presenter.MainPresenter;
import com.zackyzhang.petadoption.ui.presenter.MainPresenterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BasePresenterActivity<MainPresenter, MainActivityContract.View>
        implements MainActivityContract.View, PetOnClickHandler, SheltersFragment.ShelterOnClickHandler {
    private static final String TAG = "MainActivity";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String VIEWPAGER_FRAGMENT_TAG = "ViewPagerFragmentTAG";
    private final String SHELTER_FRAGMENT_TAG = "ShelterFragmentTAG";
    private final String FAVORITE_FRAGMENT_TAG = "FavoriteFragmentTAG";
//    private static final int LOADER_ID = 101;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottomNavigationBar)
    BottomBar mBottomBar;

//    private MainActivityContract.Presenter presenter;
    private FragmentManager mFragmentManager;
    private ViewPagerFragment viewPagerFragment;
    private SheltersFragment mSheltersFragment;
    private FavoriteFragment mFavoriteFragment;
    private Location mLastLocation;
    private String currentZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mFragmentManager = getSupportFragmentManager();
        setupBottomBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getGoogleApiHelper().connect();
//        setupBottomBar();
        onTabItemSelected(mBottomBar.getCurrentTabId());
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getGoogleApiHelper().disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                View searchMenuView = mToolbar.findViewById(R.id.menu_search);
                Intent intent = new Intent(this, SearchActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, searchMenuView,
                            getString(R.string.transition_search_back)).toBundle();
                    startActivity(intent, options);
                } else  startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected String tag() {
        return "MainActivity";
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return new MainPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull MainPresenter presenter) {

    }

    @Override
    public void loadZipCode(String zipCode) {
        Timber.tag(TAG).d("zipCode: " + zipCode);
        currentZipCode = zipCode;
        onTabItemSelected(mBottomBar.getCurrentTabId());
    }

    @Override
    public double[] getCurrentLocation(GoogleApiClient googleApiClient) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return null;
        }
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        double[] currentLatLng = new double[2];
        if (locationAvailability != null && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mLastLocation != null) {
                currentLatLng[0] = mLastLocation.getLatitude();
                currentLatLng[1] = mLastLocation.getLongitude();
            }
        }
        return currentLatLng;
    }

    @Override
    public String getZipCode(double[] latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng[0], latLng[1], 1);
            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "LOCATION_PERMISSION Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "LOCATION_PERMISSION Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupBottomBar() {
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                onTabItemSelected(tabId);
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_nearby) {
                    // The tab with id R.id.tab_favorites was reselected,
                    // change your content accordingly.
                }
            }
        });
    }

    private void onTabItemSelected(int id) {
        switch (id) {
            case R.id.tab_nearby:
                if (currentZipCode == null)
                    return;
                viewPagerFragment = (ViewPagerFragment)
                        mFragmentManager.findFragmentByTag(VIEWPAGER_FRAGMENT_TAG);
                if (viewPagerFragment == null) {
                    viewPagerFragment = ViewPagerFragment.newInstance(currentZipCode);
                }
                mFragmentManager.beginTransaction()
                        .addToBackStack(VIEWPAGER_FRAGMENT_TAG)
                        .replace(R.id.contentContainer, viewPagerFragment, VIEWPAGER_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.tab_favorites:
                mFavoriteFragment = (FavoriteFragment)
                        mFragmentManager.findFragmentByTag(FAVORITE_FRAGMENT_TAG);
                if (mFavoriteFragment == null) {
                    mFavoriteFragment = FavoriteFragment.newInstance();
                }
                mFragmentManager.beginTransaction()
                        .addToBackStack(FAVORITE_FRAGMENT_TAG)
                        .replace(R.id.contentContainer, mFavoriteFragment, FAVORITE_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.tab_shelters:
                mSheltersFragment = (SheltersFragment)
                        mFragmentManager.findFragmentByTag(SHELTER_FRAGMENT_TAG);
                if (mSheltersFragment == null) {
                    mSheltersFragment = SheltersFragment.newInstance(currentZipCode);
                }
                mFragmentManager.beginTransaction()
                        .addToBackStack(SHELTER_FRAGMENT_TAG)
                        .replace(R.id.contentContainer, mSheltersFragment, SHELTER_FRAGMENT_TAG)
                        .commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(PetBean pet) {
        Timber.tag(TAG).d("pet name: " + pet.getName());
        Intent intent = PetDetailActivity.newIntent(this, pet);
        startActivity(intent);
    }

    @Override
    public void onClickShelter(String id, String name, String phone, String email, String lat, String lng, String address) {
        Timber.tag(TAG).d("shelter id: " + id);
        Intent intent = ShelterPetsActivity.newIntent(this, id, name, phone, email, lat, lng, address);
        startActivity(intent);
    }

    @Override
    public void onClickCall(String number) {
        Uri call = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(intent);
    }

    @Override
    public void onClickDirection(String lat, String lng, String address) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
