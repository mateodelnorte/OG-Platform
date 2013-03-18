/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.worker.cache;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapMaker;
import com.opengamma.engine.view.compilation.CompiledViewDefinitionWithGraphs;
import com.opengamma.util.ehcache.EHCacheUtils;

/**
 * An EH-Cache based implementation of {@link ViewExecutionCache}.
 */
public class EHCacheViewExecutionCache implements ViewExecutionCache {

  private static final Logger s_logger = LoggerFactory.getLogger(EHCacheViewExecutionCache.class);

  private static final String COMPILED_VIEW_DEFINITIONS = "compiledViewDefinitions";

  private final CacheManager _cacheManager;

  private final ConcurrentMap<ViewExecutionCacheKey, CompiledViewDefinitionWithGraphs> _compiledViewDefinitionsFrontCache = new MapMaker().weakValues().makeMap();

  private final Cache _compiledViewDefinitions;

  public EHCacheViewExecutionCache(final CacheManager cacheManager) {
    _cacheManager = cacheManager;
    EHCacheUtils.addCache(_cacheManager, COMPILED_VIEW_DEFINITIONS);
    _compiledViewDefinitions = EHCacheUtils.getCacheFromManager(_cacheManager, COMPILED_VIEW_DEFINITIONS);
  }

  private static final class CompiledViewDefinitionWithGraphsReader implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object readResolve() {
      s_logger.error("Loading spooled CompiledViewDefinitionWithGraphs is not supported");
      return new CompiledViewDefinitionWithGraphsHolder(null);
    }

  }

  private static final class CompiledViewDefinitionWithGraphsHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompiledViewDefinitionWithGraphs _viewDefinition;

    public CompiledViewDefinitionWithGraphsHolder(final CompiledViewDefinitionWithGraphs viewDefinition) {
      _viewDefinition = viewDefinition;
    }

    public CompiledViewDefinitionWithGraphs get() {
      return _viewDefinition;
    }

    private Object writeReplace() {
      s_logger.error("Spooling {} to disk not supported", _viewDefinition);
      return new CompiledViewDefinitionWithGraphsReader();
    }

  }

  @Override
  public CompiledViewDefinitionWithGraphs getCompiledViewDefinitionWithGraphs(ViewExecutionCacheKey key) {
    CompiledViewDefinitionWithGraphs graphs = _compiledViewDefinitionsFrontCache.get(key);
    if (graphs != null) {
      s_logger.debug("Front cache hit CompiledViewDefinitionWithGraphs for {}", key);
      return graphs;
    }
    final Element element = _compiledViewDefinitions.get(key);
    if (element != null) {
      s_logger.debug("EHCache hit CompiledViewDefinitionWithGraphs for {}", key);
      graphs = ((CompiledViewDefinitionWithGraphsHolder) element.getObjectValue()).get();
      if (graphs != null) {
        final CompiledViewDefinitionWithGraphs existing = _compiledViewDefinitionsFrontCache.putIfAbsent(key, graphs);
        if (existing != null) {
          graphs = existing;
        }
      } else {
        // This will only happen until we've fixed the serialisation
        s_logger.debug("Deserialisation miss CompiledViewDefinitionWithGraphs for {}", key);
      }
    } else {
      s_logger.debug("EHCache miss CompiledViewDefinitionWithGraphs for {}", key);
    }
    return graphs;
  }

  @Override
  public void setCompiledViewDefinitionWithGraphs(ViewExecutionCacheKey key, CompiledViewDefinitionWithGraphs viewDefinition) {
    CompiledViewDefinitionWithGraphs existing = _compiledViewDefinitionsFrontCache.putIfAbsent(key, viewDefinition);
    if (existing != null) {
      if (existing != viewDefinition) {
        s_logger.debug("Discarding updated CompiledViewDefinitionWithGraphs for {}", key);
      }
      return;
    }
    s_logger.info("Storing CompiledViewDefinitionWithGraphs for {}", key);
    _compiledViewDefinitions.put(new Element(key, viewDefinition));
  }

}
