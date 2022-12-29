package com.rboladao.rgine;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Shader {
  private static final String TAG = "Shader.class";

  private int rendererID;
  private Map<String, Integer> uniformCache = new HashMap<>();

  public Shader(Context context, String vertPath, String fragPath) {

    String vertSource = loadShader(context, vertPath);
    String fragSource = loadShader(context, fragPath);
    rendererID = createShader(vertSource, fragSource);
  }

  public void bind() {
    GLES30.glUseProgram(rendererID);
  }

  public void unbind() {
    GLES30.glUseProgram(0);
  }

  public void finalize() {
    GLES30.glDeleteProgram(rendererID);
  }

  public void setUniform1i(String name, int value) {
    GLES30.glUniform1i(getUniformLocation(name), value);
  }

  public void setUniform1f(String name, float value) {
    GLES30.glUniform1f(getUniformLocation(name), value);
  }

  public void setUniform4f(String name, float f0, float f1, float f2, float f3) {
    GLES30.glUniform4f(getUniformLocation(name), f0, f1, f2, f3);
  }

  public void setUniformMat4fv(String name, float[] matrix, int offset) {
    GLES30.glUniformMatrix4fv(getUniformLocation(name), 1, false, matrix, offset);
  }

  private int getUniformLocation(String name) {
    if (uniformCache.containsKey(name)) {
      return uniformCache.get(name);
    }

    int location = GLES30.glGetUniformLocation(rendererID, name);
    if (location == -1) {
      Log.e(TAG, "Uniform location " + name + " is -1");
    }
    uniformCache.put(name, location);
    return location;
  }

  private int compilerShader(int type, String source) {
    int id = GLES30.glCreateShader(type);
    GLES30.glShaderSource(id, source);
    GLES30.glCompileShader(id);

    int[] status = new int[1];

    GLES30.glGetShaderiv(id, GLES30.GL_COMPILE_STATUS, status, 0);
    if (status[0] == 0) {
      Log.e(TAG, GLES30.glGetShaderInfoLog(id));
      GLES30.glDeleteShader(id);
      return 0;
    }

    return id;
  }

  private int createShader(String vertSource, String fragSource) {
    int program = GLES30.glCreateProgram();

    int vs = compilerShader(GLES30.GL_VERTEX_SHADER, vertSource);
    int fs = compilerShader(GLES30.GL_FRAGMENT_SHADER, fragSource);

    GLES30.glAttachShader(program, vs);
    GLES30.glAttachShader(program, fs);
    GLES30.glLinkProgram(program);
    GLES30.glValidateProgram(program);

    GLES30.glDetachShader(program, vs);
    GLES30.glDetachShader(program, fs);
    GLES30.glDeleteShader(vs);
    GLES30.glDeleteShader(fs);

    return program;
  }

  private String loadShader(Context context, String filepath) {
    BufferedReader reader = null;
    try {

      reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filepath)));

      StringBuilder result = new StringBuilder();

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line).append("\n");
      }

      return result.toString();

    } catch (IOException e) {
      Log.e(TAG, "Could not load: " + filepath, e);
      return "";
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          Log.e(TAG, "Could not close: " + filepath, e);
        }
      }
    }
  }
}
