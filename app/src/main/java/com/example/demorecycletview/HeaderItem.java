package com.example.demorecycletview;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter_extensions.drag.IDraggable;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

public class HeaderItem extends AbstractItem<HeaderItem, HeaderItem.ViewHolder> implements IDraggable<HeaderItem, IItem> {

    public String header;
    public String name;

    private boolean mIsDraggable = true;

    public HeaderItem withHeader(String header) {
        this.header = header;
        return this;
    }

    public HeaderItem withName(String Name) {
        this.name = Name;
        return this;
    }




    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public HeaderItem withIsDraggable(boolean draggable) {
        this.mIsDraggable = draggable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.simple_item_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.simple_item;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<HeaderItem> {
        protected View view;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.material_drawer_name);

            this.view = view;
        }

        @Override
        public void bindView(@NonNull HeaderItem item, @NonNull List<Object> payloads) {
            Context ctx = itemView.getContext();
            text.setText(item.name);
            UIUtils.setBackground(view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.GREEN, true));

        }

        @Override
        public void unbindView(@NonNull HeaderItem item) {
            text.setText(null);
        }
    }
}