package com.sida.doordashlite.interfaces;

import com.sida.doordashlite.model.StoreModel;

import java.util.List;

public interface IOnFetchResultListener {
    void onSuccess(List<StoreModel> models);
    void onError(int errorCode);
}