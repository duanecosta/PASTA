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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(IdentityManager.class);

  private static final String PUBLIC = "public";
  private static final String GLOBAL = "*";

  /* Constructors */

  /* Instance methods */

  /**
   *
   * @param credential
   * @param idp
   * @return
   * @throws UserValidationException
   * @throws PastaConfigurationException
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public String login(Credential credential, ProviderFactory.IdP idp)
      throws UserValidationException, PastaConfigurationException, SQLException,
                 ClassNotFoundException {

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
    List<Token.Identity> tokenIdentities = token.getIdentity();
    Token.Identity tokenIdentity;

    // Set "public" identity for all users
    tokenIdentity = objectFactory.createTokenIdentity();
    tokenIdentity.setId(new BigInteger("1"));
    tokenIdentity.setIdentifier(PUBLIC);
    tokenIdentity.setProvider(GLOBAL);
    tokenIdentities.add(tokenIdentity);

    if (credential != null && idp != null) {

      /*
       * Setup the appropriate provider and validate the user's identity
       */
      Provider provider;
      provider = ProviderFactory.getProvider(idp);
      provider.validateUser(credential);// can throw UserValidationException

      /*
       * Obtain user identity information
       */
      String userId = credential.getUser();
      String providerName = provider.getProviderName();

      /*
       * Load user Identity if exists, otherwise create new Identity and save
       * to Identity database
       */

      Date now = new Date();
      Identity identity = null;
      try {
        identity = new Identity(userId, provider.providerId);
        identity.setVerifyTimestamp(now);
        identity.updateIdentity();
      }
      catch (IdentityDoesNotExistException e) { // create new identity and save
        identity = new Identity();
        identity.setUserId(userId);
        identity.setProviderId(provider.getProviderId());
        identity.setVerifyTimestamp(now);
        identity.saveIdentity();
      }

      /*
       * Build identity object for token
       */
      tokenIdentity = objectFactory.createTokenIdentity();
      tokenIdentity.setId(new BigInteger("2"));
      tokenIdentity.setIdentifier(userId);
      tokenIdentity.setProvider(providerName);
      tokenIdentities.add(tokenIdentity);

      /*
       * Add group identities to token identity block
       */
      Integer id = 3;
      ArrayList<Group> groups = provider.getGroups();
      for (Group group: groups) {
        tokenIdentity = objectFactory.createTokenIdentity();
        tokenIdentity.setId(new BigInteger((id++).toString()));
        tokenIdentity.setIdentifier(group.getGroupName());
        tokenIdentity.setProvider(providerName);
        tokenIdentities.add(tokenIdentity);
      }

      /*
       * If profile exists, add mapped identities to token identity block
       */

    }

    tokenXml = TokenUtility.marshalToken(token);

    return tokenXml;

  }

  /* Class methods */

}
