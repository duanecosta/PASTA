/*
 * Copyright 2011-2014 the University of New Mexico.
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

/**
 * User: servilla
 * Date: 12/13/13
 * Time: 12:25 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * Stores portable credential information for a mUser.
 */
public class Credential {

  /* Instance variables */

  private String mUser;
  private String mPassword;
  private String mAssertion;
 
  /* Class variables */
 
  /* Constructors */

  /* Instance methods */

  /**
   * Set credential user identifier
   *
   * @param user User identifier
   */
  public void setUser(String user) {
    this.mUser = user;
  }

  /**
   * Get credential user identifier
   *
   * @return User identifier
   */
  public String getUser() {
    return mUser;
  }

  /**
   * Set credential password
   *
   * @param password Credential password
   */
  public void setPassword(String password) {
    this.mPassword = password;
  }

  /**
   * Get credential password
   *
   * @return Credential password
   */
  public String getPassword() {
    return mPassword;
  }

  /**
   * Set credential assertion
   *
   * @param assertion Credential assertion
   */
  public void setAssertion(String assertion) {
    this.mAssertion = assertion;
  }

  /**
   * Get credential assertion
   *
   * @return Credential assertion
   */
  public String getAssertion() {
    return mAssertion;
  }

  /* Class methods */

}
