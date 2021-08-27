package com.example.demorecycletview;

import android.content.Context;
import android.graphics.Color;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem;
import com.mikepenz.fastadapter_extensions.drag.IDraggable;

import java.util.List;

public class SimpleSubItem<Parent extends IItem & IExpandable & ISubItem & IClickable> extends AbstractExpandableItem<Parent, SimpleSubItem.ViewHolder, SimpleSubItem<Parent>> implements IDraggable<SimpleSubItem, IItem> {

    public String name;

    private boolean mIsDraggable = true;


    public SimpleSubItem<Parent> withName(String Name) {
        this.name = Name;
        return this;
    }



    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public SimpleSubItem withIsDraggable(boolean draggable) {
        this.mIsDraggable = draggable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.simple_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.simple_view;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

         Context ctx = viewHolder.itemView.getContext();

        viewHolder.view.clearAnimation();
        ViewCompat.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.GRAY, true));
        viewHolder.name.setText(name);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        TextView name;
        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.material_drawer_name);
           this.view = view;
        }
    }
}