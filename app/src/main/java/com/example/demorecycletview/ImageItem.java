package com.example.demorecycletview;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.utils.EventHookUtil;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.List;


public class ImageItem extends AbstractItem<ImageItem, ImageItem.ViewHolder> {

    public String mImageUrl;
    public String mName;
    public String mDescription;
    public boolean mStarred = false;

    public ImageItem withImage(String imageUrl) {
        this.mImageUrl = imageUrl;
        return this;
    }

    public ImageItem withName(String name) {
        this.mName = name;
        return this;
    }

    public ImageItem withDescription(String description) {
        this.mDescription = description;
        return this;
    }

    public ImageItem withStarred(boolean starred) {
        this.mStarred = starred;
        return this;
    }


    @Override
    public int getType() {
        return R.id.image_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.image_item;
    }


    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();

        viewHolder.imageName.setText(mName);
        viewHolder.imageDescription.setText(mDescription);
        viewHolder.imageView.setImageBitmap(null);

        //we pre-style our heart :D
        style(viewHolder.imageLovedOn, mStarred ? 1 : 0);
        style(viewHolder.imageLovedOff, mStarred ? 0 : 1);


        Glide.with(ctx).load(mImageUrl).into(viewHolder.imageView);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        //Glide.clear(holder.imageView);
        holder.imageView.setImageDrawable(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    private void style(View view, int value) {
        view.setScaleX(value);
        view.setScaleY(value);
        view.setAlpha(value);
    }


    public void animateHeart(View imageLovedOn, View imageLovedOff, boolean on) {
        imageLovedOn.setVisibility(View.VISIBLE);
        imageLovedOff.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            viewPropertyStartCompat(imageLovedOff.animate().scaleX(on ? 0 : 1).scaleY(on ? 0 : 1).alpha(on ? 0 : 1));
            viewPropertyStartCompat(imageLovedOn.animate().scaleX(on ? 1 : 0).scaleY(on ? 1 : 0).alpha(on ? 1 : 0));
        }
    }


    public static void viewPropertyStartCompat(ViewPropertyAnimator animator) {
        if (Build.VERSION.SDK_INT >= 14) {
            animator.start();
        }
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageView imageView;
        protected TextView imageName;
        protected TextView imageDescription;
       public RelativeLayout imageLovedContainer;
        protected IconicsImageView imageLovedOn;
         protected IconicsImageView imageLovedOff;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.item_image_img);
            imageName = view.findViewById(R.id.item_image_name);
            imageDescription = view.findViewById(R.id.item_image_description);
            imageLovedContainer = view.findViewById(R.id.item_image_loved_container);
            imageLovedOn = view.findViewById(R.id.item_image_loved_yes);
            imageLovedOff = view.findViewById(R.id.item_image_loved_no);

            this.view = view;

            int columns = 1;
            int screenWidth = view.getContext().getResources().getDisplayMetrics().widthPixels;
            int finalHeight = (int) (screenWidth / 1.5);
            imageView.setMinimumHeight(finalHeight / columns);
            imageView.setMaxHeight(finalHeight / columns);
            imageView.setAdjustViewBounds(false);
            //set height as layoutParameter too
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            lp.height = finalHeight / columns;
            imageView.setLayoutParams(lp);
        }
    }

    public static class ImageItemHeartClickEvent extends ClickEventHook<ImageItem> {
        @Nullable
        @Override
        public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ImageItem.ViewHolder) {
                return EventHookUtil.toList(((ViewHolder) viewHolder).imageLovedContainer);
            }
            return super.onBindMany(viewHolder);
        }

        @Override
        public void onClick(View v, int position, FastAdapter<ImageItem> fastAdapter, ImageItem item) {
            item.withStarred(!item.mStarred);
            //we animate the heart
            item.animateHeart(((ViewGroup) v).getChildAt(0), ((ViewGroup) v).getChildAt(1), item.mStarred);

            //we display the info about the click
            Toast.makeText(v.getContext(), item.mImageUrl + " - " + item.mStarred, Toast.LENGTH_SHORT).show();
        }
    }
}