package com.sida.doordashlite;

import com.sida.doordashlite.model.StoreModel;
import com.sida.doordashlite.network.FetchStoreListRequest;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class APIRequestTest {
    @Test
    public void ListRequestGetUrlTest() {
        FetchStoreListRequest fetchStoreListRequest = new FetchStoreListRequest(1.0,2.0,10,20);
        assertEquals("https://api.doordash.com/v2/restaurant/?lat=1.0&lng=2.0&offset=10&limit=20", fetchStoreListRequest.getUrl());
    }

    @Test
    public void ListRequestResponseHandlerTest() {
        FetchStoreListRequest fetchStoreListRequest = new FetchStoreListRequest(10);

        String mock = "[{\"is_time_surging\":false,\"max_order_size\":null,\"delivery_fee\":0,\"max_composite_score\":10,\"id\":36,\"merchant_promotions\":[{\"minimum_subtotal_monetary_fields\":{\"currency\":\"USD\",\"display_string\":\"$0.00\",\"unit_amount\":null,\"decimal_places\":2},\"delivery_fee\":null,\"delivery_fee_monetary_fields\":{\"currency\":\"USD\",\"display_string\":\"$0.00\",\"unit_amount\":null,\"decimal_places\":2},\"minimum_subtotal\":null,\"new_store_customers_only\":false,\"id\":3853}],\"average_rating\":4.6,\"menus\":[{\"popular_items\":[],\"is_catering\":false,\"subtitle\":\"Lunch\",\"id\":125339,\"name\":\"Agave Mexican Bistro (Lunch) (Mountain View)\"},{\"popular_items\":[],\"is_catering\":false,\"subtitle\":\"Dinner\",\"id\":125639,\"name\":\"Agave Mexican Bistro (Dinner) (Mountain View)\"}],\"composite_score\":9,\"status_type\":\"open\",\"is_only_catering\":false,\"status\":\"47 mins\",\"number_of_ratings\":1561,\"asap_time\":47,\"description\":\"Traditional Mexican Meets Contemporary California\",\"business\":{\"id\":40,\"name\":\"Agave Mexican Bistro\"},\"tags\":[\"Mexican\"],\"yelp_review_count\":297,\"business_id\":40,\"extra_sos_delivery_fee\":0,\"yelp_rating\":3.5,\"cover_img_url\":\"https://cdn.doordash.com/media/restaurant/cover/Agave-Mexican-Bistro.png\",\"header_img_url\":\"\",\"address\":{\"city\":\"Mountain View\",\"state\":\"CA\",\"street\":\"194 Castro Street\",\"lat\":37.394068,\"lng\":-122.079161,\"printable_address\":\"194 Castro Street, Mountain View, CA 94041, USA\"},\"price_range\":2,\"slug\":\"agave-mexican-bistro-mountain-view\",\"name\":\"Agave Mexican Bistro\",\"is_newly_added\":false,\"url\":\"/store/agave-mexican-bistro-mountain-view-36/\",\"service_rate\":11.0,\"promotion\":null,\"featured_category_description\":null}]";
        String mockInvalid = "{";

        fetchStoreListRequest.handleResponse(mock);
        List<StoreModel> models = fetchStoreListRequest.getStoreModels();

        assertEquals(1, models.size());
        assertEquals("Agave Mexican Bistro", models.get(0).getName());

        fetchStoreListRequest.handleResponse(mockInvalid);
        assertEquals(1, fetchStoreListRequest.getErrorCode());

    }
}
