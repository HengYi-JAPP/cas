<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.hengyi.cas.server.User" %>
<%
    String employeeid = request.getParameter("uid");
    String oldpassword = request.getParameter("oldpassword");
    String newpassword = request.getParameter("newpassword");
    if (employeeid != null & oldpassword != null & newpassword != null) {
        if (!employeeid.equals("") & !oldpassword.equals("") & !newpassword.equals("")) {
            if (User.change(employeeid, oldpassword, newpassword)) {
                response.sendRedirect("./casChangeSuccess.jsp");
                return;
            } else {
                oldpassword = "";
                newpassword = "";
            }
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>用户密码修改</title>
    <link href="/themes/hengyi/images/max.css" rel="stylesheet" type="text/css">
    <!-- slide -->
    <link href="/themes/hengyi/images/slide.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/themes/hengyi/images/jquery1.42.min.js"></script>
    <script type="text/javascript" src="/themes/hengyi/images/jquery.easing.1.3.js"></script>
    <!--****设置IE6下png背景透明****-->
    <!--[if IE 6]>
    <script type="text/javascript" src="/themes/hengyi/images/DD_belatedPNG.js"></script>
    <script type="text/javascript">
        DD_belatedPNG.fix('*');
    </script>
    <![endif]-->
    <script type="text/javascript" src="/themes/hengyi/images/max.js"></script>
</head>
<body>

<div class="external-wrap">

    <div class="header">
        <div class="logo">
            <div class="l-img"><img src="/themes/hengyi/images/logo.png" alt=""></div>
            <div class="l-text">
                <p class="t-name">恒逸信息网</p>
                <p class="t-website">cas.hengyi.com</p>
            </div>
        </div>
    </div>

    <!-- content -->

    <div class="login-wrap">
        <!-- slide -->
        <div class="prospect-wrap">
            <div class="prospect">
            </div>
            <div id="slide">
                <!-- slide content -->
                <div id="focusBar">
                    <a href="javascript:void(0)" class="arrL" onclick="prePage()">&nbsp;</a>
                    <a href="javascript:void(0)" class="arrR" onclick="nextPage()">&nbsp;</a>
                    <ul class="mypng">
                        <li id="focusIndex1" style=" display:block;"><!--此处需判断li的display:block-->
                            <div class="focusL"><img src="/themes/hengyi/images/ban1.png"/></div>
                            <div class="focusR"><img src="/themes/hengyi/images/t1.png"/></div>
                        </li>
                        <li id="focusIndex2" style=" display:none;">
                            <div class="focusL"><img src="/themes/hengyi/images/ban2.png"/></div>
                            <div class="focusR"><img src="/themes/hengyi/images/t2.png"/></div>
                        </li>
                        <li id="focusIndex3" style="display:none;">
                            <div class="focusL"><img src="/themes/hengyi/images/ban3.png"/></div>
                            <div class="focusR"><img src="/themes/hengyi/images/t3.png"/></div>
                        </li>
                    </ul>
                </div>

                <script type="text/javascript" src="/themes/hengyi/images/script.js"></script>
                <!-- slide content end -->
            </div>

        </div>
        <!-- slide end -->

        <!-- login -->
        <div class="l-window">
            <div class="Interface">
                <div class="title">
                    <i></i> 修改密码
                </div>

                <form onsubmit="return submitForm();" action="./casChangePassword.jsp" method="post" name="change">
                    <input type="hidden" name="ua" id="UA_InputId" value="">
                    <input type="hidden" name="uid" readonly value="<%=employeeid%>"/>

                    <p class="frame">
                        <input type="password" name="oldpassword" class="key max-password" placeholder="旧密码">
                    </p>

                    <p class="frame">
                        <input type="password" name="newpassword" class="key max-password" placeholder="新密码">
                    </p>

                    <p class="frame">
                        <input type="password" name="confirmpassword" class="key max-password" placeholder="新密码确认">
                    </p>

                    <p class="frame login-entry">
                        <button type="submit" class="btn login-btn">
                            <span>确定</span>
                        </button>
                    </p>
                </form>
            </div>
        </div>
        <!-- login end -->
    </div>

    <!-- copyright -->
    <div class="footer">
        <div class="copyright">
            <div class="text">
                2012-2014 @浙江恒逸集团有限公司 版权所有 浙江省杭州市萧山区市心北路260号恒逸南岸明珠 <br/>
                <span>Tel: (0086) 0571-82795888	</span> <span>Fax: (0086) 0571-82797666 </span>
            </div>
            <div class="pir">
                <img src="/themes/hengyi/images/i1.png" alt="">
                <img src="/themes/hengyi/images/i2.png" alt="">
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    function submitForm() {
        if (document.change.oldpassword.value === "") {
            alert("旧密码为空");
            return false;
        }
        if (document.change.newpassword.value === "") {
            alert("新密码不得为空");
            return false;
        }
        if (document.change.newpassword.value === document.change.oldpassword.value) {
            alert("新密码与旧密码相同");
            return false;
        }
        if (document.change.newpassword.value !== document.change.confirmpassword.value) {
            alert("新密码确认有误");
            return false;
        }
        return true;
    }
</script>
</body>
</html>
