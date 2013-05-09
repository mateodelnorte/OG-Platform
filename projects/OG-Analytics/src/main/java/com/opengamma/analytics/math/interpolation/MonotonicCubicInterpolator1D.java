/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.math.interpolation;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

import com.opengamma.analytics.math.function.PiecewisePolynomialFunction1D;
import com.opengamma.analytics.math.interpolation.data.Interpolator1DDataBundle;
import com.opengamma.analytics.math.interpolation.data.Interpolator1DPiecewisePoynomialDataBundle;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;

/**
 * 
 */
public class MonotonicCubicInterpolator1D extends Interpolator1D {

  /** Serialization version */
  private static final long serialVersionUID = 1L;

  // TODO have options on method
  private static final MonotonicityPreservingCubicSplineInterpolator BASE = new MonotonicityPreservingCubicSplineInterpolator(new PiecewiseCubicHermiteSplineInterpolator());
  private static final PiecewisePolynomialFunction1D FUNC = new PiecewisePolynomialFunction1D();

  @Override
  public Double interpolate(final Interpolator1DDataBundle data, final Double value) {
    Validate.notNull(value, "value");
    Validate.notNull(data, "data bundle");
    Validate.isTrue(data instanceof Interpolator1DPiecewisePoynomialDataBundle);
    final Interpolator1DPiecewisePoynomialDataBundle polyData = (Interpolator1DPiecewisePoynomialDataBundle) data;
    final DoubleMatrix1D res = FUNC.evaluate(polyData.getPiecewisePolynomialResult(), value);
    return res.getEntry(0);
  }

  @Override
  public double[] getNodeSensitivitiesForValue(final Interpolator1DDataBundle data, final Double value) {
    throw new NotImplementedException();
  }

  @Override
  public Interpolator1DDataBundle getDataBundle(final double[] x, final double[] y) {
    final PiecewisePolynomialResult poly = BASE.interpolate(x, y);
    return new Interpolator1DPiecewisePoynomialDataBundle(poly);
  }

  @Override
  public Interpolator1DDataBundle getDataBundleFromSortedArrays(final double[] x, final double[] y) {
    final PiecewisePolynomialResult poly = BASE.interpolate(x, y);
    return new Interpolator1DPiecewisePoynomialDataBundle(poly);
  }

}
