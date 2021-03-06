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

package edu.lternet.pasta.identitymanager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * User: servilla
 * Date: 11/18/13
 * Time: 11:15 AM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class ConfigurationListener implements ServletContextListener {

  /* Instance variables */
 
  /* Class variables */

  private static final Logger logger =
      Logger.getLogger(edu.lternet.pasta.identitymanager.ConfigurationListener.class);

  private static PropertiesConfiguration config = null;

  /* Constructors */

  /* Instance methods */

  /*
   * (non-Javadoc)
   *
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
   * ServletContextEvent)
   */
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {

  }

  /*
   * (non-Javadoc)
   *
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
   * ServletContextEvent)
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();

    // Create an absolute path for accessing configuration properties
    String cwd = servletContext.getRealPath(".");

    // Initialize log4j
    String log4jPropertiesPath = cwd + "/WEB-INF/conf/log4j.properties";
    PropertyConfigurator.configureAndWatch(log4jPropertiesPath);

    // Initialize commons configuration
    String appConfigPath = cwd + "/WEB-INF/conf/identity.properties";
    try {
      config = new PropertiesConfiguration(appConfigPath);
      config.setProperty("system.cwd", cwd);
      config.save();
    } catch (ConfigurationException e) {
      logger.error(e);
      e.printStackTrace();
    }

  }

  /* Class methods */

  /**
   * Configure logger and properties applications for non-servlet based
   * execution (e.g., main(String[] args)).
   */
  public static void configure() {

    String cwd = System.getProperty("user.dir");

    // Initialize log4j
    String log4jPropertiesPath = cwd + "/WebRoot/WEB-INF/conf/log4j.properties";
    PropertyConfigurator.configureAndWatch(log4jPropertiesPath);

    // Initialize commons configuration
    String appConfigPath = cwd + "/WebRoot/WEB-INF/conf/identity.properties";
    try {
      config = new PropertiesConfiguration(appConfigPath);
      config.setProperty("system.cwd", cwd);
      config.save();
    } catch (ConfigurationException e) {
      logger.error(e);
      e.printStackTrace();
    }

  }

  /**
   * Return the Configuration object to obtain property options.
   *
   * @return The Configuration object.
   */
  public static PropertiesConfiguration getOptions() {

    PropertiesConfiguration options = config;
    return options;

  }

}
