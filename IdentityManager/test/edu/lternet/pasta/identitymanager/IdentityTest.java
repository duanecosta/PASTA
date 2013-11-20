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

import java.sql.*;
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

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

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

    identity = null;
    IdentityTest.purgeIdentity(userIdJack, providerIdLTERX);

  }

  /**
   * Test to ensure that a new user identifier is correctly set.
   */
  @Test
  public void testSetUserIdentifier() {

    String batman = new String(userIdJack);
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

    Integer batId = providerIdLTERX;
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

    Integer batId = profileIdJack;
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

  private static void purgeIdentity(String userId, Integer providerId) throws Exception {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.profile_id,");
    strBuilder.append("identity.identity.verify_timestamp FROM ");
    strBuilder.append("identity.identity WHERE identity.identity.user_id='");
    strBuilder.append(userId);
    strBuilder.append("' AND identity.identity.provider_id=");
    strBuilder.append(Integer.toString(providerId));
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("initIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) { // Record exists

        strBuilder = new StringBuilder();
        strBuilder.append("DELETE FROM identity.identity ");
        strBuilder.append("WHERE identity.identity.user_id='");
        strBuilder.append(userId);
        strBuilder.append("' AND identity.identity.provider_id=");
        strBuilder.append(Integer.toString(providerId));
        strBuilder.append(";");


        sql = strBuilder.toString();

        stmt = dbConn.createStatement();

        if (stmt.executeUpdate(sql) == 0) {
          String gripe = "deleteIdentity: '" + sql + "' failed";
          throw new SQLException(gripe);
        }

      }
    }
    catch (SQLException e) {
      logger.error("initIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }


  /*
   * Returns a connection to the database.
   */
  private static Connection getConnection() throws ClassNotFoundException {

    Connection conn = null;
    SQLWarning warn;

    // Load the JDBC driver
    try {
      Class.forName(dbDriver);
    }
    catch (ClassNotFoundException e) {
      logger.error("getConnection: " + e.getMessage());
      throw e;
    }

    // Make the database connection
    try {
      conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

      // If a SQLWarning object is available, print its warning(s).
      // There may be multiple warnings chained.
      warn = conn.getWarnings();

      if (warn != null) {
        while (warn != null) {
          logger.warn("SQLState: " + warn.getSQLState());
          logger.warn("Message:  " + warn.getMessage());
          logger.warn("Vendor: " + warn.getErrorCode());
          warn = warn.getNextWarning();
        }
      }
    }
    catch (SQLException e) {
      logger.error("Database access failed: " + e);
    }

    return conn;

  }

  /*
 * Load local properties from identity.properties
 */
  private static void loadConfiguration() throws Exception {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new PastaConfigurationException(gripe);
    } else {
      try {
        dbDriver = options.getString("db.Driver");
        dbURL = options.getString("db.URL.junit");
        dbUser = options.getString("db.User");
        dbPassword = options.getString("db.Password");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new PastaConfigurationException(e.getMessage());
      }
    }

  }

 /* Class methods */

  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

    IdentityTest.loadConfiguration();

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {


  }

}
