/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.view.rest;

import javax.ws.rs.Path;

import org.fudgemsg.FudgeContext;

import com.opengamma.core.position.Portfolio;
import com.opengamma.core.position.PositionSource;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.function.CompiledFunctionService;
import com.opengamma.engine.view.compilation.PortfolioCompiler;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;

/**
 * REST interface to the AvailableOutputs helper
 */
@Path("availableOutputs")
public class AvailableOutputsService {

  private final CompiledFunctionService _compiledFunctions;
  private final FudgeContext _fudgeContext;
  private final PositionSource _positionSource;
  private final SecuritySource _securitySource;
  private String _anyValue;

  public AvailableOutputsService(final FudgeContext fudgeContext, final CompiledFunctionService compiledFunctionService, final PositionSource positionSource, final SecuritySource securitySource) {
    ArgumentChecker.notNull(fudgeContext, "fudgeContext");
    ArgumentChecker.notNull(compiledFunctionService, "compiledFunctionService");
    ArgumentChecker.notNull(positionSource, "positionSource");
    ArgumentChecker.notNull(securitySource, "securitySource");
    _fudgeContext = fudgeContext;
    _compiledFunctions = compiledFunctionService;
    _positionSource = positionSource;
    _securitySource = securitySource;
  }

  public CompiledFunctionService getCompiledFunctions() {
    return _compiledFunctions;
  }

  public FudgeContext getFudgeContext() {
    return _fudgeContext;
  }

  public PositionSource getPositionSource() {
    return _positionSource;
  }

  public SecuritySource getSecuritySource() {
    return _securitySource;
  }

  public String getWildcardIndicator() {
    return _anyValue;
  }

  public void setWildcardIndicator(final String anyValue) {
    _anyValue = anyValue;
  }

  protected Portfolio getPortfolio(final String uid) {
    final Portfolio rawPortfolio = getPositionSource().getPortfolio(UniqueId.parse(uid));
    if (rawPortfolio == null) {
      return null;
    }
    return PortfolioCompiler.resolvePortfolio(rawPortfolio, getCompiledFunctions().getExecutorService(), getSecuritySource());
  }

  @Path("portfolio")
  public AvailablePortfolioOutputsResource portfolio() {
    return new AvailablePortfolioOutputsResource(this);
  }

}
