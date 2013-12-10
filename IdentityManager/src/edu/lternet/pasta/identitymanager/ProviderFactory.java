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
 * Time: 1:57 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public final class ProviderFactory {

  /* Instance variables */
 
  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(ProviderFactory.class);
 
  /* Constructors */

  /* Instance methods */

  /* Class methods */

  public static Provider getProvider(IdP providerId) {

    Provider provider = null;

    if (providerId == IdP.LTERLDAP) {
      try {
        provider = new LterLdapProvider();
      }
      catch (PastaConfigurationException e) {
        logger.error("getProvider: " + e.getMessage());
        e.printStackTrace();
      }
    }

    return provider;

  }

  enum IdP {

    LTERLDAP, LTEREXTDB, GOOGLE, INCOMMON;

  }

}
