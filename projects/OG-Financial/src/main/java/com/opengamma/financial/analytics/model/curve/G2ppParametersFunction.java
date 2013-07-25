/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.curve;

import static com.opengamma.engine.value.ValuePropertyNames.CURRENCY;
import static com.opengamma.engine.value.ValueRequirementNames.G2PP_PARAMETERS;
import static com.opengamma.financial.analytics.model.curve.CurveCalculationPropertyNamesAndValues.PROPERTY_G2PP_PARAMETERS;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.opengamma.analytics.financial.model.interestrate.definition.G2ppPiecewiseConstantParameters;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.async.AsynchronousExecution;

/**
 * Function that supplies Hull-White one factor parameters from a snapshot.
 */
public class G2ppParametersFunction extends AbstractFunction.NonCompiledInvoker {
  private static final G2ppPiecewiseConstantParameters CONSTANT_PARAMETERS =
      new G2ppPiecewiseConstantParameters(new double[] {0.01, 0.02} , new double[][] {new double[] {0.01, 0.02}, new double[] {0.01, 0.02}}, new double[] {1}, 0.4);

  @Override
  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
      final Set<ValueRequirement> desiredValues) throws AsynchronousExecution {
    final ValueProperties properties = Iterables.getOnlyElement(desiredValues).getConstraints().copy().get();
    final ValueSpecification spec = new ValueSpecification(G2PP_PARAMETERS, ComputationTargetSpecification.NULL, properties);
    return Collections.singleton(new ComputedValue(spec, CONSTANT_PARAMETERS));
  }

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.NULL;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    final ValueProperties properties = createValueProperties()
        .withAny(PROPERTY_G2PP_PARAMETERS)
        .withAny(CURRENCY)
        .get();
    return Collections.singleton(new ValueSpecification(G2PP_PARAMETERS, ComputationTargetSpecification.NULL, properties));
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final ValueProperties constraints = desiredValue.getConstraints();
    final Set<String> names = constraints.getValues(PROPERTY_G2PP_PARAMETERS);
    if (names == null || names.size() != 1) {
      return null;
    }
    final Set<String> currencies = constraints.getValues(CURRENCY);
    if (currencies == null || currencies.size() != 1) {
      return null;
    }
    return Collections.emptySet(); //TODO - just putting in dummy values for now
  }

}
