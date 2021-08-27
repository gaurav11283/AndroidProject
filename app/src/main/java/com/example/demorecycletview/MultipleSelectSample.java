package com.example.demorecycletview;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.UndoHelper;
import com.mikepenz.materialize.MaterializeBuilder;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MultipleSelectSample extends AppCompatActivity {

    private FastAdapter<SimpleItem> mFastAdapter;
    ItemAdapter<SimpleItem> headerAdapter;
    private UndoHelper mUndoHelper;
    Toolbar toolbar;
    List<SimpleItem> items;
    ItemAdapter<SimpleItem> itemAdapter;
    RecyclerView rv;
    private ActionModeHelper<SimpleItem> mActionModeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_select);

        inIt();

        setToolbar();


        //set the back arrow in the toolbar

        //inform that longClick is required
        Toast.makeText(this, "LongClick to enable Multi-Selection", Toast.LENGTH_LONG).show();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbar);
        headerAdapter = new ItemAdapter<>();
        itemAdapter = new ItemAdapter<>();
        rv = (RecyclerView) findViewById(R.id.recycler);
        items = new ArrayList<>();


    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Multiple select");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        setAdapter();

    }

    private void setAdapter() {
        mFastAdapter = FastAdapter.with(Arrays.asList(headerAdapter, itemAdapter));

        //configure our mFastAdapter
        //as we provide id's for the items we want the hasStableIds enabled to speed up things
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withSelectable(true);
        mFastAdapter.withMultiSelect(true);
        mFastAdapter.withSelectOnLongClick(true);
        mFastAdapter.withSelectionListener(new ISelectionListener<SimpleItem>() {
            @Override
            public void onSelectionChanged(SimpleItem item, boolean selected) {
                Log.i("FastAdapter", "SelectedCount: " + mFastAdapter.getSelections().size() + " ItemsCount: " + mFastAdapter.getSelectedItems().size());
            }
        });
        mFastAdapter.withOnPreClickListener(new OnClickListener<SimpleItem>() {
            @Override
            public boolean onClick(View v, IAdapter<SimpleItem> adapter, @NonNull SimpleItem item, int position) {
               Boolean res = mActionModeHelper.onClick(item);
                return res != null ? res : false;
            }
        });
        mFastAdapter.withOnClickListener(new OnClickListener<SimpleItem>() {
            @Override
            public boolean onClick(View v, IAdapter<SimpleItem> adapter, @NonNull SimpleItem item, int position) {
                Toast.makeText(v.getContext(), "SelectedCount: " + mFastAdapter.getSelections().size() + " ItemsCount: " + mFastAdapter.getSelectedItems().size(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mFastAdapter.withOnPreLongClickListener(new OnLongClickListener<SimpleItem>() {
            @Override
            public boolean onLongClick(View v, IAdapter<SimpleItem> adapter, SimpleItem item, int position) {
                ActionMode actionMode = mActionModeHelper.onLongClick(MultipleSelectSample.this, position);

                if (actionMode != null) {
                   findViewById(R.id.toolbar).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(MultipleSelectSample.this, R.attr.colorPrimary, R.color.material_drawer_primary));
                }
return actionMode != null;
            }
        });

        //
        mUndoHelper = new UndoHelper<>(mFastAdapter, new UndoHelper.UndoListener<SimpleItem>() {
            @Override
            public void commitRemove(Set<Integer> positions, ArrayList<FastAdapter.RelativeInfo<SimpleItem>> removed) {
                Log.e("UndoHelper", "Positions: " + positions.toString() + " Removed: " + removed.size());
            }
        });

        //we init our ActionModeHelper
        mActionModeHelper = new ActionModeHelper<>(mFastAdapter, R.menu.delete, new ActionBarCallBack());
        setData();
    }

    private void setData() {
        for (int i = 1; i <= 100; i++) {
            SimpleItem item = new SimpleItem();
            item
                    .withName("Test " + i).
                    withIdentifier(100 + i);
            items.add(item);
        }
        setDataInAdapter();
    }

    private void setDataInAdapter() {

        new MaterializeBuilder().withActivity(this).build();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mFastAdapter);

        itemAdapter.add(items);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         outState = mFastAdapter.saveInstanceState(outState);
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

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            mUndoHelper.remove(findViewById(android.R.id.content), "Item removed", "Undo", 9, mFastAdapter.getSelections());
            mode.finish();
            return true;
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