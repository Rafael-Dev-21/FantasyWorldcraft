package com.rboladao.rgine;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private static final String TAG = "Loader.class";
    
    public RawModel loadRawModel(Context context, String path) {
       BufferedReader reader = null;
       List<String> vertexList = new ArrayList<>();
       List<String> faceList = new ArrayList<>();
       
       try {
           reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path)));
           String line = null;
           while ((line = reader.readLine()) != null) {
               String[] toks = line.split(" ");
               if (line.startsWith("# ")) continue;
               if (line.startsWith("v ")) {
                   for (int i = 1; i < toks.length; i++) {
                       if (!toks[i].isEmpty())
                       vertexList.add(toks[i]);
                   }
               }
               if (line.startsWith("f ")) {
                   for (int i = 1; i < toks.length; i++) {
                       if (!toks[i].isEmpty())
                       faceList.add(toks[i]);
                   }
               }
           }
       } catch (IOException e) {
           Log.e(TAG, "Could not open file " + path, e);
       }
       
       float[] vertexData = new float[vertexList.size()];
       for (int i = 0; i < vertexList.size(); i++)
           vertexData[i] = Float.parseFloat(vertexList.get(i));
           
       int[] faceData = new int[faceList.size()];
       for (int i = 0; i < faceList.size(); i++)
           faceData[i] = Integer.parseInt(faceList.get(i));
       
       return new RawModel(vertexData, faceData);
    }
}
