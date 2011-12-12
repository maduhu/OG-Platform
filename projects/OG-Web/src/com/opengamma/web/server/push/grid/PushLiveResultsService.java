/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.push.grid;

import com.opengamma.DataNotFoundException;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.position.PositionSource;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.engine.view.client.ViewClient;
import com.opengamma.financial.aggregation.AggregationFunction;
import com.opengamma.financial.view.ManageableViewDefinitionRepository;
import com.opengamma.id.UniqueId;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.web.server.AggregatedViewDefinitionManager;
import com.opengamma.web.server.conversion.ResultConverterCache;
import com.opengamma.web.server.push.AnalyticsListener;
import com.opengamma.web.server.push.Viewport;
import com.opengamma.web.server.push.ViewportDefinition;
import com.opengamma.web.server.push.ViewportFactory;
import org.fudgemsg.FudgeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Connects the REST interface to the engine.
 * TODO temporary name just to distinguish it from the similarly named class in the parent package
 */
/* package */ class PushLiveResultsService implements ViewportFactory {

  private static final Logger s_logger = LoggerFactory.getLogger(PushLiveResultsService.class);

  /** Client's web view keyed on viewport ID */
  private final Map<String, PushWebView> _viewportIdToView = new HashMap<String, PushWebView>();
  /** Viewport IDs keyed on client ID */
  private final Map<String, String> _clientIdToViewportId = new HashMap<String, String>();
  private final ViewProcessor _viewProcessor;
  private final UserPrincipal _user;
  private final ResultConverterCache _resultConverterCache;
  private final Object _lock = new Object();
  private final AggregatedViewDefinitionManager _aggregatedViewDefinitionManager;

  public PushLiveResultsService(ViewProcessor viewProcessor,
                                PositionSource positionSource,
                                SecuritySource securitySource,
                                PortfolioMaster userPortfolioMaster,
                                PositionMaster userPositionMaster,
                                ManageableViewDefinitionRepository userViewDefinitionRepository,
                                UserPrincipal user,
                                FudgeContext fudgeContext,
                                List<AggregationFunction<?>> portfolioAggregators) {
    ArgumentChecker.notNull(viewProcessor, "viewProcessor");
    ArgumentChecker.notNull(user, "user");

    _viewProcessor = viewProcessor;
    _user = user;
    _resultConverterCache = new ResultConverterCache(fudgeContext);
    _aggregatedViewDefinitionManager = new AggregatedViewDefinitionManager(positionSource,
                                                                           securitySource,
                                                                           viewProcessor.getViewDefinitionRepository(),
                                                                           userViewDefinitionRepository,
                                                                           userPortfolioMaster,
                                                                           userPositionMaster,
                                                                           mapPortfolioAggregators(portfolioAggregators));
  }

  public void clientDisconnected(String clientId) {
    s_logger.debug("Client " + clientId + " disconnected");
    PushWebView view = null;
    synchronized (_lock) {
      String viewportId = _clientIdToViewportId.remove(clientId);
      if (viewportId != null) {
        view = _viewportIdToView.remove(viewportId);
      }
    }
    if (view != null) {
      shutDownWebView(view);
    }
  }

  // TODO why is this here and not in the constructor of AggregatedViewDefinitionManager?
  private static Map<String, AggregationFunction<?>> mapPortfolioAggregators(List<AggregationFunction<?>> portfolioAggregators) {
    Map<String, AggregationFunction<?>> result = new HashMap<String, AggregationFunction<?>>();
    for (AggregationFunction<?> portfolioAggregator : portfolioAggregators) {
      result.put(portfolioAggregator.getName(), portfolioAggregator);
    }
    return result;
  }

  // used by the REST interface that gets analytics as CSV - will that be moved here?
  // TODO might be better to pass the call through rather than returning the WebView and calling that
  /*public PushWebView getClientView(String clientId) {
    synchronized (_lock) {
      String viewportId = _clientIdToViewportId.get(clientId);
      return _viewportIdToView.get(viewportId);
    }
  }*/

  @Override
  public Viewport createViewport(String clientId, String viewportId, ViewportDefinition viewportDefinition, AnalyticsListener listener) {
    synchronized (_lock) {
      UniqueId baseViewDefinitionId = getViewDefinitionId(viewportDefinition.getViewDefinitionName());
      String currentViewportId = _clientIdToViewportId.remove(clientId);
      PushWebView webView;
      String aggregatorName = viewportDefinition.getAggregatorName();

      if (currentViewportId != null) {
        webView = _viewportIdToView.get(currentViewportId);
        // TODO is this relevant any more?
        if (webView != null) {
          if (webView.matches(baseViewDefinitionId, viewportDefinition)) {
            // Already initialized
            // TODO is there any possibility the WebView won't have a compiled view def at this point?
            return webView.configureViewport(viewportDefinition, listener);
          }
          // Existing view is different - client is switching views
          shutDownWebView(webView);
          _viewportIdToView.remove(currentViewportId);
        }
      }

      ViewClient viewClient = _viewProcessor.createViewClient(_user);
      try {
        UniqueId viewDefinitionId = _aggregatedViewDefinitionManager.getViewDefinitionId(baseViewDefinitionId, aggregatorName);
        webView = new PushWebView(viewClient, viewportDefinition, baseViewDefinitionId, viewDefinitionId, _resultConverterCache, listener);
      } catch (Exception e) {
        viewClient.shutdown();
        throw new OpenGammaRuntimeException("Error attaching client to view definition '" + baseViewDefinitionId + "'", e);
      }
      _viewportIdToView.put(viewportId, webView);
      _clientIdToViewportId.put(clientId, viewportId);
      return webView;
    }
  }

  private UniqueId getViewDefinitionId(String viewDefinitionName) {
    ViewDefinition view = _viewProcessor.getViewDefinitionRepository().getDefinition(viewDefinitionName);
    if (view == null) {
      throw new OpenGammaRuntimeException("Unable to find view definition with name " + viewDefinitionName);
    }
    return view.getUniqueId().toLatest();
  }

  private void shutDownWebView(PushWebView webView) {
    webView.shutdown();
    _aggregatedViewDefinitionManager.releaseViewDefinition(webView.getBaseViewDefinitionId(), webView.getAggregatorName());
  }

  @Override
  public Viewport getViewport(String viewportId) {
    synchronized (_lock) {
      PushWebView view = _viewportIdToView.get(viewportId);
      if (view == null) {
        throw new DataNotFoundException("Unable to find viewport for key: " + viewportId);
      }
      return view;
    }
  }

  /*@SuppressWarnings("unchecked")
  public void processUpdateModeRequest(Client remote, Message message) {
    WebView webView = getClientView(remote.getId());
    if (webView == null) {
      return;
    }
    Map<String, Object> dataMap = (Map<String, Object>) message.getData();
    String gridName = (String) dataMap.get("gridName");
    long jsRowId = (Long) dataMap.get("rowId");
    long jsColId = (Long) dataMap.get("colId");
    ConversionMode mode = ConversionMode.valueOf((String) dataMap.get("mode"));
    WebViewGrid grid = webView.getGridByName(gridName);
    if (grid == null) {
      s_logger.warn("Request to change update mode for cell in unknown grid '{}'", gridName);
    } else {
      grid.setConversionMode(WebGridCell.of((int) jsRowId, (int) jsColId), mode);
    }
  }*/
}
