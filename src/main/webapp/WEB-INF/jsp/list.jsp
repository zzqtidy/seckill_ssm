<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2017/11/8
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<%@ include file="common/tag.jsp" %>
<head>
    <title>列表页</title>
    <%@ include file="common/header.jsp" %>
</head>
<body>
<div class="tab-panel">
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading text-center">
                <h2>秒杀列表</h2>
            </div>
            <div class="panel-body">
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
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.createTime}"/></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.startTime}"/></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seckill.endTime}"/></td>
                            <td>${seckill.number}</td>
                            <td><a class="btn btn-info" href="/seckill/${seckill.seckillId}/detail" target="_blank">详情</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

</body>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
