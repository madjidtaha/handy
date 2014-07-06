package fr.anarchy.handy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.anarchy.handy.fr.anarchy.handy.json.LoadJSON;
import fr.anarchy.handy.fr.anarchy.handy.json.LoadNames;
import fr.anarchy.handy.fr.anarchy.handy.json.LoadTypes;
import fr.anarchy.handy.fr.anarchy.handy.json.Pokedex;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, StatsCalcFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    Fragment fragment;
    FragmentManager fragmentManager;

    AssetManager assetManager;
    String[] jsonList;

    ExecutorService executor;
    Runnable worker;
    public Handler myHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        new Pokedex();
        myHandler = new Handler();
        assetManager = getAssets();

        try {
            // put list of file names in a string array
            jsonList = assetManager.list(Pokedex.jsonDir);

            // the tread pool helps knowing when the thread is finished
            executor = Executors.newFixedThreadPool(jsonList.length);
            for (int i = 0; i < jsonList.length; i++) {
                // executes threads to load json files in parallel
                worker = new LoadJSON(assetManager, Pokedex.jsonDir + "/" + jsonList[i]);
                executor.execute(worker);
            }

            executor.shutdown();
            // wait until json files are loaded
            while (!executor.isTerminated()) { }

            executor = Executors.newFixedThreadPool(2);
            // update pokemonNames global array
            executor.execute(new LoadNames());
            // update pokemonTypes global array
            executor.execute(new LoadTypes());

            executor.shutdown();
            // wait until global arrays are available
            while (!executor.isTerminated()) { }

            // update the home cards
            myHandler.post(new Runnable() {
                public void run() {
                    getGridPlayFragment().updateCards();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                fragment = new GridGplayFragment();
                break;
            case 1:
                fragment = new StatsCalcFragment();
                break;
        }

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public GridGplayFragment getGridPlayFragment() {
        return (GridGplayFragment) fragment;
    }

}
