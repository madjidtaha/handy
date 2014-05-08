package fr.anarchy.handy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import fr.anarchy.handy.adapters.HomePokemonCardAdapter;

public class HomeActivity extends Activity {

	GridView gridView;
	HomePokemonCardAdapter homeCardAdapter;
	
	public PokemonDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_grid_view);
		
		db = new PokemonDatabase(this);

		gridView = (GridView) findViewById(R.id.gridViewCustom);
		// Create the Custom Adapter Object
		homeCardAdapter = new HomePokemonCardAdapter(this, db);
//		// Set the Adapter to GridView
		gridView.setAdapter(homeCardAdapter);


	}
}
