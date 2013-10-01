/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.cycle;

import java.util.Collection;
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

import com.google.common.collect.Lists;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.PublicSPI;
import com.opengamma.util.tuple.Pair;

/**
 * Encapsulates the results from a computation cache query.
 */
@PublicSPI
@BeanDefinition
public class ComputationCacheResponse extends DirectBean {

  /**
   * The results obtained from the computation caches.
   */
  @PropertyDefinition
  private List<Pair<ValueSpecification, Object>> _results;

  public void setResults(Collection<Pair<ValueSpecification, Object>> result) {
    setResults(Lists.newArrayList(result));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ComputationCacheResponse}.
   * @return the meta-bean, not null
   */
  public static ComputationCacheResponse.Meta meta() {
    return ComputationCacheResponse.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ComputationCacheResponse.Meta.INSTANCE);
  }

  @Override
  public ComputationCacheResponse.Meta metaBean() {
    return ComputationCacheResponse.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the results obtained from the computation caches.
   * @return the value of the property
   */
  public List<Pair<ValueSpecification, Object>> getResults() {
    return _results;
  }

  /**
   * Sets the results obtained from the computation caches.
   * @param results  the new value of the property
   */
  public void setResults(List<Pair<ValueSpecification, Object>> results) {
    this._results = results;
  }

  /**
   * Gets the the {@code results} property.
   * @return the property, not null
   */
  public final Property<List<Pair<ValueSpecification, Object>>> results() {
    return metaBean().results().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ComputationCacheResponse clone() {
    BeanBuilder<? extends ComputationCacheResponse> builder = metaBean().builder();
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
      ComputationCacheResponse other = (ComputationCacheResponse) obj;
      return JodaBeanUtils.equal(getResults(), other.getResults());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getResults());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("ComputationCacheResponse{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("results").append('=').append(getResults()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ComputationCacheResponse}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code results} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Pair<ValueSpecification, Object>>> _results = DirectMetaProperty.ofReadWrite(
        this, "results", ComputationCacheResponse.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "results");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1097546742:  // results
          return _results;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ComputationCacheResponse> builder() {
      return new DirectBeanBuilder<ComputationCacheResponse>(new ComputationCacheResponse());
    }

    @Override
    public Class<? extends ComputationCacheResponse> beanType() {
      return ComputationCacheResponse.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code results} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Pair<ValueSpecification, Object>>> results() {
      return _results;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1097546742:  // results
          return ((ComputationCacheResponse) bean).getResults();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1097546742:  // results
          ((ComputationCacheResponse) bean).setResults((List<Pair<ValueSpecification, Object>>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
