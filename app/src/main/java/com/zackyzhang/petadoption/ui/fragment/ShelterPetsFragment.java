package com.zackyzhang.petadoption.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zackyzhang.petadoption.EndlessRecyclerViewScrollListener;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetListAdapter;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.base.BasePresenterFragment;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.ShelterContactListener;
import com.zackyzhang.petadoption.ui.base.ShelterPetsContract;
import com.zackyzhang.petadoption.ui.presenter.ShelterPetsPresenter;
import com.zackyzhang.petadoption.ui.presenter.ShelterPetsPresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lei on 8/22/17.
 */

public class ShelterPetsFragment extends BasePresenterFragment<ShelterPetsPresenter, ShelterPetsContract.View>
        implements ShelterPetsContract.View, PetListAdapter.OnHeaderClickListener, PetListAdapter.OnPetClickListener {

    private static final String EXTRA_SHELTER_ID = "com.zackyzhang.petadoption.ui.fragment.shelterid";
    private static final String EXTRA_SHELTER_NAME = "com.zackyzhang.petadoption.ui.fragment.sheltername";
    private static final String EXTRA_SHELTER_PHONE = "com.zackyzhang.petadoption.ui.fragment.shelterphone";
    private static final String EXTRA_SHELTER_EMAIL = "com.zackyzhang.petadoption.ui.fragment.shelteremail";
    private static final String EXTRA_SHELTER_LAT = "com.zackyzhang.petadoption.ui.fragment.shelterlat";
    private static final String EXTRA_SHELTER_LNG = "com.zackyzhang.petadoption.ui.fragment.shelterlng";
    private static final String EXTRA_SHELTER_ADDRESS = "com.zackyzhang.petadoption.ui.fragment.shelteraddress";

    private Unbinder mUnbinder;
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
    private ShelterContactListener mShelterContactListener;
    private PetOnClickHandler mPetOnClickHandler;

    @BindView(R.id.rv_shelter_pets)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_pets_found)
    TextView mNoPets;

    public static ShelterPetsFragment newInstance(String id, String name, String phone, String email, String lat, String lng, String address) {
        ShelterPetsFragment fragment = new ShelterPetsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_SHELTER_ID, id);
        args.putString(EXTRA_SHELTER_NAME, name);
        args.putString(EXTRA_SHELTER_PHONE, phone);
        args.putString(EXTRA_SHELTER_EMAIL, email);
        args.putString(EXTRA_SHELTER_LAT, lat);
        args.putString(EXTRA_SHELTER_LNG, lng);
        args.putString(EXTRA_SHELTER_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShelterContactListener) {
            mShelterContactListener = (ShelterContactListener) context;
        }
        if (context instanceof PetOnClickHandler) {
            mPetOnClickHandler = (PetOnClickHandler) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShelterId = getArguments().getString(EXTRA_SHELTER_ID);
            mShelterName = getArguments().getString(EXTRA_SHELTER_NAME);
            mShelterPhone = getArguments().getString(EXTRA_SHELTER_PHONE);
            mShelterEmail = getArguments().getString(EXTRA_SHELTER_EMAIL);
            mShelterLat = getArguments().getString(EXTRA_SHELTER_LAT);
            mShelterLng = getArguments().getString(EXTRA_SHELTER_LNG);
            mShelterAddress = getArguments().getString(EXTRA_SHELTER_ADDRESS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shelter_pets, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        setupRecyclerView();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setShelterId(mShelterId);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PetListAdapter(getActivity(), true);
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

    @NonNull
    @Override
    protected String tag() {
        return "ShelterPetsFragment";
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
    public void onCallClick(String number) {
        mShelterContactListener.callShelter(number);
    }

    @Override
    public void onEmailClick(String email) {
        mShelterContactListener.emailShelter(email);
    }

    @Override
    public void onDirectionClick(String lat, String lng, String address) {
        mShelterContactListener.directToShelter(lat, lng, address);
    }

    @Override
    public void onItemClick(PetBean pet) {
        mPetOnClickHandler.onItemClick(pet);
    }
}
