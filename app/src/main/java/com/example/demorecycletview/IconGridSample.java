package com.example.demorecycletview;

import android.os.Bundle;

import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IconGridSample extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter fastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Icon Grid Sample");

        //style our ui
        new MaterializeBuilder().withActivity(this).build();

        //create our FastAdapter which will manage everything
        fastItemAdapter = new FastItemAdapter();

        ExpandableExtension expandableExtension = new ExpandableExtension();
        fastItemAdapter.addExtension(expandableExtension);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (fastItemAdapter.getItemViewType(position)) {
                    case R.id.expand_parent_view:
                        return 3;
                    case R.id.icon_parent:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(fastItemAdapter);

        List<ITypeface> mFonts = new ArrayList<>(Iconics.getRegisteredFonts(this));
        Collections.sort(mFonts, new Comparator<ITypeface>() {
            @Override
            public int compare(final ITypeface object1, final ITypeface object2) {
                return object1.getFontName().compareTo(object2.getFontName());
            }
        });

        //add all icons of all registered Fonts to the list
        int count = 0;
        ArrayList<SimpleSubExpandableItem> items = new ArrayList<>(Iconics.getRegisteredFonts(this).size());
        for (ITypeface font : mFonts) {
            SimpleSubExpandableItem expandableItem = new SimpleSubExpandableItem();
            expandableItem
                    .withName(font.getFontName())
                    .withIdentifier(count);

            ArrayList<IItem> icons = new ArrayList<>();
            for (String icon : font.getIcons()) {
                IconItem iconItem = new IconItem();
                iconItem.withIcon(font.getIcon(icon));
                icons.add(iconItem);
            }
            expandableItem.withSubItems(icons);

            items.add(expandableItem);
            count++;
        }

        fastItemAdapter.add(items);
        if (savedInstanceState != null) {
            fastItemAdapter.withSavedInstanceState(savedInstanceState);
        } else {
             expandableExtension.expand(2);
        }

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