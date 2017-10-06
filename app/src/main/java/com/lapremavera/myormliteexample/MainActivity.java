package com.lapremavera.myormliteexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText mNewPersonNameInput;
    private ListView mListView;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = (ListView) findViewById(R.id.recordslist);
        mNewPersonNameInput = (EditText) findViewById(R.id.newRecordInput);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        populateList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void populateList()
    {
        List<Person> list = mDatabaseHelper.GetData();
        List<String> infolist = new ArrayList<>();
        for (Person person: list)
        {
            infolist.add(person.getName() + "with id" + person.getAccountId());
        }
        mListView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, infolist));
    }

    public void addRecord(View v)
    {
        String strName = mNewPersonNameInput.getText().toString().trim();
        if (TextUtils.isEmpty(strName))
        {
            Toast.makeText(this, "Please specify name of the person to add", Toast.LENGTH_SHORT).show();
            return;
        }
        Person person = new Person();
        person.setName(strName);
        mDatabaseHelper.addData(person);
        Toast.makeText(this, "Data Successfully Added", Toast.LENGTH_SHORT).show();
        populateList();
    }

    public void deleteAllRecords (View v)
    {
        List<Person> list = mDatabaseHelper.GetData();
        if (null != list && list.size()>0)
        {
            mDatabaseHelper.deleteAll();
            mNewPersonNameInput.setText("");
            Toast.makeText(this, "Removed all data from the database", Toast.LENGTH_SHORT).show();
            populateList();
        }
        else
        {
            Toast.makeText(this, "No data found in the database", Toast.LENGTH_SHORT).show();
        }
    }
}
