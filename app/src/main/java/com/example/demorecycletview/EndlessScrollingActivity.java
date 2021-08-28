package com.example.demorecycletview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.mikepenz.fastadapter_extensions.utilities.DragDropUtil;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.mikepenz.fastadapter.adapters.ItemAdapter.items;

public class EndlessScrollingActivity extends AppCompatActivity implements ItemTouchCallback {

    private FastItemAdapter<SimpleItem> fastItemAdapter;
    private ItemAdapter footerAdapter;

    //drag & drop
    private SimpleDragCallback touchCallback;
    private ItemTouchHelper touchHelper;

    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        new MaterializeBuilder().withActivity(this).build();

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);

        footerAdapter = items();
        fastItemAdapter.addAdapter(1, footerAdapter);


        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fastItemAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(footerAdapter) {
            @Override
            public void onLoadMore(final int currentPage) {
                footerAdapter.clear();
                footerAdapter.add(new ProgressItem().withEnabled(false));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footerAdapter.clear();
                        for (int i = 1; i < 16; i++) {
                            fastItemAdapter.add(fastItemAdapter.getAdapterItemCount(), new SimpleItem().withName("Item " + i + " Page " + currentPage));
                        }
                    }
                }, 2000);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        List<SimpleItem> items = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            items.add(new SimpleItem().withName("Item " + i + " Page 0"));
        }
        fastItemAdapter.add(items);

        touchCallback = new SimpleDragCallback(this);
        touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        fastItemAdapter.withSavedInstanceState(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

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
        DragDropUtil.onMove(fastItemAdapter.getItemAdapter(), oldPosition, newPosition); // change position
        return true;
    }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) {
        // save the new item order, i.e. in your database
    }

}