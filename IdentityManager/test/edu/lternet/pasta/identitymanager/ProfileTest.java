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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.*;
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
public class ProfileTest {

  /* Instance variables */

  private Profile profile;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(Profile.class);

  private static String dbDriver;   // database driver
  private static String dbURL;      // database URL
  private static String dbUser;     // database user name
  private static String dbPassword; // database user password

  private static Date now = new Date();

  private static Boolean activeTest = true;
  private static Timestamp createTimestampTest = new Timestamp(now.getTime());
  private static Timestamp updateTimestampTest = new Timestamp(now.getTime());
  private static String surNameTest = "Odocoileus";
  private static String givenNameTest = "hemionus";
  private static String nickNameTest = "Muley";
  private static String institutionTest = "Ungulate Research Station";
  private static String emailTest = "h.odocoileu@gmail.com";
  private static String intentTest = "Ungulate research";


  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    profile = new Profile();

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    profile = null;

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

  private static void insertProfile() throws Exception {

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("INSERT INTO identity.profile ");
    strBuilder.append("(active,create_timestamp,update_timestamp,sur_name,");
    strBuilder.append("given_name,nick_name,institution,email,intent) ");
    strBuilder.append("VALUES (");

    if (activeTest) {
      strBuilder.append("TRUE,'");
    }
    else {
      strBuilder.append("FALSE,'");
    }

    strBuilder.append(createTimestampTest);
    strBuilder.append("','");
    strBuilder.append(updateTimestampTest);
    strBuilder.append("','");
    strBuilder.append(surNameTest);
    strBuilder.append("','");
    strBuilder.append(givenNameTest);
    strBuilder.append("','");
    strBuilder.append(nickNameTest);
    strBuilder.append("','");
    strBuilder.append(institutionTest);
    strBuilder.append("','");
    strBuilder.append(emailTest);
    strBuilder.append("','");
    strBuilder.append(intentTest);
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

      if (stmt.executeUpdate(sql) == 0) {
        String gripe = "saveProfile: '" + sql + "' failed";
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

  private static void purgeProfile(Integer profileId) throws Exception {

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

    loadConfiguration();

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {


  }

}
