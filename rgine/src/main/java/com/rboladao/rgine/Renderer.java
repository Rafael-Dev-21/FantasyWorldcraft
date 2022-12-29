package com.rboladao.rgine;

import android.opengl.GLES30;
import com.rboladao.rgine.RawModel;

public class Renderer {

  public Renderer() {
    GLES30.glEnable(GLES30.GL_DEPTH_TEST);
  }

  public void clear() {
    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
  }

  public void draw(VertexArray va, IndexBuffer ib, Shader shader) {
    va.bind();
    ib.bind();
    shader.bind();

    GLES30.glDrawElements(GLES30.GL_TRIANGLES, ib.size(), GLES30.GL_UNSIGNED_INT, 0);
  }
  
  public void drawModel(RawModel model, Shader shader) {
      model.bind();
      shader.bind();
      
      GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.getCount(), GLES30.GL_UNSIGNED_INT, 0);
  }

  public void setClearColor(float r, float g, float b, float a) {
    GLES30.glClearColor(r, g, b, a);
  }
}
