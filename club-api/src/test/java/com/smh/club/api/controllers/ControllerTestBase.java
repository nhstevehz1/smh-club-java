package com.smh.club.api.controllers;

import java.util.ArrayList;
import java.util.List;

public abstract class ControllerTestBase<T> {

    protected abstract T createDataObject(int flag);

    protected List<T> createDataObjectList(int size) {
        List<T> list = new ArrayList<>();

        for (int ii = 0; ii < size; ii++) {
            list.add(createDataObject(ii));
        }
        return list;
    }
}
