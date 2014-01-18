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
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;

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

  private static final int HOST = 0;
  private static final int PORT = 1;
  private static final int KEYSTORE = 2;

  private static final Logger logger =
      Logger.getLogger(LterLdapProvider.class);

  private static String host;
  private static Integer port;
  private static String keystore;

  /* Constructors */

  public LterLdapProvider(Integer providerId)
      throws PastaConfigurationException, ProviderDoesNotExistException,
                 SQLException, ClassNotFoundException {

    super(providerId);

    // Parse provider connection information for LTER LDAP connection
    String connParts[] = this.providerConnection.split(":");
    host = connParts[HOST];
    port = Integer.valueOf(connParts[PORT]);
    keystore = connParts[KEYSTORE];

  }

  /* Instance methods */

  /**
   * Validates the user's identity based on the provided credentials.
   *
   * @param credential
   */
  public void validateUser(Credential credential)
      throws UserValidationException {

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

    if (!isValid) {
      String gripe = String.format("validateUser: the user '%s' was not " +
                                       "validated!", user);
      throw new UserValidationException(gripe);
    }

  }

  /**
   * Returns the list of LTER Groups that the user is affiliated with.
   *
   * @return List of Groups
   */
  public ArrayList<Group> getGroups() {

    //TODO: Connection to LTER personnel DB webservice here to retrieve groups

    ArrayList<Group> groups = new ArrayList<Group>();

    // Add default LTER group
    Group group = new Group();
    group.setProviderName(providerName);
    group.setGroupName("LTER");
    groups.add(group);

    return groups;

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

  /* Class methods */

}
