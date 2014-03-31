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

import com.sun.javafx.binding.StringFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * User: servilla
 * Date: 3/28/14
 * Time: 1:17 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.common.security.authorization
 * <p/>
 * <class description>
 */
public class AccessElementTest {

  /* Instance variables */

  private AccessElement mAccessElement;

  /* Class variables */

  private static final String PASTA_AUTHSYSTEM = "https://pasta.lternet.edu/authentication";
  private static final String ACCESS_ORDER = "allowFirst";
  private static final String RULE_1 = "https://pasta.lternet" +
                                           ".edu/authentication - allowFirst " +
                                           "- allow - uid=ucarroll,o=LTER," +
                                           "dc=ecoinformatics," +
                                           "dc=org - changePermission";
  private static final String RULE_2 = "https://pasta.lternet" +
                                           ".edu/authentication - allowFirst " +
                                           "- deny - public - read";


  /* Constructors */

  /* Instance methods */

  @Before
  public void setUp() throws Exception {

    StringBuilder ae = new StringBuilder();
    ae.append("<access ");
    ae.append("system='https://pasta.lternet.edu' ");
    ae.append("order='allowFirst' ");
    ae.append("authSystem='https://pasta.lternet.edu/authentication'>");
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

    mAccessElement = new AccessElement(ae.toString());

  }

  @After
  public void tearDown() throws Exception {

    mAccessElement = null;

  }

  @Test
  public void testGetAuthSystem() throws Exception {

    String authSystem = mAccessElement.getAuthSystem();
    String message = String.format("Expected authentication system \"%s\", " +
                                       "but received \"%s\"",
                                      PASTA_AUTHSYSTEM, authSystem);
    assertTrue(message, PASTA_AUTHSYSTEM.equals(authSystem));

  }

  @Test
  public void testGetAccessOrder() throws Exception {

    String accessOrder = mAccessElement.getAccessOrder();
    String message = String.format("Expected access order \"%s\", " +
                                       "but received \"%s\"", ACCESS_ORDER,
                                      accessOrder);
    assertTrue(message, ACCESS_ORDER.equals(accessOrder));

  }

  @Test
  public void testGetRuleList() throws Exception {

    ArrayList<Rule> rules = mAccessElement.getRuleList();

    String message = String.format("Expected rule size of \"2\", " +
                                       "but received \"%d\"", rules.size());
    assertEquals(message, 2, rules.size());

    message = String.format("Expected rule 1 to be \"%s\", " +
                                "but received \"%s\"", RULE_1,
                               rules.get(0).toString());
    assertTrue(message, RULE_1.equals(rules.get(0).toString()));

    message = String.format("Expected rule 2 to be \"%s\", " +
                                "but received \"%s\"", RULE_2,
                               rules.get(1).toString());
    assertTrue(message, RULE_2.equals(rules.get(1).toString()));

    for (Rule rule: rules) {
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
