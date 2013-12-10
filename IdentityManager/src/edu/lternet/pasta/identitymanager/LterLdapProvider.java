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

import org.apache.log4j.Logger;

/**
 * User: servilla
 * Date: 12/10/13
 * Time: 11:58 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class LterLdapProvider extends Provider {

  /* Instance variables */
 
  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(LterLdapProvider.class);


 
  /* Constructors */

  public LterLdapProvider() throws PastaConfigurationException {

  }

  /* Instance methods */

  public boolean validateUser() {

    boolean isValid = false;

    return isValid;

  }

  /* Class methods */

}
