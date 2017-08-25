package com.zackyzhang.petadoption.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.GoogleApiHelper;
import com.zackyzhang.petadoption.api.LocationCallback;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.ShelterContactListener;
import com.zackyzhang.petadoption.ui.fragment.FavoriteFragment;
import com.zackyzhang.petadoption.ui.fragment.PetDetailFragment;
import com.zackyzhang.petadoption.ui.fragment.ShelterPetsFragment;
import com.zackyzhang.petadoption.ui.fragment.SheltersFragment;
import com.zackyzhang.petadoption.ui.fragment.ViewPagerFragment;
import com.zackyzhang.petadoption.widget.WidgetUpdateJobDispatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements LocationCallback, PetOnClickHandler,
        PetDetailFragment.PetDetailPaneCallback, ShelterContactListener {
    private static final String TAG = "MainActivity";

    private final String VIEWPAGER_FRAGMENT_TAG = "ViewPagerFragmentTAG";
    private final String SHELTER_FRAGMENT_TAG = "ShelterFragmentTAG";
    private final String FAVORITE_FRAGMENT_TAG = "FavoriteFragmentTAG";
    private final String BOTTOM_BAR_STATE = "BottombarState";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottomNavigationBar)
    BottomNavigationView mBottomBar;

    private FragmentManager mFragmentManager;
    private ViewPagerFragment viewPagerFragment;
    private SheltersFragment mSheltersFragment;
    private FavoriteFragment mFavoriteFragment;
    private GoogleApiHelper mGoogleApiHelper;
    private boolean mTwoPane;
    private int bottomBarState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mFragmentManager = getSupportFragmentManager();
        mGoogleApiHelper = MyApplication.getGoogleApiHelper();
        mGoogleApiHelper.setLocationCallback(this);
        WidgetUpdateJobDispatcher.scheduleFirebaseJobDispatcher(this);

        if (findViewById(R.id.detail_fragment) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;

        }
        setupBottomBar();
        if (savedInstanceState == null) {
            viewPagerFragment = ViewPagerFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .addToBackStack(VIEWPAGER_FRAGMENT_TAG)
                    .replace(R.id.contentContainer, viewPagerFragment, VIEWPAGER_FRAGMENT_TAG)
                    .commit();
        } else {
            bottomBarState = savedInstanceState.getInt(BOTTOM_BAR_STATE);
            Timber.tag(TAG).d("id restore: " + bottomBarState);
            if (bottomBarState == R.id.tab_nearby)
                mBottomBar.setSelectedItemId(R.id.tab_nearby);
            mBottomBar.setSelectedItemId(bottomBarState);
        }

        Timber.tag(TAG).d("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.tag(TAG).d("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getGoogleApiHelper().connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getGoogleApiHelper().disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BOTTOM_BAR_STATE, mBottomBar.getSelectedItemId());
        super.onSaveInstanceState(outState);
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
                } else startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomBar() {
        Timber.tag(TAG).d("mBottomBar.getSelectedItemId: " + mBottomBar.getSelectedItemId());
        mBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });

    }

    private void onTabItemSelected(int id) {
        switch (id) {
            case R.id.tab_nearby:
                Timber.tag(TAG).d("select tab_nearby");
                if (mTwoPane) {
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.detail_fragment);
                    if (fragment != null) {
                        mFragmentManager.beginTransaction()
                                .remove(mFragmentManager.findFragmentById(R.id.detail_fragment))
                                .commit();
                    }
                }
                viewPagerFragment = (ViewPagerFragment)
                    mFragmentManager.findFragmentByTag(VIEWPAGER_FRAGMENT_TAG);
                if (viewPagerFragment == null) {
                    viewPagerFragment = ViewPagerFragment.newInstance();
                }
                mFragmentManager.beginTransaction()
                        .addToBackStack(VIEWPAGER_FRAGMENT_TAG)
                        .replace(R.id.contentContainer, viewPagerFragment, VIEWPAGER_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.tab_favorites:
                Timber.tag(TAG).d("select tab_favorites");
                if (mTwoPane) {
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.detail_fragment);
                    if (fragment != null) {
                        mFragmentManager.beginTransaction()
                                .remove(mFragmentManager.findFragmentById(R.id.detail_fragment))
                                .commit();
                    }
                }
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
                Timber.tag(TAG).d("select tab_shelters");
                if (mTwoPane) {
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.detail_fragment);
                    if (fragment != null) {
                        mFragmentManager.beginTransaction()
                                .remove(mFragmentManager.findFragmentById(R.id.detail_fragment))
                                .commit();
                    }
                }
                mSheltersFragment = (SheltersFragment)
                        mFragmentManager.findFragmentByTag(SHELTER_FRAGMENT_TAG);
                if (mSheltersFragment == null) {
                    mSheltersFragment = SheltersFragment.newInstance();
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
    public void onItemClick(PetBean pet, View transitionView) {
        Timber.tag(TAG).d("pet name: " + pet.getName());
        if (mTwoPane) {
            PetDetailFragment detailFragment = PetDetailFragment.newInstance(pet);
            mFragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment, detailFragment)
                    .commit();
        } else {
            Intent intent = PetDetailActivity.newIntent(this, pet);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        MainActivity.this, transitionView, transitionView.getTransitionName())
                        .toBundle();
                startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onLocationApiConnected() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.contentContainer);
        if (fragment instanceof ViewPagerFragment) {
            Timber.tag(TAG).d("current fragment: ViewPagerFragment ");
            if (viewPagerFragment != null) {
                viewPagerFragment.googleClientConnected(this);
            }
        }
        if (fragment instanceof SheltersFragment) {
            Timber.tag(TAG).d("current fragment: SheltersFragment");
            if (mSheltersFragment != null) {
                mSheltersFragment.googleClientConnected();
            }
        }
        if (fragment instanceof FavoriteFragment) {
            Timber.tag(TAG).d("current fragment: FavoriteFragment");

        }
    }

    @Override
    public void onSliderClick(List<String> urls, int position) {
        Intent intent = GalleryActivity.newIntent(this, (ArrayList) urls, position);
        startActivity(intent);
    }

    // ShelterContactListener method
    @Override
    public void clickShelter(String id, String name, String phone, String email, String lat, String lng, String address) {
        if (mTwoPane) {
            ShelterPetsFragment shelterPetsFragment = ShelterPetsFragment.newInstance(id, name, phone, email, lat, lng, address);
            mFragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment, shelterPetsFragment)
                    .commit();
        } else {
            Intent intent = ShelterPetsActivity.newIntent(this, id, name, phone, email, lat, lng, address);
            startActivity(intent);
        }
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
}
