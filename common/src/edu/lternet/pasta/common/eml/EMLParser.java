/*
 *
 * $Date: 2012-04-02 11:10:19 -0700 (Mon, 02 Apr 2012) $
 * $Author: dcosta $
 * $Revision: $
 *
 * Copyright 2011,2012 the University of New Mexico.
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

package edu.lternet.pasta.common.eml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import edu.lternet.pasta.common.XmlUtility;
import edu.lternet.pasta.common.eml.Entity.EntityType;


/*
 * This class parses EML metadata for values needed by the DAS, such
 * as entity names, email addresses, and data URLs.
 */
public class EMLParser {

  /*
   * Class fields
   */

  private static final Logger logger = Logger.getLogger(EMLParser.class);

  // constants
  public static final String CREATOR_PATH = "//eml/dataset/creator";
  public static final String ENTITY_PATH_PARENT = "//dataset/";
  public static final String PUB_DATE_PATH = "//eml/dataset/pubDate";
  public static final String TITLE_PATH = "//dataset/title";
  public static final String OTHER_ENTITY = "otherEntity";
  public static final String TABLE_ENTITY = "dataTable";
  public static final String SPATIAL_RASTER_ENTITY = "spatialRaster";
  public static final String SPATIAL_VECTOR_ENTITY = "spatialVector";
  public static final String STORED_PROCEDURE_ENTITY = 
                                                  "storedProcedure";
  public static final String VIEW_ENTITY = "view";
  private static final String PACKAGE_ID_PATH = "//eml/@packageId";
  
  private static final String ENTITY_NAME = "entityName";
  private static final String OBJECT_NAME = "physical/objectName";
  private static final String ONLINE_URL = "physical/distribution/online/url";
  
  String[] ENTITY_TYPES = 
  {
      OTHER_ENTITY,
      SPATIAL_RASTER_ENTITY,
      SPATIAL_VECTOR_ENTITY,
      STORED_PROCEDURE_ENTITY,
      TABLE_ENTITY,
      VIEW_ENTITY
  };
  
  /*
   * Instance fields
   */
  
  DataPackage dataPackage = null;
  
  
  /*
   * Constructors
   */
  
  
  /*
   * Class methods
   */
  
  
  /*
   * Instance methods
   */
  
