package com.halcyon.ubb.studentlifemanager.view.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BasicEventViewHolder extends RecyclerView.ViewHolder {
    final public ViewDataBinding binding;

    public BasicEventViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

