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

import edu.lternet.pasta.common.security.authentication.TokenUtility;
import edu.lternet.pasta.common.security.authentication.jaxb.ObjectFactory;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;
import org.apache.commons.configuration.Configuration;
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

  private Long mMapIdentityTtl;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(IdentityManager.class);

  private static final String PUBLIC = "public";
  private static final String AUTHENTICATED = "authenticated";
  private static final String PROFILED = "profiled";
  private static final String GLOBAL = "*";

  /* Constructors */

  public IdentityManager() throws PastaConfigurationException {

    loadConfiguration();

  }

  /* Instance methods */

  /**
   * Logs the user into PASTA and returns an authentication token.
   *
   * @param credential The user's credential
   * @param idp The Identity Provider identifier
   * @return Authentication token
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
                         "not null!";
      throw new UserValidationException(gripe);
    }

    String tokenXml;

    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();
    List<Token.Identity> tokenIdentities = token.getIdentity();
    Token.Identity tokenIdentity;

    Date now = new Date();
    token.setExpires(BigInteger.valueOf(now.getTime()));

    // Set "public" identity for all users
    tokenIdentities.add(setPublicIdentity());

    if (credential != null) { // && idp != null is always true at this point

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
      String providerId = provider.getProviderId();

      /*
       * Load user Identity if exists, otherwise create new Identity and save
       * to Identity database
       */
      Identity identity;
      now = new Date();
      try {
        identity = new Identity(userId, provider.getProviderId());
        identity.setVerifyTimestamp(now);
        identity.updateIdentity();
      }
      catch (IdentityDoesNotExistException e) { // create new identity and save
        String message = String.format("login: creating new identity for user" +
                                           " '%s' and IdP '%s' at %s",
                                          userId, provider.getProviderId(),
                                          now.toString());
        logger.warn(message);
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
      tokenIdentity.setId("identity");
      tokenIdentity.setIdentifier(userId);
      tokenIdentity.setProvider(providerId);
      tokenIdentities.add(tokenIdentity);

      // Set "authenticated" identity for all users
      tokenIdentities.add(setAuthenticatedIdentity());

      /*
       * Add group identities to token identity block
       */
      ArrayList<Group> groups = provider.getGroups();
      for (Group group: groups) {
        tokenIdentity = objectFactory.createTokenIdentity();
        tokenIdentity.setId("group");
        tokenIdentity.setIdentifier(group.getGroupName());
        tokenIdentity.setProvider(providerId);
        tokenIdentities.add(tokenIdentity);
      }

      /*
       * If profile exists, add mapped identities to token
       */

      Profile profile = null;

      try {
        profile = new Profile(identity.getProfileId());
      }
      catch (ProfileDoesNotExistException e) {
        String message = String.format("login: profile does not exist for " +
                                           "user '%s' and IdP '%s'", userId,
                                          provider.getProviderId());
        logger.warn(message);
      }

      if (profile != null) {

        token.setSurName(profile.getSurName());
        token.setGivenName(profile.getGivenName());
        token.setNickName(profile.getNickName());

        ArrayList<Identity> mapIdentities = profile.getIdentities();
        for (Identity mapIdentity: mapIdentities) {
          // ensure identity is not stale
          if ((mapIdentity.getVerifyTimestamp()).getTime() + mMapIdentityTtl
                  >= now.getTime()) {
            tokenIdentity = objectFactory.createTokenIdentity();
            tokenIdentity.setId("mapped");
            tokenIdentity.setIdentifier(mapIdentity.getUserId());
            tokenIdentity.setProvider(mapIdentity.getProviderId());

            if (!token.contains(tokenIdentity)) {
              tokenIdentities.add(tokenIdentity);
            }

          } else {
            String message = String.format("login: identity for user '%s' and" +
                                               " IdP '%s' is stale and has " +
                                               "not been added to token",
                                              mapIdentity.getUserId(),
                                              mapIdentity.getProviderId());
            logger.warn(message);
          }
        }

        // Set "profiled" identity for all users
        tokenIdentities.add(setProfiledIdentity());

      }

    }

    tokenXml = TokenUtility.marshalToken(token);

    return tokenXml;

  }

  /*
   * Load local properties from identity.properties
   */
  protected void loadConfiguration() throws PastaConfigurationException {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new PastaConfigurationException(gripe);
    } else {
      try {
       mMapIdentityTtl = options.getLong("ttl.MapIdentity");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new PastaConfigurationException(e.getMessage());
      }
    }

  }

  private Token.Identity setPublicIdentity() {

    ObjectFactory objectFactory = new ObjectFactory();
    Token.Identity tokenIdentity;

    tokenIdentity = objectFactory.createTokenIdentity();
    tokenIdentity.setId(PUBLIC);
    tokenIdentity.setIdentifier(PUBLIC);
    tokenIdentity.setProvider(GLOBAL);

    return tokenIdentity;

  }

  private Token.Identity setAuthenticatedIdentity() {

    ObjectFactory objectFactory = new ObjectFactory();
    Token.Identity tokenIdentity;

    tokenIdentity = objectFactory.createTokenIdentity();
    tokenIdentity.setId(AUTHENTICATED);
    tokenIdentity.setIdentifier(AUTHENTICATED);
    tokenIdentity.setProvider(GLOBAL);

    return tokenIdentity;

  }

  private Token.Identity setProfiledIdentity() {

    ObjectFactory objectFactory = new ObjectFactory();
    Token.Identity tokenIdentity;

    tokenIdentity = objectFactory.createTokenIdentity();
    tokenIdentity.setId(PROFILED);
    tokenIdentity.setIdentifier(PROFILED);
    tokenIdentity.setProvider(GLOBAL);

    return tokenIdentity;

  }

  /* Class methods */

}
