/*
 * Copyright 2011-2013 the University of New Mexico.
 *
 * This work was supported by National Science Foundation Cooperative
 * Agreements #DEB-0832652 and #DEB-0936498.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package edu.lternet.pasta.identitymanager;

import org.junit.*;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * User: servilla
 * Date: 11/18/13
 * Time: 10:34 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identity
 * <p/>
 * <class description>
 */
public class IdentityTest {

  /* Instance variables */

  Identity identity;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(edu.lternet.pasta.identitymanager.IdentityTest.class);

  private static String userIdUnknown = "unknown";
  private static Integer profileIdUnknown = 0;
  private static Date verifyTimestampUnknown = new Date(0L);

  private static String userIdCarroll = "uid=ucarroll,org=LTER,dc=ecoinformatics,dc=org";
  private static Integer profileIdCarroll = 1;
  private static Date verifyTimestampCarroll = new Date(1384893613000L);

  private static String userIdJack = "uid=cjack,org=LTER,dc=ecoinformatics,dc=org";
  private static Integer profileIdJack = 2;
  private static Date verifyTimestampJack = new Date(1384917912619L);

  private static Integer providerIdUnknown = 0;
  private static Integer providerIdLTER = 1;
  private static Integer providerIdLTERX = 2;

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    identity = new Identity();
    Identity.setDatabase("junit");

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    try {
      identity.initIdentity(IdentityTest.userIdJack, IdentityTest.providerIdLTERX);
      identity.deleteIdentity();
    }
    catch (Exception e) {
      // Squashing exception message
    }
    identity = null;

  }

  /**
   * Test to ensure that a new user identifier is correctly set.
   */
  @Test
  public void testSetUserIdentifier() {

    String batman = new String("batman");
    String userId;

    identity.setUserIdentifier(batman);
    userId = identity.getUserIdentifier();
    String message = "Expected user identifier '" + batman +
                     "', but received '" + userId + "'!";
    assertTrue(message, batman.equals(userId));

  }

  /**
   * Test to ensure that a new provider identifier is correctly set.
   */
  @Test
  public void testSetProviderIdentifier() {

    Integer batId = 1024;
    Integer providerId;

    identity.setProviderIdentifier(batId);
    providerId = identity.getProviderIdentifier();
    String message = "Expected provider identifier '" + batId +
                         "', but received '" + providerId + "'!";
    assertTrue(message, batId.equals(providerId));

  }

  /**
   * Test to ensure that a new profile identifier is correctly set.
   */
  @Test
  public void testSetProfileIdentifier() {

    Integer batId = 1024;
    Integer profileId;

    identity.setProfileIdentifier(batId);
    profileId = identity.getProfileIdentifier();
    String message = "Expected provider identifier '" + batId +
                         "', but received '" + profileId + "'!";
    assertTrue(message, batId.equals(profileId));

  }

  /**
   * Test to ensure that a new verify timestamp is correctly set.
   */
  @Test
  public void testSetVerifyTimestamp() {

    Date now = new Date();
    Date verifyTimestamp;

    identity.setVerifyTimestamp(now);
    verifyTimestamp = identity.getVerifyTimestamp();
    String message = "Expected verify timestamp '" + now +
                         "', but received '" + verifyTimestamp + "'!";
    assertTrue(message, now.equals(verifyTimestamp));

  }

  /**
   * Test to see if a the <em>Identity</em> object is correctly initialized
   * with a known user identifier and provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testInitIdentityKnown() throws Exception {

    identity.initIdentity(IdentityTest.userIdCarroll, IdentityTest.providerIdLTER);

    String userId = identity.getUserIdentifier();
    Integer providerId = identity.getProviderIdentifier();
    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();

    if (userId != null && profileId != null && profileId != null && verifyTimestamp != null) {
      System.out.printf("%s - %d - %d - %d (%s)", userId, providerId, profileId,
                           verifyTimestamp.getTime(), verifyTimestamp.toString());
    }


    String message = "Identity object initialization failed for user '" +
                         IdentityTest.userIdCarroll + "' with provider identifier '" +
                         IdentityTest.providerIdLTER + "'!";

    if (profileId != null) {
      assertTrue(message, IdentityTest.profileIdCarroll.equals(profileId));
    } else {
      fail(message);
    }

    if (verifyTimestamp != null) {
      assertTrue(message, IdentityTest.verifyTimestampCarroll.equals(verifyTimestamp));
    } else {
      fail(message);
    }

  }

  /**
   * Test to see if a the <em>Identity</em> object is correctly initialized
   * with an unknown user identifier and unknown provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testInitIdentityUnknown() throws Exception {

    identity.initIdentity(IdentityTest.userIdUnknown, IdentityTest.providerIdUnknown);

    String userId = identity.getUserIdentifier();
    Integer providerId = identity.getProviderIdentifier();
    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();

    if (userId != null && profileId != null && profileId != null && verifyTimestamp != null) {
      System.out.printf("%s - %d - %d - %d (%s)", userId, providerId, profileId,
                           verifyTimestamp.getTime(), verifyTimestamp.toString());
    }


    String message = "Identity object initialization failed for user '" +
                         IdentityTest.userIdUnknown + "' with provider identifier '" +
                         IdentityTest.providerIdUnknown + "'!";
    assertNull(message, profileId);
    assertNull(message, verifyTimestamp);

  }

  /**
   * Test to see if a the <em>Identity</em> object is correctly initialized
   * with an unknown user identifier and known provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testInitIdentityUnknownUserId() throws Exception {

    identity.initIdentity(IdentityTest.userIdUnknown, IdentityTest.providerIdLTER);

    String userId = identity.getUserIdentifier();
    Integer providerId = identity.getProviderIdentifier();
    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();

    if (userId != null && profileId != null && profileId != null && verifyTimestamp != null) {
      System.out.printf("%s - %d - %d - %d (%s)", userId, providerId, profileId,
                           verifyTimestamp.getTime(), verifyTimestamp.toString());
    }


    String message = "Identity object initialization failed for user '" +
                     IdentityTest.userIdJack + "' with provider identifier '" +
                     IdentityTest.providerIdUnknown + "'!";
    assertNull(message, profileId);
    assertNull(message, verifyTimestamp);

  }

  /**
   * Test to see if a the <em>Identity</em> object is correctly initialized
   * with a known user identifier and an unknown provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testInitIdentityUnknownProviderId() throws Exception {

    identity.initIdentity(IdentityTest.userIdCarroll, IdentityTest.providerIdUnknown);

    String userId = identity.getUserIdentifier();
    Integer providerId = identity.getProviderIdentifier();
    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();

    if (userId != null && profileId != null && profileId != null && verifyTimestamp != null) {
      System.out.printf("%s - %d - %d - %d (%s)", userId, providerId, profileId,
                           verifyTimestamp.getTime(), verifyTimestamp.toString());
    }


    String message = "Identity object initialization failed for user '" +
                         IdentityTest.userIdCarroll + "' with provider identifier '" +
                         IdentityTest.providerIdUnknown + "'!";
    assertNull(message, profileId);
    assertNull(message, verifyTimestamp);

  }

  @Test
  public void testInsertIdentity() throws Exception {

    String message = null;

    identity.initIdentity(userIdJack, providerIdLTERX);
    identity.setProfileIdentifier(profileIdJack);
    identity.setVerifyTimestamp(verifyTimestampJack);
    identity.insertIdentity();

    identity.initIdentity(userIdJack, providerIdLTERX);

    Integer profileId = identity.getProfileIdentifier();
    if (profileId != null) {
      message = "Expected profile identifier '" + profileIdJack +
                "', but received '" + profileId + "'!";
      assertTrue(message, profileIdJack.equals(profileId));
    } else {
      fail("Profile identifier returned is 'NULL'");
    }

    Date verifyTimestamp = identity.getVerifyTimestamp();
    if (verifyTimestamp != null) {
      message = "Expected verify timestamp '" + verifyTimestampJack +
                    "', but received '" + verifyTimestamp + "'!";
      assertTrue(message, verifyTimestampJack.equals(verifyTimestamp));
    } else {
      fail("Verify timestamp returned is 'NULL'");
    }

  }

 /* Class methods */

  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {


  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {


  }

}
