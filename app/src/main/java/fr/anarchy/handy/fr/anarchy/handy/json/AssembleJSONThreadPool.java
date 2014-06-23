package fr.anarchy.handy.fr.anarchy.handy.json;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

import fr.anarchy.handy.GridGplayFragment;
import fr.anarchy.handy.MainActivity;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by rayan on 6/21/14.
 */
public class AssembleJSONThreadPool extends Thread implements Runnable {

    MainActivity main;
    InputStream archiveInputStream;
    AssetManager assManager;

    ExecutorService executor;
    Runnable worker;

    public AssembleJSONThreadPool(MainActivity m) {
        main = m;
        assManager = main.getAssets();
        archiveInputStream = null;
    }

    public void run() {

        // put thread in background so that it doesn't compete with ui thread
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        // get the input stream of the archie file
        try {
            // it's a simple inputstream so it's not uncompressed yet
            archiveInputStream = assManager.open("pokedex.zip");

            // the tread pool helps knowing when the thread is finished
            executor = Executors.newFixedThreadPool(1);
            // executes the thread
            worker = new LoadJSONPokedex(archiveInputStream);
            executor.execute(worker);

            executor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!executor.isTerminated()) {

        }

        // the json files are totally read, we can now safley use the global json variable

        // this thread updates the names of pokemon cards
        main.myHandler.post(new Runnable() {

            public void run() {

                try {

                    // get the json we need
                    JSONArray ja = GlobalJSON.getJSONArray("pokemon_species_names");

                    Log.v("ja", ja.length()+"");

                    int index = 0;

                    for (int i = 0; i < ja.length(); i++) {
                        // iterate through the json objects
                        JSONObject jo = ja.getJSONObject(i);

                        String lang = jo.optString("local_language_id");

                        Log.v("lang", lang);

                        // get only things for our language
                        if (lang.equals(GlobalJSON.local_language_id+"")) {
                            // get the json key that we want
                            String name = jo.optString("name");
                            // get the grid
                            GridGplayFragment gFrag = main.getGridPlayFragment();

                            Log.v("index", index+"");
                            Log.v("name", name);


                            if(index < GlobalJSON.pokemonNames.length) {
                                GlobalJSON.pokemonNames[index] = name;
                            }

                            // chane the name
                            gFrag.changeCardsName(index);

                            index++;

                        }

                    }

                } catch (Exception e) {

                }
            }
        });
    }
}
