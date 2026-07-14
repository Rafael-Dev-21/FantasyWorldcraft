package com.rafael04th.fwc.render;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDisableVertexAttribArray;

import android.util.Log;

import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Transform;
import com.rafael04th.fwc.graphics.VoxelFormats;
import com.rafael04th.fwc.graphics.WireFrameMesher;
import com.rafael04th.fwc.world.BlockHit;

import org.joml.Matrix4f;

import java.util.Optional;

public class SelectionRenderer {
    private static final float SCALE = 1.01f;
    private final float[] scratch = new float[16];
    private final Matrix4f scratchMat = new Matrix4f();// Owning
    private final Transform transform;
    // Owning
    private final ShaderProgram program;
    // Owning
    private final Mesh mesh;
    public SelectionRenderer(ShaderProgram program) {
        this.transform = new Transform();
        this.program = program;
        this.transform.scale.set(SCALE, SCALE, SCALE);
        this.mesh = new Mesh(new WireFrameMesher().cube(new float[]{0,0,0}).build(), VoxelFormats.JUST_POSITION);
    }
    public void render(Camera camera, Optional<BlockHit> result) {
        if (result.isPresent()) {
            BlockHit hit = result.get();
            transform.translation.set(hit.x, hit.y, hit.z);
            program.bind();
            mesh.bind();
            camera.getCameraMatrix().mul(transform.getTransformMatrix(), scratchMat).get(scratch);
            program.setAttribs(mesh.getFormat());
            program.setUniformMat4("uMVP", scratch, 0);
            program.setUniform4f("uColor", 1f, 0, 1f, 1f);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDepthMask(false);
            mesh.render(GL_LINES);
            glDepthMask(true);
        }
    }

    public void dispose() {
        mesh.dispose();
        program.dispose();
    }
}
