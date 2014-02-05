<!--
  ~ Copyright 2011-2013 the University of New Mexico.
  ~
  ~ This work was supported by National Science Foundation Cooperative
  ~ Agreements #DEB-0832652 and #DEB-0936498.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~ http://www.apache.org/licenses/LICENSE-2.0.
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  ~ either express or implied. See the License for the specific
  ~ language governing permissions and limitations under the License.
  -->
<%@ page import="edu.lternet.pasta.portal.DataPackageSurvey" pageEncoding="UTF-8" %>

<%

DataPackageSurvey dps = new DataPackageSurvey();
String[] recent = dps.surveyDataPackages("recent", 4);

String recentScope1 = recent[0];
String recentIdentifier1 = recent[1];
String recentTitle1 = recent[2];
String recentDate1 = recent[3];

String recentScope2 = recent[4];
String recentIdentifier2 = recent[5];
String recentTitle2 = recent[6];
String recentDate2 = recent[7];

String recentScope3 = recent[8];
String recentIdentifier3 = recent[9];
String recentTitle3 = recent[10];
String recentDate3 = recent[11];

String recentScope4 = recent[12];
String recentIdentifier4 = recent[13];
String recentTitle4 = recent[14];
String recentDate4 = recent[15];

%>

	<!-- Divider -->
	<div class="footers row-fluid pull-left distance_1">
		<div class="row-fluid ">
			<div class="span12 page_top_header line-divider">
			</div>
		</div>

	<!-- /Footer -->
    <footer class="row-fluid ">
      <div class="row-fluid">
				<div class="span12">
					<div class="container">
						<div class="row-fluid">
							<div class="span4">
								<div class="widget widget_recent_posts">
									<div class="footer_title">
										<h2 class="widget-title">Recent Inserts</h2>
									</div>
									<dl>
										<dt><a href="./mapbrowse?scope=<%= recentScope1 %>&identifier=<%= recentIdentifier1 %>"><span class="post_icon"></span></a></dt>
										<dd class="without_avatar"><%= recentDate1 %>
										<a href="./mapbrowse?scope=<%= recentScope1 %>&identifier=<%= recentIdentifier1 %>"><%= recentTitle1 %></a> </dd>
									</dl>
									<dl>
										<dt><a href="./mapbrowse?scope=<%= recentScope2 %>&identifier=<%= recentIdentifier2 %>"><span class="post_icon"></span></a></dt>
										<dd class="without_avatar"><%= recentDate2 %>
										<a href="./mapbrowse?scope=<%= recentScope2 %>&identifier=<%= recentIdentifier2 %>"><%= recentTitle2 %></a> </dd>
									</dl>
								</div>
							</div>
							<div class="span6">
								<div class="widget widget_recent_posts">
									<div class="footer_title">
										<h2 class="widget-title">Recent Updates</h2>
									</div>
									<dl>
										<dt><a href="./mapbrowse?scope=<%= recentScope3 %>&identifier=<%= recentIdentifier3 %>"><span class="post_icon"></span></a></dt>
										<dd class="without_avatar"><%= recentDate3%>
										<a href="./mapbrowse?scope=<%= recentScope3 %>&identifier=<%= recentIdentifier3 %>"><%= recentTitle3 %></a> </dd>
									</dl>
									<dl>
										<dt><a href="./mapbrowse?scope=<%= recentScope4 %>&identifier=<%= recentIdentifier4 %>"><span class="post_icon"></span></a></dt>
										<dd class="without_avatar"><%= recentDate4 %>
										<a href="./mapbrowse?scope=<%= recentScope4 %>&identifier=<%= recentIdentifier4 %>"><%= recentTitle4 %></a> </dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
		  </div>
    </footer>
  <!-- /Footer -->
      
    <div class="row-fluid base_color_background footer_copyright">
			<div class="span12">
				<div class="container">
					<span class="arrow row-fluid"><span class="span12"></span>
					</span>
					<div class="row-fluid">
						<div class="span12 ">
							Copyright 2009-2013 <a href="http://www.lternet.edu">Long Term Ecological Research
							Network</a>. This material is based upon work supported 
							by the <a href="http://www.nsf.gov/">National Science 
							Foundation</a> under Cooperative Agreements
							<a href="http://www.fastlane.nsf.gov/servlet/showaward?award=0832652" target="_blank">
							#DEB-0832652</a> and
							<a href="http://www.fastlane.nsf.gov/servlet/showaward?award=0936498" target="_blank">
							#DEB-0936498</a>. Any opinions, findings, conclusions, 
							or recommendations expressed in the material are those 
							of the author(s) and do not necessarily reflect the 
							views of the National Science Foundation. Please
							<a href="http://www.LTERnet.edu/contact" target="_blank">
							contact us</a> with questions, comments, or for technical 
							assistance regarding this web site or the LTER Network.<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
  <!-- /Divider -->
		