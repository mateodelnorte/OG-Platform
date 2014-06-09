/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.regression;

import org.joda.beans.BeanDefinition;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;

import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.master.historicaltimeseries.ManageableHistoricalTimeSeries;
import com.opengamma.master.historicaltimeseries.ManageableHistoricalTimeSeriesInfo;
import com.opengamma.util.ArgumentChecker;
import java.util.Map;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 *
 */
@BeanDefinition
public class TimeSeriesWithInfo extends DirectBean implements UniqueIdentifiable {

  @PropertyDefinition(validate = "notNull")
  private ManageableHistoricalTimeSeriesInfo _info;

  @PropertyDefinition(validate = "notNull")
  private ManageableHistoricalTimeSeries _timeSeries;

  public TimeSeriesWithInfo() {
  }

  public TimeSeriesWithInfo(ManageableHistoricalTimeSeriesInfo info, ManageableHistoricalTimeSeries timeSeries) {
    ArgumentChecker.notNull(info, "info");
    ArgumentChecker.notNull(timeSeries, "timeSeries");
    _info = info;
    _timeSeries = timeSeries;
  }

  @Override
  public UniqueId getUniqueId() {
    return _timeSeries.getUniqueId();
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code TimeSeriesWithInfo}.
   * @return the meta-bean, not null
   */
  public static TimeSeriesWithInfo.Meta meta() {
    return TimeSeriesWithInfo.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(TimeSeriesWithInfo.Meta.INSTANCE);
  }

  @Override
  public TimeSeriesWithInfo.Meta metaBean() {
    return TimeSeriesWithInfo.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 3237038:  // info
        return getInfo();
      case 779431844:  // timeSeries
        return getTimeSeries();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 3237038:  // info
        setInfo((ManageableHistoricalTimeSeriesInfo) newValue);
        return;
      case 779431844:  // timeSeries
        setTimeSeries((ManageableHistoricalTimeSeries) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_info, "info");
    JodaBeanUtils.notNull(_timeSeries, "timeSeries");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      TimeSeriesWithInfo other = (TimeSeriesWithInfo) obj;
      return JodaBeanUtils.equal(getInfo(), other.getInfo()) &&
          JodaBeanUtils.equal(getTimeSeries(), other.getTimeSeries());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getInfo());
    hash += hash * 31 + JodaBeanUtils.hashCode(getTimeSeries());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the info.
   * @return the value of the property, not null
   */
  public ManageableHistoricalTimeSeriesInfo getInfo() {
    return _info;
  }

  /**
   * Sets the info.
   * @param info  the new value of the property, not null
   */
  public void setInfo(ManageableHistoricalTimeSeriesInfo info) {
    JodaBeanUtils.notNull(info, "info");
    this._info = info;
  }

  /**
   * Gets the the {@code info} property.
   * @return the property, not null
   */
  public final Property<ManageableHistoricalTimeSeriesInfo> info() {
    return metaBean().info().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the timeSeries.
   * @return the value of the property, not null
   */
  public ManageableHistoricalTimeSeries getTimeSeries() {
    return _timeSeries;
  }

  /**
   * Sets the timeSeries.
   * @param timeSeries  the new value of the property, not null
   */
  public void setTimeSeries(ManageableHistoricalTimeSeries timeSeries) {
    JodaBeanUtils.notNull(timeSeries, "timeSeries");
    this._timeSeries = timeSeries;
  }

  /**
   * Gets the the {@code timeSeries} property.
   * @return the property, not null
   */
  public final Property<ManageableHistoricalTimeSeries> timeSeries() {
    return metaBean().timeSeries().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code TimeSeriesWithInfo}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<ManageableHistoricalTimeSeriesInfo> _info = DirectMetaProperty.ofReadWrite(
        this, "info", TimeSeriesWithInfo.class, ManageableHistoricalTimeSeriesInfo.class);
    /**
     * The meta-property for the {@code timeSeries} property.
     */
    private final MetaProperty<ManageableHistoricalTimeSeries> _timeSeries = DirectMetaProperty.ofReadWrite(
        this, "timeSeries", TimeSeriesWithInfo.class, ManageableHistoricalTimeSeries.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "timeSeries");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return _info;
        case 779431844:  // timeSeries
          return _timeSeries;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends TimeSeriesWithInfo> builder() {
      return new DirectBeanBuilder<TimeSeriesWithInfo>(new TimeSeriesWithInfo());
    }

    @Override
    public Class<? extends TimeSeriesWithInfo> beanType() {
      return TimeSeriesWithInfo.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ManageableHistoricalTimeSeriesInfo> info() {
      return _info;
    }

    /**
     * The meta-property for the {@code timeSeries} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ManageableHistoricalTimeSeries> timeSeries() {
      return _timeSeries;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
