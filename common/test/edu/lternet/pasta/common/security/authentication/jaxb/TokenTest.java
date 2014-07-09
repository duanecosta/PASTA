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

package edu.lternet.pasta.common.security.authentication.jaxb;

import org.junit.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * User: servilla
 * Date: 1/16/14
 * Time: 12:01 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication.jaxb
 * <p/>
 * <class description>
 */
public class TokenTest {

  /* Instance variables */

  private Token token;
  private Token.Identity identity;
  private List<Token.Identity> identities;
 
  /* Class variables */
 
  /* Constructors */

  /* Instance methods */

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    ObjectFactory of = new ObjectFactory();
    token = of.createToken();

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

    token = null;
    identity = null;
    identities = null;

  }

  @Test
  public void testTokenMarshall() throws Exception {
    Date now = new Date();
    BigInteger expiry = BigInteger.valueOf(now.getTime());
    token.setExpires(expiry);
    token.setSurName("Carroll");
    token.setGivenName("Utah");
    token.setNickName("Dusty");

    identities = token.getIdentity();

    identity = new Token.Identity();
    identity.setId(Token.Identity.LOGIN);
    identity.setIdentifier("uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org");
    identity.setProvider("https://pasta.lternet.edu/authentication");
    identities.add(identity);

    identity = new Token.Identity();
    identity.setId(Token.Identity.MAP);
    identity.setIdentifier("utah.carroll@gmail.com");
    identity.setProvider("https://google.com");
    identities.add(identity);

    try {
      JAXBContext jc = JAXBContext.newInstance("edu.lternet.pasta.common.security.authentication.jaxb");
      Marshaller m = jc.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      m.marshal(token, baos);
      String tokenXml = new String(baos.toByteArray(), "UTF-8");
      System.out.print(tokenXml);
    }
    catch (JAXBException e) {
      System.err.printf("testTokenMarshall: %s", e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (UnsupportedEncodingException e) {
      System.err.printf("testTokenMarshall: %s", e.getMessage());
      e.printStackTrace();
      throw e;
    }

  }

  @Test
  public void testTokenUnmarshall() throws Exception {

    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
    xml.append("<token nickName=\"Dusty\" givenName=\"Utah\" surName=\"Carroll\" expires=\"1389903375597\">\n");
    xml.append("    <identity id=\"login\">\n");
    xml.append("        <identifier>uid=ucarroll,o=LTER,dc=ecoinformatics,dc=org</identifier>\n");
    xml.append("        <provider>https://pasta.lternet.edu/authentication</provider>\n");
    xml.append("    </identity>\n");
    xml.append("    <identity id=\"map\">\n");
    xml.append("        <identifier>utah.carroll@gmail.com</identifier>\n");
    xml.append("        <provider>https://google.com</provider>\n");
    xml.append("    </identity>\n");
    xml.append("</token>\n");

    String tokenXml = xml.toString();

    try {
      JAXBContext jc = JAXBContext.newInstance("edu.lternet.pasta.common.security.authentication.jaxb");
      Unmarshaller u = jc.createUnmarshaller();
      InputStream is = new ByteArrayInputStream(tokenXml.getBytes("UTF-8"));
      token = (Token) u.unmarshal(is);
    }
    catch (JAXBException e) {
      System.err.printf("testTokenUnmarshall: %s", e.getMessage());
      e.printStackTrace();
      throw e;
    }
    catch (UnsupportedEncodingException e) {
      System.err.printf("testTokenUnmarshall: %s", e.getMessage());
      e.printStackTrace();
      throw e;
    }

    System.out.printf("Expiry: %s%n", token.getExpires());
    System.out.printf("Surname: %s%n", token.getSurName());
    System.out.printf("GivenName: %s%n", token.getGivenName());
    System.out.printf("NickName: %s%n", token.getNickName());

    identities = token.getIdentity();
    for (Token.Identity identity: identities) {
      System.out.printf("   Id: %s%n", identity.getId());
      System.out.printf("   Identifier: %s%n", identity.getIdentifier());
      System.out.printf("   Provider: %s%n", identity.getProvider());
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
