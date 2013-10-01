/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.batch.rest;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.beans.Bean;
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

import com.opengamma.batch.domain.RiskRun;
import com.opengamma.util.PublicSPI;
import com.opengamma.util.paging.Paging;

/**
 * Result from searching for live data values.
 * <p>
 */
@PublicSPI
@BeanDefinition
public class BatchRunSearchResult extends DirectBean {

  /**
   * Creates an instance.
   */
  public BatchRunSearchResult() {
  }
  
  /**
   * The paging information, not null if correctly created.
   */
  @PropertyDefinition
  private Paging _paging;
  /**
   * The values, not null.
   */
  @PropertyDefinition
  private final List<RiskRun> _values = new ArrayList<RiskRun>();


  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BatchRunSearchResult}.
   * @return the meta-bean, not null
   */
  public static BatchRunSearchResult.Meta meta() {
    return BatchRunSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(BatchRunSearchResult.Meta.INSTANCE);
  }

  @Override
  public BatchRunSearchResult.Meta metaBean() {
    return BatchRunSearchResult.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the paging information, not null if correctly created.
   * @return the value of the property
   */
  public Paging getPaging() {
    return _paging;
  }

  /**
   * Sets the paging information, not null if correctly created.
   * @param paging  the new value of the property
   */
  public void setPaging(Paging paging) {
    this._paging = paging;
  }

  /**
   * Gets the the {@code paging} property.
   * @return the property, not null
   */
  public final Property<Paging> paging() {
    return metaBean().paging().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the values, not null.
   * @return the value of the property
   */
  public List<RiskRun> getValues() {
    return _values;
  }

  /**
   * Sets the values, not null.
   * @param values  the new value of the property
   */
  public void setValues(List<RiskRun> values) {
    this._values.clear();
    this._values.addAll(values);
  }

  /**
   * Gets the the {@code values} property.
   * @return the property, not null
   */
  public final Property<List<RiskRun>> values() {
    return metaBean().values().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public BatchRunSearchResult clone() {
    BeanBuilder<? extends BatchRunSearchResult> builder = metaBean().builder();
    for (MetaProperty<?> mp : metaBean().metaPropertyIterable()) {
      if (mp.style().isBuildable()) {
        Object value = mp.get(this);
        if (value instanceof Bean) {
          value = ((Bean) value).clone();
        }
        builder.set(mp.name(), value);
      }
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BatchRunSearchResult other = (BatchRunSearchResult) obj;
      return JodaBeanUtils.equal(getPaging(), other.getPaging()) &&
          JodaBeanUtils.equal(getValues(), other.getValues());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getPaging());
    hash += hash * 31 + JodaBeanUtils.hashCode(getValues());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("BatchRunSearchResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("paging").append('=').append(getPaging()).append(',').append(' ');
    buf.append("values").append('=').append(getValues()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BatchRunSearchResult}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code paging} property.
     */
    private final MetaProperty<Paging> _paging = DirectMetaProperty.ofReadWrite(
        this, "paging", BatchRunSearchResult.class, Paging.class);
    /**
     * The meta-property for the {@code values} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<RiskRun>> _values = DirectMetaProperty.ofReadWrite(
        this, "values", BatchRunSearchResult.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "paging",
        "values");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          return _paging;
        case -823812830:  // values
          return _values;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BatchRunSearchResult> builder() {
      return new DirectBeanBuilder<BatchRunSearchResult>(new BatchRunSearchResult());
    }

    @Override
    public Class<? extends BatchRunSearchResult> beanType() {
      return BatchRunSearchResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code paging} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Paging> paging() {
      return _paging;
    }

    /**
     * The meta-property for the {@code values} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<RiskRun>> values() {
      return _values;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          return ((BatchRunSearchResult) bean).getPaging();
        case -823812830:  // values
          return ((BatchRunSearchResult) bean).getValues();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          ((BatchRunSearchResult) bean).setPaging((Paging) newValue);
          return;
        case -823812830:  // values
          ((BatchRunSearchResult) bean).setValues((List<RiskRun>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
