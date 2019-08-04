<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
	require([ "dojo/parser", "dijit/form/Button" ]);
</script>
<button data-dojo-type="dijit/form/Button" type="button">
	Add
	<script type="dojo/connect" data-dojo-event="onClick">
    window.location = "editProject.do";
  </script>
</button>
<table class="data">
	<tr>
		<th>Code</th>
		<th>Name</th>
		<th>Category</th>
		<th>From</th>
		<th>To</th>
		<th>Owner</th>
		<th>SO Number</th>
		<th>Remark</th>
		<th>Created Date</th>
		<th>Status</th>
		<th>Action</th>
	</tr>
	<c:if test="${!empty projectList}">
		<c:forEach items="${projectList}" var="project">
			<tr>
				<td>${project.cpProjectCod}</td>
				<td>${project.cpProjectName}</td>
				<td>${project.cpCategory}</td>
				<td>${project.cpStartDate}</td>
				<td>${project.cpEndDate}</td>
				<td>${project.cpOwner}</td>
				<td>${project.cpSonum}</td>
				<td>${project.cpRemarker}</td>
				<td>${project.cpCreateBy} - ${project.cpCreateDate}</td>
				<td>${project.cpStatus}</td>
				<td><a href="editProject.do?projCod=${project.encodedCpProjectCod}">Edit</a></td>
			</tr>
		</c:forEach>
	</c:if>
</table>
