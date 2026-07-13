package com.rafael04th.fwc.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
  InputStream loadAsset(String filename) throws IOException;
  InputStream loadFile(String filename) throws IOException;
  OutputStream saveFile(String filename) throws IOException;
}
