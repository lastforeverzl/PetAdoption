package com.zackyzhang.petadoption.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.EndlessRecyclerViewScrollListener;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.ShelterBean;
import com.zackyzhang.petadoption.ui.base.BasePresenterFragment;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;
import com.zackyzhang.petadoption.ui.base.SheltersContract;
import com.zackyzhang.petadoption.ui.presenter.SheltersPresenter;
import com.zackyzhang.petadoption.ui.presenter.SheltersPresenterFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by lei on 8/11/17.
 */

public class SheltersFragment extends BasePresenterFragment<SheltersPresenter, SheltersContract.View> implements SheltersContract.View {
    private static final String TAG = "SheltersFragment";

    private static final String ARG_ZIPCODE = "arg_zipcode";

    private String mZipCode;
    private SheltersPresenter presenter;
    private SheltersAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView mRecyclerView;
    private TextView noShelters;

    public interface ShelterOnClickHandler {
        void onClickShelter(String id, String name, String phone, String email, String lat, String lng, String adress);

        void onClickCall(String number);

        void onClickDirection(String lat, String lng, String address);
    }

    private ShelterOnClickHandler mClickHandler;

    public static SheltersFragment newInstance(String zipCode) {
        SheltersFragment fragment = new SheltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ZIPCODE, zipCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShelterOnClickHandler) {
            mClickHandler = (ShelterOnClickHandler) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mZipCode = getArguments().getString(ARG_ZIPCODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shelters, container, false);
        mRecyclerView = root.findViewById(R.id.id_recyclerview);
        noShelters = root.findViewById(R.id.tv_no_shelters_found);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SheltersAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchMoreData();
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
        return root;
    }

    @NonNull
    @Override
    protected String tag() {
        return "SheltersFragment";
    }

    @NonNull
    @Override
    protected PresenterFactory getPresenterFactory() {
        return new SheltersPresenterFactory(mZipCode);
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull SheltersPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadData(List<ShelterBean> shelters) {
        if (shelters == null) {
            noShelters.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mAdapter.setData(shelters);
    }

    class SheltersAdapter extends RecyclerView.Adapter<SheltersAdapter.Holder> {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<ShelterBean> mList;

        public SheltersAdapter(Context context) {
            mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.shelter_item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ShelterBean shelter = mList.get(position);
            holder.shelterName.setText(shelter.getName());
            if (!ApiUtils.shelterAddress(shelter).equals("")) {
                holder.shelterAddress.setText(ApiUtils.shelterAddress(shelter));
            } else {
                holder.shelterAddress.setText("");
            }
            if (shelter.getPhone() != null) {
                holder.shelterPhone.setText(shelter.getPhone().trim());
                holder.imShelterCall.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.tvShelterCall.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                holder.shelterPhone.setText("");
                holder.imShelterCall.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
                holder.tvShelterCall.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
            }
            if (shelter.getEmail() != null) {
                Timber.tag(TAG).d(shelter.getName() + ": " + shelter.getEmail());
                holder.shelterEmail.setText(shelter.getEmail());
            } else {
                holder.shelterEmail.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public void setData(List<ShelterBean> data) {
            mList = data;
            Timber.tag(TAG).d("adapter mList size: " + mList.size());
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {

            @BindView(R.id.shelter_name)
            TextView shelterName;
            @BindView(R.id.shelter_address)
            TextView shelterAddress;
            @BindView(R.id.shelter_phone)
            TextView shelterPhone;
            @BindView(R.id.shelter_email)
            TextView shelterEmail;
            @BindView(R.id.shelter_direction)
            TextView shelterDirection;
            @BindView(R.id.im_shelter_call)
            ImageView imShelterCall;
            @BindView(R.id.tv_shelter_call)
            TextView tvShelterCall;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.shelter_layout)
            public void onClickShelter() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                String address = ApiUtils.shelterAddress(shelter);
                mClickHandler.onClickShelter(shelter.getId(),
                        shelter.getName(),
                        shelter.getPhone(),
                        shelter.getEmail(),
                        shelter.getLatitude(),
                        shelter.getLongitude(),
                        address);
            }

            @OnClick(R.id.shelter_call)
            public void onClickCall() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                if (shelter.getPhone() != null) {
                    String number = ApiUtils.cleanPhoneNumber(shelter.getPhone());
                    mClickHandler.onClickCall(number);
                }
            }

            @OnClick(R.id.shelter_direction)
            public void onClickDirection() {
                int adapterPosition = getAdapterPosition();
                ShelterBean shelter = mList.get(adapterPosition);
                String address = ApiUtils.shelterAddress(shelter);
                if (shelter.getLatitude() != null && shelter.getLongitude() != null) {
                    mClickHandler.onClickDirection(shelter.getLatitude(), shelter.getLongitude(), address);
                }
            }

        }
    }
}
