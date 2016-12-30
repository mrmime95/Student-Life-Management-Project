package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter.recycler;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 *
 * Created by Baroti Csaba on 12/14/2016.
 */

public abstract class RecyclerViewBindingAdapter<Model,ViewModel> extends RecyclerView.Adapter<BasicEventViewHolder> {
    ObservableList<Model> mItems;

    //not every viewholder has real model => fake events
    ArrayList<ViewModel> mUIItems;

    int mItemBinder;
    private final WeakReferenceOnListChangedCallback<Model,ViewModel> mOnListChangedCallback;

    RecyclerViewBindingAdapter(int itemBinder, Collection<Model> items) {
        mItemBinder=itemBinder;
        mUIItems=new ArrayList<>();
        mOnListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        setItems(items);
    }

    RecyclerViewBindingAdapter(int itemBinder) {
        this(itemBinder,null);
    }

    @Override
    public int getItemCount() {
        return mUIItems.size();
    }

    @SuppressWarnings("unchecked")
    public void setItems(@Nullable Collection<Model> items)
    {
        if (mItems==items)
            return;

        if (mItems != null)
        {
            mItems.removeOnListChangedCallback(mOnListChangedCallback);
        }

        if (items instanceof ObservableList)
        {
            mItems = (ObservableList<Model>) items;
            listChanged();
            mItems.addOnListChangedCallback(mOnListChangedCallback);
        }
        else if (items != null)
        {
            mItems = new ObservableArrayList<>();
            mItems.addAll(items);
            listChanged();
            mItems.addOnListChangedCallback(mOnListChangedCallback);
        }
        else
        {
            mItems = null;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateItems() {
        if (mItems != null)
        {
            mItems.removeOnListChangedCallback(mOnListChangedCallback);
            listChanged();
            mItems.addOnListChangedCallback(mOnListChangedCallback);
        }
    }

    protected abstract void listChanged();

    @SuppressWarnings("unchecked")
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if (mItems != null)
        {
            mItems.removeOnListChangedCallback(mOnListChangedCallback);
        }
    }

    private static class WeakReferenceOnListChangedCallback<Model,ViewModel> extends ObservableList.OnListChangedCallback {

        private final WeakReference<RecyclerViewBindingAdapter<Model,ViewModel>> adapterReference;

        WeakReferenceOnListChangedCallback(RecyclerViewBindingAdapter<Model,ViewModel> bindingRecyclerViewAdapter) {
            this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            RecyclerView.Adapter<BasicEventViewHolder> adapter = adapterReference.get();
            if (adapter != null) {
                adapterReference.get().updateItems();
            }
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter<BasicEventViewHolder> adapter = adapterReference.get();
            if (adapter != null) {
                adapterReference.get().updateItems();
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter<BasicEventViewHolder> adapter = adapterReference.get();
            if (adapter != null) {
                adapterReference.get().updateItems();
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            RecyclerView.Adapter<BasicEventViewHolder> adapter = adapterReference.get();
            if (adapter != null) {
                adapterReference.get().updateItems();
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter<BasicEventViewHolder> adapter = adapterReference.get();
            if (adapter != null) {
                adapterReference.get().updateItems();
            }
        }
    }

}
