<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
<head>
<title>listing</title>
</head>
<body>
	
<table>
		<tr>
			<th>Statistics About Islam Invitation</th>

		</tr>
		
	</table>
	<table border="1" style="width: 100%">
		<tr>
			<th>The Top 100 tweets about Islam Invitation</th>

		</tr>
		<tr>
		<tr>
			<th>User Name</th>
			<th>Tweet</th>
		</tr>
		
		<c:forEach var="tweet" items="${requestScope.tweets}">
			<tr>
				<td>${tweet.userName}</td>

				<td>${tweet.text}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table border="1" style="width: 100%">

		<tr>
			<th>The Top 5 retweets</th>

		</tr>


		<c:forEach var="retweet" items="${requestScope.retweets}">
			<tr>
				<td>${retweet}</td>

			</tr>
		</c:forEach>
	</table>
	<br />

	<table border="1" style="width: 100%">

		<tr>
			<th>The Top 5 followed</th>

		</tr>


		<c:forEach var="retweet" items="${requestScope.followers}">
			<tr>
				<td>${followers}</td>

			</tr>
		</c:forEach>
	</table>
	<br />

	<table border="1" style="width: 100%">

		<tr>
			<th>The Top 5 mentioned</th>

		</tr>


		<c:forEach var="retweet" items="${requestScope.mentioned}">
			<tr>
				<td>${mentioned}</td>

			</tr>
		</c:forEach>
	</table>

</body>
</html>