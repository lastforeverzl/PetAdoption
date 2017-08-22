package com.zackyzhang.petadoption.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.GoogleApiHelper;
import com.zackyzhang.petadoption.api.LocationCallback;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.fragment.FavoriteFragment;
import com.zackyzhang.petadoption.ui.fragment.SheltersFragment;
import com.zackyzhang.petadoption.ui.fragment.ViewPagerFragment;
import com.zackyzhang.petadoption.widget.WidgetUpdateJobDispatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements LocationCallback, PetOnClickHandler, SheltersFragment.ShelterOnClickHandler {
    private static final String TAG = "MainActivity";

    private final String VIEWPAGER_FRAGMENT_TAG = "ViewPagerFragmentTAG";
    private final String SHELTER_FRAGMENT_TAG = "ShelterFragmentTAG";
    private final String FAVORITE_FRAGMENT_TAG = "FavoriteFragmentTAG";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottomNavigationBar)
    BottomBar mBottomBar;

    private FragmentManager mFragmentManager;
    private ViewPagerFragment viewPagerFragment;
    private SheltersFragment mSheltersFragment;
    private FavoriteFragment mFavoriteFragment;
    private GoogleApiHelper mGoogleApiHelper;

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
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                onTabItemSelected(tabId);
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_shelters || tabId == R.id.tab_favorites) {
                    Fragment fragment = mFragmentManager.findFragmentById(R.id.contentContainer);
                    if (fragment instanceof SheltersFragment) {
                        ((SheltersFragment) fragment).scrollToTopCallback();
                    }
                    if (fragment instanceof FavoriteFragment) {
                        ((FavoriteFragment) fragment).scrollToTopCallback();
                    }
                }
            }
        });
    }

    private void onTabItemSelected(int id) {
        switch (id) {
            case R.id.tab_nearby:
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

    @Override
    public void onLocationApiConnected() {
        Timber.tag(TAG).d("onLocationApiConnected");
        setupBottomBar();
    }
}
