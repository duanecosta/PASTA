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

package edu.lternet.pasta.common.security.authorization;

import edu.lternet.pasta.common.security.token.AuthToken;
import edu.lternet.pasta.common.security.token.DummyCookieHttpHeaders;
import org.junit.*;

import javax.ws.rs.core.HttpHeaders;

import static org.junit.Assert.assertTrue;

/**
 * User: servilla
 * Date: 4/9/14
 * Time: 11:30 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authorization
 * <p/>
 * <class description>
 */
public class AccessMatrixTest {

  /* Instance variables */

  private AccessMatrix mAccessMatrix;
  private AccessElement mAccessElement;
  private AuthToken mAuthToken;

 
  /* Class variables */

  private static final String OWNER="uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org";
 
  /* Constructors */

  /* Instance methods */

  @Before
  public void setUp() throws Exception {

    StringBuilder ae = new StringBuilder();
    ae.append("<access ");
    ae.append("system=\"https://pasta.lternet.edu\" ");
    ae.append("order=\"allowFirst\" ");
    ae.append("authSystem=\"https://pasta.lternet.edu/authentication\">");
    ae.append("<allow>");
    ae.append("<principal>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</principal>");
    ae.append("<permission>read</permission>");
    ae.append("</allow>");
    ae.append("<allow>");
    ae.append("<principal>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</principal>");
    ae.append("<permission>write</permission>");
    ae.append("</allow>");
    ae.append("<allow>");
    ae.append("<principal>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</principal>");
    ae.append("<permission>changePermission</permission>");
    ae.append("</allow>");
    ae.append("<deny>");
    ae.append("<principal>public</principal>");
    ae.append("<permission>read</permission>");
    ae.append("</deny>");
    ae.append("</access>");

    mAccessMatrix = new AccessMatrix(ae.toString());
    HttpHeaders httpHeadersOwner = new DummyCookieHttpHeaders(OWNER);
    mAuthToken = DummyCookieHttpHeaders.getAuthToken(httpHeadersOwner);

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testIsAuthorized() throws Exception {

    assertTrue(mAccessMatrix.isAuthorized(mAuthToken, OWNER, Rule.Permission.changePermission));

  }

  @Test
  public void testGetRuleList() throws Exception {

    for (Rule rule: mAccessMatrix.getRuleList()) {
      System.out.printf("%s%n", rule.toString());
    }

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
