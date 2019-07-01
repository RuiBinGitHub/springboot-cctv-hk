$(document).ready(function() {
    var code = null;
    var language = $("#mainTop").text() == "重設密碼" ? "zh" : "en";
    /********************************************************************/
    $("#logo img").attr("title", "用户登录");
    /********************************************************************/
    //输入框显示提示信息
    if (language == "zh") {
	    $("#tab1 input[type=text]:eq(0)").attr("placeholder", "請輸入登錄賬號");
	    $("#tab1 input[type=text]:eq(1)").attr("placeholder", "請輸入電子郵箱");
	    $("#tab1 input[type=text]:eq(2)").attr("placeholder", "請輸入驗證碼");
	    $("#tab1 input[type=password]:eq(0)").attr("placeholder", "請輸入您的新密碼");
	    $("#tab1 input[type=password]:eq(1)").attr("placeholder", "請確認您的新密碼");
    } else {
    	$("#tab1 input[type=text]:eq(0)").attr("placeholder", "Please enter your UserName");
	    $("#tab1 input[type=text]:eq(1)").attr("placeholder", "Please enter your E-Mail");
	    $("#tab1 input[type=text]:eq(2)").attr("placeholder", "Enter Code");
	    $("#tab1 input[type=password]:eq(0)").attr("placeholder", "Enter your new password");
	    $("#tab1 input[type=password]:eq(1)").attr("placeholder", "Confirm your new password");
    }
    /********************************************************************/
    $(".combtn").click(function() {
        if (!checkUserName() || !checkPassWord())
            return false;
        if (!checkMail() || !checkCode())
            return false;
        $("#form1").submit();
    });

    /** 检测账号 */
    function checkUserName() {
        var username = $("#tab1 input[type=text]:eq(0)").val();
        if (username.length < 6 || username.length > 16) {
            $("#tab1 input[type=text]:eq(0)").css("border-color", "#f00");
            if (language == "zh")
                $("#tips").text("*用戶名稱格式不正確！");
            else
                $("#tips").text("*Please check the input nickname");
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
            if (language == "zh")
                $("#tips").text("*登錄密碼格式不正確！");
            else
                $("#tips").text("*Please check the input username");
            return false;
        }
        if (password1 != password2) {
            $("#tab1 input[type=password]:eq(0)").css("border-color", "#f00");
            $("#tab1 input[type=password]:eq(1)").css("border-color", "#f00");
            if (language == "zh")
                $("#tips").text("*兩次密碼輸入不壹致");
            else
                $("#tips").text("*Two password inconsistencies");
            return false;
        }
        return true;
    }

    /** 检测邮箱 */
    function checkMail() {
        if (!checkUserName())
            return false
        var mail = $("#tab1 input[type=text]:eq(1)").val();
        if (mail == "" || !mail.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
            $("#tab1 input[type=text]:eq(1)").css("border-color", "#f00");
            if (language == "zh")
                $("#tips").text("*电子郵箱格式不正確！");
            else
                $("#tips").text("*Please check the input E-Mail!");
            return false;
        }
        var username = $("#tab1 input[type=text]:eq(0)").val();
        if (!Ajax("checknamemail", {
            username: username,
            mail: mail
        })) {
            $("#tab1 input[type=text]:eq(1)").css("border-color", "#f00");
            if (language == "zh")
                $("#tips").text("*登錄賬號與郵箱不匹配！");
            else
                $("#tips").text("*The UserName does not match the E-Mail!!");
            return false;
        }
        return true;
    }
    /** 检测验证码 */
    function checkCode() {
        var temp = $("#tab1 input[type=text]:eq(2)").val();
        if (temp == "" || temp != code) {
            $("#tab1 input[type=text]:eq(2)").css("border-color", "#f00");
            if (language == "zh")
                $("#tips").text("*驗證碼輸入不正確！");
            else
                $("#tips").text("*Please check the input verification code!");
            return false;
        }
        return true;
    }

    var time = 0;
    $("#getBtn").click(function() {
        if (!checkMail())
            return false;
        var mail = $("#tab1 input[type=text]:eq(1)").val();
        if ((code = Ajax("sendmail", {"mail": mail})) == "") {
            $("#tab1 input[type=text]:eq(1)").css("border-color", "#f00");
            $("#tips").text("*电子邮箱不正确!");
            return false;
        }
        time = 60;
        $(this).attr("disabled", true);
        $("#getBtn").css("color", "#CCCCCC");
        changeTime();
    });

    var btnText = "獲取驗證碼";
    if (language == "en")
        btnText = "Get Code";
    function changeTime() {
        if (time-- == 0) {
            $("#getBtn").attr("value", btnText);
            $("#getBtn").attr("disabled", false);
            $("#getBtn").css("color", "#28B779");
        } else {
            $("#getBtn").attr("value", time + " S後重新獲取");
            setTimeout(changeTime, 1000);
        }
    }
    $("#tab1 input[type=text],#tab1 input[type=password]").focus(function() {
        $(this).css("border-color", "#999");
        $("#tips").text("");
    });
    /** 输入框输入改变事件 */
    $("#tab1 input[type=text],#tab1 input[type=password]").bind("input", function() {
        $(this).css("border-color", "#999");
        $("#tips").text("");
    });
    /** 执行AJAX操作 */
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url:url,
            data:data,
            type:"post",
            async:false,
            dataType:"json",
            success:function(data) {
                result = data;
            }
        });
        return result;
    }
});
