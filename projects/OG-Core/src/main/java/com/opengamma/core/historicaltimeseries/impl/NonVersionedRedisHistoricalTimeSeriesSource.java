/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.core.historicaltimeseries.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeries;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.UniqueId;
import com.opengamma.timeseries.date.localdate.ImmutableLocalDateDoubleTimeSeries;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.timeseries.date.localdate.LocalDateDoubleTimeSeriesBuilder;
import com.opengamma.timeseries.date.localdate.LocalDateToIntConverter;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.metric.MetricProducer;
import com.opengamma.util.tuple.Pair;

/**
 * An extremely minimal and lightweight {@code HistoricalTimeSeriesSource} that pulls data
 * directly from Redis for situations where versioning, multiple fields, multiple data sources,
 * and identifier management is not necessary.
 * <p/>
 * The following constraints must hold for this Source to be of any utility whatsoever:
 * <ul>
 *   <li>Historical lookups are not required. Because they are not supported.</li>
 *   <li>Version corrections are not required. Because they are not supported.</li>
 *   <li>Each time series has a <b>single</b> {@link ExternalId} which then acts
 *       as the {@link UniqueId} internally.</li>
 *   <li>Each external ID has a single time series (thus there is not the capacity to store
 *       different Data Source, Data Provider, Observation Time, Data Field series).</li>
 * </ul>
 * <p/>
 * Where a method is not supported semantically, an {@link UnsupportedOperationException}
 * will be thrown. Where use indicates that this class may be being used incorrectly,
 * a log message will be written at {@code WARN} level.
 */
public class NonVersionedRedisHistoricalTimeSeriesSource implements HistoricalTimeSeriesSource, MetricProducer {
  private static final Logger s_logger = LoggerFactory.getLogger(NonVersionedRedisHistoricalTimeSeriesSource.class);
  private final JedisPool _jedisPool;
  private final String _redisPrefix;
  
  private Timer _getSeriesTimer = new Timer();
  private Timer _updateSeriesTimer = new Timer();
  
  public NonVersionedRedisHistoricalTimeSeriesSource(JedisPool jedisPool) {
    this(jedisPool, "");
  }
    
  public NonVersionedRedisHistoricalTimeSeriesSource(JedisPool jedisPool, String redisPrefix) {
    ArgumentChecker.notNull(jedisPool, "jedisPool");
    ArgumentChecker.notNull(redisPrefix, "redisPrefix");
    _jedisPool = jedisPool;
    _redisPrefix = redisPrefix;
  }

  /**
   * Gets the jedisPool.
   * @return the jedisPool
   */
  protected JedisPool getJedisPool() {
    return _jedisPool;
  }

  /**
   * Gets the redisPrefix.
   * @return the redisPrefix
   */
  protected String getRedisPrefix() {
    return _redisPrefix;
  }
  
  @Override
  public void registerMetrics(MetricRegistry summaryRegistry, MetricRegistry detailRegistry, String namePrefix) {
    _getSeriesTimer = summaryRegistry.timer(namePrefix + ".get");
    _updateSeriesTimer = summaryRegistry.timer(namePrefix + ".update");
  }
  
  public void updateTimeSeries(UniqueId uniqueId, LocalDateDoubleTimeSeries timeseries) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    ArgumentChecker.notNull(timeseries, "timeseries");
    
