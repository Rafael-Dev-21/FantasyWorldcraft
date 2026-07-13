package com.rafael04th.fwc.graphics;

import static android.opengl.GLES20.*;

public class VoxelFormats {
  public static final VertexFormat POSITION_UV = new VertexFormat();
  public static final VertexFormat POSITION_UV_2D = new VertexFormat();
  public static final VertexFormat JUST_POSITION = new VertexFormat();
  public static final VertexFormat POSITION_UV_COLOR = new VertexFormat();
  
  static {
    POSITION_UV.push(GL_FLOAT, 3, false, "aPosition");
    POSITION_UV.push(GL_FLOAT, 2, false, "aTexCoord");
    
    POSITION_UV_2D.push(GL_FLOAT, 2, false, "aPosition");
    POSITION_UV_2D.push(GL_FLOAT, 2, false, "aTexCoord");
    
    JUST_POSITION.push(GL_FLOAT, 3, false, "aPosition");

    POSITION_UV_COLOR.push(GL_FLOAT, 3, false, "aPosition");
    POSITION_UV_COLOR.push(GL_FLOAT, 2, false, "aTexCoord");
    POSITION_UV_COLOR.push(GL_FLOAT, 3, false, "aColor");
  }
}
