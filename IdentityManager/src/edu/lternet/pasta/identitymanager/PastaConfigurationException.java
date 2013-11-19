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

package edu.lternet.pasta.identitymanager;

/**
 * User: servilla
 * Date: 11/18/13
 * Time: 7:44 PM
 * <p/>
 * Project: NIS
 * Package: edu.lternet.pasta.identitymanager
 * <p/>
 * <class description>
 */
public class PastaConfigurationException extends Exception {

  /*
   * Constructors
   */

  /**
   * Pasta Configuration Exception.
   */
  public PastaConfigurationException() {

  }

  /**
   * Pasta Configuration Exception.
   *
   * @param gripe
   *          The cause of the exception in natural language text as a String
   *          object.
   */
  public PastaConfigurationException(String gripe) {
    super(gripe);
  }

}
