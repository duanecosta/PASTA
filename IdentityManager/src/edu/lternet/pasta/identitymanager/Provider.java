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
 *
 * An abstract class that describes and manages an Identity Provider.
 *
 */
public abstract class Provider {

  /* Instance variables */

  protected Integer providerId;
  protected String providerName;
  protected String providerConnection;
  protected String contactName;
  protected String contactPhone;
  protected String contactEmail;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(Provider.class);

  protected static String dbDriver;   // database driver
  protected static String dbURL;      // database URL
  protected static String dbUser;     // database user name
  protected static String dbPassword; // database user password

  protected static String cwd;

  /* Constructors */

  /**
   * Creates a new Provider.
   *
   * @throws PastaConfigurationException
   */
  public Provider() throws PastaConfigurationException {
    loadConfiguration();
  }

  /**
   * Creates a new Provider from the Provider record in the Provider database
   * based on the provided identifier.
   *
   * @param providerId The Provider identifier
   * @throws PastaConfigurationException
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws ProviderDoesNotExistException
   */
  public Provider(Integer providerId) throws PastaConfigurationException,
                                                 ClassNotFoundException,
                                                 SQLException,
                                                 ProviderDoesNotExistException {
    this.providerId = providerId;

    loadConfiguration();

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.provider.provider_name,");
    strBuilder.append("identity.provider.provider_conn,");
    strBuilder.append("identity.provider.contact_name,");
    strBuilder.append("identity.provider.contact_phone,");
    strBuilder.append("identity.provider.contact_email FROM ");
    strBuilder.append("identity.provider WHERE provider_id=");
    strBuilder.append(providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("Provider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        this.providerName = rs.getString("provider_name");
        this.providerConnection = rs.getString("provider_conn");
        this.contactName = rs.getString("contact_name");
        this.contactPhone = rs.getString("contact_phone");
        this.contactEmail = rs.getString("contact_email");
      }
      else {
        String gripe = String.format("Provider with identifier '%d' does not exist!\n", providerId);
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

  }

  /* Instance methods */

  /**
   * Sets the provider identifier of the Provider.
   *
   * @param providerId The provider identifier
   */
  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * Gets the provider identifier of the Provider.
   *
   * @return The provider identifier
   */
  public Integer getProviderId() {
    return providerId;
  }

  /**
   * Sets the provider name of the Provider.
   *
   * @param providerName The provider name
   */
  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  /**
   * Gets the provider name of the Provider.
   *
   * @return The provider name
   */
  public String getProviderName() {
    return providerName;
  }

  /**
   * Sets the network-based provider connection information for the
   * Provider.
   *
   * @param providerConnection The provider connection information
   */
  public void setProviderConnection(String providerConnection) {
    this.providerConnection = providerConnection;
  }

  /**
   * Gets the network-based provider connection information for the
   * Provider.
   *
   * @return The provider connection information
   */
  public String getProviderConnection() {
    return providerConnection;
  }

  /**
   * Sets the contact name of the provider for the Provider.
   *
   * @param contactName The provider's contact name
   */
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * Gets the contact name of the provider for the Provider.
   *
   * @return The provider's contact name
   */
  public String getContactName() {
    return contactName;
  }

  /**
   * Sets the contact's phone number of the provider for the Provider.
   *
   * @param contactPhone The contact's phone number
   */
  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  /**
   * Gets the contact's phone number of the provider for the Provider.
   *
   * @return The contact's phone number
   */
  public String getContactPhone() {
    return contactPhone;
  }

  /**
   * Sets the contact's email address of the provider for the Provider.
   *
   * @param contactEmail The contact's email address
   */
  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  /**
   * Gets the contact's email address of the provider for the Provider.
   *
   * @return The contact's email address
   */
  public String getContactEmail() {
    return contactEmail;
  }

  /**
   * Gets the list of Identities associated with the Provider.
   *
   * @return List of Identities
   */
  public ArrayList<Identity> getIdentities() throws ClassNotFoundException,
      SQLException {

    ArrayList<Identity> identities = null;

    // Find all Identities in the Identity table that use this provider for
    // identity validation
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("SELECT identity.identity.user_id FROM ");
    strBuilder.append("identity.identity WHERE ");
    strBuilder.append("identity.identity.provider_id=");
    strBuilder.append(this.providerId);
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
        identities = new ArrayList<Identity>();
        try {
          Identity identity = new Identity(userId, this.providerId);
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
   * Save the Provider to the Provider database.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void saveProvider() throws ClassNotFoundException, SQLException {

    String contactName = "NULL";
    String contactPhone = "NULL";
    String contactEmail = "NULL";

    if (this.contactName != null) contactName = this.contactName;
    if (this.contactPhone != null) contactPhone = this.contactPhone;
    if (this.contactEmail != null) contactEmail = this.contactEmail;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.provider (provider_name,");
    strBuilder.append("provider_conn,contact_name,contact_phone,");
    strBuilder.append("contact_email) VALUES ('");
    strBuilder.append(this.providerName);
    strBuilder.append("','");
    strBuilder.append(this.providerConnection);
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
      logger.error("saveProvider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "saveProvider: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("saveProvider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }


  }

  /**
   * Update the Provider in the Provider database based on the provider
   * identifier.
   *
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public void updateProvider() throws ClassNotFoundException, SQLException {

    String contactName = "NULL";
    String contactPhone = "NULL";
    String contactEmail = "NULL";

    if (this.contactName != null) contactName = this.contactName;
    if (this.contactPhone != null) contactPhone = this.contactPhone;
    if (this.contactEmail != null) contactEmail = this.contactEmail;

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("UPDATE identity.provider SET ");
    strBuilder.append("provider_name='");
    strBuilder.append(this.providerName);
    strBuilder.append("', provider_conn=");
    strBuilder.append(this.providerConnection);
    strBuilder.append("', contact_name=");
    strBuilder.append(contactName);
    strBuilder.append("', contact_phone=");
    strBuilder.append(contactPhone);
    strBuilder.append("', contact_email=");
    strBuilder.append(contactEmail);
    strBuilder.append(" WHERE identity.provider.provider_id='");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("updateProvider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "updateProvider: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("updateProvider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

  }

  public void deleteProvider() throws ClassNotFoundException, SQLException {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("DELETE FROM identity.provider ");
    strBuilder.append("WHERE identity.identity.provider_id='");
    strBuilder.append(this.providerId.toString());
    strBuilder.append(";");

    String sql = strBuilder.toString();

    Connection dbConn;

    try {
      dbConn = getConnection();
    }
    catch (ClassNotFoundException e) {
      logger.error("deleteProvider: " + e);
      e.printStackTrace();
      throw e;
    }

    try {
      Statement stmt = dbConn.createStatement();

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "deleteProvider: '" + sql + "' failed";
        throw new SQLException(gripe);
      }

    }
    catch (SQLException e) {
      logger.error("deleteProvider: " + e);
      logger.error(sql);
      e.printStackTrace();
      throw e;
    }
    finally {
      dbConn.close();
    }

    this.providerId = null;
    this.providerConnection = null;
    this.contactName = null;
    this.contactPhone = null;
    this.contactEmail = null;

  }

  /**
   * Validates the user's identity based on the provided credentials.
   *
   * @param credential
   * @return The validation state of the user's identity
   */
  public abstract boolean validateUser(Credential credential);

  /**
   * Returns a list of Groups that the user is affiliated with.
   *
   * @return List of Groups
   */
  public abstract ArrayList<Group> getGroups();

  /*
   * Load local properties from identity.properties
   */
  protected void loadConfiguration() throws PastaConfigurationException {

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
        cwd = options.getString("system.cwd");
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
  protected Connection getConnection() throws ClassNotFoundException {

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
      logger.error(String.format("getConnection (Database access failed): %s",
                                    e.getMessage()));
      e.printStackTrace();
    }

    return conn;

  }

  /* Class methods */

}
