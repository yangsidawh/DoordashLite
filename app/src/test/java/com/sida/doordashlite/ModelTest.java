package com.sida.doordashlite;

import com.sida.doordashlite.model.StoreModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    @Test
    public void CreateStoreModelTest() {
        List<String> mockTags = new ArrayList<>(2);
        mockTags.add("Test tag1");
        mockTags.add("Test tag2");
        StoreModel storeModel = new StoreModel(1,"Test store", "food", "https://image.com/", "Closed", 0, mockTags);

        assertEquals(1, storeModel.getId());
        assertEquals("Test tag1, Test tag2", storeModel.getTagString());
    }
}
