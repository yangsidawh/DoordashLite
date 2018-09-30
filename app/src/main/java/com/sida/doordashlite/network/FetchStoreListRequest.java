package com.sida.doordashlite.network;

import com.sida.doordashlite.model.StoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class FetchStoreListRequest extends DoordashAPIRequest {
    private static final double DEFAULT_LAT = 37.422740;
    private static final double DEFAULT_LAN = -122.139956;
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 50;
    private static final String TAG = "FetchStoreListRequest";


    private double mLat;
    private double mLan;
    private int mOffset;
    private int mLimit;

    private List<StoreModel> mStoreModels;

    // For real time location info case
    public FetchStoreListRequest(double lat, double lan, int offset, int limit) {
        mLat = lat;
        mLan = lan;
        mOffset = offset;
        mLimit = limit;
        init();
    }

    public FetchStoreListRequest(int offset) {
        mLat = DEFAULT_LAT;
        mLan = DEFAULT_LAN;
        mOffset = offset;
        mLimit = DEFAULT_LIMIT;
        init();
    }

    private void init() {
        mStoreModels = new ArrayList<>();
        mRequest = new Request.Builder()
                .url(getUrl())
                .build();

    }

    @Override
    public String getUrl() {
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append("?lat=");
        builder.append(mLat);
        builder.append("&lng=");
        builder.append(mLan);
        builder.append("&offset=");
        builder.append(mOffset);
        builder.append("&limit=");
        builder.append(mLimit);

        return builder.toString();
    }

    @Override
    public boolean handleResponse(String response) {
        try {
            if (!"".equals(response)) {
                JSONArray storeArr = new JSONArray(response);
                int size = storeArr.length();
                for (int i = 0; i < size; i++) {
                    JSONObject storeObj = storeArr.getJSONObject(i);
                    String name = storeObj.optString("name", "");
                    int id = storeObj.optInt("id", -1);
                    String desc = storeObj.optString("description", "");
                    String coverImageUrl = storeObj.optString("cover_img_url", "");
                    String status = storeObj.optString("status", "");
                    int deliveryFee = storeObj.optInt("delivery_fee", 0);
                    JSONArray tagsArr = storeObj.getJSONArray("tags");
                    List<String> tags = new ArrayList<>(tagsArr.length());

                    for (int j = 0; j<tagsArr.length(); j++) {
                        tags.add(tagsArr.getString(j));
                    }

                    StoreModel storeModel = new StoreModel(id, name, desc, coverImageUrl, status, deliveryFee, tags);
                    mStoreModels.add(storeModel);
                }
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

    public List<StoreModel> getStoreModels() {
        return mStoreModels;
    }
}
