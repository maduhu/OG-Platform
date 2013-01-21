/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.futureoption;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.financial.analytics.model.CalculationPropertyNamesAndValues;
import com.opengamma.financial.analytics.model.curve.forward.ForwardCurveValuePropertyNames;
import com.opengamma.financial.analytics.model.equity.option.EquityOptionFunction;
import com.opengamma.financial.analytics.model.volatility.surface.black.BlackVolatilitySurfacePropertyNamesAndValues;
import com.opengamma.financial.security.FinancialSecurityUtils;

/**
 *
 */
public abstract class CommodityFutureOptionBlackFunction extends CommodityFutureOptionFunction {

  /**
   * @param valueRequirementName The value requirement name
   */
  public CommodityFutureOptionBlackFunction(final String... valueRequirementName) {
    super(valueRequirementName);
  }

  @Override
  protected ValueProperties.Builder createValueProperties(final ComputationTarget target) {
    return createValueProperties()
        .with(ValuePropertyNames.CALCULATION_METHOD, getCalculationMethod())
        .with(CalculationPropertyNamesAndValues.PROPERTY_MODEL_TYPE, getModelType())
        .withAny(ValuePropertyNames.SURFACE)
        .withAny(ValuePropertyNames.CURVE)
        .withAny(ValuePropertyNames.CURVE_CALCULATION_CONFIG)
        .withAny(BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_SMILE_INTERPOLATOR)
        .withAny(ForwardCurveValuePropertyNames.PROPERTY_FORWARD_CURVE_CALCULATION_METHOD)
        .withAny(EquityOptionFunction.PROPERTY_FORWARD_CURVE_NAME)
        .with(ValuePropertyNames.CURRENCY, FinancialSecurityUtils.getCurrency(target.getSecurity()).getCode());
  }

  @Override
  protected ValueProperties.Builder createValueProperties(final ComputationTarget target, final ValueRequirement desiredValue) {
    final String fundingCurveName = getFundingCurveName(desiredValue);
    final String curveConfigName = getCurveConfigName(desiredValue);
    final String volSurfaceName = desiredValue.getConstraint(ValuePropertyNames.SURFACE);
    final String smileInterpolatorName = desiredValue.getConstraint(BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_SMILE_INTERPOLATOR);
    final String forwardCurveCalculationMethod = desiredValue.getConstraint(ForwardCurveValuePropertyNames.PROPERTY_FORWARD_CURVE_CALCULATION_METHOD);
    final String forwardCurveName = desiredValue.getConstraint(EquityOptionFunction.PROPERTY_FORWARD_CURVE_NAME);
    final ValueProperties.Builder builder = createValueProperties()
        .with(ValuePropertyNames.CALCULATION_METHOD, getCalculationMethod())
        .with(CalculationPropertyNamesAndValues.PROPERTY_MODEL_TYPE, getModelType())
        .with(ValuePropertyNames.CURVE, fundingCurveName)
        .with(ValuePropertyNames.CURVE_CALCULATION_CONFIG, curveConfigName)
        .with(ValuePropertyNames.SURFACE, volSurfaceName)
        .with(BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_SMILE_INTERPOLATOR, smileInterpolatorName)
        .with(ForwardCurveValuePropertyNames.PROPERTY_FORWARD_CURVE_CALCULATION_METHOD, forwardCurveCalculationMethod)
        .with(EquityOptionFunction.PROPERTY_FORWARD_CURVE_NAME, forwardCurveName)
        .with(ValuePropertyNames.CURRENCY, FinancialSecurityUtils.getCurrency(target.getSecurity()).getCode());
    return builder;
  }

  @Override
  protected String getCalculationMethod() {
    return CalculationPropertyNamesAndValues.BLACK_METHOD;
  }

  @Override
  protected String getModelType() {
    return CalculationPropertyNamesAndValues.ANALYTIC;
  }
}
