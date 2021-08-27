package com.example.demorecycletview;

import android.os.Bundle;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpandableSample extends AppCompatActivity {

    private FastItemAdapter<IItem> fastItemAdapter;
    private ExpandableExtension<IItem> expandableExtension;
    public List<IItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      // LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_sample);

      setToolbar();
      setData();



    }

    private void setData() {
        items = new ArrayList<>();
        AtomicInteger identifier = new AtomicInteger(1);
        for (int i = 1; i <= 100; i++) {
            if (i % 3 != 0) {
                items.add(new SimpleSubItem().withName("Test " + i).withIdentifier(identifier.getAndIncrement()));
                continue;
            }

            SimpleSubExpandableItem parent = new SimpleSubExpandableItem();
            parent.withName("Test " + i).withIdentifier(identifier.getAndIncrement());

            List<IItem> subItems = new LinkedList<>();
            for (int ii = 1; ii <= 5; ii++) {
                SimpleSubExpandableItem subItem = new SimpleSubExpandableItem();
                subItem.withName("-- SubTest " + ii).withIdentifier(identifier.getAndIncrement());

                if (ii % 2 == 0) {
                    continue;
                }

                List<IItem> subSubItems = new LinkedList<>();
                for (int iii = 1; iii <= 3; iii++) {
                    SimpleSubExpandableItem subSubItem = new SimpleSubExpandableItem();
                    subSubItem.withName("---- SubSubTest " + iii).withIdentifier(identifier.getAndIncrement());

                    List<IItem> subSubSubItems = new LinkedList<>();
                    for (int iiii = 1; iiii <= 4; iiii++) {
                        SimpleSubExpandableItem subSubSubItem = new SimpleSubExpandableItem();
                        subSubSubItem.withName("---- SubSubSubTest " + iiii).withIdentifier(identifier.getAndIncrement());
                        subSubSubItems.add(subSubSubItem);
                    }
                    subSubItem.withSubItems(subSubSubItems);
                    subSubItems.add(subSubItem);
                }
                subItem.withSubItems(subSubItems);
                subItems.add(subItem);
            }
            parent.withSubItems(subItems);
            items.add(parent);
        }

        setDataInAdapter();
    }

    private void setDataInAdapter() {
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(true);
        expandableExtension = new ExpandableExtension<>();
        fastItemAdapter.addExtension(expandableExtension);

        new MaterializeBuilder().withActivity(this).build();

        RecyclerView rv =  findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(fastItemAdapter);


        fastItemAdapter.add(items);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EXPANDABLE ITEM");
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
}