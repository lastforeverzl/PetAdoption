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
import com.zackyzhang.petadoption.ui.base.TabContract;
import com.zackyzhang.petadoption.ui.presenter.TabPresenter;
import com.zackyzhang.petadoption.ui.presenter.TabPresenterFactory;

import java.util.List;

import timber.log.Timber;

/**
 * Created by lei on 8/7/17.
 */

public class TabFragment extends BasePresenterFragment<TabPresenter, TabContract.View>
        implements TabContract.View, PetListAdapter.OnPetClickListener {
    private static final String TAG = "TabFragment";
    private static final String ARG_ANIMAL = "arg_animal";
    private static final String ARG_ZIPCODE = "arg_zipcode";

    private String mAnimal;
    private String mZipCode;
    private TabPresenter presenter;
    private List<PetBean> mPetList;
//    private TabAdapter mAdapter;
    private PetListAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView mRecyclerView;
    private TextView mNoPets;

    private PetOnClickHandler mClickHandler;

    public static TabFragment newInstance(String animal, String zipCode) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ANIMAL, animal);
        args.putString(ARG_ZIPCODE, zipCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PetOnClickHandler) {
            mClickHandler = (PetOnClickHandler) context;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAnimal = getArguments().getString(ARG_ANIMAL);
            mZipCode = getArguments().getString(ARG_ZIPCODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab, container, false);
        mRecyclerView = root.findViewById(R.id.id_recyclerview);
        mNoPets = root.findViewById(R.id.tv_no_pets_found);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mAdapter = new TabAdapter(getActivity());
        mAdapter = new PetListAdapter(getActivity(), false);
        mAdapter.setOnPetClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchMoreData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(TAG).d(mAnimal+ " onDestroy");
    }

    @NonNull
    @Override
    protected String tag() {
        return mAnimal;
    }

    @NonNull
    @Override
    protected PresenterFactory getPresenterFactory() {
        return new TabPresenterFactory(mAnimal, mZipCode);
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull TabPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadData(List<PetBean> pets) {
        if (pets == null) {
            mNoPets.setVisibility(View.VISIBLE);
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
//
//    class TabAdapter extends RecyclerView.Adapter<TabAdapter.Holder> {
//        private Context mContext;
//        private LayoutInflater mLayoutInflater;
//        private List<PetBean> mList;
//
//        public TabAdapter(Context context) {
//            mContext = context;
//            this.mLayoutInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = mLayoutInflater.inflate(R.layout.pet_item_list, parent, false);
//            return new Holder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(Holder holder, int position) {
//            PetBean pet = mList.get(position);
//            String name = pet.getName();
//            String info = ApiUtils.getPetInfo(pet);
//            String location = ApiUtils.getPetLocation(pet.getContact());
//            holder.mName.setText(name);
//            holder.mInformation.setText(info);
//            holder.mLocation.setText(location);
//            String url = ApiUtils.getFirstPhotoUrl(pet);
//            Picasso.with(mContext).load(url).fit().centerCrop().into(holder.mImageView);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList == null ? 0 : mList.size();
//        }
//
//        public void setData(List<PetBean> data) {
//            mList = data;
//            Timber.tag(TAG).d("adapter mList size: " + mList.size());
//            notifyDataSetChanged();
//        }
//
//        class Holder extends RecyclerView.ViewHolder {
//
//            @BindView(R.id.animal_photo)
//            ImageView mImageView;
//            @BindView(R.id.animal_name)
//            TextView mName;
//            @BindView(R.id.animal_location)
//            TextView mLocation;
//            @BindView(R.id.animal_info)
//            TextView mInformation;
//
//            public Holder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//            }
//
//            @OnClick(R.id.card_view)
//            public void onClick() {
//                int adapterPosition = getAdapterPosition();
//                PetBean pet = mList.get(adapterPosition);
//                mClickHandler.onItemClick(pet);
//            }
//        }
//    }
}
