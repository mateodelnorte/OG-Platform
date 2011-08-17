/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.core.security.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Collections;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.TimeZone;

import org.testng.annotations.Test;

import com.opengamma.DataNotFoundException;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecurityLink;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;

/**
 * Tests {@link DefaultSecurityResolver}.
 */
public class DefaultSecurityResolverTest {
  
  private final ObjectId _objectId;
  private final ExternalId _securityExternalId;
  private final ExternalIdBundle _intersectingExternalIdBundle;
  private final Security _securityV1;
  private final Security _securityV2;
  
  private final Instant _securityV2ValidFrom;
  private final Instant _now = Instant.now();
  
  private final SecuritySource _securitySource;
  
  public DefaultSecurityResolverTest() {
    _securityExternalId = ExternalId.of("Scheme1", "Value1");
    ExternalIdBundle externalIdBundle = ExternalIdBundle.of(_securityExternalId, ExternalId.of("Scheme2", "Value2"));
    _intersectingExternalIdBundle = ExternalIdBundle.of(_securityExternalId, ExternalId.of("Scheme3", "Value3"));
    
    _objectId = ObjectId.of("Sec", "a");
    
    _securityV1 = new SimpleSecurity(_objectId.atVersion("1"), externalIdBundle, "Type", "Security V1");
    _securityV2 = new SimpleSecurity(_objectId.atVersion("2"), externalIdBundle, "Type", "Security V2");
    
    _securityV2ValidFrom = LocalDate.of(2011, 01, 01).atStartOfDayInZone(TimeZone.UTC).toInstant();
    
    _securitySource = mock(SecuritySource.class);
    
    // By unique ID
    when(_securitySource.getSecurity(_securityV1.getUniqueId())).thenReturn(_securityV1);
    when(_securitySource.getSecurity(_securityV2.getUniqueId())).thenReturn(_securityV2);
    
    // By object ID and version-correction
    when(_securitySource.getSecurity(_objectId, VersionCorrection.of(_securityV2ValidFrom.minusMillis(1), _now))).thenReturn(_securityV1);
    when(_securitySource.getSecurity(_objectId, VersionCorrection.of(_securityV2ValidFrom, _now))).thenReturn(_securityV2);
    
    // By external ID bundle and version-correction
    when(_securitySource.getSecurities(ExternalIdBundle.of(_securityExternalId), VersionCorrection.of(_securityV2ValidFrom, _now))).thenReturn(Collections.singleton(_securityV2));
    when(_securitySource.getSecurities(_intersectingExternalIdBundle, VersionCorrection.of(_securityV2ValidFrom, _now))).thenReturn(Collections.singleton(_securityV2));
    when(_securitySource.getSecurities(_intersectingExternalIdBundle, VersionCorrection.of(_securityV2ValidFrom.minusMillis(1), _now))).thenReturn(Collections.singleton(_securityV1));
  }
  
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidVersionCorrectionLatestVersion() {
    new DefaultSecurityResolver(_securitySource, VersionCorrection.of(null, Instant.now()));
  }
  
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidVersionCorrectionLatestCorrection() {
    new DefaultSecurityResolver(_securitySource, VersionCorrection.of(Instant.now(), null));
  }
  
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidVersionCorrectionLatest() {
    new DefaultSecurityResolver(_securitySource, VersionCorrection.LATEST);
  }
  
  @Test
  public void testResolveLinkWithObjectId() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    SecurityLink link = new SimpleSecurityLink(_objectId);
    Security resolvedSecurity = resolver.resolve(link);
    assertEquals(_securityV2, resolvedSecurity);
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testResolveLinkWithUnknownObjectId() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(Instant.ofEpochMillis(123), Instant.ofEpochMillis(123)));
    SecurityLink link = new SimpleSecurityLink(ObjectId.of("Unknown", "Unknown"));
    resolver.resolve(link);
  }
  
  @Test
  public void testResolveLinkWithExternalIdBundle() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    
    SecurityLink link = new SimpleSecurityLink(_securityExternalId);
    Security resolvedSecurity = resolver.resolve(link);
    assertEquals(_securityV2, resolvedSecurity);
    
    link = new SimpleSecurityLink(ExternalIdBundle.of(_intersectingExternalIdBundle));
    resolvedSecurity = resolver.resolve(link);
    assertEquals(_securityV2, resolvedSecurity);
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testResolveLinkWithUnknownExternalIdBundle() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(Instant.ofEpochMillis(123), Instant.ofEpochMillis(123)));
    SecurityLink link = new SimpleSecurityLink(ExternalId.of("Unknown", "Unknown"));
    resolver.resolve(link);
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testResolveEmptyLink() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(Instant.ofEpochMillis(123), Instant.ofEpochMillis(123)));
    SecurityLink link = new SimpleSecurityLink();
    resolver.resolve(link);    
  }
  
  @Test
  public void testGetSecurityByUniqueId() {   
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom.minusMillis(1), _now));
    Security security = resolver.getSecurity(_securityV1.getUniqueId());
    assertEquals(_securityV1, security);
    
    // Should still return security even if not valid for the version-correction of resolver
    resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    security = resolver.getSecurity(_securityV1.getUniqueId());
    assertEquals(_securityV1, security);
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetSecurityByUnknownUniqueId() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    resolver.getSecurity(UniqueId.of("Unknown", "Unknown"));
  }
  
  @Test
  public void testGetSecurityByObjectId() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom.minusMillis(1), _now));
    Security security = resolver.getSecurity(_objectId);
    assertEquals(_securityV1, security);
    
    resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    security = resolver.getSecurity(_objectId);
    assertEquals(_securityV2, security);
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetSecurityByUnknownObjectId() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    resolver.getSecurity(ObjectId.of("Unknown", "Unknown"));
  }
  
  @Test
  public void testGetSecurityByExternalIdBundle() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom.minusMillis(1), _now));
    Security security = resolver.getSecurity(_intersectingExternalIdBundle);
    assertEquals(_securityV1, security);
    
    resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    security = resolver.getSecurity(_intersectingExternalIdBundle);
    assertEquals(_securityV2, security);    
  }
  
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetSecurityByUnknownExternalIdBundle() {
    DefaultSecurityResolver resolver = new DefaultSecurityResolver(_securitySource, VersionCorrection.of(_securityV2ValidFrom, _now));
    resolver.getSecurity(ExternalIdBundle.of(ExternalId.of("Unknown", "Unknown")));
  }
  
}
