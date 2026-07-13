package com.rafael04th.fwc.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {

  public Vector3f translation;
  public Vector3f scale;
  public Quaternionf rotation;
  private Matrix4f transformMatrix;
  
  public Transform() {
    translation = new Vector3f(0, 0, 0);
    scale = new Vector3f(1, 1, 1);
    rotation = new Quaternionf();
    transformMatrix = new Matrix4f();
  }
  
  public Matrix4f getTransformMatrix() {
     return transformMatrix
       .identity()
       .translate(translation)
       .rotate(rotation)
       .scale(scale);
  }
  
  public void get(float[] mat4) {
    getTransformMatrix().get(mat4);
  }
}
