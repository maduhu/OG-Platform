/**
 * Copyright (C) 2009 - 2011 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.option.pricing.fourier;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.financial.model.option.pricing.analytic.formula.BlackFunctionData;
import com.opengamma.financial.model.option.pricing.analytic.formula.EuropeanVanillaOption;
import com.opengamma.financial.model.volatility.BlackImpliedVolatilityFormula;
import com.opengamma.math.ComplexMathUtils;
import com.opengamma.math.function.Function1D;
import com.opengamma.math.number.ComplexNumber;
import com.opengamma.util.monitor.OperationTimer;

/**
 * 
 */
public class CGMYFourierPricer1Test {
  private static final Logger s_logger = LoggerFactory.getLogger(CGMYFourierPricer1Test.class);
  private static final int WARMUP_CYCLES = 200;
  private static final int BENCHMARK_CYCLES = 10000;
  private static final boolean TEST_TIMING = false;
  private static final double FORWARD = 1;
  private static final double T = 2.0;
  private static final double DF = 0.93;
  private static final double C = 0.03;
  private static final double G = 0.001;
  private static final double M = 1.001;
  private static final double Y = 1.5;
  private static final CharacteristicExponent1 CGMY_CE = new CGMYCharacteristicExponent1(C, G, M, Y);
  private static final BlackImpliedVolatilityFormula BLACK_IMPLIED_VOL = new BlackImpliedVolatilityFormula();

  @Test
  public void testCGMY() {
    final FourierPricer1 pricer = new FourierPricer1();
    final BlackFunctionData data = new BlackFunctionData(FORWARD, DF, 0);
    for (int i = 0; i < 21; i++) {
      final double k = 0.01 + 0.14 * i / 20.0;
      final EuropeanVanillaOption option = new EuropeanVanillaOption(k, T, true);
      //final double price = pricer.price(FORWARD, k * FORWARD, DF, T, true, CGMY_CE, -0.5, 1e-6);
      final double price = pricer.price(data, option, CGMY_CE, -0.5, 1e-6);
      final double impVol = BLACK_IMPLIED_VOL.getImpliedVolatility(data, option, price);
      //System.out.println(k + "\t" + impVol);
    }
  }

  //TODO nothing is being tested in here
  @Test
  public void testIntegrandCGMY() {
    final EuropeanPriceIntegrand1 integrand = new EuropeanPriceIntegrand1(CGMY_CE, -0.5, true);
    final EuropeanVanillaOption option = new EuropeanVanillaOption(0.25 * FORWARD, T, true);
    final BlackFunctionData data = new BlackFunctionData(FORWARD, DF, 0);
    final double k = Math.log(option.getStrike() / data.getForward());
    final Function1D<ComplexNumber, ComplexNumber> ce = integrand.getCharacteristicExponent().getFunction(T);
    final Function1D<ComplexNumber, ComplexNumber> gaussian = integrand.useVarianceReduction() ? CGMY_CE.getFunction(T) : null;
    if (TEST_TIMING) {
      ComplexNumber res = ComplexNumber.ZERO;
      for (int count = 0; count < WARMUP_CYCLES; count++) {
        for (int i = 0; i < 100; i++) {
          final double x = -15. + i * 30. / 200.0;
          res = ComplexMathUtils.add(res, integrand.getIntegrand(x, ce, gaussian, k));
        }
      }
      res = ComplexMathUtils.add(res, res);
      if (BENCHMARK_CYCLES > 0) {
        final OperationTimer timer = new OperationTimer(s_logger, "processing {} cycles on integral", BENCHMARK_CYCLES);
        for (int count = 0; count < BENCHMARK_CYCLES; count++) {
          for (int i = 0; i < 100; i++) {
            final double x = -15. + i * 30. / 200.0;
            res = ComplexMathUtils.add(res, integrand.getIntegrand(x, ce, gaussian, k));
          }
        }
        timer.finished();
      }
      res = ComplexMathUtils.add(res, res);
    }
    for (int i = 0; i < 201; i++) {
      final double x = -15. + i * 30. / 200.0;
      final ComplexNumber res = integrand.getIntegrand(x, ce, gaussian, k);
      //System.out.println(x + "\t" + res.getReal() + "\t" + res.getImaginary());
    }
  }

}
