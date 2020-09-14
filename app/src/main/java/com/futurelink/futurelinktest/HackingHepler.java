package com.futurelink.futurelinktest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HackingHepler {

    public static final String directory  = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        if (files != null) {
            for (String filename : files) {

                if (filename.equals("image.jpg") || filename.equals("resume.pdf") || filename.equals("code.java")) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = assetManager.open(filename);

                        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
                        File directory = contextWrapper.getDir("resources", Context.MODE_PRIVATE);

                        File outFile = new File(directory, filename);

                        Log.d("Saving file:", outFile.getName());
                        Log.d("In Path:", outFile.getAbsolutePath());

                        out = new FileOutputStream(outFile);
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        Log.e("tag", "Failed to copy asset file: " + filename, e);
                    }
                }
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
