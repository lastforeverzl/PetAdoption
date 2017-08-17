package com.zackyzhang.petadoption.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.zackyzhang.petadoption.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by lei on 8/10/17.
 */

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity";

    private static final String EXTRA_IMAGES_URLS =
            "com.zackyzhang.petadoption.ui.activity.gallery_urls";
    private static final String EXTRA_IMAGE_POSITION =
            "com.zackyzhang.petadoption.ui.activity.gallery_position";

    private List<String> urls;
    private int position;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent newIntent(Context context, ArrayList<String> urls, int position) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGES_URLS, urls);
        intent.putExtra(EXTRA_IMAGE_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        urls = getIntent().getStringArrayListExtra(EXTRA_IMAGES_URLS);
        for (String url : urls) {
            Timber.tag(TAG).d(url);
        }
        position = getIntent().getIntExtra(EXTRA_IMAGE_POSITION, 0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setToolbarTitle(position);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SamplePagerAdapter(this));
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    private void setToolbarTitle(int position) {
        String title = (position + 1) + "/" + urls.size();
        getSupportActionBar().setTitle(title);
    }

    class SamplePagerAdapter extends PagerAdapter {

        private Context mContext;

        public SamplePagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Picasso.with(mContext).load(urls.get(position)).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
