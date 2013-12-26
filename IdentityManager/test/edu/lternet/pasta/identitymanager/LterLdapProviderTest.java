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

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: servilla
 * Date: 11/24/13
 * Time: 3:32 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class LterLdapProviderTest {

  /* Instance variables */

  LterLdapProvider provider;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(LterLdapProviderTest.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  private static Integer providerIdLTER = 1;
  private static String providerNameLTER = "LTER";
  private static String providerConnectionLTER = "ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks";
  private static String contactNameLTER = "System Administrator";
  private static String contactPhoneLTER = "505-277-2551";
  private static String contactEmailLTER = "tech-support@lternet.edu";

  private static String userIdJack;
  private static String passwordJack;
  private static Integer profileIdJack = 2;
  private static Date verifyTimestampJack = new Date(1384917912619L);

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    provider = new LterLdapProvider();

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    LterLdapProviderTest.purgeIdentity(userIdJack, providerIdLTER);
    provider = null;

  }

  /**
   * Test to ensure that a new provider name is correctly set.
   */
  @Test
  public void testSetProviderName() {

    provider.setProviderName(providerNameLTER);
    String providerName = provider.getProviderName();
    String message = "Expected provider name '" + providerNameLTER +
                     "', but received '" + providerName + "'!\n";
    assertTrue(message, providerNameLTER.equals(providerName));

  }

  /**
   * Test to ensure that a new provider connection is correctly set.
   */
  @Test
  public void testSetProviderConnection() {

    provider.setProviderConnection(providerConnectionLTER);
    String providerConnection = provider.getProviderConnection();
    String message = "Expected provider connection '" + providerConnectionLTER +
                     "', but received '" + providerConnection + "'!\n";
    assertTrue(message, providerConnectionLTER.equals(providerConnection));

  }

  /**
   * Test to ensure that a new provider connection is correctly set.
   */
  @Test
  public void testSetContactName() {

    provider.setContactName(contactNameLTER);
    String contactName = provider.getContactName();
    String message = "Expected contact name '" + contactNameLTER +
                         "', but received '" + contactName + "'!\n";
    assertTrue(message, contactNameLTER.equals(contactName));

  }

  /**
   * Test to ensure that a new provider connection is correctly set.
   */
  @Test
  public void testSetContactPhone() {

    provider.setContactPhone(contactPhoneLTER);
    String contactPhone = provider.getContactPhone();
    String message = "Expected contact phone '" + contactPhoneLTER +
                         "', but received '" + contactPhone + "'!\n";
    assertTrue(message, contactPhoneLTER.equals(contactPhone));

  }

  /**
   * Test to ensure that a new provider connection is correctly set.
   */
  @Test
  public void testSetContactEmail() {

    provider.setContactEmail(contactEmailLTER);
    String contactEmail = provider.getContactEmail();
    String message = "Expected contact email '" + contactEmailLTER +
                         "', but received '" + contactEmail + "'!\n";
    assertTrue(message, contactEmailLTER.equals(contactEmail));

  }

  /**
   * Test to ensure a new Provider is saved to the Provider database.
   *
   * @throws Exception
   */
  @Test
  public void testSaveProvider() throws Exception {

    String providerName = "A";
    String providerConnection = "B";
    String contactName = "C";
    String contactPhone = "D";
    String contactEmail = "E";

    provider.setProviderName(providerName);
    provider.setProviderConnection(providerConnection);
    provider.setContactName(contactName);
    provider.setContactPhone(contactPhone);
    provider.setContactEmail(contactEmail);

    provider.saveProvider();

    /*
    Integer providerId = LterLdapProviderTest.getProviderId(providerName,
      providerConnection, contactName, contactPhone, contactEmail);
    */

    Integer providerId = provider.getProviderId();

    String message = "Expected a provider identifier value, but received null!";
    assertNotNull(message, providerId);

    if (providerId != null) LterLdapProviderTest.purgeProvider(providerId);

  }

  @Test
  public void testUpdateProvider() throws Exception {

    String providerName = "A";
    String providerConnection = "ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks";
    String contactName = "C";
    String contactPhone = "D";
    String contactEmail = "E";

    LterLdapProviderTest.insertProvider(providerName, providerConnection,
      contactName, contactPhone, contactEmail);

    Integer providerId = LterLdapProviderTest.getProviderId(providerName,
      providerConnection, contactName, contactPhone, contactEmail);

    LterLdapProvider provider = new LterLdapProvider(providerId);

    providerName = "1";
    providerConnection = "ldap.lternet.edu:389:/WebRoot/WEB-INF/conf/lternet.jks";
    contactName = "3";
    contactPhone = "4";
    contactEmail = "5";

    provider.setProviderName(providerName);
    provider.setProviderConnection(providerConnection);
    provider.setContactName(contactName);
    provider.setContactPhone(contactPhone);
    provider.setContactEmail(contactEmail);

    provider.updateProvider();

    String testProviderName = LterLdapProviderTest.getProviderValue(providerId, "providerName");
    String message = String.format("Expected provider name '%s', but received '%s'!", providerName, testProviderName);
    assertTrue(message, providerName.equals(testProviderName));

  }

  @Test
  public void testDeleteProvider() {

    assertTrue(true);

  }
  /**
   * Test to ensure that user 'Cactus Jack' is in identity list for the
   * provider LTER.
   * 
   * @throws Exception
   */
  @Test
  public void testGetIdentities() throws Exception {

    LterLdapProviderTest.insertIdentity(userIdJack, providerIdLTER,
                                           profileIdJack, verifyTimestampJack);

    provider.setProviderId(providerIdLTER);
    ArrayList<Identity> identityList = provider.getIdentities();
    String message = "Expected identityList to contain identities but " +
                     "received null!\n";
    assertFalse(message,  identityList == null);

    boolean hasUserIdJack = false;
    for (Identity i : identityList) {
      if (i.getUserId().equals(userIdJack)) hasUserIdJack = true;
    }
    message = "Expected identityList to contain identity for 'Cactus Jack', " +
              "but 'Cactus Jack' not in list!\n";
    assertTrue(message, hasUserIdJack);

  }

  /**
   * Test to ensure that the identity for 'Cactus Jack' can validate
   * successfully.
   */
  @Test
  public void testValidateUser() throws Exception {

    LterLdapProvider provider = new LterLdapProvider(providerIdLTER);

    Credential credential = new Credential();
    credential.setUser(userIdJack);
    credential.setPassword(passwordJack);

    assertTrue(provider.validateUser(credential));

  }

  /* Class methods */

  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

    LterLdapProviderTest.loadConfiguration();

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  /*
   * Inserts a test Provider into the Provider database.
   */
  private static void insertProvider(String providerName,
    String providerConnection, String contactName, String contactPhone,
    String contactEmail) throws Exception {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.provider ");
    strBuilder.append("(provider_name,provider_conn,contact_name,");
    strBuilder.append("contact_phone,contact_email) VALUES (");
    strBuilder.append("'");
    strBuilder.append(providerName);
    strBuilder.append("','");
    strBuilder.append(providerConnection);
    strBuilder.append("','");
    strBuilder.append(contactName);
    strBuilder.append("','");
    strBuilder.append(contactPhone);
    strBuilder.append("','");
    strBuilder.append(contactEmail);
    strBuilder.append("');");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("insertProvider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "insertProvider: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("insertProvider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /*
   * Returns the provider identifier of the Provider database record that
   * matches the provider name, provider connection, contact name, contact
   * phone, and contact email.
   */
  private static Integer getProviderId(String providerName,
    String providerConnection, String contactName, String contactPhone,
    String contactEmail) throws Exception {

    Integer providerId = null;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.provider.provider_id FROM ");
    strBuilder.append("identity.provider WHERE provider_name='");
    strBuilder.append(providerName);
    strBuilder.append("' AND provider_conn='");
    strBuilder.append(providerConnection);
    strBuilder.append("' AND contact_name='");
    strBuilder.append(contactName);
    strBuilder.append("' AND contact_phone='");
    strBuilder.append(contactPhone);
    strBuilder.append("' AND contact_email='");
    strBuilder.append(contactEmail);
    strBuilder.append("';");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("getProviderId: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        providerId = rs.getInt("provider_id");
       }
      else {
        String gripe = String.format("Provider with name '%s' does not exist!\n", providerName);
        throw new ProviderDoesNotExistException(gripe);
      }
    }
    catch (SQLException e) {
      logger.error("Provider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    return providerId;

  }

  /*
   * Return the Provider field value for the Provider identified by the
   * provider identifier in the Provider database.
   */
  private static String getProviderValue(Integer providerId, String field)
      throws Exception {

    String providerName;
    String providerConnection;
    String contactName;
    String contactPhone;
    String contactEmail;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.provider.provider_name,");
    strBuilder.append("identity.provider.provider_conn,");
    strBuilder.append("identity.provider.contact_name,");
    strBuilder.append("identity.provider.contact_phone,");
    strBuilder.append("identity.provider.contact_email FROM ");
    strBuilder.append("identity.provider WHERE provider_id=");
    strBuilder.append(providerId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("getProviderId: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        providerName = rs.getString("provider_name");
        providerConnection = rs.getString("provider_conn");
        contactName = rs.getString("contact_name");
        contactPhone = rs.getString("contact_phone");
        contactEmail = rs.getString("contact_email");
      }
      else {
        String gripe = String.format("Provider with provider identifier '%d' " +
                                         "does not exist!\n", providerId);
        throw new ProviderDoesNotExistException(gripe);
      }
    }
    catch (SQLException e) {
      logger.error("Provider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    if (field.equals("providerName")) {
      return providerName;
    } else if (field.equals("providerConnection")) {
      return providerConnection;
    } else if (field.equals("contactName")) {
      return contactName;
    } else if (field.equals("contactPhone")) {
      return contactPhone;
    } else if (field.equals("contactEmail")) {
      return contactEmail;
    } else {
      return null;
    }

  }

  /*
   * Purges the Provider identified by the provider identifier from the Provider
   * database.
   */
  private static void purgeProvider(Integer providerId) throws Exception {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("DELETE FROM identity.provider WHERE provider_id=");
    strBuilder.append(providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("purgeProvider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "purgeProvider: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("purgeProvider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

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
        userIdJack = options.getString("junit.cjack.dn");
        passwordJack = options.getString("junit.cjack.password");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new PastaConfigurationException(e.getMessage());
      }
    }

  }

}
