package com.rafael04th.fwc.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.opengl.GLES20.*;

import android.util.Log;

public class ShaderProgram {
  private final int program;
  
  private final Map<String, Integer> uniformCache = new HashMap<>();
  private final Map<String, Integer> attribCache = new HashMap<>();
  
  public ShaderProgram(String vertSrc, String fragSrc) {
    program = makeProgram(vertSrc, fragSrc);
  }
  
  public void bind() {
    glUseProgram(program);
  }
  public void unbind() {
    glUseProgram(0);
  }
  
  public void setAttribs(VertexFormat fmt) {
    int stride = fmt.getStride();
    List<VertexFormat.Element> els = fmt.getElements();
    int offset = 0;
    for (VertexFormat.Element el : els) {
      int size = VertexFormat.sizeForType(el.type);
      glEnableVertexAttribArray(getAttribLoc(el.name));
      glVertexAttribPointer(getAttribLoc(el.name), el.count, el.type, el.normalized, stride, offset);
      offset += el.count * size;
    }
  }
  
  public void setUniform1i(String name, int i) {
    glUniform1i(getUniformLoc(name), i);
  }
  public void setUniformMat4(String name, float[] mat, int i) {
    glUniformMatrix4fv(getUniformLoc(name), 1, false, mat, i);
  }
  public void setUniform3f(String name, float x, float y, float z) {
    glUniform3f(getUniformLoc(name), x, y, z);
  }
  public void setUniform4f(String name, float x, float y, float z, float w) {
    glUniform4f(getUniformLoc(name), x, y, z, w);
  }
  
  public int getAttribLoc(String name) {
    if (attribCache.containsKey(name)) {
      try {
        return attribCache.get(name);
      } catch (NullPointerException e) {
        Log.e("ShaderProgram.class", "Could not get attribute", e);
      }
    }
    int loc = glGetAttribLocation(program, name);
    attribCache.put(name, loc);
    return loc;
  }
  
   public int getUniformLoc(String name) {
    if (uniformCache.containsKey(name)) {
      try {
        return uniformCache.get(name);
      } catch (NullPointerException e) {
        Log.e("ShaderProgram.class", "Could not return uniform", e);
      }
    }
    int loc = glGetUniformLocation(program, name);
    uniformCache.put(name, loc);
    return loc;
  }
  
  private int makeShader(int type, String source) {
    int shader = glCreateShader(type);
    glShaderSource(shader, source);
    glCompileShader(shader);
    
    int[] status = new int[1];
    glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
    if (status[0] == GL_FALSE) {
      String log = glGetShaderInfoLog(shader);
      glDeleteShader(shader);
      throw new RuntimeException("Shader compile failed:\n" + log);
    }
    return shader;
  }
  private int makeProgram(String vertSource, String fragSource) {
    int vertShader = makeShader(GL_VERTEX_SHADER, vertSource);
    int fragShader = makeShader(GL_FRAGMENT_SHADER, fragSource);
    int program = glCreateProgram();
    glAttachShader(program, vertShader);
    glAttachShader(program, fragShader);
    glLinkProgram(program);
    
    int[] status = new int[1];
    glGetProgramiv(program, GL_LINK_STATUS, status, 0);
    if (status[0] == GL_FALSE) {
      String log = glGetProgramInfoLog(program);
      glDeleteProgram(program);
      throw new RuntimeException("Program link failed:\n" + log);
    }
    
    glDetachShader(program, vertShader);
    glDetachShader(program, fragShader);
    glDeleteShader(vertShader);
    glDeleteShader(fragShader);
    return program;
  }
  
  public void dispose() {
    glDeleteProgram(program);
  }
}
