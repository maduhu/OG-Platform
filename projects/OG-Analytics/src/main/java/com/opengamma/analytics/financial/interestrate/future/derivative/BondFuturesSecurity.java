/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.future.derivative;

import java.util.Arrays;

import org.apache.commons.lang.Validate;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondFixedSecurity;
import com.opengamma.util.money.Currency;

/**
 * Description of a bond future security (derivative version).
 */
public class BondFuturesSecurity implements InstrumentDerivative {

  /**
   * The last trading time.
   */
  private final double _tradingLastTime;
  /**
   * The first notice time.
   */
  private final double _noticeFirstTime;
  /**
   * The last notice time.
   */
  private final double _noticeLastTime;
  /**
   * The first delivery time. It is the first notice date plus the settlement days.
   */
  private final double _deliveryFirstTime;
  /**
   * The last delivery time. It is the last notice date plus the settlement days.
   */
  private final double _deliveryLastTime;
  /**
   * The notional of the bond future (also called face value or contract value).
   */
  private final double _notional;
  /**
   * The basket of deliverable bonds.
   */
  private final BondFixedSecurity[] _deliveryBasket;
  /**
   * The conversion factor of each bond in the basket.
   */
  private final double[] _conversionFactor;

  /**
   * Constructor from all the details.
   * @param tradingLastTime The last trading time.
   * @param noticeFirstTime The first notice time.
   * @param noticeLastTime The last notice time.
   * @param deliveryFirstTime The first delivery time.
   * @param deliveryLastTime The last delivery time.
   * @param notional The notional of the bond future.
   * @param deliveryBasket The basket of deliverable bonds.
   * @param conversionFactor The conversion factor of each bond in the basket.
   */
  public BondFuturesSecurity(double tradingLastTime, double noticeFirstTime, double noticeLastTime, double deliveryFirstTime, double deliveryLastTime, double notional,
      BondFixedSecurity[] deliveryBasket, double[] conversionFactor) {
    Validate.notNull(deliveryBasket, "Delivery basket");
    Validate.notNull(conversionFactor, "Conversion factors");
    Validate.isTrue(deliveryBasket.length > 0, "At least one bond in basket");
    Validate.isTrue(deliveryBasket.length == conversionFactor.length, "Conversion factor size");
    this._tradingLastTime = tradingLastTime;
    this._noticeFirstTime = noticeFirstTime;
    this._noticeLastTime = noticeLastTime;
    this._deliveryFirstTime = deliveryFirstTime;
    this._deliveryLastTime = deliveryLastTime;
    this._deliveryBasket = deliveryBasket;
    this._conversionFactor = conversionFactor;
    this._notional = notional;
  }

  /**
   * Gets the last trading time.
   * @return The last trading time.
   */
  public double getTradingLastTime() {
    return _tradingLastTime;
  }

  /**
   * Gets the first notice time.
   * @return The first notice time.
   */
  public double getNoticeFirstTime() {
    return _noticeFirstTime;
  }

  /**
   * Gets the last notice time.
   * @return The last notice time.
   */
  public double getNoticeLastTime() {
    return _noticeLastTime;
  }

  /**
   * Gets the first delivery time.
   * @return The first delivery time.
   */
  public double getDeliveryFirstTime() {
    return _deliveryFirstTime;
  }

  /**
   * Gets the last delivery time.
   * @return The last delivery time.
   */
  public double getDeliveryLastTime() {
    return _deliveryLastTime;
  }

  /**
   * Gets the notional of the bond future.
   * @return The notional of the bond future.
   */
  public double getNotional() {
    return _notional;
  }

  /**
   * Gets the basket of deliverable bonds.
   * @return The basket of deliverable bonds.
   */
  public BondFixedSecurity[] getDeliveryBasket() {
    return _deliveryBasket;
  }

  /**
   * Gets the conversion factor of each bond in the basket.
   * @return The conversion factor of each bond in the basket.
   */
  public double[] getConversionFactor() {
    return _conversionFactor;
  }

  /**
   * Gets the future currency.
   * @return The currency.
   */
  public Currency getCurrency() {
    return _deliveryBasket[0].getCurrency();
  }

  @Override
  public <S, T> T accept(final InstrumentDerivativeVisitor<S, T> visitor, final S data) {
    return null; // visitor.visitBondFutureSecurity(this, data);
  }

  @Override
  public <T> T accept(final InstrumentDerivativeVisitor<?, T> visitor) {
    return null; // visitor.visitBondFutureSecurity(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(_conversionFactor);
    result = prime * result + Arrays.hashCode(_deliveryBasket);
    long temp;
    temp = Double.doubleToLongBits(_deliveryFirstTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_deliveryLastTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_noticeFirstTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_noticeLastTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_notional);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_tradingLastTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BondFuturesSecurity other = (BondFuturesSecurity) obj;
    if (!Arrays.equals(_conversionFactor, other._conversionFactor)) {
      return false;
    }
    if (!Arrays.equals(_deliveryBasket, other._deliveryBasket)) {
      return false;
    }
    if (Double.doubleToLongBits(_deliveryFirstTime) != Double.doubleToLongBits(other._deliveryFirstTime)) {
      return false;
    }
    if (Double.doubleToLongBits(_deliveryLastTime) != Double.doubleToLongBits(other._deliveryLastTime)) {
      return false;
    }
    if (Double.doubleToLongBits(_noticeFirstTime) != Double.doubleToLongBits(other._noticeFirstTime)) {
      return false;
    }
    if (Double.doubleToLongBits(_noticeLastTime) != Double.doubleToLongBits(other._noticeLastTime)) {
      return false;
    }
    if (Double.doubleToLongBits(_notional) != Double.doubleToLongBits(other._notional)) {
      return false;
    }
    if (Double.doubleToLongBits(_tradingLastTime) != Double.doubleToLongBits(other._tradingLastTime)) {
      return false;
    }
    return true;
  }

}
