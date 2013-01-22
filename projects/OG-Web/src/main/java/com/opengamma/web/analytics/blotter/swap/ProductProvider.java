/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.blotter.swap;

import com.opengamma.financial.security.swap.SwapSecurity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.Pair;
import com.opengamma.web.analytics.blotter.BlotterColumnMappings;

/**
*
*/
public class ProductProvider implements BlotterColumnMappings.ValueProvider<SwapSecurity> {

  @Override
  public String getValue(SwapSecurity security) {
    Pair<Currency,Currency> currencies = new CurrencyVisitor().visit(security);
    // if the leg currencies are the same just use the code, if they're different use both codes with the
    // fixed currency first or the pay currency first for float/float swaps
    if (currencies.getFirst().equals(currencies.getSecond())) {
      return currencies.getFirst().getCode();
    } else {
      return currencies.getFirst() + "/" + currencies.getSecond();
    }
  }
}