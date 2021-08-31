package com.example.demorecycletview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ItemFilterListener;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemTouchCallback, ItemFilterListener<inflateView> {
    RecyclerView mRecyclerView;
    Toolbar toolbar;
    ArrayList<inflateView> mArray;
    Activity activity;
    FastAdapter fastAdapter;
    ItemAdapter itemAdapter;
    private Drawer mResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inIt();
        setToolbar();
        setData();

    }


    private void inIt() {
        mRecyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.main_toolbar);

        mArray = new ArrayList();
        activity = this;
        setDrawer();

    }

    private void setDrawer() {

        mResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withDescription("this is home").withSelectable(false).withIdentifier(1).withIcon(R.drawable.ic_baseline_home_24),
                        new PrimaryDrawerItem().withName("Multiple Selection").withDescription("select multiple item").withSelectable(false).withIdentifier(2).withIcon(R.drawable.ic_multiple),
                        new PrimaryDrawerItem().withName("expandable Activity").withDescription("Expand items to get subitem").withSelectable(false).withIdentifier(3).withIcon(R.drawable.ic_expand_more),
                        new PrimaryDrawerItem().withName("Swipeable Activity").withDescription("Swipe to delete").withSelectable(false).withIdentifier(4).withIcon(R.drawable.ic_swipe),
                        new PrimaryDrawerItem().withName("CheckBox Activity").withDescription("click on checkbox to select").withSelectable(false).withIdentifier(5).withIcon(R.drawable.ic_checkbox),
                        new PrimaryDrawerItem().withName("Radio Button Activity").withDescription("select any one item").withSelectable(false).withIdentifier(6).withIcon(R.drawable.ic_radio),
                        new PrimaryDrawerItem().withName("Icon Grid Activity").withDescription("Expand to see").withSelectable(false).withIdentifier(7).withIcon(R.drawable.ic_icon),
                        new PrimaryDrawerItem().withName("Image Item Activity").withDescription("Images").withSelectable(false).withIdentifier(9).withIcon(R.drawable.ic_icon),
                        new PrimaryDrawerItem().withName("Sort Item Activity").withDescription("Sort accordingly").withSelectable(false).withIdentifier(10).withIcon(R.drawable.ic_sort),
                        new PrimaryDrawerItem().withName("ModelIconItem Activity").withDescription("Model icon view").withSelectable(false).withIdentifier(11).withIcon(R.drawable.ic_sort),
                        new PrimaryDrawerItem().withName("Sticky header Activity").withDescription("Sticky header").withSelectable(false).withIdentifier(12).withIcon(R.drawable.ic_sort),
                        new PrimaryDrawerItem().withName("Realm Activity").withDescription("Realm Activity").withSelectable(false).withIdentifier(13).withIcon(R.drawable.ic_sort),
                        new PrimaryDrawerItem().withName("EndlessScrolling List Activity").withDescription("Endless Scroll list Activity").withSelectable(false).withIdentifier(14).withIcon(R.drawable.ic_sort),
                        new PrimaryDrawerItem().withName("Advanced List Activity").withDescription("Advanced list Activity").withSelectable(false).withIdentifier(15).withIcon(R.drawable.ic_sort),

                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("copyright").withSelectable(false).withIdentifier(8).withIcon(R.drawable.ic_baseline_home_24)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {


                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainActivity.this, MultipleSelectSample.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainActivity.this, MultipleSelectSample.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, ExpandableSample.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainActivity.this, SwipableSample.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(MainActivity.this, CheckboxSample.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(MainActivity.this, RadioButtonSample.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainActivity.this, IconGridSample.class);
                            }
                            else if (drawerItem.getIdentifier() == 9) {
                                intent = new Intent(MainActivity.this, ImageListSample.class);
                            }
                            else if (drawerItem.getIdentifier() == 10) {
                                intent = new Intent(MainActivity.this, SortActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 11) {
                                intent = new Intent(MainActivity.this, ModelItemActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 12) {
                                intent = new Intent(MainActivity.this, StickyHeaderActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 13) {
                                intent = new Intent(MainActivity.this, RealmActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 14) {
                                intent = new Intent(MainActivity.this, EndlessScrollingActivity.class);
                            }
                            else if (drawerItem.getIdentifier() == 15) {
                                intent = new Intent(MainActivity.this, AdvancedSample.class);
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSelectedItemByPosition(-1)
                .build();
    }


    private void setToolbar() {
        toolbar.setTitle("search and drag & drop");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void setData() {
        for (int i = 1; i <= 100; i++) {
            mArray.add(new inflateView("MyItem " + i));
        }
        Log.e("mytag", "setData: " + mArray);
        setDataInAdapter();
    }

    private void setDataInAdapter() {
        itemAdapter = new ItemAdapter();
        itemAdapter.add(mArray);
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withSelectable(true);

        itemAdapter.getItemFilter().withFilterPredicate(new IItemAdapter.Predicate<inflateView>() {

            @Override
            public boolean filter(inflateView item, @Nullable CharSequence constraint) {
                return item.text.toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        itemAdapter.getItemFilter().withItemFilterListener(this);
        mRecyclerView.setAdapter(fastAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        SimpleDragCallback dragCallback = new SimpleDragCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    //DRAG AND DROP USING FAST ADAPTER
    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap((itemAdapter.getAdapterItems()), oldPosition, newPosition); // change position
        fastAdapter.notifyAdapterItemMoved(oldPosition, newPosition);
        return true;
    }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) {

    }

    //Search by using fastAdapter concept
    @Override
    public void itemsFiltered(@Nullable CharSequence constraint, @Nullable List<inflateView> results) {

    }

    @Override
    public void onReset() {

    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.home) {
//            Intent intent = new Intent(this, helpActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.signin) {
//            Toast.makeText(getApplicationContext(), "signin clicked", Toast.LENGTH_SHORT);
//            Log.e("mytag", "signin" );
//        } else if (id == R.id.cntct) {
//            Toast.makeText(getApplicationContext(), "Contact clicked", Toast.LENGTH_SHORT);
//            Log.e("mytag", "contact" );
//        } else if (id == R.id.sign) {
//            Toast.makeText(getApplicationContext(), "sign out", Toast.LENGTH_SHORT);
//            Log.e("mytag", "signout" );
//        } else if (id == R.id.faq) {
//            Toast.makeText(getApplicationContext(), "FAQ clicked", Toast.LENGTH_SHORT);
//            Log.e("mytag", "faq" );
//        }
//        else if(id== R.id.help){
//            Toast.makeText(getApplicationContext(),"Help clicked",Toast.LENGTH_SHORT);
//            Log.e("mytag", "help" );
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}