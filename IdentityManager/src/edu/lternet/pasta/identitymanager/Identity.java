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


/**
 * User: servilla
 * Date: 11/18/13
 * Time: 10:35 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identity
 * <p/>
 * <class description>
 */
public class Identity {

  /* Instance variables */

  private static final Logger logger =
      Logger.getLogger(Identity.class);
  String userId;
  int providerId;
  int profileId = -1;

  private String dbDriver;   // database driver
  private String dbURL;      // database URL
  private String dbUser;     // database user name
  private String dbPassword; // database user password

  /* Class variables */

 
  /* Constructors */

  public Identity(String userId, int providerId)
      throws PastaConfigurationException, SQLException, ClassNotFoundException {

    loadConfiguration();
    initIdentity(userId, providerId);

  }

  /* Instance methods */

  /**
   * Returns the user identity for the given identity object.
   *
   * @return The user identity
   */
  public String getUserIdentity() {
    return this.userId;
  }

  /*
   * Loads local properties from identity.properties
   */
  private void loadConfiguration() throws PastaConfigurationException {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new PastaConfigurationException(gripe);
    } else {
      try {
        this.dbDriver = options.getString("db.Driver");
        this.dbURL = options.getString("db.URL");
        this.dbUser = options.getString("db.User");
        this.dbPassword = options.getString("db.Password");
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
      Class.forName(this.dbDriver);
    }
    catch (ClassNotFoundException e) {
      logger.error("Can't load driver " + e.getMessage());
      throw (e);
    }

    // Make the database connection
    try {
      conn = DriverManager.getConnection(this.dbURL, this.dbUser,
                                            this.dbPassword);

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
   * Initialize the identity object and update entry with current
   * verification date/time or create new entry if does not exist.
   */
  private void initIdentity(String userId, int providerId)
      throws SQLException, ClassNotFoundException {

    this.userId = userId;
    this.providerId = providerId;

    String sql = "SELECT identity.identity.profile_id FROM " +
                 "identity.identity WHERE " +
                 "identity.identity.user_id='" + userId + "' AND " +
                 "identity.identity.provider_id=" +
                 Integer.toString(providerId) + ";";

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

      if (rs.next()) {

        this.profileId = rs.getInt("profile_id");

        // Identity already in database, perform "update".
        sql = "UPDATE identity.identity " +
              "SET verification_date=now() " +
              "WHERE identity.identity.user_id='" + userId + "' AND " +
              "identity.identity.provider_id=" +
              Integer.toString(providerId) + ";";

        if (stmt.executeUpdate(sql) == 0) {
          String gripe = "initIdentity: update '" + sql + "' failed";
          throw new SQLException(gripe);
        }

      } else {

        // Identity not in database, perform "insert".
        sql = "INSERT INTO identity.identity VALUES " +
                  "('" + userId + "'," + providerId + ", NULL, now());";

        if (stmt.executeUpdate(sql) == 0) {
          String gripe = "initIdentity: insert '" + sql + "' failed";
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

  /* Class methods */

}
