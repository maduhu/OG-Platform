/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.integration.loadsave.portfolio.web.PortfolioLoaderResource;

/**
 * Factory for registering REST components from the OG-Integration project that can't be configured with the other
 * REST resources because they're not available to the OG-Web project component factories.
 */
@BeanDefinition
public class IntegrationWebComponentFactory extends AbstractComponentFactory {

  //@PropertyDefinition(validate = "notNull")
  //private BloombergSecurityMaster _bloombergSecurityMaster;
  //@PropertyDefinition(validate = "notNull")
  //private HistoricalTimeSeriesMaster _historicalTimeSeriesMaster;
  //@PropertyDefinition(validate = "notNull")
  //private HistoricalTimeSeriesSource _bloombergHistoricalTimeSeriesSource;
  //@PropertyDefinition(validate = "notNull")
  //private ReferenceDataProvider _bloombergReferenceDataProvider;
  //@PropertyDefinition(validate = "notNull")
  //private PortfolioMaster _portfolioMaster;
  //@PropertyDefinition(validate = "notNull")
  //private PositionMaster _positionMaster;
  //@PropertyDefinition(validate = "notNull")
  //private SecurityMaster _securityMaster;

    @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) throws Exception {
      repo.getRestComponents().publishResource(new PortfolioLoaderResource());
      /*repo.getRestComponents().publishResource(new PortfolioLoaderResource(_bloombergSecurityMaster,
                                                                           _historicalTimeSeriesMaster,
                                                                           _bloombergHistoricalTimeSeriesSource,
                                                                           _bloombergReferenceDataProvider,
                                                                           _portfolioMaster,
                                                                           _positionMaster,
                                                                           _securityMaster));*/
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code IntegrationWebComponentFactory}.
   * @return the meta-bean, not null
   */
  public static IntegrationWebComponentFactory.Meta meta() {
    return IntegrationWebComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(IntegrationWebComponentFactory.Meta.INSTANCE);
  }

  @Override
  public IntegrationWebComponentFactory.Meta metaBean() {
    return IntegrationWebComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code IntegrationWebComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends IntegrationWebComponentFactory> builder() {
      return new DirectBeanBuilder<IntegrationWebComponentFactory>(new IntegrationWebComponentFactory());
    }

    @Override
    public Class<? extends IntegrationWebComponentFactory> beanType() {
      return IntegrationWebComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
