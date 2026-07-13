package com.rafael04th.fwc.physics;

import com.rafael04th.fwc.world.AABB;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.World;
import java.util.Optional;
import org.joml.Vector3f;

public class WorldCollideMover implements Integrator.Mover {
  private static final float EPSILON = 1e-6f;
  private World world;

  public void setWorld(World w) {
    world = w;
  }

  public void move(KinematicBody body, float deltaTime) {
    moveAxis(body, deltaTime, 0);
    moveAxis(body, deltaTime, 1);
    moveAxis(body, deltaTime, 2);
  }
  
  private void moveAxis(KinematicBody body, float deltaTime, int axis) {
    Vector3f pos = body.getPosition();
    Vector3f vel = body.getVelocity();
    
    float currentVel = getAxis(vel, axis);
    float currentPos = getAxis(pos, axis);
    setAxis(pos, axis, currentPos + currentVel * deltaTime);
    
    if (axis == 1) {
      body.setGrounded(false);
    }
    
    AABB bb = body.getBounds();
    
    int minI = (int)Math.floor(bb.min.x);
    int maxI = (int)Math.floor(bb.max.x)+1;
    int minJ = (int)Math.floor(bb.min.y);
    int maxJ = (int)Math.floor(bb.max.y)+1;
    int minK = (int)Math.floor(bb.min.z);
    int maxK = (int)Math.floor(bb.max.z)+1;
    
    float maxPenPositive = 0f;
    float maxPenNegative = 0f;
    
    for (int i = minI; i <= maxI; i++) {
      for (int j = minJ; j <= maxJ; j++) {
        for (int k = minK; k <= maxK; k++) {
          if (!blockCollidable(world, i, j, k)) continue;
          
          if (bb.max.x <= (i - 0.5f) + EPSILON || bb.min.x >= (i + 0.5f) - EPSILON) continue;
          if (bb.max.y <= (j - 0.5f) + EPSILON || bb.min.y >= (j + 0.5f) - EPSILON) continue;
          if (bb.max.z <= (k - 0.5f) + EPSILON || bb.min.z >= (k + 0.5f) - EPSILON) continue;
          
          float bbMin = getAABBMin(bb, axis);
          float bbMax = getAABBMax(bb, axis);
          float blockMin = getBlockMin(axis, i, j, k);
          float blockMax = getBlockMax(axis, i, j, k);
          
          float penPositive = bbMax - blockMin;
          float penNegative = blockMax - bbMin;
          
          maxPenPositive = Math.max(maxPenPositive, penPositive);
          maxPenNegative = Math.max(maxPenNegative, penNegative);
        }
      }
    }
    
    float resolvedVel = getAxis(vel, axis);
    float resolvedPos = getAxis(pos, axis);
    
    if (resolvedVel > EPSILON && maxPenPositive > EPSILON) {
      setAxis(pos, axis, resolvedPos-maxPenPositive);
      setAxis(vel, axis, 0f);
    } else if (resolvedVel < -EPSILON && maxPenNegative > EPSILON) {
      setAxis(pos, axis, resolvedPos+maxPenNegative);
      setAxis(vel, axis, 0);
      if (axis == 1) {
        body.setGrounded(true);
      }
    } else if (Math.abs(resolvedVel) <= EPSILON) {
      if (maxPenPositive > EPSILON && maxPenPositive <= maxPenNegative) {
        setAxis(pos, axis, resolvedPos-maxPenPositive);
      } else if (maxPenNegative > EPSILON) {
        setAxis(pos, axis, resolvedPos+maxPenNegative);
        if (axis == 1) {
          body.setGrounded(true);
        }
      }
    }
  }
  
  private float getAxis(Vector3f v, int axis) {
    return axis == 0 ? v.x : (axis == 1 ? v.y : v.z);
  }
  
  private void setAxis(Vector3f v, int axis, float value) {
    if (axis == 0) v.x = value;
    else if (axis == 1) v.y = value;
    else v.z = value;
  }
  
  private float getAABBMin(AABB bb, int axis) {
    return axis == 0 ? bb.min.x : (axis == 1 ? bb.min.y : bb.min.z);
  }
  
  private float getAABBMax(AABB bb, int axis) {
    return axis == 0 ? bb.max.x : (axis == 1 ? bb.max.y : bb.max.z);
  }
  
  private float getBlockMin(int axis, int i, int j, int k) {
    return axis == 0 ? i - 0.5f : (axis == 1 ? j - 0.5f : k - 0.5f);
  }
  
  private float getBlockMax(int axis, int i, int j, int k) {
    return axis == 0 ? i + 0.5f : (axis == 1 ? j + 0.5f : k + 0.5f);
  }

  private boolean blockCollidable(World w, int i, int j, int k) {
    Optional<Block> result = w.get(i, j, k);
    return result.isPresent() && result.get() != Blocks.AIR;
  }
}
