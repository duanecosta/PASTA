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

import org.apache.log4j.Logger;

/**
 * User: servilla
 * Date: 12/16/13
 * Time: 9:51 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * A class that models a group or role.
 */
public class Group {

  /* Instance variables */

  private String groupName;
  private String providerName;
 
  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(Group.class);

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
   * Sets the provider name for the Group.
   *
   * @param name Provider name
   */
  public void setProviderName(String name) {

    providerName = name;

  }

  /**
   * Gets the provider name of the Group.
   *
   * @return Provider name
   */
  public String getProviderName() {

    return providerName;

  }
  /* Class methods */

}
