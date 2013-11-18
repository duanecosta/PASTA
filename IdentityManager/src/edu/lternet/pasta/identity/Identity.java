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

package edu.lternet.pasta.identity;

import edu.lternet.pasta.identitymanager.ConfigurationListener;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import java.io.IOException;


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

  String userId;
  int providerId;
  int profileId = -1;

  private String dbDriver;   // database driver
  private String dbURL;      // database URL
  private String dbUser;     // database user name
  private String dbPassword; // database user password

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(edu.lternet.pasta.identity.Identity.class);

 
  /* Constructors */

  public Identity(String userId, int providerId) {

    this.userId = userId;
    this.providerId = providerId;

  }

  /* Instance methods */

  /**
   *
   * @return The user's identity
   */
  public String getUserIdentity() {
    return this.userId;
  }

  private void loadConfiguration() throws IOException {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new IOException(gripe);
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
      }
    }

  }

  /*
   * Reads Identity attributes from database if exists.
   */
  private void readIdentity() {


  }

  /* Class methods */

}
