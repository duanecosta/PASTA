<?xml version="1.0" encoding="UTF-8"?>

<!-- 
  $Date$
  $Author: mservilla	$
  $Revision$
  
	Copyright 2011,2012 the University of New Mexico.
	
	This work was supported by National Science Foundation Cooperative
	Agreements #DEB-0832652 and #DEB-0936498.
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	http://www.apache.org/licenses/LICENSE-2.0.
	
	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
	either express or implied. See the License for the specific
	language governing permissions and limitations under the License.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="/">

    <div class="section-table">
      <fieldset>
        <legend>PASTA Audit Report</legend>
        <table>
          <tbody>
            <tr>
              <td class="header">Id</td>
              <td class="header">Date/Time</td>
              <td class="header">Status</td>
              <td class="header">Service</td>
              <td class="header">Method</td>
              <td class="header">Code</td>
              <td class="header">Resource</td>
              <td class="header">User</td>
              <td class="header">Group(s)</td>
              <!-- <td class="header">AuthSystem</td> -->
              <td class="header">Message</td>
            </tr>
            <xsl:for-each select="/auditReport/auditRecord">
              <xsl:sort select="./oid" data-type="number" />
              <tr>
                <xsl:apply-templates select="./oid" />
                <xsl:apply-templates select="./entryTime" />
                <xsl:apply-templates select="./category" />
                <xsl:apply-templates select="./service" />
                <xsl:apply-templates select="./serviceMethod" />
                <xsl:apply-templates select="./responseStatus" />
                <xsl:apply-templates select="./resourceId" />
                <xsl:apply-templates select="./user" />
                <xsl:apply-templates select="./groups" />
                <!-- <xsl:apply-templates select="./authSystem" /> -->
                <xsl:apply-templates select="./entryText" />
              </tr>
            </xsl:for-each>
          </tbody>
        </table>
      </fieldset>
    </div>
    <!-- section-table -->

  </xsl:template>
  
  <xsl:template match="oid">
    <td class="data" align="center">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="entryTime">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="category">
    <td class="data" align="center">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="service">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="serviceMethod">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="responseStatus">
    <td class="data" align="center">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="resourceId">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="user">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="groups">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="authSystem">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
  <xsl:template match="entryText">
    <td class="data">
      <xsl:value-of select="."/>
    </td>
  </xsl:template>
  
</xsl:stylesheet>
