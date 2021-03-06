/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.marketdata.manipulator.function;

/**
 * Interface defining the manipulation of a structured object (yield curve, vol surface etc) to be
 * undertaken.
 *
 * @param <T> the type of structure (yield curve, vol surface etc)
 */
public interface StructureManipulator<T> {

  /**
   * Transforms a structured object into another structured object of the same type but with the
   * values manipulated in some way. The input object should be unaltered and a new object output.
   *
   * For example, take a YieldCurve and shift it by 10%.
   *
   * @param structure the structured object to transform, not null
   * @return a transformed structure
   */
  T execute(T structure);

  /**
   * Indicates the type of structure that this class can handle. This method should be called before
   * a call to execute is made to ensure the value is compatible. If this is not done, the results
   * from the execute method are undetermined.
   *
   * @return the type of structure that can be handled, not null
   */
  Class<T> getExpectedType();
}
