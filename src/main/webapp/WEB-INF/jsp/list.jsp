<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2017/11/8
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>列表页</title>
</head>
<body>
<div class="tab-panel">
    <div class="container">
        <table class="table table-striped table-bordered table-hover">
            <thead>
                <tr class="info">
                    <th>ID</th>
                    <th>名称</th>
                    <th>创建时间</th>
                    <th>开始时间</th>
                    <th>创建时间</th>
                    <th>数量</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${list}" var="seckill">
                    <tr>
                        <td>${seckill.seckillId}</td>
                        <td>${seckill.name}</td>
                        <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.createTime}" /></td>
                        <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.startTime}" /></td>
                        <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.endTime}" /></td>
                        <td>${seckill.number}</td>
                        <td><a class="btn btn-success">详情</a></td>

                    </tr>
                </c:forEach>ßßß
            </tbody>
        </table>
    </div>
</div>

</body>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
