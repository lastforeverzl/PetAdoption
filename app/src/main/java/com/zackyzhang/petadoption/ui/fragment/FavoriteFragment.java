package com.zackyzhang.petadoption.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.ui.PetListAdapter;
import com.zackyzhang.petadoption.ui.PetOnClickHandler;
import com.zackyzhang.petadoption.ui.base.BasePresenterFragment;
import com.zackyzhang.petadoption.ui.base.FavoriteContract;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.presenter.FavoritePresenter;
import com.zackyzhang.petadoption.ui.presenter.FavoritePresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by lei on 8/16/17.
 */

public class FavoriteFragment extends BasePresenterFragment<FavoritePresenter, FavoriteContract.View>
        implements FavoriteContract.View, PetListAdapter.OnPetClickListener {
    private static final String TAG = "FavoriteFragment";

    private Unbinder mUnbinder;
    private FavoritePresenter presenter;
    private PetListAdapter mAdapter;
    private List<PetBean> mPetList;

    @BindView(R.id.tv_no_favorite_found)
    TextView noFavorite;
    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerView;

    private PetOnClickHandler mClickHandler;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PetOnClickHandler) {
            mClickHandler = (PetOnClickHandler) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PetListAdapter(getActivity(), false);
        mAdapter.setOnPetClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @NonNull
    @Override
    protected String tag() {
        return "FavoriteFragment";
    }

    @NonNull
    @Override
    protected PresenterFactory<FavoritePresenter> getPresenterFactory() {
        return new FavoritePresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull FavoritePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void loadData(List<PetBean> pets) {
        if (pets == null || pets.isEmpty()) {
            Timber.tag(TAG).d("pets is null");
            noFavorite.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        mPetList = pets;
        mAdapter.setData(pets);
    }

    @Override
    public void onItemClick(PetBean pet) {
        mClickHandler.onItemClick(pet);
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
}
