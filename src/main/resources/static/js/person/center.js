$(document).ready(function() {
    var code = null;
    var language = $("#rightTop").text() == "個人中心" ? "zh" : "en";
    /********************************************************************/
    $(".link").click(function(){
    	var name = $(".nameBox").val();
    	if (name != "" && Ajax("updatename", {name: name})) {
    		if (language == "zh")
            	showTips("名称修改成功！");
            else
            	showTips("Operating successfully!");
            setTimeout("location.reload()", 2000);	
    	}
    });
    //获取使用期限
    var idate = $(".table:eq(0) tr:eq(3) td:eq(1)").text();
    var count = $(".table:eq(0) tr:eq(3) td:eq(3)").text();
    $(".table:eq(0) tr:eq(3) td:eq(3)").text(getDate(idate, count));
    /********************************************************************/
    $("#link1").click(function() {
        $("#page").show();
        $("#pageTab1 .textbox").val("");
        $("#pageTab1 a:eq(0)").text("");
        $("#webform1").show();
        $("#webform2").hide();
    });
    $("#link2").click(function() {
        $("#page").show();
        $("#pageTab2 .textbox").val("");
        $("#pageTab2 a:eq(0)").text("");
        $("#webform1").hide();
        $("#webform2").show();
    });
    $("#link3").click(function() {
        showTips("系统维护中...");
    });
    /********************************************************************/
    $("#pageTab1 .textbox").focus(function() {
        $(this).css("border-color", "#ccc");
        $("#pageTab1 a:eq(0)").text("");
    });
    $("#pageTab2 .textbox").focus(function() {
        $(this).css("border-color", "#ccc");
        $("#pageTab2 a:eq(0)").text("");
    });
    /********************************************************************/
    $(".closebtn").click(function() {
    	$("#pageTab1 .textbox").css("border-color", "#CCC");
    	$("#pageTab2 .textbox").css("border-color", "#CCC");
        $("#page").hide();
    });
    /********************************************************************/
    /** 修改密码 */
    $(".btn:eq(0)").click(function() {
        var iname = $("#pageTab1 input:eq(0)").val();
        var pass1 = $("#pageTab1 input:eq(1)").val();
        var pass2 = $("#pageTab1 input:eq(2)").val();
        if (iname.length < 6 || iname.length > 16) {
        	if (language == "zh")
        		$("#pageTab1 a:eq(0)").text("*密碼格式不正確,請重新輸入！");
        	else
        		$("#pageTab1 a:eq(0)").text("*Please check the input password!");
            $("#pageTab1 input:eq(0)").css("border-color", "#f00");
            return;
        }
        if (pass1.length < 6 || pass1.length > 16) {
        	if (language == "zh")
        		$("#pageTab1 a:eq(0)").text("*密碼格式不正確,請重新輸入！");
        	else
        		$("#pageTab1 a:eq(0)").text("*Please check the input password!");
            $("#pageTab1 input:eq(1)").css("border-color", "#f00");
            return;
        }
        if (pass1 != pass2) {
            if (language == "zh")
        		$("#pageTab1 a:eq(0)").text("*兩次密碼不壹致,請重新輸入！");
        	else
        		$("#pageTab1 a:eq(0)").text("*Please check the input password");
            $("#pageTab1 input:eq(1)").css("border-color", "#f00");
            $("#pageTab1 input:eq(2)").css("border-color", "#f00");
            return;
        }
        
        if (Ajax("updatepass", {name: iname, pass: pass1})) {
            if (language == "zh")
            	showTips("密碼修改成功！");
            else
            	showTips("Operating successfully!");
            setTimeout("location.reload()", 2000);
        }
    });
    /** 修改邮箱 */
    $(".btn:eq(1)").click(function() {
        if (checkMail() && checkCode()) {
        	$(this).attr("disabled", true);
            $(this).css("border-color", "#CCC");
            var mail = $("#pageTab2 input[type=text]:eq(0)").val();
            var code = $("#pageTab2 input[type=text]:eq(1)").val();
            if (Ajax("updatemail", {mail: mail, code: code})) {
            	if (language == "zh")
            		showTips("郵箱修改成功！");
            	else
                	showTips("Operating successfully!");
                setTimeout("location.reload()", 2000);
            }
        }
    });
    /********************************************************************/
    function checkMail() {
        /** 检测邮箱 */
        var mail = $("#pageTab2 input[type=text]:eq(0)").val();
        if (!mail.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
            if (language == "zh")
            	$("#pageTab2 a:eq(0)").text("*電子郵箱格式不正確!");
            else
            	$("#pageTab2 a:eq(0)").text("*Please check the input email");
            $("#pageTab2 input[type=text]:eq(0)").css("border-color", "#f00");
            return false;
        }
        if (Ajax("/CCTV/user/isexistmail", {mail: mail})) {
        	if (language == "zh")
        		$("#pageTab2 a:eq(0)").text("*電子郵箱已經被使用!");
        	else
        		$("#pageTab2 a:eq(0)").text("*E-mail has been used!");
            $("#pageTab2 input[type=text]:eq(0)").css("border-color", "#f00");
            return false;
        }
        return true;
    }
    /** 检测验证码 */
    function checkCode() {
        var temp = $("#pageTab2 input[type=text]:eq(1)").val();
        if (temp.length == 0 || temp != code) {
        	if (language == "zh")
        		$("#pageTab2 a:eq(0)").text("*請輸入正確的驗證碼！");
        	else
        		$("#pageTab2 a:eq(0)").text("*Please check the input code!");
            $("#pageTab2 input[type=text]:eq(1)").css("border-color", "#f00");
            return false;
        }
        return true;
    }
    /********************************************************************/
    var time = 60;
    $("#getCode").click(function() {
        /** 获取验证码 */
        if (checkMail()) {
            time = 60;
            $(this).attr("disabled", true);
            $(this).css("border-color", "#CCC");
            $(this).css("color", "#CCC");
            setTimeout(ChangeTime, 1000);
            $("#pageTab2 input[type]:eq(1)").focus();
            var mail = $("#pageTab2 input[type]:eq(0)").val();
            code = Ajax("/CCTV/user/sendmail", {"mail": mail});
        }
    });
    var value = $("#getCode").val();
    function ChangeTime() {
        if (--time == 0) {
        	$("#getCode").css("color", "#51C024");
            $("#getCode").css("border-color", "#51C024");
            $("#getCode").attr("disabled", false);
            $("#getCode").attr("value", value);
        } else {
            $("#getCode").attr("value", time + " S後重新獲取");
            setTimeout(ChangeTime, 1000);
        }
    }
    /********************************************************************/
    function getDate(idate, count) {
        var date = new Date(idate);
        date.setDate(date.getDate() + Number(count));
        var y = date.getFullYear();
        var m = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1);
        var d = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return y + "-" + m + "-" + d;
    }
    /********************************************************************/
    /** 显示提示信息 */
    function showTips(text) {
        $("#Tip").show().delay(1800).hide(200);
        $("#Tip").text(text);
    }
    /** 执行AJAX操作 */
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url:url,
            data:data,
            type:"post",
            async:false,
            datatype:"json",
            success:function(data) {
                result = data;
            }
        });
        return result;
    }
});