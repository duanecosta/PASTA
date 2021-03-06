/*
 *
 * $Date$
 * $Author$
 * $Revision$
 *
 * Copyright 2010 the University of New Mexico.
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
 *
 */

package edu.lternet.pasta.eventmanager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.lternet.pasta.common.EmlPackageId;
import edu.lternet.pasta.common.FileUtility;
import edu.lternet.pasta.common.XmlParsingException;
import edu.lternet.pasta.datapackagemanager.ConfigurationListener;
import edu.lternet.pasta.eventmanager.EmlSubscription;
import edu.lternet.pasta.eventmanager.SubscribedUrl;
import edu.lternet.pasta.eventmanager.XmlSubscriptionFormatV1;

public class TestXmlSubscriptionFormatV1 {
	
	/*
	 * Class variables
	 */
    
    public static final String HOME = 
        "test/edu/lternet/pasta/eventmanager/META-INF";
    
    private static ConfigurationListener configurationListener = null;
    private static final String dirPath = "WebRoot/WEB-INF/conf";

    public static String makeFileName(String fileName) {
        return new File(HOME, fileName).getAbsolutePath();
    }
    
    
    /*
     * Instance variables
     */
    
    private String goodFile;
    private String badFile;
    private XmlSubscriptionFormatV1 formatter;
    
    
    /*
     * Class methods
     */
  
    
    /**
     * Initialize objects before any tests are run.
     */
    @BeforeClass
    public static void setUpClass() {
      configurationListener = new ConfigurationListener();
      configurationListener.initialize(dirPath);
    }
    
    
    /*
     * Instance methods
     */
    
    @Before
    public void init() {
        goodFile = makeFileName("good_eml_subscription.xml");
        badFile = makeFileName("bad_eml_subscription.xml");
        formatter = new XmlSubscriptionFormatV1();
    }
    
    @Test
    public void testParseWithGoodXml() {
        
        String xml = FileUtility.fileToString(goodFile);
        
        EmlSubscription emlSubscription = formatter.parse(xml);
        EmlPackageId epi = emlSubscription.getPackageId();
        
        assertNull(emlSubscription.getCreator());
        assertEquals("lter-lno", epi.getScope());
        assertEquals(12, epi.getIdentifier().intValue());
        assertEquals(74, epi.getRevision().intValue());
        assertEquals("http://foo?bar&blah", emlSubscription.getUrl().toString());
    }
    
    @Test(expected=XmlParsingException.class)
    public void testParseWithBadXml() {
        String xml = FileUtility.fileToString(badFile);
        formatter.parse(xml);
    }
    
    private EmlSubscription makeSubscription() {
        
        EmlSubscription sb = new EmlSubscription();
        sb.setCreator("junit");
        sb.setPackageId(new EmlPackageId("lter-lno", 12, 74));
        sb.setUrl(new SubscribedUrl("http://foo?bar&blah").toString()); // with &
        
        return sb;
    }
    
    @Test
    public void testFormatSingle() {
        
        String xml = formatter.format(makeSubscription());

        assertTrue(xml.contains("<creator>junit</creator>"));
        assertTrue(xml.contains("<packageId>lter-lno.12.74</packageId>"));        
        assertTrue(xml.contains("<url>http://foo?bar&amp;blah</url>"));        
        assertFalse(xml.contains("<id>"));
    }
    
    @Test
    public void testFormatCollection() {
        
        Collection<EmlSubscription> c = Collections.singleton(makeSubscription());
        
        String xml = formatter.format(c);
        
        assertTrue(xml.contains("<subscriptions>"));
        assertTrue(xml.contains("<creator>junit</creator>"));
        assertTrue(xml.contains("<packageId>lter-lno.12.74</packageId>"));        
        assertTrue(xml.contains("<url>http://foo?bar&amp;blah</url>"));        
        assertFalse(xml.contains("<id>"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatSingleWithInactive() {
        EmlSubscription s = makeSubscription();
        s.inactivate();
        formatter.format(s);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFormatCollectionWithInactive() {
        
        EmlSubscription s = makeSubscription();
        s.inactivate();
        
        Collection<EmlSubscription> c = Collections.singleton(s);
        formatter.format(c);
    }
}
