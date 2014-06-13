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

import edu.lternet.pasta.common.security.authentication.jaxb.ObjectFactory;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;
import org.apache.http.auth.AUTH;
import org.junit.*;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
  private Token mAuthToken;

 
  /* Class variables */

  private static final String OWNER = "uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org";
  private static final String MAP = "ucarroll@gmail.com";
  private static final String ALTOWNER = "uid=cjack,o=LTER,dc=ecoinformatics,dc=org";
  private static final String AUTHSYSTEM = "https://pasta.lternet.edu/authentication";
  private static final String ALTAUTHSYSTEM = "https://google.com";

  /* Constructors */

  /* Instance methods */

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  /**
   * Tests if the owner is authorized to perform an action on the
   * resource defined by the set of access control rules if the owner is
   * not in the set of access control rules.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedOwner() throws Exception {

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
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Cactus");
    token.setGivenName("Jack");
    token.setNickName("Prickly");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier(ALTOWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);

    assertTrue(am.isAuthorized(token, ALTOWNER, AUTHSYSTEM, Rule.Permission.changePermission));

  }

  /**
   * Tests if the logged in user is authorized to perform an action on the
   * resource defined by the set of access control rules if not the resource
   * owner.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedLogin() throws Exception {

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
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier("uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org");
    identity.setProvider("https://pasta.lternet.edu/authentication");
    identities.add(identity);

    assertTrue(am.isAuthorized(token, ALTOWNER, AUTHSYSTEM, Rule.Permission.changePermission));

  }

  /**
   * Tests if a mapped identity "ucarroll@gmail.com" for the login identity
   * "uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org" is authorized to perform
   * an action on the resource defined by the set of access control rules if
   * not the resource owner.
   *
   * @throws Exception
   */
  @Test
  public void  testIsAuthorizedMap() throws Exception {

    StringBuilder ae = new StringBuilder();
    ae.append("<access ");
    ae.append("system=\"https://pasta.lternet.edu\" ");
    ae.append("order=\"allowFirst\" ");
    ae.append("authSystem=\"https://google.com\">");
    ae.append("<allow>");
    ae.append("<principal>ucarroll@gmail.com</principal>");
    ae.append("<permission>read</permission>");
    ae.append("</allow>");
    ae.append("<allow>");
    ae.append("<principal>ucarroll@gmail.com</principal>");
    ae.append("<permission>write</permission>");
    ae.append("</allow>");
    ae.append("<allow>");
    ae.append("<principal>ucarroll@gmail.com</principal>");
    ae.append("<permission>changePermission</permission>");
    ae.append("</allow>");
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier(OWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);
    identity = new Token.Identity();
    identity.setId(Token.Identity.MAP);
    identity.setIdentifier(MAP);
    identity.setProvider(ALTAUTHSYSTEM);
    identities.add(identity);

    assertTrue(am.isAuthorized(token, ALTOWNER, AUTHSYSTEM, Rule.Permission.changePermission));

  }

  /**
   * Test if the user identified in the token can read the resource when only
   * the global "public" identity has a single read allowed access rule.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedPublicRead() throws Exception {

    StringBuilder ae = new StringBuilder();
    ae.append("<access ");
    ae.append("system=\"https://pasta.lternet.edu\" ");
    ae.append("order=\"allowFirst\" ");
    ae.append("authSystem=\"https://pasta.lternet.edu/authentication\">");
    ae.append("<allow>");
    ae.append("<principal>public</principal>");
    ae.append("<permission>read</permission>");
    ae.append("</allow>");
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier(OWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);
    identity = new Token.Identity();
    identity.setId(Token.Identity.MAP);
    identity.setIdentifier(ALTOWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);

    assertTrue(am.isAuthorized(token, MAP, ALTAUTHSYSTEM, Rule.Permission.read));

  }

  /**
   * Test if the user identified in the token can read the resource when only
   * the global "public" identity has a single read allowed access rule.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedDenied() throws Exception {

    StringBuilder ae = new StringBuilder();
    ae.append("<access ");
    ae.append("system=\"https://pasta.lternet.edu\" ");
    ae.append("order=\"allowFirst\" ");
    ae.append("authSystem=\"https://pasta.lternet.edu/authentication\">");
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier(OWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);
    identity = new Token.Identity();
    identity.setId(Token.Identity.MAP);
    identity.setIdentifier(ALTOWNER);
    identity.setProvider(AUTHSYSTEM);
    identities.add(identity);

    assertFalse(am.isAuthorized(token, MAP, ALTAUTHSYSTEM, Rule.Permission.read));

  }

  /**
   * Test if the logged in user identified by the token is first allowed, and
   * then denied when the user identity is explicitly denied access to the
   * resource.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedAllowedDenied() throws Exception {

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
    ae.append("<principal>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</principal>");
    ae.append("<permission>changePermission</permission>");
    ae.append("</deny>");
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier("uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org");
    identity.setProvider("https://pasta.lternet.edu/authentication");
    identities.add(identity);

    assertFalse(am.isAuthorized(token, ALTOWNER, AUTHSYSTEM,
                                   Rule.Permission.changePermission));

  }

  /**
   * Test if the logged in user identified by the token is first allowed, and
   * then denied when a mapped identity is explicitly denied access to the
   * resource.
   *
   * @throws Exception
   */
  @Test
  public void testIsAuthorizedMapDenied() throws Exception {

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
    ae.append("<principal>uid=cjack,o=LTER,dc=ecoinformatics,dc=org</principal>");
    ae.append("<permission>changePermission</permission>");
    ae.append("</deny>");
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());
    ObjectFactory objectFactory = new ObjectFactory();
    Token token = objectFactory.createToken();

    BigInteger expiry = new BigInteger("1389903375597");
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");


    List<Token.Identity> identities = token.getIdentity();
    Token.Identity identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier("uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org");
    identity.setProvider("https://pasta.lternet.edu/authentication");
    identities.add(identity);
    identity = new Token.Identity();
    identity.setId(Token.Identity.MAP);
    identity.setIdentifier("uid=cjack,o=LTER,dc=ecoinformatics,dc=org");
    identity.setProvider("https://pasta.lternet.edu/authentication");
    identities.add(identity);

    assertFalse(am.isAuthorized(token, MAP, ALTAUTHSYSTEM, Rule.Permission.changePermission));

  }

  /**
   * Test that the hash table of rules is correctly generated. In this
   * particular case, the high-water mark rule is also tested.
   *
   * @throws Exception
   */
  @Test
  public void testGetRuleList() throws Exception {

    String expectedRule = "https://pasta.lternet.edu/authentication - " +
                              "allowFirst - allow - uid=ucarroll,o=LTER," +
                              "dc=ecoinformatics,dc=org - changePermission";

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
    ae.append("</access>");

    AccessMatrix am = new AccessMatrix(ae.toString());

    for (Rule rule: am.getRuleList()) {
      String testRule = rule.toString();
      String message = String.format("Expected single rule \"%s\", but received \"%s\"!",
                                        expectedRule, testRule);
      assertTrue(message, testRule.equals(expectedRule));
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
