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

package edu.lternet.pasta.common.security.authentication;

import edu.lternet.pasta.common.security.authentication.jaxb.ObjectFactory;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;

/**
 * User: servilla
 * Date: 6/11/14
 * Time: 2:39 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * Generates identities with a fixed set of attributes.
 */
public final class IdentityFactory {

  /* Instance variables */

  /* Class variables */

  public static final String GLOBAL = "*";
 
  /* Constructors */

  /* Instance methods */

  /* Class methods */

  public static Token.Identity getGlobalIdentity(GlobalId gid) {
    ObjectFactory objectFactory = new ObjectFactory();
    Token.Identity identity = objectFactory.createTokenIdentity();

    identity.setId(Token.Identity.GROUP);
    identity.setIdentifier(gid.valueOf());
    identity.setProvider(GLOBAL);

    return identity;

  }

  public enum GlobalId {

    PUBLIC("public"),
    AUTHENTICATED("authenticated"),
    AURA("profiled");

    private String id;

    private GlobalId(String id) {this.id = id;}
    public String valueOf() {return id;}

  }

}
