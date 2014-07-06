package fr.anarchy.handy.fr.anarchy.handy.json;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rayan on 7/6/14.
 */
public class LoadNames extends Thread implements Runnable{
    // this thread updates the names of pokemon cards
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            try {
                // get the json we need
                JSONArray ja = Pokedex.getJSONArray("pokemon_species_names");

                int index = 0;
                for (int i = 0; i < ja.length(); i++) {
                    // iterate through the json objects
                    JSONObject jo = ja.getJSONObject(i);

                    int lang = Integer.parseInt(jo.optString("local_language_id"));
                    // get only things for our language
                    if (lang == Pokedex.local_language_id) {
                        // get the json key that we want
                        String name = jo.optString("name");
                        // save in global variable
                        if(index < Pokedex.pokemonNames.length) {
                            Pokedex.pokemonNames[index] = name;
                        }
                        index++;
                    }
                }
            } catch (Exception e) {
            }
        }

}
