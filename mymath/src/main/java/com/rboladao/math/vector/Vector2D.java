package com.rboladao.math.vector;

import java.io.Serializable;

/** Essa classe é um vetor bidimensional básico. */
public class Vector2D implements Cloneable, Comparable<Vector2D>, Serializable {

  private static final long serialVersionUID = 0L;

  /** O valor x do vetor. */
  private double _x;
  /** O valor y do vetor. */
  private double _y;

  /** Um construtor noargs para um vetor zerado. */
  public Vector2D() {
    this._x = this._y = 0;
  }

  /**
   * Um construtor com dois argumentos, o x e o y.
   *
   * @param x O valor de x.
   * @param y O valor de y.
   */
  public Vector2D(double x, double y) {
    this._x = x;
    this._y = y;
  }

  /**
   * Um construtor de clonagem.
   *
   * @param other Outro vetor.
   */
  public Vector2D(Vector2D other) {
    this._x = other._x;
    this._y = other._y;
  }

  /**
   * Configura o valor de x.
   *
   * @param x O novo x;
   */
  public void setX(double x) {
    this._x = x;
  }
  /**
   * Retorna o valor de x;
   *
   * @return x;
   */
  public double getX() {
    return this._x;
  }

  /**
   * Configura o valor de y;
   *
   * @param y O novo y;
   */
  public void setY(double y) {
    this._y = y;
  }

  /**
   * Retorna o valor de y
   *
   * @return y;
   */
  public double getY() {
    return this._y;
  }

  /**
   * Retorna o tamanho do vetor.
   *
   * @return √(x²+y²).
   */
  public double size() {
    return Math.sqrt(_x * _x + _y * _y);
  }

  /**
   * Retorna este vetor negativado
   *
   * @return this = -this;
   */
  public Vector2D negateMe() {
    this._x = -this._x;
    this._y = -this._y;
    return this;
  }
  
  /**
   * Retorna uma cópia negativa deste vetor.
   * 
   * @return -this
   */
  public Vector2D negate() {
    return clone().negateMe();
  }

  /**
   * Negativa um vetor.
   *
   * @param vec Um vetor.
   * @return -vec.
   */
  public static Vector2D negate(Vector2D vec) {
    return vec.clone().negate();
  }

  /**
   * Adiciona um vetor a este.
   *
   * @param other Um outro vetor.
   * @return this += other;
   */
  public Vector2D addMe(Vector2D other) {
    this._x += other._x;
    this._y += other._y;

    return this;
  }
  
  /**
   * Adiciona um vetor a uma cópia deste vetor.
   *
   * @param other Um outro vetor.
   * @return this + other.
   */
  public Vector2D add(Vector2D other) {
    return clone().addMe(other);
  }

  /**
   * Soma dois vetores.
   *
   * @param left O vetor a esquerda da equação.
   * @param right O vetor a direita da equação.
   * @return left + right.
   */
  public static Vector2D add(Vector2D left, Vector2D right) {
    return left.clone().add(right);
  }

  /**
   * Retorna este vetor menos outro.
   *
   * @param other Outro vetor.
   * @return this -= other.
   */
  public Vector2D subtractMe(Vector2D other) {
    this._x -= other._x;
    this._y -= other._y;
    return this;
  }
  
  /**
   * Retorna uma cópia deste vetor menos outro.
   *
   * @param other Outro vetor.
   * @return this - other.
   */
  public Vector2D subtract(Vector2D other) {
    return clone().subtractMe(other);
  }

  /**
   * Subtrai dois vetores.
   *
   * @param left O vetor a esquerda da equação.
   * @param right O vetor a direita da equação.
   * @return left - right
   */
  public static Vector2D subtract(Vector2D left, Vector2D right) {
    return left.clone().subtract(right);
  }
  
  /**
   * Retorna este vetor vezes uma escala.
   *
   * @param scalar Uma escala.
   * @return this *= scalar.
   */
  public Vector2D multiplyMe(double scalar) {
    this._x *= scalar;
    this._y *= scalar;
    return this;
  }
  
  /**
   * Retorna uma cópia deste vetor vezes uma escala.
   *
   * @param scalar Uma escala.
   * @return this * scalar.
   */
  public Vector2D multiply(double scalar) {
    return clone().multiplyMe(scalar);
  }

  /**
   * Multiplica um vetor por uma escala.
   *
   * @param left O vetor a esquerda da equação.
   * @param right A escala a direita da equação.
   * @return left * right
   */
  public static Vector2D multiply(Vector2D left, double right) {
    return left.clone().multiply(right);
  }
  
