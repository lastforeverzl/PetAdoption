package com.zackyzhang.petadoption.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.ShelterBean;
import com.zackyzhang.petadoption.ui.base.BasePresenterFragment;
import com.zackyzhang.petadoption.ui.base.PetDetailContract;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.ShelterContactListener;
import com.zackyzhang.petadoption.ui.presenter.PetDetailPresenter;
import com.zackyzhang.petadoption.ui.presenter.PetDetailPresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by lei on 8/22/17.
 */

public class PetDetailFragment extends BasePresenterFragment<PetDetailPresenter, PetDetailContract.View>
        implements PetDetailContract.View, BaseSliderView.OnSliderClickListener {
    private static final String TAG = "PetDetailFragment";
    private static final String ARG_ANIMAL = "arg_animal";

    private Unbinder mUnbinder;
    private PetBean mPet;
    private PetDetailPresenter presenter;
    private boolean isInFavorite = false;
    private List<String> mUrls;
    private ShelterBean mShelter;

    @BindView(R.id.photo_slider)
    SliderLayout mSliderLayout;
    @BindView(R.id.animal_name)
    TextView petName;
    @BindView(R.id.animal_info)
    TextView petInfo;
    @BindView(R.id.animal_adoptable)
    TextView petAdoptable;
    @BindView(R.id.animal_update_date)
    TextView petUpdateDate;
    @BindView(R.id.animal_description)
    TextView petDescription;
    @BindView(R.id.shelter_name)
    TextView shelterName;
    @BindView(R.id.shelter_address)
    TextView shelterAddress;
    @BindView(R.id.shelter_phone)
    TextView shelterPhone;
    @BindView(R.id.bt_email)
    Button email;
    @BindView(R.id.fab_favorite)
    FloatingActionButton mFavoriteButton;

    private PetDetailPaneCallback mSliderClickHandler;
    private ShelterContactListener mShelterContactListener;

    public interface PetDetailPaneCallback {
        void onSliderClick(List<String> urls, int position);
    }

    public static PetDetailFragment newInstance(PetBean pet) {
        PetDetailFragment fragment = new PetDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ANIMAL, pet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PetDetailPaneCallback) {
            mSliderClickHandler = (PetDetailPaneCallback) context;
        }
        if (context instanceof ShelterContactListener) {
            mShelterContactListener = (ShelterContactListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPet = getArguments().getParcelable(ARG_ANIMAL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setShelterId(mPet.getShelterId());
        presenter.checkFavorite(mPet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_petdetail, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        setupSlider();
        setupPetInfo();
        return root;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void loadData(ShelterBean shelter) {
        this.mShelter = shelter;
        Timber.tag(TAG).d(mShelter.getName());
        if (shelter != null)
            setupShelterInfo();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void notInFavorite() {
        isInFavorite = false;
        mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_24dp);
    }

    @Override
    public void inFavorite() {
        isInFavorite = true;
        mFavoriteButton.setImageResource(R.drawable.ic_favorite_white_24px);
    }

    @NonNull
    @Override
    protected String tag() {
        return "PetDetailFragment";
    }

    @NonNull
    @Override
    protected PresenterFactory<PetDetailPresenter> getPresenterFactory() {
        return new PetDetailPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull PetDetailPresenter presenter) {
        this.presenter = presenter;
    }

    private void setupSlider() {
        mUrls = ApiUtils.getPhotoUrls(mPet);
        if (mUrls.isEmpty()) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView
                    .image(R.drawable.no_image_placeholder)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mSliderLayout.addSlider(sliderView);
        }
        for (String url : mUrls) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            sliderView
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mSliderLayout.addSlider(sliderView);
        }
        mSliderLayout.stopAutoCycle();
    }

    private void setupPetInfo() {
        petName.setText(mPet.getName());
        petInfo.setText(ApiUtils.getPetInfo(mPet));
        if (!ApiUtils.getAdoptionStatus(mPet).isEmpty()) {
            petAdoptable.setText(ApiUtils.getAdoptionStatus(mPet));
        } else {
            petAdoptable.setVisibility(View.GONE);
        }
        if (ApiUtils.getLastUpdate(mPet) != null) {
            String date = "Update at " + ApiUtils.getLastUpdate(mPet);
            petUpdateDate.setText(date);
        } else {
            petUpdateDate.setVisibility(View.GONE);
        }
        if (mPet.getDescription() != null)
            petDescription.setText(mPet.getDescription());
        else
            petDescription.setVisibility(View.GONE);
    }

    private void setupShelterInfo() {
        if (mShelter.getName() != null)
            shelterName.setText(mShelter.getName());
        else
            shelterName.setVisibility(View.GONE);
        if (mShelter.getAddress1() != null || mShelter.getAddress2() != null)
            shelterAddress.setText(ApiUtils.shelterAddress(mShelter));
        else
            shelterAddress.setVisibility(View.GONE);
        if (mShelter.getPhone() != null)
            shelterPhone.setText(mShelter.getPhone().trim());
        else
            shelterPhone.setVisibility(View.GONE);
        if (mShelter.getEmail() == null)
            email.setBackgroundResource(R.drawable.pet_detail_button_gray);
    }

    @OnClick(R.id.fab_favorite)
    public void clickFavoriteButton(FloatingActionButton view) {
        if (isInFavorite) {
            presenter.removeFavorite();
            view.setImageResource(R.drawable.ic_favorite_border_24dp);
        } else {
            presenter.addFavorite();
            view.setImageResource(R.drawable.ic_favorite_white_24px);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = mSliderLayout.getCurrentPosition();
        mSliderClickHandler.onSliderClick(mUrls, position);
    }

    @OnClick(R.id.bt_get_direction)
    public void getDirection() {
        String lat = mShelter.getLatitude();
        String lng = mShelter.getLongitude();
        String address = ApiUtils.shelterAddress(mShelter);
        mShelterContactListener.directToShelter(lat, lng, address);
    }

    @OnClick(R.id.bt_email)
    public void sendEmail() {
        String email = mShelter.getEmail();
        Timber.tag(TAG).d("email: " + email);
        if (email != null) {
            mShelterContactListener.emailShelter(email);
        }
    }

    @OnClick(R.id.shelter_phone)
    public void callShelter() {
        mShelterContactListener.callShelter(ApiUtils.cleanPhoneNumber(mShelter.getPhone()));
    }
}
