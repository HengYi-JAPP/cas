<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.net.Socket" %>
<%
    String employeeid = request.getParameter("uid");
    if (employeeid == null) {
        response.sendRedirect("./casNull.jsp");
        return;
    }
    String adminpassword = request.getParameter("adminpassword");
    String username = request.getParameter("username");
    String username2 = request.getParameter("username2");
    String newpassword = request.getParameter("newpassword");
    if (employeeid != null & adminpassword != null & newpassword != null & username != null & username2 != null) {
        if (employeeid.equals("")) {
            response.sendRedirect("./casNull.jsp");
            return;
        }
        if (!employeeid.equals("") & !adminpassword.equals("") & !newpassword.equals("") & !username.equals("") & !username2.equals("")) {

            String result = "0";
            try {
                Socket socket = new Socket("127.0.0.1", new Integer(8722).intValue());
                PrintWriter pwr = new PrintWriter(socket.getOutputStream());
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                BufferedReader read = new BufferedReader(in);
                result = read.readLine();
                String command = "createoauser " + employeeid + " " + adminpassword + " " + username + " " + username2 + " " + newpassword;
                pwr.print(command + "\n\r");
                pwr.flush();
                result = read.readLine();
                read.close();
                pwr.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                result = "1";
            }
            String msg = " <script language=javascript> alert('create oauser failure');</script> ";
            if (result.equals("0")) {

                msg = "<script language=javascript>alert('create oauser succeed')</script> ";
            }
            out.println(msg);
        }
    }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>用户密码重置</title>
<body>
<form name="resetpwd" action="./casNewOAUser.jsp" method="post">
    <input name="uid" type="hidden" value="<%=employeeid%>"><br>
    管理员密码:<input name="adminpassword" type="password" value=""><br>
    OA用户名: <input name="username" type="text" value=""><br>
    用户姓名: <input name="username2" type="text" value=""><br>
    新密码: <input name="newpassword" type="password" value=""><br>
    <input type=button value="提交" onclick="submitForm();"> <input type=button value="重置" onclick="clearInput();">

</form>
</body>
</html>

<script type="text/javascript">
    function clearInput() {
        document.resetpwd.adminpassword.value = "";
        document.resetpwd.newpassword.value = "";
        document.resetpwd.username.value = "";
        document.resetpwd.username2.value = "";
    }

    function submitForm() {
        if (document.resetpwd.adminpassword.value == "") {
            alert("管理员密码为空");
            return;
        }
        if (document.resetpwd.newpassword.value == "") {
            alert("用户密码不得为空");
            return;
        }
        if (document.resetpwd.username.value == "") {
            alert("用户名不得为空");
            return;
        }
        if (document.resetpwd.username2.value == "") {
            alert("用户姓名不得为空");
            return;
        }
        if (confirm("确定要新增用户?")) {
            document.resetpwd.submit();
        }
    }
</script>

