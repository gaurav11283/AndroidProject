package com.example.demorecycletview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demorecycletview.R;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;


public class SimpleImageItem extends AbstractItem<SimpleImageItem, SimpleImageItem.ViewHolder> {

    private String mImageUrl;
    private String mName;
    private String mDescription;

    public SimpleImageItem withImage(String imageUrl) {
        this.mImageUrl = imageUrl;
        return this;
    }

    public SimpleImageItem withName(String name) {
        this.mName = name;
        return this;
    }

    public SimpleImageItem withDescription(String description) {
        this.mDescription = description;
        return this;
    }


    @Override
    public int getType() {
        return R.id.Parent;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.simple_image_item;
    }


    @Override
    public void bindView(SimpleImageItem.ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);


        Context ctx = viewHolder.itemView.getContext();

        viewHolder.imageName.setText(mName);
        viewHolder.imageDescription.setText(mDescription);
        viewHolder.imageView.setImageBitmap(null);
        int color = UIUtils.getThemeColor(ctx, R.attr.colorPrimary);

        viewHolder.view.clearAnimation();
        viewHolder.view.setForeground(FastAdapterUIUtils.getSelectablePressedBackground(ctx, FastAdapterUIUtils.adjustAlpha(color, 100), 50, true));
        Glide.with(ctx).load(mImageUrl).into(viewHolder.imageView);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.imageView.setImageDrawable(null);
        holder.imageDescription.setText(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected FrameLayout view;
        protected ImageView imageView;
        protected TextView imageName;
        protected TextView imageDescription;

        public ViewHolder(View view) {
            super(view);
            imageView =view.findViewById(R.id.item_image_img);
            imageName = view.findViewById(R.id.item_image_name);
            imageDescription = view.findViewById(R.id.item_image_description);

            this.view = (FrameLayout) view;
            int screenWidth = view.getContext().getResources().getDisplayMetrics().widthPixels;
            int finalHeight = (int) (screenWidth / 1.5) / 2;
            imageView.setMinimumHeight(finalHeight);
            imageView.setMaxHeight(finalHeight);
            imageView.setAdjustViewBounds(false);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            lp.height = finalHeight;
            imageView.setLayoutParams(lp);
        }
    }
}