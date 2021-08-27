package com.example.demorecycletview;

import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class inflateView extends AbstractItem<inflateView,inflateView.ViewHolder> {
     String text;

     public inflateView(String txt)
     {
         this.text=txt;
     }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.main_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.main_item;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<inflateView> {
        TextView txt;
        public ViewHolder(View itemView) {
            super(itemView);
            txt =itemView.findViewById(R.id.text);
        }

        @Override
        public void bindView(inflateView item, List payloads) {
            txt.setText(item.text);

        }

        @Override
        public void unbindView(inflateView item) {

        }
    }
}
