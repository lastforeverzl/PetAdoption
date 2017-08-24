package com.zackyzhang.petadoption.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.ShelterBean;
import com.zackyzhang.petadoption.ui.base.BasePresenterActivity;
import com.zackyzhang.petadoption.ui.base.PetDetailContract;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.presenter.PetDetailPresenter;
import com.zackyzhang.petadoption.ui.presenter.PetDetailPresenterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lei on 8/10/17.
 */

public class PetDetailActivity extends BasePresenterActivity<PetDetailPresenter, PetDetailContract.View>
        implements PetDetailContract.View, BaseSliderView.OnSliderClickListener {
    private static final String TAG = "PetDetailActivity";

    public static final String EXTRA_PET_DETAIL = "com.zackyzhang.petadoption.ui.activity.petdetail";
    private PetDetailPresenter presenter;
    private PetBean mPet;
    private ShelterBean mShelter;
    private List<String> mUrls;
    private boolean isInFavorite = false;
    private MenuItem favoriteIcon;

    @BindView(R.id.photo_slider)
    SliderLayout mSliderLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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

    public static Intent newIntent(Context context, PetBean pet) {
        Intent intent = new Intent(context, PetDetailActivity.class);
        intent.putExtra(EXTRA_PET_DETAIL, pet);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petdetail);
        ButterKnife.bind(this);

        mPet = getIntent().getParcelableExtra(EXTRA_PET_DETAIL);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        setupSlider();
        setupPetInfo();
        setupTransitionName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pet_detail, menu);
        favoriteIcon = menu.findItem(R.id.favorite_pet);
        if (isInFavorite) {
            favoriteIcon.setIcon(R.drawable.ic_favorite_white_24px);
        } else {
            favoriteIcon.setIcon(R.drawable.ic_favorite_border_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            case R.id.favorite_pet:
                if (isInFavorite) {
                    presenter.removeFavorite();
                    favoriteIcon.setIcon(R.drawable.ic_favorite_border_24dp);
                } else {
                    presenter.addFavorite();
                    favoriteIcon.setIcon(R.drawable.ic_favorite_white_24px);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setShelterId(mPet.getShelterId());
        presenter.checkFavorite(mPet);
    }

    @Override
    protected void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }

    @NonNull
    @Override
    protected String tag() {
        return "PetDetailActivity";
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

    @Override
    public void loadData(ShelterBean shelter) {
        this.mShelter = shelter;
        setupShelterInfo();
    }

    @Override
    public Context getActivityContext() {
        return PetDetailActivity.this;
    }

    @Override
    public void notInFavorite() {
        isInFavorite = false;
    }

    @Override
    public void inFavorite() {
        isInFavorite = true;
    }

    private void setupTransitionName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSliderLayout.setTransitionName(getString(R.string.image_transition_name));
        }
    }

    private void setupSlider() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSliderLayout.setTransitionName(getString(R.string.image_transition_name));
        }
        mUrls = ApiUtils.getPhotoUrls(mPet);
        if (mUrls.isEmpty()) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView
                    .image(R.drawable.no_image_placeholder)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            mSliderLayout.addSlider(sliderView);
        }
        for (String url : mUrls) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = mSliderLayout.getCurrentPosition();
        Intent intent = GalleryActivity.newIntent(this, (ArrayList) mUrls, position);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                    PetDetailActivity.this, mSliderLayout, mSliderLayout.getTransitionName())
                    .toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    @OnClick(R.id.bt_get_direction)
    public void getDirection() {
        String lat = mShelter.getLatitude();
        String lng = mShelter.getLongitude();
        String address = ApiUtils.shelterAddress(mShelter);
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @OnClick(R.id.bt_email)
    public void sendEmail(View view) {
        String email = mShelter.getEmail();
        if (email != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.shelter_phone)
    public void callShelter() {
        Uri call = Uri.parse("tel:" + mShelter.getPhone().trim());
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(intent);
    }
}

