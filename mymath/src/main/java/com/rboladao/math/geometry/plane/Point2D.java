package com.rboladao.math.geometry.plane;

import java.io.Serializable;

public class Point2D implements Cloneable, Serializable {

  private static final long serialVersionUID = 0L;

  private double _x;
  private double _y;

  public Point2D() {
    _x = _y = 0;
  }

  public Point2D(double x, double y) {
    _x = x;
    _y = y;
  }

  public void setX(double x) {
    _x = x;
  }

  public void setY(double y) {
    _y = y;
  }

  public double getX() {
    return _x;
  }

  public double getY() {
    return _y;
  }

  public double distance(Point2D other) {
    double disX = _x - other._x;
    double disY = _y - other._y;

    return Math.sqrt(disX * disX + disY * disY);
  }

  public boolean isCollide(Point2D other) {
    return this.equals(other) && this != other;
  }

  @Override
  public Point2D clone() {
    try {
      return (Point2D) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Point2D)) return false;
    Point2D point = (Point2D) o;
    return this._x == point._x && this._y == point._y;
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = result * 31 + hashDouble(_x);
    result = result * 31 + hashDouble(_y);

    return result;
  }

  private int hashDouble(double d) {
    long longBits = Double.doubleToLongBits(d);
    return (int) (longBits ^ (longBits >>> 32));
  }

  @Override
  public String toString() {
    return "[" + _x + ", " + _y + "]";
  }
}
