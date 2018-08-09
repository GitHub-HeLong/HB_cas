    <!DOCTYPE html>
        <%@ page pageEncoding="UTF-8" %>
        <%@ page contentType="text/html; charset=UTF-8" %>
        <style>
        html{
        height:97%;
        width: 97%;
        background: #FFFFFF;
        }
        body{
        position:relative;
        height:100%;
        -moz-user-select: none;
        -ms-user-select: none;
        width: 100%;
        }
        body >label{
        position: absolute;
        width: 221px;
        height: 165px;
        margin: 0 auto;
        background: url("images/AuthorizationFailure.png") no-repeat 50px 0;
        padding-top: 135px;
        display: none;
        text-align: center;
        font-family: 'Microsoft Yahei', Arial, Simsun, sans-serif;
        font-size: 36px;
        color: #c8c8c8;
        }
        body >a{
        position: absolute;
        width: 221px;
        height: 50px;
        border-radius: 5px;
        text-align: center;
        font-family: 'Microsoft Yahei', Arial, Simsun, sans-serif;
        font-size: 20px;
        line-height: 50px;
        background-color:#067ECE !important; ;
        color: #FFFFFF;
        display:none;
        }
        body >a:hover{
        cursor: pointer;
        }
        </style>
        <head>
        <meta charset="UTF-8">
        <title></title>
        </head>
        <body onload="ready()">
        <label id="label_icon">无访问权限</label>
        <a id="a_btn">重新登陆</a>
        </body>
        <script>
        window.onresize=function(){
        resize();
        }
        function ready(){
        resize();
        setUrl();
        }
        function resize(){
        var clientHeight = document.body.clientHeight;
        var clientWidth = document.body.clientWidth;
        var top = (clientHeight - 300)/2;
        var left = (clientWidth - 221)/2;
        document.getElementById("label_icon").style.top = top+"px";
        document.getElementById("label_icon").style.left = left+"px";
        document.getElementById("label_icon").style.display = "block"
        var top = clientHeight / 2 + 100;
        document.getElementById("a_btn").style.top = top+"px";
        document.getElementById("a_btn").style.left = left+"px";
        document.getElementById("a_btn").style.display = "block"
        }

        function setUrl(){
        var url = getUrlParam("logoutUrl");
        document.getElementById("a_btn").href = url;
        }

        function getUrlParam(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null) return  unescape(r[2]); return null;
        }
        </script>
        </html>