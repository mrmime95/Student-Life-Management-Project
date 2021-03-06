package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter.recycler;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.ui.timetable.viewmodel.EventViewModel;

import java.util.Collection;

/**
 *
 *
 * Created by Baroti Csaba on 12/14/2016.
 */

public class RecyclerViewEventBindingAdapter extends RecyclerViewBindingAdapter<Event,EventViewModel> {
    private final static int EVENT_POINT=1;
    private final static int EVENT=2;
    private final static int EVENT_START=3;

    @SuppressWarnings("unused")
    public RecyclerViewEventBindingAdapter(int itemBinder, Collection<Event> items) {
        super(itemBinder, items);
    }

    public RecyclerViewEventBindingAdapter(int itemBinder) {
        super(itemBinder);
    }

    @Override
    protected void listChanged() {
        mUIItems.clear();
        mUIItems=RecyclerViewAdapterEventHelper.createEventsForRecycler(mItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //last position is always a fake event and its a point
        if (position==mUIItems.size()-1)
            return EVENT_POINT;

        EventViewModel item=mUIItems.get(position);

        if (item.isFake.get())
            return  EVENT_START;

        return EVENT;
    }

    @Override
    public BasicEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();

        ViewDataBinding binding;

        switch (viewType) {
            case EVENT:
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.timetableday_event, parent, false);
                break;
            case EVENT_START:
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.timetableday_event_start, parent, false);
                break;
            default:
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.timetableday_event_point, parent, false);
        }

        return new BasicEventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BasicEventViewHolder holder, int position) {
        final EventViewModel item = mUIItems.get(position);
        holder.binding.setVariable(mItemBinder, item);
        //holder.binding.getRoot().setTag(ITEM_MODEL, item);
        //holder.binding.getRoot().setOnClickListener(this);
        //holder.binding.getRoot().setOnLongClickListener(this);
        holder.binding.executePendingBindings();
    }
}
