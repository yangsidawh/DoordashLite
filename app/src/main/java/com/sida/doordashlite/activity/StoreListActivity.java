package com.sida.doordashlite.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sida.doordashlite.R;
import com.sida.doordashlite.adapter.StoreListAdapter;
import com.sida.doordashlite.model.StoreModel;
import com.sida.doordashlite.viewmodel.StoreListViewModel;

import java.util.List;

import static com.sida.doordashlite.network.DoordashAPIRequest.ERROR_NETWORK;

public class StoreListActivity extends AppCompatActivity {
    private static final String TAG = "StoreListActivity";

    private StoreListViewModel mStoreListViewModel;
    private LinearLayoutManager mLayoutManager;
    private StoreListAdapter mAdapter;
    RecyclerView.OnScrollListener mScrollListener;

    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_list);

        mStoreListViewModel = ViewModelProviders.of(this).get(StoreListViewModel.class);
        mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_storelist);
        final StoreListAdapter adapter = new StoreListAdapter(this);
        mAdapter = adapter;

        mStoreListViewModel.getAllStores().observe(this, new Observer<List<StoreModel>>() {
            @Override
            public void onChanged(@Nullable List<StoreModel> storeModels) {
                adapter.setStores(storeModels);
            }
        });

        mStoreListViewModel.hasMore().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasMore) {
                // Show/hide progress bar based on if we get more data from server
                adapter.setHasMore(hasMore);
            }
        });

        mStoreListViewModel.getErrorCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer errorCode) {
                showError(errorCode);
            }
        });

        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsLoading) {
                    return;
                }

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    int offset = mAdapter.getDataCount();
                    mStoreListViewModel.loadMoreStores(offset);
                }
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation()));
        recyclerView.addOnScrollListener(mScrollListener);
    }

    private void showError(int errorCode) {
        if (!this.isFinishing()) {
            switch(errorCode) {
                case ERROR_NETWORK:
                    Toast.makeText(this, getResources().getString(R.string.offline_message), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, getResources().getString(R.string.default_error_message), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
