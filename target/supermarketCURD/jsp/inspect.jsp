<%--
  Created by IntelliJ IDEA.
  User: me
  Date: 2020/4/13
  Time: 23:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>验证是否为本人操作</title>
    <div class="info">${message}</div>
    <form method="post" action="${pageContext.request.contextPath }/jsp/inspect.do">
        你好！${userSession.userName },我们需要验证是否为您本人<br/>
        现在我们已经给您的邮箱发送了验证信息，收到后请在此处填写。<br/>
        <input name="code" type="text"/><br/>
        <input value="确定" type="submit"/><br/>
    </form>
    <a href="${pageContext.request.contextPath }/login.jsp">退出登录</a>
</head>
<body>

</body>
</html>
