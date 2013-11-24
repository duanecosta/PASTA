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

    identity.setUserIdentifier(userIdJack);
    String userId = identity.getUserIdentifier();
    String message = "Expected user identifier '" + userIdJack +
                     "', but received '" + userId + "'!";
    assertTrue(message, userIdJack.equals(userId));

  }

  /**
   * Test to ensure that a new provider identifier is correctly set.
   */
  @Test
  public void testSetProviderIdentifier() {

    identity.setProviderIdentifier(providerIdLTERX);
    Integer providerId = identity.getProviderIdentifier();
    String message = "Expected provider identifier '" + providerIdLTERX +
                         "', but received '" + providerId + "'!";
    assertTrue(message, providerIdLTERX.equals(providerId));

  }

  /**
   * Test to ensure that a new profile identifier is correctly set.
   */
  @Test
  public void testSetProfileIdentifier() {

    identity.setProfileIdentifier(profileIdJack);
    Integer profileId = identity.getProfileIdentifier();
    String message = "Expected provider identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!";
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

    String header = "************** testInitIdentityKnown **************";
    System.out.printf("\n%s\n", header);

    identity.initIdentity(userIdCarroll, providerIdLTER);

    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();
    System.out.printf("%s\n", identity.toString());

    String message = "Identity object initialization failed for user '" +
                     IdentityTest.userIdCarroll + "' with provider identifier '" +
                     IdentityTest.providerIdLTER + "'!";

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
   * Test to see if a the <em>Identity</em> object is correctly initialized
   * with an unknown user identifier and unknown provider identifier.
   *
   * @throws Exception
   */
  @Test
  public void testInitIdentityUnknown() throws Exception {

    String header = "************** testInitIdentityUnknown **************";
    System.out.printf("\n%s\n", header);

    identity.initIdentity(userIdUnknown, providerIdUnknown);

    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();
    System.out.printf("%s\n", identity.toString());


    String message = "Identity object initialization failed for user '" +
                     userIdUnknown + "' with provider identifier '" +
                     providerIdUnknown + "'!";
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

    String header = "************** testInitIdentityUnknownUserId **************";
    System.out.printf("\n%s\n", header);

    identity.initIdentity(userIdUnknown, providerIdLTER);

    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();
    System.out.printf("%s\n", identity.toString());


    String message = "Identity object initialization failed for user '" +
                     userIdJack + "' with provider identifier '" +
                     providerIdUnknown + "'!";
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

    String header = "************** testInitIdentityUnknownProviderId **************";
    System.out.printf("\n%s\n", header);

    identity.initIdentity(userIdCarroll, providerIdUnknown);

    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();
    System.out.printf("%s\n", identity.toString());

    String message = "Identity object initialization failed for user '" +
                     userIdCarroll + "' with provider identifier '" +
                     providerIdUnknown + "'!";
    assertNull(message, profileId);
    assertNull(message, verifyTimestamp);

  }

  /**
   * Test to see if the <em>Identity</em> object is correctly inserted into the
   * Identity database
   *
   * @throws Exception
   */
  @Test
  public void testInsertIdentity() throws Exception {

    String header = "************** testInsertIdentity **************";
    System.out.printf("\n%s\n", header);

    identity.initIdentity(userIdJack, providerIdLTERX);
    identity.setProfileIdentifier(profileIdJack);
    identity.setVerifyTimestamp(verifyTimestampJack);
    identity.insertIdentity();
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());

    Integer profileId = identity.getProfileIdentifier();
    if (profileId != null) {
      String message = "Expected profile identifier '" + profileIdJack +
                "', but received '" + profileId + "'!";
      assertTrue(message, profileIdJack.equals(profileId));
    } else {
      fail("Profile identifier returned is 'NULL'");
    }

    Date verifyTimestamp = identity.getVerifyTimestamp();
    if (verifyTimestamp != null) {
      String message = "Expected verify timestamp '" + verifyTimestampJack +
                    "', but received '" + verifyTimestamp + "'!";
      assertTrue(message, verifyTimestampJack.equals(verifyTimestamp));
    } else {
      fail("Verify timestamp returned is 'NULL'");
    }

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

    /*
     * Load new Identity record for Cactus Jack with profile for Utah Carroll
     * and verify timestamp about the same time as the release date for the
     * Rollings Stones - Let it Bleed album
     */
    Date verifyTimestamp = new Date(0l);
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdCarroll,
                                   verifyTimestamp);
      System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
      System.out.printf("%s\n", identity.toString());
    identity.setProfileIdentifier(profileIdJack);
    Date now = new Date();
    identity.setVerifyTimestamp(now);
    identity.updateIdentity();
    identity.initIdentity(userIdJack, providerIdLTERX);
      System.out.printf("%s\n", identity.toString());
    verifyTimestamp = identity.getVerifyTimestamp();
    Integer profileId = identity.getProfileIdentifier();

    String message = "Expected profile identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!";
    assertTrue(message, profileIdJack.equals(profileId));

    message = "Expected verify timestamp '" + now.toString() +
                     "', but received '" + verifyTimestamp + "'!";
    assertTrue(message, now.equals(verifyTimestamp));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new profile identifier and verify timestamp.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateIdentityParam() throws Exception {

    String header = "************** testUpdateIdentityParam **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack with profile for Utah Carroll
     * and verify timestamp about the same time as the release date for the
     * Rollings Stones - Let it Bleed album
     */
    Date verifyTimestamp = new Date(0l);
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdCarroll, verifyTimestamp);
    System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    Date now = new Date();
    identity.updateIdentity(profileIdJack, now);
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    verifyTimestamp = identity.getVerifyTimestamp();
    Integer profileId = identity.getProfileIdentifier();

    String message = "Expected profile identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!";
    assertTrue(message, profileIdJack.equals(profileId));

    message = "Expected verify timestamp '" + now.toString() +
                         "', but received '" + verifyTimestamp + "'!";
    assertTrue(message, now.equals(verifyTimestamp));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new profile identifier.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateProfileId() throws Exception {

    String header = "************** testUpdateProfileId **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack with profile for Utah Carroll
     */
    Date now = new Date();
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdCarroll, now);
    System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    identity.setProfileIdentifier(profileIdJack);
    identity.updateProfileId();
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    Integer profileId = identity.getProfileIdentifier();

    String message = "Expected profile identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!";
    assertTrue(message, profileIdJack.equals(profileId));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new profile identifier.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateProfileIdParam() throws Exception {

    String header = "************** testUpdateProfileIdParam **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack with profile for Utah Carroll
     */
    Date now = new Date();
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdCarroll, now);
    System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    identity.updateProfileId(profileIdJack);
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    Integer profileId = identity.getProfileIdentifier();

    String message = "Expected profile identifier '" + profileIdJack +
                         "', but received '" + profileId + "'!";
    assertTrue(message, profileIdJack.equals(profileId));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new verify timestamp.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateVerifyTimestamp() throws Exception {

    String header = "************** testUpdateVerifyTimestamp **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack with verify timestamp about the
     * same time as the release date for the Rollings Stones - Let it Bleed
     * album
     */
    Date verifyTimestamp = new Date(0l);
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdJack,
                                   verifyTimestamp);
    System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    Date now = new Date();
    identity.setVerifyTimestamp(now);
    identity.updateIdentity();
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    verifyTimestamp = identity.getVerifyTimestamp();

    String message = "Expected verify timestamp '" + now.toString() +
                         "', but received '" + verifyTimestamp + "'!";
    assertTrue(message, now.equals(verifyTimestamp));

  }

  /**
   * Test to see if the <em>Identity</em> object database record is correctly
   * updated with a new verify timestamp.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateVerifyTimestampParam() throws Exception {

    String header = "************** testUpdateVerifyTimestampParam **************";
    System.out.printf("\n%s\n", header);

    /*
     * Load new Identity record for Cactus Jack with verify timestamp about the
     * same time as the release date for the Rollings Stones - Let it Bleed
     * album
     */
    Date verifyTimestamp = new Date(0l);
    IdentityTest.insertIdentity(userIdJack, providerIdLTERX, profileIdJack, verifyTimestamp);
    System.out.printf("%s\n", identity.toString());

    // Initialize incorrect Identity object
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    Date now = new Date();
    identity.updateVerifyTimestamp(now);
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    verifyTimestamp = identity.getVerifyTimestamp();

    String message = "Expected verify timestamp '" + now.toString() +
                  "', but received '" + verifyTimestamp + "'!";
    assertTrue(message, now.equals(verifyTimestamp));

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

    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());
    identity.deleteIdentity();
    identity.initIdentity(userIdJack, providerIdLTERX);
    System.out.printf("%s\n", identity.toString());

    Integer profileId = identity.getProfileIdentifier();
    Date verifyTimestamp = identity.getVerifyTimestamp();

    String message = "Expected NULL values for both the profile identifier " +
                     "and verify timestamp, but received '" + profileId +
                     "' and '" + verifyTimestamp + "'!";
    assertNull(message, profileId);
    assertNull(message, verifyTimestamp);

  }

  /*
   * Inserts a test Identity object record into the Identity database for the
   * given user identifier, provider identifier, profile identifier, and verify
   * timestamp.
   */
  private static void insertIdentity(String userId, Integer providerId,
      Integer profileId, Date verifyTimestamp) throws Exception {

    Timestamp timestamp = new Timestamp(verifyTimestamp.getTime());

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.identity ");
    strBuilder.append("(user_id,provider_id,profile_id,verify_timestamp) ");
    strBuilder.append("VALUES ('");
    strBuilder.append(userId);
    strBuilder.append("',");
    strBuilder.append(providerId);
    strBuilder.append(",");
    strBuilder.append(profileId);
    strBuilder.append(",'");
    strBuilder.append(timestamp);
    strBuilder.append("');");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("insertIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "insertIdentity: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("insertIdentity: " + e);
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
  private static void purgeIdentity(String userId, Integer providerId)
      throws Exception {

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