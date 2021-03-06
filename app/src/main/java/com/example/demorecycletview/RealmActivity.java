package com.example.demorecycletview;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaInAnimator;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RealmActivity extends AppCompatActivity {
    //save our FastAdapter
    private FastItemAdapter<RealmItem> mFastItemAdapter;
    //save our Realm instance to close it later
    private Realm mRealm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Realm Activity");

        //style our ui
        new MaterializeBuilder().withActivity(this).build();

        //create our FastAdapter which will manage everything
        mFastItemAdapter = new FastItemAdapter<>();

        //configure our fastAdapter
        mFastItemAdapter.withOnClickListener(new OnClickListener<RealmItem>() {
            @Override
            public boolean onClick(View v, IAdapter<RealmItem> adapter, @NonNull RealmItem item, int position) {
                Toast.makeText(v.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //get our recyclerView and do basic setup
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new AlphaInAnimator());
        rv.setAdapter(mFastItemAdapter);

        //Get a realm instance for this activity
        mRealm = Realm.getDefaultInstance();

        //Add a realm on change listener (don??t forget to close this realm instance before adding this listener again)
        mRealm.where(RealmItem.class).findAllAsync().addChangeListener(new RealmChangeListener<RealmResults<RealmItem>>() {
            @Override
            public void onChange(RealmResults<RealmItem> userItems) {
                //This will call twice
                //1.) from findAllAsync()
                //2.) from createData()
                mFastItemAdapter.setNewList(userItems);
            }
        });

        //fill with some sample data
        createData();

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //restore selections (this has to be done after the items were added
        mFastItemAdapter.withSavedInstanceState(savedInstanceState);
    }

    private void createData() {
        //Execute transaction
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                List<RealmItem> users = new LinkedList<>();
                for (int i = 1; i <= 5; i++) {
                    RealmItem user = new RealmItem();
                    user.withName("Sample Realm Element " + i).withIdentifier(i);
                    users.add(user);
                }
                //insert the created objects to realm
                //a bulk insert has lower object allocations then a copy
                realm.insertOrUpdate(users);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.item_add).setIcon(new IconicsDrawable(this, MaterialDesignIconic.Icon.gmi_plus_square).color(Color.BLACK).actionBar());
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundle
        outState = mFastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_add:
                mRealm.where(RealmItem.class).findAllAsync().addChangeListener(new RealmChangeListener<RealmResults<RealmItem>>() {
                    @Override
                    public void onChange(RealmResults<RealmItem> userItems) {
                        //Remove the change listener
                        userItems.removeChangeListener(this);
                        //Store the primary key to get access from a other thread
                        final long newPrimaryKey = userItems.last().getIdentifier() + 1;
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmItem newUser = realm.createObject(RealmItem.class, newPrimaryKey);
                                newUser.withName("Sample Realm Element " + newPrimaryKey);
                            }
                        });
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Prevent the realm instance from leaking
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRealm();
    }

    private void closeRealm() {
        if (!mRealm.isClosed()) {
            mRealm.close();
        }
    }
}