package fr.anarchy.handy.fr.anarchy.handy.json;

import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.anarchy.handy.MainActivity;

/**
 * Created by rayan on 6/21/14.
 */
public class LoadJSON extends Thread implements Runnable {
    InputStream archiveInputStream;
    AssetManager assetManager;
    String gzipName;

    public LoadJSON(AssetManager am, String gzn) {
        assetManager = am;
        gzipName = gzn;
    }

    public void getJSONFilesInArray() {
        try {
            archiveInputStream = assetManager.open(gzipName);
            // the BufferedInputStream seems to be more eficient
            InputStream buffIs = new BufferedInputStream(archiveInputStream);
            int len = 0;
            int reading = 0;
            while ((len = reading) >= 0) {
                StringWriter writer = new StringWriter();
                // Apache IOCommons library helps with getting the string from the stream
                IOUtils.copy(buffIs, writer);
                // put the json content in the map with the file name for key
                try {
                    gzipName = gzipName.replaceAll(".json", "");
                    gzipName = gzipName.replaceAll(Pokedex.jsonDir+"/", "");
                    Pokedex.pokedexJSONObject.put(gzipName, writer.toString());
                } catch (JSONException e) {
                }
                //continue reading
                reading = buffIs.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // put thread in background so that it doesn't compete with ui thread
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        getJSONFilesInArray();

    }
}

