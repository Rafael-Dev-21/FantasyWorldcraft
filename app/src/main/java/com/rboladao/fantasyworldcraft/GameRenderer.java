package com.rboladao.fantasyworldcraft;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import com.rboladao.math.vector.Vector3D;
import com.rboladao.rgine.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class GameRenderer implements GLSurfaceView.Renderer {

  private float[] proj = new float[16];
  private float[] view = new float[16];
  private float[] model = new float[16];
  
  private Shader shader;
  private Renderer renderer;
  private Texture texture;
  
  private Loader loader = new Loader();
  private RawModel cube;

  private Context context;

  private float yaw = -90;
  private float pitch = 0;

  private float sensitivity = 0.05f;

  private Vector3D eye, front, up;

  public GameRenderer(Context c) {
    context = c;
  }

  public void onSurfaceCreated(GL10 unused, EGLConfig config) {

    renderer = new Renderer();
    
    renderer.setClearColor(0f, 0.8f, 0f, 1f);

    eye = new Vector3D(0, 0, 3);
    front = new Vector3D(0, 0, 0);
    up = new Vector3D(0, 1, 0);
    
    cube = loader.loadRawModel(context, "models/cube.txt");
    cube.unbind();

    shader = new Shader(context, "shaders/basic_vert.txt", "shaders/basic_frag.txt");
    shader.setUniform4f("u_Color", 0.9f, 0.9f, 0.0f, 1.0f);

    texture = new Texture(context, "palha.png");
    texture.bind();
    shader.setUniform1i("u_Texture", 0);

    shader.unbind();
  }

  public void onDrawFrame(GL10 unused) {
    renderer.clear();

    double radYaw = Math.toRadians(yaw);
    double radPitch = Math.toRadians(pitch);

    front.setX(Math.cos(radYaw) * Math.cos(radPitch));
    front.setY(Math.sin(radPitch));
    front.setZ(Math.sin(radYaw) * Math.cos(radPitch));
    front.normalizeMe();

    Vector3D target = eye.add(front);

    Matrix.setLookAtM(
        view,
        0,
        (float) eye.getX(),
        (float) eye.getY(),
        (float) eye.getZ(),
        (float) target.getX(),
        (float) target.getY(),
        (float) target.getZ(),
        (float) up.getX(),
        (float) up.getY(),
        (float) up.getZ());

    Matrix.setIdentityM(model, 0);

    long time = SystemClock.uptimeMillis() % 4000L;
    float angle = 0.090f * (float) (time);
    Matrix.rotateM(model, 0, angle, 0, 1, 0);

    shader.bind();
    shader.setUniformMat4fv("view", view, 0);
    shader.setUniformMat4fv("model", model, 0);
    renderer.drawModel(cube, shader);
  }

  public void onSurfaceChanged(GL10 unused, int width, int height) {
    GLES30.glViewport(0, 0, width, height);

    float ratio = (float) width / height;

    Matrix.perspectiveM(proj, 0, 60f, ratio, 0.01f, 100.0f);
    shader.bind();
    shader.setUniformMat4fv("proj", proj, 0);
  }

  public void rotCam(float xOffset, float yOffset) {
    xOffset = xOffset * sensitivity;
    yOffset = yOffset * sensitivity;

    yaw = yaw + xOffset;
    pitch = pitch + yOffset;

    if (pitch > 89.0f) {
      pitch = 89.0f;
    }
    if (pitch < -89.0f) {
      pitch = -89.0f;
    }
  }
}
