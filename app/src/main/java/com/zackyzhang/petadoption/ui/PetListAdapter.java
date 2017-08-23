package com.zackyzhang.petadoption.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class PetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PetListAdapter";

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PetBean> mList;
    private boolean withHeader;
    private String phone;
    private String email;
    private String lat;
    private String lng;
    private String address;
    private OnPetClickListener onPetClickListener;
    private OnHeaderClickListener onHeaderClickListener;

    public interface OnPetClickListener {
        void onItemClick(PetBean pet);
    }

    public interface OnHeaderClickListener {
        void onCallClick(String number);

        void onEmailClick(String email);

        void onDirectionClick(String lat, String lng, String address);
    }

    public PetListAdapter(Context context, boolean withHeader) {
        mContext = context;
        this.withHeader = withHeader;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (withHeader) {
            if (viewType == TYPE_HEADER) {
                View v = mLayoutInflater.inflate(R.layout.shelter_pets_header, parent, false);
                holder = new HeaderHolder(v);
            } else {
                View v = mLayoutInflater.inflate(R.layout.pet_item_list, parent, false);
                holder = new Holder(v);
            }
        } else {
            View view = mLayoutInflater.inflate(R.layout.pet_item_list, parent, false);
            holder = new Holder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            ((HeaderHolder) holder).bind();
        } else {
            ((Holder) holder).bind(mList.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (withHeader) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<PetBean> data) {
        mList = data;
        Timber.tag(TAG).d("adapter mList size: " + mList.size());
        notifyDataSetChanged();
    }

    public void setHeader(String phone, String email, String lat, String lng, String address) {
        this.phone = phone;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public void clearAdapter() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void setOnPetClickListener(OnPetClickListener listener) {
        onPetClickListener = listener;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        onHeaderClickListener = listener;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_shelter_call)
        ImageView imCall;
        @BindView(R.id.im_shelter_email)
        ImageView imEmail;
        @BindView(R.id.tv_shelter_call)
        TextView tvCall;
        @BindView(R.id.tv_shelter_email)
        TextView tvEmail;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            if (phone == null) {
                imCall.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
                tvCall.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
            }
            if (email == null) {
                imEmail.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
                tvEmail.setTextColor(ContextCompat.getColor(mContext, R.color.colorGrayInactive));
            }
        }

        @OnClick(R.id.shelter_call)
        public void onCallClick() {
            if (phone != null) {
                String number = ApiUtils.cleanPhoneNumber(phone);
                Timber.tag(TAG).d(number);
                onHeaderClickListener.onCallClick(number);
            }
        }

        @OnClick(R.id.shelter_email)
        public void onEmailClick() {
            if (email != null) {
                Timber.tag(TAG).d(email);
                onHeaderClickListener.onEmailClick(email);
            }
        }

        @OnClick(R.id.shelter_direction)
        public void onDirectionClick() {
            Timber.tag(TAG).d(lat + ", " + lng);
            onHeaderClickListener.onDirectionClick(lat, lng, address);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.animal_photo)
        ImageView mImageView;
        @BindView(R.id.animal_name)
        TextView mName;
        @BindView(R.id.animal_location)
        TextView mLocation;
        @BindView(R.id.animal_info)
        TextView mInformation;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void onClick() {
            int adapterPosition = getAdapterPosition();
            PetBean pet = mList.get(adapterPosition);
            onPetClickListener.onItemClick(pet);
        }

        public void bind(PetBean pet) {
            String name = pet.getName();
            String info = ApiUtils.getPetInfo(pet);
            if (pet.getContact() != null) {
                String location = ApiUtils.getPetLocation(pet.getContact());
                mLocation.setText(location);
            }
            mName.setText(name);
            mInformation.setText(info);
            String url = ApiUtils.getFirstPhotoUrl(pet);
            Picasso.with(mContext).load(url).placeholder(R.drawable.no_image_placeholder).fit().centerCrop().into(mImageView);
        }
    }
}
