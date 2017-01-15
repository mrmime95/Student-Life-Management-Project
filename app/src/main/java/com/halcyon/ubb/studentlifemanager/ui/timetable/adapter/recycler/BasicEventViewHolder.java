package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter.recycler;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

class BasicEventViewHolder extends RecyclerView.ViewHolder {
    final ViewDataBinding binding;

    BasicEventViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

