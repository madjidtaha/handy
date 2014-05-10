package fr.anarchy.handy.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.anarchy.handy.PokemonDatabase;
import fr.anarchy.handy.R;

public class HomePokemonCardAdapter extends ArrayAdapter {
	Context context;

	private Cursor pokemons;

	int resId;

	int[] pokemonVectorImage;


	public HomePokemonCardAdapter(Context context, PokemonDatabase db) {
		super(context, 0);
		this.context = context;

		pokemonVectorImage = new int[20];

		String packageName = this.context.getPackageName();

		for (int imageIndex = 0; imageIndex < pokemonVectorImage.length; imageIndex++) {
			resId = this.context.getResources().getIdentifier(
					"pokemon_vector_" + (imageIndex + 1), "drawable",
					packageName);
			pokemonVectorImage[imageIndex] = resId;

			Log.v("image", imageIndex + "");
		}

		String[] sqlSelect = { "0 _id", "identifier", "capture_rate" };
		String sqlTables = "pokemon_species";

		pokemons = db.getPokemonInfos(sqlSelect, sqlTables);
		pokemons.moveToFirst();
	}

	public int getCount() {
		return 20;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		pokemons.moveToPosition(position);
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();

		if (row == null) {
			row = inflater.inflate(R.layout.home_pokemon_card, parent, false);

		} else {

			 row = inflater.inflate(R.layout.home_pokemon_card, parent, false);
			
			 TextView textViewTitle = (TextView) row.findViewById(R.id.textView);
			 ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView);
			 
			 
			
			 textViewTitle.setText(pokemons.getString(1));
			 imageViewIte.setImageResource(pokemonVectorImage[position]);

		}

		// pokemons.moveToNext();
		// Log.v("pokemon",pokemons.getPosition()+"");

		return row;

	}
}
