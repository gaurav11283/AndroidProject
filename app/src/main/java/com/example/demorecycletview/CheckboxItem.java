package com.example.demorecycletview;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

public class CheckboxItem extends AbstractItem<CheckboxItem, CheckboxItem.ViewHolder> {

   public String name;



    public CheckboxItem withName(String Name) {
        this.name = Name;
        return this;
    }


    @Override
    public int getType() {
        return R.id.checkbox_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.checkbox_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.checkBox.setChecked(isSelected());

        viewHolder.text.setText(name);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.text.setText(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        public CheckBox checkBox;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox);
            text = view.findViewById(R.id.material_drawer_name);
            this.view = view;
        }
    }

    public static class CheckBoxClickEvent extends ClickEventHook<CheckboxItem> {
        @Override
        public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof CheckboxItem.ViewHolder) {
                return ((CheckboxItem.ViewHolder) viewHolder).checkBox;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<CheckboxItem> fastAdapter, CheckboxItem item) {
            fastAdapter.toggleSelection(position);
        }
    }
}