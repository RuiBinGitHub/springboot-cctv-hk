$(document).ready(function() {
	var code = null;
	var language = $("#mainTop").text() == "用戶註冊" ? "zh" : "en";
	/********************************************************************/
    $("#logo img").attr("title", "用户登录");
    /********************************************************************/
    if (language == "zh") {
	    $("#tab1 input[type=text]:eq(0)").attr("placeholder", "用戶昵稱，4-10位");
	    $("#tab1 input[type=text]:eq(1)").attr("placeholder", "6-16位，由數字和字母組成");
	    $("#tab1 input[type=password]:eq(0)").attr("placeholder", "6-16位，由數字和字母組成");
	    $("#tab1 input[type=password]:eq(1)").attr("placeholder", "6-16位，由數字和字母組成");
	    $("#tab1 input[type=text]:eq(2)").attr("placeholder", "輸入公司名稱");
	    $("#tab1 input[type=text]:eq(3)").attr("placeholder", "20位驗證號碼");
	    $("#tab1 input[type=text]:eq(4)").attr("placeholder", "輸入電子郵箱");
	    $("#tab1 input[type=text]:eq(5)").attr("placeholder", "請輸入驗證碼");
    } else {
    	$("#tab1 input[type=text]:eq(0)").attr("placeholder", "Nick Name, 4-24 digits");
	    $("#tab1 input[type=text]:eq(1)").attr("placeholder", "Username，6-16 digits");
	    $("#tab1 input[type=password]:eq(0)").attr("placeholder", "Password，6-16 digits");
	    $("#tab1 input[type=password]:eq(1)").attr("placeholder", "Password，6-16 digits");
	    $("#tab1 input[type=text]:eq(2)").attr("placeholder", "Enter Company Name");
	    $("#tab1 input[type=text]:eq(3)").attr("placeholder", "Enter Serial Number");
	    $("#tab1 input[type=text]:eq(4)").attr("placeholder", "Enter your E-Mail");
	    $("#tab1 input[type=text]:eq(5)").attr("placeholder", "Enter your code");
    }
    /********************************************************************/
    $(".combtn").click(function() {
        if (!checkNickName())
            return false;
        if (!checkUserName() || !checkPassWord())
            return false;
        if (!checkCompany())
            return false;
        if (!checkMail() || !checkCode())
            return false;
        $("#form1").submit();
    });

    /** 检测昵称 */
    function checkNickName() {
        var nickname = $("#tab1 input[type=text]:eq(0)").val();
        if (nickname.length < 4 || nickname.length > 10) {
            $("#tab1 input[type=text]:eq(0)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*用戶名稱格式不正確！");
            else
            	$("#tips").text("*Please check the input nickname");
            return false;
        }
        return true;
    }
    /** 检测账号 */
    function checkUserName() {
        var username = $("#tab1 input[type=text]:eq(1)").val();
        if (username.length < 6 || username.length > 16) {
            $("#tab1 input[type=text]:eq(1)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*登錄賬號格式不正確！");
            else
            	$("#tips").text("*Please check the input username");
            return false;
        }
        if (Ajax("isexistname", {name: username})) {
            $("#tab1 input[type=text]:eq(1)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*賬號已存在，請重新輸入！");
            else
            	$("#tips").text("*User name already exists!");
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
            	$("#tips").text("*Please check the input password");
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

    /** 检查公司 */
    function checkCompany() {
        if ($("#tab1 input[type=text]:eq(2)").val() == "") {
            $("#tab1 input[type=text]:eq(2)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*請輸入公司名稱！");
            else
            	$("#tips").text("*Please enter the company name!");
            return false;
        }
        if ($("#tab1 input[type=text]:eq(3)").val() == "") {
            $("#tab1 input[type=text]:eq(3)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*請輸入公司序列號！");
            else
            	$("#tips").text("*Please enter the company serial number!");
            return false;
        }
        return true;
    }

    /** 检测邮箱 */
    function checkMail() {
        var mail = $("#tab1 input[type=text]:eq(4)").val();
        if (!mail.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
            $("#tab1 input[type=text]:eq(4)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*电子郵箱格式不正確！");
            else
            	$("#tips").text("*Please check the input E-Mail!");
            return false;
        }
        if (Ajax("isexistmail", {mail: mail})) {
            $("#tab1 input[type=text]:eq(4)").css("border-color", "#f00");
            if (language == "zh")
            	$("#tips").text("*郵箱已被註冊,請重新輸入");
            else
            	$("#tips").text("*E-mail has been used!");
            return false;
        }
        return true;
    }
    /** 检测验证码 */
    function checkCode() {
        var temp = $("#tab1 input[type=text]:eq(5)").val();
        if (temp == "" || temp != code) {
            $("#tab1 input[type=text]:eq(5)").css("border-color", "#f00");
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
        var mail = $("#tab1 input[type=text]:eq(4)").val();
        var data = Ajax("sendmail", {"mail": mail});
        if (data == ""){
        	$("#tab1 input[type=text]:eq(4)").css("border-color", "#f00");
        	if (language == "zh")
            	$("#tips").text("*电子郵箱格式不正確！");
            else
            	$("#tips").text("*Please check the input E-Mail!");
        	return false;
        }
        time = 60;
        code = data;
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

    /** 输入框获取焦点事件 */
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
