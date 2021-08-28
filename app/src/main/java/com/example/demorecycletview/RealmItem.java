package com.example.demorecycletview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Collections;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;


public class RealmItem extends RealmObject implements IItem<RealmItem, RealmItem.ViewHolder> {

    private String name;

    public String getName() {
        return name;
    }


    public RealmItem withName(String name) {
        this.name = name;
        return this;
    }

    protected long mIdentifier = -1;

    public RealmItem withIdentifier(long identifier) {
        this.mIdentifier = identifier;
        return this;
    }
    @Override
    public long getIdentifier() {
        return mIdentifier;
    }


    // the tag for this item
    @Ignore
    protected Object mTag;

    /**
     * set the tag of this item
     *
     * @param object
     * @return this
     */
    public RealmItem withTag(Object object) {
        this.mTag = object;
        return this;
    }

    /**
     * @return the tag of this item
     */
    @Override
    public Object getTag() {
        return mTag;
    }

    // defines if this item is enabled
    @Ignore
    protected boolean mEnabled = true;

    /**
     * set if this item is enabled
     *
     * @param enabled true if this item is enabled
     * @return this
     */
    public RealmItem withEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

    /**
     * @return if this item is enabled
     */
    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    // defines if the item is selected
    @Ignore
    protected boolean mSelected = false;


    @Override
    public RealmItem withSetSelected(boolean selected) {
        this.mSelected = selected;
        return this;
    }


    @Override
    public boolean isSelected() {
        return mSelected;
    }

    // defines if this item is selectable
    @Ignore
    protected boolean mSelectable = true;

    @Override
    public RealmItem withSelectable(boolean selectable) {
        this.mSelectable = selectable;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return mSelectable;
    }


    @Override
    public int getType() {
        return R.id.realm_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.realm_item;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));

        //as we already know the type of our ViewHolder cast it to our type
        bindView(viewHolder, Collections.EMPTY_LIST);

        //return the bound view
        return viewHolder.itemView;
    }


    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));

        //as we already know the type of our ViewHolder cast it to our type
        bindView(viewHolder, Collections.EMPTY_LIST);
        //return the bound and generatedView
        return viewHolder.itemView;
    }


    @Override
    public ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    private ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        holder.itemView.setSelected(isSelected());
        holder.name.setText(name);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.name.setText(null);
    }

    @Override
    public void attachToWindow(ViewHolder holder) {}

    @Override
    public void detachFromWindow(ViewHolder holder) {}

    @Override
    public boolean failedToRecycle(ViewHolder holder) {
        return false;
    }


    @Override
    public boolean equals(int id) {
        return id == mIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractItem<?, ?> that = (AbstractItem<?, ?>) o;
        return mIdentifier == that.getIdentifier();
    }


    @Override
    public int hashCode() {
        return Long.valueOf(mIdentifier).hashCode();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.text);
        }
    }
}