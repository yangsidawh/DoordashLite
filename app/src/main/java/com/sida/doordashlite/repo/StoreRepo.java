package com.sida.doordashlite.repo;

import android.os.AsyncTask;

import com.sida.doordashlite.interfaces.IOnFetchResultListener;
import com.sida.doordashlite.model.StoreModel;
import com.sida.doordashlite.network.DoordashAPIRequest;
import com.sida.doordashlite.network.FetchStoreListRequest;

import java.util.List;

public class StoreRepo {
    public StoreRepo() {}

    public void getStoreListFromOffset(int offset, IOnFetchResultListener onFetchResultListener) {
        FetchStoreTask fetchStoreTask = new FetchStoreTask(offset, onFetchResultListener);
        fetchStoreTask.execute();
    }

    public void getStoreDetial(int id, IOnFetchResultListener onFetchResultListener) {
        FetchStoreTask fetchStoreTask = new FetchStoreTask(id, onFetchResultListener);
        fetchStoreTask.execute();
    }

    private static class FetchStoreTask extends AsyncTask<Void, Void, List<StoreModel>> {
        int mOffset;
        FetchStoreListRequest mRequest;
        IOnFetchResultListener mOnFetchResultListener;

        FetchStoreTask(int offset, IOnFetchResultListener onFetchResultListener) {
            mOnFetchResultListener = onFetchResultListener;
            mOffset = offset;
            mRequest = new FetchStoreListRequest(mOffset);
        }

        @Override
        protected List<StoreModel> doInBackground(Void... voids) {
            boolean success = mRequest.doNetworkRequest();
            if (success) {
                if (mRequest.getErrorCode() != DoordashAPIRequest.ERROR_NONE) {
                    return null;
                } else {
                    return mRequest.getStoreModels();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<StoreModel> storeModels) {
            if (storeModels == null) {
                mOnFetchResultListener.onError(mRequest.getErrorCode());
            } else {
                mOnFetchResultListener.onSuccess(storeModels);
            }
        }
    }
}
