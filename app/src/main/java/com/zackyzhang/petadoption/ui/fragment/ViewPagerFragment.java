package com.zackyzhang.petadoption.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.GoogleApiHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by lei on 8/8/17.
 */

public class ViewPagerFragment extends Fragment {
    private static final String TAG = "ViewPagerFragment";
    private static final String ARG_ZIPCODE = "arg_zipcode";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Context mContext;
    private Unbinder mUnbinder;
    private static String[] animalType;
    private String mZipCode;
    private Location mLastLocation;
    private GoogleApiHelper mGoogleApiHelper;
    private GoogleApiClient mGoogleApiClient;
    private double[] currentLatLng;

    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.sliding_tabs)
    TabLayout mSlidingTabs;

    public static ViewPagerFragment newInstance() {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Timber.tag(TAG).d("viewpager newInstance");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        animalType = getActivity().getResources().getStringArray(R.array.animals);
        Timber.tag(TAG).d("onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        Timber.tag(TAG).d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(TAG).d("onDestroy");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        mGoogleApiHelper = MyApplication.getGoogleApiHelper();
        mGoogleApiClient = mGoogleApiHelper.getGoogleApiClient();
        if (mZipCode != null) {
            setupView();
        } else {
            if (mGoogleApiClient.isConnected()) {
                permissionRequest();
            }
        }
        return root;
    }

    public void googleClientConnected(Context context) {
        Timber.tag(TAG).d("googleClientConnected");
        mContext = context;
        if (mZipCode == null) {
            permissionRequest();
        }
    }

    private void setupView() {
        pager.setAdapter(buildAdapter());
        mSlidingTabs.setupWithViewPager(pager);
    }

    private void getZipCode() {
        if (!mGoogleApiClient.isConnected()) {
            Timber.tag(TAG).d("getZipCode mGoogleApiClient not connected");
            return;
        }
        currentLatLng = getCurrentLocation(mGoogleApiClient);
        mZipCode = getZipCode(currentLatLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag(TAG).d("permission granted");
                    getZipCode();
                    setupView();
                } else {
                    Timber.tag(TAG).d("permission denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private PagerAdapter buildAdapter() {
        return new PagerAdapter(getActivity(), getChildFragmentManager());
    }

    public void permissionRequest() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        getZipCode();
        setupView();
    }

    public String getZipCode(double[] latLng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng[0], latLng[1], 1);
            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double[] getCurrentLocation(GoogleApiClient googleApiClient) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public class PagerAdapter extends FragmentPagerAdapter {

        Context ctxt = null;

        public PagerAdapter(Context ctxt, FragmentManager mgr) {
            super(mgr);
            this.ctxt = ctxt;
        }

        @Override
        public int getCount() {
            return animalType.length;
        }

        @Override
        public Fragment getItem(int position) {
            Timber.tag(TAG).d(mZipCode);
            return TabFragment.newInstance(animalType[position], mZipCode);
//            return TabFragment.newInstance(animalType[position], "94568"); // For test on emulator
        }

        @Override
        public String getPageTitle(int position) {
            return animalType[position];
        }
    }
}
