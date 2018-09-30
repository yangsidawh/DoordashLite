package com.sida.doordashlite.network;

import android.text.TextUtils;
import android.util.Log;


import java.io.IOException;

import androidx.annotation.WorkerThread;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Base class for doordash API request
public abstract class DoordashAPIRequest {
    static final String BASE_URL = "https://api.doordash.com/v2/restaurant/";
    static final String TAG = "DoorDashAPIRequest";
    static final int HTTP_SUCCESS = 200;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_PARSING = 1;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_EMPTY_RESULT = 3;
    public static final int ERROR_DEFAULT = 4;

    private OkHttpClient mOkHttpClient;
    Request mRequest;
    int mErrorCode = ERROR_NONE;

    // Overwrite in extend class
    public String getUrl() {
        return BASE_URL;
    }

    public boolean handleResponse(String response) {
        return true;
    }

    public boolean handleError(String response) {
        return true;
    }

    public DoordashAPIRequest() {
        mOkHttpClient = new OkHttpClient();
        mRequest = new Request.Builder()
                .url(getUrl())
                .build();
    }

    @WorkerThread
    public boolean doNetworkRequest() {
        String responseBody;
        Response response;
        try {
            response = mOkHttpClient.newCall(mRequest).execute();
            responseBody = response.body() == null ? "" : response.body().string();
        } catch (IOException ex) {
            Log.e(TAG, "Fail to load store list. " + ex.getMessage());
            mErrorCode = ERROR_NETWORK;
            return false;
        }

        if (response.code() != 200) {
            mErrorCode = ERROR_NETWORK;
            return handleError(responseBody);
        }

        if (TextUtils.isEmpty(responseBody)) {
            return handleError("");
        } else {
            return handleResponse(responseBody);
        }
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
