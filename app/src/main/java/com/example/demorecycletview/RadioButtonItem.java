package com.example.demorecycletview;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;
import java.util.Set;


public class RadioButtonItem extends AbstractItem<RadioButtonItem, RadioButtonItem.ViewHolder> {

  public String name;


    public RadioButtonItem withName(String Name) {
        this.name = Name;
        return this;
    }




    @Override
    public int getType() {
        return R.id.radio_parent;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.radio_button_item;
    }


    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.radioButton.setChecked(isSelected());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        public RadioButton radioButton;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            radioButton = view.findViewById(R.id.radiobutton);
            text = view.findViewById(R.id.material_drawer_name);
            this.view = view;
        }
    }

    public static class RadioButtonClickEvent extends ClickEventHook<RadioButtonItem> {
        @Override
        public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RadioButtonItem.ViewHolder) {
                return ((ViewHolder) viewHolder).radioButton;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<RadioButtonItem> fastAdapter, RadioButtonItem item) {
            if (!item.isSelected()) {
                Set<Integer> selections = fastAdapter.getSelections();
                if (!selections.isEmpty()) {
                    int selectedPosition = selections.iterator().next();
                    fastAdapter.deselect();
                    fastAdapter.notifyItemChanged(selectedPosition);
                }
                fastAdapter.select(position);
            }
        }
    }
}