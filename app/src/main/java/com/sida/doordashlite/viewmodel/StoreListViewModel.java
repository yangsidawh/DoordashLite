package com.sida.doordashlite.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.sida.doordashlite.interfaces.IOnFetchResultListener;
import com.sida.doordashlite.model.StoreModel;
import com.sida.doordashlite.repo.StoreRepo;

import java.util.ArrayList;
import java.util.List;

import static com.sida.doordashlite.network.DoordashAPIRequest.ERROR_NETWORK;

public class StoreListViewModel extends AndroidViewModel {
    private MutableLiveData<List<StoreModel>> mAllStores;
    private MutableLiveData<Boolean> mHasMore;
    private MutableLiveData<Integer> mErrorCode;

    private StoreRepo mStoreRepo;
    IOnFetchResultListener mOnFetchResultListener;

    private boolean mIsLoading = false;

    public StoreListViewModel(@NonNull Application application) {
        super(application);
        mStoreRepo = new StoreRepo();
        init();
    }

    void init() {
        mHasMore = new MutableLiveData<>();
        mAllStores = new MutableLiveData<>();
        mErrorCode = new MutableLiveData<>();
        mOnFetchResultListener = new IOnFetchResultListener() {
            @Override
            public void onSuccess(List<StoreModel> models) {
                mIsLoading = false;
                if (models.size() == 0) {
                    mHasMore.postValue(false);
                } else {
                    List<StoreModel> existingModels = mAllStores != null && mAllStores.getValue() != null ? mAllStores.getValue() : new ArrayList<StoreModel>();
                    existingModels.addAll(models);
                    mAllStores.postValue(existingModels);
                }
            }

            @Override
            public void onError(int errorCode) {
                mIsLoading = false;
                mErrorCode.postValue(errorCode);
            }
        };

        mIsLoading = true;
        mStoreRepo.getStoreListFromOffset(0, mOnFetchResultListener);
        mHasMore.setValue(true);
    }

    public LiveData<List<StoreModel>> getAllStores() {
        return mAllStores;
    }

    public void loadMoreStores(int offset) {
        if (!mIsLoading) {
            mStoreRepo.getStoreListFromOffset(offset, mOnFetchResultListener);
        }
    }

    public MutableLiveData<Boolean> hasMore() {
        return mHasMore;
    }

    public MutableLiveData<Integer> getErrorCode() {
        return mErrorCode;
    }
}
