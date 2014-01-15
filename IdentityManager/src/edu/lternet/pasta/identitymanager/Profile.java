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

import java.sql.*;
import java.util.*;
import java.util.Date;

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
public class Profile {

  /* Instance variables */

  private Integer profileId;
  private Boolean active;
  private Timestamp createTimestamp;
  private Timestamp updateTimestamp;
  private String surName;
  private String givenName;
  private String nickName;
  private String institution;
  private String email;
  private String intent;
  private ArrayList<Identity> identities;

  /* Class variables */

  private static final Logger logger = Logger.getLogger(Profile.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  /* Constructors */

  /**
   * Create a new Profile.
   *
   * @throws PastaConfigurationException
   */
  public Profile() throws PastaConfigurationException {

    loadConfiguration();

  }

  /**
   * Creates a new Profile from the Profile record in the Profile database based
   * on the profile identifier.
   *
   * @param profileId Profile identifier
   * @throws PastaConfigurationException
   * @throws ProfileDoesNotExistException
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public Profile(Integer profileId) throws PastaConfigurationException,
                                               ProfileDoesNotExistException,
                                               SQLException,
                                               ClassNotFoundException {

    this.profileId = profileId;

    loadConfiguration();

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.profile.active,");
    strBuilder.append("identity.profile.create_timestamp,");
    strBuilder.append("identity.profile.update_timestamp,");
    strBuilder.append("identity.profile.sur_name,");
    strBuilder.append("identity.profile.given_name,");
    strBuilder.append("identity.profile.nick_name,");
    strBuilder.append("identity.profile.institution,");
    strBuilder.append("identity.profile.email,");
    strBuilder.append("identity.profile.intent FROM identity.profile WHERE ");
    strBuilder.append("profile_id=");
    strBuilder.append(this.profileId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("Profile: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        active = rs.getBoolean("active");
        createTimestamp = rs.getTimestamp("create_timestamp");
        updateTimestamp = rs.getTimestamp("update_timestamp");
        surName = rs.getString("sur_name");
        givenName = rs.getString("given_name");
        nickName = rs.getString("nick_name");
        institution = rs.getString("institution");
        email = rs.getString("email");
        intent = rs.getString("intent");
      } else {
        String gripe = "Profile does not exist for profileId '" + profileId +
                        "'!\n";
        throw new ProfileDoesNotExistException(gripe);
      }
    }
    catch (SQLException e) {
      logger.error("Profile: " + e);
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
   * Gets the profile identifier of the Profile.
   *
   * @return Profile identifier
   */
  public Integer getProfileId() {
    return profileId;
  }

  /**
   * Sets the profile identifier of the Profile.
   *
   * @param profileId
   */
  public void setProfileId(Integer profileId) {
    this.profileId = profileId;
  }

  /**
   * Gets the active state of the Profile.
   *
   * @return Active state of the Profile
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the active state of the Profile.
   *
   * @param active
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Gets the create timestamp of the Profile.
   *
   * @return Create timestamp of the Profile
   */
  public Date getCreateTimestamp() {

    Date date = null;

    if (this.createTimestamp != null)
      date = new Date(this.createTimestamp.getTime());

    return date;

  }

  /**
   * Set the create timestamp of the Profile.
   *
   * @param date Profile create timestamp
   */
  public void setCreateTimestamp(Date date) {

    if (date != null)
      this.createTimestamp = new Timestamp(date.getTime());

  }

  /**
   * Gets the update timestamp of the Profile.
   *
   * @return Update timestamp of the Profile
   */
  public Date getUpdateTimestamp() {

    Date date = null;

    if (this.updateTimestamp != null)
      date = new Date(this.updateTimestamp.getTime());

    return date;

  }

  /**
   * Sets the update timestamp of the Profile.
   *
   * @param date Profile update timestamp
   */
  public void setUpdateTimestamp(Date date) {

    if (date != null)
      this.updateTimestamp = new Timestamp(date.getTime());

  }

  /**
   * Gets the user's surname of the Profile.
   *
   * @return User's surname
   */
  public String getSurName() {
    return surName;
  }

  /**
   * Sets the user's surname of the Profile.
   *
   * @param surName
   */
  public void setSurName(String surName) {
    this.surName = surName;
  }

  /**
   * Gets the user's given name of the Profile.
   *
   * @return User's given name
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Sets the user's given name of the Profile.
   *
   * @param givenName
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Gets the user's nick name of the Profile.
   *
   * @return User's nick name
   */
  public String getNickName() {
    return nickName;
  }

  /**
   * Sets the user's nick name of the Profile.
   *
   * @param nickName
   */
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  /**
   * Gets the user's institution name of the Profile.
   *
   * @return User's institution name
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Sets the user's institution name of the Profile.
   *
   * @param institution
   */
  public void setInstitution(String institution) {
    this.institution = institution;
  }

  /**
   * Gets the user's email of the Profile.
   *
   * @return User's email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the user's email of the Profile.
   *
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the data use intent of the Profile.
   *
   * @return Data use intent
   */
  public String getIntent() {
    return intent;
  }

  /**
   * Sets the data use intent of the Profile.
   *
   * @param intent
   */
  public void setIntent(String intent) {
    this.intent = intent;
  }

  /**
   * Gets the list of identities associated with the Profile.
   *
   * @return List of identities
   */
  public ArrayList<Identity> getIdentities() throws ClassNotFoundException,
                                                        SQLException {

    ArrayList<Identity> identities = null;

    // Find all Identities in the Identity table that are associated with this
    // Profile
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.user_id,");
    strBuilder.append("identity.identity.provider_id FROM ");
    strBuilder.append("identity.identity WHERE ");
    strBuilder.append("identity.identity.profile_id=");
    strBuilder.append(this.profileId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("getIdentities: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String userId = rs.getString("user_id");
        Integer providerId = rs.getInt("provider_id");
        identities = new ArrayList<Identity>();
        try {
          Identity identity = new Identity(userId, providerId);
          identities.add(identity);
        }
        catch (PastaConfigurationException e) {
          logger.error("getIdentities: " + e.getMessage());
          e.printStackTrace();
        }
        catch (IdentityDoesNotExistException e) {
          logger.error("getIdentities: " + e.getMessage());
          e.printStackTrace();
        }
      }
    }
    catch (SQLException e) {
      logger.error("getIdentities: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    return identities;

  }

  /**
   * Loads a Profile from the Profile record in the Profile database based
   * on the profile identifier.
   *
   * @param profileId Profile identifier
   * @throws PastaConfigurationException
   * @throws ProfileDoesNotExistException
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public void getProfile(Integer profileId) throws PastaConfigurationException,
                                                       ProfileDoesNotExistException,
                                                       SQLException,
                                                       ClassNotFoundException {

    this.profileId = profileId;

    loadConfiguration();

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.profile.active,");
    strBuilder.append("identity.profile.create_timestamp,");
    strBuilder.append("identity.profile.update_timestamp,");
    strBuilder.append("identity.profile.sur_name,");
    strBuilder.append("identity.profile.given_name,");
    strBuilder.append("identity.profile.nick_name,");
    strBuilder.append("identity.profile.institution,");
    strBuilder.append("identity.profile.email,");
    strBuilder.append("identity.profile.intent FROM identity.profile WHERE ");
    strBuilder.append("profile_id=");
    strBuilder.append(this.profileId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("getProfile: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        active = rs.getBoolean("active");
        createTimestamp = rs.getTimestamp("create_timestamp");
        updateTimestamp = rs.getTimestamp("update_timestamp");
        surName = rs.getString("sur_name");
        givenName = rs.getString("given_name");
        nickName = rs.getString("nick_name");
        institution = rs.getString("institution");
        email = rs.getString("email");
        intent = rs.getString("intent");
      } else {
        String gripe = "Profile does not exist for profileId '" + profileId +
                           "'!\n";
        throw new ProfileDoesNotExistException(gripe);
      }
    }
    catch (SQLException e) {
      logger.error("getProfile: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /**
   * Saves a Profile to the Profile database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void saveProfile() throws ClassNotFoundException, SQLException {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.profile ");
    strBuilder.append("(active,create_timestamp,update_timestamp,sur_name,");
    strBuilder.append("given_name,nick_name,institution,email,intent) ");
    strBuilder.append("VALUES (");

    if (active) {
      strBuilder.append("TRUE,'");
    }
    else {
      strBuilder.append("FALSE,'");
    }

    strBuilder.append(createTimestamp);
    strBuilder.append("','");
    strBuilder.append(updateTimestamp);
    strBuilder.append("','");
    strBuilder.append(surName);
    strBuilder.append("','");
    strBuilder.append(givenName);
    strBuilder.append("','");
    strBuilder.append(nickName);
    strBuilder.append("','");
    strBuilder.append(institution);
    strBuilder.append("','");
    strBuilder.append(email);
    strBuilder.append("','");
    strBuilder.append(intent);
    strBuilder.append("');");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("saveProfile: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql, stmt.RETURN_GENERATED_KEYS) == 0) {
        String gripe = "saveProfile: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        profileId = rs.getInt("profile_id");
      }
      else {
        String gripe = "saveProfile: setting profileId from getGeneratedKeys " +
                           "failed!";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("saveProfile: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /**
   * Updates a Profile to a Profile record in the Profile database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void updateProfile() throws ClassNotFoundException, SQLException,
                                         ProfileDoesNotExistException {

    if (profileId == null) {
      String gripe = "updateProfile: profile identifier is 'null'!";
      throw new ProfileDoesNotExistException(gripe);
    }

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("UPDATE identity.profile SET ");
    strBuilder.append("(active,create_timestamp,update_timestamp,sur_name,");
    strBuilder.append("given_name,nick_name,institution,email,intent) ");
    strBuilder.append("= (");

    if (active) {
      strBuilder.append("TRUE,'");
    }
    else {
      strBuilder.append("FALSE,'");
    }

    strBuilder.append(createTimestamp);
    strBuilder.append("','");
    strBuilder.append(updateTimestamp);
    strBuilder.append("','");
    strBuilder.append(surName);
    strBuilder.append("','");
    strBuilder.append(givenName);
    strBuilder.append("','");
    strBuilder.append(nickName);
    strBuilder.append("','");
    strBuilder.append(institution);
    strBuilder.append("','");
    strBuilder.append(email);
    strBuilder.append("','");
    strBuilder.append(intent);
    strBuilder.append("') WHERE profile_id=");
    strBuilder.append(profileId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("updateProfile: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "updateProfile: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("updateProfile: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  /**
   * Deletes a Profile from Profile database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void deleteProfile() throws ClassNotFoundException, SQLException,
                                         ProfileDoesNotExistException {

    if (profileId == null) {
      String gripe = "updateProfile: profile identifier is 'null'!";
      throw new ProfileDoesNotExistException(gripe);
    }

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("DELETE FROM identity.profile WHERE profile_id=");
    strBuilder.append(profileId);
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("deleteProfile: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "deleteProfile: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("deleteProfile: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    profileId = null;
    active = null;
    createTimestamp = null;
    updateTimestamp = null;
    surName = null;
    givenName = null;
    nickName = null;
    institution = null;
    email = null;
    intent = null;

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