  /**
   * Parses an EML document.
   * 
   * @param   xml          The XML string representation of the EML document
   * @return  dataPackage  a DataPackage object holding parsed values
   */
  public DataPackage parseDocument(String xml) {
    DataPackage dataPackage = null;
    
    if (xml != null) {
      try {
        InputStream inputStream = IOUtils.toInputStream(xml, "UTF-8");
        dataPackage = parseDocument(inputStream);
      }
      catch (Exception e) {
        logger.error("Error parsing EML metacdata: " + e.getMessage());
      }
    }
    
    return dataPackage;
  }
  
  
  /**
   * Parses an EML document.
   * 
   * @param   inputStream          the input stream to the EML document
   * @return  dataPackage          a DataPackage object holding parsed values
   */
  public DataPackage parseDocument(InputStream inputStream) 
          throws ParserConfigurationException {
    
    this.dataPackage = new DataPackage();
    ArrayList<Entity> entityList = dataPackage.getEntityList();
    
    DocumentBuilder documentBuilder = 
              DocumentBuilderFactory.newInstance().newDocumentBuilder();
    CachedXPathAPI xpathapi = new CachedXPathAPI();

    Document document = null;

    try {
      document = documentBuilder.parse(inputStream);
      
      if (document != null) {
        // process packageId
        Node packageIdNode = null;
        packageIdNode = xpathapi.selectSingleNode(document, PACKAGE_ID_PATH);

        if (packageIdNode != null) {
          String packageId = packageIdNode.getNodeValue();
          this.dataPackage.setPackageId(packageId);
        }
        
        // Parse the title nodes
        NodeList titleNodeList = xpathapi.selectNodeList(document, TITLE_PATH);
        for (int i = 0; i < titleNodeList.getLength(); i++) {
          String title = titleNodeList.item(i).getTextContent();
          title = XmlUtility.xmlEncode(title);
          dataPackage.titles.add(title);
        }

        // Parse the creator nodes
        NodeList creatorNodeList = xpathapi.selectNodeList(document, CREATOR_PATH);
        if (creatorNodeList != null) {
          for (int i =0; i < creatorNodeList.getLength(); i++) {
            Node creatorNode = creatorNodeList.item(i);
            ResponsibleParty rp = new ResponsibleParty("creator");
            parseResponsibleParty(creatorNode, rp);
            dataPackage.addCreator(rp);
          }
        }

        // Parse the pubDate node
        Node pubDateNode = xpathapi.selectSingleNode(document, PUB_DATE_PATH);
        if (pubDateNode != null) {
          String pubDate = pubDateNode.getTextContent();
          this.dataPackage.setPubDate(pubDate);
        }

        for (int j = 0; j < ENTITY_TYPES.length; j++) {
          String elementName = ENTITY_TYPES[j];
          String elementPath = ENTITY_PATH_PARENT + elementName;
          EntityType entityType = Entity.entityTypeFromElementName(elementName);
      
          // Parse the entity name
          NodeList entityNodeList = 
                                xpathapi.selectNodeList(document, elementPath);
      
          if (entityNodeList != null) {
            for (int i = 0; i < entityNodeList.getLength(); i++) {
              Entity entity = new Entity();
              entity.setEntityType(entityType);
              Node entityNode = entityNodeList.item(i);
          
              // get the entityName
              NodeList entityNameNodeList = xpathapi.selectNodeList(
                                          entityNode,
                                          ENTITY_NAME
                                                                 );
          
              if (entityNameNodeList != null && entityNameNodeList.getLength() > 0) {
                String entityName = entityNameNodeList.item(0).getTextContent();
                entity.setName(entityName);
              }
          
          
              // get the objectName
              NodeList objectNameNodeList = xpathapi.selectNodeList(
                                          entityNode,
                                          OBJECT_NAME
                                                                 );
          
              if (objectNameNodeList != null && objectNameNodeList.getLength() > 0) {
                String objectName = objectNameNodeList.item(0).getTextContent();
                entity.setObjectName(objectName);
              }
          
              // get the distribution information
              NodeList urlNodeList = xpathapi.selectNodeList(
                                             entityNode,
                                             ONLINE_URL
                                 );
          
              if (urlNodeList != null && urlNodeList.getLength() > 0) {
                String url = urlNodeList.item(0).getTextContent();
                entity.setUrl(url);
              }
          
              entityList.add(entity);
            }
          } 
        } 
      }
    }
    catch (SAXException e) {
      logger.error("Error parsing document: SAXException");
      e.printStackTrace();
    } 
    catch (IOException e) {
      logger.error("Error parsing document: IOException");
      e.printStackTrace();
    }
    catch (TransformerException e) {
      logger.error("Error parsing document: TransformerException");
      e.printStackTrace();
    }

    return this.dataPackage;
  }
  
  
  private void parseResponsibleParty(Node node, ResponsibleParty rp) {
    if (node instanceof Element) {
      Element element = (Element) node;
      String elementTagName = element.getTagName();
      
      if (elementTagName.equals("contact") ||
          elementTagName.equals("creator") ||
          elementTagName.equals("metadataProvider")) {
        NodeList nodeList = element.getChildNodes();
        
        for (int i = 0; i < nodeList.getLength(); i++) {
          Node rpNode = nodeList.item(i);
          
          if (rpNode instanceof Element) {
            Element rpElement = (Element) rpNode;
            String rpElementTagName = rpElement.getTagName();
            String rpElementAttribute = rpElement.getAttribute("phonetype");
            
            if (rpElementTagName.equals("individualName")) {
              NodeList individualNameNodeList = rpElement.getChildNodes();
              
              for (int j = 0; j < individualNameNodeList.getLength(); j++) {
                Node individualNameNode = individualNameNodeList.item(j);
                
                if (individualNameNode instanceof Element) {
                  Element individualNameElement = (Element) individualNameNode;
                  String individualNameElementTagName = individualNameElement.getTagName();
                  
                  if (individualNameElementTagName.equals("salutation")) {
                    NodeList salutationNodeList = individualNameElement.getChildNodes();
                    
                    for (int k = 0; k < salutationNodeList.getLength(); k++) {
                      Node salutationNode = salutationNodeList.item(k);
                      
                      if (salutationNode instanceof Text) {
                        Text salutationText = (Text) salutationNode;
                        String salutationNodeValue = salutationText.getNodeValue();
                        logger.debug("salutationNodeValue: " + salutationNodeValue);
                        rp.setSalutation(salutationNodeValue);
                      }
                    }
                  }
                  else if (individualNameElementTagName.equals("givenName")) {
                    NodeList givenNameNodeList = individualNameElement.getChildNodes();
                    
                    for (int l = 0; l < givenNameNodeList.getLength(); l++) {
                      Node givenNameNode = givenNameNodeList.item(l);
                      
                      if (givenNameNode instanceof Text) {
                        Text givenNameText = (Text) givenNameNode;
                        String givenNameNodeValue = givenNameText.getNodeValue();
                        logger.debug("givenNameNodeValue: " + givenNameNodeValue);
                        rp.addGivenName(givenNameNodeValue);
                      }
                    }
                  }
                  else if (individualNameElementTagName.equals("surName")) {
                    NodeList surNameNodeList = individualNameElement.getChildNodes();
                    
                    for (int m = 0; m < surNameNodeList.getLength(); m++) {
                      Node surNameNode = surNameNodeList.item(m);
                      
                      if (surNameNode instanceof Text) {
                        Text surNameText = (Text) surNameNode;
                        String surNameNodeValue = surNameText.getNodeValue();
                        logger.debug("surNameNodeValue: " + surNameNodeValue);
                        rp.setSurName(surNameNodeValue);
                      }
                    }
                  }
                }
              }
            }
            else if (rpElementTagName.equals("organizationName")) {
              NodeList organizationNameNodeList = rpElement.getChildNodes();
              
              for (int n = 0; n < organizationNameNodeList.getLength(); n++) {
                Node organizationNameNode = organizationNameNodeList.item(n);
                
                if (organizationNameNode instanceof Text) {
                  Text organizationNameText = (Text) organizationNameNode;
                  String organizationNameNodeValue = organizationNameText.getNodeValue();
                  logger.debug("organizationNameNodeValue: " + organizationNameNodeValue);
                  rp.setOrganizationName(organizationNameNodeValue);
                }
              }
            }
            else if (rpElementTagName.equals("positionName")) {
              NodeList positionNameNodeList = rpElement.getChildNodes();
              
              for (int n = 0; n < positionNameNodeList.getLength(); n++) {
                Node positionNameNode = positionNameNodeList.item(n);
                
                if (positionNameNode instanceof Text) {
                  Text positionNameText = (Text) positionNameNode;
                  String positionNameNodeValue = positionNameText.getNodeValue();
                  logger.debug("positionNameNodeValue: " + positionNameNodeValue);
                  rp.setPositionName(positionNameNodeValue);
                }
              }
            }
            else if (rpElementTagName.equals("address")) {
              NodeList addressNodeList = rpElement.getChildNodes();
              
              for (int o = 0; o < addressNodeList.getLength(); o++) {
                Node addressNode = addressNodeList.item(o);
                
                if (addressNode instanceof Element) {
                  Element addressElement = (Element) addressNode;
                  String addressElementTagName = addressElement.getTagName();
                  
                  if (addressElementTagName.equals("deliveryPoint")) {
                    NodeList deliveryPointNodeList = addressElement.getChildNodes();
                    
                    for (int p = 0; p < deliveryPointNodeList.getLength(); p++) {
                      Node deliveryPointNode = deliveryPointNodeList.item(p);
                      
                      if (deliveryPointNode instanceof Text) {
                        Text deliveryPointText = (Text) deliveryPointNode;
                        String deliveryPointNodeValue = deliveryPointText.getNodeValue();
                        logger.debug("deliveryPointNodeValue: " + deliveryPointNodeValue);
                        rp.addDeliveryPoint(deliveryPointNodeValue);
                      }
                    }
                  }
                  else if (addressElementTagName.equals("city")) {
                    NodeList cityNodeList = addressElement.getChildNodes();
                    
                    for (int q = 0; q < cityNodeList.getLength(); q++) {
                      Node cityNode = cityNodeList.item(q);
                      
                      if (cityNode instanceof Text) {
                        Text cityText = (Text) cityNode;
                        String cityNodeValue = cityText.getNodeValue();
                        logger.debug("cityNodeValue: " + cityNodeValue);
                        rp.setCity(cityNodeValue);
                      }
                    }
                  }
                  else if (addressElementTagName.equals("administrativeArea")) {
                    NodeList administrativeAreaNodeList = addressElement.getChildNodes();
                    
                    for (int r = 0; r < administrativeAreaNodeList.getLength(); r++) {
                      Node administrativeAreaNode = administrativeAreaNodeList.item(r);
                      
                      if (administrativeAreaNode instanceof Text) {
                        Text administrativeAreaText = (Text) administrativeAreaNode;
                        String administrativeAreaNodeValue = administrativeAreaText.getNodeValue();
                        logger.debug("administrativeAreaNodeValue: " + administrativeAreaNodeValue);
                        rp.setAdministrativeArea(administrativeAreaNodeValue);
                      }
                    }
                  }
                  else if (addressElementTagName.equals("postalCode")) {
                    NodeList postalCodeNodeList = addressElement.getChildNodes();
                    
                    for (int r = 0; r < postalCodeNodeList.getLength(); r++) {
                      Node postalCodeNode = postalCodeNodeList.item(r);
                      
                      if (postalCodeNode instanceof Text) {
                        Text postalCodeText = (Text) postalCodeNode;
                        String postalCodeNodeValue = postalCodeText.getNodeValue();
                        logger.debug("postalCodeNodeValue: " + postalCodeNodeValue);
                        rp.setPostalCode(postalCodeNodeValue);
                      }
                    }
                  }
                  else if (addressElementTagName.equals("country")) {
                    NodeList countryNodeList = addressElement.getChildNodes();
                    
                    for (int s = 0; s < countryNodeList.getLength(); s++) {
                      Node countryNode = countryNodeList.item(s);
                      
                      if (countryNode instanceof Text) {
                        Text countryText = (Text) countryNode;
                        String countryNodeValue = countryText.getNodeValue();
                        logger.debug("countryNodeValue: " + countryNodeValue);
                        rp.setCountry(countryNodeValue);
                      }
                    }
                  }
                }
              }
            }
            else if (rpElementTagName.equals("phone") &&
                     !(rpElementAttribute.equals("facsimile") ||
                     rpElementAttribute.equals("fax"))) {
              
              NodeList phoneNodeList = rpElement.getChildNodes();
              
              for (int t = 0; t < phoneNodeList.getLength(); t++) {
                Node phoneNode = phoneNodeList.item(t);
                
                if (phoneNode instanceof Text) {
                  Text phoneText = (Text) phoneNode;
                  String phoneNodeValue = phoneText.getNodeValue();
                  logger.debug("phoneNodeValue: " + phoneNodeValue);
                  rp.setPhone(phoneNodeValue);
                }
              }
            }
            else if (rpElementTagName.equals("electronicMailAddress")) {
              NodeList electronicMailAddressNodeList = rpElement.getChildNodes();
              
              for (int u = 0; u < electronicMailAddressNodeList.getLength(); u++) {
                Node electronicMailAddressNode = electronicMailAddressNodeList.item(u);
                
                if (electronicMailAddressNode instanceof Text) {
                  Text electronicMailAddressText = (Text) electronicMailAddressNode;
                  String electronicMailAddressNodeValue = electronicMailAddressText.getNodeValue();
                  logger.debug("electronicMailAddressNodeValue: " + electronicMailAddressNodeValue);
                  rp.setElectronicMailAddress(electronicMailAddressNodeValue);
                }
              }
            }
            else if (rpElementTagName.equals("onlineUrl")) {
              NodeList onlineUrlNodeList = rpElement.getChildNodes();
              
              for (int u = 0; u < onlineUrlNodeList.getLength(); u++) {
                Node onlineUrlNode = onlineUrlNodeList.item(u);
                
                if (onlineUrlNode instanceof Text) {
                  Text onlineUrlText = (Text) onlineUrlNode;
                  String onlineUrlNodeValue = onlineUrlText.getNodeValue();
                  logger.debug("onlineUrlNodeValue: " + onlineUrlNodeValue);
                  rp.setOnlineUrl(onlineUrlNodeValue);
                }
              }
            }
          }
        }
      }
    }
  }
  
}
