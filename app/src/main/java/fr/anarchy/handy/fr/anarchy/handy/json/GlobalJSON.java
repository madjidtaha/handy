package fr.anarchy.handy.fr.anarchy.handy.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rayan on 6/21/14.
 */
public class GlobalJSON {
    // language used
    public static int local_language_id = 9;
    public static int pokemonNumber = 151;

    // number of files in pokedex.zip
    public static int nbJSONFiles = 170;

    public static JSONObject pokedexJSONObject;

    /* we put all json files content in a map
    seperating the content like this helps with performance
    and we can refer to it easily */
    public static Map<String, String>pokedexMap;

    public static String[] pokemonNames;

    public GlobalJSON(){

        pokemonNames = new String [pokemonNumber];

        for (int i = 0; i<pokemonNames.length; i++){
            pokemonNames[i] = ".................";
        }

        pokedexMap = new HashMap<String, String>();

        pokedexJSONObject = new JSONObject();

    }

    public static JSONArray getJSONArray(String jsonReq) {

        JSONObject jsonObject;
        JSONArray jsonArray = null;
        String jsonString;

        try {

            jsonString = pokedexJSONObject.getString(jsonReq);

            jsonArray = new JSONArray(jsonString);

        } catch (JSONException e) {

            Log.e("json error", e.getMessage());

        }

        return jsonArray;

    }
}
