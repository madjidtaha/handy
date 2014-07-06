package fr.anarchy.handy.fr.anarchy.handy.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rayan on 7/6/14.
 */
public class LoadTypes extends Thread implements Runnable{

    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            List<String[]> type_names = new ArrayList<String[]>();
            List<int[]> pokemon_types_ids_slot_1 = new ArrayList<int[]>();
            List<int[]> pokemon_types_ids_slot_2 = new ArrayList<int[]>();

            // get the json we need
            JSONArray types_names_array = Pokedex.getJSONArray("type_names");
            JSONArray pokemon_types_array = Pokedex.getJSONArray("pokemon_types");

            int index = 0;

            // get pokemon type names
            for (int i = 0; i < types_names_array.length(); i++) {
                // iterate through the json objects
                JSONObject types_names_object = types_names_array.getJSONObject(i);

                int lang = Integer.parseInt(types_names_object.optString("local_language_id"));
                // get only things for our language
                if (lang == Pokedex.local_language_id) {
                    // get the json keys that we want
                    String type_name = types_names_object.optString("name");
                    String type_id = types_names_object.optString("type_id");
                    // save type ids and type names
                    type_names.add(new String[]{type_id, type_name});
                    index++;
                }
            }

            // get pokemon type ids by slot
            for (int i = 0; i < pokemon_types_array.length(); i++) {
                JSONObject pokemon_types_object = pokemon_types_array.getJSONObject(i);

                String pokemon_id = pokemon_types_object.optString("pokemon_id");
                String type_id = pokemon_types_object.optString("type_id");
                String slot = pokemon_types_object.optString("slot");

                // sort pokemon id and type id by slot
                if(slot.equals("1")) {
                    pokemon_types_ids_slot_1.add(new int[]{Integer.parseInt(pokemon_id), Integer.parseInt(type_id)});
                }else {
                    pokemon_types_ids_slot_2.add(new int[]{Integer.parseInt(pokemon_id), Integer.parseInt(type_id)});
                }
            }

            // get a list that contains pokemon id + type 1 id + type 2 id (0 if no type2 iid)
            List<int[]> pokemon_types_ids = new ArrayList<int[]>();

            // for each item in slot 1 list
            for(int i = 0; i < pokemon_types_ids_slot_1.size(); i++){
                int pokemon_id_slot_1 = pokemon_types_ids_slot_1.get(i)[0];
                int type_id_slot_1 = pokemon_types_ids_slot_1.get(i)[1];
                // add the type 1 id and 0 fir type 2
                pokemon_types_ids.add(new int[]{type_id_slot_1, 0});

                // for each type 1, iterate through all type 2 list
                for(int j = 0; j < pokemon_types_ids_slot_2.size(); j++){
                    int pokemon_id_slot_2 = pokemon_types_ids_slot_2.get(j)[0];
                    int type_id_slot_2 = pokemon_types_ids_slot_2.get(j)[1];
                    // if the ids are the same, replace the type 2 id by the right one
                    if(pokemon_id_slot_1 == pokemon_id_slot_2){
                        pokemon_types_ids.set(i, new int[]{type_id_slot_1, type_id_slot_2});
                    }
                }
            }

            // put the correct type names in the global array
            for(int i = 0; i < Pokedex.pokemonNumber; i++){
                int type_id_slot_1 = pokemon_types_ids.get(i)[0];
                int type_id_slot_2 = pokemon_types_ids.get(i)[1];
                // for each type id, loop through all the names
                for(int j = 0; j < type_names.size(); j++) {
                    String[] type_name = type_names.get(j);
                    // get the type id from the name
                    int type_id = Integer.parseInt(type_name[0]);
                    String type_name_name = type_name[1];
                    // if they match, save the name in the global variable

                    if (type_id == type_id_slot_1) {
                        // if no type 2
                        if (type_id_slot_2 == 0) {
                            Pokedex.pokemonTypes1[i] = "";
                            Pokedex.pokemonTypes2[i] = type_name_name;
                        }
                        else{
                            Pokedex.pokemonTypes1[i] = type_name_name;
                        }
                    } else if (type_id == type_id_slot_2) {
                        Pokedex.pokemonTypes2[i] = type_name_name;
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
