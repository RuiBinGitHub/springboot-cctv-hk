$(document).ready(function() {
	
    var code = null;
    $("#icon").attr("title", "用户登录");
    $("#tab1 input[type=password]:eq(0)").attr("name", "password");
    /********************************************************************/
    //输入框显示提示信息
    $("#tab1 input[type=text]:eq(0)").attr("placeholder", "Please enter your UserName");
    $("#tab1 input[type=text]:eq(1)").attr("placeholder", "Please enter your E-Mail");
    $("#tab1 input[type=text]:eq(2)").attr("placeholder", "Please enter your Code");
    $("#tab1 input[type=password]:eq(0)").attr("placeholder", "Please enter your new Password");
    $("#tab1 input[type=password]:eq(1)").attr("placeholder", "Please confirm your Password");
    /********************************************************************/
    $("#btn2").click(function() {
        if (!checkUserName() || !checkPassWord())
            return false;
        if (!checkMail() || !checkCode())
            return false;
        $("#form1").submit();
    });
    /********************************************************************/
    /** 检测账号 */
    function checkUserName() {
        var username = $("#tab1 .textbox:eq(0)").val();
        if (username.length < 6 || username.length > 16) {
            $("#tab1 .textbox:eq(0)").css("border-color", "#f00");
            $("#tips").text("*Please check the input username!");
            return false;
        }
        return true;
    }

    /** 检测密码 */
    function checkPassWord() {
        var password1 = $("#tab1 input[type=password]:eq(0)").val();
        var password2 = $("#tab1 input[type=password]:eq(1)").val();
        if (password1.length < 6 || password1.length > 16) {
            $("#tab1 input[type=password]:eq(0)").css("border-color", "#f00");
            $("#tips").text("*Please check the input password!");
            return false;
        }
        if (password1 != password2) {
            $("#tab1 input[type=password]:eq(0)").css("border-color", "#f00");
            $("#tab1 input[type=password]:eq(1)").css("border-color", "#f00");
            $("#tips").text("*Two inconsistent password input!");
            return false;
        }
        return true;
    }

    /** 检测邮箱 */
    function checkMail() {
        if (!checkUserName())
            return false;
        var mail = $("#tab1 .textbox:eq(1)").val();
        var expr = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
        if (mail == "" || !mail.match(expr)) {
            $("#tab1 .textbox:eq(1)").css("border-color", "#f00");
            $("#tips").text("*Please check the input E-Mail!");
            return false;
        }
        var name = $("#tab1 .textbox:eq(0)").val();
        if (!Ajax("checknamemail", {username: name, mail: mail})) {
            $("#tab1 .textbox:eq(1)").css("border-color", "#f00");
            $("#tips").text("*The UserName doesn't match the E-Mail!");
            return false;
        }
        return true;
    }

    /** 检测验证码 */
    function checkCode() {
        var temp = $("#tab1 .textbox:eq(2)").val();
        if (temp == "" || temp != code) {
            $("#tab1 .textbox:eq(2)").css("border-color", "#f00");
            $("#tips").text("*Please check the input code!");
            return false;
        }
        return true;
    }

    var time = 0;
    $("#btn1").click(function() {
        if (!checkMail())
            return false;
        var mail = $("#tab1 .textbox:eq(1)").val();
        if ((code = Ajax("sendmail", {"mail": mail})) == "") {
            $("#tab1 .textbox:eq(1)").css("border-color", "#f00");
            $("#tips").text("*Please check the input E-Mail!");
            return false;
        }
        time = 60;
        $(this).attr("disabled", true);
        $(this).css("color", "#CCCCCC");
        changeTime();
    });

    function changeTime() {
        if (time-- == 0) {
            $("#btn1").attr("value", "Get Code");
            $("#btn1").attr("disabled", false);
            $("#btn1").css("color", "#00953E");
        } else {
            $("#btn1").attr("value", time + " second");
            setTimeout(changeTime, 1000);
        }
    }
    $("#tab1 .textbox").bind("input", function() {
        $(this).css("border-color", "#00953E");
        $("#tips").text("");
    });
    
    /** 执行AJAX操作 */
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url: url,
            data: data,
            type: "post",
            async: false,
            dataType: "json",
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
});
