<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Statistics</title>
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
		<tr>
			<th>User Name</th>
			<th>Tweet</th>
		</tr>

		<c:forEach var="retweet" items="${requestScope.retweets}">
			<tr>
				<td>${retweet.userName}</td>

				<td>${retweet.text}</td>
			</tr>
		</c:forEach>
	</table>
	<br />

	<table border="1" style="width: 100%">

		<tr>
			<th>The Top 5 followed</th>

		</tr>
		<tr>
			<th>User Name</th>
			<th>Tweet</th>
		</tr>


		<c:forEach var="followers" items="${requestScope.followers}">
			<tr>
				<td>${followers.userName}</td>

				<td>${followers.text}</td>
			</tr>
		</c:forEach>
	</table>
	<br />

	<table border="1" style="width: 100%">

		<tr>
			<th>The Top 5 mentioned</th>

		</tr>
		<tr>
			<th>User Name</th>
			<th>Tweet</th>
		</tr>

		<c:forEach var="mentioned" items="${requestScope.mentioned}">
			<tr>
				<td>${mentioned.userName}</td>

				<td>${mentioned.text}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>