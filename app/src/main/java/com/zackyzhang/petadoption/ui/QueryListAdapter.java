package com.zackyzhang.petadoption.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zackyzhang.petadoption.Constants;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.data.RecentQuery;
import com.zackyzhang.petadoption.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lei on 8/15/17.
 */

public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.Holder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<RecentQuery> queryList;

    public QueryListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        queryList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.query_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        RecentQuery rq = queryList.get(position);
        final String address = rq.getAddress();
        holder.queryAddress.setText(address);
        final String type = rq.getAnimalType();
        final String size = rq.getAnimalSize();
        final String sex = rq.getAnimalSex();
        final String age = rq.getAnimalAge();
        holder.queryFilters.setText(
                Constants.petTypeMap.get(type) + " - " +
                Constants.petSizeMap.get(size) + " - " +
                Constants.petSexMap.get(sex) + " - " + age);
        final String zipCode = rq.getZipCode();
        holder.queryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SearchActivity) mContext).clickRecentQuery(address, zipCode, type, size, sex, age);
            }
        });
    }

    @Override
    public int getItemCount() {
        return queryList.size();
    }

    public void setQueryList(List<RecentQuery> list) {
        queryList = list;
        notifyDataSetChanged();
    }

    public void clearQueryList() {
        queryList.clear();
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.query_layout)
        LinearLayout queryContainer;
        @BindView(R.id.query_address)
        TextView queryAddress;
        @BindView(R.id.query_filters)
        TextView queryFilters;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
