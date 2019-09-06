$(document).ready(function() {

	$("#pass").attr("type", "password");
	$("#name").attr("placeholder", "Please enter your username");
	$("#pass").attr("placeholder", "Please enter your password");
	/** ***************************************************************** */
	$("#btn").click(function() {
		var pass = $("#pass").val(); // 获取输入密码
		if ($("#name").val() == "") {
			$("#tips").text("*Please enter your username!");
			$("#name").css("border-color", "#f00");
			return false;
		}
		if ($("#pass").val() == "") {
			$("#tips").text("*Please enter your password!");
			$("#pass").css("border-color", "#f00");
			return false;
		}
		$(this).attr("disable", true);
		$(this).css("background-color", "#ccc");
		$(this).val("Sign In...");
		$("#form1").submit();
	});

	/** 输入框获取焦点事件 */
	$("#name, #pass").bind("input", function() {
		$(this).css("border-color", "#00953E");
		$("#tips").text("");
	});
	/** 键盘按下事件 */
	$("#name, #pass").keydown(function() {
		if (event.keyCode == 13)
			$("#btn").click();
	});

});