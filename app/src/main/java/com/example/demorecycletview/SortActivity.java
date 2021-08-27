package com.example.demorecycletview;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.utils.ComparableItemListImpl;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialize.MaterializeBuilder;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortActivity extends AppCompatActivity {

    private static final String[] ALPHABET = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z"};
    private static final int SORT_ASCENDING = 0;
    private static final int SORT_DESCENDING = 1;
    private static final int SORT_NONE = -1;


    Toolbar toolbar;
    RecyclerView recyclerView;


    private FastAdapter<SimpleItem> fastAdapter;
    private ItemAdapter<SimpleItem> itemAdapter;
    private ComparableItemListImpl<SimpleItem> itemListImpl;

    @SortingStrategy
    private int sortingStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content)
                .getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        recyclerView = findViewById(R.id.recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sort List");

        new MaterializeBuilder().withActivity(this).build();

        itemListImpl = new ComparableItemListImpl<>(getComparator());
        itemAdapter = new ItemAdapter<>(itemListImpl);
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withSelectable(true);


        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fastAdapter);

        if (savedInstanceState != null) {
            sortingStrategy = toSortingStrategy(savedInstanceState.getInt("sorting_strategy"));
        } else {
            sortingStrategy = SORT_NONE;
        }

        itemAdapter.setNewList(generateUnsortedList());

        fastAdapter.withSavedInstanceState(savedInstanceState);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @SortingStrategy
    int toSortingStrategy(int val) {
        return val;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         outState = fastAdapter.saveInstanceState(outState);
         outState.putInt("sorting_strategy", sortingStrategy);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        menu.findItem(R.id.item_sort_random).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_wrap_text).color(Color.BLACK).actionBar());
        menu.findItem(R.id.item_sort_asc).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_sort_asc).color(Color.BLACK).actionBar());
        menu.findItem(R.id.item_sort_desc).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_sort_desc).color(Color.BLACK).actionBar());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()) {
            case R.id.item_sort_random:
                sortingStrategy = SORT_NONE;
               Collections.shuffle(itemAdapter.getAdapterItems());
                fastAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_sort_asc:
                sortingStrategy = SORT_ASCENDING;
                itemListImpl.withComparator(getComparator());
                return true;
            case R.id.item_sort_desc:
               sortingStrategy = SORT_DESCENDING;
                itemListImpl.withComparator(getComparator());
                return true;
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "selections = " +
                        fastAdapter.getSelections(), Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    private Comparator<SimpleItem> getComparator() {
        switch (sortingStrategy) {
            case SORT_ASCENDING:
                return new AlphabetComparatorAscending();
            case SORT_DESCENDING:
                return new AlphabetComparatorDescending();
            case SORT_NONE:
                return null;
        }

        throw new RuntimeException("This sortingStrategy is not supported.");
    }

    private List<SimpleItem> generateUnsortedList() {
        ArrayList<SimpleItem> result = new ArrayList<>(26);

        for (int i = 0; i < 26; i++) {
            result.add(makeItem(i));
        }

        Collections.shuffle(result);

        return result;
    }

    private SimpleItem makeItem(@IntRange(from = 0, to = 25) int position) {
        SimpleItem result = new SimpleItem();

        result.withName(ALPHABET[position]);

        position++;
        return result;

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_NONE, SORT_ASCENDING, SORT_DESCENDING})
    public @interface SortingStrategy {
    }


    private class AlphabetComparatorAscending implements Comparator<SimpleItem>, Serializable {
        @Override
        public int compare(SimpleItem lhs, SimpleItem rhs) {
            return lhs.name.compareTo(rhs.name);
        }
    }

    private class AlphabetComparatorDescending implements Comparator<SimpleItem>, Serializable {
        @Override
        public int compare(SimpleItem lhs, SimpleItem rhs) {
            return rhs.name.compareTo(lhs.name);
        }
    }
}