    updateTimeSeries(toRedisKey(uniqueId), timeseries);
  }
  
  protected void updateTimeSeries(String redisKey, LocalDateDoubleTimeSeries timeseries) {    
    try (Timer.Context context = _updateSeriesTimer.time()) {
      LocalDateDoubleTimeSeries previousHts = loadTimeSeriesFromRedis(redisKey);
      Jedis jedis = getJedisPool().getResource();
      try {
        LocalDateDoubleTimeSeries mergedHts = timeseries;
        if (previousHts != null) {
          mergedHts = mergeTimeseries(previousHts, timeseries);
          jedis.del(redisKey);
        }
        jedis.rpush(redisKey, encodeTimeseries(mergedHts));
        getJedisPool().returnResource(jedis);
      } catch (Exception e) {
        s_logger.error("Unable to put timeseries with id: " + redisKey, e);
        getJedisPool().returnBrokenResource(jedis);
        throw new OpenGammaRuntimeException("Unable to put timeseries with id: " + redisKey, e);
      }
    }
  }
  
  public void updateTimeSeriesPoint(UniqueId uniqueId, LocalDate valueDate, double value) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    ArgumentChecker.notNull(valueDate, "valueDate");
    
    updateTimeSeriesPoint(toRedisKey(uniqueId), valueDate, value);
  }
  
  protected void updateTimeSeriesPoint(String redisKey, LocalDate valueDate, double value) {
    LocalDateDoubleTimeSeriesBuilder builder = ImmutableLocalDateDoubleTimeSeries.builder();
    builder.put(valueDate, value);
    updateTimeSeries(redisKey, builder.build());
  }
  
  private String[] encodeTimeseries(LocalDateDoubleTimeSeries timeseries) {
    String[] tsArray = new String[timeseries.size() * 2];
    int index = 0;
    for (Entry<LocalDate, Double> entry : timeseries) {
      final LocalDate date = entry.getKey();
      final Double value = entry.getValue();
      tsArray[index++] = Integer.toString(LocalDateToIntConverter.convertToInt(date));
      tsArray[index++] = Double.toString(value);
    }
    return tsArray;
  }

  private LocalDateDoubleTimeSeries mergeTimeseries(final LocalDateDoubleTimeSeries previousHts, final LocalDateDoubleTimeSeries currentHts) {
    LocalDateDoubleTimeSeriesBuilder builder = ImmutableLocalDateDoubleTimeSeries.builder();
    for (Entry<LocalDate, Double> entry : previousHts) {
      builder.put(entry.getKey(), entry.getValue());
    }
    for (Entry<LocalDate, Double> entry : currentHts) {
      builder.put(entry.getKey(), entry.getValue());
    }
    return builder.build();
  }
  
  /**
   * Completely empty the underlying Redis server.
   * You should only call this if you really know what you're doing.
   */
  public void completelyClearRedis() {
    Jedis jedis = getJedisPool().getResource();
    try {
      jedis.flushDB();
      getJedisPool().returnResource(jedis);
    } catch (Exception e) {
      s_logger.error("Unable to clear database", e);
      getJedisPool().returnBrokenResource(jedis);
      throw new OpenGammaRuntimeException("Unable to clear database", e);
    }
  }
  
  protected String toRedisKey(UniqueId uniqueId) {
    return toRedisKey(uniqueId, null);
  }
  
  protected String toRedisKey(ExternalIdBundle identifierBundle) {
    return toRedisKey(toUniqueId(identifierBundle));
  }
  
  protected String toRedisKey(UniqueId uniqueId, LocalDate simulationExecutionDate) {
    StringBuilder sb = new StringBuilder();
    
    sb.append(getRedisPrefix());
    sb.append('_');
    sb.append(uniqueId);
    if (simulationExecutionDate != null) {
      sb.append('_');
      sb.append(simulationExecutionDate.toString());
    }
    
    return sb.toString();
  }
  
  protected UniqueId toUniqueId(ExternalIdBundle identifierBundle) {
    if (identifierBundle.size() != 1) {
      s_logger.warn("Using NonVersionedRedisHistoricalTimeSeriesSource with bundle {} other than 1. Probable misuse.", identifierBundle);
    }
    ExternalId id = identifierBundle.iterator().next();
    UniqueId uniqueId = UniqueId.of(id.getScheme().getName(), id.getValue());
    return uniqueId;
  }
    
  // ------------------------------------------------------------------------
  // SUPPORTED HISTORICAL TIME SERIES SOURCE OPERATIONS:
  // ------------------------------------------------------------------------
    
  protected LocalDateDoubleTimeSeries loadTimeSeriesFromRedis(String redisKey) {
    // This is the only method that needs implementation.
    try (Timer.Context context = _getSeriesTimer.time()) {
      Jedis jedis = getJedisPool().getResource();
      LocalDateDoubleTimeSeries ts = null;
      try {
        List<String> htsDataPoints = jedis.lrange(redisKey, 0, -1);
        if (!htsDataPoints.isEmpty()) {
          List<LocalDate> times = Lists.newArrayListWithCapacity(htsDataPoints.size() / 2);
          List<Double> values = Lists.newArrayListWithCapacity(htsDataPoints.size() / 2);
          
          int counter = 0;
          for (String dataPoint : htsDataPoints) {
            if ((counter % 2) == 0) {
              times.add(LocalDateToIntConverter.convertToLocalDate(Integer.parseInt(dataPoint)));
            } else {
              values.add(Double.parseDouble(dataPoint));
            }
            counter++;
          }
          ts = ImmutableLocalDateDoubleTimeSeries.of(times, values);
        }
        getJedisPool().returnResource(jedis);
      } catch (Exception e) {
        s_logger.error("Unable to load points from redis for " + redisKey, e);
        getJedisPool().returnBrokenResource(jedis);
        throw new OpenGammaRuntimeException("Unable to load points from redis for " + redisKey, e);
      }
      return ts;
    }
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(UniqueId uniqueId, LocalDate start, boolean includeStart, LocalDate end, boolean includeEnd) {
    LocalDateDoubleTimeSeries ts = loadTimeSeriesFromRedis(toRedisKey(uniqueId));
    if (ts == null) {
      return null;
    }
    
    if (start != null) {
      ArgumentChecker.notNull(end, "end");
      ts = ts.subSeries(start, includeStart, end, includeEnd);
    }
    return new SimpleHistoricalTimeSeries(uniqueId, ts);
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, String resolutionKey, LocalDate start, boolean includeStart, LocalDate end,
                                                      boolean includeEnd, int maxPoints) {
    if (identifierBundle.isEmpty()) {
      return null;
    }
    final ExternalId id = identifierBundle.iterator().next();
    final UniqueId uniqueId = UniqueId.of(id.getScheme().getName(), id.getValue());
    return getHistoricalTimeSeries(uniqueId, start, includeStart, end, includeEnd);
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(UniqueId uniqueId) {
    s_logger.debug("getHistoricalTimeSeries for {}", uniqueId);
    LocalDateDoubleTimeSeries ts = loadTimeSeriesFromRedis(toRedisKey(uniqueId));
    if (ts == null) {
      return null;
    } else {
      return new SimpleHistoricalTimeSeries(uniqueId, ts);
    }
  }
  
  public ExternalIdBundle getExternalIdBundle(UniqueId uniqueId) {
    return ExternalId.of(uniqueId.getScheme(), uniqueId.getValue()).toBundle();
  }
  
  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(UniqueId uniqueId) {
    try (Timer.Context context = _getSeriesTimer.time()) {
      Pair<LocalDate, Double> latestPoint = null;
      LocalDateDoubleTimeSeries ts = loadTimeSeriesFromRedis(toRedisKey(uniqueId));
      if (ts != null) {
        latestPoint = Pair.of(ts.getLatestTime(), ts.getLatestValue());
      }
      return latestPoint;
    }
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(UniqueId uniqueId, LocalDate start, boolean includeStart, LocalDate end, boolean includeEnd) {
    LocalDateDoubleTimeSeries ts = loadTimeSeriesFromRedis(toRedisKey(uniqueId));
    if (ts == null) {
      return null;
    }
    
    if (start != null) {
      ArgumentChecker.notNull(end, "end");
      ts = ts.subSeries(start, includeStart, end, includeEnd);
    }
    return Pair.of(ts.getLatestTime(), ts.getLatestValue());
  }
  
  protected LocalDateDoubleTimeSeries getLocalDateDoubleTimeSeries(ExternalIdBundle identifierBundle) {
    return loadTimeSeriesFromRedis(toRedisKey(identifierBundle));
  }
  
  protected HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    
    LocalDateDoubleTimeSeries ts = getLocalDateDoubleTimeSeries(identifierBundle);
    HistoricalTimeSeries hts = new SimpleHistoricalTimeSeries(uniqueId, ts);
    return hts;
  }

  @Override
  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, String dataSource, String dataProvider, String dataField) {
    return getHistoricalTimeSeries(identifierBundle);
  }

  @Override
  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String dataSource, String dataProvider, String dataField) {
    return getHistoricalTimeSeries(identifierBundle);
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String dataSource, String dataProvider, String dataField) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getLatestDataPoint(uniqueId);
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(ExternalIdBundle identifierBundle, String dataSource, String dataProvider, String dataField) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getLatestDataPoint(uniqueId);
  }

  @Override
  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, String resolutionKey) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getHistoricalTimeSeries(uniqueId);
  }

  @Override
  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String resolutionKey) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getHistoricalTimeSeries(uniqueId);
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(String dataField, ExternalIdBundle identifierBundle, String resolutionKey) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getLatestDataPoint(uniqueId);
  }

  @Override
  public Pair<LocalDate, Double> getLatestDataPoint(String dataField, ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String resolutionKey) {
    UniqueId uniqueId = toUniqueId(identifierBundle);
    return getLatestDataPoint(uniqueId);
  }

  // ------------------------------------------------------------------------
  // UNSUPPORTED OPERATIONS:
  // ------------------------------------------------------------------------
  public ChangeManager changeManager() {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(UniqueId uniqueId, LocalDate start, boolean includeStart, LocalDate end, boolean includeEnd, int maxPoints) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, String dataSource, String dataProvider, String dataField, LocalDate start, boolean includeStart,
      LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, String dataSource, String dataProvider, String dataField, LocalDate start, boolean includeStart,
      LocalDate end, boolean includeEnd, int maxPoints) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String dataSource, String dataProvider, String dataField, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String dataSource, String dataProvider, String dataField, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd, int maxPoints) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public Pair<LocalDate, Double> getLatestDataPoint(ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String dataSource, String dataProvider, String dataField, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public Pair<LocalDate, Double> getLatestDataPoint(ExternalIdBundle identifierBundle, String dataSource, String dataProvider, String dataField, LocalDate start, boolean includeStart, LocalDate end,
      boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, String resolutionKey, LocalDate start, boolean includeStart, LocalDate end,
      boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String resolutionKey, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public HistoricalTimeSeries getHistoricalTimeSeries(String dataField, ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String resolutionKey, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd, int maxPoints) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public Pair<LocalDate, Double> getLatestDataPoint(
      String dataField, ExternalIdBundle identifierBundle, String resolutionKey,
      LocalDate start, boolean includeStart, LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public Pair<LocalDate, Double> getLatestDataPoint(String dataField, ExternalIdBundle identifierBundle, LocalDate identifierValidityDate, String resolutionKey, LocalDate start, boolean includeStart,
      LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

  public Map<ExternalIdBundle, HistoricalTimeSeries> getHistoricalTimeSeries(Set<ExternalIdBundle> identifierSet, String dataSource, String dataProvider, String dataField, LocalDate start,
      boolean includeStart, LocalDate end, boolean includeEnd) {
    throw new UnsupportedOperationException("Unsupported operation.");
  }

}
