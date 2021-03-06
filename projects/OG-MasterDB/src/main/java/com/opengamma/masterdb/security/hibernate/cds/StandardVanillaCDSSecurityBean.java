/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.masterdb.security.hibernate.cds;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.masterdb.security.hibernate.ZonedDateTimeBean;

/**
 * 
 */
@BeanDefinition
public class StandardVanillaCDSSecurityBean extends StandardCDSSecurityBean {
  
  @PropertyDefinition
  private Double _coupon;
  @PropertyDefinition
  private ZonedDateTimeBean _cashSettlementDate;
  @PropertyDefinition
  private Boolean _adjustCashSettlementDate;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code StandardVanillaCDSSecurityBean}.
   * @return the meta-bean, not null
   */
  public static StandardVanillaCDSSecurityBean.Meta meta() {
    return StandardVanillaCDSSecurityBean.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(StandardVanillaCDSSecurityBean.Meta.INSTANCE);
  }

  @Override
  public StandardVanillaCDSSecurityBean.Meta metaBean() {
    return StandardVanillaCDSSecurityBean.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1354573786:  // coupon
        return getCoupon();
      case 487875210:  // cashSettlementDate
        return getCashSettlementDate();
      case -1224855431:  // adjustCashSettlementDate
        return getAdjustCashSettlementDate();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1354573786:  // coupon
        setCoupon((Double) newValue);
        return;
      case 487875210:  // cashSettlementDate
        setCashSettlementDate((ZonedDateTimeBean) newValue);
        return;
      case -1224855431:  // adjustCashSettlementDate
        setAdjustCashSettlementDate((Boolean) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      StandardVanillaCDSSecurityBean other = (StandardVanillaCDSSecurityBean) obj;
      return JodaBeanUtils.equal(getCoupon(), other.getCoupon()) &&
          JodaBeanUtils.equal(getCashSettlementDate(), other.getCashSettlementDate()) &&
          JodaBeanUtils.equal(getAdjustCashSettlementDate(), other.getAdjustCashSettlementDate()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getCoupon());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCashSettlementDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getAdjustCashSettlementDate());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the coupon.
   * @return the value of the property
   */
  public Double getCoupon() {
    return _coupon;
  }

  /**
   * Sets the coupon.
   * @param coupon  the new value of the property
   */
  public void setCoupon(Double coupon) {
    this._coupon = coupon;
  }

  /**
   * Gets the the {@code coupon} property.
   * @return the property, not null
   */
  public final Property<Double> coupon() {
    return metaBean().coupon().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cashSettlementDate.
   * @return the value of the property
   */
  public ZonedDateTimeBean getCashSettlementDate() {
    return _cashSettlementDate;
  }

  /**
   * Sets the cashSettlementDate.
   * @param cashSettlementDate  the new value of the property
   */
  public void setCashSettlementDate(ZonedDateTimeBean cashSettlementDate) {
    this._cashSettlementDate = cashSettlementDate;
  }

  /**
   * Gets the the {@code cashSettlementDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTimeBean> cashSettlementDate() {
    return metaBean().cashSettlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the adjustCashSettlementDate.
   * @return the value of the property
   */
  public Boolean getAdjustCashSettlementDate() {
    return _adjustCashSettlementDate;
  }

  /**
   * Sets the adjustCashSettlementDate.
   * @param adjustCashSettlementDate  the new value of the property
   */
  public void setAdjustCashSettlementDate(Boolean adjustCashSettlementDate) {
    this._adjustCashSettlementDate = adjustCashSettlementDate;
  }

  /**
   * Gets the the {@code adjustCashSettlementDate} property.
   * @return the property, not null
   */
  public final Property<Boolean> adjustCashSettlementDate() {
    return metaBean().adjustCashSettlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code StandardVanillaCDSSecurityBean}.
   */
  public static class Meta extends StandardCDSSecurityBean.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code coupon} property.
     */
    private final MetaProperty<Double> _coupon = DirectMetaProperty.ofReadWrite(
        this, "coupon", StandardVanillaCDSSecurityBean.class, Double.class);
    /**
     * The meta-property for the {@code cashSettlementDate} property.
     */
    private final MetaProperty<ZonedDateTimeBean> _cashSettlementDate = DirectMetaProperty.ofReadWrite(
        this, "cashSettlementDate", StandardVanillaCDSSecurityBean.class, ZonedDateTimeBean.class);
    /**
     * The meta-property for the {@code adjustCashSettlementDate} property.
     */
    private final MetaProperty<Boolean> _adjustCashSettlementDate = DirectMetaProperty.ofReadWrite(
        this, "adjustCashSettlementDate", StandardVanillaCDSSecurityBean.class, Boolean.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "coupon",
        "cashSettlementDate",
        "adjustCashSettlementDate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1354573786:  // coupon
          return _coupon;
        case 487875210:  // cashSettlementDate
          return _cashSettlementDate;
        case -1224855431:  // adjustCashSettlementDate
          return _adjustCashSettlementDate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends StandardVanillaCDSSecurityBean> builder() {
      return new DirectBeanBuilder<StandardVanillaCDSSecurityBean>(new StandardVanillaCDSSecurityBean());
    }

    @Override
    public Class<? extends StandardVanillaCDSSecurityBean> beanType() {
      return StandardVanillaCDSSecurityBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code coupon} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> coupon() {
      return _coupon;
    }

    /**
     * The meta-property for the {@code cashSettlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTimeBean> cashSettlementDate() {
      return _cashSettlementDate;
    }

    /**
     * The meta-property for the {@code adjustCashSettlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> adjustCashSettlementDate() {
      return _adjustCashSettlementDate;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
