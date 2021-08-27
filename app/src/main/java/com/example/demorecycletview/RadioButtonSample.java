package com.example.demorecycletview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.List;

public class RadioButtonSample extends AppCompatActivity {

    private FastItemAdapter<RadioButtonItem> fastItemAdapter;
    Toolbar toolbar;
    List<RadioButtonItem> items;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setSystemUiVisibility(findViewById(android.R.id.content).getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inIt();
        setData();


    }

    private void inIt() {
        toolbar = findViewById(R.id.main_toolbar);
        fastItemAdapter = new FastItemAdapter<>();
        items = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        setToolbar();
    }


    private void setData() {
        int x = 0;
        for (int i = 1; i <= 50; i++) {
            items.add(new RadioButtonItem().withName(" Test " + x).withIdentifier(100 + x));
            x++;
        }
        setDataInAdapter();
    }

    private void setDataInAdapter() {

        new MaterializeBuilder().withActivity(this).build();

        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withOnPreClickListener(new OnClickListener<RadioButtonItem>() {
            @Override
            public boolean onClick(View v, IAdapter<RadioButtonItem> adapter, @NonNull RadioButtonItem item, int position) {
                return true;
            }
        });

        fastItemAdapter.withEventHook(new RadioButtonItem.RadioButtonClickEvent());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fastItemAdapter);
        fastItemAdapter.add(items);


    }


    private void setToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Radio Button");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "selections = " + fastItemAdapter.getSelections(), Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}