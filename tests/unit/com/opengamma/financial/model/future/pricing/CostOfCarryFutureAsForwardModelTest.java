/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.model.future.pricing;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.time.calendar.ZonedDateTime;

import org.junit.Test;

import com.opengamma.financial.greeks.Greek;
import com.opengamma.financial.greeks.GreekResultCollection;
import com.opengamma.financial.model.forward.definition.ForwardDefinition;
import com.opengamma.financial.model.forward.definition.StandardForwardDataBundle;
import com.opengamma.financial.model.forward.pricing.CostOfCarryForwardModel;
import com.opengamma.financial.model.forward.pricing.ForwardModel;
import com.opengamma.financial.model.future.definition.FutureDefinition;
import com.opengamma.financial.model.future.definition.StandardFutureDataBundle;
import com.opengamma.financial.model.interestrate.curve.ConstantInterestRateDiscountCurve;
import com.opengamma.util.time.DateUtil;
import com.opengamma.util.time.Expiry;

/**
 * @author emcleod
 *
 */
public class CostOfCarryFutureAsForwardModelTest {
  private static final double R = 0.05;
  private static final double D = 0.01;
  private static final double SPOT = 110;
  private static final double STORAGE = 5;
  private static final ZonedDateTime DATE = DateUtil.getUTCDate(2010, 1, 1);
  private static final Expiry EXPIRY = new Expiry(DateUtil.getDateOffsetWithYearFraction(DATE, 0.75));
  private static final ForwardModel<StandardForwardDataBundle> FORWARD_MODEL = new CostOfCarryForwardModel();
  private static final ForwardDefinition FORWARD_DEFINITION = new ForwardDefinition(EXPIRY);
  private static final StandardForwardDataBundle FORWARD_DATA = new StandardForwardDataBundle(D,
      new ConstantInterestRateDiscountCurve(R), SPOT, DATE, STORAGE);
  private static final FutureModel<StandardFutureDataBundle> FUTURE_MODEL = new CostOfCarryFutureAsForwardModel();
  private static final FutureDefinition FUTURE_DEFINITION = new FutureDefinition(EXPIRY);
  private static final StandardFutureDataBundle FUTURE_DATA = new StandardFutureDataBundle(D,
      new ConstantInterestRateDiscountCurve(R), SPOT, DATE, STORAGE);
  private static final Set<Greek> GREEKS = EnumSet.of(Greek.FAIR_PRICE, Greek.DELTA);

  @Test(expected = NullPointerException.class)
  public void testNullDefinition() {
    FUTURE_MODEL.getGreeks(null, FUTURE_DATA, GREEKS);
  }

  @Test(expected = NullPointerException.class)
  public void testNullData() {
    FUTURE_MODEL.getGreeks(FUTURE_DEFINITION, null, GREEKS);
  }

  @Test(expected = NullPointerException.class)
  public void testNullGreekSet() {
    FUTURE_MODEL.getGreeks(FUTURE_DEFINITION, FUTURE_DATA, null);
  }

  @Test
  public void testRequiredGreeks() {
    assertEquals(new GreekResultCollection(), FUTURE_MODEL.getGreeks(FUTURE_DEFINITION, FUTURE_DATA, Collections
        .<Greek> emptySet()));
    assertEquals(new GreekResultCollection(), FUTURE_MODEL.getGreeks(FUTURE_DEFINITION, FUTURE_DATA, EnumSet
        .of(Greek.DELTA)));
  }

  @Test
  public void test() {
    final GreekResultCollection forwardResult = FORWARD_MODEL.getGreeks(FORWARD_DEFINITION, FORWARD_DATA, GREEKS);
    final GreekResultCollection futureResult = FUTURE_MODEL.getGreeks(FUTURE_DEFINITION, FUTURE_DATA, GREEKS);
    assertEquals(futureResult.size(), 1);
    assertEquals(forwardResult.size(), futureResult.size());
    assertEquals(forwardResult.keySet().iterator().next(), futureResult.keySet().iterator().next());
    assertEquals((Double) forwardResult.values().iterator().next().getResult(), (Double) futureResult.values()
        .iterator().next().getResult(), 1e-12);
  }
}
