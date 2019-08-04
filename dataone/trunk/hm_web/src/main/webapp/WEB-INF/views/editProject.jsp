<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	require([ "dojo/parser", "dijit/form/DateTextBox", "dijit/form/Button" ]);
</script>

<form:form method="post" commandName="project">
	<h1>Edit : ${project.cpProjectName}</h1>
	<table width="900">
		<tr>
			<td>
				<table width="500" height="374">
					<tr>
						<td><form:label path="cpProjectCod">
								Project Code:
							</form:label></td>
						<form:hidden path="hiddenCpProjectCod" />
						<td><c:choose>
								<c:when test="${empty project.hiddenCpProjectCod}">
									<form:input path="cpProjectCod" />
								</c:when>
								<c:otherwise>
									<form:hidden path="cpProjectCod" />
									${project.cpProjectCod}
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td><form:label path="cpProjectName">
								Project Name:
							</form:label></td>
						<td><form:input path="cpProjectName" /></td>
					</tr>
					<tr>
						<td><form:label path="cpCategory">
								Project Category:
							</form:label>
						</td>
						<td>
							<form:select path="cpCategory" size="1">
									<form:options items="${pojCategoryList}" />
							</form:select>
						</td>
					</tr>
					<tr>
						<td><form:label path="cpStartDate">
								Start Date:
							</form:label></td>
						<td><form:input path="cpStartDate" data-dojo-type="dijit/form/DateTextBox" required="true" /></td>
					</tr>
					<tr>
						<td><form:label path="cpEndDate">
								End Date:
							</form:label></td>
						<td><form:input path="cpEndDate" data-dojo-type="dijit/form/DateTextBox" required="true" /></td>
					</tr>
					<tr>
						<td><form:label path="cpOwner">
							Owner:
							</form:label></td>
						<td><form:input path="cpOwner" /></td>
					</tr>
					<tr>
						<td><form:label path="cpSonum">
							SO Number:
							</form:label></td>
						<td><form:input path="cpSonum" /></td>
					</tr>
					<tr>
						<td><form:label path="cpRemarker">
							Remark:
							</form:label></td>
						<td><form:textarea path="cpRemarker" /></td>
					</tr>
					<tr>
						<td><form:label path="cpStatus">
							Status:
							</form:label></td>
						<td><form:select path="cpStatus" size="1">
								<form:options items="${activeInactiveList}" />
							</form:select></td>
					</tr>
					<c:if test="${not empty project.errMsg}">
						<tr>
							<td></td>
							<td>
								<div class="msg error">${project.errMsg}</div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td></td>
						<td>
							<button data-dojo-type="dijit/form/Button" type="submit">Commit</button>
							<button data-dojo-type="dijit/form/Button" type="button">
								Cancel
								<script type="dojo/connect" data-dojo-event="onClick">
  						  			window.location = "../admin/projects.do";
 					 			</script>
							</button>
						</td>
					</tr>
				</table>
			</td>
			<td>
				Project Code Syntax<p/>
				Year(2)-Client(5)-Project(8)-Project Type(2)-suffix(2)<p/>
				Example<br/>
				13-OGCIO-EPS-PM-01<p/><p/>
				<table width="350">
					<tr>
						<td colspan="2">Project Type Lookup</td>
					</tr>
					<tr>
						<td>Internal / Admin</td>
						<td>IN</td>
					</tr>
					<tr>
						<td>Maintenance</td>
						<td>MA</td>
					</tr>
					<tr>
						<td>Pre-Sale</td>
						<td>PR</td>
					</tr>
					<tr>
						<td>Professional Service</td>
						<td>PS</td>
					</tr>
					<tr>
						<td>Project</td>
						<td>PM</td>
					</tr>
					<tr>
						<td>R & D</td>
						<td>RD</td>
					</tr>
					<tr>
						<td>Training</td>
						<td>TR</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</form:form>