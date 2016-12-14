package com.halcyon.ubb.studentlifemanager.view;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.halcyon.ubb.studentlifemanager.view.adapter.RecyclerViewBindingAdapter;

import java.util.Collection;

/**
 *
 * Created by Baroti Csaba on 12/14/2016.
 */

public class RecyclerViewBindings {
    private static final int KEY_ITEMS = -123;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <Model,ViewModel> void setItems(RecyclerView recyclerView, Collection<ViewModel> items) {
        RecyclerViewBindingAdapter adapter = (RecyclerViewBindingAdapter<Model,ViewModel>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }
}
