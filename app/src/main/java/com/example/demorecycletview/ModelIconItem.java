package com.example.demorecycletview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.ModelAbstractItem;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.List;

public class ModelIconItem extends ModelAbstractItem<IconModel, ModelIconItem, ModelIconItem.ViewHolder> {

    public ModelIconItem(IconModel icon) {
        super(icon);
    }

    @Override
    public int getType() {
        return R.id.icon_parent;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.icon_item;
    }


    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

       viewHolder.image.setIcon(new IconicsDrawable(viewHolder.image.getContext(), getModel().icon));
        viewHolder.name.setText(getModel().icon.getName());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.image.setImageDrawable(null);
        holder.name.setText(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
       public TextView name;
        public IconicsImageView image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            image = view.findViewById(R.id.icon);
            this.view = view;
        }
    }
}