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
public class Pokedex {
    public static String jsonDir = "json";
    // language used
    public static int local_language_id = 9;
    public static int pokemonNumber = 649;

    // number of files in pokedex.zip
    public static int nbJSONFiles = 170;

    public static JSONObject pokedexJSONObject;

    /* we put all json files content in a map.
    Dividing the content like this helps with performance
    and we can refer to it easily */
    public static Map<String, String>pokedexMap;

    /* Global arrays we need to refer to because they are loaded in memory
     after the related JSON is parsed in the treadpool */
    public static String[] pokemonNames;
    public static String[] pokemonTypes1;
    public static String[] pokemonTypes2;

    public Pokedex(){
        // instantiations only
        pokedexMap = new HashMap<String, String>();
        pokedexJSONObject = new JSONObject();

        pokemonNames = new String [pokemonNumber];
        pokemonTypes1 = new String [pokemonNumber];
        pokemonTypes2 = new String [pokemonNumber];

        for (int i = 0; i < pokemonNames.length; i++){
            pokemonNames[i] = ".................";
            pokemonTypes1[i] = "type1";
            pokemonTypes2[i] = "type2";
        }
    }

    // helps getting a json array in a more concise way
    public static JSONArray getJSONArray(String jsonReq) {
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
