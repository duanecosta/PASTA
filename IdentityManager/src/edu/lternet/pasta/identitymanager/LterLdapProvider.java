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

import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustStoreTrustManager;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;

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

  private static String host;
  private static Integer port;
  private static String keystore;
  private static String cwd;

  /* Constructors */

  public LterLdapProvider() throws PastaConfigurationException {

    loadConfiguration();

  }

  /* Instance methods */

  /**
   * Validates the user's identity based on the provided credentials.
   *
   * @param credential
   * @return The validation state of the user's identity
   */
  public boolean validateUser(Credential credential) {

    String user = credential.getUser();
    String password = credential.getPassword();

    boolean isValid = false;
    LDAPConnection connection;

    // First attempt TLS connection, followed by non-TLS connection
    if ((connection = makeTlsConnection()) == null)
      connection = makeConnection();

    if (connection != null) {
      try {
        LDAPResult result = connection.bind(user, password);
        ResultCode code = result.getResultCode();

        if (code.intValue() == ResultCode.SUCCESS_INT_VALUE) {
          String uid = DN.getRDNString(user);
          String base = user.replace(uid + ",", "");
          String userId = uid.split("=")[1];
          Filter filter = Filter.createEqualityFilter("uid", userId);
          SearchRequest searchRequest = new SearchRequest(base,
                                                             SearchScope.SUB,
                                                             filter, "uid");
          SearchResult searchResult = connection.search(searchRequest);
          SearchResultEntry entry;
          entry = searchResult.getSearchEntry(user);

          // Perform case-sensitive UID test for final authentication
          if (entry != null && entry.getAttributeValue("uid").equals(userId)) {
            isValid = true;
          }

        } else {
          String gripe = String.format("validateUser: LDAPConnection.bind() did" +
                                           " not throw an exception, " +
                                           "but the 'bind' failed - %s\n",
                                          code.toString());
          logger.error(gripe);
        }
      }
      catch (LDAPException e) {
        logger.error(String.format("validateUser: %s\n", e.getMessage()));
      }
      finally {
        connection.close();
      }
    }

    return isValid;

  }

  /*
   * Returns an LDAP TLS connection to the specified host and port.
   */
  private LDAPConnection makeTlsConnection() {

    LDAPConnection connection = null;

    try {
      connection = new LDAPConnection(host, port);
      // Securing the connection in accordance with LDAPv3
      SSLUtil sslUtil = new SSLUtil(new TrustStoreTrustManager(cwd + keystore));
      SSLContext sslContext;
      sslContext = sslUtil.createSSLContext();
      ExtendedRequest request = new StartTLSExtendedRequest(sslContext);
      ExtendedResult result = connection.processExtendedOperation(request);
      ResultCode code = result.getResultCode();
      if (code != ResultCode.SUCCESS) {
        connection = null;
      }
    } catch (GeneralSecurityException e) {
      logger.error(String.format("makeTlsConnection: %s\n", e.getMessage()));
      e.printStackTrace();
      connection = null;
    } catch (LDAPException e) {
      logger.error(String.format("makeTlsConnection: %s\n", e.getMessage()));
      e.printStackTrace();
      connection = null;
    }

    return connection;

  }

  /*
   * Returns an LDAP connection to the specified host and port.
   */
  private LDAPConnection makeConnection() {

    LDAPConnection connection = null;

    try {
      connection = new LDAPConnection(host, port);
    } catch (LDAPException e) {
      logger.error(String.format("makeConnection: %s\n", e.getMessage()));
      e.printStackTrace();
      connection = null;
    }

    return connection;

  }


  /*
   * Load local properties from identity.properties
   */
  private void loadConfiguration() throws PastaConfigurationException {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new PastaConfigurationException(gripe);
    } else {
      try {
        host = options.getString("ldap.Host");
        port = options.getInt("ldap.Port");
        keystore = options.getString("ldap.Keystore");
        cwd = options.getString("system.cwd");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new PastaConfigurationException(e.getMessage());
      }
    }

  }

  /* Class methods */

}
