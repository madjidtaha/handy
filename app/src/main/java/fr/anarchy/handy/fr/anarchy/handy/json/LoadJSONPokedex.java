package fr.anarchy.handy.fr.anarchy.handy.json;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by rayan on 6/21/14.
 */
public class LoadJSONPokedex extends Thread implements Runnable {

    InputStream archiveInputStream;

    public LoadJSONPokedex(InputStream ais) {
        archiveInputStream = ais;
    }

    public void run() {

        // put thread in background so that it doesn't compete with ui thread
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        getJSONFilesInArray();

    }

    public void getJSONFilesInArray() {
        // the BufferedInputStream seems to be more eficient
        InputStream buffIs = new BufferedInputStream(archiveInputStream);
        // this is the uncompressed zip stream
        ZipInputStream zis = new ZipInputStream(buffIs);
        ZipEntry ze = null;

        // index to track the current file in the archive stream
        int jsonIndex = 0;

        try {
            // iterate through all files in the archive stream
            while ((ze = zis.getNextEntry()) != null) {
                // security measure
                if (jsonIndex < GlobalJSON.nbJSONFiles) {
                    // length of the stream buffer
                    int len = 0;
                    int reading = 0;
                    // read the stream
                    while ((len = reading) >= 0) {

                        StringWriter writer = new StringWriter();

                        // Apache IOCommons library helps with getting the string from the stream
                        IOUtils.copy(zis, writer);

                        // put the json content in the map with the file name for key
                        // GlobalJSON.pokedexMap.put(ze.getName().replaceAll(".json",""), writer.toString());
                        try {
                            GlobalJSON.pokedexJSONObject.put(ze.getName().replaceAll(".json", ""), writer.toString());
                        } catch (JSONException e) {
                         //   Log.e("error",e.getMessage());
                        }

                        reading = zis.read();
                    }
                    jsonIndex++;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
