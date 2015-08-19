<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@page
	import="com.letv.cdn.receiver.servlet.support.AbstractBaseLiveMultipleServlet"%>
<%@page import="com.letv.cdn.receiver.servlet.HlsSSServlet"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>thread-stats</title>
</head>
<body>
	<div border="1px" width="100px" height="309px">
		<h4>daemonExecutor</h4>
		<span>currPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonExecutor.getPoolSize()%></span>
		<span>activeCount:<%=AbstractBaseLiveMultipleServlet.daemonExecutor.getActiveCount()%></span>
		<span>largestPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonExecutor.getLargestPoolSize()%></span>
		<span>maximumPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonExecutor.getMaximumPoolSize()%></span>
		<span>currQueueSize:<%=AbstractBaseLiveMultipleServlet.daemonExecutor.getQueue().size()%></span>
	</div>
	<div border="1px" width="100px" height="309px">
		<h4>daemonMemcachedExecutor</h4>
		<span>currPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonMemcachedExecutor.getPoolSize()%></span>
		<span>activeCount:<%=AbstractBaseLiveMultipleServlet.daemonMemcachedExecutor.getActiveCount()%></span>
		<span>largestPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonMemcachedExecutor.getLargestPoolSize()%></span>
		<span>maximumPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonMemcachedExecutor.getMaximumPoolSize()%></span>
		<span>currQueueSize:<%=AbstractBaseLiveMultipleServlet.daemonMemcachedExecutor.getQueue().size()%></span>
	</div>
	<div border="1px" width="100px" height="309px">
		<h4>daemonKafkaExecutor</h4>
		<span>currPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonKafkaExecutor.getPoolSize()%></span>
		<span>activeCount:<%=AbstractBaseLiveMultipleServlet.daemonKafkaExecutor.getActiveCount()%></span>
		<span>largestPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonKafkaExecutor.getLargestPoolSize()%></span>
		<span>maximumPoolSize:<%=AbstractBaseLiveMultipleServlet.daemonKafkaExecutor.getMaximumPoolSize()%></span>
		<span>currQueueSize:<%=AbstractBaseLiveMultipleServlet.daemonKafkaExecutor.getQueue().size()%></span>
	</div>
	<div border="1px" width="100px" height="309px">
		<h4>HLS_LRUCache</h4>
		<span>currSize:<%=HlsSSServlet.getLruSize()%></span>
	</div>
</body>
</html>