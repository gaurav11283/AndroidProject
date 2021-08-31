package com.example.demorecycletview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.demorecycletview.R;
import com.example.demorecycletview.SimpleItem;
import com.example.demorecycletview.SimpleSubExpandableItem;
import com.example.demorecycletview.SimpleSubItem;
import com.example.demorecycletview.StickyHeaderAdapter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IAdapterExtension;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mikepenz.fastadapter.adapters.ItemAdapter.items;

/**
 * This sample showcases compatibility the awesome Sticky-Headers library by timehop
 * https://github.com/timehop/sticky-headers-recyclerview
 */
public class AdvancedSample extends AppCompatActivity {
    private static final String[] headers = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private FastAdapter<IItem> mFastAdapter;
    private ItemAdapter<SimpleItem> mHeaderAdapter;
    private ItemAdapter<IItem> mItemAdapter;
    private ExpandableExtension<IItem> mExpandableExtension;

    private ActionModeHelper<IItem> mActionModeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Advanced Activity");

        new MaterializeBuilder().withActivity(this).build();

        mHeaderAdapter = items();
        mItemAdapter = items();
        StickyHeaderAdapter<IItem> stickyHeaderAdapter = new StickyHeaderAdapter<>();

         mExpandableExtension = new ExpandableExtension<>();

        mFastAdapter = FastAdapter.with(Arrays.asList(mHeaderAdapter, mItemAdapter), Arrays.<IAdapterExtension<IItem>>asList(mExpandableExtension));

        mFastAdapter.withSelectable(true);
        mFastAdapter.withMultiSelect(true);
        mFastAdapter.withSelectOnLongClick(true);
        mFastAdapter.withOnPreClickListener(new OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter adapter, @NonNull IItem item, int position) {
                Boolean res = mActionModeHelper.onClick(item);
                return res != null ? res : false;
            }
        });

        mFastAdapter.withOnPreLongClickListener(new OnLongClickListener<IItem>() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                if (item instanceof IExpandable) {
                    if (((IExpandable) item).getSubItems() != null) {
                        return true;
                    }
                }

                ActionMode actionMode = mActionModeHelper.onLongClick(AdvancedSample.this, position);
                if (actionMode != null) {
                    //we want color our CAB
                    findViewById(R.id.action_mode_bar).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(AdvancedSample.this, R.attr.colorPrimary, R.color.material_drawer_primary));
                }
                //if we have no actionMode we do not consume the event
                return actionMode != null;
            }
        });

        //we init our ActionModeHelper
        mActionModeHelper = new ActionModeHelper<>(mFastAdapter, R.menu.delete, new ActionBarCallBack());

        //get our recyclerView and do basic setup
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(stickyHeaderAdapter.wrap(mFastAdapter));

        final StickyRecyclerHeadersDecoration decoration = new StickyRecyclerHeadersDecoration(stickyHeaderAdapter);
        rv.addItemDecoration(decoration);

        //so the headers are aware of changes
        mFastAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                decoration.invalidateHeaders();
            }
        });

        //init cache with the added items, this is useful for shorter lists with many many different view types (at least 4 or more
        //new RecyclerViewCacheUtil().withCacheSize(2).apply(rv, items);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //we define the items
        setItems();

        //restore selections (this has to be done after the items were added
        mFastAdapter.withSavedInstanceState(savedInstanceState);
    }

    private void setItems() {
        SimpleItem sampleItem = new SimpleItem().withName("Header")
                .withSelectable(false)
                .withIdentifier(1);
        mHeaderAdapter.add(sampleItem);
        //fill with some sample data
        AtomicInteger id = new AtomicInteger(1);
        List<IItem> items = new ArrayList<>();
        int size = 25;
        for (int i = 1; i <= size; i++) {
            if (i % 6 == 0) {
                SimpleSubExpandableItem<SimpleSubExpandableItem, SimpleSubExpandableItem> expandableItem = new SimpleSubExpandableItem<>();
                expandableItem.withName("Test " + id.get())
                        .withHeader(headers[i / 5])
                        .withIdentifier(id.getAndIncrement());
                List<SimpleSubExpandableItem> subItems = new LinkedList<>();
                for (int ii = 1; ii <= 3; ii++) {
                    SimpleSubExpandableItem<SimpleSubExpandableItem, SimpleSubItem> subItem = new SimpleSubExpandableItem<>();
                    subItem.withName("-- SubTest " + id.get())
                            .withHeader(headers[i / 5])
                            .withIdentifier(id.getAndIncrement());

                    List<SimpleSubItem> subSubItems = new LinkedList<>();
                    for (int iii = 1; iii <= 3; iii++) {
                        SimpleSubItem subSubItem = new SimpleSubItem();
                        subSubItem.withName("---- SubSubTest " + id.get())
                                .withHeader(headers[i / 5])
                                .withIdentifier(id.getAndIncrement());
                        subSubItems.add(subSubItem);
                    }
                    subItem.withSubItems(subSubItems);

                    subItems.add(subItem);
                }
                expandableItem.withSubItems(subItems);
                items.add(expandableItem);
            } else {
                items.add(new SimpleSubItem().withName("Test " + id.get()).withHeader(headers[i / 5]).withIdentifier(id.getAndIncrement()));
            }
        }
        mItemAdapter.set(items);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = mFastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Our ActionBarCallBack to showcase the CAB
     */
    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //logic if an item was clicked
            //return false as we want default behavior to go on
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}