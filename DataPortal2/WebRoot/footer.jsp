<!--
  ~ Copyright 2011-2014 the University of New Mexico.
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
String[] recentInserts = dps.surveyDataPackages("recentInserts", 2);
String[] recentUpdates = dps.surveyDataPackages("recentUpdates", 2);

String recentScope1 = recentInserts[0];
String recentIdentifier1 = recentInserts[1];
String recentTitle1 = recentInserts[2];
String recentDate1 = recentInserts[3];

String recentScope2 = recentInserts[4];
String recentIdentifier2 = recentInserts[5];
String recentTitle2 = recentInserts[6];
String recentDate2 = recentInserts[7];

String recentScope3 = recentUpdates[0];
String recentIdentifier3 = recentUpdates[1];
String recentTitle3 = recentUpdates[2];
String recentDate3 = recentUpdates[3];

String recentScope4 = recentUpdates[4];
String recentIdentifier4 = recentUpdates[5];
String recentTitle4 = recentUpdates[6];
String recentDate4 = recentUpdates[7];

String icon1 = "";
String icon2 = "";
String icon3 = "";
String icon4 = "";

String title1 = "";
String title2 = "";
String title3 = "";
String title4 = "";

String date1 = "";
String date2 = "";
String date3 = "";
String date4 = "";

String iconLink = "<a href='./mapbrowse?scope=%s&identifier=%s'><span class='post_icon'></span></a>";
String titleLink = "<a href='./mapbrowse?scope=%s&identifier=%s'>%s</a>";

if (recentScope1 != null && !recentScope1.equals("")) {
  icon1 = String.format(iconLink, recentScope1,  recentIdentifier1);
  title1 = String.format(titleLink, recentScope1, recentIdentifier1, recentTitle1);
  date1 = recentDate1;
}

if (recentScope2 != null && !recentScope2.equals("")) {
  icon2 = String.format(iconLink, recentScope2,  recentIdentifier2);
  title2 = String.format(titleLink, recentScope2, recentIdentifier2, recentTitle2);
  date2 = recentDate2;
}

if (recentScope3 != null && !recentScope3.equals("")) {
  icon3 = String.format(iconLink, recentScope3, recentIdentifier3);
  title3 = String.format(titleLink, recentScope3, recentIdentifier3, recentTitle3);
  date3 = recentDate3;
}

if (recentScope4 != null && !recentScope4.equals("")) {
  icon4 = String.format(iconLink, recentScope4,  recentIdentifier4);
  title4 = String.format(titleLink, recentScope4,  recentIdentifier4, recentTitle4);
  date4 = recentDate4;
}

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
										<h2 class="widget-title">Recently Added</h2>
									</div>
									<dl>
										<dt><%= icon1 %></dt>
										<dd class="without_avatar"><%= date1 %><%= title1 %></dd>
									</dl>
									<dl>
										<dt><%= icon2 %></dt>
										<dd class="without_avatar"><%= date2 %><%= title2 %></dd>
									</dl>
								</div>
							</div>
							<div class="span6">
								<div class="widget widget_recent_posts">
									<div class="footer_title">
										<h2 class="widget-title">Recently Updated</h2>
									</div>
									<dl>
										<dt><%= icon3 %></dt>
										<dd class="without_avatar"><%= date3 %><%= title3 %></dd>
									</dl>
									<dl>
										<dt><%= icon4 %></dt>
										<dd class="without_avatar"><%= date4 %><%= title4 %></dd>
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
							Copyright 2009-2014 <a href="http://www.lternet.edu">Long Term Ecological Research
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
		