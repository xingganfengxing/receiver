<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@page import="com.letv.cdn.receiver.util.Constants"%>
<%@page import="com.letv.cdn.receiver.util.XMemcacheUtil"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>vod-sequence-stats</title>
</head>
<body>
	<div border="1px" width="100px" height="309px">
		<h4>sequence</h4>
		<span>local_receiver_curr:<%=Constants.receiver_curr%></span>
		<%
		    Object o = XMemcacheUtil.getFromCache(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR);
		    String receiver_curr = null == o ? "-1" : o.toString();
		    Long filter_curr = Constants.filter_curr;
		    String subtraction = String.valueOf(Long.parseLong(receiver_curr) - filter_curr);
		%>
		<span>receiver_curr:<%=receiver_curr%></span>
		<span>filter_curr:<%=filter_curr%></span>
		<span>subtraction:<%=subtraction%></span>

	</div>
</body>
</html>