package com.sida.doordashlite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sida.doordashlite.R;
import com.sida.doordashlite.activity.StoreDetailActivity;
import com.sida.doordashlite.model.StoreModel;

import java.util.ArrayList;
import java.util.List;

public class StoreListAdapter extends  RecyclerView.Adapter {
    private final LayoutInflater mInflater;
    private List<StoreModel> mStores;
    private boolean mHasMore;
    private Activity mActivity;

    public StoreListAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity.getApplicationContext());
        mStores = new ArrayList<>();
        mActivity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType) {
            case 1:
                View progressView = mInflater.inflate(R.layout.progress_bar_item, parent, false);
                return new ProgressBarViewHolder(progressView);
            case 0:
            default:
                View itemView = mInflater.inflate(R.layout.store_list_item, parent, false);
                return new StoreItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoreItemViewHolder) {
            final StoreModel storeModel = mStores.get(position);
            StoreItemViewHolder itemViewHolder = (StoreItemViewHolder) holder;
            itemViewHolder.tvName.setText(storeModel.getName());
            itemViewHolder.tvTag.setText(storeModel.getDescription());
            itemViewHolder.tvStatus.setText(storeModel.getStatus());
            Glide.with(mActivity).load(storeModel.getCoverImageUrl()).apply(new RequestOptions().fitCenter()).into(itemViewHolder.ivThumbnail);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, StoreDetailActivity.class);
                    intent.putExtra("id", storeModel.getId());
                    intent.putExtra("name", storeModel.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStores.size() + (mHasMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return position < mStores.size() ? 0 : 1;
    }

    private class StoreItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvTag;
        private final TextView tvStatus;
        private final ImageView ivThumbnail;

        private StoreItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTag = itemView.findViewById(R.id.tv_tags);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }
    }

    private class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        private ProgressBarViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setStores(List<StoreModel> storeModels) {
        mStores = storeModels;
        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        if (mHasMore != hasMore) {
            mHasMore = hasMore;
            notifyDataSetChanged();
        }
    }

    public int getDataCount() {
        return mStores.size();
    }
}