  /**
   * Retorna este vetor dividido por uma escala.
   *
   * @param scalar Uma escala.
   * @return this /= scalar.
   */
  public Vector2D divideMe(double scalar) {
    this._x /= scalar;
    this._y /= scalar;
    return this;
  }
  
  /**
   * Retorna uma cópia deste vetor dividido por uma escala.
   *
   * @param scalar Uma escala.
   * @return this / scalar.
   */
  public Vector2D divide(double scalar) {
    return clone().divideMe(scalar);
  }

  /**
   * Divide um vetor por uma escala.
   *
   * @param left O vetor a esquerda da equação.
   * @param right A escala a direita da equação.
   * @return left - right
   */
  public static Vector2D divide(Vector2D left, double right) {
    return left.clone().divide(right);
  }

  /**
   * Retorna o ângulo do vetor em rads.
   *
   * @return O ângulo.
   */
  public double angle() {
    return -Math.atan2(-_y, _x);
  }

  /**
   * Normaliza o vetor.
   *
   * @return this /= size.
   */
  public Vector2D normalizeMe() {
    return divideMe(size());
  }
  
  /**
   * Normaliza o vetor.
   *
   * @return this / size.
   */
  public Vector2D normalize() {
    return clone().normalizeMe();
  }
  
  /**
   * Retorna um novo vetor pelo tamanho e ângulo.
   *
   * @param size O tamanho do vetor.
   * @param angle O ângulo do vetor.
   * @return [cos(angle)*size, sim(angle)*size].
   */
  public static Vector2D newBySizeAngle(double size, double angle) {
    return new Vector2D(Math.cos(angle) * size, Math.sin(angle) * size);
  }
  
  /**
   * Rotaciona o vetor.
   *
   * @param angle O ângulo em rads.
   * @return this *= [[cos -sin][sin cos]].
   */
  public Vector2D rotateMe(double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);

    double newX = _x * cos - _y * sin;
    double newY = _x * sin + _y * cos;

    this._x = newX;
    this._y = newY;

    return this;
  }

  /**
   * Rotaciona o vetor.
   *
   * @param angle O ângulo em rads.
   * @return this * [[cos -sin][sin cos]].
   */
  public Vector2D rotate(double angle) {
    return clone().rotateMe(angle);
  }
  
  /**
   * Retorna o produto escalar entre dois vetores.
   * 
   * @param other Outro vetor.
   * @return x0 * x1 + y0 * y1.
   */
  public double dot(Vector2D other) {
    return this._x * other._x + this._y * other._y;
  }

  /**
   * Retorna o ângulo entre dois vetores.
   *
   * @param other Outro vetor.
   * @return a° - b°.
   */
  public double relativeAngleBetween(Vector2D other) {
    return angle() - other.angle();
  }
  
  /**
   * Retorna a distância entre dois vetores.
   *
   * @param other Outro vetor.
   * @return |this - other|.
   */
  public double distance(Vector2D other) {
    return this.subtract(other).size();
  }

  /**
   * Redimensiona o vetor.
   *
   * @param size O novo tamanho do vetor.
   * @return this = norm * size.
   */
  public Vector2D resizeMe(double size) {
    return normalizeMe().multiplyMe(size);
  }

  /**
   * Redimensiona o vetor.
   *
   * @param size O novo tamanho do vetor.
   * @return norm * size.
   */
  public Vector2D resize(double size) {
    return clone().resizeMe(size);
  }

  /**
   * Retorna uma cópia do vetor.
   * 
   * @return super.clone().
   */
  @Override
  public Vector2D clone() {
    try {
      return (Vector2D) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  /**
   * Compara o tamanho de dois vetores.
   *
   * @param other Outro vetor.
   * @return |this| - |other|.
   */
  @Override
  public int compareTo(Vector2D other) {
    return (int) (this.size() - other.size());
  }

  /**
   * Confere a igualdade de dois vetores.
   *
   * @param o Um objeto.
   * @return this == o || x0 == x1 && y0 == y1.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Vector2D)) return false;
    Vector2D other = (Vector2D) o;
    return _x == other._x && _y == other._y;
  }
  
  /**
   * Retorna um código de espalhamento para hashtables e hashmaps.
   *
   * @return um código de hash.
   */
  @Override
  public int hashCode() {
    int result = 17;

    result = result * 31 + hashDouble(_x);
    result = result * 31 + hashDouble(_y);

    return result;
  }
  
  /** @hide */
  private int hashDouble(double d) {
    long longBits = Double.doubleToLongBits(d);
    return (int) (longBits ^ (longBits >>> 32));
  }
  
  /**
   * Retorna uma string.
   *
   * @return {x: x, y: y}.
   */
  @Override
  public String toString() {
    return "{x: " + _x + ", y:" + _y + "}";
  }
}
