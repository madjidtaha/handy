package fr.anarchy.handy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import java.util.logging.LogRecord;

import fr.anarchy.handy.fr.anarchy.handy.json.AssembleJSONThreadPool;
import fr.anarchy.handy.fr.anarchy.handy.json.GlobalJSON;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, StatsCalcFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    Fragment fragment;
    FragmentManager fragmentManager;

    AssembleJSONThreadPool assemble;

   public Handler myHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        new GlobalJSON();

        myHandler = new Handler();

        assemble = new AssembleJSONThreadPool(this);

        assemble.start();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position){
            case 0 :
                fragment = new GridGplayFragment();
                break;
            case 1 :
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

    public GridGplayFragment getGridPlayFragment(){
        return (GridGplayFragment) fragment;
    }

}
