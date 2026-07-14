package com.rafael04th.fwc.graphics.render;

import android.opengl.GLES20;
import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.MeshBuilder;
import com.rafael04th.fwc.graphics.MeshData;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.graphics.TextureRegion;
import com.rafael04th.fwc.graphics.VoxelFormats;

public class SpriteBatcher {
  private Mesh mesh = null;
  private MeshBuilder meshBuilder;
  int numSprites;

  public SpriteBatcher(int maxSprites) {
    meshBuilder = new MeshBuilder(false, true, false, 1, 1);
    numSprites = 0;

    int j = 0;
    for (int i = 0; i < maxSprites; i++, j += 4) {
      meshBuilder.indices(
        j+0, j+1, j+2,
        j+2, j+3, j+0
      );
    } 
  }

  public void beginBatch(ShaderProgram program, Texture texture) {
    program.bind();
    texture.bind();
    program.setUniform1i("uTex", 0);
    meshBuilder.clear(false, true);
    numSprites = 0;
  }

  public void endBatch(ShaderProgram program) {
    GLES20.glDepthMask(false);
    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    GLES20.glDisable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    MeshData md = meshBuilder.build();
    if (mesh == null)
      mesh = new Mesh(md, VoxelFormats.POSITION_UV);
    else
      mesh.reuploadData(md);
    mesh.bind();
    program.setAttribs(mesh.getFormat());
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, numSprites*6, GLES20.GL_UNSIGNED_INT, 0);
    GLES20.glEnable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glDepthMask(true);
    GLES20.glDisable(GLES20.GL_BLEND);
  }

  public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
    float halfWidth = width/2;
    float halfHeight = height/2;
    float x1 = x - halfWidth;
    float y1 = y - halfHeight;
    float x2 = x + halfWidth;
    float y2 = y + halfHeight;

    meshBuilder
      .vertex(x1, y1, 1, 0, 0, 0, region.u1, region.v2, 0, 0, 0)
      .vertex(x2, y1, 1, 0, 0, 0, region.u2, region.v2, 0, 0, 0)
      .vertex(x2, y2, 1, 0, 0, 0, region.u2, region.v1, 0, 0, 0)
      .vertex(x1, y2, 1, 0, 0, 0, region.u1, region.v1, 0, 0, 0);

    numSprites++;
  }

  public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
    float halfWidth = width/2;
    float halfHeight = height/2;

    float rad = (float)(angle * Math.PI / 180f);
    float cos = (float)Math.cos(rad);
    float sin = (float)Math.sin(rad);

    float x1 = -halfWidth * cos - (-halfHeight) * sin;
    float y1 = -halfWidth * sin + (-halfHeight) * cos;
    float x2 = halfWidth * cos - (-halfHeight) * sin;
    float y2 = halfWidth * sin + (-halfHeight) * cos;
    float x3 = halfWidth * cos - halfHeight * sin;
    float y3 = halfWidth * sin + halfHeight * cos;
    float x4 = -halfWidth * cos - halfHeight * sin;
    float y4 = -halfWidth * sin + halfHeight * cos;
    
    x1 += x;
    y1 += y;
    x2 += x;
    y2 += y;
    x3 += x;
    y3 += y;
    x4 += x;
    y4 += y;

    meshBuilder
      .vertex(x1, y1, 1, 0, 0, 0, region.u1, region.v2, 0, 0, 0)
      .vertex(x2, y2, 1, 0, 0, 0, region.u2, region.v2, 0, 0, 0)
      .vertex(x3, y3, 1, 0, 0, 0, region.u2, region.v1, 0, 0, 0)
      .vertex(x4, y4, 1, 0, 0, 0, region.u1, region.v1, 0, 0, 0);

    numSprites++;
  }

    public void dispose() {
      if (mesh != null) mesh.dispose();
    }
}
