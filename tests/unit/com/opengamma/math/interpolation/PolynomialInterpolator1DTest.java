/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.interpolation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.opengamma.math.function.Function1D;
import com.opengamma.math.function.PolynomialFunction1D;

/**
 * 
 */
public class PolynomialInterpolator1DTest {
  private static final Interpolator1D<Interpolator1DModel> INTERPOLATOR_NO_OFFSET = new PolynomialInterpolator1D(3);
  private static final Interpolator1D<Interpolator1DModel> INTERPOLATOR_WITH_OFFSET = new PolynomialInterpolator1D(3, 2);
  private static final Interpolator1DModel MODEL = Interpolator1DModelFactory.fromArrays(new double[] {1, 2, 3, 4, 5}, new double[] {6, 7, 8, 9, 10});
  private static final double EPS = 1e-15;

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalDegree1() {
    new PolynomialInterpolator1D(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalDegree2() {
    new PolynomialInterpolator1D(0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeOffset() {
    new PolynomialInterpolator1D(3, -4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLargeOffset() {
    new PolynomialInterpolator1D(3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    INTERPOLATOR_WITH_OFFSET.interpolate(null, 3.);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullData() {
    INTERPOLATOR_WITH_OFFSET.interpolate(MODEL, null);
  }

  @Test(expected = InterpolationException.class)
  public void testInsufficientData() {
    INTERPOLATOR_WITH_OFFSET.interpolate(Interpolator1DModelFactory.fromArrays(new double[] {1, 2, 3}, new double[] {4, 5, 6}), 1.5);
  }

  @Test(expected = InterpolationException.class)
  public void testOutOfRange() {
    INTERPOLATOR_WITH_OFFSET.interpolate(MODEL, 0.);
  }

  @Test(expected = InterpolationException.class)
  public void testOutOfRangeWithOffset() {
    INTERPOLATOR_WITH_OFFSET.interpolate(MODEL, 2.1);
  }

  @Test(expected = InterpolationException.class)
  public void testHighOutOfRange() {
    INTERPOLATOR_WITH_OFFSET.interpolate(MODEL, 10.);
  }

  @Test(expected = InterpolationException.class)
  public void testHighOutOfRangeWithOffset() {
    INTERPOLATOR_NO_OFFSET.interpolate(MODEL, 4.5);
  }

  @Test
  public void testHashCodeAndEquals() {
    assertEquals(INTERPOLATOR_NO_OFFSET, new PolynomialInterpolator1D(3));
    assertEquals(INTERPOLATOR_NO_OFFSET.hashCode(), new PolynomialInterpolator1D(3).hashCode());
    assertFalse(INTERPOLATOR_NO_OFFSET.equals(new PolynomialInterpolator1D(5)));
    assertEquals(INTERPOLATOR_WITH_OFFSET, new PolynomialInterpolator1D(3, 2));
    assertEquals(INTERPOLATOR_WITH_OFFSET.hashCode(), new PolynomialInterpolator1D(3, 2).hashCode());
    assertFalse(INTERPOLATOR_WITH_OFFSET.equals(new PolynomialInterpolator1D(5, 2)));
    assertFalse(INTERPOLATOR_WITH_OFFSET.equals(new PolynomialInterpolator1D(3, 1)));
  }

  @Test
  public void testInterpolation() {
    final Function1D<Double, Double> quadratic = new PolynomialFunction1D(new Double[] {-4., 3., 1.});
    final Function1D<Double, Double> quartic = new PolynomialFunction1D(new Double[] {-4., 3., 1., 1., 1.});
    final Map<Double, Double> quadraticMap = new HashMap<Double, Double>();
    final Map<Double, Double> quarticMap = new HashMap<Double, Double>();
    double x;
    for (int i = 0; i < 10; i++) {
      x = i / 10.;
      quadraticMap.put(x, quadratic.evaluate(x));
      quarticMap.put(x, quartic.evaluate(x));
    }
    x = 0.35;
    final Interpolator1DModel quadraticData = Interpolator1DModelFactory.fromMap(quadraticMap);
    final Interpolator1DModel quarticData = Interpolator1DModelFactory.fromMap(quarticMap);
    Interpolator1D<Interpolator1DModel> quadraticInterpolator = new PolynomialInterpolator1D(2);
    InterpolationResult<Double> quadraticResult = quadraticInterpolator.interpolate(quadraticData, x);
    Interpolator1D<Interpolator1DModel> quarticInterpolator = new PolynomialInterpolator1D(4);
    InterpolationResult<Double> quarticResult = quarticInterpolator.interpolate(quarticData, x);
    assertEquals(quadraticResult.getResult(), quadratic.evaluate(x), EPS);
    assertEquals(quarticResult.getResult(), quartic.evaluate(x), EPS);
    quadraticInterpolator = new PolynomialInterpolator1D(2, 1);
    quadraticResult = quadraticInterpolator.interpolate(quadraticData, x);
    quarticInterpolator = new PolynomialInterpolator1D(4, 1);
    quarticResult = quarticInterpolator.interpolate(quarticData, x);
    assertEquals(quadraticResult.getResult(), quadratic.evaluate(x), EPS);
    assertEquals(quarticResult.getResult(), quartic.evaluate(x), EPS);
  }
}
