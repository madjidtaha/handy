package fr.anarchy.handy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class PokemonDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "pokedex";
	private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;
    SQLiteQueryBuilder qb;

    public PokemonDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// you can use an alternate constructor to specify a database location 
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

        db = getReadableDatabase();
        qb = new SQLiteQueryBuilder();
    }

	public Cursor getPokemonInfos(String [] query, String selector, String sqlTables) {



//		String [] sqlSelect = {"0 _id", "identifier", "capture_rate"}; 
//		String sqlTables = "pokemon_species";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, query, selector, null,
				null, null, null);

		c.moveToFirst();
		return c;

	}

    public Cursor rawQuery(String query){
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

}
