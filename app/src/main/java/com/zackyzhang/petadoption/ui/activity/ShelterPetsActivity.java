package com.zackyzhang.petadoption.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.ShelterContactListener;
import com.zackyzhang.petadoption.ui.fragment.ShelterPetsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class ShelterPetsActivity extends AppCompatActivity
        implements PetOnClickHandler, ShelterContactListener {

    private static final String TAG = "ShelterPetsActivity";
    private static final String EXTRA_SHELTER_ID = "com.zackyzhang.petadoption.ui.activity.shelterid";
    private static final String EXTRA_SHELTER_NAME = "com.zackyzhang.petadoption.ui.activity.sheltername";
    private static final String EXTRA_SHELTER_PHONE = "com.zackyzhang.petadoption.ui.activity.shelterphone";
    private static final String EXTRA_SHELTER_EMAIL = "com.zackyzhang.petadoption.ui.activity.shelteremail";
    private static final String EXTRA_SHELTER_LAT = "com.zackyzhang.petadoption.ui.activity.shelterlat";
    private static final String EXTRA_SHELTER_LNG = "com.zackyzhang.petadoption.ui.activity.shelterlng";
    private static final String EXTRA_SHELTER_ADDRESS = "com.zackyzhang.petadoption.ui.activity.shelteraddress";
    private final String SHELTER_PETS_FRAGMENT_TAG = "ShelterPetsFragmentTAG";

    private String mShelterId;
    private String mShelterName;
    private String mShelterPhone;
    private String mShelterEmail;
    private String mShelterLat;
    private String mShelterLng;
    private String mShelterAddress;

    private ShelterPetsFragment mShelterPetsFragment;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        mShelterPetsFragment = (ShelterPetsFragment) fragmentManager.findFragmentByTag(SHELTER_PETS_FRAGMENT_TAG);
        if (mShelterPetsFragment == null) {
            mShelterPetsFragment = ShelterPetsFragment.newInstance(
                    mShelterId,
                    mShelterName,
                    mShelterPhone,
                    mShelterEmail,
                    mShelterLat,
                    mShelterLng,
                    mShelterAddress);
        }
        fragmentManager.beginTransaction()
                .addToBackStack(SHELTER_PETS_FRAGMENT_TAG)
                .replace(R.id.shelter_pets_fragment, mShelterPetsFragment, SHELTER_PETS_FRAGMENT_TAG)
                .commit();
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

    @Override
    public void clickShelter(String id, String name, String phone, String email, String lat, String lng, String address) {

    }

    @Override
    public void callShelter(String number) {
        Uri call = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(intent);
    }

    @Override
    public void directToShelter(String lat, String lng, String address) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void emailShelter(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(PetBean pet, View transitionView) {
        Intent intent = PetDetailActivity.newIntent(this, pet);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                    ShelterPetsActivity.this, transitionView, transitionView.getTransitionName())
                    .toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }
}
