package com.example.demorecycletview;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter_extensions.drag.IDraggable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter_extensions.swipe.ISwipeable;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

public class SwipableItem extends AbstractItem<SwipableItem, SwipableItem.ViewHolder> implements ISwipeable<SwipableItem, IItem>, IDraggable<SwipableItem, IItem> {

    public String name;

//    public String undoTextSwipeFromRight;
//    public String undoTextSwipeFromLeft;
//    public String undoTextSwipeFromTop;
//    public String undoTextSwipeFromBottom;

    public int swipedDirection;
    private Runnable swipedAction;
    public boolean swipable = true;
    public boolean draggable = true;

    public SwipableItem withName(String Name) {
        this.name = Name;
        return this;
    }



    @Override
    public boolean isSwipeable() {
        return swipable;
    }

    @Override
    public SwipableItem withIsSwipeable(boolean swipable) {
        this.swipable = swipable;
        return this;
    }

    @Override
    public boolean isDraggable() {
        return draggable;
    }

    @Override
    public SwipableItem withIsDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public void setSwipedDirection(int swipedDirection) {
        this.swipedDirection = swipedDirection;
    }

    public void setSwipedAction(Runnable action) {
        this.swipedAction = action;
    }

    @Override
    public int getType() {
        return R.id.swipe_result_content;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.swipable_item;
    }


    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.nameText.setText(name);

        viewHolder.swipeResultContent.setVisibility(swipedDirection != 0 ? View.VISIBLE : View.GONE);
        viewHolder.itemContent.setVisibility(swipedDirection != 0 ? View.GONE : View.VISIBLE);

        CharSequence swipedAction = null;
        CharSequence swipedText = null;
        if (swipedDirection != 0) {
          //  swipedAction = viewHolder.itemView.getContext().getString("undo");

            //this line of code is used to set text while swiping
            swipedText = swipedDirection == ItemTouchHelper.LEFT ? "Removed" : "Archived";

            //this line of code is used to set color while swiping
            viewHolder.swipeResultContent.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), swipedDirection == ItemTouchHelper.LEFT ? R.color.md_red_900 : R.color.md_blue_900));
        }
        viewHolder.swipedAction.setText(swipedAction == null ? "" : swipedAction);
        viewHolder.swipedText.setText(swipedText == null ? "" : swipedText);
        viewHolder.swipedActionRunnable = this.swipedAction;
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.nameText.setText(null);
        holder.swipedAction.setText(null);
        holder.swipedText.setText(null);
        holder.swipedActionRunnable = null;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        View swipeResultContent;
        View itemContent;
       TextView swipedText;
        TextView swipedAction;

        public Runnable swipedActionRunnable;

        public ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.material_drawer_name);
            swipeResultContent = view.findViewById(R.id.swipe_result_content);
            itemContent =view.findViewById(R.id.item_content);
            swipedText = view.findViewById(R.id.swiped_action);
            swipedAction = view.findViewById(R.id.swiped_action);
            swipedAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swipedActionRunnable != null) {
                        swipedActionRunnable.run();
                    }
                }
            });
        }
    }
}