package com.sida.doordashlite.network;

import com.sida.doordashlite.model.StoreModel;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

public class FetchStoreDetailRequest extends DoordashAPIRequest {
    private int mId;
    private StoreModel mStore;

    public FetchStoreDetailRequest(int id) {
        mId = id;
        init();
    }

    @Override
    public String getUrl() {
        return BASE_URL + mId;
    }

    private void init() {
        mRequest = new Request.Builder()
                .url(getUrl())
                .build();
    }

    @Override
    public boolean handleResponse(String response) {
        try {
            if (!response.equals("")) {
                JSONObject store = new JSONObject(response);
                int id = store.optInt("id");
                String name = store.optString("name");
                String desc = store.optString("description");
                String status = store.optString("status");
                int fee = store.optInt("delivery_fee");

                mStore = new StoreModel(id, name, desc, "", status, fee, null);
            } else {
                mErrorCode = ERROR_EMPTY_RESULT;
            }
        } catch (JSONException ex) {
            mErrorCode = ERROR_PARSING;
            return false;
        }

        return true;
    }

    @Override
    public boolean handleError(String response) {
        try {
            if (!response.equals("")) {
                JSONObject json  = new JSONObject(response);
                //TODO: Error code parsing
                mErrorCode = ERROR_DEFAULT;
            } else {
                mErrorCode = ERROR_PARSING;
            }
        } catch (JSONException ex) {
            mErrorCode = ERROR_PARSING;
            return false;
        }

        return true;
    }

    public StoreModel getStore() {
        return mStore;
    }
}
