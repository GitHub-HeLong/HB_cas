<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<title></title>

<!--<spring:theme code="standard.custom.css.file" var="customCssFile" />-->
<link rel="stylesheet" href="<c:url value='${customCssFile}' />" />
</head>
<body>
<div class="background" style="width:inherit;height:inherit;position:relative;">

    <!--<a style="display: none;" href="${plugin.downUrl}" id="downfile"></a>-->
    <!--<div class="login-png"></div>-->
	<div class="login-title-new">
		<a></a>
	</div>
	<div class="login-background">
		<div class="login-box">
		<div class="box" id="login">
	  		<form:form method="post" id="fm1" name="fm1" commandName="${commandName}" htmlEscape="true">
	    	<form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" />
	    	<%-- <h2><spring:message code="screen.welcome.instructions" /></h2> --%>
    <section class="row">
      <!--<label for="username">用户名</label>-->
      <c:choose>
        <c:when test="${not empty  sessionScope.openIdLocalId}">
          <strong>${sessionScope.openIdLocalId}</strong>
          <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
        </c:when>
        <c:otherwise>
          <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
          <form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" placeholder="用户名" />
        </c:otherwise>
      </c:choose>
    </section>
    
    <section class="row">
      <!--<label for="password">密码</label>-->
      <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
      <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off"  placeholder="密码" />
    </section>
    
    <%-- <section class="row check">
      <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
      <label for="warn" class="warm-text">转向其他站点前提示我</label>
    </section> --%>
    
    <section class="row btn-row">
      <input type="hidden" name="lt" value="${loginTicket}" />
      <input type="hidden" name="execution" value="${flowExecutionKey}" />
      <input type="hidden" name="_eventId" value="submit" />

      <input class="btn" name="submit" accesskey="l" value="登录" tabindex="4" type="submit" />
                <!-- <input class="btn" name="reset" accesskey="c" onclick="document.getElementById('username').value='';document.getElementById('password').value=''" value="重置" tabindex="5" type="button"/> -->
    </section>
  </form:form>
</div>
	</div>
    <!--<div id="down">
    <a  style="margin-right:10px;float:right" href="" id="downfile">视频插件安装</a>
    <a  style="margin-right:10px;float:right" href="" id="msvcfile">视频插件环境安装</a>
    <a  style="margin-right:10px;float:right" href="" id="firefile">火狐浏览器安装</a>
    </div>-->
	</div>
</div>
</body>
<script>
    /*if(navigator.userAgent.indexOf("Firefox")>0){
        if(!navigator.plugins.hasOwnProperty("npmedia Dynamic Link Library")){
            var state = confirm("您的电脑没有安装视频插件，是否安装插件？");
            if(state){
            	//var fd = new FormData();
                var xhr = new XMLHttpRequest();
                xhr.open("POST", "/cas/get/plugin");
                //发送
                xhr.onload = function () {
                    if (xhr.status === 200) {
                    	var url = xhr.getResponseHeader('pluginDownUrl');
                    	document.getElementById("downfile").href = url;
                    	document.getElementById("downfile").click();
                    }
                };
                xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
                xhr.send("fhjh=123");
                //document.getElementById("downfile").click();
            }
        }
        else if(navigator.plugins["npmedia Dynamic Link Library"]["version"]){
        	var fd = new FormData();
            var xhr = new XMLHttpRequest();
            
            xhr.open("POST", "/cas/plugin");
            //发送
            xhr.onload = function () {
                if (xhr.status === 200) {
                	var version = xhr.getResponseHeader('version');
                	if(version !== "latest version"){
                		var state = confirm("视频插件已更新，是否升级插件？");
                        if(state){
                        	var url = xhr.getResponseHeader('pluginDownUrl');
                        	var url1 = unescape(url);
                        	document.getElementById("downfile").href = url;
                            document.getElementById("downfile").click();
                        }
                	}
                } 
            };
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");//
            xhr.send("version="+navigator.plugins["npmedia Dynamic Link Library"]["version"]);
        }
    }*/
  /*  var xhr = new XMLHttpRequest();
    xhr.open("POST", "/cas/pluginUrl");
    xhr.onload = function () {
    if (xhr.status === 200) {
    var url = xhr.getResponseHeader('pluginDownUrl');
    document.getElementById("downfile").href = url;
    var fireurl = xhr.getResponseHeader('firefoxDownUrl');
    document.getElementById("firefile").href = fireurl;
    var msvcurl = xhr.getResponseHeader('msvcDownUrl');
    document.getElementById("msvcfile").href = msvcurl;
    }
    };
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xhr.send("fhjh=123");  */
</script>
</html>