/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.legalentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.Attributable;
import com.opengamma.core.legalentity.Account;
import com.opengamma.core.legalentity.Capability;
import com.opengamma.core.legalentity.LegalEntity;
import com.opengamma.core.legalentity.Obligation;
import com.opengamma.core.legalentity.Rating;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;
import org.joda.beans.impl.direct.DirectBeanBuilder;

/**
 * A legal entity.
 */
@PublicSPI
@BeanDefinition
public class ManageableLegalEntity
    implements LegalEntity, Bean, UniqueIdentifiable, MutableUniqueIdentifiable, Attributable, Serializable {

  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier of the legal entity.
   * This must be null when adding to a master and not null when retrieved from a master.
   */
  @PropertyDefinition
  private UniqueId _uniqueId;
  /**
   * The bundle of external identifiers that define the legal entity.
   * This field must not be null for the object to be valid.
   */
  @PropertyDefinition(validate = "notNull")
  private ExternalIdBundle _externalIdBundle = ExternalIdBundle.EMPTY;
  /**
   * The map of attributes, which can be used for attaching additional application-level information.
   */
  @PropertyDefinition
  private final Map<String, String> _attributes = new HashMap<>();
  /**
   * The name of the legal entity.
   * This field must not be null for the object to be valid.
   */
  @PropertyDefinition(validate = "notNull")
  private String _name = "";


  @PropertyDefinition(validate = "notNull")
  private Collection<Rating> _ratings = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Capability> _capabilities = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Object> _issuedSecurities = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Obligation> _obligations = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Account> _sccounts = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Object> _portfolios = new ArrayList<>();

  @PropertyDefinition(validate = "notNull")
  private Collection<Account> _accounts = new ArrayList<>();

  /**
   * Creates a legal entity.
   */
  protected ManageableLegalEntity() {
  }

  /**
   * Creates a legal entity specifying the values of the main fields.
   *
   * @param name             the name of the legal entity, not null
   * @param externalIdBundle the bundle of identifiers that define the legal entity, not null
   */
  protected ManageableLegalEntity(String name, ExternalIdBundle externalIdBundle) {
    ArgumentChecker.notNull(name, "name");
    ArgumentChecker.notNull(externalIdBundle, "externalIdBundle");
    setName(name);
    setExternalIdBundle(externalIdBundle);
  }

  /**
   * Creates a legal entity specifying the values of the main fields.
   *
   * @param uniqueId         the unique identifier, not null
   * @param name             the name of the legal entity, not null
   * @param externalIdBundle the bundle of identifiers that define the legal entity, not null
   */
  protected ManageableLegalEntity(UniqueId uniqueId, String name, ExternalIdBundle externalIdBundle) {
    ArgumentChecker.notNull(name, "name");
    ArgumentChecker.notNull(externalIdBundle, "externalIdBundle");
    setUniqueId(uniqueId);
    setName(name);
    setExternalIdBundle(externalIdBundle);
  }

  //-------------------------------------------------------------------------

  /**
   * Adds an external identifier to the bundle representing this legal entity.
   *
   * @param legalEntityId the identifier to add, not null
   */
  public void addExternalId(ExternalId legalEntityId) {
    setExternalIdBundle(getExternalIdBundle().withExternalId(legalEntityId));
  }

  @Override
  public void addAttribute(String key, String value) {
    ArgumentChecker.notNull(key, "key");
    ArgumentChecker.notNull(value, "value");
    _attributes.put(key, value);
  }


  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ManageableLegalEntity}.
   * @return the meta-bean, not null
   */
  public static ManageableLegalEntity.Meta meta() {
    return ManageableLegalEntity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ManageableLegalEntity.Meta.INSTANCE);
  }

  @Override
  public ManageableLegalEntity.Meta metaBean() {
    return ManageableLegalEntity.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unique identifier of the legal entity.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @return the value of the property
   */
  public UniqueId getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the unique identifier of the legal entity.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueId uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * This must be null when adding to a master and not null when retrieved from a master.
   * @return the property, not null
   */
  public final Property<UniqueId> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the bundle of external identifiers that define the legal entity.
   * This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  public ExternalIdBundle getExternalIdBundle() {
    return _externalIdBundle;
  }

  /**
   * Sets the bundle of external identifiers that define the legal entity.
   * This field must not be null for the object to be valid.
   * @param externalIdBundle  the new value of the property, not null
   */
  public void setExternalIdBundle(ExternalIdBundle externalIdBundle) {
    JodaBeanUtils.notNull(externalIdBundle, "externalIdBundle");
    this._externalIdBundle = externalIdBundle;
  }

  /**
   * Gets the the {@code externalIdBundle} property.
   * This field must not be null for the object to be valid.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> externalIdBundle() {
    return metaBean().externalIdBundle().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the map of attributes, which can be used for attaching additional application-level information.
   * @return the value of the property, not null
   */
  public Map<String, String> getAttributes() {
    return _attributes;
  }

  /**
   * Sets the map of attributes, which can be used for attaching additional application-level information.
   * @param attributes  the new value of the property, not null
   */
  public void setAttributes(Map<String, String> attributes) {
    JodaBeanUtils.notNull(attributes, "attributes");
    this._attributes.clear();
    this._attributes.putAll(attributes);
  }

  /**
   * Gets the the {@code attributes} property.
   * @return the property, not null
   */
  public final Property<Map<String, String>> attributes() {
    return metaBean().attributes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the name of the legal entity.
   * This field must not be null for the object to be valid.
   * @return the value of the property, not null
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the name of the legal entity.
   * This field must not be null for the object to be valid.
   * @param name  the new value of the property, not null
   */
  public void setName(String name) {
    JodaBeanUtils.notNull(name, "name");
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * This field must not be null for the object to be valid.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the ratings.
   * @return the value of the property, not null
   */
  public Collection<Rating> getRatings() {
    return _ratings;
  }

  /**
   * Sets the ratings.
   * @param ratings  the new value of the property, not null
   */
  public void setRatings(Collection<Rating> ratings) {
    JodaBeanUtils.notNull(ratings, "ratings");
    this._ratings = ratings;
  }

  /**
   * Gets the the {@code ratings} property.
   * @return the property, not null
   */
  public final Property<Collection<Rating>> ratings() {
    return metaBean().ratings().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the capabilities.
   * @return the value of the property, not null
   */
  public Collection<Capability> getCapabilities() {
    return _capabilities;
  }

  /**
   * Sets the capabilities.
   * @param capabilities  the new value of the property, not null
   */
  public void setCapabilities(Collection<Capability> capabilities) {
    JodaBeanUtils.notNull(capabilities, "capabilities");
    this._capabilities = capabilities;
  }

  /**
   * Gets the the {@code capabilities} property.
   * @return the property, not null
   */
  public final Property<Collection<Capability>> capabilities() {
    return metaBean().capabilities().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the issuedSecurities.
   * @return the value of the property, not null
   */
  public Collection<Object> getIssuedSecurities() {
    return _issuedSecurities;
  }

  /**
   * Sets the issuedSecurities.
   * @param issuedSecurities  the new value of the property, not null
   */
  public void setIssuedSecurities(Collection<Object> issuedSecurities) {
    JodaBeanUtils.notNull(issuedSecurities, "issuedSecurities");
    this._issuedSecurities = issuedSecurities;
  }

  /**
   * Gets the the {@code issuedSecurities} property.
   * @return the property, not null
   */
  public final Property<Collection<Object>> issuedSecurities() {
    return metaBean().issuedSecurities().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the obligations.
   * @return the value of the property, not null
   */
  public Collection<Obligation> getObligations() {
    return _obligations;
  }

  /**
   * Sets the obligations.
   * @param obligations  the new value of the property, not null
   */
  public void setObligations(Collection<Obligation> obligations) {
    JodaBeanUtils.notNull(obligations, "obligations");
    this._obligations = obligations;
  }

  /**
   * Gets the the {@code obligations} property.
   * @return the property, not null
   */
  public final Property<Collection<Obligation>> obligations() {
    return metaBean().obligations().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the sccounts.
   * @return the value of the property, not null
   */
  public Collection<Account> getSccounts() {
    return _sccounts;
  }

  /**
   * Sets the sccounts.
   * @param sccounts  the new value of the property, not null
   */
  public void setSccounts(Collection<Account> sccounts) {
    JodaBeanUtils.notNull(sccounts, "sccounts");
    this._sccounts = sccounts;
  }

  /**
   * Gets the the {@code sccounts} property.
   * @return the property, not null
   */
  public final Property<Collection<Account>> sccounts() {
    return metaBean().sccounts().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the portfolios.
   * @return the value of the property, not null
   */
  public Collection<Object> getPortfolios() {
    return _portfolios;
  }

  /**
   * Sets the portfolios.
   * @param portfolios  the new value of the property, not null
   */
  public void setPortfolios(Collection<Object> portfolios) {
    JodaBeanUtils.notNull(portfolios, "portfolios");
    this._portfolios = portfolios;
  }

  /**
   * Gets the the {@code portfolios} property.
   * @return the property, not null
   */
  public final Property<Collection<Object>> portfolios() {
    return metaBean().portfolios().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the accounts.
   * @return the value of the property, not null
   */
  public Collection<Account> getAccounts() {
    return _accounts;
  }

  /**
   * Sets the accounts.
   * @param accounts  the new value of the property, not null
   */
  public void setAccounts(Collection<Account> accounts) {
    JodaBeanUtils.notNull(accounts, "accounts");
    this._accounts = accounts;
  }

  /**
   * Gets the the {@code accounts} property.
   * @return the property, not null
   */
  public final Property<Collection<Account>> accounts() {
    return metaBean().accounts().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public ManageableLegalEntity clone() {
    BeanBuilder<? extends ManageableLegalEntity> builder = metaBean().builder();
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
      ManageableLegalEntity other = (ManageableLegalEntity) obj;
      return JodaBeanUtils.equal(getUniqueId(), other.getUniqueId()) &&
          JodaBeanUtils.equal(getExternalIdBundle(), other.getExternalIdBundle()) &&
          JodaBeanUtils.equal(getAttributes(), other.getAttributes()) &&
          JodaBeanUtils.equal(getName(), other.getName()) &&
          JodaBeanUtils.equal(getRatings(), other.getRatings()) &&
          JodaBeanUtils.equal(getCapabilities(), other.getCapabilities()) &&
          JodaBeanUtils.equal(getIssuedSecurities(), other.getIssuedSecurities()) &&
          JodaBeanUtils.equal(getObligations(), other.getObligations()) &&
          JodaBeanUtils.equal(getSccounts(), other.getSccounts()) &&
          JodaBeanUtils.equal(getPortfolios(), other.getPortfolios()) &&
          JodaBeanUtils.equal(getAccounts(), other.getAccounts());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getUniqueId());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExternalIdBundle());
    hash += hash * 31 + JodaBeanUtils.hashCode(getAttributes());
    hash += hash * 31 + JodaBeanUtils.hashCode(getName());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRatings());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCapabilities());
    hash += hash * 31 + JodaBeanUtils.hashCode(getIssuedSecurities());
    hash += hash * 31 + JodaBeanUtils.hashCode(getObligations());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSccounts());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPortfolios());
    hash += hash * 31 + JodaBeanUtils.hashCode(getAccounts());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(384);
    buf.append("ManageableLegalEntity{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("uniqueId").append('=').append(JodaBeanUtils.toString(getUniqueId())).append(',').append(' ');
    buf.append("externalIdBundle").append('=').append(JodaBeanUtils.toString(getExternalIdBundle())).append(',').append(' ');
    buf.append("attributes").append('=').append(JodaBeanUtils.toString(getAttributes())).append(',').append(' ');
    buf.append("name").append('=').append(JodaBeanUtils.toString(getName())).append(',').append(' ');
    buf.append("ratings").append('=').append(JodaBeanUtils.toString(getRatings())).append(',').append(' ');
    buf.append("capabilities").append('=').append(JodaBeanUtils.toString(getCapabilities())).append(',').append(' ');
    buf.append("issuedSecurities").append('=').append(JodaBeanUtils.toString(getIssuedSecurities())).append(',').append(' ');
    buf.append("obligations").append('=').append(JodaBeanUtils.toString(getObligations())).append(',').append(' ');
    buf.append("sccounts").append('=').append(JodaBeanUtils.toString(getSccounts())).append(',').append(' ');
    buf.append("portfolios").append('=').append(JodaBeanUtils.toString(getPortfolios())).append(',').append(' ');
    buf.append("accounts").append('=').append(JodaBeanUtils.toString(getAccounts())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ManageableLegalEntity}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueId> _uniqueId = DirectMetaProperty.ofReadWrite(
        this, "uniqueId", ManageableLegalEntity.class, UniqueId.class);
    /**
     * The meta-property for the {@code externalIdBundle} property.
     */
    private final MetaProperty<ExternalIdBundle> _externalIdBundle = DirectMetaProperty.ofReadWrite(
        this, "externalIdBundle", ManageableLegalEntity.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code attributes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<String, String>> _attributes = DirectMetaProperty.ofReadWrite(
        this, "attributes", ManageableLegalEntity.class, (Class) Map.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(
        this, "name", ManageableLegalEntity.class, String.class);
    /**
     * The meta-property for the {@code ratings} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Rating>> _ratings = DirectMetaProperty.ofReadWrite(
        this, "ratings", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code capabilities} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Capability>> _capabilities = DirectMetaProperty.ofReadWrite(
        this, "capabilities", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code issuedSecurities} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Object>> _issuedSecurities = DirectMetaProperty.ofReadWrite(
        this, "issuedSecurities", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code obligations} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Obligation>> _obligations = DirectMetaProperty.ofReadWrite(
        this, "obligations", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code sccounts} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Account>> _sccounts = DirectMetaProperty.ofReadWrite(
        this, "sccounts", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code portfolios} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Object>> _portfolios = DirectMetaProperty.ofReadWrite(
        this, "portfolios", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-property for the {@code accounts} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Collection<Account>> _accounts = DirectMetaProperty.ofReadWrite(
        this, "accounts", ManageableLegalEntity.class, (Class) Collection.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "uniqueId",
        "externalIdBundle",
        "attributes",
        "name",
        "ratings",
        "capabilities",
        "issuedSecurities",
        "obligations",
        "sccounts",
        "portfolios",
        "accounts");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return _uniqueId;
        case -736922008:  // externalIdBundle
          return _externalIdBundle;
        case 405645655:  // attributes
          return _attributes;
        case 3373707:  // name
          return _name;
        case 983597686:  // ratings
          return _ratings;
        case -1487597642:  // capabilities
          return _capabilities;
        case 1643621609:  // issuedSecurities
          return _issuedSecurities;
        case 809305781:  // obligations
          return _obligations;
        case -831331436:  // sccounts
          return _sccounts;
        case 415474731:  // portfolios
          return _portfolios;
        case -2137146394:  // accounts
          return _accounts;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ManageableLegalEntity> builder() {
      return new DirectBeanBuilder<ManageableLegalEntity>(new ManageableLegalEntity());
    }

    @Override
    public Class<? extends ManageableLegalEntity> beanType() {
      return ManageableLegalEntity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueId> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code externalIdBundle} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> externalIdBundle() {
      return _externalIdBundle;
    }

    /**
     * The meta-property for the {@code attributes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Map<String, String>> attributes() {
      return _attributes;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
    }

    /**
     * The meta-property for the {@code ratings} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Rating>> ratings() {
      return _ratings;
    }

    /**
     * The meta-property for the {@code capabilities} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Capability>> capabilities() {
      return _capabilities;
    }

    /**
     * The meta-property for the {@code issuedSecurities} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Object>> issuedSecurities() {
      return _issuedSecurities;
    }

    /**
     * The meta-property for the {@code obligations} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Obligation>> obligations() {
      return _obligations;
    }

    /**
     * The meta-property for the {@code sccounts} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Account>> sccounts() {
      return _sccounts;
    }

    /**
     * The meta-property for the {@code portfolios} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Object>> portfolios() {
      return _portfolios;
    }

    /**
     * The meta-property for the {@code accounts} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Collection<Account>> accounts() {
      return _accounts;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          return ((ManageableLegalEntity) bean).getUniqueId();
        case -736922008:  // externalIdBundle
          return ((ManageableLegalEntity) bean).getExternalIdBundle();
        case 405645655:  // attributes
          return ((ManageableLegalEntity) bean).getAttributes();
        case 3373707:  // name
          return ((ManageableLegalEntity) bean).getName();
        case 983597686:  // ratings
          return ((ManageableLegalEntity) bean).getRatings();
        case -1487597642:  // capabilities
          return ((ManageableLegalEntity) bean).getCapabilities();
        case 1643621609:  // issuedSecurities
          return ((ManageableLegalEntity) bean).getIssuedSecurities();
        case 809305781:  // obligations
          return ((ManageableLegalEntity) bean).getObligations();
        case -831331436:  // sccounts
          return ((ManageableLegalEntity) bean).getSccounts();
        case 415474731:  // portfolios
          return ((ManageableLegalEntity) bean).getPortfolios();
        case -2137146394:  // accounts
          return ((ManageableLegalEntity) bean).getAccounts();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -294460212:  // uniqueId
          ((ManageableLegalEntity) bean).setUniqueId((UniqueId) newValue);
          return;
        case -736922008:  // externalIdBundle
          ((ManageableLegalEntity) bean).setExternalIdBundle((ExternalIdBundle) newValue);
          return;
        case 405645655:  // attributes
          ((ManageableLegalEntity) bean).setAttributes((Map<String, String>) newValue);
          return;
        case 3373707:  // name
          ((ManageableLegalEntity) bean).setName((String) newValue);
          return;
        case 983597686:  // ratings
          ((ManageableLegalEntity) bean).setRatings((Collection<Rating>) newValue);
          return;
        case -1487597642:  // capabilities
          ((ManageableLegalEntity) bean).setCapabilities((Collection<Capability>) newValue);
          return;
        case 1643621609:  // issuedSecurities
          ((ManageableLegalEntity) bean).setIssuedSecurities((Collection<Object>) newValue);
          return;
        case 809305781:  // obligations
          ((ManageableLegalEntity) bean).setObligations((Collection<Obligation>) newValue);
          return;
        case -831331436:  // sccounts
          ((ManageableLegalEntity) bean).setSccounts((Collection<Account>) newValue);
          return;
        case 415474731:  // portfolios
          ((ManageableLegalEntity) bean).setPortfolios((Collection<Object>) newValue);
          return;
        case -2137146394:  // accounts
          ((ManageableLegalEntity) bean).setAccounts((Collection<Account>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._externalIdBundle, "externalIdBundle");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._attributes, "attributes");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._name, "name");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._ratings, "ratings");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._capabilities, "capabilities");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._issuedSecurities, "issuedSecurities");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._obligations, "obligations");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._sccounts, "sccounts");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._portfolios, "portfolios");
      JodaBeanUtils.notNull(((ManageableLegalEntity) bean)._accounts, "accounts");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
