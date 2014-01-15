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

package edu.lternet.pasta.common.security.authentication;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * User: servilla
 * Date: 1/13/14
 * Time: 12:12 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * <class description>
 */
public class TokenTest {

  /* Instance variables */

  private Token token;
 
  /* Class variables */

  private static String userIdCarroll = "uid=ucarroll,org=LTER,dc=ecoinformatics,dc=org";
  private static String userIdJack = "uid=cjack,org=LTER,dc=ecoinformatics,dc=org";
  private static String provider = "https://pasta.lternet.edu/authentication";

  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    token = new Token();

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    token = null;

  }

  /**
   * Tests identity management of the Token.
   */
  @Test
  public void testIdentities() {

    token.addIdentity(userIdCarroll, provider);
    token.addIdentity(userIdJack, provider);

    String message;

    message = String.format("Expected 2 identities, but received %d!", token.size());
    if (token.size() != 2) fail(message);

    message = String.format("Expected identity '%s', but received '%s'!", userIdCarroll, token.getIdentifier(0));
    if (token.getIdentifier(0) != userIdCarroll) fail(message);
    message = String.format("Expected provider '%s', but received '%s'!", provider, token.getProvider(0));
    if (token.getProvider(0) != provider) fail(message);

    message = String.format("Expected identity '%s', but received '%s'!", userIdJack, token.getIdentifier(1));
    if (token.getIdentifier(1) != userIdJack) fail(message);
    message = String.format("Expected provider '%s', but received '%s'!", provider, token.getProvider(1));
    if (token.getProvider(1) != provider) fail(message);

  }

  /* Class methods */

  /**
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  /**
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

}
