<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Send WNS message</title>
</head>
<body>
<form name="messageForm" method="POST" action="send">
	<input type="hidden" name="appName" value="WNS sample">
	<select name="type">
		<option value="wns/toast">Toast</option>
		<option value="wns/badge">Badge</option>
		<option value="wns/tile">Tile</option>
		<option value="wns/raw">Raw</option>
	</select>
	<input type="text" name="message"><br/>
	<input type="submit" value="보내기">
</form>
</body>
</html>