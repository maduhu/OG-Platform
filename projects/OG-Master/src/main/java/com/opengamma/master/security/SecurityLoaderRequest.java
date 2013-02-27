/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;

/**
 * Request to load one or more securities.
 * <p>
 * This class is mutable and not thread-safe.
 */
@PublicSPI
@BeanDefinition
public class SecurityLoaderRequest extends DirectBean {

  /**
   * The set of security external identifiers to load.
   */
  @PropertyDefinition
  private final Set<ExternalIdBundle> _externalIdBundles = Sets.newHashSet();
  /**
   * The flag indicating that the securities should be forcibly updated.
   * When true, any security already present will be re-loaded from the
   * underlying data source.
   */
  @PropertyDefinition
  private boolean _forceUpdate;
  /**
   * The flag indicating that the security object map should be populated in the result.
   * This flag is false by default to avoid sending information that may not be needed.
   */
  @PropertyDefinition
  private boolean _returnSecurityObjects;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance to load a single security.
   * 
   * @param externalIdBundle  the identifier bundle, not null
   * @return the request, not null
   */
  public static SecurityLoaderRequest create(ExternalIdBundle externalIdBundle) {
    SecurityLoaderRequest request = new SecurityLoaderRequest();
    request.addExternalIds(externalIdBundle);
    return request;
  }

  /**
   * Obtains an instance to load multiple securities.
   * 
   * @param externalIdBundles  the identifier bundle, not null
   * @return the request, not null
   */
  public static SecurityLoaderRequest create(Iterable<ExternalIdBundle> externalIdBundles) {
    SecurityLoaderRequest request = new SecurityLoaderRequest();
    request.addExternalIds(externalIdBundles);
    return request;
  }

