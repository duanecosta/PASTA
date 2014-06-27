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

import edu.lternet.pasta.common.security.authentication.jaxb.ObjectFactory;
import edu.lternet.pasta.common.security.authentication.jaxb.Token;

import org.junit.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: servilla
 * Date: 1/16/14
 * Time: 7:37 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * <class description>
 */
public class TokenUtilityTest {

  /* Instance variables */

  private ObjectFactory of;
  private Token token;
  private String tokenXml;

  /* Class variables */

  private static final String PASTA_IDP = "https://pasta.lternet.edu/authentication";
 
  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    of = new ObjectFactory();
    token = of.createToken();

    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
    xml.append("<token expires=\"1389903375597\" surName=\"Carroll\" givenName=\"Utah\" nickName=\"Dusty\">\n");
    xml.append("    <identity id=\"login\">\n");
    xml.append("        <identifier>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</identifier>\n");
    xml.append("        <provider>https://pasta.lternet.edu/authentication</provider>\n");
    xml.append("    </identity>\n");
    xml.append("    <identity id=\"map\">\n");
    xml.append("        <identifier>utah.carroll@gmail.com</identifier>\n");
    xml.append("        <provider>https://google.com</provider>\n");
    xml.append("    </identity>\n");
    xml.append("</token>\n");

    tokenXml = xml.toString();

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
    identity.setIdentifier("utah.carroll@gmail.com");
    identity.setProvider("https://google.com");
    identities.add(identity);

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    of = null;
    token = null;
    tokenXml = null;

  }

  /**
   * Test token "Time-To-Live" expiry.
   */
  @Test
  public void testHasExpired() {

    String message = "Expected token to have expired, but token still alive!";
    assertTrue(message, TokenUtility.hasExpired(token));

    // Reset token expiration to "now" + 10 seconds
    Date now = new Date();
    BigInteger expiry = BigInteger.valueOf(now.getTime() + 10000L);
    token.setExpires(expiry);

    message = "Expected token to still be alive, but token has expired!";
    assertFalse(message, TokenUtility.hasExpired(token));

  }

  /**
   * Test marshaling an authentication token to XML.
   */
  @Test
  public void testMarshalToken() {

    String testXml = TokenUtility.marshalToken(token);
    String message = "XML strings for 'tokenXml' and 'testXml' do not match!";
    assertEquals(message, tokenXml, testXml);

  }

  /**
   * Test unmarshaling XML to an authentication token.
   */
  @Test
  public void testUnmarshalToken() {

    Token testToken = TokenUtility.unmarshalToken(tokenXml);
    String message = "Token objects for 'token' and 'testToken' do not match!";

    assertEquals(message,token.getExpires(), testToken.getExpires());
    assertEquals(message,token.getSurName(), testToken.getSurName());
    assertEquals(message,token.getGivenName(), testToken.getGivenName());
    assertEquals(message,token.getNickName(), testToken.getNickName());

    List<Token.Identity> identities = token.getIdentity();
    List<Token.Identity> testIdentities = testToken.getIdentity();
    if (identities.size() != testIdentities.size()) fail(message);

    Token.Identity identity0, identity1;
    identity0 = identities.get(0);
    identity1 = identities.get(1);

    Token.Identity testIdentity0, testIdentity1;
    testIdentity0 = testIdentities.get(0);
    testIdentity1 = testIdentities.get(1);

    assertEquals(message, identity0.getId(), testIdentity0.getId());
    assertEquals(message, identity0.getIdentifier(), testIdentity0.getIdentifier());
    assertEquals(message, identity0.getProvider(), testIdentity0.getProvider());

    assertEquals(message, identity1.getId(), testIdentity1.getId());
    assertEquals(message, identity1.getIdentifier(), testIdentity1.getIdentifier());
    assertEquals(message, identity1.getProvider(), testIdentity1.getProvider());

  }

  /**
   * Test getter for user's login identity provider.
   */
  @Test
  public void testGetLoginProvider() {

    String provider = TokenUtility.getLoginIdentity(token).getProvider();

    String message = String.format("Expected login provider to be %s, but received %s!%n", PASTA_IDP, provider);
    assertTrue(message, PASTA_IDP.equals(provider));

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
