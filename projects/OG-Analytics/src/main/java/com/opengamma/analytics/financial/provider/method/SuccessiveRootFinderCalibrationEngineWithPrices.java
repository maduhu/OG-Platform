/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.method;

import com.opengamma.analytics.financial.provider.description.interestrate.ParameterProviderInterface;

/**
 *  Calibration engine calibrating successively the instruments in the basket trough a root-finding process.
 *  Instruments prices are input.
 * @param <DATA_TYPE>  The type of the data for the base calculator.
 */
public abstract class SuccessiveRootFinderCalibrationEngineWithPrices<DATA_TYPE extends ParameterProviderInterface> extends CalibrationEngineWithPrices<DATA_TYPE> {

  /**
   * The calibration objective.
   */
  private final SuccessiveRootFinderCalibrationObjective _calibrationObjective;

  /**
   * The constructor.
   * @param calibrationObjective The calibration objective.
   */
  public SuccessiveRootFinderCalibrationEngineWithPrices(final SuccessiveRootFinderCalibrationObjective calibrationObjective) {
    super(calibrationObjective.getFXMatrix(), calibrationObjective.getCcy());
    _calibrationObjective = calibrationObjective;
  }

  /**
   * Gets the calibration objective.
   * @return The calibration objective.
   */
  public SuccessiveRootFinderCalibrationObjective getCalibrationObjective() {
    return _calibrationObjective;
  }
}
