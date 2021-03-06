package com.example.demorecycletview;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.List;


public class StickyHeaderAdapter<Item extends IItem> extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter {
    @Override
    public long getHeaderId(int position) {
        IItem item = getItem(position);

        if (item instanceof HeaderItem && ((HeaderItem) item).header != null) {
            return ((HeaderItem) item).header.charAt(0);
        } else if (item instanceof SimpleSubItem && ((SimpleSubItem) item).header != null) {
            return ((SimpleSubItem) item).header.charAt(0);
        } else if (item instanceof SimpleSubExpandableItem && ((SimpleSubExpandableItem) item).header != null) {
            return ((SimpleSubExpandableItem) item).header.charAt(0);
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;

        IItem item = getItem(position);
        if (item instanceof SimpleItem && ((HeaderItem) item).header != null) {
            textView.setText(String.valueOf(((HeaderItem) item).header.charAt(0)));
        } else if (item instanceof SimpleSubItem && ((SimpleSubItem) item).header != null) {
            textView.setText(String.valueOf(((SimpleSubItem) item).header.charAt(0)));
        } else if (item instanceof SimpleSubExpandableItem && ((SimpleSubExpandableItem) item).header != null) {
            textView.setText(String.valueOf(((SimpleSubExpandableItem) item).header.charAt(0)));
        }
        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

    private FastAdapter<Item> mFastAdapter;


    public StickyHeaderAdapter<Item> wrap(FastAdapter fastAdapter) {
        this.mFastAdapter = fastAdapter;
        return this;
    }


    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (mFastAdapter != null) {
            mFastAdapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (mFastAdapter != null) {
            mFastAdapter.unregisterAdapterDataObserver(observer);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mFastAdapter.getItemViewType(position);
    }


    @Override
    public long getItemId(int position) {
        return mFastAdapter.getItemId(position);
    }


    public FastAdapter<Item> getFastAdapter() {
        return mFastAdapter;
    }


    public Item getItem(int position) {
        return mFastAdapter.getItem(position);
    }


    @Override
    public int getItemCount() {
        return mFastAdapter.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mFastAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mFastAdapter.onBindViewHolder(holder, position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        mFastAdapter.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mFastAdapter.setHasStableIds(hasStableIds);
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mFastAdapter.onViewRecycled(holder);
    }


    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return mFastAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mFastAdapter.onViewDetachedFromWindow(holder);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mFastAdapter.onViewAttachedToWindow(holder);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mFastAdapter.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mFastAdapter.onDetachedFromRecyclerView(recyclerView);
    }
}