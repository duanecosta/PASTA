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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;


/**
 * User: servilla
 * Date: 11/18/13
 * Time: 10:35 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identity
 * <p/>
 *
 * Manages Identity objects for users who have been successfully identified
 * through an Identity Provider.
 */
public class Identity {

  /* Instance variables */

  private String userId = null;
  private Integer providerId = null;
  private Integer profileId = null;
  private Timestamp verifyTimestamp = null;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(edu.lternet.pasta.identitymanager.Identity.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  /* Constructors */

  /**
   * Create a new Identity.
   *
   * @throws PastaConfigurationException
   */
  public Identity() throws PastaConfigurationException {

    loadConfiguration();

  }

  /**
   * Create a new Identity from the Identity record in the Identity database
   * based on the user identifier and provider identifier, which together are
   * the primary keys.
   *
   * @param userId User identifier
   * @param providerId Provider identifier
   * @throws PastaConfigurationException
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws IdentityDoesNotExistException
   */
  public Identity(String userId, Integer providerId)
      throws PastaConfigurationException, ClassNotFoundException, SQLException,
                 IdentityDoesNotExistException {

    this.userId = userId;
    this.providerId = providerId;

    loadConfiguration();

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.profile_id, ");
    strBuilder.append("identity.identity.verify_timestamp FROM ");
    strBuilder.append("identity.identity WHERE ");
    strBuilder.append("identity.identity.user_id='");
    strBuilder.append(this.userId);
    strBuilder.append("' AND identity.identity.provider_id=");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("Identity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
         this.profileId = rs.getInt("profile_id");
        this.verifyTimestamp = rs.getTimestamp("verify_timestamp");
      } else {
        String gripe = "Identity does not exist for userId '" + userId +
                       "' and providerId '" + providerId + "'!\n";
        throw new IdentityDoesNotExistException(gripe);
      }
    }
    catch (SQLException e) {
      logger.error("Identity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /* Instance methods */

  /**
   * Returns the user identifier of the <em>Identity</em> object.
   *
   * @return The user identifier
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Sets the user identifier of the <em>Identity</em> object.
   *
   * @param userId The user identifier
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * Returns the provider identifier of the <em>Identity</em> object.
   *
   * @return The provider identifier
   */
  public Integer getProviderId() {
    return providerId;
  }

  /**
   * Sets the provider identifier of the <em>Identity</em> object.
   *
   * @param providerId The provider identifier
   */
  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * Returns the profile identifier of the <em>Identity</em> object.
   *
   * @return The profile identifier
   */
  public Integer getProfileId() {
    return profileId;
  }

  /**
   * Sets the profile identifier of the <em>Identity</em> object.
   *
   * @param profileId The profile identifier
   */
  public void setProfileId(Integer profileId) {
    this.profileId = profileId;
  }

  /**
   * Returns the last verify timestamp of the <em>Identity</em> object if
   * it exists or null.
   *
   * @return The verify timestamp
   */
  public Date getVerifyTimestamp() {

    Date date = null;

    if (this.verifyTimestamp != null) date =
        new Date(this.verifyTimestamp.getTime());

    return date;
  }

  /**
   * Sets the verify timestamp of the <em>Identity</em> object.
   *
   * @param verifyTimestamp The verify timestamp
   */
  public void setVerifyTimestamp(Date verifyTimestamp) {

    if (verifyTimestamp != null)
        this.verifyTimestamp = new Timestamp(verifyTimestamp.getTime());

  }

  /**
   * Loads the Identity from the Identity record in the Identity database
   * based on the user identifier and provider identifier, which together are
   * the primary keys.
   *
   * @param userId The user identifier
   * @param providerId The provider identifier
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public void getIdentity(String userId, int providerId)
      throws SQLException, ClassNotFoundException,
                 IdentityDoesNotExistException {

    this.userId = userId;
    this.providerId = providerId;
    this.profileId = null;
    this.verifyTimestamp = null;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.profile_id,");
    strBuilder.append("identity.identity.verify_timestamp FROM ");
    strBuilder.append("identity.identity WHERE identity.identity.user_id='");
    strBuilder.append(this.userId);
    strBuilder.append("' AND identity.identity.provider_id=");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

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
        this.profileId = rs.getInt("profile_id");
        this.verifyTimestamp = rs.getTimestamp("verify_timestamp");
      }
      else {
        String gripe = String.format("Identity record does not exist for user" +
                                         " identity '%s' and provider " +
                                         "identity '%s'!", userId, providerId);
        throw new IdentityDoesNotExistException(gripe);
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

  }

  /**
   * Saves the Identity to the Identity database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void saveIdentity() throws ClassNotFoundException, SQLException {

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
    strBuilder.append(verifyTimestamp);
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

  /**
   * Updates only the profile identifier and verify timestamp of the Identity
   * to Identity database.
   *
   * Note that the user identifier and provider identifier should be considered
   * immutable, and should not be updated.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void updateIdentity() throws ClassNotFoundException, SQLException {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("UPDATE identity.identity SET ");
    strBuilder.append("verify_timestamp='");
    strBuilder.append(this.verifyTimestamp);
    strBuilder.append("', profile_id=");
    strBuilder.append(this.profileId);
    strBuilder.append(" WHERE identity.identity.user_id='");
    strBuilder.append(this.userId);
    strBuilder.append("' AND identity.identity.provider_id=");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("updateIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "updateIdentity: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("updateIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /**
   * Deletes the Identity from the Identity database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void deleteIdentity() throws ClassNotFoundException, SQLException {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("DELETE FROM identity.identity ");
    strBuilder.append("WHERE identity.identity.user_id='");
    strBuilder.append(this.userId);
    strBuilder.append("' AND identity.identity.provider_id=");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("deleteIdentity: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "deleteIdentity: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("deleteIdentity: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    this.userId = null;
    this.providerId = null;
    this.profileId = null;
    this.verifyTimestamp = null;

  }

  /**
   * Returns a formatted string representation of the Identity.
   *
   * @return
   */
  @Override
  public String toString() {

    String userId = "NULL";
    String providerId = "NULL";
    String profileId = "NULL";
    String verifyTimestamp = "NULL";

    if (this.userId != null) userId = this.userId;
    if (this.providerId != null) providerId = this.providerId.toString();
    if (this.profileId != null) profileId = this.profileId.toString();
    if (this.verifyTimestamp != null) verifyTimestamp =
                                          this.verifyTimestamp.toString();

    return String.format("%s, %s, %s, %s", userId, profileId, profileId,
                            verifyTimestamp);

  }

  /*
   * Load local properties from identity.properties
   */
  private void loadConfiguration() throws PastaConfigurationException {

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

  /*
   * Returns a connection to the database.
   */
  private Connection getConnection() throws ClassNotFoundException {

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

  /* Class methods */

}