  //-------------------------------------------------------------------------
  /**
   * Creates an instance.
   */
  protected SecurityLoaderRequest() {
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an array of historical time-series external identifiers to the collection to load.
   * 
   * @param externalIds  the historical time-series identifiers to load, not null
   */
  public void addExternalIds(ExternalId... externalIds) {
    ArgumentChecker.notNull(externalIds, "externalIds");
    List<ExternalIdBundle> list = new ArrayList<ExternalIdBundle>();
    for (ExternalId externalId : externalIds) {
      list.add(ExternalIdBundle.of(externalId));
    }
    getExternalIdBundles().addAll(list);
  }

  /**
   * Adds an array of historical time-series external identifiers to the collection to load.
   * 
   * @param externalIdBundles  the historical time-series identifiers to load, not null
   */
  public void addExternalIds(ExternalIdBundle... externalIdBundles) {
    ArgumentChecker.notNull(externalIdBundles, "externalIdBundles");
    getExternalIdBundles().addAll(Arrays.asList(externalIdBundles));
  }

  /**
   * Adds a collection of historical time-series external identifiers to the collection to load.
   * 
   * @param externalIdBundles  the historical time-series identifiers to load, not null
   */
  public void addExternalIds(Iterable<ExternalIdBundle> externalIdBundles) {
    ArgumentChecker.notNull(externalIdBundles, "externalIdBundles");
    Iterables.addAll(getExternalIdBundles(), externalIdBundles);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SecurityLoaderRequest}.
   * @return the meta-bean, not null
   */
  public static SecurityLoaderRequest.Meta meta() {
    return SecurityLoaderRequest.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(SecurityLoaderRequest.Meta.INSTANCE);
  }

  @Override
  public SecurityLoaderRequest.Meta metaBean() {
    return SecurityLoaderRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1369745653:  // externalIdBundles
        return getExternalIdBundles();
      case -406875244:  // forceUpdate
        return isForceUpdate();
      case -1487031708:  // returnSecurityObjects
        return isReturnSecurityObjects();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -1369745653:  // externalIdBundles
        setExternalIdBundles((Set<ExternalIdBundle>) newValue);
        return;
      case -406875244:  // forceUpdate
        setForceUpdate((Boolean) newValue);
        return;
      case -1487031708:  // returnSecurityObjects
        setReturnSecurityObjects((Boolean) newValue);
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
      SecurityLoaderRequest other = (SecurityLoaderRequest) obj;
      return JodaBeanUtils.equal(getExternalIdBundles(), other.getExternalIdBundles()) &&
          JodaBeanUtils.equal(isForceUpdate(), other.isForceUpdate()) &&
          JodaBeanUtils.equal(isReturnSecurityObjects(), other.isReturnSecurityObjects());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundles());
    hash += hash * 31 + JodaBeanUtils.hashCode(isForceUpdate());
    hash += hash * 31 + JodaBeanUtils.hashCode(isReturnSecurityObjects());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the set of security external identifiers to load.
   * @return the value of the property
   */
  public Set<ExternalIdBundle> getExternalIdBundles() {
    return _externalIdBundles;
  }

  /**
   * Sets the set of security external identifiers to load.
   * @param externalIdBundles  the new value of the property
   */
  public void setExternalIdBundles(Set<ExternalIdBundle> externalIdBundles) {
    this._externalIdBundles.clear();
    this._externalIdBundles.addAll(externalIdBundles);
  }

  /**
   * Gets the the {@code externalIdBundles} property.
   * @return the property, not null
   */
  public final Property<Set<ExternalIdBundle>> externalIdBundles() {
    return metaBean().externalIdBundles().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the flag indicating that the securities should be forcibly updated.
   * When true, any security already present will be re-loaded from the
   * underlying data source.
   * @return the value of the property
   */
  public boolean isForceUpdate() {
    return _forceUpdate;
  }

  /**
   * Sets the flag indicating that the securities should be forcibly updated.
   * When true, any security already present will be re-loaded from the
   * underlying data source.
   * @param forceUpdate  the new value of the property
   */
  public void setForceUpdate(boolean forceUpdate) {
    this._forceUpdate = forceUpdate;
  }

  /**
   * Gets the the {@code forceUpdate} property.
   * When true, any security already present will be re-loaded from the
   * underlying data source.
   * @return the property, not null
   */
  public final Property<Boolean> forceUpdate() {
    return metaBean().forceUpdate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the flag indicating that the security object map should be populated in the result.
   * This flag is false by default to avoid sending information that may not be needed.
   * @return the value of the property
   */
  public boolean isReturnSecurityObjects() {
    return _returnSecurityObjects;
  }

  /**
   * Sets the flag indicating that the security object map should be populated in the result.
   * This flag is false by default to avoid sending information that may not be needed.
   * @param returnSecurityObjects  the new value of the property
   */
  public void setReturnSecurityObjects(boolean returnSecurityObjects) {
    this._returnSecurityObjects = returnSecurityObjects;
  }

  /**
   * Gets the the {@code returnSecurityObjects} property.
   * This flag is false by default to avoid sending information that may not be needed.
   * @return the property, not null
   */
  public final Property<Boolean> returnSecurityObjects() {
    return metaBean().returnSecurityObjects().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecurityLoaderRequest}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code externalIdBundles} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<ExternalIdBundle>> _externalIdBundles = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundles", SecurityLoaderRequest.class, (Class) Set.class);
    /**
     * The meta-property for the {@code forceUpdate} property.
     */
    private final MetaProperty<Boolean> _forceUpdate = DirectMetaProperty.ofReadWrite(
        this, "forceUpdate", SecurityLoaderRequest.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code returnSecurityObjects} property.
     */
    private final MetaProperty<Boolean> _returnSecurityObjects = DirectMetaProperty.ofReadWrite(
        this, "returnSecurityObjects", SecurityLoaderRequest.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "externalIdBundles",
        "forceUpdate",
        "returnSecurityObjects");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1369745653:  // externalIdBundles
          return _externalIdBundles;
        case -406875244:  // forceUpdate
          return _forceUpdate;
        case -1487031708:  // returnSecurityObjects
          return _returnSecurityObjects;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SecurityLoaderRequest> builder() {
      return new DirectBeanBuilder<SecurityLoaderRequest>(new SecurityLoaderRequest());
    }

    @Override
    public Class<? extends SecurityLoaderRequest> beanType() {
      return SecurityLoaderRequest.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code externalIdBundles} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<ExternalIdBundle>> externalIdBundles() {
      return _externalIdBundles;
    }

    /**
     * The meta-property for the {@code forceUpdate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> forceUpdate() {
      return _forceUpdate;
    }

    /**
     * The meta-property for the {@code returnSecurityObjects} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> returnSecurityObjects() {
      return _returnSecurityObjects;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}