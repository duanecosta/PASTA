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
 * Date: 12/16/13
 * Time: 9:51 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * A class that models a group or role.
 */
public class Group {

  /* Instance variables */

  private String groupName;
  private String mProviderId;
 
  /* Class variables */

  /* Constructors */

  /* Instance methods */

  /**
   * Sets the group name for the Group.
   *
   * @param name Group name
   */
  public void setGroupName(String name) {
    groupName = name;
  }

  /**
   * Gets the group name of the Group.
   *
   * @return Group name
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * Sets the provider identifier for the Group.
   *
   * @param name Provider identifier
   */
  public void setProviderId(String name) {

    mProviderId = name;

  }

  /**
   * Gets the provider identifier of the Group.
   *
   * @return Provider identifier
   */
  public String getProviderId() {

    return mProviderId;

  }
  /* Class methods */

}
