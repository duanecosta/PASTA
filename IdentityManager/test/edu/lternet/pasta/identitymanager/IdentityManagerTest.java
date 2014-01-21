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

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.*;
import sun.util.logging.resources.logging;

import java.util.List;

/**
 * User: servilla
 * Date: 1/17/14
 * Time: 8:28 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class IdentityManagerTest {

  /* Instance variables */

  private IdentityManager idm;

  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(IdentityManagerTest.class);

  private static String cJackDn;
  private static String cJackPwd;

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    idm = new IdentityManager();

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    idm = null;

  }

  @Test
  public void testLoginLter() throws Exception {

    Credential credential = new Credential();
    credential.setUser(cJackDn);
    credential.setPassword(cJackPwd);

    ProviderFactory.IdP idp = ProviderFactory.IdP.LTERLDAP;

    String tokenXml = idm.login(credential, idp);

    System.out.print(tokenXml);

  }

  /* Class methods */

  /*
 * Load local properties from identity.properties
 */
  private static void loadConfiguration() throws Exception {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      String gripe = "Failed to load the IdentityManager properties file: 'identity.properties'";
      throw new PastaConfigurationException(gripe);
    } else {
      try {
        cJackDn = options.getString("junit.cjack.dn");
        cJackPwd = options.getString("junit.cjack.password");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new PastaConfigurationException(e.getMessage());
      }
    }

  }



  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

    loadConfiguration();

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

}
