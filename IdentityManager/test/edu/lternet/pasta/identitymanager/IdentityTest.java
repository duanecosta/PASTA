/*
 * Copyright 2011-2014 the University of New Mexico.
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

  private static String userIdCarroll = "uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org";
  private static Integer profileIdCarroll = 1;
  private static Date verifyTimestampCarroll = new Date(1384893613000L);

  private static String userIdJack = "uid=cjack,o=LTER,dc=ecoinformatics,dc=org";
  private static Integer profileIdJack = 2;
  private static Date verifyTimestampJack = new Date(1384917912619L);

  private static String providerIdLTER = "https://pasta.lternet.edu/authentication";
  private static String providerIdLTERX = "https://lternet.edu";

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    identity = new Identity();

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
   * Test to ensure that Identity loading from Identity database is successful.
   *
   * @throws Exception
   */
  @Test
  public void testIdentityLoad() throws Exception {

    Identity identity = new Identity(userIdCarroll, providerIdLTER);

    String userId = identity.getUserId();
    String message = String.format("Expected userId '%s', but received " +
                                       "'%s'!", userIdCarroll, userId);
    assertTrue(message, userIdCarroll.equals(userId));

    String proivderId = identity.getProviderId();
    message = String.format("Expected providerId '%s', but received '%s'!",
                               providerIdLTER, proivderId);
    assertEquals(message, providerIdLTER, proivderId);

    Integer profileId = identity.getProfileId();
    message = String.format("Expected profileId '%d', but received '%d'!",
                               profileIdCarroll, profileId);
    assertEquals(message, profileIdCarroll, profileId);

    Date verifyTimestamp = identity.getVerifyTimestamp();
    message = String.format("Expected verfityTimestamp '%s', " +
      "but received '%s'!", verifyTimestampCarroll.toString(),
      verifyTimestamp.toString());
      assertTrue(message,
      verifyTimestampCarroll.toString().equals(verifyTimestamp.toString()));

  }

  /**
   * Test to ensure that a new user identifier is correctly set.
   */
  @Test
  public void testSetUserIdentifier() {

    identity.setUserId(userIdJack);
    String userId = identity.getUserId();
    String message = "Expected user identifier '" + userIdJack +
                     "', but received '" + userId + "'!\n";
    assertTrue(message, userIdJack.equals(userId));

  }

  /**
   * Test to ensure that a new provider identifier is correctly set.
   */
  @Test
  public void testSetProviderIdentifier() {

    identity.setProviderId(providerIdLTERX);
    String providerId = identity.getProviderId();
    String message = "Expected provider identifier '" + providerIdLTERX +
                         "', but received '" + providerId + "'!\n";
    assertTrue(message, providerIdLTERX.equals(providerId));

  }

  /**
   * Test to ensure that a new profile identifier is correctly set.
   */
  @Test
  public void testSetProfileIdentifier() {

    identity.setProfileId(profileIdJack);
    Integer profileId = identity.getProfileId();
    String message = "Expected provider identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!\n";
    assertTrue(message, profileIdJack.equals(profileId));

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
                         "', but received '" + verifyTimestamp + "'!\n";
    assertTrue(message, now.equals(verifyTimestamp));

  }

  /**
   * Test to see if the Identity is correctly initialized from the Identity
   * database based on the user identifier and provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testGetIdentity() throws Exception {

    String header = "************** testGetIdentity **************";
    System.out.printf("\n%s\n", header);

    identity.getIdentity(userIdCarroll, providerIdLTER);

    Integer profileId = identity.getProfileId();
    Date verifyTimestamp = identity.getVerifyTimestamp();
    System.out.printf("%s\n", identity.toString());

    String message = "Identity object initialization failed for user '" +
                     IdentityTest.userIdCarroll + "' with provider identifier '" +
                     IdentityTest.providerIdLTER + "'!\n";

    if (profileId != null) {
      assertTrue(message, profileIdCarroll.equals(profileId));
    } else {
      fail(message);
    }

    if (verifyTimestamp != null) {
      assertTrue(message, verifyTimestampCarroll.equals(verifyTimestamp));
    } else {
      fail(message);
    }

  }

  /**
   * Test to see if the <em>Identity</em> object is correctly inserted into the
   * Identity database
   *
   * @throws Exception
   */
  @Test
  public void testSaveIdentity() throws Exception {

    String header = "************** testSaveIdentity **************";
    System.out.printf("\n%s\n", header);

    identity.setUserId(userIdJack);
    identity.setProviderId(providerIdLTERX);
    identity.setProfileId(profileIdJack);
    identity.setVerifyTimestamp(verifyTimestampJack);

    identity.saveIdentity();

    System.out.printf("%s\n", identity.toString());

    // Test for Identity database record existence
    String message = String.format("Expected the Identity with user " +
                                       "identifier '%s' and provider " +
                                       "identifier '%s' to exist, " +
                                       "but it does not exist!", userIdJack,
                                      providerIdLTERX);
    assertTrue(message, IdentityTest.identityExists(userIdJack,
                                                       providerIdLTERX));

    // Test Identity database record field profileId
    Integer profileId = identity.getProfileId();
    message = String.format("Expected Identity profileId '%d', " +
                                "but received '%d'!", profileIdJack, profileId);
    assertEquals(message, profileIdJack, profileId);

    // Test Identity database record field verifyTimestamp
    Date verifyTimestamp = identity.getVerifyTimestamp();
    message = String.format("Expected Identity verifyTimestamp '%s', " +
                                "but received '%s'!",
                               verifyTimestampJack.toString(),
                               verifyTimestamp.toString());
    assertTrue(message, verifyTimestampJack.toString().equals(verifyTimestamp
                                                                  .toString()));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new profile identifier and verify timestamp.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateIdentity() throws Exception {

    String header = "************** testUpdateIdentity **************";
    System.out.printf("\n%s\n", header);

    // Insert wrong profileId and verifyTimestamp
    Date verifyTimestamp = new Date(0l);
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, null,
                                   verifyTimestamp);

    System.out.printf("%s\n", identity.toString());

    identity.setUserId(userIdJack);
    identity.setProviderId(providerIdLTERX);
    identity.setProfileId(profileIdJack);
    identity.setVerifyTimestamp(verifyTimestampJack);

    identity.updateIdentity();

    System.out.printf("%s\n", identity.toString());

    // Test Identity database record field profileId
    Integer profileId = identity.getProfileId();
    String message = String.format("Expected Identity profileId '%d', " +
                                "but received '%d'!", profileIdJack, profileId);
    assertEquals(message, profileIdJack, profileId);

    // Test Identity database record field verifyTimestamp
    verifyTimestamp = identity.getVerifyTimestamp();
    message = String.format("Expected Identity verifyTimestamp '%s', " +
                                "but received '%s'!",
                               verifyTimestampJack.toString(),
                               verifyTimestamp.toString());
    assertTrue(message, verifyTimestampJack.toString().equals(verifyTimestamp
                                                                  .toString()));


  }


  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * removed from the Identity database.
   *
   * @throws Exception
   */
  @Test
  public void testDeleteIdentity() throws Exception {

    String header = "************** testDeleteIdentity **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack
     */
    Date now = new Date();
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdJack, now);

    identity.getIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    identity.deleteIdentity();

    String message = String.format("Expected that the Identity for the user " +
                                       "identifier '%s' and provider " +
                                       "identifier '%s' did not exist, " +
                                       "but it does exist!", userIdJack,
                                      providerIdLTERX);
    assertFalse(message, IdentityTest.identityExists(userIdJack, providerIdLTERX));

  }

  /* Class methods */

  /*
   * Inserts a test Identity object record into the Identity database for the
   * given user identifier, provider identifier, profile identifier, and verify
   * timestamp.
   */
  private static void insertIdentity(String userId, String providerId,
      Integer profileId, Date verifyTimestamp) throws Exception {

    Timestamp timestamp = new Timestamp(verifyTimestamp.getTime());

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.identity ");
    strBuilder.append("(user_id,provider_id,profile_id,verify_timestamp) ");
    strBuilder.append("VALUES ('");
    strBuilder.append(userId);
    strBuilder.append("','");
    strBuilder.append(providerId);
    strBuilder.append("',");

    if (profileId == null) {
      strBuilder.append("NULL");
    } else {
      strBuilder.append(profileId.toString());
    }

    strBuilder.append(",'");
    strBuilder.append(timestamp);
    strBuilder.append("');");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("saveIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "saveIdentity: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("saveIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /*
   * Removes the Identity record from the Identity database if the record exists
   * for the given user identifier and provider identifier.
   */
  private static void purgeIdentity(String userId, String providerId)
      throws Exception {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.profile_id,");
    strBuilder.append("identity.identity.verify_timestamp FROM ");
    strBuilder.append("identity.identity WHERE identity.identity.user_id='");
    strBuilder.append(userId);
    strBuilder.append("' AND identity.identity.provider_id='");
    strBuilder.append(providerId);
    strBuilder.append("';");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("purgeIdentity: " + e);
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
        strBuilder.append("' AND identity.identity.provider_id='");
        strBuilder.append(providerId);
        strBuilder.append("';");


        sql = strBuilder.toString();

        stmt = dbConn.createStatement();

        if (stmt.executeUpdate(sql) == 0) {
          String gripe = "deleteIdentity: '" + sql + "' failed";
          throw new SQLException(gripe);
        }

      }
    }
    catch (SQLException e) {
      logger.error("purgeIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /*
   * Tests whether an Identity based on the user identifier and provider
   * identifier exists in the Identity database.
   */
  private static boolean identityExists(String userId, String providerId)
      throws Exception {

    boolean identityExists = false;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT * FROM ");
    strBuilder.append("identity.identity WHERE identity.identity.user_id='");
    strBuilder.append(userId);
    strBuilder.append("' AND identity.identity.provider_id='");
    strBuilder.append(providerId);
    strBuilder.append("';");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("getIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        identityExists = true;
      }
    }
    catch (SQLException e) {
      logger.error("getIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    return identityExists;

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
        dbURL = options.getString("db.URL");
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