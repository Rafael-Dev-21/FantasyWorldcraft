package com.rboladao.math.geometry.plane;

import java.io.Serializable;

public class Circle implements Cloneable, Comparable<Circle>, Serializable {

  private static final long serialVersionUID = 0l;

  private Point2D _point;
  private double _radius;

  public Circle() {
    _point = new Point2D();
    _radius = 1;
  }

  public Circle(double radius) {
    this(0, 0, radius);
  }

  public Circle(double x, double y) {
    this(x, y, 1);
  }

  public Circle(double x, double y, double radius) {
    this(new Point2D(x, y), radius);
  }

  public Circle(Point2D point) {
    this(point, 1);
  }

  public Circle(Point2D point, double radius) {
    _point = point;
    _radius = radius;
  }

  public void setX(double x) {
    _point.setX(x);
  }

  public void setY(double y) {
    _point.setY(y);
  }

  public double getX() {
    return _point.getX();
  }

  public double getY() {
    return _point.getY();
  }

  public void setPosition(float x, float y) {
    _point = new Point2D(x, y);
  }

  public Point2D getPosition() {
    return _point.clone();
  }

  public void setRadius(double r) {
    _radius = r;
  }

  public double getRadius() {
    return _radius;
  }

  public double getArea() {
    return _radius * _radius * Math.PI;
  }

  public double getPerimeter() {
    return _radius * Math.PI * 2;
  }

  public boolean isCollide(Circle other) {
    return _point.distance(other._point) < _radius + other._radius;
  }

  public boolean isCollide(Point2D point) {
    return _point.distance(point) < _radius;
  }

  @Override
  public Circle clone() {
    try {
      Circle clone = (Circle) super.clone();
      clone._point = _point.clone();

      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public int compareTo(Circle other) {
    return (int) (_radius - other._radius);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Circle)) return false;
    Circle circle = (Circle) other;
    return this._radius == circle._radius && this._point.equals(circle._point);
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = result * 31 + _point.hashCode();
    result = result * 31 + hashDouble(_radius);

    return result;
  }

  private int hashDouble(double d) {
    long longBits = Double.doubleToLongBits(d);

    return (int) (longBits ^ (longBits >>> 32));
  }

  @Override
  public String toString() {
    return "{point: " + _point + ", radius: " + _radius + "}";
  }
}
