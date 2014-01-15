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

package edu.lternet.pasta.common.security.authentication;

import java.util.ArrayList;

/**
 * User: servilla
 * Date: 1/13/14
 * Time: 11:37 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * <class description>
 */
public class Token {

  private class Identity {
    String identifier;
    String provider;

    private String getIdentifier() {
      return identifier;
    }

    private void setIdentifier(String identifier) {
      this.identifier = identifier;
    }

    private String getProvider() {
      return provider;
    }

    private void setProvider(String provider) {
      this.provider = provider;
    }

  }

  /* Instance variables */

  private Long expiry;
  private String surName;
  private String givenName;
  private String nickName;
  private ArrayList<Identity> identities = new ArrayList<Identity>();

  /* Class variables */

  private static final String PUBLIC = "public";
  private static final String AUTHENTICATED = "authenticated";
  private static final String PROFILED = "profiled";

  private static final String GLOBAL = "*";
 
  /* Constructors */

  /* Instance methods */

  /**
   * Gets token expiry date/time.
   *
   * @return Token expiry
   */
  public Long getExpiry() {
    return expiry;
  }

  /**
   * Sets token expiry date/time.
   *
   * @param expiry Token expiry
   */
  public void setExpiry(Long expiry) {
    this.expiry = expiry;
  }

  /**
   * Gets surname of the token owner.
   *
   * @return Surname
   */
  public String getSurName() {
    return surName;
  }

  /**
   * Sets surname of the token owner.
   *
   * @param surName Surname
   */
  public void setSurName(String surName) {
    this.surName = surName;
  }

  /**
   * Gets given name of the token owner.
   *
   * @return Given name
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Sets given name of the token owner.
   *
   * @param givenName Given name
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Gets nick name of the token owner.
   *
   * @return Nick name
   */
  public String getNickName() {
    return nickName;
  }

  /**
   * Sets nick name of the token owner.
   *
   * @param nickName Nick name
   */
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  /**
   * Returns the number of Token identities.
   *
   * @return Number of Token identities
   */
  public Integer size() {

    return identities.size();

  }

  /**
   * Returns the identifier associated with the identity identified by index.
   *
   * @param id Identity index
   * @return Identity identifier
   * @throws IndexOutOfBoundsException
   */
  public String getIdentifier(Integer id) throws IndexOutOfBoundsException {

    if (id < 0 || id >= identities.size()) {
      String gripe = String.format("getIdentifier: id value '%d' out of bounds!", id);
      throw new IndexOutOfBoundsException(gripe);
    }

    return identities.get(id).getIdentifier();

  }

  /**
   * Returns the provider associated with the identity identified by index.
   *
   * @param id Identity index
   * @return Identity provider
   * @throws IndexOutOfBoundsException
   */
  public String getProvider(Integer id) throws IndexOutOfBoundsException {

    if (id < 0 || id >= identities.size()) {
      String gripe = String.format("getIdentifier: id value '%d' out of bounds!", id);
      throw new IndexOutOfBoundsException(gripe);
    }

    return identities.get(id).getProvider();

  }

  /**
   * Adds new Identity to list of token owner identities.
   *
   * @param identifier Identity identifier (user or group or role)
   * @param provider Identity provider
   */
  public void addIdentity(String identifier, String provider) {

    Identity identity = new Identity();
    identity.setIdentifier(identifier);
    identity.setProvider(provider);
    identities.add(identity);

  }

  /**
   * Sets the "public" identity.
   */
  public void setPublic() {

    addIdentity(PUBLIC, GLOBAL);

  }

  /**
   * Sets the "authenticated" identity.
   */
  public void setAuthenticated() {

    addIdentity(AUTHENTICATED, GLOBAL);

  }

  /**
   * Sets the "profiled" identity.
   */
  public void setProfiled() {

    addIdentity(PROFILED, GLOBAL);

  }

  /* Class methods */

}
