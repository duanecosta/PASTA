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
import java.util.ArrayList;

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
  private boolean active;
  private Timestamp createTimestamp;
  private Timestamp updateTimestamp;
  private String surName;
  private String givenName;
  private String nickName;
  private String institution;
  private String email;
  private String intent;
  private ArrayList<Identity> identities;
  private ArrayList<Group> groups;

  /* Class variables */

  private static final Logger logger = Logger.getLogger(Provider.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  /* Constructors */

  public Profile() throws PastaConfigurationException {

    loadConfiguration();

  }

  public Profile(Integer profileId) throws PastaConfigurationException {

    loadConfiguration();



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
   * @return Create timestamp of the Profoile
   */
  public Timestamp getCreateTimestamp() {
    return createTimestamp;
  }

  /**
   * Set the create timestamp of the Profile.
   *
   * @param createTimestamp
   */
  public void setCreateTimestamp(Timestamp createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  /**
   * Gets the update timestamp of the Profile.
   *
   * @return Update timestamp of the Profile.
   */
  public Timestamp getUpdateTimestamp() {
    return updateTimestamp;
  }

  /**
   * Sets the update timestamp of the Profile.
   *
   * @param updateTimestamp
   */
  public void setUpdateTimestamp(Timestamp updateTimestamp) {
    this.updateTimestamp = updateTimestamp;
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
   * Gets the list of user groups associated with the Profile.
   *
   * @return List of user groups
   */
  public ArrayList<Group> getGroups() {
    return groups;
  }

  /**
   * Sets the list of user groups associated with the Profile.
   *
   * @param groups
   */
  public void setGroups(ArrayList<Group> groups) {
    this.groups = groups;
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

  /*
   * Sets the database URL to a new connection string (intended use is for unit
   * testing).
   */
  protected static void setDatabase(String name) {
    dbURL = "jdbc:postgresql://localhost/" + name;
  }

}
