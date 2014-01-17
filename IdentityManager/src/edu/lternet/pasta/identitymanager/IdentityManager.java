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

import edu.lternet.pasta.common.security.authentication.TokenUtility;
import edu.lternet.pasta.common.security.authentication.jaxb.ObjectFactory;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.List;

/**
 * User: servilla
 * Date: 1/17/14
 * Time: 8:27 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class IdentityManager {

  /* Instance variables */

  private Provider mProvider = null;
  private Identity mIdentity = null;
  private Profile mProfile = null;
  private Integer mId = 0;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(IdentityManager.class);

  private static final String PUBLIC = "public";
  private static final String GLOBAL = "*";

  /* Constructors */

  /* Instance methods */

  public String login(Credential credential, ProviderFactory.IdP idp)
      throws UserValidationException {

    // Credential and identity provider must be set together, or not at all
    if (credential != null && idp == null) {
      String gripe = "login: identity provider is null, " +
                         "when credential is not null!";
      throw new UserValidationException(gripe);
    }

    if (credential == null && idp != null) {
      String gripe = "login: credential is null, when identity provider is " +
                         "not be null!";
      throw new UserValidationException(gripe);
    }

    String tokenXml = null;

    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();
    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity;

    // Set "public" identity for all users
    mId++;
    identity = objectFactory.createTokenIdentity();
    identity.setId(new BigInteger(mId.toString()));
    identity.setIdentifier(PUBLIC);
    identity.setProvider(GLOBAL);
    identities.add(identity);

    if (credential != null && idp != null) {
      mId++;
      mProvider = ProviderFactory.getProvider(idp);
      String userId = credential.getUser();
      String providerName = mProvider.getProviderName();
      identity = objectFactory.createTokenIdentity();
      identity.setId(new BigInteger(mId.toString()));
      identity.setIdentifier(userId);
      identity.setProvider(providerName);
      identities.add(identity);
    }

    tokenXml = TokenUtility.marshalToken(token);

    return tokenXml;

  }

  /* Class methods */

}
