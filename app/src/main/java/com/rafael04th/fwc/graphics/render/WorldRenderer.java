package com.rafael04th.fwc.graphics.render;

import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.graphics.Transform;
import com.rafael04th.fwc.world.AABB;
import com.rafael04th.fwc.world.Chunk;
import com.rafael04th.fwc.world.World;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;

public class WorldRenderer {

  private final ShaderProgram program;
  private final Camera camera;
  private final Texture texture;

  private final Transform t = new Transform();
  private final Matrix4f mvp = new Matrix4f();
  private final float[] scratch = new float[16];

  public WorldRenderer(ShaderProgram sp, Camera cam, Texture tex) {
    program = sp;
    camera = cam;
    texture = tex;
  }

  public void render(World world) {
    program.bind();
    texture.bind();
    program.setUniform1i("uTex", 0);

    for (int i = -3; i < 4; i++) {
      for (int j = -3; j < 4; j++) {
        int cx = (int) Math.floor(camera.getPosition().x / Chunk.WIDTH) + i;
        int cz = (int) Math.floor(camera.getPosition().z / Chunk.DEPTH) + j;
        Chunk c = world.at(cx, cz);
        if (c == null) continue;
        
        FrustumIntersection frustum = camera.getFrustum();
        AABB chunkBounds = c.getBounds();
        int result = frustum.intersectAab(chunkBounds.min, chunkBounds.max);
        if (result == FrustumIntersection.OUTSIDE)
          continue;

        Mesh m = c.getMesh();
        if (m == null) continue;

        m.bind();
        t.translation.set(c.x() * Chunk.WIDTH, 0, c.z() * Chunk.DEPTH);
        camera.getCameraMatrix().mul(t.getTransformMatrix(), mvp).get(scratch);
        program.setUniformMat4("uMVP", scratch, 0);
        program.setAttribs(m.getFormat());
        m.render();
      }
    }
  }
}
