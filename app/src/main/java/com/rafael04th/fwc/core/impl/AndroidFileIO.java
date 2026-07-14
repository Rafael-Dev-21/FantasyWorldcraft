package com.rafael04th.fwc.core.impl;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.rafael04th.fwc.io.FileIO;

public class AndroidFileIO implements FileIO {
  private final AssetManager am;
  private final File extDir;
  
  AndroidFileIO(Context context) {
    this.am = context.getAssets();
    this.extDir = context.getExternalFilesDir(null);
  }
  
  public InputStream loadAsset(String filename) throws IOException {
    return am.open(filename);
  }
  
  public InputStream loadFile(String filename) throws IOException {
    File file = new File(extDir, filename);
    return new FileInputStream(file);
  }
  
  public OutputStream saveFile(String filename) throws IOException {
    File file = new File(extDir, filename);
    return new FileOutputStream(file);
  }
}
