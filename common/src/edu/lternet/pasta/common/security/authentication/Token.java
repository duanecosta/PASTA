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

  /* Instance variables */

  private Long expiry;
  private Integer profileId;
  private String surName;
  private String givenName;
  private String nickName;
  private ArrayList<Group> groups;

  /* Class variables */
 
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
   * Gets profile identifier of the token owner.
   *
   * @return Profile identifier
   */
  public Integer getProfileId() {
    return profileId;
  }

  /**
   * Sets profile identifier of the token owner.
   *
   * @param profileId Profile identifier
   */
  public void setProfileId(Integer profileId) {
    this.profileId = profileId;
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
   * Gets groups of the token owner.
   *
   * @return Groups
   */
  public ArrayList<Group> getGroups() {
    return groups;
  }

  /**
   * Sets groups of the token owner.
   *
   * @param groups Groups
   */
  public void setGroups(ArrayList<Group> groups) {
    this.groups = groups;
  }

  /* Class methods */

}
