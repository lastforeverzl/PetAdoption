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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.EndlessRecyclerViewScrollListener;
import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.GoogleApiHelper;
import com.zackyzhang.petadoption.api.model.ShelterBean;
import com.zackyzhang.petadoption.ui.base.BasePresenterFragment;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.ShelterContactListener;
import com.zackyzhang.petadoption.ui.base.SheltersContract;
import com.zackyzhang.petadoption.ui.presenter.SheltersPresenter;
import com.zackyzhang.petadoption.ui.presenter.SheltersPresenterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by lei on 8/11/17.
 */

public class SheltersFragment extends BasePresenterFragment<SheltersPresenter, SheltersContract.View> implements SheltersContract.View {
    private static final String TAG = "SheltersFragment";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_ZIPCODE = "arg_zipcode";

    private String mZipCode;
    private SheltersPresenter presenter;
    private SheltersAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView mRecyclerView;
    private TextView noShelters;
    private Location mLastLocation;
    private GoogleApiHelper mGoogleApiHelper;
    private GoogleApiClient mGoogleApiClient;
    private double[] currentLatLng;

    private ShelterContactListener mShelterContactListener;

    public static SheltersFragment newInstance() {
        SheltersFragment fragment = new SheltersFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShelterContactListener) {
            mShelterContactListener = (ShelterContactListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG).d("onCreate");
        mGoogleApiHelper = MyApplication.getGoogleApiHelper();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shelters, container, false);
        mRecyclerView = root.findViewById(R.id.id_recyclerview);
        noShelters = root.findViewById(R.id.tv_no_shelters_found);

        setupRecyclerView();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(TAG).d("onDestroy");
    }

    @NonNull
    @Override
    protected String tag() {
        return "SheltersFragment";
    }

    @NonNull
    @Override
    protected PresenterFactory getPresenterFactory() {
        return new SheltersPresenterFactory(mZipCode);
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull SheltersPresenter presenter) {
        Timber.tag(TAG).d("presenter created");
        this.presenter = presenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.tag(TAG).d("onRequestPermissionsResult");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag(TAG).d("permission granted");
                    sendPresenterZipCode();
                } else {
                    Timber.tag(TAG).d("permission denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void googleClientConnected() {
        Timber.tag(TAG).d("googleClientConnected");
        if (mZipCode == null)
            checkPermission();
    }

    private void sendPresenterZipCode() {
        mGoogleApiClient = mGoogleApiHelper.getGoogleApiClient();
        if (!mGoogleApiClient.isConnected())
            return;
        currentLatLng = getCurrentLocation(mGoogleApiClient);
        Timber.tag(TAG).d("currentLatLng: " + currentLatLng[0] + currentLatLng[1]);
        mZipCode = getZipCode(currentLatLng);
        presenter.setZipCode(mZipCode);
//        presenter.setZipCode("94568"); // for test on emulator
    }

    private String getZipCode(double[] latLng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng[0], latLng[1], 1);
            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private double[] getCurrentLocation(GoogleApiClient googleApiClient) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability == null) {
            Timber.tag(TAG).d("locationAvailability is null");
        }
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

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SheltersAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchMoreData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void loadData(List<ShelterBean> shelters) {
        if (shelters == null) {
            noShelters.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mAdapter.setData(shelters);
    }

    @Override
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            Timber.tag(TAG).d("onLocationApiConnected if");
            return;
        }
        sendPresenterZipCode();
    }

    public void scrollToTopCallback() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    class SheltersAdapter extends RecyclerView.Adapter<SheltersAdapter.Holder> {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<ShelterBean> mList;

        public SheltersAdapter(Context context) {
            mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.shelter_item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ShelterBean shelter = mList.get(position);
            holder.shelterName.setText(shelter.getName());
            if (!ApiUtils.shelterAddress(shelter).equals("")) {
                holder.shelterAddress.setText(ApiUtils.shelterAddress(shelter));
            } else {
                holder.shelterAddress.setText("");
            }
            if (shelter.getPhone() != null) {
                holder.shelterPhone.setText(shelter.getPhone().trim());
                holder.imShelterCall.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.tvShelterCall.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                holder.shelterPhone.setText("");
                holder.imShelterCall.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
                holder.tvShelterCall.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
            }
            if (shelter.getEmail() != null) {
                holder.shelterEmail.setText(shelter.getEmail());
            } else {
                holder.shelterEmail.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public void setData(List<ShelterBean> data) {
            mList = data;
            Timber.tag(TAG).d("adapter mList size: " + mList.size());
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {

            @BindView(R.id.shelter_name)
            TextView shelterName;
            @BindView(R.id.shelter_address)
            TextView shelterAddress;
            @BindView(R.id.shelter_phone)
            TextView shelterPhone;
            @BindView(R.id.shelter_email)
            TextView shelterEmail;
            @BindView(R.id.shelter_direction)
            TextView shelterDirection;
            @BindView(R.id.im_shelter_call)
            ImageView imShelterCall;
            @BindView(R.id.tv_shelter_call)
            TextView tvShelterCall;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.shelter_layout)
            public void onClickShelter() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                String address = ApiUtils.shelterAddress(shelter);
                mShelterContactListener.clickShelter(shelter.getId(),
                        shelter.getName(),
                        shelter.getPhone(),
                        shelter.getEmail(),
                        shelter.getLatitude(),
                        shelter.getLongitude(),
                        address);
            }

            @OnClick(R.id.shelter_call)
            public void onClickCall() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                if (shelter.getPhone() != null) {
                    String number = ApiUtils.cleanPhoneNumber(shelter.getPhone());
                    mShelterContactListener.callShelter(number);
                }
            }

            @OnClick(R.id.shelter_direction)
            public void onClickDirection() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                String address = ApiUtils.shelterAddress(shelter);
                if (shelter.getLatitude() != null && shelter.getLongitude() != null) {
                    mShelterContactListener.directToShelter(shelter.getLatitude(), shelter.getLongitude(), address);
                }
            }

        }
    }
}
