package com.zackyzhang.petadoption.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zackyzhang.petadoption.R;

import timber.log.Timber;

/**
 * Created by lei on 8/8/17.
 */

public class ViewPagerFragment extends Fragment {
    private static final String TAG = "ViewPagerFragment";
    private static final String ARG_ZIPCODE = "arg_zipcode";

    private static String[] animalType;
    private String mZipCode;

    public static ViewPagerFragment newInstance(String zipCode) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ZIPCODE, zipCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mZipCode = getArguments().getString(ARG_ZIPCODE);
        animalType = getActivity().getResources().getStringArray(R.array.animals);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(TAG).d("ViewPagerFragment destroy");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewpager, container, false);
        ViewPager pager = root.findViewById(R.id.viewpager);
        pager.setAdapter(buildAdapter());
        TabLayout mSlidingTabs = root.findViewById(R.id.sliding_tabs);
        mSlidingTabs.setupWithViewPager(pager);
        return root;
    }

    private PagerAdapter buildAdapter() {
        return new PagerAdapter(getActivity(), getChildFragmentManager());
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        Context ctxt=null;

        public PagerAdapter(Context ctxt, FragmentManager mgr) {
            super(mgr);
            this.ctxt=ctxt;
        }

        @Override
        public int getCount() {
            return animalType.length;
        }

        @Override
        public Fragment getItem(int position) {
            Timber.tag(TAG).d(mZipCode);
            return TabFragment.newInstance(animalType[position], mZipCode);
        }

        @Override
        public String getPageTitle(int position) {
            return animalType[position];
        }
    }
}
