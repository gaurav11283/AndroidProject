package com.example.demorecycletview;


import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

public class SimpleSubExpandableItem<Parent extends IItem & IExpandable, SubItem extends IItem & ISubItem> extends AbstractExpandableItem<SimpleSubExpandableItem<Parent, SubItem>, SimpleSubExpandableItem.ViewHolder, SubItem> {

    public String name;
    public String header;


    private OnClickListener<SimpleSubExpandableItem> mOnClickListener;

    public SimpleSubExpandableItem<Parent, SubItem> withHeader(String Header) {
        this.header = Header;
        return this;
    }
    public SimpleSubExpandableItem<Parent, SubItem> withName(String Name) {
        this.name = Name;
        return this;
    }



    public OnClickListener<SimpleSubExpandableItem> getOnClickListener() {
        return mOnClickListener;
    }

    public SimpleSubExpandableItem<Parent, SubItem> withOnClickListener(OnClickListener<SimpleSubExpandableItem> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

   final private OnClickListener<SimpleSubExpandableItem<Parent, SubItem>> onClickListener = new OnClickListener<SimpleSubExpandableItem<Parent, SubItem>>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, @NonNull SimpleSubExpandableItem item, int position) {
            if (item.getSubItems() != null) {
                if (!item.isExpanded()) {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(180).start();
                } else {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(0).start();
                }
                return mOnClickListener == null || mOnClickListener.onClick(v, adapter, item, position);
            }
            return mOnClickListener != null && mOnClickListener.onClick(v, adapter, item, position);
        }
    };

    @Override
    public OnClickListener<SimpleSubExpandableItem<Parent, SubItem>> getOnItemClickListener() {
        return onClickListener;
    }

    @Override
    public boolean isSelectable() {
        return getSubItems() == null;
    }


    @Override
    public int getType() {
        return R.id.expand_parent_view;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.expandable_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);


        Context ctx = viewHolder.itemView.getContext();
        viewHolder.view.clearAnimation();
        ViewCompat.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));

        viewHolder.txt.setText(name);
        if (getSubItems() == null || getSubItems().size() == 0) {
            viewHolder.icon.setVisibility(View.GONE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
        }

        if (isExpanded()) {
            ViewCompat.setRotation(viewHolder.icon, 0);
        } else {
            ViewCompat.setRotation(viewHolder.icon, 180);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.icon.clearAnimation();
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }



    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        TextView txt;
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            txt = view.findViewById(R.id.material_drawer_name);
            icon = view.findViewById(R.id.material_drawer_icon);
            this.view = view;
        }
    }
}