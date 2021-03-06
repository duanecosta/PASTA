<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName()
      + ":" + request.getServerPort() + path + "/";
      
  HttpSession httpSession = request.getSession();
        
%>

<!doctype html>
<html>
<head>
<base href="<%=basePath%>">

<title>Data Package Viewer</title>

<link rel="stylesheet" type="text/css" href="./css/lter-nis.css">

<jsp:include page="/WEB-INF/jsp/javascript.jsp" />

</head>

<body>

	<div class="wrapper">

		<jsp:include page="/WEB-INF/jsp/head.jsp" />
		<jsp:include page="/WEB-INF/jsp/menuTopLevel.jsp" />

		<div class="content">

			<h2 align="center">Data Package Viewer</h2>

            <fieldset>
                <legend>View Data Package</legend>

                <p>View data package using the package identifier</p>

                <div class="section">
                    <form id="mapbrowse" name="mapbrowse" method="post"
                        action="./mapbrowse">
                        <table>
                            <tbody>
                                <tr>
                                    <td>
                                       <label for="packageid">PackageId:</label>
                                    </td>
                                    <td>
                                       <input type="text" name="packageid" required="required" size="30" />
                                    </td>
                                    <td>
                                       <input type="submit" name="view" value="view" />
                                       <input type="reset" name="reset" value="reset" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </fieldset>

			<fieldset>
				<legend>View Metadata</legend>

				<p>View data package metadata using the package identifier</p>

				<div class="section">
					<form id="metadataviewer" name="metadataviewer" method="post"
						action="./metadataviewer" target="_blank">
						<table>
							<tbody>
								<tr>
									<td>
									   <label for="packageid">PackageId:</label>
									</td>
									<td>
									   <input type="text" name="packageid" required="required" size="30" />
									</td>
									<td>
									   <input type="submit" name="view" value="view" />
									   <input type="reset" name="reset" value="reset" />
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</fieldset>

			<fieldset>
				<legend>View Quality Report</legend>

				<p>View data package quality report using the package identifier</p>

				<div class="section">
					<form id="reportviewer" name="reportviewer" method="post"
						action="./reportviewer" target="_blank">
						<table>
							<tbody>
								<tr>
                                <td>
                                       <label for="packageid">PackageId:</label>
                                    </td>
                                    <td>
                                       <input type="text" name="packageid" required="required" size="30" />
                                    </td>
                                    <td>
                                       <input type="submit" name="view" value="view" />
                                       <input type="reset" name="reset" value="reset" />
                                    </td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</fieldset>

		</div>
		<!-- end of content -->

		<jsp:include page="/WEB-INF/jsp/foot.jsp" />

	</div>
	<!-- end of wrapper -->

</body>
</html>
