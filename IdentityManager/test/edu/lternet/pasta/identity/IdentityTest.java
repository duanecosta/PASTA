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

package edu.lternet.pasta.identity;

import edu.lternet.pasta.identitymanager.ConfigurationListener;

import org.junit.*;
import static org.junit.Assert.*;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * User: servilla
 * Date: 11/18/13
 * Time: 10:34 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identity
 * <p/>
 * <class description>
 */
public class IdentityTest {

  /* Instance variables */

  Identity identity;

  /* Class variables */

  private static final Logger logger = Logger.getLogger(edu.lternet.pasta.identity.IdentityTest.class);

  private static String userId;
  private static int providerId = -1;

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    identity = new Identity(userId, providerId);

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    identity = null;

  }

  @Test
  public void testConstruction() {

    System.err.printf("%s\n", identity.getUserIdentity());
    assert(true);

  }

  /* Class methods */

  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

    ConfigurationListener.configure();
    Configuration options = ConfigurationListener.getOptions();

    if (options == null) {
      fail("Failed to load the IdentityManager properties file: 'identity.properties'");
    } else {
      try {
        userId = options.getString("identity.testuseridentity");
        providerId = options.getInt("identity.testprovideridentity");
      }
      catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
      }
    }

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {


  }

}
