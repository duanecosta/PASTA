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

import edu.lternet.pasta.common.security.authentication.jaxb.Token;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * User: servilla
 * Date: 1/16/14
 * Time: 2:32 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authentication
 * <p/>
 * <class description>
 */
public class TokenUtility {

  /* Instance variables */
 
  /* Class variables */

  private static final String jaxbPackage =
      "edu.lternet.pasta.common.security.authentication.jaxb";
 
  /* Constructors */

  /* Instance methods */

  /* Class methods */

  /**
   * Determines if the authentication token has expired based on its TTL.
   *
   * @param token The authentication token
   * @return State of token expiry
   */
  public static boolean hasExpired(Token token) {

    boolean expired = false;

    long now = new Date().getTime();
    long expiry = Long.parseLong(token.getExpires().toString());

    if (now > expiry) expired = true;

    return expired;

  }

  /**
   * Marshalls the authentication token into XML.
   *
   * @param token The authentication token
   * @return Marshalled XML of authentication token
   */
  public static String marshallToken(Token token) {

    String xml = null;

    try {
      JAXBContext jc = JAXBContext.newInstance(jaxbPackage);
      Marshaller m = jc.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      m.marshal(token, baos);
      xml = new String(baos.toByteArray(), "UTF-8");
    }
    catch (JAXBException e) {
      System.err.printf("marshallToken: %s", e.getMessage());
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e) {
      System.err.printf("marshallToken: %s", e.getMessage());
      e.printStackTrace();
    }

    return xml;

  }

  /**
   * Unmarshalls the XML into an authentication token.
   *
   * @param xml The marshalled XML of the authentication token
   * @return The authentication token
   */
  public static Token unmarshalToken(String xml) {

    Token token = null;

    try {
      JAXBContext jc = JAXBContext.newInstance(jaxbPackage);
      Unmarshaller u = jc.createUnmarshaller();
      InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
      token = (Token) u.unmarshal(is);
    }
    catch (JAXBException e) {
      System.err.printf("unmarshalToken: %s", e.getMessage());
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e) {
      System.err.printf("unmarshalToken: %s", e.getMessage());
      e.printStackTrace();
    }

    return token;

  }

}
