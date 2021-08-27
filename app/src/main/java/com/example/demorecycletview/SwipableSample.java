package com.example.demorecycletview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeDragCallback;
import com.mikepenz.fastadapter_extensions.utilities.DragDropUtil;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwipableSample extends AppCompatActivity implements ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback {


    private FastItemAdapter<SwipableItem> fastItemAdapter;
    RecyclerView mRecyclerView;
    Toolbar toolbar;


    private SimpleDragCallback touchCallback;
    private ItemTouchHelper touchHelper;
    List<SwipableItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inIt();
        setToolbar();

    }

    private void inIt() {
        toolbar = findViewById(R.id.main_toolbar);
        mRecyclerView = findViewById(R.id.recycler);
        fastItemAdapter = new FastItemAdapter<>();
        items = new ArrayList<>();


    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swipeable List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        setData();

    }


    private void setData() {

        int x = 0;
            for (int i = 1; i <= 100; i++) {
                SwipableItem swipableItem = new SwipableItem().withName( " Test " + i).withIdentifier(100 + x);
                swipableItem.withIsSwipeable(i % 5 != 0);
                swipableItem.withIsDraggable(i % 5 != 0);
                items.add(swipableItem);
                x++;
            }


        setDataInAdapter();
    }

    private void setDataInAdapter() {
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);

        new MaterializeBuilder().withActivity(this).build();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(fastItemAdapter);
        fastItemAdapter.add(items);



        Drawable leaveBehindDrawableLeft = new IconicsDrawable(this)
                .icon(MaterialDesignIconic.Icon.gmi_delete)
                .color(Color.WHITE)
                .sizeDp(24);
        Drawable leaveBehindDrawableRight = new IconicsDrawable(this)
                .icon(MaterialDesignIconic.Icon.gmi_archive)
                .color(Color.WHITE)
                .sizeDp(24);

        touchCallback = new SimpleSwipeDragCallback(
                this,
                this,
                leaveBehindDrawableLeft,
                ItemTouchHelper.LEFT,
                ContextCompat.getColor(this, R.color.md_red_900)
        )
                .withBackgroundSwipeRight(ContextCompat.getColor(this, R.color.md_blue_900))
                .withLeaveBehindSwipeRight(leaveBehindDrawableRight);



        touchHelper = new ItemTouchHelper(touchCallback); // Create ItemTouchHelper and pass with parameter the SimpleDragCallback
        touchHelper.attachToRecyclerView(mRecyclerView); // Attach ItemTouchHelper to RecyclerView



        //    fastItemAdapter.withSavedInstanceState(savedInstanceState);



    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //add the values which need to be saved from the adapter to the bundle
//        outState = fastItemAdapter.saveInstanceState(outState);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        DragDropUtil.onMove(fastItemAdapter.getItemAdapter(), oldPosition, newPosition);  // change position
        return true;
    }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) {
        // save the new item order, i.e. in your database
    }

    @Override
    public void itemSwiped(int position, int direction) {

        final SwipableItem item = fastItemAdapter.getItem(position);
        item.setSwipedDirection(direction);

        final Runnable removeRunnable = new Runnable() {
            @Override
            public void run() {
                item.setSwipedAction(null);
                int position = fastItemAdapter.getAdapterPosition(item);
                if (position != RecyclerView.NO_POSITION) {
                    fastItemAdapter.getItemFilter().remove(position);
                }
            }
        };
        final View rv = findViewById(R.id.recycler);
        rv.postDelayed(removeRunnable, 3000);

        item.setSwipedAction(new Runnable() {
            @Override
            public void run() {
                rv.removeCallbacks(removeRunnable);
                item.setSwipedDirection(0);
                int position = fastItemAdapter.getAdapterPosition(item);
                if (position != RecyclerView.NO_POSITION) {
                    fastItemAdapter.notifyItemChanged(position);
                }
            }
        });

        fastItemAdapter.notifyItemChanged(position);

        }

}