package com.rboladao.math.vector;

import java.io.Serializable;

public class Vector3D implements Cloneable, Comparable<Vector3D>, Serializable {

  private static final long serialVersionUID = 0l;

  private double _x;
  private double _y;
  private double _z;

  public Vector3D() {
    _x = _y = _z = 0;
  }

  public Vector3D(double x, double y, double z) {
    _x = x;
    _y = y;
    _z = z;
  }

  public Vector3D(Vector2D plane) {
    this(plane, 0);
  }

  public Vector3D(Vector2D plane, double depth) {
    _x = plane.getX();
    _y = plane.getY();
    _z = depth;
  }

  public Vector3D(Vector3D old) {
    _x = old._x;
    _y = old._y;
    _z = old._z;
  }

  public void setX(double x) {
    _x = x;
  }

  public void setY(double y) {
    _y = y;
  }

  public void setZ(double z) {
    _z = z;
  }

  public double getX() {
    return _x;
  }

  public double getY() {
    return _y;
  }

  public double getZ() {
    return _z;
  }

  public double size() {
    return Math.sqrt(_x * _x + _y * _y + _z * _z);
  }
  
  public Vector3D negateMe() {
    _x = -_x;
    _y = -_y;
    _z = -_z;
    return this;
  }
  
  public Vector3D negate() {
    return clone().negateMe();
  }

  public Vector3D addMe(Vector3D other) {
    this._x += other._x;
    this._y += other._y;
    this._z += other._z;
    return this;
  }

  public Vector3D add(Vector3D other) {
    return clone().addMe(other);
  }

  public Vector3D subtractMe(Vector3D other) {
    this._x -= other._x;
    this._y -= other._y;
    this._z -= other._z;
    return this;
  }
  
  public Vector3D subtract(Vector3D other) {
    return clone().subtractMe(other);
  }
  
  public Vector3D multiplyMe(double scalar) {
    this._x *= scalar;
    this._y *= scalar;
    this._z *= scalar;
    return this;
  }
  
  public Vector3D multiply(double scalar) {
    return clone().multiplyMe(scalar);
  }
  
  public Vector3D divideMe(double scalar) {
    this._x /= scalar;
    this._y /= scalar;
    this._z /= scalar;
    return this;
  }
  
  public Vector3D divide(double scalar) {
    return clone().divideMe(scalar);
  }
  
  public Vector3D normalizeMe() {
    return divideMe(size());
  }
  
  public Vector3D normalize() {
    return clone().normalizeMe();
  }
  
  public Vector3D resizeMe(double size) {
    return normalizeMe().multiplyMe(size);
  }
  
  public Vector3D resize(double size) {
    return clone().resizeMe(size);
  }
  
  public double distance(Vector3D other) {
    return this.subtractMe(other).size();
  }
  
  public double dot(Vector3D other) {
    return this._x * other._x + this._y * other._y + this._z * other._z;
  }
  
  public Vector3D crossMe(Vector3D other) {
    double newX = this._y * other._z - this._z * other._y;
    double newY = this._z * other._x - this._x * other._z;
    double newZ = this._x * other._y - this._y * other._x;
    
    this._x = newX;
    this._y = newY;
    this._z = newZ;
    
    return this;
  }
  
  public Vector3D cross(Vector3D other) {
    return this.clone().crossMe(other);
  }
  
  public Vector3D rotateMeX(double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    
    double newY = cos * _y - sin * _z;
    double newZ = sin * _y + cos * _z;
    
    this._y = newY;
    this._z = newZ;
    
    return this;
  }
  
  public Vector3D rotateX(double angle) {
    return clone().rotateMeX(angle);
  }
  
  public Vector3D rotateMeY(double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    
    double newZ = cos * _z - sin * _x;
    double newX = sin * _z + cos * _x;
    
    this._x = newX;
    this._z = newZ;
    
    return this;
  }
  
  public Vector3D rotateY(double angle) {
    return clone().rotateMeY(angle);
  }
  
  public Vector3D rotateMeZ(double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    
    double newX = cos * _x - sin * _y;
    double newY = sin * _x + cos * _y;
    
    this._x = newX;
    this._y = newY;
    
    return this;
  }
  
  public Vector3D rotateZ(double angle) {
    return clone().rotateMeZ(angle);
  }
  
  public Vector3D rotateMeAxis(Vector3D axis, double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    double k = 1.0 - cos;
    
    double newX = _x * (cos + k * axis._x * axis._x) + _y * (k * axis._x * axis._y - sin * axis._z) + _z * (k * axis._x * axis._z + sin * axis._y);

    double newY = _x * (k * axis._x * axis._y + sin * axis._z) + _y * (cos + k * axis._y * axis._y) + _z * (k * axis._y * axis._z - sin * axis._x);

    double newZ = _x * (k * axis._x * axis._z - sin * axis._y) + _y * (k * axis._y * axis._z + sin * axis._x) + _z * (cos + k * axis._z * axis._z);

    _x = newX;
    _y = newY;
    _z = newZ;

    return this;
  }
  
  public Vector3D rotateAxis(Vector3D axis, double angle) {
    return this.clone().rotateMeAxis(axis, angle);
  }
  
  public static double angleBetween(Vector3D left, Vector3D right) {
    return Math.acos(left.dot(right) / (left.size()*right.size()));
  }

  @Override
  public Vector3D clone() {
    try {
      return (Vector3D) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
  
  @Override
  public int compareTo(Vector3D other) {
    return (int) (this.size() - other.size());
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Vector3D)) return false;
    Vector3D other = (Vector3D) o;
    return _x == other._x && _y == other._y && _z == other._z;
  }
  
  @Override
  public int hashCode() {
    int result = 17;

    result = result * 31 + hashDouble(_x);
    result = result * 31 + hashDouble(_y);
    result = result * 31 + hashDouble(_z);

    return result;
  }

  private int hashDouble(double d) {
    long longBits = Double.doubleToLongBits(d);
    return (int) (longBits ^ (longBits >>> 32));
  }

  @Override
  public String toString() {
    return "{x: " + _x + ", y:" + _y + ", z: " + _z + "}";
  }
}
