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
import java.sql.SQLException;
import java.sql.SQLWarning;
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
 * Manages Identity Provider objects, which describe an Identity Provider,
 * including provider connection information and contact information.
 */
public class Provider {

  /* Instance variables */

  Integer providerId;
  String providerName;
  String providerConnection;
  String contactName;
  String contactPhone;
  String contactEmail;

  ArrayList<Identity> identities;
 
  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(edu.lternet.pasta.identitymanager.Provider.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  /* Constructors */

  public Provider() throws PastaConfigurationException {

    loadConfiguration();

  }

  /* Instance methods */

  /**
   * Sets the provider identifier of the <em>Provider</em> object.
   *
   * @param providerId The provider identifier
   */
  public void setProviderId(Integer providerId) {
    this.providerId = providerId;
  }

  /**
   * Gets the provider identifier of the <em>Provider</em> object.
   *
   * @return The provider identifier
   */
  public Integer getProviderId() {
    return providerId;
  }

  /**
   * Sets the provider name of the <em>Provider</em> object.
   *
   * @param providerName The provider name
   */
  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  /**
   * Gets the provider name of the <em>Provider</em> object.
   *
   * @return The provider name
   */
  public String getProviderName() {
    return providerName;
  }

  /**
   * Sets the network-based provider connection information for the
   * <em>Provider</em> object.
   *
   * @param providerConnection The provider connection information
   */
  public void setProviderConnection(String providerConnection) {
    this.providerConnection = providerConnection;
  }

  /**
   * Gets the network-based provider connection information for the
   * <em>Provider</em> object.
   *
   * @return The provider connection information
   */
  public String getProviderConnection() {
    return providerConnection;
  }

  /**
   * Sets the contact name of the provider for the <em>Provider</em> object.
   *
   * @param contactName The provider's contact name
   */
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * Gets the contact name of the provider for the <em>Provider</em> object.
   *
   * @return The provider's contact name
   */
  public String getContactName() {
    return contactName;
  }

  /**
   * Sets the contact's phone number of the provider for the <em>Provider</em>
   * object.
   *
   * @param contactPhone The contact's phone number
   */
  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  /**
   * Gets the contact's phone number of the provider for the <em>Provider</em>
   * object.
   *
   * @return The contact's phone number
   */
  public String getContactPhone() {
    return contactPhone;
  }

  /**
   * Sets the contact's email address of the provider for the <em>Provider</em>
   * object.
   *
   * @param contactEmail The contact's email address
   */
  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  /**
   * Gets the contact's email address of the provider for the <em>Provider</em>
   * object.
   *
   * @return The contact's email address
   */
  public String getContactEmail() {
    return contactEmail;
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
